# Spring AOP 실전 예제 가이드

이 문서는 Spring AOP의 주요 사용 사례와 실제 구현 예시를 다룹니다.

## 목차
- [보안 검사 (Security Check)](#보안-검사-security-check)
- [트랜잭션 관리 (Transaction Management)](#트랜잭션-관리-transaction-management)
- [캐시 처리 (Cache Management)](#캐시-처리-cache-management)
- [비즈니스 로직 (Business Logic)](#비즈니스-로직-business-logic)
- [로깅/모니터링 (Logging/Monitoring)](#로깅모니터링-loggingmonitoring)

## 보안 검사 (Security Check)

### SecurityAspect.java
```java
@Aspect
@Component
@Order(1) // 보안 검사는 가장 먼저 실행되어야 함
public class SecurityAspect {
    
    @Pointcut("@annotation(com.example.security.RequiresAuth)")
    public void securityPointcut() {}
    
    @Before("securityPointcut()")
    public void checkSecurity(JoinPoint joinPoint) {
        // 현재 사용자의 인증 상태 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("인증이 필요합니다.");
        }
        
        // 메소드에 필요한 권한 확인
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequiresAuth requiresAuth = signature.getMethod().getAnnotation(RequiresAuth.class);
        String[] requiredRoles = requiresAuth.roles();
        
        boolean hasRequiredRole = Arrays.stream(requiredRoles)
            .anyMatch(role -> auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role)));
                
        if (!hasRequiredRole) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAuth {
    String[] roles() default {};
}
```

## 트랜잭션 관리 (Transaction Management)

### TransactionAspect.java
```java
@Aspect
@Component
@Order(2) // 보안 검사 다음으로 실행
public class TransactionAspect {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Pointcut("@annotation(com.example.transaction.Transactional)")
    public void transactionPointcut() {}

    @Around("transactionPointcut()")
    public Object handleTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        
        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
}
```

## 캐시 처리 (Cache Management)

### CacheAspect.java
```java
@Aspect
@Component
@Order(3)
public class CacheAspect {

    @Autowired
    private CacheManager cacheManager;

    @Pointcut("@annotation(com.example.cache.Cacheable)")
    public void cacheablePointcut() {}

    @Around("cacheablePointcut()")
    public Object handleCache(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Cacheable cacheable = signature.getMethod().getAnnotation(Cacheable.class);
        
        String cacheKey = generateCacheKey(joinPoint);
        Cache cache = cacheManager.getCache(cacheable.value());
        
        Cache.ValueWrapper valueWrapper = cache.get(cacheKey);
        if (valueWrapper != null) {
            return valueWrapper.get();
        }

        Object result = joinPoint.proceed();
        cache.put(cacheKey, result);
        return result;
    }

    private String generateCacheKey(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().toShortString() + 
               Arrays.toString(joinPoint.getArgs());
    }
}
```

## 비즈니스 로직 (Business Logic)

### ValidationAspect.java
```java
@Aspect
@Component
@Order(4)
public class ValidationAspect {

    @Pointcut("@annotation(com.example.validation.ValidateInput)")
    public void validationPointcut() {}

    @Before("validationPointcut()")
    public void validateInput(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<Object>> violations = validator.validate(arg);
                
                if (!violations.isEmpty()) {
                    throw new ValidationException("입력값 검증 실패: " + 
                        violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", ")));
                }
            }
        }
    }
}
```

## 로깅/모니터링 (Logging/Monitoring)

### LoggingAspect.java
```java
@Aspect
@Component
@Order(5)
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceLayerPointcut() {}

    @Around("serviceLayerPointcut()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        
        // 메소드 시작 로깅
        logger.info("Method {} started with arguments: {}", 
            methodName, 
            Arrays.toString(joinPoint.getArgs()));
        
        try {
            Object result = joinPoint.proceed();
            
            // 메소드 종료 로깅
            long endTime = System.currentTimeMillis();
            logger.info("Method {} completed in {}ms with result: {}", 
                methodName, 
                (endTime - startTime),
                result);
                
            // 성능 메트릭 기록
            recordMetrics(methodName, (endTime - startTime));
            
            return result;
        } catch (Exception e) {
            // 예외 로깅
            logger.error("Method {} failed with error: {}", 
                methodName, 
                e.getMessage());
            throw e;
        }
    }

    private void recordMetrics(String methodName, long executionTime) {
        // Prometheus나 다른 모니터링 시스템에 메트릭 기록
        // 예: Micrometer를 사용한 메트릭 기록
        Metrics.timer("method.execution.time", 
            Tags.of("method", methodName))
            .record(executionTime, TimeUnit.MILLISECONDS);
    }
}
```

## 사용 예시

### UserService.java
```java
@Service
public class UserService {

    @RequiresAuth(roles = {"ADMIN"})
    @Transactional
    @Cacheable("users")
    @ValidateInput
    public User createUser(@Valid UserCreateDto userDto) {
        // 비즈니스 로직 구현
        return userRepository.save(new User(userDto));
    }
}
```

## 설정

### AopConfig.java
```java
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    
    @Bean
    public SecurityAspect securityAspect() {
        return new SecurityAspect();
    }
    
    @Bean
    public TransactionAspect transactionAspect() {
        return new TransactionAspect();
    }
    
    @Bean
    public CacheAspect cacheAspect() {
        return new CacheAspect();
    }
    
    @Bean
    public ValidationAspect validationAspect() {
        return new ValidationAspect();
    }
    
    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
```

## 실행 순서

Aspect들의 실행 순서는 @Order 어노테이션으로 정의됩니다:

1. SecurityAspect (@Order(1)) - 보안 검사
2. TransactionAspect (@Order(2)) - 트랜잭션 관리
3. CacheAspect (@Order(3)) - 캐시 처리
4. ValidationAspect (@Order(4)) - 입력값 검증
5. LoggingAspect (@Order(5)) - 로깅/모니터링

이러한 순서는 다음과 같은 이유로 설정되었습니다:
- 보안 검사가 가장 먼저 실행되어 불필요한 처리를 방지
- 트랜잭션은 보안 검사 후 시작
- 캐시는 트랜잭션 내에서 처리
- 검증은 주요 처리 전에 수행
- 로깅은 모든 처리를 포함하도록 마지막에 수행