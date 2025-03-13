
# Jakarta EE 명세 소개

Jakarta EE는 엔터프라이즈 애플리케이션을 개발하기 위한 표준 기술 명세입니다. 다음은 Jakarta EE의 주요 명세 목록입니다:


## 목차 
- [Jakarta Servlet](#1-jakarta-servlet)
- [Jakarta Server Pages](#2-jakarta-server-pages-jsp)
- [Jakarta Enterprise Beans](#3-jakarta-enterprise-beans-ejb)
- [Jakarta Persistence API](#4-jakarta-persistence-api-jpa)
- [Jakarta Contexts and Dependency Injection](#5-jakarta-contexts-and-dependency-injection-cdi)
- [Jakarta RESTful Web Services](#6-jakarta-restful-web-services-jax-rs)
- [Jakarta Bean Validation](#7-jakarta-bean-validation)
- [Jakarta Transactions](#8-jakarta-transactions-jta)
- [Jakarta JSON Processing & Binding](#9-jakarta-json-processing--binding)
- [Jakarta WebSocket](#10-jakarta-websocket)
- [Jakarta Security](#11-jakarta-security)

## 1. Jakarta Servlet

### 개요
Jakarta Servlet은 웹 애플리케이션에서 HTTP 요청과 응답을 처리하는 기본 API입니다. 서블릿 컨테이너에서 실행되며, 클라이언트 요청을 처리하고 동적인 웹 콘텐츠를 생성하는 역할을 합니다.

### 주요 특징
- **요청 및 응답 처리**: `HttpServletRequest` 및 `HttpServletResponse` 객체를 통해 HTTP 요청 및 응답을 다룸
- **세션 관리**: 사용자 세션을 관리하여 상태 유지 가능
- **필터 및 리스너**: 요청 전/후 처리 및 애플리케이션 이벤트 감지 기능 제공
- **비동기 요청 처리**: 비동기 방식으로 요청을 처리할 수 있음

### 코드 예제
```java
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<h1>Hello, Jakarta EE!</h1>");
    }
}
```

### 2. Jakarta Server Pages (JSP)

### 개요
JSP는 Java 코드를 HTML 페이지에 임베드할 수 있게 해주는 서버 사이드 기술입니다. 동적 웹 콘텐츠를 생성하기 위한 템플릿 엔진으로 작동합니다.

### 주요 특징
- **스크립트 요소**: `<% %>` (스크립틀릿), `<%= %>` (표현식), `<%! %>` (선언)
- **내장 객체**: request, response, session, application, out 등 자동으로 사용 가능
- **액션 태그**: `<jsp:include>`, `<jsp:forward>`, `<jsp:useBean>` 등 제공
- **표현 언어(EL)**: `${...}` 구문으로 속성 접근 간소화
- **생명주기**: JSP는 첫 요청 시 서블릿으로 변환, 컴파일되어 실행됨

### 코드 예제
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>JSP 예제</title>
</head>
<body>
    <h1>JSP 예제</h1>
    
    <% 
    // 스크립틀릿 - Java 코드 실행
    String message = "안녕하세요!";
    for (int i = 1; i <= 3; i++) {
    %>
        <p><%= message %> #<%= i %></p> <!-- 표현식 -->
    <% 
    }
    %>
    
    <%! 
    // 선언 - 메소드 또는 필드 선언
    public String getTime() {
        return new java.util.Date().toString();
    }
    %>
    
    <p>현재 시간: <%= getTime() %></p>
    <p>요청 파라미터: ${param.name}</p> <!-- EL 사용 -->
</body>
</html>
```


## 3. Jakarta Enterprise Beans (EJB)

### 개요
EJB는 분산 비즈니스 컴포넌트를 구현하기 위한 서버 측 컴포넌트 아키텍처입니다. 트랜잭션 관리, 보안, 동시성 제어 등 엔터프라이즈 애플리케이션의 핵심 인프라 기능을 제공합니다.

### EJB 유형
1. **세션 빈(Session Beans)**
   - **상태 유지 세션 빈(Stateful)**: 클라이언트 상태를 유지
   - **상태 비유지 세션 빈(Stateless)**: 클라이언트 상태를 유지하지 않음
   - **싱글톤 세션 빈(Singleton)**: 애플리케이션당 하나의 인스턴스만 존재

2. **메시지 구동 빈(Message-Driven Beans, MDB)**
   - 비동기 메시징을 처리하기 위한 컴포넌트
   - JMS(Java Messaging Service)와 통합

### 코드 예제
```java
// Stateless Session Bean 예제
import jakarta.ejb.*;

@Stateless
public class UserServiceBean implements UserService {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User createUser(User user) {
        em.persist(user);
        return user;
    }
}

// Message-Driven Bean 예제
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", 
                                  propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", 
                                  propertyValue = "myQueue")
    }
)
public class OrderProcessorMDB implements MessageListener {
    @Inject
    private OrderService orderService;
    
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String orderId = textMessage.getText();
            orderService.processOrder(orderId);
        } catch (JMSException e) {
            // 예외 처리
        }
    }
}
```


## 4. Jakarta Persistence API (JPA)

### 개요
JPA는 자바 객체와 관계형 데이터베이스 간의 매핑을 위한 객체 관계형 매핑(ORM) 표준입니다. SQL 코드 감소, 객체 지향적 접근, 데이터베이스 독립성 등의 이점을 제공합니다.

### 주요 구성 요소

#### 1. 엔티티(Entity)
데이터베이스 테이블에 매핑되는 자바 클래스입니다.

#### 2. 엔티티 매니저(EntityManager)
엔티티의 영속성을 관리하는 인터페이스입니다.

#### 3. 영속성 컨텍스트(Persistence Context)
엔티티 매니저를 통해 관리되는 엔티티 인스턴스의 집합입니다.

#### 4. 영속성 유닛(Persistence Unit)
엔티티 클래스 집합과 관련 구성을 정의합니다.

### 주요 어노테이션

#### 엔티티 매핑
- **@Entity**: 클래스를 엔티티로 지정
- **@Table**: 테이블 이름, 스키마 등 지정
- **@Id**: 기본 키 필드 지정
- **@GeneratedValue**: 기본 키 생성 전략 지정
- **@Column**: 열 이름, 제약조건 등 지정
- **@Temporal**: 날짜/시간 타입 매핑
- **@Enumerated**: enum 타입 매핑
- **@Lob**: Large Object 매핑

#### 관계 매핑
- **@OneToOne**: 일대일 관계
- **@OneToMany**: 일대다 관계
- **@ManyToOne**: 다대일 관계
- **@ManyToMany**: 다대다 관계
- **@JoinColumn**: 외래 키 열 지정
- **@JoinTable**: 조인 테이블 지정

#### 상속 매핑
- **@Inheritance**: 상속 전략 지정
- **@MappedSuperclass**: 매핑 정보만 상속
- **@DiscriminatorColumn**: 구분 열 지정
- **@DiscriminatorValue**: 구분 값 지정


## 8. Jakarta Bean Validation

Jakarta Bean Validation은 Java 객체의 유효성을 검증하기 위한 표준 API입니다.

### 주요 특징
- **애너테이션 기반 검증**: `@NotNull`, `@Size`, `@Min`, `@Max` 등 다양한 제약조건 애너테이션 제공
- **객체 그래프 검증**: 중첩된 객체들에 대한 유효성 검증 지원
- **커스텀 검증 로직**: 사용자 정의 애너테이션과 검증기 생성 가능
- **메시지 국제화**: 다국어 오류 메시지 지원
- **그룹 검증**: 검증 규칙을 그룹별로 적용 가능

### 사용 예시
```java
public class User {
    @NotNull(message = "이름은 필수 항목입니다")
    private String name;
    
    @Email(message = "유효한 이메일 주소를 입력해주세요")
    private String email;
    
    @Min(value = 18, message = "나이는 18세 이상이어야 합니다")
    private int age;
    
    // Getters and setters
}
```
## 5. Jakarta Contexts and Dependency Injection (CDI)

### 개요
CDI는 Java EE 애플리케이션에서 의존성 주입과 컨텍스트 관리를 위한 표준 프레임워크입니다. 느슨하게 결합된 컴포넌트를 개발하고 테스트하기 쉬운 애플리케이션을 구축할 수 있게 해줍니다.

### 주요 개념

#### 1. 빈(Bean)
CDI 빈은 CDI 컨테이너에 의해 관리되는 객체입니다. 특별한 어노테이션이 없어도 기본 생성자가 있는 클래스는 빈이 될 수 있습니다.

#### 2. 스코프(Scope)
빈의 생명주기를 정의합니다:
- **@RequestScoped**: HTTP 요청 동안 유지
- **@SessionScoped**: 사용자 세션 동안 유지
- **@ApplicationScoped**: 애플리케이션 전체 생명주기 동안 유지
- **@ConversationScoped**: 프로그래밍 방식으로 정의된 대화 동안 유지
- **@Dependent**: 주입된 빈의 스코프를 따름

#### 3. 한정자(Qualifier)
동일한 타입의 여러 빈을 구분하기 위해 사용합니다.

#### 4. 프로듀서(Producer)
자바 객체를 CDI 빈으로 노출시키는 메서드입니다.

#### 5. 이벤트(Event)
빈 간의 느슨한 결합을 유지하면서 통신할 수 있게 해주는 메커니즘입니다.

#### 6. 인터셉터(Interceptor)와 데코레이터(Decorator)
횡단 관심사(로깅, 보안, 트랜잭션 등)를 구현하는 방법을 제공합니다.

### 코드 예시

#### 기본 의존성 주입
```java
import jakarta.inject.Inject;
import jakarta.enterprise.context.RequestScoped;

// 인터페이스
public interface MessageService {
    String getMessage();
}

// 구현체
@RequestScoped
public class SimpleMessageService implements MessageService {
    @Override
    public String getMessage() {
        return "안녕하세요, CDI!";
    }
}

// 의존성 주입을 사용하는 클래스
@RequestScoped
public class MessageController {
    private MessageService messageService;
    
    // 생성자 주입
    @Inject
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    
    public String displayMessage() {
        return messageService.getMessage();
    }
}
```

#### 한정자 사용
```java
import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// 한정자 정의
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface Premium {
}

// 한정자가 없는 기본 구현
@RequestScoped
public class StandardMessageService implements MessageService {
    @Override
    public String getMessage() {
        return "일반 메시지 서비스입니다.";
    }
}

// 한정자가 있는 구현
@Premium
@RequestScoped
public class PremiumMessageService implements MessageService {
    @Override
    public String getMessage() {
        return "프리미엄 메시지 서비스입니다.";
    }
}

// 한정자로 특정 구현체 주입
@RequestScoped
public class AdvancedController {
    @Inject
    private MessageService standardService; // 기본 구현 주입
    
    @Inject @Premium
    private MessageService premiumService; // 프리미엄 구현 주입
    
    public String displayMessages() {
        return "표준: " + standardService.getMessage() + 
               "\n프리미엄: " + premiumService.getMessage();
    }
}
```

#### 프로듀서 메서드
```java
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

public class ConfigurationProducer {
    @Produces
    @ApplicationScoped
    public Database produceDatabase() {
        Database db = new Database();
        db.setUrl("jdbc:mysql://localhost:3306/mydb");
        db.setUsername("root");
        db.setPassword("password");
        return db;
    }
    
    @Produces
    @Named("applicationName")
    public String getApplicationName() {
        return "My CDI Application";
    }
}
```

#### 이벤트 사용
```java
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Event;

// 이벤트 클래스
public class UserRegisteredEvent {
    private String username;
    
    public UserRegisteredEvent(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }
}

// 이벤트 발행자
@ApplicationScoped
public class UserService {
    @Inject
    private Event<UserRegisteredEvent> userRegisteredEvent;
    
    public void registerUser(String username, String password) {
        // 사용자 등록 로직
        System.out.println("사용자 등록: " + username);
        
        // 이벤트 발행
        userRegisteredEvent.fire(new UserRegisteredEvent(username));
    }
}

// 이벤트 구독자
@ApplicationScoped
public class EmailService {
    public void sendWelcomeEmail(@Observes UserRegisteredEvent event) {
        System.out.println(event.getUsername() + "에게 환영 이메일 발송");
        // 이메일 발송 로직
    }
}

@ApplicationScoped
public class NotificationService {
    public void notifyAdmin(@Observes UserRegisteredEvent event) {
        System.out.println("관리자에게 알림: " + event.getUsername() + " 사용자가 등록됨");
        // 관리자 알림 로직
    }
}
```

### 구현체
- **Weld**: 참조 구현체 (Red Hat)
- **OpenWebBeans**: Apache의 CDI 구현체

## 6. Jakarta RESTful Web Services (JAX-RS)

### 개요
JAX-RS는 RESTful 웹 서비스를 개발하기 위한 API입니다. HTTP 메서드(GET, POST, PUT, DELETE 등)와 URI 경로를 Java 메서드에 매핑하여 REST 서비스를 쉽게 구현할 수 있게 해줍니다.

### 주요 특징
- **어노테이션 기반**: `@Path`, `@GET`, `@POST` 등의 어노테이션으로 선언적 개발
- **컨텐츠 협상**: JSON, XML 등 다양한 미디어 타입 지원
- **파라미터 바인딩**: 경로, 쿼리, 매트릭스 파라미터 등 자동 바인딩
- **필터와 인터셉터**: 요청/응답 처리 과정에 추가 로직 적용 가능
- **비동기 처리**: 비동기 요청 처리 지원

### 코드 예제
```java
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.Inject;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    private UserService userService;
    
    @GET
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
    
    @POST
    public Response createUser(User user, @Context UriInfo uriInfo) {
        User created = userService.create(user);
        URI uri = uriInfo.getAbsolutePathBuilder()
                        .path(String.valueOf(created.getId()))
                        .build();
        return Response.created(uri).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public User updateUser(@PathParam("id") Long id, User user) {
        user.setId(id);
        return userService.update(user);
    }
    
    @DELETE
    @Path("/{id}")
    public void deleteUser(@PathParam("id") Long id) {
        userService.delete(id);
    }
    
    @GET
    @Path("/search")
    public List<User> searchUsers(@QueryParam("name") String name) {
        return userService.findByName(name);
    }
}
```

## 7. Jakarta Bean Validation

### 개요
Bean Validation은 Java 객체에 대한 제약 조건을 선언적으로 정의하고 검증하기 위한 API입니다. 어노테이션을 사용하여 필드, 메서드, 생성자 등에 제약 조건을 지정할 수 있습니다.

### 주요 어노테이션
- **@NotNull**: null이 아님
- **@NotEmpty**: null이 아니며 비어있지 않음
- **@NotBlank**: null이 아니며 공백이 아님
- **@Size**: 문자열 또는 컬렉션의 크기 제한
- **@Min/@Max**: 숫자의 최소/최대값 제한
- **@Pattern**: 정규식 패턴 매칭
- **@Email**: 이메일 형식 검증
- **@Past/@Future**: 날짜가 과거/미래인지 검증

### 코드 예제
```java
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class ValidationExample {
    
    // 모델 클래스
    public static class User {
        @NotNull(message = "ID는 필수입니다")
        private Long id;
        
        @NotBlank(message = "이름은 공백일 수 없습니다")
        @Size(min = 2, max = 30, message = "이름은 2~30자 사이여야 합니다")
        private String name;
        
        @NotNull
        @Email(message = "유효한 이메일 주소여야 합니다")
        private String email;
        
        @Min(value = 0, message = "나이는 0보다 커야 합니다")
        @Max(value = 150, message = "나이는 150보다 작아야 합니다")
        private int age;
        
        @Past(message = "생년월일은 과거 날짜여야 합니다")
        private LocalDate birthDate;
        
        @Valid  // 중첩된 객체 검증
        private Address address;
        
        // getters, setters 생략
    }
    
    public static class Address {
        @NotBlank(message = "도시는 필수입니다")
        private String city;
        
        @NotBlank(message = "우편번호는 필수입니다")
        @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다")
        private String zipCode;
        
        // getters, setters 생략
    }
    
    public static void main(String[] args) {
        // 검증기 생성
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        // 검증할 객체 생성
        User user = new User();
        user.setName("");  // 잘못된 값
        user.setEmail("invalid-email");  // 잘못된 값
        
        // 검증 실행
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // 검증 결과 출력
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
        }
    }
}
```



## 8. Jakarta Transactions (JTA)

Jakarta Transactions(JTA)는 분산 트랜잭션을 관리하기 위한 API입니다.

### 주요 특징
- **분산 트랜잭션 관리**: 여러 자원에 걸친 트랜잭션 조정
- **XA 프로토콜 지원**: 분산 트랜잭션을 위한 2단계 커밋 프로토콜 지원
- **선언적 트랜잭션**: 애너테이션 기반 트랜잭션 관리
- **프로그래밍 방식 트랜잭션**: 코드로 직접 트랜잭션 관리 가능
- **롤백 처리**: 오류 발생 시 자동 롤백 메커니즘

### 사용 예시
```java
@Transactional
public void transferMoney(Account from, Account to, BigDecimal amount) {
    from.withdraw(amount);
    to.deposit(amount);
}
```

## 9. Jakarta JSON Processing & Binding

Jakarta JSON Processing & Binding은 JSON 데이터를 처리하고 Java 객체로 변환하기 위한 API입니다.

### JSON Processing
- **JSON 파싱 및 생성**: JSON 문서의 저수준 처리
- **DOM 스타일 API (JsonObject, JsonArray)**: 트리 구조로 JSON 처리
- **스트리밍 API (JsonParser, JsonGenerator)**: 이벤트 기반 JSON 처리

### JSON Binding (JSON-B)
- **객체-JSON 매핑**: Java 객체와 JSON 간 자동 변환
- **커스터마이징**: 매핑 규칙 커스터마이징 지원
- **애너테이션 기반 설정**: `@JsonbProperty`, `@JsonbDateFormat` 등

### 사용 예시
```java
// JSON Processing
JsonObject jsonObject = Json.createObjectBuilder()
    .add("name", "홍길동")
    .add("age", 30)
    .build();

// JSON Binding
Jsonb jsonb = JsonbBuilder.create();
User user = jsonb.fromJson("{\"name\":\"홍길동\",\"age\":30}", User.class);
String json = jsonb.toJson(user);
```

## 10. Jakarta WebSocket

Jakarta WebSocket은 웹 애플리케이션에서 클라이언트와 서버 간 양방향 실시간 통신을 제공하는 API입니다.

### 주요 특징
- **전이중 통신**: 서버와 클라이언트 간 동시 양방향 통신
- **낮은 지연시간**: HTTP보다 오버헤드가 적어 실시간 애플리케이션에 적합
- **애너테이션 기반 엔드포인트**: `@ServerEndpoint`, `@ClientEndpoint` 등
- **메시지 핸들러**: `@OnMessage`, `@OnOpen`, `@OnClose`, `@OnError`
- **세션 관리**: 클라이언트 연결 세션 추적 및 관리

### 사용 예시
```java
@ServerEndpoint("/chat/{room}")
public class ChatEndpoint {
    
    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room) {
        // 연결 시 처리 로직
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        // 메시지 수신 시 처리 로직
    }
    
    @OnClose
    public void onClose(Session session) {
        // 연결 종료 시 처리 로직
    }
}
```

## 11. Jakarta Security

### 개요
Jakarta Security는 애플리케이션의 인증 및 권한 부여 기능을 제공하는 API입니다. 표준화된 인증 메커니즘을 제공하여 보안 관리를 용이하게 합니다.

### 주요 기능
- **인증(Authentication)**: 사용자의 신원을 확인하는 기능 제공
- **권한 부여(Authorization)**: 특정 리소스 접근을 제한하는 기능
- **Identity Store**: 데이터베이스 또는 LDAP 기반 사용자 정보 저장소 지원

### 코드 예제
```java
import jakarta.security.enterprise.authentication.mechanism.http.*;
import jakarta.security.enterprise.identitystore.*;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@BasicAuthenticationMechanismDefinition(realmName="exampleRealm")
@ApplicationPath("/api")
public class SecurityConfig extends Application {
}
```

