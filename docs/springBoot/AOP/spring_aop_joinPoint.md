# Spring AOP Join Point 활용 가이드

## 목차
- [개요](#개요)
- [Join Point 종류](#join-point-종류)
- [실제 활용 예제](#실제-활용-예제)
    - [메소드 실행 시간 측정](#메소드-실행-시간-측정)
    - [로그인 검증](#로그인-검증)
    - [트랜잭션 관리](#트랜잭션-관리)
    - [예외 처리 및 로깅](#예외-처리-및-로깅)

## 개요
Spring AOP의 Join Point는 애플리케이션에서 관점(Aspect)을 적용할 수 있는 지점을 의미합니다. 적절한 Join Point 선택은 효과적인 관점 지향 프로그래밍 구현의 핵심입니다.

## Join Point 종류

1. **execution**: 가장 많이 사용되는 Join Point로, 메소드 실행 시점을 가리킵니다.
2. **within**: 특정 타입 내의 모든 메소드를 지정합니다.
3. **this**: 프록시 객체를 대상으로 합니다.
4. **target**: 대상 객체를 지정합니다.
5. **args**: 특정 파라미터를 가진 메소드를 지정합니다.
6. **@annotation**: 특정 어노테이션이 적용된 메소드를 지정합니다.

## 실제 활용 예제

### 메소드 실행 시간 측정

```java
@Aspect
@Component
public class PerformanceAspect {
    
    @Around("execution(* com.example.service.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        
        Object result = joinPoint.proceed();
        
        long executionTime = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature() + " 실행 시간: " + executionTime + "ms");
        
        return result;
    }
}
```

### 로그인 검증

```java
@Aspect
@Component
public class SecurityAspect {
    
    @Before("@annotation(requiresAuth)")
    public void checkAuthentication(JoinPoint joinPoint, RequiresAuth requiresAuth) {
        HttpServletRequest request = 
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            
        if (request.getSession().getAttribute("user") == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
    }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAuth {}
```

### 트랜잭션 관리

```java
@Aspect
@Component
public class TransactionAspect {
    
    @Around("@annotation(Transactional)")
    public Object handleTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        TransactionStatus status = null;
        try {
            status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            if (status != null) {
                transactionManager.rollback(status);
            }
            throw e;
        }
    }
}
```

### 예외 처리 및 로깅

```java
@Aspect
@Component
public class LoggingAspect {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @AfterThrowing(
        pointcut = "within(com.example.service.*)",
        throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        logger.error(
            "Exception in {}.{}: {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            exception.getMessage()
        );
    }
}
```

## 사용 시기 및 방법

1. **execution** 사용 시기:
    - 특정 메소드 실행을 가로챌 때
    - 성능 모니터링, 로깅 등 범용적인 기능 구현 시

2. **@annotation** 사용 시기:
    - 특정 기능을 필요로 하는 메소드만 선별적으로 지정할 때
    - 커스텀 어노테이션으로 기능을 명시적으로 표현하고 싶을 때

3. **within** 사용 시기:
    - 특정 패키지나 클래스의 모든 메소드에 일괄적으로 적용할 때
    - 계층별 공통 기능 구현 시

4. **args** 사용 시기:
    - 특정 파라미터 타입을 가진 메소드만 선택적으로 처리할 때
    - 파라미터 기반의 validation 등을 구현할 때

## Best Practices

1. Join Point 표현식은 가능한 구체적으로 작성하여 불필요한 프록시 생성을 방지합니다.
2. 성능에 민감한 메소드의 경우, Around Advice 사용을 신중히 고려합니다.
3. 예외 처리는 AfterThrowing을 사용하여 일관된 방식으로 처리합니다.
4. 로깅이나 보안 같은 공통 관심사는 적절한 추상화 수준에서 구현합니다.