# Spring Data JPA: JpaRepository vs EntityManager

## 목차
- [개요](#1-개요)
- [JpaRepository](#2-jparepository)
    - [개념](#개념)
    - [주요 기능](#주요-기능)
    - [사용 예시](#사용-예시)
    - [장점](#장점)
    - [단점](#단점)
- [EntityManager](#3-entitymanager)
- [JpaRepository vs EntityManager 비교](#4-jparepository-vs-entitymanager-비교)
- [결론](#5-결론)

## 1. 개요
Spring Data JPA에서 데이터를 다룰 때 `JpaRepository`와 `EntityManager` 두 가지 방법을 사용할 수 있습니다. 이 문서는 두 방식의 차이점을 비교하고 각각의 장단점을 설명합니다.


## 2. JpaRepository
### 개념
- `JpaRepository`는 Spring Data JPA에서 제공하는 인터페이스로, 기본적인 CRUD 및 페이징, 정렬 등의 기능을 자동으로 제공합니다.
- 인터페이스만 정의하면 Spring이 구현체를 자동으로 생성해줍니다.

### 주요 기능
- 기본적인 CRUD 메서드 제공 (`save`, `findById`, `delete` 등)
- 페이징 및 정렬 (`findAll(Pageable pageable)`, `findAll(Sort sort)`)
- 커스텀 쿼리 메서드 (`findByName`, `findByAgeGreaterThan` 등)
- `@Query` 어노테이션을 활용한 직접적인 JPQL 사용 가능

### 사용 예시
```java
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
```

### 장점
- 기본적인 CRUD를 자동으로 구현하여 코드량 감소
- `@Query`를 활용하여 간단한 쿼리를 쉽게 작성 가능
- 페이징 및 정렬 기능 제공
- Spring의 트랜잭션 및 AOP 기능과의 쉬운 통합

### 단점
- 복잡한 쿼리 작성 시 한계가 있음
- 성능 최적화가 필요한 경우 EntityManager보다 유연성이 부족할 수 있음



## 3. EntityManager
### 개념
- `EntityManager`는 JPA의 핵심 객체로, 엔터티를 직접 관리하고 데이터베이스와 상호작용할 수 있도록 해줍니다.
- JPA의 `javax.persistence.EntityManager` 인터페이스를 사용하며, 복잡한 JPQL, Native Query, 배치 처리 등에 적합합니다.

### 주요 기능
- 직접적인 CRUD 수행 (`persist`, `merge`, `remove`, `find` 등)
- JPQL 및 Native Query 실행 (`createQuery`, `createNativeQuery`)
- 트랜잭션 관리 및 동적 쿼리 작성 가능
- 1차 캐시, 영속성 컨텍스트 활용 가능

### 사용 예시
```java
@Repository
public class UserDao {
    
    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findByName(String name) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
                .setParameter("name", name)
                .getResultList();
    }
}
```

### 장점
- 복잡한 쿼리 작성 가능 (JPQL, Native Query, Criteria API 지원)
- 성능 최적화 가능 (배치 처리, 캐싱 활용 등)
- 영속성 컨텍스트를 활용한 더 세밀한 엔터티 관리 가능

### 단점
- 직접적인 쿼리 작성 필요 → 코드량 증가
- 트랜잭션 및 영속성 컨텍스트 관리가 필요함



## 4. JpaRepository vs EntityManager 비교

| 비교 항목       | JpaRepository | EntityManager |
|-|--|--|
| 사용 편의성    | 인터페이스 기반으로 간단하게 사용 가능 | 코드량이 많아질 수 있음 |
| 기본 CRUD 지원 | 기본 제공 (`save`, `delete`, `findAll` 등) | 직접 작성해야 함 |
| 복잡한 쿼리 작성 | 제한적 (Native Query 사용 가능하지만 불편함) | 자유롭게 작성 가능 |
| 성능 최적화    | 기본적인 최적화 제공 | 직접 튜닝 가능 |
| 트랜잭션 관리  | Spring이 자동 관리 | 직접 관리해야 할 수도 있음 |



## 5. 결론
- **JpaRepository**는 기본적인 CRUD, 페이징, 간단한 쿼리 작성에 적합하며, 빠른 개발을 원할 때 유용합니다.
- **EntityManager**는 복잡한 쿼리, 성능 최적화, 영속성 컨텍스트를 세밀하게 조정해야 할 때 적합합니다.
- 프로젝트의 요구 사항에 따라 적절한 방법을 선택하거나, 둘을 함께 사용할 수도 있습니다.

