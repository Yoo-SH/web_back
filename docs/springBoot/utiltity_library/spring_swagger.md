# Spring Boot에서 Swagger 사용법

## 목차
1. Swagger란?
2. Swagger의 주요 기능
3. Spring Boot에서 Swagger 설정하기
   - 의존성 추가
   - 기본 설정
4. Swagger UI 사용법
5. Swagger 설정 확장
   - API 문서 커스터마이징
   - JWT 인증 추가
6. 기본적인 REST API 예제
7. Swagger에서 에러 처리 예시
8. YAML 문서로 API 정의하기
9. 결론

---

## 1. Swagger란?
Swagger는 RESTful API 문서를 자동으로 생성해주는 도구로, OpenAPI Specification(OAS)을 기반으로 합니다. 이를 통해 API의 정의, 테스트, 관리가 용이해집니다.

## 2. Swagger의 주요 기능
- API 문서를 자동 생성
- API 요청 및 응답 테스트 기능 제공
- 다양한 프로그래밍 언어 지원
- API 버전 관리 가능

## 3. Spring Boot에서 Swagger 설정하기

### 3.1 의존성 추가
Spring Boot 프로젝트에 Swagger를 추가하려면 `springdoc-openapi` 의존성을 `pom.xml`에 추가합니다.

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 3.2 기본 설정
Swagger를 사용하려면 별도의 설정이 필요하지 않지만, API 정보를 추가하려면 다음과 같이 설정할 수 있습니다.

```java
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "Sample API", version = "1.0", description = "Spring Boot Swagger 예제")
)
public class SwaggerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerApplication.class, args);
    }
}
```

## 4. Swagger UI 사용법
Spring Boot에서 Swagger를 설정하면 기본적으로 `http://localhost:8080/swagger-ui.html` 에서 API 문서를 확인할 수 있습니다.

## 5. Swagger 설정 확장
### 5.1 API 문서 커스터마이징
Swagger 문서를 보다 상세하게 작성하려면 `@Operation`과 `@Parameter` 등의 애노테이션을 사용할 수 있습니다.

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.Parameter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hello")
    @Operation(summary = "Hello API", description = "간단한 인사말을 반환합니다.")
    public String hello(@Parameter(description = "사용자 이름") @RequestParam String name) {
        return "Hello, " + name;
    }
}
```

### 5.2 JWT 인증 추가
JWT 인증이 필요한 경우, Swagger에 인증 정보를 추가해야 합니다.

```java
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
```

## 6. 기본적인 REST API 예제
Swagger와 함께 사용할 수 있는 기본적인 REST API 예제입니다.

```java
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private final Map<Integer, String> users = new HashMap<>();
    
    @PostMapping("/users")
    @Operation(summary = "사용자 추가", description = "새로운 사용자를 추가합니다.")
    public String addUser(@RequestParam int id, @RequestParam String name) {
        users.put(id, name);
        return "User added: " + name;
    }
    
    @GetMapping("/users/{id}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다.")
    public String getUser(@PathVariable int id) {
        return users.getOrDefault(id, "User not found");
    }
}
```

## 7. Swagger에서 에러 처리 예시
Swagger에서 에러 응답을 정의하는 방법입니다.

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ErrorHandlingController {
    
    @GetMapping("/error-test")
    @Operation(summary = "에러 발생 예제", description = "400 에러를 발생시키는 예제입니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    public String errorTest(@RequestParam(required = false) String param) {
        if (param == null) {
            throw new IllegalArgumentException("파라미터가 필요합니다.");
        }
        return "정상 요청: " + param;
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
```

## 8. YAML 문서로 API 정의하기
Swagger 문서는 JSON뿐만 아니라 YAML 형식으로도 작성할 수 있습니다.

```yaml
openapi: 3.0.1
info:
  title: Sample API
  version: 1.0.0
  description: Spring Boot Swagger 예제
paths:
  /api/hello:
    get:
      summary: Hello API
      description: 간단한 인사말을 반환합니다.
      parameters:
        - name: name
          in: query
          description: 사용자 이름
          required: true
          schema:
            type: string
      responses:
        '200':
          description: 성공
          content:
            application/json:
              schema:
                type: string
```

## 9. 결론
Spring Boot에서 Swagger를 사용하면 API 문서를 자동으로 생성하고 관리할 수 있습니다. 이를 활용하여 API 개발 생산성을 향상시키고, 협업을 용이하게 할 수 있습니다.
