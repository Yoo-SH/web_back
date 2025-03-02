# 스프링 컨테이너 (Spring Container)

## 목차
- [소개](#소개)
- [스프링 컨테이너의 종류](#스프링-컨테이너의-종류)
- [빈(Bean)](#빈bean)
- [컨테이너 생성 및 빈 등록 방법](#컨테이너-생성-및-빈-등록-방법)
- [스프링 컨테이너 생명주기](#스프링-컨테이너-생명주기)
- [의존성 주입(DI)](#의존성-주입di)
- [스코프(Scope)](#스코프scope)
- [자동 와이어링(Auto-wiring)](#자동-와이어링auto-wiring)
- [주요 어노테이션](#주요-어노테이션)
- [주요 인터페이스와 구현체](#주요-인터페이스와-구현체)
- [실전 예제](#실전-예제)
- [문제 해결 및 디버깅](#문제-해결-및-디버깅)
- [베스트 프랙티스](#베스트-프랙티스)

## 소개

스프링 컨테이너는 스프링 프레임워크의 핵심 컴포넌트로, 애플리케이션의 객체(빈)들을 생성하고 관리하는 역할을 합니다. DI(Dependency Injection, 의존성 주입)와 IoC(Inversion of Control, 제어의 역전)를 통해 객체 간 결합도를 낮추고 코드의 재사용성과 테스트 용이성을 높입니다.

스프링 컨테이너는 다음과 같은 주요 기능을 제공합니다:
- 빈(Bean) 생성 및 관리
- 의존성 주입(DI)
- 라이프사이클 관리
- AOP(Aspect-Oriented Programming) 지원

## 스프링 컨테이너의 종류

스프링은 두 가지 유형의 컨테이너를 제공합니다:

### 1. BeanFactory

- 스프링의 가장 기본적인 컨테이너 인터페이스
- 빈을 관리하고 제공하는 기본 기능만 포함
- 빈의 지연 로딩(Lazy Loading) 방식 사용: 빈이 요청될 때만 인스턴스 생성
- 리소스 사용이 제한적인 환경에 적합
- 구현체: `XmlBeanFactory` 등

```java
BeanFactory factory = new XmlBeanFactory(new FileSystemResource("beans.xml"));
```

### 2. ApplicationContext

- BeanFactory를 확장한 더 풍부한 기능을 가진 컨테이너 인터페이스
- 빈의 즉시 로딩(Eager Loading) 방식 사용: 컨테이너 시작 시 모든 싱글톤 빈 인스턴스 생성
- 추가 기능:
  - 국제화 지원(i18n)
  - 이벤트 발행 메커니즘
  - 애플리케이션 레이어 특정 컨텍스트
  - 리소스 로딩 메커니즘
- 대부분의 애플리케이션에서 권장됨
- 구현체: `ClassPathXmlApplicationContext`, `AnnotationConfigApplicationContext`, `FileSystemXmlApplicationContext`, `WebApplicationContext` 등

```java
ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
```

## 빈(Bean)

빈(Bean)은 스프링 컨테이너가 관리하는 객체입니다.

### 빈의 특성

- 생성과 소멸 관리: 컨테이너에 의해 생명주기 관리
- 의존성 주입: 필요한 의존성을 컨테이너가 자동으로 주입
- 스코프 관리: 싱글톤, 프로토타입 등 다양한 스코프 지원
- 지연 초기화: 필요 시점에 초기화 가능

### 빈 정의(Bean Definition)

빈 정의는 다음 정보를 포함합니다:
- 클래스 이름: 빈의 실제 구현 클래스
- 빈 식별자(ID/Name): 빈을 식별하는 고유 이름
- 스코프: 빈의 생명주기(싱글톤, 프로토타입 등)
- 생성자 인자: 빈 생성 시 필요한 생성자 인자
- 프로퍼티 값: 빈의 프로퍼티에 주입할 값
- 자동 와이어링 모드: 의존성 자동 주입 방식
- 초기화/소멸 메서드: 빈의 초기화/소멸 시 호출될 메서드

## 컨테이너 생성 및 빈 등록 방법

### 1. XML 기반 설정

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 빈 정의 -->
    <bean id="userService" class="com.example.service.UserServiceImpl">
        <property name="userRepository" ref="userRepository"/>
    </bean>
    
    <bean id="userRepository" class="com.example.repository.JdbcUserRepository">
        <constructor-arg ref="dataSource"/>
    </bean>
    
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
        <property name="username" value="user"/>
        <property name="password" value="password"/>
    </bean>
</beans>
```

```java
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
UserService userService = context.getBean("userService", UserService.class);
```

### 2. 자바 기반 설정 (Java Configuration)

```java
@Configuration
public class AppConfig {
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        return dataSource;
    }
    
    @Bean
    public UserRepository userRepository() {
        return new JdbcUserRepository(dataSource());
    }
    
    @Bean
    public UserService userService() {
        UserServiceImpl service = new UserServiceImpl();
        service.setUserRepository(userRepository());
        return service;
    }
}
```

```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
UserService userService = context.getBean(UserService.class);
```

### 3. 컴포넌트 스캔 (Component Scanning)

```java
@Configuration
@ComponentScan("com.example")
public class AppConfig {
    // 빈 설정
}
```

컴포넌트 클래스:

```java
@Component
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // 메서드 구현
}

@Repository
public class JdbcUserRepository implements UserRepository {
    // 구현 코드
}
```

```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
UserService userService = context.getBean(UserService.class);
```

## 스프링 컨테이너 생명주기

### 1. 초기화 단계

1. 구성 파일(XML, Java Config) 로드
2. 빈 정의 생성 및 등록
3. 빈 후처리기(BeanPostProcessor) 등록
4. 빈 인스턴스 생성
5. 의존성 주입
6. 빈 초기화 콜백 메서드 호출 (`@PostConstruct`, `InitializingBean.afterPropertiesSet()`, 사용자 정의 init 메서드)

### 2. 사용 단계

- 애플리케이션 실행 중 빈 사용

### 3. 소멸 단계

1. 컨테이너 종료 시작
2. 빈 소멸 콜백 메서드 호출 (`@PreDestroy`, `DisposableBean.destroy()`, 사용자 정의 destroy 메서드)
3. 리소스 정리
4. 컨테이너 종료 완료

```java
// 컨테이너 생성 및 초기화
ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        
// 컨테이너 사용
MyBean bean = context.getBean("myBean", MyBean.class);
bean.doSomething();
        
// 컨테이너 종료
context.close();
```

## 의존성 주입(DI)

컨테이너가 빈 간의 의존성을 자동으로 연결해주는 메커니즘입니다.

### 1. 생성자 주입 (Constructor Injection)

```java
@Component
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

XML 기반:
```xml
<bean id="userService" class="com.example.service.UserServiceImpl">
    <constructor-arg ref="userRepository"/>
</bean>
```

### 2. 세터 주입 (Setter Injection)

```java
@Component
public class UserServiceImpl implements UserService {
    
    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

XML 기반:
```xml
<bean id="userService" class="com.example.service.UserServiceImpl">
    <property name="userRepository" ref="userRepository"/>
</bean>
```

### 3. 필드 주입 (Field Injection)

```java
@Component
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
}
```

## 스코프(Scope)

빈의 생명주기와 가시성을 정의합니다.

### 주요 스코프 유형

1. **싱글톤(Singleton)**: 기본 스코프, 컨테이너당 하나의 인스턴스만 생성
   ```java
   @Component
   @Scope("singleton")
   public class UserServiceImpl implements UserService { }
   ```

2. **프로토타입(Prototype)**: 요청할 때마다 새로운 인스턴스 생성
   ```java
   @Component
   @Scope("prototype")
   public class ShoppingCart { }
   ```

3. **요청(Request)**: HTTP 요청마다 새로운 인스턴스 생성 (웹 애플리케이션)
   ```java
   @Component
   @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
   public class RequestScopedBean { }
   ```

4. **세션(Session)**: HTTP 세션마다 새로운 인스턴스 생성 (웹 애플리케이션)
   ```java
   @Component
   @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
   public class UserPreferences { }
   ```

5. **애플리케이션(Application)**: ServletContext 생명주기당 하나의 인스턴스 (웹 애플리케이션)

6. **웹소켓(Websocket)**: WebSocket 세션당 하나의 인스턴스 (웹 애플리케이션)

## 자동 와이어링(Auto-wiring)

스프링이 빈 간의 의존성을 자동으로 연결하는 방법입니다.

### 자동 와이어링 모드

1. **no**: 자동 와이어링 비활성화 (명시적 지정 필요)
2. **byName**: 빈의 이름과 일치하는 프로퍼티에 자동 주입
3. **byType**: 빈의 타입과 일치하는 프로퍼티에 자동 주입
4. **constructor**: 생성자 파라미터와 일치하는 빈을 자동 주입
5. **autodetect**: 생성자 → byType 순으로 시도 (Spring 3 이후 deprecated)

XML 예제:
```xml
<bean id="userService" class="com.example.service.UserServiceImpl" autowire="byName"/>
```

## 주요 어노테이션

### 컴포넌트 정의

- **@Component**: 일반적인 스프링 관리 컴포넌트
- **@Service**: 비즈니스 로직 레이어 컴포넌트
- **@Repository**: 데이터 액세스 레이어 컴포넌트
- **@Controller**: 웹 컨트롤러 컴포넌트
- **@RestController**: REST API 컨트롤러 컴포넌트
- **@Configuration**: 자바 기반 설정 클래스

### 의존성 주입

- **@Autowired**: 의존성 자동 주입
- **@Qualifier**: 주입할 특정 빈 지정
- **@Value**: 프로퍼티 값 주입
- **@Resource**: JSR-250 표준 주입 어노테이션
- **@Inject**: JSR-330 표준 주입 어노테이션

### 빈 설정

- **@Bean**: 자바 설정 클래스 내 빈 생성 메서드
- **@Scope**: 빈의 스코프 지정
- **@Primary**: 동일 타입의 여러 빈 중 우선순위 지정
- **@Lazy**: 지연 초기화 설정
- **@Profile**: 활성화된 프로파일에 따른 빈 등록
- **@DependsOn**: 빈 초기화 순서 지정
- **@Conditional**: 조건부 빈 등록

### 생명주기

- **@PostConstruct**: 빈 초기화 콜백 메서드
- **@PreDestroy**: 빈 소멸 콜백 메서드

## 주요 인터페이스와 구현체

### 컨테이너 관련 인터페이스

1. **BeanFactory**: 스프링 IoC 컨테이너의 기본 인터페이스
   ```java
   public interface BeanFactory {
       Object getBean(String name);
       <T> T getBean(Class<T> requiredType);
       <T> T getBean(String name, Class<T> requiredType);
       // 기타 메서드...
   }
   ```

2. **ApplicationContext**: BeanFactory를 확장한 고급 컨테이너 인터페이스
   ```java
   public interface ApplicationContext extends BeanFactory, MessageSource, 
                                              ApplicationEventPublisher, ResourceLoader {
       // 추가 메서드...
   }
   ```

3. **ConfigurableApplicationContext**: 컨테이너의 설정과 생명주기 관리를 위한 인터페이스
   ```java
   public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle {
       void refresh() throws BeansException, IllegalStateException;
       void close();
       // 기타 메서드...
   }
   ```

### 주요 구현체

1. **ClassPathXmlApplicationContext**: 클래스패스의 XML 파일에서 설정을 로드
2. **FileSystemXmlApplicationContext**: 파일 시스템의 XML 파일에서 설정을 로드
3. **AnnotationConfigApplicationContext**: 자바 기반 설정 클래스에서 설정을 로드
4. **XmlWebApplicationContext**: 웹 애플리케이션의 XML 설정 파일에서 설정을 로드
5. **AnnotationConfigWebApplicationContext**: 웹 애플리케이션의 자바 기반 설정 클래스에서 설정을 로드

## 실전 예제

### 전체 예제 - 자바 설정과 컴포넌트 스캔 조합

```java
// 설정 클래스
@Configuration
@ComponentScan("com.example")
@PropertySource("classpath:application.properties")
public class AppConfig {
    
    @Autowired
    private Environment env;
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        return dataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

// 서비스 구현
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}

// 레포지토리 구현
@Repository
public class JdbcUserRepository implements UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public User findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT id, name, email FROM users WHERE id = ?",
            new BeanPropertyRowMapper<>(User.class),
            id
        );
    }
    
    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT id, name, email FROM users",
            new BeanPropertyRowMapper<>(User.class)
        );
    }
    
    @Override
    public void save(User user) {
        if (user.getId() == null) {
            jdbcTemplate.update(
                "INSERT INTO users(name, email) VALUES(?, ?)",
                user.getName(), user.getEmail()
            );
        } else {
            jdbcTemplate.update(
                "UPDATE users SET name = ?, email = ? WHERE id = ?",
                user.getName(), user.getEmail(), user.getId()
            );
        }
    }
}

// 애플리케이션 실행
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        
        // 서비스 사용
        User user = new User();
        user.setName("홍길동");
        user.setEmail("hong@example.com");
        userService.save(user);
        
        List<User> users = userService.findAll();
        for (User u : users) {
            System.out.println(u);
        }
    }
}
```

## 문제 해결 및 디버깅

### 일반적인 문제와 해결책

1. **NoSuchBeanDefinitionException**
   - 문제: 요청한 빈을 찾을 수 없음
   - 해결책: 컴포넌트 스캔 패키지 확인, 빈 이름/타입 확인, 설정 클래스 로드 확인

2. **NoUniqueBeanDefinitionException**
   - 문제: 동일한 타입의 빈이 여러 개 존재
   - 해결책: `@Qualifier` 사용, `@Primary` 지정, 이름으로 빈 검색

3. **BeanCreationException**
   - 문제: 빈 생성 중 예외 발생
   - 해결책: 의존성 확인, 생성자/초기화 메서드 확인

4. **의존성 순환 참조(Circular Dependency)**
   - 문제: 빈 A가 빈 B를 의존하고, 빈 B가 다시 빈 A를 의존
   - 해결책: 생성자 주입 대신 세터 주입 사용, 구조 재설계, `@Lazy` 사용

### 디버깅 팁

1. 로깅 활성화:
```properties
# application.properties
logging.level.org.springframework=DEBUG
```

2. 빈 정의 정보 출력:
```java
ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
Arrays.stream(context.getBeanDefinitionNames())
      .forEach(System.out::println);
```

3. 빈 의존성 확인:
```java
DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
for (String name : context.getBeanDefinitionNames()) {
    BeanDefinition bd = factory.getBeanDefinition(name);
    System.out.println(name + " -> " + bd.getDependsOn());
}
```

## 베스트 프랙티스

1. **생성자 주입 선호**
   - 불변성 보장
   - 필수 의존성 명확화
   - 순환 참조 감지
   - 테스트 용이성

2. **컴포넌트 스캔과 명시적 설정 조합**
   - 컴포넌트 스캔: 애플리케이션 코드에 사용
   - 명시적 `@Bean`: 서드파티 라이브러리에 사용

3. **적절한 스테레오타입 어노테이션 사용**
   - `@Service`: 비즈니스 로직
   - `@Repository`: 데이터 액세스
   - `@Controller`/`@RestController`: 웹 컨트롤러

4. **프로퍼티 외부화**
   - `@PropertySource`와 `Environment` 또는 `@Value` 사용
   - 환경별 프로파일 설정(`@Profile`)

5. **명명 규칙 준수**
   - 빈 이름은 카멜 케이스(camelCase) 사용
   - 의미 있는 이름 부여

6. **AOP 활용**
   - 교차 관심사(cross-cutting concerns)를 분리
   - 트랜잭션, 로깅, 보안 등에 활용

7. **테스트 용이성 확보**
   - 인터페이스 기반 설계
   - 의존성 주입을 통한 목(mock) 객체 사용
   - `@SpringBootTest`, `@ContextConfiguration` 활용

8. **빈 생명주기 인지**
   - 초기화/소멸 콜백 적절히 활용
   - 스코프에 따른 동작 차이 이해