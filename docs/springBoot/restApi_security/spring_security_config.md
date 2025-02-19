# Spring Security 완벽 가이드

## 목차
1. [개요](#개요)
2. [주요 특징](#주요-특징)
3. [핵심 컴포넌트](#핵심-컴포넌트)
4. [기본 설정](#기본-설정)
5. [데이터베이스 연동](#데이터베이스-연동)
6. [SecurityConfig 상세 설정](#securityconfig-상세-설정)
7. [인증과 인가](#인증과-인가)
8. [고급 기능](#고급-기능)
9. [보안 모범 사례](#보안-모범-사례)

## 개요
Spring Security는 Spring 기반 애플리케이션의 보안을 담당하는 강력한 프레임워크입니다. 인증, 인가 및 다양한 보안 기능을 제공하여 애플리케이션을 안전하게 보호합니다.

## 주요 특징
- 포괄적인 인증 및 인가 지원
- 세션 관리
- CSRF 보호
- Security Headers 통합
- OAuth 2.0 / OpenID Connect 지원
- CORS 지원
- 암호화 기능

## 핵심 컴포넌트

### SecurityContextHolder
```java
// 현재 사용자 정보 얻기
SecurityContext context = SecurityContextHolder.getContext();
Authentication authentication = context.getAuthentication();
String username = authentication.getName();
```

### SecurityContext
- Authentication 객체를 보관
- 현재 사용자의 인증 및 인가 정보 포함

### Authentication
- 현재 사용자의 인증 정보를 담는 객체
- Principal과 GrantedAuthority 제공

### AuthenticationManager
- 인증 수행의 주체
- 가장 널리 사용되는 구현체는 ProviderManager

## 기본 설정

### 의존성 추가
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 데이터베이스 연동

#### 1. 사용자 엔티티
```java
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
    
    private boolean enabled = true;
}
```

#### 2. 리포지토리
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
```

#### 3. UserDetailsService
```java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles().toArray(new String[0]))
            .disabled(!user.isEnabled())
            .build();
    }
}
```

## SecurityConfig 상세 설정

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 인증 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/signup", "/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            
            // 로그인 설정
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login-process")
                .defaultSuccessUrl("/home")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
            )
            
            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            )
            
            // 세션 관리
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?expired")
            )
            
            // CSRF 설정
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/**")
            )
            
            // CORS 설정
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource())
            )
            
            // 예외 처리
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            
            // Remember-Me 설정
            .rememberMe(remember -> remember
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400)
                .userDetailsService(userDetailsService)
            );
            
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### 커스텀 핸들러

#### 인증 성공 핸들러
```java
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
            HttpServletResponse response, Authentication authentication) 
            throws IOException, ServletException {
        
        Set<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_MANAGER")) {
            response.sendRedirect("/manager/dashboard");
        } else {
            response.sendRedirect("/user/dashboard");
        }
    }
}
```

#### 인증 실패 핸들러
```java
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
            HttpServletResponse response, AuthenticationException exception) 
            throws IOException, ServletException {
        
        String errorMessage = "Invalid username or password";
        
        if (exception instanceof BadCredentialsException) {
            errorMessage = "Invalid credentials";
        } else if (exception instanceof LockedException) {
            errorMessage = "Account is locked";
        } else if (exception instanceof DisabledException) {
            errorMessage = "Account is disabled";
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = "Account has expired";
        }
        
        response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
    }
}
```

## 보안 모범 사례

### 1. 패스워드 관리
```java
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public User registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singletonList("ROLE_USER"));
        
        return userRepository.save(user);
    }
}
```

### 2. 데이터베이스 스키마
```sql
-- users 테이블
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN DEFAULT true
);

-- authorities 테이블
CREATE TABLE authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username)
);

-- Remember-Me 테이블
CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) PRIMARY KEY,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL
);
```

### 3. 애플리케이션 설정 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/security_db
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your-client-id
            client-secret: your-client-secret
```

## 보안 체크리스트

1. 기본 보안
    - 모든 비밀번호는 암호화하여 저장
    - HTTPS 사용
    - CSRF 보호 활성화
    - XSS 방어 설정

2. 세션 관리
    - 세션 타임아웃 설정
    - 동시 세션 제어
    - 세션 고정 보호

3. 접근 제어
    - 최소 권한 원칙 적용
    - URL 기반 보안 설정
    - 메소드 수준 보안 적용

4. 에러 처리
    - 사용자 친화적인 에러 메시지
    - 상세한 에러 정보 숨기기
    - 적절한 로깅 설정