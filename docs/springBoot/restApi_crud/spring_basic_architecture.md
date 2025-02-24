# Spring MVC CRUD 가이드

## 목차
- [개요](#개요)
- [아키텍처](#아키텍처)
- [구현 가이드](#구현-가이드)
- [코드 예제](#코드-예제)
- [추가 설정](#추가-설정)

## 개요

Spring MVC에서 CRUD(Create, Read, Update, Delete) 작업은 웹 애플리케이션의 기본적인 데이터 처리 기능을 구현하는 핵심 요소입니다. 이 가이드에서는 Spring MVC를 사용하여 CRUD 작업을 구현하는 방법을 상세히 설명합니다.

## 아키텍처

Spring MVC CRUD 아키텍처는 다음과 같은 계층으로 구성됩니다:

1. **Controller Layer**
    - 클라이언트 요청 처리
    - URL 매핑 및 라우팅
    - 데이터 검증
    - Service 계층과 통신

2. **Service Layer**
    - 비즈니스 로직 처리
    - 트랜잭션 관리
    - Repository 계층과 통신

3. **Repository Layer**
    - 데이터베이스 접근
    - CRUD 연산 실행
    - JPA/Hibernate 활용


<img src=https://github.com/user-attachments/assets/e24df745-5f49-47d5-9683-0c8c805ce1ed width=500px>



__jparepository를 사용하면 기본 crud를 제공하기에 따로 crud를 직접 구현할 필요성이 없음.__
<img src=https://github.com/user-attachments/assets/db328df4-8381-4a94-99ae-22982ba3002b width=500px>


__serviceImple 구현은 HATEOAS를 사용하면 직접 구현할 필요가 없음__
<img src=https://github.com/user-attachments/assets/220ae7f2-5e88-41d6-a3f2-b77f8d34e99b width=500px>

__비교: entity manager를 사용한 crud 구현__
<img src=https://github.com/user-attachments/assets/6cfdfe6e-1e4b-4b4f-a480-5e9c4e94823e width=500px>

## 구현 가이드

### 1. 엔티티 클래스 생성

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    
    // Getters and Setters
}
```

### 2. Repository 인터페이스 생성

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
```

### 3. Service 클래스 구현

```java
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    // Create
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    // Read
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    // Update
    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }
    
    // Delete
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

### 4. Controller 클래스 구현

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    // Create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    
    // Read
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    // Update
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
```

## 추가 설정

### 1. application.properties 설정

```properties
# 데이터베이스 설정
spring.datasource.url=jdbc:mysql://localhost:3306/dbname
spring.datasource.username=username
spring.datasource.password=password

# JPA 설정
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 2. pom.xml 의존성

```xml
<dependencies>
    <!-- Spring MVC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
</dependencies>
```

## API 엔드포인트

| 메소드 | URL | 설명         |
|---|---|------------|
| POST | /api/users | 새 사용자 생성   |
| GET | /api/users | 모든 사용자 조회  |
| GET | /api/users/{id} | 특정 사용자 조회  |
| PUT | /api/users/{id} | 사용자 정보 수정  |
| DELETE | /api/users/{id} | 사용자 삭제     |

## 예외 처리

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

## 보안 설정

Spring Security를 사용한 기본적인 보안 설정:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/**").authenticated()
            .and()
            .httpBasic();
    }
}
```

## 테스트

JUnit을 사용한 테스트 예제:

```java
@SpringBootTest
public class UserControllerTest {
    
    @Autowired
    private UserController userController;
    
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        
        ResponseEntity<User> response = userController.createUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }
}
```