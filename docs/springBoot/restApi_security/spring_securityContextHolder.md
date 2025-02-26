# Spring Security - SecurityContextHolder 구조 설명

## 목차
1. [개요](#개요)
2. [객체 비교 요약표](#객체-비교-요약표)
3. [SecurityContextHolder](#securitycontextholder)
    - [실무 활용](#실무-활용)
    - [저장 전략 설정](#저장-전략-설정)
4. [SecurityContext](#securitycontext)
    - [실무 활용](#securitycontext-실무-활용)
5. [Authentication](#authentication)
    - [주요 메소드](#주요-메소드)
    - [실무 활용](#authentication-실무-활용)
6. [Principal (UserDetails)](#principal-userdetails)
    - [실무 활용](#principal-실무-활용)
7. [Credentials](#credentials)
    - [실무 활용](#credentials-실무-활용)
8. [Authorities (GrantedAuthority)](#authorities-grantedauthority)
    - [실무 활용](#authorities-실무-활용)
9. [Details (WebAuthenticationDetails)](#details-webauthenticationdetails)
    - [실무 활용](#details-실무-활용)
10. [실무 활용 사례](#실무-활용-사례)
    - [서비스 계층에서 현재 사용자 접근하기](#서비스-계층에서-현재-사용자-접근하기)
    - [@AuthenticationPrincipal 어노테이션 활용](#authenticationprincipal-어노테이션-활용)
    - [비동기 환경(Async)에서의 보안 컨텍스트 전파](#비동기-환경async에서의-보안-컨텍스트-전파)
11. [보안 고려사항](#보안-고려사항)
12. [테스트 코드 예제](#테스트-코드-예제)

## 개요
이 문서는 Spring Security의 핵심 클래스인 SecurityContextHolder와 그 관련 객체들의 구조 및 실무 활용 방법에 대해 설명합니다.


## 객체 비교 요약표

| 객체 | 역할 | 접근 방법 | 주요 사용 사례 | 주의사항 |
|------|------|-----------|--------------|----------|
| **SecurityContextHolder** | 보안 컨텍스트 저장소 | `SecurityContextHolder.getContext()` | 인증된 사용자 정보에 접근하기 위한 진입점 | 저장 전략(MODE_THREADLOCAL 등)을 상황에 맞게 설정해야 함 |
| **SecurityContext** | Authentication 객체 보관 | `SecurityContextHolder.getContext()` | 인증 객체 보관 및 접근 | 멀티스레드 환경에서 적절한 관리 필요 |
| **Authentication** | 인증 정보 저장 | `SecurityContext.getAuthentication()` | 사용자 인증 상태 확인 및 정보 접근 | 인증 후 credentials는 보안을 위해 삭제 고려 |
| **Principal** | 인증된 사용자 정보 | `Authentication.getPrincipal()` | 로그인한 사용자 식별 및 정보 접근 | 일반적으로 UserDetails 구현체로 캐스팅 필요 |
| **Credentials** | 인증 자격 증명 | `Authentication.getCredentials()` | 인증 과정에서 비밀번호 등 검증 | 인증 후에는 보안을 위해 null 처리 권장 |
| **Authorities** | 사용자 권한 목록 | `Authentication.getAuthorities()` | 접근 제어 및 권한 기반 기능 제한 | `@PreAuthorize` 등과 함께 활용 |
| **Details** | 인증 요청 부가 정보 | `Authentication.getDetails()` | 로깅, 감사, IP 추적 등 | 일반적으로 WebAuthenticationDetails로 캐스팅 |

## SecurityContextHolder
SecurityContextHolder는 Spring Security에서 현재 인증된 사용자 정보에 접근할 수 있게 해주는 핵심 클래스입니다.

### 실무 활용
```java
// 현재 인증된 사용자 정보 가져오기
SecurityContext context = SecurityContextHolder.getContext();
```

### 저장 전략 설정
```java
// 애플리케이션 시작 시 SecurityContextHolder 모드 설정
@Configuration
public class SecurityConfig {
    @PostConstruct
    public void init() {
        // SecurityContextHolder의 전략 설정
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}
```

## SecurityContext
SecurityContext는 Authentication 객체를 보관하는 인터페이스입니다.

### SecurityContext 실무 활용
```java
// 현재 SecurityContext 가져오기
SecurityContext context = SecurityContextHolder.getContext();

// 새로운 SecurityContext 생성
SecurityContext newContext = SecurityContextHolder.createEmptyContext();
```

## Authentication
Authentication은 현재 인증된 사용자 정보와 권한을 담고 있는 객체입니다.

### 주요 메소드
- `getPrincipal()`: 인증된 사용자 객체 반환
- `getAuthorities()`: 사용자 권한 목록 반환
- `getCredentials()`: 인증에 사용된 자격 증명(보통 비밀번호) 반환
- `getDetails()`: 인증 과정의 추가 세부 정보 반환
- `isAuthenticated()`: 인증 완료 여부 반환

### Authentication 실무 활용
```java
// 현재 인증 정보 가져오기
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

// 인증 여부 확인
if (authentication != null && authentication.isAuthenticated()) {
    // 인증된 사용자 처리
}

// 컨트롤러에서 직접 주입 받는 방식
@GetMapping("/profile")
public String viewProfile(Authentication authentication) {
    // 컨트롤러 매개변수로 Authentication을 받을 수 있음
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return "profile";
}
```

## Principal (UserDetails)
Principal은 인증된 사용자를 나타내는 객체로, 일반적으로 UserDetails 인터페이스의 구현체입니다.

### Principal 실무 활용
```java
// 현재 인증된 사용자 정보 접근
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
UserDetails userDetails = (UserDetails) authentication.getPrincipal();

// 사용자 정보 활용
String username = userDetails.getUsername();
boolean enabled = userDetails.isEnabled();

// UserDetailsService 구현 예제
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true, true, true,
            getAuthorities(user.getRoles())
        );
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
}
```

## Credentials
Credentials는 사용자 인증에 사용된 비밀번호 등의 자격 증명 정보입니다.

### Credentials 실무 활용
```java
// 보안상의 이유로 인증 후에는 일반적으로 credentials를 지움
// 이는 ProviderManager에서 자동으로 처리됨

// 수동으로 처리할 경우
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
if (auth instanceof UsernamePasswordAuthenticationToken) {
    UsernamePasswordAuthenticationToken newAuth = 
        new UsernamePasswordAuthenticationToken(
            auth.getPrincipal(), 
            null,  // credentials을 null로 설정
            auth.getAuthorities()
        );
    newAuth.setDetails(auth.getDetails());
    SecurityContextHolder.getContext().setAuthentication(newAuth);
}
```

## Authorities (GrantedAuthority)
Authorities는 사용자에게 부여된 권한 목록으로, GrantedAuthority 인터페이스의 구현체 컬렉션입니다.

### Authorities 실무 활용
```java
// 현재 사용자의 권한 확인
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

// 특정 권한을 가지고 있는지 확인
boolean isAdmin = authorities.stream()
    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

// 권한 기반 접근 제어
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin")
public String adminDashboard() {
    // 관리자만 접근 가능
    return "admin/dashboard";
}

// SpEL을 사용한 복잡한 권한 검사
@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)")
@GetMapping("/users/{id}")
public String userProfile(@PathVariable Long id) {
    // 관리자이거나 자신의 프로필인 경우에만 접근 가능
    return "user/profile";
}
```

## Details (WebAuthenticationDetails)
Details는 인증 요청에 대한 추가 정보를 담고 있습니다. 웹 요청의 경우 일반적으로 WebAuthenticationDetails 클래스가 사용됩니다.

### Details 실무 활용
```java
// 인증 세부 정보 접근
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (authentication.getDetails() instanceof WebAuthenticationDetails) {
    WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
    String remoteAddress = details.getRemoteAddress();
    String sessionId = details.getSessionId();
    
    // 로깅 또는 감사 목적으로 활용
    log.info("User {} logged in from IP: {}", authentication.getName(), remoteAddress);
}

// 커스텀 인증 상세 정보 생성
public class CustomWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, CustomWebAuthenticationDetails> {
    @Override
    public CustomWebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new CustomWebAuthenticationDetails(request);
    }
}

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private final String userAgent;
    
    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.userAgent = request.getHeader("User-Agent");
    }
    
    public String getUserAgent() {
        return userAgent;
    }
}
```

## 실무 활용 사례

### 서비스 계층에서 현재 사용자 접근하기
```java
@Service
public class UserService {
    
    // 현재 로그인한 사용자 정보 가져오기
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("인증된 사용자가 아닙니다.");
        }
        
        return (User) authentication.getPrincipal();
    }
    
    // 현재 사용자 ID를 기반으로 작업 수행
    public void updateProfile(ProfileUpdateDto profileDto) {
        User currentUser = getCurrentUser();
        // 프로필 업데이트 로직...
    }
}
```

### @AuthenticationPrincipal 어노테이션 활용
```java
@GetMapping("/dashboard")
public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    model.addAttribute("username", userDetails.getUsername());
    return "dashboard";
}

// 커스텀 어노테이션 생성
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {}

// 커스텀 어노테이션 사용
@GetMapping("/profile")
public String profile(@CurrentUser User user) {
    // 커스텀 User 객체로 직접 접근
    return "profile";
}
```

### 비동기 환경(Async)에서의 보안 컨텍스트 전파
```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Async-");
        
        // SecurityContext를 비동기 스레드에 전파하기 위한 데코레이터
        executor.setTaskDecorator(task -> {
            SecurityContext context = SecurityContextHolder.getContext();
            return () -> {
                try {
                    SecurityContextHolder.setContext(context);
                    task.run();
                } finally {
                    SecurityContextHolder.clearContext();
                }
            };
        });
        
        executor.initialize();
        return executor;
    }
}

@Service
public class NotificationService {
    
    @Async
    public CompletableFuture<Void> sendNotification(Long recipientId) {
        // 비동기 메서드에서도 SecurityContext 접근 가능
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sender = authentication.getName();
        
        // 알림 전송 로직...
        
        return CompletableFuture.completedFuture(null);
    }
}
```

## 보안 고려사항
1. SecurityContextHolder는 ThreadLocal 기반이므로 각 요청(스레드)마다 독립적입니다.
2. 비동기 처리 시 SecurityContext의 전파를 신경써야 합니다.
3. 인증 완료 후에는 보안을 위해 credentials를 null로 설정하는 것이 좋습니다.
4. 테스트 시 SecurityContextHolder를 명시적으로 설정하여 인증된 상태를 시뮬레이션할 수 있습니다.

## 테스트 코드 예제
```java
@Test
public void whenUserIsAuthenticated_thenCanAccessSecuredMethod() {
    // 테스트용 인증 객체 설정
    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
        "testuser", "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
    );
    
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities()
    );
    
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
    
    // 테스트 수행
    try {
        // 보안이 적용된 메서드 호출
        someService.securedMethod();
        // 성공적으로 호출되었으면 테스트 통과
    } finally {
        // 테스트 후 SecurityContext 정리
        SecurityContextHolder.clearContext();
    }
}
```
