# Spring Boot 세션 기반 사용자 권한 인증 및 시간 관리

## 목차
1. [세션 기반 인증 개요](#1-세션-기반-인증-개요)
2. [세션 구성 및 설정](#2-세션-구성-및-설정)
3. [세션 기반 인증 구현](#3-세션-기반-인증-구현)
4. [세션 시간 관리](#4-세션-시간-관리)
5. [세션 보안 강화](#5-세션-보안-강화)
6. [분산 환경에서의 세션 관리](#6-분산-환경에서의-세션-관리)
7. [세션 vs 토큰 인증](#7-세션-vs-토큰-인증)
8. [모범 사례](#8-모범-사례)
9. [문제 해결](#9-문제-해결)

## 1. 세션 기반 인증 개요

세션 기반 인증은 서버 측에서 사용자의 인증 상태를 유지하는 전통적인 방식입니다.

### 작동 원리

1. 사용자가 로그인 성공 시 서버는 세션 ID를 생성
2. 세션 ID는 클라이언트에 쿠키로 전송
3. 클라이언트는 후속 요청에 세션 ID를 포함
4. 서버는 세션 ID를 검증하여 사용자 식별

### 장점

- 구현이 비교적 간단
- 서버 측에서 세션을 쉽게 무효화할 수 있음
- 사용자 데이터가 서버에 저장되어 클라이언트에 노출되지 않음

### 단점

- 서버 메모리 사용량 증가
- 수평적 확장에 어려움 (분산 환경에서 추가 설정 필요)
- CSRF 공격에 취약할 수 있음

## 2. 세션 구성 및 설정

### 기본 의존성 추가
```xml
<!-- Maven -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### application.properties/yml 설정
```properties
# 세션 타임아웃 설정 (초 단위, 기본값은 1800초 = 30분)
server.servlet.session.timeout=1800

# 세션 쿠키 설정
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.same-site=lax

# 세션 추적 모드 설정 (COOKIE, URL, SSL)
server.servlet.session.tracking-modes=cookie
```

### 세션 리스너 구현
```java
@Component
public class SessionEventListener implements HttpSessionListener {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionEventListener.class);
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        logger.info("Session created: {}", session.getId());
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        logger.info("Session destroyed: {}", session.getId());
    }
}
```

## 3. 세션 기반 인증 구현

### 시큐리티 설정
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login?invalid=true")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?expired=true")
            )
            .rememberMe(remember -> remember
                .key("uniqueAndSecretKey")
                .tokenValiditySeconds(86400) // 24시간
            );
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 사용자 서비스 구현
```java
@Service
@Transactional
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles().toArray(new String[0]))
            .accountExpired(!user.isActive())
            .accountLocked(!user.isActive())
            .credentialsExpired(!user.isPasswordValid())
            .disabled(!user.isActive())
            .build();
    }
    
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton("USER"));
        user.setActive(true);
        user.setPasswordValid(true);
        return userRepository.save(user);
    }
}
```

### 로그인 컨트롤러
```java
@Controller
public class AuthController {
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // 세션에 사용자 정보 저장
        session.setAttribute("username", username);
        
        // 마지막 로그인 시간 저장
        session.setAttribute("lastAccess", new Date());
        
        model.addAttribute("username", username);
        return "dashboard";
    }
    
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        
        Date lastAccess = (Date) session.getAttribute("lastAccess");
        model.addAttribute("username", username);
        model.addAttribute("lastAccess", lastAccess);
        
        // 현재 접속 시간으로 업데이트
        session.setAttribute("lastAccess", new Date());
        
        return "profile";
    }
}
```

### 권한 검사 어노테이션 사용
```java
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "admin/dashboard";
    }
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageUsers() {
        return "admin/users";
    }
}
```

## 4. 세션 시간 관리

### 전역 세션 타임아웃 설정
```properties
# application.properties
server.servlet.session.timeout=1800
```

### 프로그래밍 방식으로 세션 타임아웃 설정
```java
@Configuration
public class SessionConfig {
    
    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListenerWithMetrics() {
        ServletListenerRegistrationBean<HttpSessionListener> listenerBean = 
            new ServletListenerRegistrationBean<>();
            
        listenerBean.setListener(new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                // 특정 조건에 따라 세션 타임아웃 동적 설정
                event.getSession().setMaxInactiveInterval(3600); // 1시간
            }
        });
        
        return listenerBean;
    }
}
```

### 세션 활동 추적 및 자동 연장
```java
@Component
public class SessionActivityFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionActivityFilter.class);
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        
        if (session != null) {
            // 마지막 활동 시간 업데이트
            session.setAttribute("lastActivity", System.currentTimeMillis());
            
            // 사용자 활동 패턴에 따라 세션 타임아웃 조정
            String uri = httpRequest.getRequestURI();
            if (uri.startsWith("/admin")) {
                // 관리자 페이지는 짧은 타임아웃 설정
                session.setMaxInactiveInterval(900); // 15분
            } else if (uri.startsWith("/user/transaction")) {
                // 거래 페이지는 더 짧은 타임아웃 설정
                session.setMaxInactiveInterval(300); // 5분
            }
            
            logger.debug("Session {} accessed path: {}", session.getId(), uri);
        }
        
        chain.doFilter(request, response);
    }
}
```

### 세션 만료 시간 확인 및 갱신
```java
@RestController
@RequestMapping("/api/session")
public class SessionController {
    
    @GetMapping("/info")
    public Map<String, Object> getSessionInfo(HttpSession session) {
        Map<String, Object> info = new HashMap<>();
        
        // 세션 ID (해시 처리하여 보안 강화)
        info.put("sessionId", session.getId().hashCode());
        
        // 생성 시간
        info.put("creationTime", new Date(session.getCreationTime()));
        
        // 마지막 접근 시간
        info.put("lastAccessTime", new Date(session.getLastAccessedTime()));
        
        // 세션 만료까지 남은 시간 (초)
        info.put("maxInactiveInterval", session.getMaxInactiveInterval());
        
        // 세션 만료 예상 시간
        long expiryTimeMillis = session.getLastAccessedTime() + 
                                (session.getMaxInactiveInterval() * 1000L);
        info.put("expiryTime", new Date(expiryTimeMillis));
        
        return info;
    }
    
    @PostMapping("/extend")
    public Map<String, Object> extendSession(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        // 현재 남은 시간 확인
        int currentTimeout = session.getMaxInactiveInterval();
        
        // 추가 시간 설정 (예: 30분)
        session.setMaxInactiveInterval(1800);
        
        result.put("success", true);
        result.put("previousTimeout", currentTimeout);
        result.put("newTimeout", session.getMaxInactiveInterval());
        result.put("newExpiryTime", new Date(session.getLastAccessedTime() + 
                                           (session.getMaxInactiveInterval() * 1000L)));
        
        return result;
    }
}
```

### 세션 타임아웃 알림 (클라이언트 측)
```javascript
// session-monitor.js
document.addEventListener('DOMContentLoaded', function() {
    let warningTimeout = 60; // 세션 만료 1분 전에 경고
    let warningShown = false;
    
    function checkSessionStatus() {
        fetch('/api/session/info')
            .then(response => response.json())
            .then(data => {
                const expiryTime = new Date(data.expiryTime);
                const now = new Date();
                
                // 세션 만료까지 남은 시간 (초)
                const secondsLeft = Math.floor((expiryTime - now) / 1000);
                
                if (secondsLeft <= warningTimeout && !warningShown) {
                    showWarning(secondsLeft);
                    warningShown = true;
                }
            });
    }
    
    function showWarning(secondsLeft) {
        const warningDiv = document.createElement('div');
        warningDiv.className = 'session-warning';
        warningDiv.innerHTML = `
            <p>세션이 곧 만료됩니다(${secondsLeft}초 후).</p>
            <button id="extend-session">세션 연장하기</button>
        `;
        
        document.body.appendChild(warningDiv);
        
        document.getElementById('extend-session').addEventListener('click', extendSession);
    }
    
    function extendSession() {
        fetch('/api/session/extend', { method: 'POST' })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const warning = document.querySelector('.session-warning');
                    if (warning) {
                        warning.remove();
                    }
                    warningShown = false;
                    
                    const notification = document.createElement('div');
                    notification.className = 'session-notification';
                    notification.textContent = '세션이 연장되었습니다.';
                    document.body.appendChild(notification);
                    
                    setTimeout(() => {
                        notification.remove();
                    }, 3000);
                }
            });
    }
    
    // 주기적으로 세션 상태 확인 (30초마다)
    setInterval(checkSessionStatus, 30000);
    
    // 페이지 로드 시 최초 확인
    checkSessionStatus();
});
```

## 5. 세션 보안 강화

### 세션 고정 공격 방지
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 기타 설정들...
            .sessionManagement(session -> session
                // 인증 시 세션 ID 변경
                .sessionFixation().changeSessionId()
            );
        
        return http.build();
    }
}
```

### 세션 하이재킹 방지
```properties
# application.properties
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.same-site=strict
```

### 세션 데이터 암호화
```java
@Configuration
public class SessionConfig {
    
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        serializer.setUseHttpOnlyCookie(true);
        serializer.setUseSecureCookie(true);
        serializer.setSameSite("Strict");
        return serializer;
    }
    
    @Bean
    public Object springSessionRepositoryFilter() {
        // 세션 리포지토리 필터 사용자 정의
        return new Object(); // 실제 구현은 더 복잡함
    }
}
```

### 권한 변경 감지
```java
@Component
public class AuthenticationChangeListener implements ApplicationListener<AuthenticationSuccessEvent> {
    
    @Autowired
    private HttpSession session;
    
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        
        // 이전 권한과 새 권한 비교
        Authentication prevAuth = (Authentication) session.getAttribute("PREV_AUTH");
        if (prevAuth != null && !prevAuth.getAuthorities().equals(auth.getAuthorities())) {
            // 권한이 변경됨, 세션 무효화 고려
            session.invalidate();
        }
        
        // 현재 권한 저장
        session.setAttribute("PREV_AUTH", auth);
    }
}
```

### 동시 세션 제어
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 기타 설정들...
            .sessionManagement(session -> session
                .maximumSessions(1) // 동일 사용자의 최대 세션 수
                .maxSessionsPreventsLogin(true) // 세션 초과 시 새 로그인 차단
                .expiredUrl("/login?expired=true") // 세션 만료 시 리다이렉트
            );
        
        return http.build();
    }
}
```

## 6. 분산 환경에서의 세션 관리

### Spring Session 설정 (Redis 기반)
```xml
<!-- Maven 의존성 -->
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
</dependency>
```

```properties
# application.properties
spring.session.store-type=redis
spring.session.redis.namespace=spring:session
spring.session.redis.flush-mode=on-save
spring.session.redis.save-mode=on-set-attribute

# Redis 설정
spring.redis.host=localhost
spring.redis.port=6379
```

### 세션 저장소 구성
```java
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setHostName("localhost");
        factory.setPort(6379);
        return factory;
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
```

### 세션 이벤트 처리
```java
@Component
public class SessionEventListener implements ApplicationListener<AbstractSessionEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionEventListener.class);
    
    @Override
    public void onApplicationEvent(AbstractSessionEvent event) {
        if (event instanceof SessionCreatedEvent) {
            logger.info("Session created: {}", ((SessionCreatedEvent) event).getSessionId());
        } else if (event instanceof SessionDeletedEvent) {
            logger.info("Session deleted: {}", ((SessionDeletedEvent) event).getSessionId());
        } else if (event instanceof SessionExpiredEvent) {
            logger.info("Session expired: {}", ((SessionExpiredEvent) event).getSessionId());
        }
    }
}
```

### 세션 정보 조회
```java
@Service
public class SessionService {
    
    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    
    public List<Session> findSessionsByUsername(String username) {
        Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(username);
        return new ArrayList<>(sessions.values());
    }
    
    public void expireUserSessions(String username) {
        Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(username);
        
        for (Session session : sessions.values()) {
            // 세션 만료 처리
            session.setAttribute("expired", true);
            sessionRepository.save(session);
        }
    }
}
```

## 7. 세션 vs 토큰 인증

### 세션 기반 인증의 한계
- 서버 메모리 부담
- 수평적 확장성 문제
- 모바일 앱 및 API 통합 어려움

### 토큰 기반 인증 (JWT) 활용
```xml
<!-- JWT 의존성 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

```java
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

### 하이브리드 접근법
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
```

## 8. 모범 사례

### 세션 관리 체크리스트
- 세션 ID를 URL에 노출하지 않기
- 세션 쿠키에 HttpOnly, Secure, SameSite 속성 설정
- 로그인 성공 시 세션 ID 재생성
- 적절한 세션 타임아웃 설정
- 중요한 작업 후 세션 재검증
- HTTPS 사용

### 세션 정보 관리
```java
@Controller
@RequestMapping("/admin")
public class SessionManagementController {
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    @GetMapping("/sessions")
    public String listSessions(Model model) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<UserSessionDTO> userSessions = new ArrayList<>();
        
        for (Object principal : principals) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                List<SessionInformation> sessions = 
                    sessionRegistry.getAllSessions(principal, false);
                
                for (SessionInformation session : sessions) {
                    UserSessionDTO dto = new UserSessionDTO();
                    dto.setUsername(userDetails.getUsername());
                    dto.setSessionId(session.getSessionId());
                    dto.setLastActivity(session.getLastRequest());
                    dto.setExpired(session.isExpired());
                    userSessions.add(dto);
                }
            }
        }
        
        model.addAttribute("userSessions", userSessions);
        return "admin/sessions";
    }
    
    @PostMapping("/sessions/{sessionId}/expire")
    public String expireSession(@PathVariable String sessionId) {
        SessionInformation session = sessionRegistry.getSessionInformation(sessionId);
        if (session != null) {
            session.expireNow();
        }
        return "redirect:/admin/sessions";
    }
    
    @PostMapping("/sessions/user/{username}/expire")
    public String expireUserSessions(@PathVariable String username) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof UserDetails &&
                ((UserDetails) principal).getUsername().equals(username)) {
                
                List<SessionInformation> sessions = 
                    sessionRegistry.getAllSessions(principal, false);
                
                for (SessionInformation session : sessions) {
                    session.expireNow();
                }
            }
        }
        return "redirect:/admin/sessions";
    }
}
```

### 권한 변경과 세션 처리
```java
@Service
public class UserManagementService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Transactional
    public void updateUserRole(String username, String newRole) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
        
        // 권한 변경
        user.setRoles(Collections.singleton(newRole));
        userRepository.save(user);
        
        // 해당 사용자의 모든 세션 만료
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof UserDetails &&
                ((UserDetails) principal).getUsername().equals(username)) {
                
                List<SessionInformation> sessions = 
                    sessionRegistry.getAllSessions(principal, false);
                
                for (SessionInformation session : sessions) {
                    session.expireNow();
                }
            }
        }
    }
}
```

### 다단계 인증 (2FA)
```java
@Entity
public class User {
    // 기존 필드들...
    
    @Column(nullable = false)
    private boolean twoFactorEnabled = false;
    
    @Column
    private String twoFactorSecret;
}

@Service
public class TwoFactorAuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    public String generateTwoFactorSecret() {
        return Base32.random();
    }
    
    public boolean validateTwoFactorCode(String username, String code) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
        
        if (!user.isTwoFactorEnabled()) {
            return true;
        }
        
        // TOTP 검증 로직
        TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
        return totp.validateCode(user.getTwoFactorSecret(), code);
    }
}

@Controller
public class TwoFactorAuthController {
    
    @Autowired
    private TwoFactorAuthService twoFactorAuthService;
    
    @GetMapping("/login/2fa")
    public String twoFactorPage() {
        return "login/2fa";
    }
    
    @PostMapping("/login/2fa")
    public String validateTwoFactor(@RequestParam String code, HttpSession session) {
        String username = (String) session.getAttribute("PENDING_2FA_USERNAME");
        
        if (username == null) {
            return "redirect:/login";
        }
        
        if (twoFactorAuthService.validateTwoFactorCode(username, code)) {
            // 2FA 성공, 세션 업데이트
            session.setAttribute("FULLY_AUTHENTICATED", true);
            return "redirect:/dashboard";
        } else {
            return "redirect:/login/2fa?error=true";
        }
    }
}
```

## 9. 문제 해결

### 세션 관련 일반적인 문제와 해결책

#### 세션이 예상보다 빨리 만료되는 경우

```java
@Component
public class SessionDebugFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionDebugFilter.class);
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        
        if (session != null) {
            Date creationTime = new Date(session.getCreationTime());
            Date lastAccessedTime = new Date(session.getLastAccessedTime());
            int maxInactiveInterval = session.getMaxInactiveInterval();
            
            logger.debug("Session Debug - ID: {}, Created: {}, Last Accessed: {}, Max Inactive: {} seconds",
                        session.getId(), creationTime, lastAccessedTime, maxInactiveInterval);
        }
        
        chain.doFilter(request, response);
    }
}
```

#### 세션 데이터 손실 문제

```java
@ControllerAdvice
public class SessionExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionExceptionHandler.class);
    
    @ExceptionHandler(HttpSessionRequiredException.class)
    public String handleSessionRequired(HttpSessionRequiredException ex) {
        logger.warn("Session required but not available: {}", ex.getMessage());
        return "redirect:/login?session-expired=true";
    }
    
    @ExceptionHandler(IllegalStateException.class)
    public String handleSessionIllegalState(IllegalStateException ex) {
        if (ex.getMessage().contains("session")) {
            logger.warn("Session illegal state: {}", ex.getMessage());
            return "redirect:/login?session-invalid=true";
        }
        throw ex;
    }
}
```

#### 세션 저장소 문제 (Redis 사용 시)

```java
@Configuration
public class RedisHealthCheck {
    
    @Bean
    public HealthIndicator redisSessionHealthIndicator(RedisConnectionFactory redisConnectionFactory) {
        return new HealthIndicator() {
            @Override
            public Health health() {
                try {
                    RedisConnection connection = redisConnectionFactory.getConnection();
                    connection.ping();
                    connection.close();
                    return Health.up().build();
                } catch (Exception e) {
                    return Health.down().withDetail("Error", e.getMessage()).build();
                }
            }
        };
    }
}
```

### 모니터링 및 디버깅

#### 세션 활동 로깅

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}

@Component
public class SessionMonitoringListener implements HttpSessionListener {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionMonitoringListener.class);
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        logger.info("Session created: {}, Max inactive interval: {} seconds", 
                   session.getId(), session.getMaxInactiveInterval());
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        logger.info("Session destroyed: {}, Last accessed: {}", 
                   session.getId(), new Date(session.getLastAccessedTime()));
    }
}
```

#### 세션 저장소 통계 확인

```java
@Service
public class SessionMonitoringService {
    
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    
    public Map<String, Object> getSessionStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            Long totalKeys = connection.keyCommands().dbSize();
            
            Long sessionKeys = 0L;
            try (Cursor<byte[]> cursor = connection.keyCommands().scan(ScanOptions.scanOptions()
                    .match("spring:session:*").build())) {
                while (cursor.hasNext()) {
                    cursor.next();
                    sessionKeys++;
                }
            }
            
            stats.put("totalKeys", totalKeys);
            stats.put("sessionKeys", sessionKeys);
            stats.put("timestamp", new Date());
        }
        
        return stats;
    }
}
```

### 성능 최적화

#### 세션 데이터 최소화

```java
@Component
public class SessionOptimizer {
    
    @Autowired
    private HttpSession session;
    
    public void optimizeSessionData() {
        // 세션에서 불필요한 대형 객체 제거
        session.removeAttribute("largeTemporaryData");
        
        // 필요한 경우 데이터 압축
        byte[] compressedData = compressData((byte[]) session.getAttribute("originalData"));
        session.setAttribute("compressedData", compressedData);
        session.removeAttribute("originalData");
    }
    
    private byte[] compressData(byte[] data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (GZIPOutputStream gos = new GZIPOutputStream(baos)) {
                gos.write(data);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            return data; // 압축 실패 시 원본 반환
        }
    }
}
```

#### 세션 영속성 최적화

```java
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class OptimizedRedisSessionConfig {
    
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new JdkSerializationRedisSerializer();
    }
    
    @Bean
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 직렬화 설정
        template.setDefaultSerializer(springSessionDefaultRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // 연결 풀 설정
        template.setEnableTransactionSupport(false);
        
        return template;
    }
}
```

## 종합적인 세션 관리 전략

### 세션 생명주기 관리

1. **세션 생성**: 사용자 로그인 성공 시
2. **세션 유지**: 사용자 활동에 따른 세션 시간 조정
3. **세션 갱신**: 중요한 작업 수행 시 세션 재검증
4. **세션 종료**: 명시적 로그아웃, 타임아웃, 또는 보안 위반 시

### 권한 관리와 세션 통합

1. **동적 권한 변경**: 사용자 권한 변경 시 세션 무효화
2. **세션별 권한 캐싱**: 자주 사용되는 권한 정보 세션에 캐싱
3. **세션 기반 접근 제어**: 특정 세션에 대한 접근 제한

### 보안 강화 전략

1. **다중 인증 요소**: 민감한 작업 수행 시 추가 인증 요구
2. **세션 활동 모니터링**: 비정상적인 활동 감지 및 대응
3. **세션 정보 암호화**: 중요한 세션 데이터 암호화
4. **정기적인 세션 순환**: 장기 세션의 경우 정기적인 세션 ID 변경

### 확장성 고려사항

1. **클러스터링 지원**: 서버 간 세션 공유 메커니즘
2. **세션 데이터 최적화**: 세션 크기 최소화 및 효율적인 직렬화
3. **세션 저장소 분리**: 애플리케이션 서버와 세션 저장소 분리
4. **세션 저장소 확장**: 세션 저장소의 수평적 확장 지원

### 사용자 경험 개선

1. **세션 만료 알림**: 세션 만료 전 사용자에게 알림
2. **자동 로그인 옵션**: Remember Me 기능을 통한 사용자 편의성 제공
3. **디바이스 관리**: 사용자별 활성 세션 관리 및 제어
4. **세션 복구 메커니즘**: 예기치 않은 세션 종료 시 복구 방법 제공

## 결론

Spring Boot에서의 세션 기반 인증과 시간 관리는 보안, 확장성, 사용자 경험을 모두 고려해야 하는 복잡한 주제입니다. 이 README에서 다룬 내용을 바탕으로 애플리케이션의 요구사항에 맞는 세션 관리 전략을 수립하고, 지속적인 모니터링과 최적화를 통해 안전하고 효율적인 사용자 인증 시스템을 구현할 수 있습니다.

세션 관리는 정적인 구성으로 끝나는 것이 아니라, 애플리케이션의 성장과 변화하는 보안 위협에 따라 함께 발전해야 하는 영역임을 기억하세요. 정기적인 보안 감사, 성능 분석, 사용자 피드백을 통해 세션 관리 시스템을 지속적으로 개선해 나가는 것이 중요합니다.
