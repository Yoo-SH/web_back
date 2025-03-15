# Spring MVC 개요

Spring MVC는 Java 기반 웹 애플리케이션을 개발하기 위한 프레임워크로, Spring Framework의 일부입니다. Model-View-Controller(MVC) 디자인 패턴을 기반으로 하여 애플리케이션의 관심사를 분리하고 유지보수성을 높이는 데 도움을 줍니다.

__MVC pattern(model, view, controller)__
<Image src= "https://github.com/user-attachments/assets/68cbccef-d583-48c3-ae9f-be308265c679" width=500px>



## 목차
- [주요 특징](#주요-특징)
- [아키텍처 개요](#아키텍처-개요)
- [주요 어노테이션 정리](#주요-어노테이션-정리)
- [데이터 바인딩](#데이터-바인딩)
- [예제 코드](#예제-코드)
- [Spring MVC 설정 방법](#spring-mvc-설정-방법)
- [MVC -> MVCS](#mvc---mvcs)

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
- 모델과 뷰 사이를 이어주는 브릿지(bridge)역할을 수행
- 사용자의 요청을 처리하는 핵심 컴포넌트
- `@Controller` 또는 `@RestController` 어노테이션을 사용하여 정의
- 요청 매핑은 `@RequestMapping` 또는 `@GetMapping`, `@PostMapping` 등을 사용하여 설정
- 사용자가 접근하려는 URL에 따라 요청을 파악한 후, 그 요청에 맞는 Model을 의뢰하고, 데이터를 View에 반영하여 사용자에게 알려 줌
- 컨트롤러는 2가지 규칙을 갖고 설계!
    - 모델이나 뷰에 대해서 알고 있어야 합니다.
    - 모델이나 뷰의 변경을 모니터링 해야 합니다

### 3. Model
- 데이터 및 비즈니스 로직을 담당하는 부분(Controller에서 명령을 받고 Database에서 데이터를 저장하거나 삭제/업데이트/변환 등의 작업
을 수행!)
- 비즈니스 로직을 처리한 후 모델의 변경 사항을 컨트롤러와 뷰에 전달
- `Model` 객체를 사용하여 View로 데이터 전달 가능
- DTO 및 VO 객체를 활용하여 데이터 구조화 가능
- 모델은 3가지 규칙을 갖고 설계해야 한다.
    - 사용자가 편집하길 원하는 모든 데이터를 갖고 있어야 한다
    - View나 Controller에 대해서 어떤 정보도 알지 말아야 한다.
    - 변경이 발생하면, 변경 통지에 대한 처리 방법을 구현해야 한다.

### 4. View
- 사용자에게 출력되는 화면 부분
- JSP, Thymeleaf, Mustache, FreeMarker 등의 템플릿 엔진과 연동 가능
- `Model` 객체를 통해 데이터를 뷰에 전달
- View는 3가지 규칙을 갖고 설계!
    - 모델이 가지고 있는 정보를 따로 저장해서는 안됨
    - 모델이나 컨트롤러와 같이 다른 구성 요소들을 몰라야 함.
    - 변경이 일어나면 변경통지에 대한 처리방법을 구현해야만 함.

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


## MVC -> MVCS


- 비즈니스 로직이 복잡한 경우
    - Controller가 너무 많은 역할을 하면 유지보수가 어려워지므로 Service를 분리하는 것이 유리함.
- 대규모 프로젝트에서 유지보수가 중요한 경우
    - Service 레이어를 활용하여 코드의 가독성을 높이고, 역할을 분리하면 유지보수가 쉬워짐.
- 재사용 가능한 비즈니스 로직이 있는 경우
    - 여러 Controller에서 동일한 로직을 사용할 경우 Service 계층을 만들어 재사용 가능.



__MVCS pattern(model,view,controller,service)__
<Image src ="https://github.com/user-attachments/assets/4d0cbb62-13b5-4e7c-8397-cddc7b1d47e9" width=500px>


### Service?

1) 웹사이트 URL로 접근하여 정보를 요청(버튼클릭,글 작성 등)
2) Controller에서 요청 정보를 받고, Service를 호출
3) Service에서 DAO를 호출하여 DB에 접근
4) Service의 작업이 완료된 후 Service를 호출했던 Controller로 돌아 옴
5) Controller는 데이터를 View에 전달
6) View에서 사용자에게 최종적으로 보여줄 화면을 생성


<Image src ="https://github.com/user-attachments/assets/dbc632af-9512-4c86-b861-f04cc11f2428" width=500px>

### DAO?

- 데이터베이스에 데이터를 저장,수정,검색,삭제를 하기 위해서 만들어지는 모델
- 데이터를 하나로 묶어야 될 경우 데이터를 하나로 수집하는 역할을 하는 바구니가 필요..!
- CRUD 동작을 가지고있는 클래스
- 비지니스 로직을 처리하는 클래스

