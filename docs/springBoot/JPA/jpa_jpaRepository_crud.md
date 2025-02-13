# Spring JPA CRUD 가이드

## 개요
Spring Boot와 JPA를 사용하여 CRUD (Create, Read, Update, Delete) 기능을 구현하는 방법을 설명합니다.

## 프로젝트 설정
### 1. 의존성 추가 (`pom.xml`)
```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- H2 Database (테스트용) -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## 엔티티(Entity) 생성
```java
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;
}
```

## 리포지토리(Repository) 생성
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

## 서비스(Service) 계층 구현
```java
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

## 컨트롤러(Controller) 생성
```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
```

## 애플리케이션 실행 및 테스트

1. `Spring Boot` 애플리케이션 실행:
```bash
./mvnw spring-boot:run
```

2. API 테스트 (Postman 또는 cURL 사용)
    - 모든 사용자 조회: `GET http://localhost:8080/users`
    - 특정 사용자 조회: `GET http://localhost:8080/users/{id}`
    - 사용자 생성: `POST http://localhost:8080/users` (JSON 데이터 필요)
    - 사용자 업데이트: `PUT http://localhost:8080/users/{id}` (JSON 데이터 필요)
    - 사용자 삭제: `DELETE http://localhost:8080/users/{id}`

## 결론
이 가이드를 통해 Spring Boot와 JPA를 사용한 기본 CRUD 기능을 구현하는 방법을 배웠습니다. 이를 기반으로 확장하여 더 복잡한 기능을 추가할 수 있습니다.

