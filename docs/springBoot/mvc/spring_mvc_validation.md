# Spring MVC Validation

Spring MVC에서 데이터 검증(Validation)은 사용자의 입력값을 유효성 검사하고, 오류가 있을 경우 적절한 처리를 수행하는 중요한 과정입니다. Spring에서는 `javax.validation` 패키지의 애노테이션과 `BindingResult` 객체를 활용하여 간편하게 검증을 수행할 수 있습니다.

## 목차
- [Validation 설정 방법](#1-validation-설정-방법)
  - [의존성 추가](#11-의존성-추가)
- [기본 검증 어노테이션](#2-기본-검증-어노테이션)
- [DTO 클래스에서 검증 적용](#3-dto-클래스에서-검증-적용)
- [Controller에서 검증 처리](#4-controller에서-검증-처리)
- [글로벌 예외 처리 (ExceptionHandler)](#5-글로벌-예외-처리-exceptionhandler)
- [커스텀 검증 애노테이션 만들기](#6-커스텀-검증-애노테이션-만들기)
  - [커스텀 애노테이션 생성](#61-커스텀-애노테이션-생성)
  - [검증 로직 구현](#62-검증-로직-구현)
  - [DTO에서 사용](#63-dto에서-사용)
- [결론](#7-결론)

## 1. Validation 설정 방법

### 1.1. 의존성 추가

Spring MVC에서 유효성 검사를 사용하려면 `spring-boot-starter-validation` 의존성을 추가해야 합니다.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```



## 2. 기본 검증 어노테이션

Spring MVC는 `javax.validation` 패키지의 다양한 검증 어노테이션을 제공합니다.

| 어노테이션        | 설명                                           |
|---|----------------------------------------------|
| `@NotNull`        | 값이 null이 아니어야 합니다.                           |
| `@NotEmpty`       | 값이 null이 아니고 비어있지 않아야 합니다.                   |
| `@NotBlank`       | 공백을 제외하고 값이 존재해야 합니다.                        |
| `@Size`           | 문자열의 길이를 제한합니다. (예: `@Size(min=2, max=10)`)  |
| `@Min` / `@Max`   | 최소, 최대 숫자 값을 설정합니다.                          |
| `@Pattern`        | 정규식을 이용한 패턴 검증을 수행합니다.                       |
| `@Email`          | 이메일 형식인지 검증합니다.                              |
| `@Past` / `@Future` | 과거/미래 날짜인지 검증합니다.                            |



## 3. DTO 클래스에서 검증 적용

```java
import jakarta.validation.constraints.*;

public class UserDTO {
    @NotNull(message = "이름은 필수 입력 값입니다.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    private String name;
    
    @Min(value = 18, message = "나이는 최소 18세 이상이어야 합니다.")
    @Max(value = 100, message = "나이는 최대 100세 이하이어야 합니다.")
    private int age;
    
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;
}
```



## 4. Controller에서 검증 처리

컨트롤러에서는 `@Valid` 또는 `@Validated` 애노테이션을 사용하여 DTO의 유효성을 검사할 수 있습니다. 검증 오류가 발생하면 `BindingResult` 객체를 통해 오류 정보를 확인할 수 있습니다.

```java
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().toString();
        }
        return "회원가입 성공";
    }
}
```



## 5. 글로벌 예외 처리 (ExceptionHandler)

컨트롤러에서 발생하는 `MethodArgumentNotValidException` 예외를 전역적으로 처리할 수 있습니다.

```java
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
```



## 6. 커스텀 검증 애노테이션 만들기

기본 제공 어노테이션 외에도 사용자가 직접 검증 애노테이션을 만들 수 있습니다.

### 6.1. 커스텀 애노테이션 생성

```java
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "올바른 전화번호 형식이 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### 6.2. 검증 로직 구현

```java
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches("^\\d{3}-\\d{3,4}-\\d{4}$");
    }
}
```

### 6.3. DTO에서 사용

```java
public class UserDTO {
    @ValidPhoneNumber
    private String phoneNumber;
}
```



## 7. 결론

Spring MVC의 Validation 기능을 활용하면 입력 데이터의 유효성을 쉽게 검증할 수 있습니다. 기본 제공 어노테이션을 사용할 수도 있고, 필요에 따라 커스텀 검증 로직을 구현할 수도 있습니다. 전역 예외 처리기를 활용하면 보다 깔끔하게 오류 메시지를 관리할 수 있습니다.

