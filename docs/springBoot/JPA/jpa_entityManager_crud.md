# EntityManager를 이용한 CRUD 예제

## 개요
Spring에서 `EntityManager`를 이용하여 데이터베이스에 대한 CRUD(Create, Read, Update, Delete) 작업을 수행하는 방법을 설명합니다.

## 환경 설정
Spring Boot와 JPA를 사용하여 `EntityManager`를 활용하기 위해 `persistence.xml` 또는 `application.properties`에서 설정을 추가해야 합니다.

### `application.properties` 설정 예제
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=true
```

## Entity 정의
먼저, JPA에서 사용할 엔티티 클래스를 정의합니다.

```java
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // 기본 생성자
    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
```

## EntityManager를 이용한 CRUD 구현
`EntityManager`를 직접 사용하여 CRUD 작업을 수행하는 `UserRepository` 클래스를 작성합니다.

```java
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class UserRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    // CREATE
    public void save(User user) {
        entityManager.persist(user);
    }

    // READ (단건 조회)
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    // READ (전체 조회)
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    // UPDATE
    public void update(User user) {
        entityManager.merge(user);
    }

    // DELETE
    public void delete(Long id) {
        User user = findById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
```

## 서비스 계층 추가 (선택사항)
비즈니스 로직을 처리하기 위해 `UserService`를 추가할 수도 있습니다.

```java
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String name, String email) {
        User user = new User(name, email);
        userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(Long id, String name, String email) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            userRepository.update(user);
        }
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}
```

## 사용 예제
Spring Boot 애플리케이션에서 `UserService`를 이용하여 CRUD 작업을 수행할 수 있습니다.

```java
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserRunner implements CommandLineRunner {
    private final UserService userService;

    public UserRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 사용자 추가
        userService.createUser("홍길동", "hong@example.com");
        
        // 사용자 조회
        User user = userService.getUser(1L);
        System.out.println("조회된 사용자: " + user.getName() + ", " + user.getEmail());

        // 사용자 업데이트
        userService.updateUser(1L, "김철수", "kim@example.com");
        
        // 사용자 삭제
        userService.deleteUser(1L);
    }
}
```

## 결론
이 문서는 `EntityManager`를 이용하여 Spring Boot에서 CRUD 작업을 수행하는 방법을 설명합니다. `EntityManager`는 `@PersistenceContext`를 통해 주입되며, `persist()`, `find()`, `merge()`, `remove()` 등의 메서드를 사용하여 데이터베이스와 상호작용할 수 있습니다. `JpaRepository`를 사용하지 않고 JPA의 기본 API를 활용하고자 할 때 유용합니다.

