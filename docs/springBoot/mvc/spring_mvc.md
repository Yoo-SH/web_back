# Spring MVC 개요

Spring MVC는 Java 기반 웹 애플리케이션을 개발하기 위한 프레임워크로, Spring Framework의 일부입니다. Model-View-Controller(MVC) 디자인 패턴을 기반으로 하여 애플리케이션의 관심사를 분리하고 유지보수성을 높이는 데 도움을 줍니다.

## 목차
- [주요 특징](#주요-특징)
- [아키텍처 개요](#아키텍처-개요)
- [주요 어노테이션 정리](#주요-어노테이션-정리)
- [데이터 바인딩](#데이터-바인딩)
- [예제 코드](#예제-코드)
- [Spring MVC 설정 방법](#spring-mvc-설정-방법)
- [결론](#결론)

## 주요 특징
- **MVC 아키텍처 기반**: Model, View, Controller를 명확하게 분리하여 유지보수성과 확장성을 향상
- **유연한 컨트롤러**: `@Controller`, `@RestController` 등을 사용하여 HTTP 요청을 효과적으로 처리
- **강력한 데이터 바인딩**: `@RequestParam`, `@ModelAttribute`, `@PathVariable` 등을 이용하여 클라이언트 입력을 자동 변환
- **유효성 검사 지원**: `@Valid`, `@Validated` 어노테이션을 이용한 검증 기능 제공
- **다양한 뷰 기술 지원**: JSP, Thymeleaf, Freemarker 등의 다양한 템플릿 엔진 지원
- **RESTful API 지원**: `@RestController` 및 JSON 변환 기능을 활용하여 RESTful API 개발 가능
- **Interceptor 및 Filter 지원**: 요청 전후 처리 기능 제공
- **국제화(i18n) 지원**: 다국어 지원을 위한 MessageSource 제공



## 아키텍처 개요
Spring MVC는 다음과 같은 주요 구성 요소로 이루어져 있습니다.

### 1. DispatcherServlet
- 모든 요청을 중앙에서 처리하는 프론트 컨트롤러
- 요청을 적절한 컨트롤러로 라우팅
- 뷰 리졸버(ViewResolver)를 통해 적절한 View 반환

### 2. Controller
- 사용자의 요청을 처리하는 핵심 컴포넌트
- `@Controller` 또는 `@RestController` 어노테이션을 사용하여 정의
- 요청 매핑은 `@RequestMapping` 또는 `@GetMapping`, `@PostMapping` 등을 사용하여 설정

### 3. Model
- 데이터 및 비즈니스 로직을 담당하는 부분
- `Model` 객체를 사용하여 View로 데이터 전달 가능
- DTO 및 VO 객체를 활용하여 데이터 구조화 가능

### 4. View
- 사용자에게 출력되는 화면 부분
- JSP, Thymeleaf, Mustache, FreeMarker 등의 템플릿 엔진과 연동 가능
- `Model` 객체를 통해 데이터를 뷰에 전달

### 5. Interceptor
- 요청 전/후에 추가적인 처리를 수행하는 컴포넌트
- 인증, 로깅, 공통 데이터 설정 등에 사용
- `HandlerInterceptor` 인터페이스 구현하여 커스텀 인터셉터 작성 가능



## 주요 어노테이션 정리
Spring MVC에서는 다양한 어노테이션을 활용하여 애플리케이션을 개발할 수 있습니다.

### 1. 요청 매핑 관련 어노테이션
| 어노테이션               | 설명 |
|---------------------|--|
| `@RequestMapping`   | 특정 URL과 메서드를 매핑 |
| `@GetMapping`       | HTTP GET 요청을 처리 |
| `@PostMapping`      | HTTP POST 요청을 처리 |
| `@PutMapping`       | HTTP PUT 요청을 처리 |
| `@DeleteMapping`    | HTTP DELETE 요청을 처리 |

### 2. 데이터 바인딩 및 전달
| 어노테이션             | 설명 |
|-------------------|--|
| `@RequestParam`   | 쿼리 파라미터 값을 바인딩 |
| `@PathVariable`   | URL 경로 변수 값을 바인딩 |
| `@ModelAttribute` | 폼 데이터를 객체로 매핑 |
| `@RequestBody`    | JSON 요청 데이터를 객체로 변환 |

### 3. 응답 처리
| 어노테이션             | 설명 |
|-------------------|--|
| `@ResponseBody`   | 메서드 반환 값을 HTTP 응답 본문으로 사용 |
| `@RestController` | `@Controller` + `@ResponseBody` 역할 수행 |

### 4. 유효성 검사
| 어노테이션        | 설명 |
|--------------|--|
| `@Valid`     | Java Bean Validation을 수행 |
| `@Validated` | Spring의 유효성 검사를 수행 |

### 5. 기타
| 어노테이션               | 설명 |
|---------------------|--|
| `@ExceptionHandler` | 특정 예외 발생 시 예외 처리 메서드 실행 |
| `@ControllerAdvice` | 전역 예외 처리 및 공통 데이터 설정 |



## 데이터 바인딩
Spring MVC에서는 사용자의 입력 데이터를 컨트롤러에서 자동으로 변환하여 사용할 수 있도록 다양한 데이터 바인딩 기능을 제공합니다.

### 1. `@RequestParam`
- 요청 파라미터 값을 개별 변수에 바인딩합니다.
- 사용 예:
```java
@GetMapping("/user")
public String getUser(@RequestParam("name") String name) {
    return "User Name: " + name;
}
```

### 2. `@PathVariable`
- URL 경로 변수 값을 바인딩합니다.
- 사용 예:
```java
@GetMapping("/user/{id}")
public String getUserById(@PathVariable("id") Long userId) {
    return "User ID: " + userId;
}
```

### 3. `@ModelAttribute`
- 폼 데이터를 객체로 변환하여 바인딩합니다.
- 사용 예:
```java
@PostMapping("/user")
public String createUser(@ModelAttribute User user) {
    return "User Created: " + user.getName();
}
```

### 4. `@RequestBody`
- JSON 요청 데이터를 객체로 변환하여 바인딩합니다.
- 사용 예:
```java
@PostMapping("/user")
public String createUser(@RequestBody User user) {
    return "User Created: " + user.getName();
}
```


## 예제 코드

### 1. model(User.java)
```java
public class User {
    private Long id;
    private String name;

    public User() {}

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

### 2. Controller (UserController.java)
```java
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        User user = new User(id, "John Doe");
        model.addAttribute("user", user);
        return "user-view";
    }
}
```

### 3. View (user-view.html - Thymeleaf 예제)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>User Info</title>
</head>
<body>
    <h1>User Information</h1>
    <p>ID: <span th:text="${user.id}"></span></p>
    <p>Name: <span th:text="${user.name}"></span></p>
</body>
</html>
```



## Spring MVC 설정 방법
### 1. 의존성 추가 (Maven)
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### 2. Spring Boot 애플리케이션 설정
```java
@SpringBootApplication
public class SpringMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringMvcApplication.class, args);
    }
}
```



## 결론
Spring MVC는 강력한 웹 프레임워크로서, MVC 패턴을 기반으로 웹 애플리케이션을 구조화하는 데 도움을 줍니다. 다양한 어노테이션과 기능을 활용하여 유지보수성과 확장성이 뛰어난 애플리케이션을 개발할 수 있습니다.


