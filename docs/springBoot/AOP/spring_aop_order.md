# Spring AOP Order 어노테이션 가이드

## 목차
- [개요](#개요)
- [주요 어노테이션](#주요-어노테이션)
- [실제 활용 예제](#실제-활용-예제)
- [Best Practices](#best-practices)

## 개요
Spring AOP에서 여러 Advice가 하나의 JoinPoint에 적용될 때, 실행 순서를 제어하는 것은 매우 중요합니다. Spring은 이를 위해 `@Order` 어노테이션과 `Ordered` 인터페이스를 제공합니다.

## 주요 어노테이션

### 1. @Order
- 기본적인 순서 지정 어노테이션
- 값이 작을수록 우선순위가 높음 (기본값: Integer.MAX_VALUE)
- 클래스 레벨과 메소드 레벨 모두에서 사용 가능

### 2. Ordered 인터페이스
- 프로그래밍 방식으로 순서를 지정할 때 사용
- getOrder() 메소드를 구현하여 동적으로 순서 결정 가능

## 실제 활용 예제

### 1. 로깅과 트랜잭션 처리 예제

```java
@Aspect
@Component
@Order(1) // 가장 먼저 실행
public class LoggingAspect {
    
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("로그 기록: " + joinPoint.getSignature().getName());
    }
}

@Aspect
@Component
@Order(2) // 두 번째로 실행
public class TransactionAspect {
    
    @Around("execution(* com.example.service.*.*(..))")
    public Object handleTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            System.out.println("트랜잭션 시작");
            Object result = joinPoint.proceed();
            System.out.println("트랜잭션 커밋");
            return result;
        } catch (Exception e) {
            System.out.println("트랜잭션 롤백");
            throw e;
        }
    }
}
```

### 2. 보안 검사와 성능 모니터링 예제

```java
@Aspect
@Component
@Order(1) // 보안 검사가 가장 먼저 실행되어야 함
public class SecurityAspect {
    
    @Before("execution(* com.example.controller.*.*(..))")
    public void checkSecurity(JoinPoint joinPoint) {
        // 보안 검사 로직
        System.out.println("보안 검사 수행");
    }
}

@Aspect
@Component
@Order(2) // 보안 검사 후 성능 모니터링
public class PerformanceAspect {
    
    @Around("execution(* com.example.controller.*.*(..))")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        
        System.out.println("메소드 실행 시간: " + (end - start) + "ms");
        return result;
    }
}
```

## Best Practices

### 1. 순서 지정 원칙
- 인프라 관련 Aspect를 먼저 실행 (보안, 트랜잭션 등)
- 비즈니스 로직 관련 Aspect를 나중에 실행
- 모니터링/로깅 관련 Aspect는 상황에 따라 맨 처음 또는 맨 마지막에 실행

### 2. 권장하는 우선순위
1. 보안 검사 (@Order(1))
2. 트랜잭션 관리 (@Order(2))
3. 캐시 처리 (@Order(3))
4. 비즈니스 로직 관련 Aspect (@Order(4))
5. 로깅/모니터링 (@Order(5))

### 3. 주의사항
- Order 값은 팀 내에서 일관성 있게 관리
- 너무 많은 Aspect가 하나의 JoinPoint에 적용되지 않도록 주의
- Order 값은 명확한 문서화 필요

## 적용 시나리오

### 1. API 요청 처리
```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    // 실행 순서:
    // 1. SecurityAspect (인증/인가 체크)
    // 2. LoggingAspect (요청 로깅)
    // 3. TransactionAspect (트랜잭션 처리)
    // 4. PerformanceAspect (성능 모니터링)
    return userService.getUser(id);
}
```

### 2. 트랜잭션 중첩 처리
```java
@Service
public class UserService {
    @Transactional
    public void updateUser(User user) {
        // 실행 순서:
        // 1. OuterTransactionAspect (@Order(1))
        // 2. InnerTransactionAspect (@Order(2))
        // 3. ValidationAspect (@Order(3))
        userRepository.save(user);
    }
}
```