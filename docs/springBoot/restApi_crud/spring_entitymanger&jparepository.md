# Spring Data JPA: EntityManager vs JpaRepository 비교

## 목차
- [개요](#개요)
- [EntityManager 방식](#entitymanager-방식)
- [JpaRepository 방식](#jparepository-방식)
- [주요 차이점](#주요-차이점)
- [사용 예제](#사용-예제)
- [권장 사항](#권장-사항)

## 개요
Spring Data JPA에서는 데이터베이스 작업을 위해 EntityManager와 JpaRepository 두 가지 방식을 제공합니다. 각각의 특징과 사용 사례를 이해하면 프로젝트에 적합한 방식을 선택할 수 있습니다.

## EntityManager 방식
EntityManager는 JPA의 핵심 클래스로, 엔티티의 저장, 수정, 삭제, 조회 등 엔티티와 관련된 모든 작업을 수행합니다.

### 특징
- JPA의 저수준 API 직접 사용
- 세밀한 제어 가능
- 모든 데이터베이스 작업을 직접 구현

### 예제 코드
```java
@Repository
public class UserDaoEntityManager {
    @PersistenceContext
    private EntityManager em;
    
    public User findById(Long id) {
        return em.find(User.class, id);
    }
    
    public void save(User user) {
        em.persist(user);
    }
    
    public List<User> findByUsername(String username) {
        return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                 .setParameter("username", username)
                 .getResultList();
    }
}
```

## JpaRepository 방식
JpaRepository는 Spring Data JPA에서 제공하는 인터페이스로, 일반적인 CRUD 작업을 위한 추상화된 인터페이스입니다.


__jpa repository를 사용하면 기본 crud를 제공하기에 따로 직접 구현할 필요성이 없음__
<img src=https://github.com/user-attachments/assets/db328df4-8381-4a94-99ae-22982ba3002b width=500px>

__jpa interface__
<img src=https://github.com/user-attachments/assets/bf3e713c-b6f6-4022-8f65-56547ad01913 width=500px>

### 특징
- 기본 CRUD 메서드 자동 제공(구현할 필요가 없음)
- 메서드 이름으로 쿼리 생성 가능
- 페이징, 정렬 기능 기본 제공

### 예제 코드
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
    List<User> findByEmailAndActive(String email, boolean active);
    
    @Query("SELECT u FROM User u WHERE u.createdDate > :date")
    List<User> findRecentUsers(@Param("date") LocalDateTime date);
}
```

## 주요 차이점

### 1. 코드 작성량
- EntityManager: 많은 보일러플레이트 코드 필요
- JpaRepository: 최소한의 코드로 구현 가능

### 2. 추상화 수준
- EntityManager: 낮은 수준의 추상화 (세밀한 제어 가능)
- JpaRepository: 높은 수준의 추상화 (편리한 사용)

### 3. 쿼리 생성
- EntityManager: JPQL을 직접 작성
- JpaRepository: 메서드 이름으로 자동 쿼리 생성

### 4. 트랜잭션 처리
- EntityManager: 수동 트랜잭션 관리
- JpaRepository: 자동 트랜잭션 관리

### 5. 유지보수성
- EntityManager: 상대적으로 복잡한 유지보수
- JpaRepository: 간단한 유지보수

## 사용 예제

### EntityManager 사용이 적합한 경우
- 복잡한 쿼리가 필요한 경우
- 대량의 데이터를 처리하는 경우
- 세밀한 성능 최적화가 필요한 경우
- 동적 쿼리 생성이 필요한 경우

### JpaRepository 사용이 적합한 경우
- 기본적인 CRUD 작업
- 간단한 쿼리 작업
- 빠른 개발이 필요한 경우
- 표준화된 데이터 액세스 계층이 필요한 경우

## 권장 사항
1. 기본적으로 JpaRepository 사용을 권장
2. 복잡한 쿼리나 특별한 최적화가 필요한 경우에만 EntityManager 사용
3. 필요한 경우 두 방식을 혼합하여 사용 가능
4. 프로젝트의 요구사항과 복잡도를 고려하여 선택