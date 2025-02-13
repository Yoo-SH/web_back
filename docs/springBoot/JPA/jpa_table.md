# Spring 테이블 자동 생성 및 삭제 기능

Spring Boot에서는 JPA(Hibernate)를 활용하여 애플리케이션 실행 시 자동으로 테이블을 생성하거나 삭제할 수 있습니다. 이를 위해 `application.properties` 또는 `application.yml` 파일에서 관련 설정을 지정할 수 있습니다.

## 1. 테이블 자동 생성 및 삭제 설정

Spring Boot에서 테이블 자동 생성 및 삭제는 `spring.jpa.hibernate.ddl-auto` 속성을 통해 관리됩니다. 해당 속성의 주요 값들은 다음과 같습니다.

| 값          | 설명 |
|------------|------------------------------------------------------------------|
| `none`     | 기존 테이블을 유지하며 변경하지 않음.                              |
| `update`   | 기존 테이블을 유지하면서 변경된 필드만 반영.                        |
| `create`   | 애플리케이션 실행 시 기존 테이블을 삭제하고 새로 생성.              |
| `create-drop` | `create`와 동일하지만, 애플리케이션 종료 시 테이블 삭제.        |
| `validate` | 테이블과 엔터티의 매핑이 올바른지 확인(변경 사항 반영 X).           |

### 예제 설정 (`application.properties`)
```properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 예제 설정 (`application.yml`)
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: 1234
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 2. 자동 생성 및 삭제 예제 코드

### 2.1 엔터티 클래스 예제
```java
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;
}
```

### 2.2 Repository 인터페이스
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

## 3. 주의 사항
- `create` 또는 `create-drop` 설정을 사용할 경우, 애플리케이션이 실행될 때마다 기존 데이터가 삭제되므로 운영 환경에서는 사용하지 않는 것이 좋습니다.
- 운영 환경에서는 `update` 또는 `validate`를 사용하여 데이터 손실을 방지하는 것이 일반적입니다.
- 데이터베이스 변경 사항을 안전하게 반영하려면 [Flyway](https://flywaydb.org/) 또는 [Liquibase](https://www.liquibase.org/)와 같은 마이그레이션 도구를 사용하는 것이 추천됩니다.

## 4. 결론
Spring Boot는 JPA와 Hibernate를 통해 간단한 설정만으로 테이블을 자동으로 생성하고 삭제할 수 있습니다. 개발 환경에서는 `create-drop`을, 운영 환경에서는 `validate` 또는 `update`를 사용하는 것이 일반적이며, 데이터베이스 마이그레이션 도구와 함께 사용하는 것이 권장됩니다.

