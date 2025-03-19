# Spring Framework와 Jakarta EE 가이드

## 목차
- [개요](#개요)
- [Jakarta EE](#jakarta-ee)
  - [Jakarta EE 역사](#jakarta-ee-역사)
  - [주요 명세](#주요-명세)
  - [컴포넌트 모델](#컴포넌트-모델)
- [Spring Framework](#spring-framework)
  - [Spring의 역사](#spring의-역사)
  - [핵심 모듈](#핵심-모듈)
  - [Spring Boot](#spring-boot)
- [Jakarta EE와 Spring 비교](#jakarta-ee와-spring-비교)
- [함께 사용하기](#함께-사용하기)
- [시작하기](#시작하기)

## 개요
이 문서는 Jakarta EE와 Spring Framework의 주요 개념, 아키텍처, 그리고 이들을 활용한 엔터프라이즈 애플리케이션 개발에 대한 정보를 제공합니다.

## Jakarta EE

### Jakarta EE 역사
- **Java EE에서 Jakarta EE로**: 2017년에 Oracle이 Java EE를 Eclipse Foundation으로 이관하면서 Jakarta EE로 명칭 변경
- **Java EE**: 1999년 J2EE(Java 2 Platform, Enterprise Edition)로 시작
- **버전 변천**: J2EE 1.2, 1.3, 1.4 → Java EE 5, 6, 7, 8 → Jakarta EE 8, 9, 10, 11(최신)

### [주요 명세](https://github.com/Yoo-SH/web_back/blob/main/docs/springBoot/utiltity_library/spring_jakarta_specification.md)
Jakarta EE는 다음과 같은 주요 기술 명세를 포함합니다:

1. **Jakarta Servlet**: 웹 애플리케이션을 위한 HTTP 요청/응답 처리 API
2. **Jakarta Server Pages (JSP)**: 동적 웹 콘텐츠 생성을 위한 템플릿 기술
3. **Jakarta Server Faces (JSF)**: 컴포넌트 기반 UI 프레임워크
4. **Jakarta Enterprise Beans (EJB)**: 분산 비즈니스 컴포넌트
5. **Jakarta Persistence (JPA)**: 객체 관계형 매핑(ORM) API
6. **Jakarta CDI (Contexts and Dependency Injection)**: 컨텍스트 기반 의존성 주입 프레임워크
7. **Jakarta RESTful Web Services (JAX-RS)**: REST API 개발 프레임워크
8. **Jakarta Bean Validation**: 객체 유효성 검증 API
9. **Jakarta Transactions (JTA)**: 분산 트랜잭션 API
10. **Jakarta JSON Processing & Binding**: JSON 처리 및 객체 바인딩 API
11. **Jakarta WebSocket**: 양방향 실시간 통신 API
12. **Jakarta Security**: 인증 및 권한 부여 API


## Spring Framework

### Spring의 역사
- **탄생**: 2003년 Rod Johnson이 'Expert One-on-One J2EE Design and Development' 책과 함께 출시
- **초기 목적**: EJB의 복잡성을 줄이고 POJO 기반 개발 장려
- **발전**: Spring 1.0 (2004) → Spring 2.0 → ... → Spring 6.1 (현재)

### 핵심 모듈
1. **Spring Core**: 의존성 주입(DI) 및 제어의 역전(IoC) 기능을 제공하는 핵심 모듈
2. **Spring AOP**: 관점 지향 프로그래밍 구현
3. **Spring Data Access**: JDBC, ORM 통합, 트랜잭션 관리
4. **Spring Web**: 웹 애플리케이션 개발을 위한 MVC 프레임워크, RESTful 웹 서비스
5. **Spring Test**: 단위 및 통합 테스팅 프레임워크

## Jakarta EE와 Spring 비교

| 측면 | Jakarta EE | Spring Framework |
|------|------------|------------------|
| **표준** | 공식 Java 엔터프라이즈 표준 | 사실상(de facto) 표준 |
| **구성 방식** | XML, 어노테이션 | Java, 어노테이션, XML |
| **의존성 주입** | CDI | Spring IoC |
| **웹 기술** | Servlet, JSP, JSF | Spring MVC, WebFlux |
| **ORM** | JPA | Spring Data JPA |
| **확장성** | 명세에 제한됨 | 다양한 생태계 확장 |
| **인기도** | 안정적 | 매우 높음 |
| **학습 곡선** | 가파름 | 중간~가파름 |

* compare annotaion example with spring
    - jakarta @Inject <-> spring @Autowired
    - jakarta @Nameed <-> spring @Component
## 함께 사용하기
Spring Framework는 Jakarta EE 명세의 많은 부분을 구현하거나 통합합니다:

- Spring MVC는 Jakarta Servlet 기반
- Spring Data JPA는 Jakarta JPA 사용
- Spring의 @Transactional은 Jakarta Transactions과 통합
- Spring Security는 Jakarta Security와 호환
- Spring의 Validation은 Jakarta Bean Validation 구현

**일반적인 통합 시나리오**:
1. Jakarta EE 애플리케이션 서버에 Spring 애플리케이션 배포
2. Spring Boot에서 Jakarta EE 기술 사용(JPA, Bean Validation 등)
3. 하이브리드 접근법: 일부는 Jakarta EE, 일부는 Spring 사용

## 시작하기

### Jakarta EE 애플리케이션 설정

```xml
<dependency>
    <groupId>jakarta.platform</groupId>
    <artifactId>jakarta.jakartaee-api</artifactId>
    <version>10.0.0</version>
    <scope>provided</scope>
</dependency>
```

### Spring Boot 애플리케이션 설정

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
</dependencies>
```

### Spring Boot + Jakarta EE 통합 예제

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Entity
class User {
    @Id @GeneratedValue
    private Long id;
    
    @NotBlank
    private String name;
    
    // getters, setters
}

interface UserRepository extends JpaRepository<User, Long> {
}

@RestController
@RequestMapping("/users")
class UserController {
    private final UserRepository repository;
    
    UserController(UserRepository repository) {
        this.repository = repository;
    }
    
    @GetMapping
    public List<User> findAll() {
        return repository.findAll();
    }
    
    @PostMapping
    public User create(@RequestBody User user) {
        return repository.save(user);
    }
}
```
