# DB Connection Pool 실전 활용 예제

## 1. DB Connection Pool이란?
DB Connection Pool은 데이터베이스와의 연결을 미리 생성해 두고 필요할 때 이를 재사용하여 성능을 향상시키는 기법입니다. 매번 DB 연결을 생성하고 닫는 것은 비용이 많이 들기 때문에 Connection Pool을 사용하면 성능과 안정성을 개선할 수 있습니다.

## 2. 언제 사용해야 할까?
- **DB 연결 비용이 높은 경우**: 매번 연결을 새로 생성하는 대신 재사용하여 성능을 향상.
- **고객 요청이 많은 시스템**: 다중 요청을 처리할 때 Connection Pool을 사용하면 효율적으로 리소스를 관리.
- **트랜잭션이 자주 발생하는 서비스**: 금융, 쇼핑몰, IoT 서비스 등.

## 3. Spring Boot에서 HikariCP를 이용한 Connection Pool 설정
Spring Boot 2.x 이상에서는 기본적으로 **HikariCP**가 내장되어 있으며, 가장 성능이 우수한 Connection Pool 중 하나입니다.

### 3.1. `application.properties` 설정
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# HikariCP 설정
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-timeout=30000
```

### 3.2. `DataSource` 설정 (선택 사항)
Spring Boot는 자동으로 DataSource Bean을 설정하지만, 직접 Bean을 생성할 수도 있습니다.
```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        config.setUsername("root");
        config.setPassword("1234");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        return new HikariDataSource(config);
    }
}
```

## 4. Connection Pool을 활용한 Repository 예제
```java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findAllUsernames() {
        String sql = "SELECT username FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("username"));
    }
}
```

## 5. Connection Pool 모니터링 방법
HikariCP는 자체적으로 모니터링을 지원합니다. Spring Actuator를 사용하면 실시간으로 Connection Pool 상태를 확인할 수 있습니다.

### 5.1. Actuator 설정
```properties
management.endpoints.web.exposure.include=health,metrics
```

### 5.2. Connection Pool 상태 확인
```
GET http://localhost:8080/actuator/metrics/hikaricp.connections
```
이 API를 호출하면 현재 활성화된 커넥션 개수, 사용 가능 여부 등을 확인할 수 있습니다.

## 6. 결론
- DB Connection Pool을 사용하면 성능을 향상시키고 안정성을 높일 수 있습니다.
- Spring Boot에서는 기본적으로 HikariCP를 제공하여 쉽게 설정할 수 있습니다.
- Actuator를 활용하면 Connection Pool의 상태를 모니터링할 수 있습니다.
