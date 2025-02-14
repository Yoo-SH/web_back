# Spring Exception Handling 완벽 가이드

## 목차
- [개요](#개요)
- [Exception Handling 방법](#exception-handling-방법)
- [Global Exception Handler](#global-exception-handler)
- [Custom Exception](#custom-exception)
- [Exception 응답 처리](#exception-응답-처리)
- [실전 예제](#실전-예제)
- [Best Practices](#best-practices)

## 개요

Spring Framework에서는 예외 처리를 위한 다양한 방법을 제공합니다. 이 가이드에서는 Spring에서 사용할 수 있는 모든 예외 처리 방법과 베스트 프랙티스를 다룹니다.

## Exception Handling 방법

### 1. @ExceptionHandler

특정 컨트롤러 내에서 발생하는 예외를 처리하는 방법입니다.

```java
@Controller
public class UserController {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
```

### 2. @ControllerAdvice / @RestControllerAdvice

전역적으로 예외를 처리하는 방법입니다.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

## Custom Exception

사용자 정의 예외 클래스 생성 방법입니다.

```java
public class CustomBusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    
    public CustomBusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

public enum ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    INVALID_INPUT("잘못된 입력입니다.");
    
    private final String message;
    
    ErrorCode(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
```

## Exception 응답 처리

### ErrorResponse 클래스

```java
@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    
    // 생성자, 빌더 등
}
```

## 실전 예제

### 1. ValidationException 처리

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
            
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
                
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message(errors.toString())
                .path(request.getRequestURI())
                .build();
                
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
```

### 2. 비즈니스 예외 처리

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CustomBusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            CustomBusinessException ex,
            HttpServletRequest request) {
            
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getErrorCode().name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
                
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
```

## Best Practices

1. **계층별 예외 처리**
    - 컨트롤러 계층: @ExceptionHandler 사용
    - 서비스 계층: try-catch 또는 throws 사용
    - 레포지토리 계층: 구체적인 예외를 비즈니스 예외로 변환

2. **예외 계층 구조**
   ```
   BaseException
   ├── BusinessException
   │   ├── InvalidValueException
   │   └── EntityNotFoundException
   └── SystemException
       ├── DatabaseException
       └── ExternalApiException
   ```

3. **로깅 전략**
   ```java
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleException(Exception ex) {
       log.error("Unexpected error occurred", ex);
       // 사용자에게는 자세한 에러 정보를 숨기고 일반적인 메시지 전달
       return new ResponseEntity<>(
           new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
           HttpStatus.INTERNAL_SERVER_ERROR
       );
   }
   ```

4. **예외 처리 우선순위**
    - 구체적인 예외 처리를 먼저 작성
    - 일반적인 예외 처리는 나중에 작성

## 주의사항

1. 민감한 정보는 절대 클라이언트에게 노출하지 않습니다.
2. 예외 처리는 가능한 한 구체적으로 합니다.
3. 모든 예외는 적절히 로깅되어야 합니다.
4. 예외 응답 형식을 일관성 있게 유지합니다.

## 추가 팁

1. **Springboot Actuator**를 사용하여 예외 모니터링
2. **Spring AOP**를 활용한 예외 처리
3. **@ResponseStatus**를 활용한 HTTP 상태 코드 매핑
4. **@Valid**와 함께 사용하는 유효성 검사

이 문서는 지속적으로 업데이트될 수 있으며, 프로젝트의 요구사항에 맞게 수정하여 사용하시기 바랍니다.