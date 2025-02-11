# Spring Boot Actuator 설정 및 보안 적용

## Spring Boot Actuator란?
Spring Boot Actuator는 애플리케이션의 모니터링 및 관리를 위한 기능을 제공하는 모듈입니다. 애플리케이션의 상태를 확인하고, 메트릭을 수집하며, 환경 정보를 조회하는 등의 기능을 지원합니다.

### 주요 기능
- **헬스 체크 (`/actuator/health`)**: 애플리케이션의 상태를 확인
- **메트릭 (`/actuator/metrics`)**: CPU, 메모리 사용량 등의 지표 제공
- **환경 정보 (`/actuator/env`)**: 애플리케이션의 설정 값 조회
- **로그 수준 변경 (`/actuator/loggers`)**: 동적으로 로깅 레벨 변경 가능
- **쓰레드 덤프 (`/actuator/threaddump`)**: 현재 실행 중인 쓰레드 상태 확인
- **애플리케이션 정보 (`/actuator/info`)**: 버전, 이름 등 애플리케이션 관련 정보 제공

---

## 1. Actuator 특정 엔드포인트 제외하기
Spring Boot Actuator에서 특정 엔드포인트를 비활성화하거나 필요한 엔드포인트만 활성화하는 방법을 설명합니다.

### 🔹 특정 엔드포인트만 활성화
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "health,info"
```
위 설정은 `/actuator/health`와 `/actuator/info`만 활성화하고, 나머지 엔드포인트는 비활성화합니다.

### 🔹 특정 엔드포인트만 제외
```yaml
management:
  endpoints:
    web:
      exposure:
        exclude: "env,beans"
```
위 설정은 `/actuator/env`와 `/actuator/beans`를 비활성화하고, 나머지는 활성화합니다.

---

## 2. Spring Security로 특정 엔드포인트 보호하기
Spring Security를 사용하여 Actuator 엔드포인트 접근을 제한하는 방법을 설명합니다.

### 🔹 `application.yml`에서 인증 설정
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: when_authorized  # 인증된 사용자만 세부 정보 확인 가능
    shutdown:
      enabled: false  # shutdown 엔드포인트 비활성화
```
- `show-details: always` → 모든 사용자가 `/health`의 세부 정보를 볼 수 있음.
- `show-details: when_authorized` → 인증된 사용자만 세부 정보 확인 가능.
- `show-details: never` → 세부 정보 숨김.

### 🔹 Spring Security 설정 (Java 코드)
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()  // 누구나 접근 가능
                .requestMatchers("/actuator/**").hasRole("ADMIN")  // ADMIN 역할만 접근 가능
                .anyRequest().authenticated()
            )
            .formLogin()  // 로그인 폼 활성화
            .httpBasic(); // HTTP Basic 인증 활성화

        return http.build();
    }
}
```
✅ `/actuator/health`와 `/actuator/info`는 누구나 접근 가능.  
✅ 그 외 모든 Actuator 엔드포인트는 `ADMIN` 역할을 가진 사용자만 접근 가능.  
✅ `httpBasic()`을 사용하여 HTTP Basic 인증 활성화 (API 도구에서 테스트 가능).

---

## 🔥 정리
1. **Actuator 특정 엔드포인트 제외하기**
   - `include`를 사용하여 필요한 엔드포인트만 활성화
   - `exclude`를 사용하여 특정 엔드포인트만 비활성화

2. **Spring Security를 활용한 보호**
   - `application.yml`에서 인증 필요 설정
   - Java 코드에서 Spring Security로 특정 엔드포인트 접근 제한

이렇게 설정하면 보안이 강화된 Actuator 모니터링 시스템을 운영할 수 있습니다. 🚀

