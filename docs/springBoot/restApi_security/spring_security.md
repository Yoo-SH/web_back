# Spring Boot Security 가이드

## 목차
- [개요](#개요)
- [주요 기능](#주요-기능)
- [시작하기](#시작하기)
- [기본 설정](#기본-설정)
- [인증 (Authentication)](#인증-authentication)
- [인가 (Authorization)](#인가-authorization)
- [보안 필터 체인](#보안-필터-체인)
- [자주 사용되는 설정 예제](#자주-사용되는-설정-예제)

## 개요

Spring Boot Security는 애플리케이션의 보안을 담당하는 프레임워크입니다. 인증과 권한 부여를 위한 포괄적인 보안 솔루션을 제공합니다.

### 주요 특징
- 포괄적인 보안 기능
- 다양한 인증 방식 지원 (폼 로그인, HTTP Basic, JWT 등)
- 유연한 권한 설정
- 세션 관리
- CSRF 보호

## 시작하기

### 의존성 추가

Maven:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Gradle:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
```

## 기본 설정

### SecurityConfig 기본 구성

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
            );
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## 인증 (Authentication)

### 지원하는 인증 방식
1. 폼 로그인
2. HTTP Basic 인증
3. JWT 토큰 인증
4. OAuth2/OpenID Connect
5. Remember-Me 인증
6. LDAP 인증

### 사용자 정보 관리

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Override
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        // 사용자 정보 조회 로직
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .roles("USER")
                .build();
    }
}
```

## 인가 (Authorization)

### URL 기반 권한 설정

```java
http.authorizeHttpRequests((auth) -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/user/**").hasRole("USER")
    .requestMatchers("/api/**").hasAuthority("API_ACCESS")
    .anyRequest().authenticated()
);
```

### 메소드 보안

```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() {
    // 관리자만 접근 가능한 메소드
}
```

## 보안 필터 체인

Spring Security의 필터 체인 순서:
1. SecurityContextPersistenceFilter
2. LogoutFilter
3. UsernamePasswordAuthenticationFilter
4. DefaultLoginPageGeneratingFilter
5. BasicAuthenticationFilter
6. RememberMeAuthenticationFilter
7. ExceptionTranslationFilter
8. FilterSecurityInterceptor

## 자주 사용되는 설정 예제

### CORS 설정

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

### JWT 설정 예제

```java
@Configuration
public class JwtSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(), 
                UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}
```

### OAuth2 설정

application.yml 예제:
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your-client-id
            client-secret: your-client-secret
```

## 보안 모범 사례

1. 항상 HTTPS 사용
2. 비밀번호는 반드시 암호화하여 저장
3. CSRF 보호 활성화
4. 세션 고정 보호 활성화
5. XSS 방지를 위한 헤더 설정
6. 적절한 로깅 설정

## 문제 해결

일반적인 문제 및 해결 방법:

1. 인증 실패
    - 로그 레벨을 DEBUG로 설정하여 자세한 정보 확인
    - UserDetailsService 구현 확인

2. CORS 오류
    - CORS 설정 확인
    - 허용된 오리진 확인

3. 세션 관련 문제
    - 세션 설정 확인
    - 세션 만료 시간 조정

## 참고 자료

- [Spring Security 공식 문서](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Boot Security 가이드](https://spring.io/guides/gs/securing-web/)
- [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture/)