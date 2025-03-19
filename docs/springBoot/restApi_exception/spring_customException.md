# 커스텀 Response Exception Handler

## 소개
이 문서는 애플리케이션에서 `responseExceptionHandler`를 커스터마이징하는 방법을 설명합니다. 커스터마이징 시 `message`, `status`, `code`를 포함하여 예외를 처리하는 방법을 다룹니다.

## 구현 방법

### 1. 커스텀 예외 클래스 정의
예외를 구조적으로 처리하기 위해 커스텀 예외 클래스를 생성합니다.

```java
public class CustomException extends RuntimeException {
    private final int status;
    private final String code;

    public CustomException(String message, int status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
```

### 2. 예외 핸들러 생성
`@ControllerAdvice`를 사용하여 전역 예외 핸들러를 구현합니다.

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getStatus(), ex.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatus()));
    }
    
    // 추가적인 예외 처리가 필요한 경우 여기에 작성
}
```

### 3. 오류 응답 DTO 정의
오류 응답을 구조화하기 위해 DTO 클래스를 생성합니다.

```java
public class ErrorResponse {
    private String message;
    private int status;
    private String code;

    public ErrorResponse(String message, int status, String code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
```

### 4. 서비스에서 커스텀 예외 발생시키기
오류가 발생했을 때 커스텀 예외를 발생시킵니다.

```java
throw new CustomException("리소스를 찾을 수 없습니다", 404, "NOT_FOUND");
```

## 사용 예시
예외 발생 시 다음과 같은 응답을 반환합니다.

```json
{
    "message": "리소스를 찾을 수 없습니다",
    "status": 404,
    "code": "NOT_FOUND"
}
```

## 결론
이 가이드는 `responseExceptionHandler`를 커스터마이징하여 `message`, `status`, `code`를 포함한 구조화된 오류 응답을 반환하는 방법을 설명합니다. 이를 통해 오류 처리를 개선하고 디버깅을 쉽게 할 수 있습니다.

