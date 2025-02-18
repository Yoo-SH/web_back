# Spring MVC 완벽 가이드

## 목차
- [1. Spring MVC 아키텍처](#1-spring-mvc-아키텍처)
- [2. MVC 패턴 상세 구현](#2-mvc-패턴-상세-구현)
- [3. CRUD 구현](#3-crud-구현)
- [4. 프로젝트 설정](#4-프로젝트-설정)
- [5. 추가 기능](#5-추가-기능)

## 1. Spring MVC 아키텍처

### 1.1 전체 아키텍처
```
[클라이언트] → [DispatcherServlet] → [HandlerMapping] → [Controller] → [Service] → [Repository] → [Database]
                      ↑                                        ↓
                      └────── [ViewResolver] ← [View] ←────────┘
```

### 1.2 각 컴포넌트 역할


1. **DispatcherServlet (프론트 컨트롤러)**
   - 모든 요청의 진입점
   - 요청을 적절한 컨트롤러에 분배
   - View 렌더링 프로세스 조정

2. **HandlerMapping**
   - URL과 컨트롤러 매핑
   - 적절한 핸들러 메소드 찾기

3. **ViewResolver**
   - 논리적 뷰 이름을 물리적 뷰 파일로 변환
   - 뷰 템플릿 처리


4. **Controller Layer**

   - 클라이언트 요청 처리
   - URL 매핑 및 라우팅
   - 데이터 검증
   - Service 계층과 통신

5. **Service Layer**

   - 비즈니스 로직 처리
   - 트랜잭션 관리
   - Repository 계층과 통신

6. **Repository Layer**

   - 데이터베이스 접근
   - CRUD 연산 실행
   - JPA/Hibernate 활용

## 2. MVC 패턴 상세 구현

### 2.1 Model (모델)

```java
// Entity
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String username;
    
    @Email
    private String email;
    
    // Getters and Setters
}

// Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByEmail(String email);
}

// Service
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    // Other business methods
}
```

### 2.2 View (뷰)

```html
<!-- templates/users/list.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>사용자 목록</title>
    <link rel="stylesheet" th:href="@{/css/main.css}">
</head>
<body>
    <div class="container">
        <h1>사용자 목록</h1>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>작업</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="user : ${users}">
                    <td th:text="${user.id}"></td>
                    <td th:text="${user.username}"></td>
                    <td th:text="${user.email}"></td>
                    <td>
                        <a th:href="@{/users/{id}/edit(id=${user.id})}">수정</a>
                        <form th:action="@{/users/{id}(id=${user.id})}" method="post" style="display: inline;">
                            <input type="hidden" name="_method" value="DELETE"/>
                            <button type="submit">삭제</button>
                        </form>
                    </td>
                </tr>
            </tbody>
        </table>
        
        <a th:href="@{/users/new}" class="btn btn-primary">새 사용자</a>
    </div>
</body>
</html>
```

### 2.3 Controller (컨트롤러)

```java
@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    // 목록 조회
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }
    
    // 생성 폼
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }
    
    // 사용자 생성
    @PostMapping
    public String createUser(@Valid @ModelAttribute User user, 
                           BindingResult result) {
        if (result.hasErrors()) {
            return "users/form";
        }
        userService.createUser(user);
        return "redirect:/users";
    }
    
    // 기타 CRUD 메소드
}
```

## 3. CRUD 구현

### 3.1 Create (생성)

```java
// Controller
@PostMapping
public String create(@Valid @ModelAttribute User user, 
                    BindingResult result) {
    if (result.hasErrors()) {
        return "users/form";
    }
    userService.createUser(user);
    return "redirect:/users";
}

// Service
public User createUser(User user) {
    // 비즈니스 로직
    validateNewUser(user);
    return userRepository.save(user);
}
```

### 3.2 Read (조회)

```java
// Controller
@GetMapping("/{id}")
public String show(@PathVariable Long id, Model model) {
    User user = userService.getUserById(id);
    model.addAttribute("user", user);
    return "users/show";
}

// Service
public User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
}
```

### 3.3 Update (수정)

```java
// Controller
@PutMapping("/{id}")
public String update(@PathVariable Long id, 
                    @Valid @ModelAttribute User user,
                    BindingResult result) {
    if (result.hasErrors()) {
        return "users/edit";
    }
    userService.updateUser(id, user);
    return "redirect:/users";
}

// Service
public User updateUser(Long id, User user) {
    User existingUser = getUserById(id);
    existingUser.setUsername(user.getUsername());
    existingUser.setEmail(user.getEmail());
    return userRepository.save(existingUser);
}
```

### 3.4 Delete (삭제)

```java
// Controller
@DeleteMapping("/{id}")
public String delete(@PathVariable Long id) {
    userService.deleteUser(id);
    return "redirect:/users";
}

// Service
public void deleteUser(Long id) {
    User user = getUserById(id);
    userRepository.delete(user);
}
```

## 4. 프로젝트 설정

### 4.1 의존성 (pom.xml)

```xml
<dependencies>
    <!-- Spring MVC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Thymeleaf -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    
    <!-- JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

### 4.2 애플리케이션 설정 (application.properties)

```properties
# 데이터베이스
spring.datasource.url=jdbc:mysql://localhost:3306/dbname
spring.datasource.username=username
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Thymeleaf
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
```

## 5. 추가 기능

### 5.1 예외 처리

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(
            ResourceNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("error", "An unexpected error occurred");
        return "error/500";
    }
}
```

### 5.2 검증 (Validation)

```java
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    
    @Override
    public void validate(Object obj, Errors errors) {
        User user = (User) obj;
        if (user.getUsername() == null || user.getUsername().length() < 3) {
            errors.rejectValue("username", "field.min.length",
                             "Username must be at least 3 characters");
        }
    }
}
```

### 5.3 보안 설정

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/users").permitAll()
                .antMatchers("/users/**").authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            .logout()
                .permitAll();
    }
}
```

이 가이드는 Spring MVC의 기본 아키텍처부터 CRUD 구현, 그리고 추가 기능까지 포괄적으로 다루고 있습니다. 실제 프로젝트에 바로 적용할 수 있는 예제 코드와 설명을 포함하고 있습니다.