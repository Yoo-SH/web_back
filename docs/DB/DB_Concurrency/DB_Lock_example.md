# Spring DB Lock을 활용한 이중 예약 방지 시스템

이 프로젝트는 Spring과 데이터베이스 락을 사용하여 동시성 문제, 특히 밀리초 단위로 발생할 수 있는 이중 예약 문제를 해결하는 방법을 보여줍니다.

## 개요

실시간 예약 시스템에서는 여러 사용자가 동시에 같은 리소스(예: 좌석, 객실 등)를 예약하려고 시도할 수 있습니다. 이런 경우 데이터베이스 트랜잭션만으로는 충분한 보호가 되지 않을 수 있으며, 적절한 락(Lock) 메커니즘이 필요합니다.

## 해결 방법

이 프로젝트에서는 다음 두 가지 방식의 DB 락을 활용합니다:

1. **비관적 락(Pessimistic Lock)**: 트랜잭션 내에서 특정 레코드에 대한 독점적인 접근을 보장
2. **낙관적 락(Optimistic Lock)**: 버전 관리를 통해 데이터 변경 충돌을 감지

## 프로젝트 구조

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── reservation/
│   │               ├── ReservationApplication.java
│   │               ├── controller/
│   │               │   └── ReservationController.java
│   │               ├── dto/
│   │               │   └── ReservationRequest.java
│   │               ├── entity/
│   │               │   ├── Reservation.java
│   │               │   └── Resource.java
│   │               ├── exception/
│   │               │   ├── AlreadyReservedException.java
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── repository/
│   │               │   ├── ReservationRepository.java
│   │               │   └── ResourceRepository.java
│   │               └── service/
│   │                   └── ReservationService.java
│   └── resources/
│       ├── application.properties
│       └── schema.sql
└── test/
    └── java/
        └── com/
            └── example/
                └── reservation/
                    └── service/
                        └── ReservationServiceTest.java
```

## 코드 예제

### 엔티티 클래스

#### Resource.java (자원 엔티티)

```java
package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resources")
@Getter
@Setter
public class Resource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private boolean available;
    
    @Version
    private Long version; // 낙관적 락을 위한 버전 필드
}
```

#### Reservation.java (예약 엔티티)

```java
package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;
    
    private String userId;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
```

### 리포지토리

#### ResourceRepository.java

```java
package com.example.reservation.repository;

import com.example.reservation.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    // 비관적 락(Pessimistic Lock)을 사용하여 리소스 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Resource r WHERE r.id = :id")
    Optional<Resource> findByIdWithPessimisticLock(@Param("id") Long id);
    
    // 낙관적 락(Optimistic Lock)을 사용하여 리소스 조회
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT r FROM Resource r WHERE r.id = :id")
    Optional<Resource> findByIdWithOptimisticLock(@Param("id") Long id);
}
```

#### ReservationRepository.java

```java
package com.example.reservation.repository;

import com.example.reservation.entity.Reservation;
import com.example.reservation.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // 특정 리소스에 대해 시간이 겹치는 예약이 있는지 확인
    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
           "WHERE r.resource.id = :resourceId " +
           "AND ((r.startTime <= :endTime) AND (r.endTime >= :startTime))")
    boolean existsOverlappingReservation(
            @Param("resourceId") Long resourceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    List<Reservation> findByResource(Resource resource);
}
```

### 서비스 클래스

```java
package com.example.reservation.service;

import com.example.reservation.dto.ReservationRequest;
import com.example.reservation.entity.Reservation;
import com.example.reservation.entity.Resource;
import com.example.reservation.exception.AlreadyReservedException;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.ResourceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;

    public ReservationService(ReservationRepository reservationRepository, ResourceRepository resourceRepository) {
        this.reservationRepository = reservationRepository;
        this.resourceRepository = resourceRepository;
    }

    /**
     * 비관적 락을 사용한 예약 처리
     */
    @Transactional
    public Reservation reserveWithPessimisticLock(ReservationRequest request) {
        // 비관적 락으로 리소스 조회 (다른 트랜잭션이 해당 리소스를 수정하지 못하도록 락 획득)
        Resource resource = resourceRepository.findByIdWithPessimisticLock(request.getResourceId())
                .orElseThrow(() -> new EntityNotFoundException("Resource not found with id: " + request.getResourceId()));

        // 가용성 확인
        if (!resource.isAvailable()) {
            throw new AlreadyReservedException("Resource is not available");
        }

        // 겹치는 예약이 있는지 확인
        boolean hasOverlap = reservationRepository.existsOverlappingReservation(
                resource.getId(), request.getStartTime(), request.getEndTime());

        if (hasOverlap) {
            throw new AlreadyReservedException("Time slot already reserved");
        }

        // 리소스 상태 업데이트
        resource.setAvailable(false);
        resourceRepository.save(resource);

        // 예약 생성 및 저장
        Reservation reservation = new Reservation();
        reservation.setResource(resource);
        reservation.setUserId(request.getUserId());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());

        return reservationRepository.save(reservation);
    }

    /**
     * 낙관적 락을 사용한 예약 처리
     */
    @Transactional
    public Reservation reserveWithOptimisticLock(ReservationRequest request) {
        try {
            // 낙관적 락으로 리소스 조회
            Resource resource = resourceRepository.findByIdWithOptimisticLock(request.getResourceId())
                    .orElseThrow(() -> new EntityNotFoundException("Resource not found with id: " + request.getResourceId()));

            // 가용성 확인
            if (!resource.isAvailable()) {
                throw new AlreadyReservedException("Resource is not available");
            }

            // 겹치는 예약이 있는지 확인
            boolean hasOverlap = reservationRepository.existsOverlappingReservation(
                    resource.getId(), request.getStartTime(), request.getEndTime());

            if (hasOverlap) {
                throw new AlreadyReservedException("Time slot already reserved");
            }

            // 리소스 상태 업데이트
            resource.setAvailable(false);
            resourceRepository.save(resource);

            // 예약 생성 및 저장
            Reservation reservation = new Reservation();
            reservation.setResource(resource);
            reservation.setUserId(request.getUserId());
            reservation.setStartTime(request.getStartTime());
            reservation.setEndTime(request.getEndTime());

            return reservationRepository.save(reservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            // 낙관적 락 충돌 발생 - 다른 트랜잭션이 같은 리소스를 수정한 경우
            throw new AlreadyReservedException("Reservation failed: Resource was modified concurrently");
        }
    }

    /**
     * 네임드 락을 이용한 예약 처리
     * MySQL의 GET_LOCK, RELEASE_LOCK 함수를 사용하여 애플리케이션 레벨에서의 동시성 제어
     */
    @Transactional
    public Reservation reserveWithNamedLock(ReservationRequest request) {
        // 여기에 네임드 락 구현
        // MySQL에서는 다음과 같은 네이티브 쿼리를 실행할 수 있습니다:
        // "SELECT GET_LOCK('reservation_lock_" + request.getResourceId() + "', 10)"
        
        try {
            Resource resource = resourceRepository.findById(request.getResourceId())
                    .orElseThrow(() -> new EntityNotFoundException("Resource not found with id: " + request.getResourceId()));

            // 가용성 확인
            if (!resource.isAvailable()) {
                throw new AlreadyReservedException("Resource is not available");
            }

            // 겹치는 예약이 있는지 확인
            boolean hasOverlap = reservationRepository.existsOverlappingReservation(
                    resource.getId(), request.getStartTime(), request.getEndTime());

            if (hasOverlap) {
                throw new AlreadyReservedException("Time slot already reserved");
            }

            // 리소스 상태 업데이트
            resource.setAvailable(false);
            resourceRepository.save(resource);

            // 예약 생성 및 저장
            Reservation reservation = new Reservation();
            reservation.setResource(resource);
            reservation.setUserId(request.getUserId());
            reservation.setStartTime(request.getStartTime());
            reservation.setEndTime(request.getEndTime());

            return reservationRepository.save(reservation);
        } finally {
            // 락 해제
            // "SELECT RELEASE_LOCK('reservation_lock_" + request.getResourceId() + "')"
        }
    }
    
    // 기타 메서드...
}
```

### 컨트롤러

```java
package com.example.reservation.controller;

import com.example.reservation.dto.ReservationRequest;
import com.example.reservation.entity.Reservation;
import com.example.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/pessimistic")
    public ResponseEntity<Reservation> createReservationWithPessimisticLock(
            @RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.reserveWithPessimisticLock(request);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/optimistic")
    public ResponseEntity<Reservation> createReservationWithOptimisticLock(
            @RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.reserveWithOptimisticLock(request);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/named-lock")
    public ResponseEntity<Reservation> createReservationWithNamedLock(
            @RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.reserveWithNamedLock(request);
        return ResponseEntity.ok(reservation);
    }
}
```

### DTO 클래스

```java
package com.example.reservation.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationRequest {
    private Long resourceId;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
```

### 예외 처리 클래스

```java
package com.example.reservation.exception;

public class AlreadyReservedException extends RuntimeException {
    public AlreadyReservedException(String message) {
        super(message);
    }
}
```

```java
package com.example.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyReservedException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyReservedException(AlreadyReservedException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Reservation conflict");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Not found");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
```

### 테스트 코드

```java
package com.example.reservation.service;

import com.example.reservation.dto.ReservationRequest;
import com.example.reservation.entity.Reservation;
import com.example.reservation.entity.Resource;
import com.example.reservation.exception.AlreadyReservedException;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Resource testResource;

    @BeforeEach
    void setUp() {
        // 테스트용 리소스 생성
        testResource = new Resource();
        testResource.setName("Test Resource");
        testResource.setAvailable(true);
        testResource = resourceRepository.save(testResource);
    }

    @Test
    void testConcurrentReservationsWithPessimisticLock() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger();

        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(2);

        for (int i = 0; i < numberOfThreads; i++) {
            final int userId = i;
            service.execute(() -> {
                try {
                    ReservationRequest request = new ReservationRequest();
                    request.setResourceId(testResource.getId());
                    request.setUserId("user" + userId);
                    request.setStartTime(startTime);
                    request.setEndTime(endTime);

                    reservationService.reserveWithPessimisticLock(request);
                    successCount.incrementAndGet();
                } catch (AlreadyReservedException e) {
                    // 예상된 예외, 무시
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        service.shutdown();

        // 결과 확인: 하나의 예약만 성공해야 함
        assertEquals(1, successCount.get());
        List<Reservation> reservations = reservationRepository.findByResource(testResource);
        assertEquals(1, reservations.size());
    }

    @Test
    void testConcurrentReservationsWithOptimisticLock() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger();

        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(2);

        for (int i = 0; i < numberOfThreads; i++) {
            final int userId = i;
            service.execute(() -> {
                try {
                    ReservationRequest request = new ReservationRequest();
                    request.setResourceId(testResource.getId());
                    request.setUserId("user" + userId);
                    request.setStartTime(startTime);
                    request.setEndTime(endTime);

                    reservationService.reserveWithOptimisticLock(request);
                    successCount.incrementAndGet();
                } catch (AlreadyReservedException e) {
                    // 예상된 예외, 무시
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        service.shutdown();

        // 결과 확인: 하나의 예약만 성공해야 함
        assertEquals(1, successCount.get());
        List<Reservation> reservations = reservationRepository.findByResource(testResource);
        assertEquals(1, reservations.size());
    }
}
```

## 각 락 유형 설명

### 1. 비관적 락(Pessimistic Lock)

- **작동 방식**: 데이터를 읽을 때 즉시 락을 걸어 다른 트랜잭션이 동시에 같은 데이터를 수정하지 못하도록 합니다.
- **적합한 경우**: 충돌 확률이 높고, 데이터 일관성이 매우 중요한 경우
- **장점**: 충돌이 발생하지 않도록 미리 방지
- **단점**: 동시성이 떨어지고 데드락 가능성이 있음

### 2. 낙관적 락(Optimistic Lock)

- **작동 방식**: 버전 필드를 사용하여 트랜잭션이 커밋될 때 데이터가 변경되지 않았는지 확인
- **적합한 경우**: 충돌 가능성이 낮은 경우
- **장점**: 동시성이 좋고 데드락 위험이 없음
- **단점**: 충돌 시 예외가 발생하므로 애플리케이션에서 재시도 로직이 필요

### 3. 네임드 락(Named Lock)

- **작동 방식**: 데이터베이스 수준에서 특정 이름으로 락을 획득하여 동시 접근을 제어
- **적합한 경우**: 여러 서버에서 동일한 자원에 대한 동시 접근을 제어해야 하는 경우
- **장점**: 분산 환경에서도 효과적
- **단점**: DB 의존적이며, 락 관리에 주의가 필요

## 실행 방법

1. 프로젝트를 클론합니다.
2. `application.properties` 파일에서 데이터베이스 연결 정보를 설정합니다.
3. 애플리케이션을 실행합니다:
   ```bash
   ./mvnw spring-boot:run
   ```
4. API 테스트:
   ```bash
   curl -X POST http://localhost:8080/api/reservations/pessimistic \
     -H "Content-Type: application/json" \
     -d '{"resourceId":1,"userId":"user1","startTime":"2025-03-03T14:00:00","endTime":"2025-03-03T16:00:00"}'
   ```

## 성능 고려사항

- 비관적 락은 동시성이 떨어질 수 있으므로 트래픽이 많은 시스템에서는 주의가 필요합니다.
- 낙관적 락은 충돌 발생 시 재시도 로직을 구현해야 합니다.
- 분산 환경에서는 Redis나 Zookeeper와 같은 외부 락 매니저 사용도 고려해볼 수 있습니다.

## 결론

DB 락을 이용하여 동시성 문제를 해결할 때는 비즈니스 요구사항과 시스템 환경에 맞는 락 전략을 선택해야 합니다. 비관적 락은 충돌이 자주 발생하는 환경에서, 낙관적 락은 충돌이 적은 환경에서 효과적입니다. 중요한 것은 락의 적용 범위를 최소화하여 성능 저하를 방지하는 것입니다.