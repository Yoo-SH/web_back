# Spring Lombok 완벽 가이드

## 목차
- [소개](#소개)
- [주요 기능](#주요-기능)
- [설치 방법](#설치-방법)
- [자주 사용되는 어노테이션](#자주-사용되는-어노테이션)
- [모범 사례](#모범-사례)
- [주의사항](#주의사항)
- [팁과 트릭](#팁과-트릭)

## 소개

Project Lombok은 Java 기반 프로젝트에서 반복적인 코드 작성을 줄여주는 라이브러리입니다. 컴파일 시점에 특정 어노테이션을 처리하여 소스코드를 자동으로 생성해주는 방식으로 동작합니다.

### 주요 장점
- 보일러플레이트 코드 감소
- 코드의 가독성 향상
- 생산성 증가
- 버그 발생 가능성 감소

## 설치 방법

### Maven
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <scope>provided</scope>
</dependency>
```

### Gradle
```groovy
compileOnly 'org.projectlombok:lombok:1.18.30'
annotationProcessor 'org.projectlombok:lombok:1.18.30'
```

### IDE 플러그인 설치
- IntelliJ IDEA: Lombok Plugin 설치
- Eclipse: lombok.jar 실행 후 IDE 설정

## 자주 사용되는 어노테이션

### 1. @Getter/@Setter
- 클래스의 모든 필드에 대한 getter/setter 메서드 자동 생성
```java
@Getter @Setter
public class User {
    private String username;
    private String email;
}
```

### 2. @ToString
- toString() 메서드 자동 생성
- exclude 속성으로 특정 필드 제외 가능
```java
@ToString(exclude = "password")
public class User {
    private String username;
    private String password;
}
```

### 3. @EqualsAndHashCode
- equals()와 hashCode() 메서드 자동 생성
```java
@EqualsAndHashCode(callSuper = false)
public class Employee {
    private Long id;
    private String name;
}
```

### 4. @NoArgsConstructor, @AllArgsConstructor, @RequiredArgsConstructor
- 다양한 형태의 생성자 자동 생성
```java
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Product {
    @NonNull private String name;
    private Double price;
}
```

### 5. @Data
- @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 모두 포함
```java
@Data
public class Order {
    private Long id;
    private String customerName;
    private LocalDateTime orderDate;
}
```

### 6. @Builder
- 빌더 패턴 구현을 자동화
```java
@Builder
public class Post {
    private String title;
    private String content;
    private String author;
}
```

### 7. @Slf4j
- Logger 객체 자동 생성
```java
@Slf4j
public class UserService {
    public void doSomething() {
        log.info("Method executed");
    }
}
```

## 모범 사례

### 1. @Data 사용 시 주의사항
- JPA 엔티티에는 @Data 사용을 피하고, 개별 어노테이션 사용 권장
- toString()에서 순환 참조 주의

### 2. @Builder 활용
```java
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
}
```

### 3. 불변 객체 생성
```java
@Value
public class ImmutableUser {
    String username;
    String email;
}
```

## 주의사항

### 1. JPA 엔티티 사용 시
- @EqualsAndHashCode 사용 시 ID 필드만 포함
- @ToString 순환 참조 주의
- @Builder 사용 시 기본 생성자 필요

### 2. 성능 고려사항
- @ToString 사용 시 대용량 컬렉션 제외
- 불필요한 getter/setter 생성 피하기

### 3. 디버깅
- 생성된 코드 확인 방법
- IDE 지원 확인

## 팁과 트릭

### 1. 커스텀 어노테이션 조합
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Getter
@Setter
@ToString
public @interface Value { }
```

### 2. 로깅 활용
```java
@Slf4j
public class ServiceClass {
    public void method() {
        log.debug("디버그 로그");
        log.info("정보 로그");
        log.warn("경고 로그");
        log.error("에러 로그");
    }
}
```

### 3. 생성자 자동화
```java
@RequiredArgsConstructor(staticName = "of")
public class Factory {
    private final String name;
    private final int capacity;
}
```

## 참고 자료
- [Project Lombok 공식 문서](https://projectlombok.org/features/all)
- [Spring Boot with Lombok](https://spring.io/blog/2018/12/12/spring-boot-2-1-2-available-now)
- [Lombok GitHub](https://github.com/rzwitserloot/lombok)