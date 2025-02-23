# Spring JPA 개요

## 목차
- [Spring JPA란?](#1-spring-jpa란)
- [주요 기능](#2-주요-기능)
    - [Repository 인터페이스 제공](#21-repository-인터페이스-제공)
    - [쿼리 메서드 자동 생성](#22-쿼리-메서드-자동-생성)
    - [JPQL과 네이티브 쿼리 지원](#23-jpql과-네이티브-쿼리-지원)
    - [페이징 및 정렬 지원](#24-페이징-및-정렬-지원)
- [설정 방법](#3-설정-방법)
    - [의존성 추가](#31-의존성-추가)
    - [`application.yml` 설정](#32-applicationyml-설정)
    - [엔터티 클래스 생성](#33-엔터티-클래스-생성)
    - [Repository 인터페이스 생성](#34-repository-인터페이스-생성)
    - [서비스 및 컨트롤러 작성](#35-서비스-및-컨트롤러-작성)
- [Hibernate와의 관계](#4-hibernate와의-관계)
- [결론](#5-결론)
## 1. Spring JPA란?
Spring JPA(Java Persistence API)는 Spring 프레임워크에서 JPA를 편리하게 사용할 수 있도록 지원하는 모듈입니다. JPA는 Java 애플리케이션에서 관계형 데이터베이스를 쉽게 다룰 수 있도록 도와주는 표준 ORM(Object-Relational Mapping) 기술입니다.

Spring Data JPA는 JPA를 더욱 쉽게 사용할 수 있도록 다양한 기능을 추가한 라이브러리로, 반복적인 CRUD 코드를 줄이고 개발 생산성을 높이는 데 도움을 줍니다.



## 2. 주요 기능
### 2.1. Repository 인터페이스 제공
Spring Data JPA는 기본적인 CRUD 기능을 제공하는 `JpaRepository`, `CrudRepository` 등의 인터페이스를 제공합니다.

```java
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
```

### 2.2. 쿼리 메서드 자동 생성
메서드 이름만으로 SQL 쿼리를 자동 생성할 수 있습니다.
```java
List<User> findByEmail(String email);
List<User> findByAgeGreaterThan(int age);
```

### 2.3. JPQL과 네이티브 쿼리 지원
```java
@Query("SELECT u FROM User u WHERE u.name = :name")
List<User> findByNameQuery(@Param("name") String name);

@Query(value = "SELECT * FROM users WHERE name = :name", nativeQuery = true)
List<User> findByNameNativeQuery(@Param("name") String name);
```

### 2.4. 페이징 및 정렬 지원
페이징과 정렬을 간단하게 적용할 수 있습니다.
```java
Page<User> findByAgeGreaterThan(int age, Pageable pageable);
List<User> findByNameOrderByAgeDesc(String name);
```



## 3. 설정 방법
### 3.1. 의존성 추가
Maven 프로젝트의 경우 `pom.xml`에 아래와 같이 추가합니다.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 3.2. `application.yml` 설정
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.H2Dialect
```

### 3.3. 엔터티 클래스 생성
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private int age;
}
```

### 3.4. Repository 인터페이스 생성
```java
public interface UserRepository extends JpaRepository<User, Long> {}
```

### 3.5. 서비스 및 컨트롤러 작성
```java
@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
```



## 4. Hibernate와의 관계
Hibernate는 JPA의 구현체 중 하나로, Spring Data JPA에서는 기본적으로 Hibernate를 사용하여 ORM을 처리합니다. Hibernate는 객체와 데이터베이스 간의 매핑을 자동화하고, SQL을 직접 작성하지 않아도 데이터베이스 작업을 쉽게 수행할 수 있도록 돕습니다.

Spring JPA는 Hibernate를 기본 제공 구현체로 사용하며, 이를 통해 JPA의 표준 인터페이스를 활용하면서도 Hibernate의 강력한 기능을 활용할 수 있습니다. Spring Boot에서는 추가적인 설정 없이도 Hibernate를 자동으로 구성하여 편리한 데이터 처리가 가능합니다.



## 5. 결론
Spring JPA는 JPA의 복잡한 설정과 반복적인 코드 작성을 줄여주며, 보다 직관적인 방식으로 데이터베이스를 관리할 수 있도록 도와줍니다. Spring Boot와 함께 사용하면 더욱 강력한 기능을 제공하며, 빠른 개발이 가능합니다.

