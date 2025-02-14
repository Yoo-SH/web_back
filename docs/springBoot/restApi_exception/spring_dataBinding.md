# Spring Data Binding & Jackson Guide

## 목차
- [개요](#개요)
- [Spring Data Binding](#spring-data-binding)
- [JSON to Java POJO](#json-to-java-pojo)
- [Jackson Library](#jackson-library)
- [실전 예제](#실전-예제)
- [Best Practices](#best-practices)

## 개요
Spring Data Binding은 HTTP 요청 파라미터나 JSON 데이터를 Java 객체로 변환하는 메커니즘을 제공합니다. Jackson은 이러한 JSON 데이터 바인딩을 처리하는 핵심 라이브러리입니다.

## Spring Data Binding

### 기본 개념
Spring Data Binding은 다음과 같은 주요 기능을 제공합니다:
- 요청 파라미터를 Java 객체로 자동 매핑
- JSON/XML 데이터를 Java 객체로 변환
- 중첩된 객체 구조 지원
- 커스텀 타입 변환 지원

### 동작 방식
1. HTTP 요청 수신
2. ContentType 확인
3. 적절한 MessageConverter 선택
4. Java 객체로 변환

## JSON to Java POJO

### POJO (Plain Old Java Object)
```java
public class User {
    private Long id;
    private String name;
    private String email;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

### Controller 예시
```java
@RestController
@RequestMapping("/api")
public class UserController {
    
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // user 객체가 자동으로 JSON에서 변환됨
        return ResponseEntity.ok(user);
    }
}
```

## Jackson Library

### 주요 기능
- JSON 직렬화/역직렬화
- 다양한 애노테이션 지원
- 커스텀 직렬화/역직렬화 지원
- 날짜/시간 형식 지원

### 주요 애노테이션
```java
public class User {
    @JsonProperty("user_id")     // JSON 필드명 매핑
    private Long id;
    
    @JsonFormat(pattern = "yyyy-MM-dd")  // 날짜 형식 지정
    private LocalDate birthDate;
    
    @JsonIgnore              // JSON 변환 제외
    private String password;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)  // null 값 제외
    private String optional;
}
```

### Configuration 설정
```java
@Configuration
public class JacksonConfig {
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
```

## 실전 예제

### 복잡한 객체 변환
```java
public class Order {
    private Long orderId;
    private List<OrderItem> items;
    private Customer customer;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
}

// JSON 예시
{
    "orderId": 1,
    "items": [
        {
            "productId": 100,
            "quantity": 2
        }
    ],
    "customer": {
        "id": 1,
        "name": "John Doe"
    },
    "orderDate": "2024-02-13 10:30:00"
}
```

## Best Practices

### 1. Null 처리
- `@JsonInclude` 사용하여 null 값 처리 정책 설정
- `Optional` 활용하여 null 안전성 확보

### 2. 예외 처리
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
        return ResponseEntity.badRequest().body("Invalid JSON format");
    }
}
```

### 3. 성능 최적화
- ObjectMapper는 Thread-safe하므로 싱글톤으로 관리
- 필요한 필드만 직렬화/역직렬화
- 커스텀 직렬화/역직렬화 구현 시 성능 고려

### 4. 보안 고려사항
- 민감한 데이터는 `@JsonIgnore` 사용
- 입력 데이터 검증 필수
- JSON 파싱 시 크기 제한 설정