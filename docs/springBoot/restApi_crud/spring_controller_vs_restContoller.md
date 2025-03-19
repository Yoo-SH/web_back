# Spring Boot @Controller vs @RestController

## 1. @Controller
`@Controller`는 Spring MVC에서 사용되는 애너테이션으로, 주로 **View를 반환**하는 데 사용됩니다.

### 특징
- 주로 **HTML 파일**을 반환할 때 사용
- `ViewResolver`를 통해 **템플릿 엔진(ex: Thymeleaf, JSP)과 함께 사용**
- `@ResponseBody`를 사용하면 **JSON 데이터를 반환**할 수도 있음

### 예제 코드
```java
@Controller
public class MyController {
    
    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello, Spring Boot!");
        return "hello"; // `hello.html` 템플릿 반환
    }
    
    @GetMapping("/json")
    @ResponseBody
    public String jsonResponse() {
        return "Hello, JSON!";
    }
}
```

## 2. @RestController
`@RestController`는 `@Controller` + `@ResponseBody`가 결합된 애너테이션으로, **RESTful 웹 서비스를 만들 때 사용**됩니다.

### 특징
- 모든 메서드가 기본적으로 `@ResponseBody`를 적용받아 **JSON 형식으로 응답**
- `ViewResolver` 대신 **`HttpMessageConverter`를 사용**하여 데이터를 변환
- RESTful API 개발에 적합

### 예제 코드
```java
@RestController
public class MyRestController {
    
    @GetMapping("/api/hello")
    public String apiHello() {
        return "Hello, REST API!";
    }
    
    @GetMapping("/api/user")
    public User getUser() {
        return new User("John Doe", 30);
    }
}

class User {
    private String name;
    private int age;
    
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Getter & Setter 생략
}
```

## 3. 차이점 정리
| 구분            | @Controller | @RestController |
|----------------|------------|----------------|
| 주요 목적       | View 반환  | JSON 데이터 반환 |
| `@ResponseBody` 기본 적용 | ❌ | ✅ |
| 템플릿 엔진 사용 | 가능 | 불가능 |
| API 응답 방식 | View 기반 (HTML) | JSON/XML 기반 |

## 4. 언제 사용할까?
✅ **`@Controller`를 사용할 때**
- HTML을 렌더링해야 하는 경우 (Thymeleaf, JSP 등과 함께 사용)
- 웹 애플리케이션의 View를 반환해야 하는 경우

✅ **`@RestController`를 사용할 때**
- RESTful API를 개발하는 경우
- 클라이언트(React, Vue, 모바일 앱 등)에서 JSON 데이터를 필요로 하는 경우

---

