# Spring Framework 에러 처리 가이드

## 목차
- [개요](#개요)
- [주요 HTTP 상태 코드](#주요-http-상태-코드)
- [에러 처리 방법](#에러-처리-방법)
    - [ENUM을 활용한 처리](#enum을-활용한-처리)
    - [@ControllerAdvice 사용](#controlleradvice-사용)
    - [CustomException 정의](#customexception-정의)
    - [ErrorResponse 객체 생성](#errorresponse-객체-생성)
- [구현 예시](#구현-예시)
- [테스트 방법](#테스트-방법)

## 개요
Spring Framework에서는 다양한 방법으로 예외 처리를 할 수 있습니다. 이 문서에서는 REST API에서 자주 사용되는 HTTP 상태 코드(400, 401, 404 등)에 대한 처리 방법을 설명합니다.

## 주요 HTTP 상태 코드
- 400 (Bad Request): 잘못된 요청
- 401 (Unauthorized): 인증 실패
- 403 (Forbidden): 권한 없음
- 404 (Not Found): 리소스를 찾을 수 없음
- 500 (Internal Server Error): 서버 내부 오류

## 에러 처리 방법

### ENUM을 활용한 처리
ENUM을 사용하여 에러 코드와 메시지를 관리하면 일관된 에러 처리가 가능합니다.

```java
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    RESOURCE_NOT_FOUND(404, "C002", "Resource Not Found"),
    INTERNAL_SERVER_ERROR(500, "C003", "Server Error"),

    // User
    UNAUTHORIZED(401, "U001", "Unauthorized"),
    USER_NOT_FOUND(404, "U002", "User Not Found"),
    DUPLICATE_EMAIL(400, "U003", "Email is Duplicate"),

    // Business
    BUSINESS_EXCEPTION(400, "B001", "Business Exception");

    private final int status;
    private final String code;
    private final String message;
}

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String message;
    private String code;
    private int status;
    private List<FieldError> errors;
    private LocalDateTime timestamp;

    @Builder
    public ErrorResponse(ErrorCode code, List<FieldError> errors) {
        this.message = code.getMessage();
        this.code = code.getCode();
        this.status = code.getStatus();
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }
}

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code(e.getErrorCode())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorResponse.FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .value(error.getRejectedValue() == null ? "" : error.getRejectedValue().toString())
                        .reason(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.INVALID_INPUT_VALUE)
                .errors(fieldErrors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
```

사용 예시:
```java
@Service
public class UserService {
    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        // 사용자 생성 로직
    }
}
```

### @ControllerAdvice 사용
전역적인 예외 처리를 위해 `@ControllerAdvice` 어노테이션을 사용합니다.

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ErrorResponse response = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "유효성 검사 실패",
            ex.getBindingResult().getAllErrors()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            ResourceNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex) {
        ErrorResponse response = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
```

### CustomException 정의
각 상황에 맞는 커스텀 예외를 정의합니다.

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
```

### ErrorResponse 객체 생성
에러 응답을 위한 DTO를 정의합니다.

```java
@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
    private List<String> errors;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
```

## 구현 예시
컨트롤러에서의 사용 예시입니다.

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        // 유효성 검사 실패 시 자동으로 MethodArgumentNotValidException 발생
        User user = userService.createUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
```

## 테스트 방법
JUnit을 사용한 테스트 예시입니다.

```java
@SpringBootTest
@AutoConfigureMockMvc
public class ErrorHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenUserNotFound_thenReturns404() throws Exception {
        mockMvc.perform(get("/api/users/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.code").value("U002"))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void whenInvalidInput_thenReturns400() throws Exception {
        UserDTO invalidUser = new UserDTO(); // 필수 필드 누락

        mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(invalidUser)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code").value("C001"))
            .andExpect(jsonPath("$.errors").exists());
    }
}
```