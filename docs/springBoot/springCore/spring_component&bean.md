# Spring @Bean vs @Component

## 목차
1. [개요](#개요)
2. [주요 차이점](#주요-차이점)
3. [@Component 상세](#component-상세)
   - [사용 방법](#component-사용-방법)
   - [종류](#component-종류)
   - [예시 코드](#component-예시-코드)
4. [@Bean 상세](#bean-상세)
   - [사용 방법](#bean-사용-방법)
   - [특징](#bean-특징)
   - [예시 코드](#bean-예시-코드)
5. [실제 프로젝트 활용 시나리오](#실제-프로젝트-활용-시나리오)
6. [선택 가이드](#선택-가이드)
7. [Bean과 Component의 spring 처리 방식 차이](#bean과-component의-spring-처리-방식-차이)
8. [결론](#결론)

## 개요
Spring Framework에서 빈(Bean)을 등록하는 방법은 크게 두 가지가 있습니다: `@Component`와 `@Bean` 애노테이션을 사용하는 방법입니다. 두 방식 모두 객체를 스프링 컨테이너가 관리하는 빈으로 등록한다는 목적은 같지만, 사용 맥락과 활용 방식에 차이가 있습니다.

## 주요 차이점

| 구분 | @Component | @Bean |
|------|------------|-------|
| 적용 대상 | 클래스 레벨 | 메소드 레벨 |
| 위치 | 개발자가 직접 작성한 클래스 | 주로 @Configuration 클래스 내부 메소드 |
| 구성 방식 | 컴포넌트 스캔 방식 (자동 감지) | 명시적 구성 방식 |
| 주요 용도 | 개발자가 직접 작성한 클래스를 빈으로 등록 | 외부 라이브러리 클래스를 빈으로 등록 또는 조건부 빈 생성 |
| 제어 수준 | 상대적으로 단순 | 세밀한 제어 가능 |

## @Component 상세

### @Component 사용 방법
`@Component` 애노테이션은 클래스 레벨에 적용하며, 스프링의 컴포넌트 스캔 기능을 통해 자동으로 감지되어 빈으로 등록됩니다. 컴포넌트 스캔을 활성화하려면 설정 클래스에 `@ComponentScan` 애노테이션을 추가하거나, Spring Boot에서는 `@SpringBootApplication`이 이 기능을 포함합니다.

### @Component 종류
`@Component`의 특수화된 형태로 다음과 같은 애노테이션이 있습니다:
- `@Repository`: 데이터 접근 계층의 컴포넌트
- `@Service`: 비즈니스 로직 계층의 컴포넌트
- `@Controller`: 웹 요청을 처리하는 프레젠테이션 계층의 컴포넌트
- `@RestController`: REST API를 제공하는 컨트롤러

### @Component 예시 코드

```java
// UserService 클래스를 스프링 빈으로 등록
@Component
public class UserService {
    private final UserRepository userRepository;
    
    // 생성자 주입 방식으로 의존성 주입
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User findById(Long id) {
        return userRepository.findById(id);
    }
}

// 다른 컴포넌트 종류들 (모두 @Component의 특수화된 형태)
@Repository
public class UserRepository {
    // 데이터 접근 로직
}

@Controller
public class UserController {
    // 웹 요청 처리 로직
}

@Service
public class EmailService {
    // 비즈니스 로직
}
```

## @Bean 상세

### @Bean 사용 방법
`@Bean` 애노테이션은 메소드 레벨에 적용하며, 일반적으로 `@Configuration` 클래스 내부에 위치합니다. 메소드가 반환하는 객체가 스프링 컨테이너에 빈으로 등록됩니다.

### @Bean 특징
- 메소드 이름이 기본 빈 이름이 됩니다 (name 속성으로 변경 가능)
- 초기화 메소드, 소멸 메소드를 지정할 수 있습니다
- 메소드 내에서 객체 생성 로직을 세밀하게 제어할 수 있습니다
- 조건부 빈 생성이 가능합니다 (`@Conditional` 계열 애노테이션과 함께 사용)

### @Bean 예시 코드

```java
@Configuration
public class AppConfig {
    
    // 외부 라이브러리 클래스를 빈으로 등록
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        return dataSource;
    }
    
    // 조건부 빈 생성
    @Bean
    @ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
    
    // 같은 타입의 여러 빈 생성
    @Bean(name = "simplePdfGenerator")
    public PdfGenerator simplePdfGenerator() {
        return new SimplePdfGenerator();
    }
    
    @Bean(name = "advancedPdfGenerator")
    public PdfGenerator advancedPdfGenerator() {
        return new AdvancedPdfGenerator();
    }
    
    // 명시적인 의존성 주입
    @Bean
    public OrderService orderService(UserRepository userRepository, PaymentGateway paymentGateway) {
        OrderServiceImpl service = new OrderServiceImpl();
        service.setUserRepository(userRepository);
        service.setPaymentGateway(paymentGateway);
        service.setMaxRetries(3);
        return service;
    }
}
```

## 실제 프로젝트 활용 시나리오

1. **직접 개발한 비즈니스 로직**: `@Component`나 `@Service` 등을 사용
   ```java
   @Service
   public class ProductServiceImpl implements ProductService {
       // 구현 내용
   }
   ```

2. **외부 라이브러리 연동**: `@Bean` 사용
   ```java
   @Configuration
   public class AmazonS3Config {
       @Bean
       public AmazonS3Client amazonS3Client() {
           return AmazonS3ClientBuilder.standard()
                   .withRegion("ap-northeast-2")
                   .withCredentials(new DefaultAWSCredentialsProviderChain())
                   .build();
       }
   }
   ```

3. **프로필에 따른 다른 구현체 사용**: `@Bean`과 `@Profile` 조합
   ```java
   @Configuration
   public class EmailConfig {
       @Bean
       @Profile("dev")
       public EmailSender devEmailSender() {
           return new MockEmailSender();  // 개발 환경용 더미 구현체
       }
       
       @Bean
       @Profile("prod")
       public EmailSender prodEmailSender() {
           return new SmtpEmailSender();  // 실제 운영 환경용 구현체
       }
   }
   ```

4. **빈 초기화/소멸 라이프사이클 관리**:
   ```java
   @Configuration
   public class DatabaseConfig {
       @Bean(initMethod = "init", destroyMethod = "close")
       public DatabaseConnection databaseConnection() {
           DatabaseConnection conn = new DatabaseConnection();
           conn.setUrl("jdbc:postgresql://localhost:5432/mydb");
           return conn;
       }
   }
   ```

## 선택 가이드

다음 상황에서는 `@Component`를 사용하세요:
- 직접 개발한 클래스를 빈으로 등록할 때
- 단순한 의존성 주입만 필요할 때
- 스프링의 표준 스테레오타입 역할(컨트롤러, 서비스, 리포지토리)을 나타내고 싶을 때

다음 상황에서는 `@Bean`을 사용하세요:
- 외부 라이브러리의 클래스를 빈으로 등록할 때
- 조건부로 빈을 생성해야 할 때
- 동일한 타입의 여러 빈을 생성해야 할 때
- 빈 생성 과정에서 상세한 설정이 필요할 때
- 초기화/소멸 메소드를 명시적으로 지정해야 할 때

## Bean과 Component의 spring 처리 방식 차이

### 1. 처리 메커니즘:

- @Component: 컴포넌트 스캔 과정에서 처리됩니다. Spring은 @ComponentScan이 설정된 패키지와 하위 패키지를 스캔하여 @Component 애노테이션(및 @Service, @Repository, @Controller 등 파생 애노테이션)이 붙은 클래스를 찾고, 이들을 빈으로 등록합니다.

- @Bean: Spring의 구성 클래스(@Configuration) 처리 과정에서 처리됩니다. @Configuration 클래스가 처리될 때 CGLIB 프록시가 생성되고, @Bean 메서드가 호출될 때 이 프록시가 빈 인스턴스의 생명주기를 관리합니다.


### 2. 인스턴스 생성 시점:

- @Component: 애플리케이션 컨텍스트 시작 시 컴포넌트 스캔 단계에서 발견되어 인스턴스화됩니다.

- @Bean: @Configuration 클래스의 @Bean 메서드가 실행될 때 인스턴스가 생성됩니다. 이는 일반적으로 컨텍스트 초기화 중 일어나지만, 지연 초기화(lazy initialization)가 설정된 경우 빈이 처음 요청될 때까지 지연될 수 있습니다.


### 3. 프록시 처리:

- @Component: 일반적으로 클래스 자체가 프록시되지는 않지만, 내부 메서드는 AOP 등을 통해 프록시될 수 있습니다.

- @Bean: @Configuration 클래스는 CGLIB 프록시로 감싸집니다. 이는 @Bean 메서드의 여러 번 호출에도 동일한 인스턴스를 반환하도록 보장합니다(싱글톤 범위의 경우).


### 4. 생명주기 관리:

- @Component: 빈 정의가 생성된 후 표준 빈 생명주기 관리를 따릅니다. @PostConstruct, @PreDestroy 애노테이션을 사용하여 초기화/소멸 메서드를 지정할 수 있습니다.

- @Bean: 메서드 내에서 초기화 로직을 직접 코딩하거나, initMethod와 destroyMethod 속성을 통해 명시적으로 초기화/소멸 메서드를 지정할 수 있습니다.


### 5. 구성 유연성:

- @Component: 클래스 자체에 정의되어 있어, 동일한 클래스의 다양한 인스턴스를 다르게 구성하기 어렵습니다.

- @Bean: 메서드 내에서 객체 구성을 완전히 제어할 수 있어, 동일한 클래스 타입의 다양한 빈 인스턴스를 각각 다르게 구성할 수 있습니다.

## 결론

`@Component`와 `@Bean`은 각각 다른 상황에서 최적의 선택이 될 수 있습니다. 일반적으로 개발자가 직접 작성한 클래스는 `@Component`로, 외부 라이브러리나 복잡한 구성이 필요한 빈은 `@Bean`으로 등록하는 것이 권장됩니다. 두 방식을 상황에 맞게 적절히 혼합하여 사용하면 스프링 애플리케이션의 빈 구성을 효과적으로 관리할 수 있습니다.