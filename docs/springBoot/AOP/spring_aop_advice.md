# Spring AOP Advice 사용 가이드

## 목차
- [개요](#개요)
- [Advice 종류](#advice-종류)
    - [Before Advice](#before-advice)
    - [After Returning Advice](#after-returning-advice)
    - [After Throwing Advice](#after-throwing-advice)
    - [After (Finally) Advice](#after-finally-advice)
    - [Around Advice](#around-advice)

## 개요
Spring AOP(Aspect Oriented Programming)의 Advice는 각각 다른 시점에서 실행되며, 상황에 따라 적절한 Advice를 선택하여 사용해야 합니다.

## Advice 종류

### Before Advice
- **사용 시점**: 메서드 실행 전
- **주요 사용 사례**:
    - 메서드 실행 전 유효성 검사
    - 입력 파라미터 로깅
    - 보안 검사 (인증/인가)
- **예시 코드**:
```java
@Before("execution(* com.example.service.*.*(..))")
public void beforeAdvice(JoinPoint joinPoint) {
    // 메서드 실행 전 로직
    logger.info("Method called: " + joinPoint.getSignature().getName());
}
```

### After Returning Advice
- **사용 시점**: 메서드가 정상적으로 실행 완료된 후
- **주요 사용 사례**:
    - 반환값 검증
    - 결과 로깅
    - 반환값 변환 또는 후처리
- **예시 코드**:
```java
@AfterReturning(pointcut = "execution(* com.example.service.*.*(..))", returning = "result")
public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
    // 메서드 정상 완료 후 로직
    logger.info("Method returned: " + result);
}
```

### After Throwing Advice
- **사용 시점**: 메서드에서 예외 발생 시
- **주요 사용 사례**:
    - 예외 로깅
    - 예외 변환
    - 알림 발송
- **예시 코드**:
```java
@AfterThrowing(pointcut = "execution(* com.example.service.*.*(..))", throwing = "ex")
public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
    // 예외 발생 시 로직
    logger.error("Exception in " + joinPoint.getSignature().getName() + ": " + ex.getMessage());
}
```

### After (Finally) Advice
- **사용 시점**: 메서드 실행 후 (정상/예외 상관없이)
- **주요 사용 사례**:
    - 리소스 정리
    - 로그 기록 완료
    - 트랜잭션 정리
- **예시 코드**:
```java
@After("execution(* com.example.service.*.*(..))")
public void afterAdvice(JoinPoint joinPoint) {
    // 메서드 실행 완료 후 로직 (항상 실행)
    logger.info("Method execution completed: " + joinPoint.getSignature().getName());
}
```

### Around Advice
- **사용 시점**: 메서드 실행 전후 모두
- **주요 사용 사례**:
    - 메서드 실행 시간 측정
    - 트랜잭션 관리
    - 캐싱
    - 메서드 실행 제어
- **예시 코드**:
```java
@Around("execution(* com.example.service.*.*(..))")
public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    // 메서드 실행 전 로직
    long startTime = System.currentTimeMillis();
    
    Object result = null;
    try {
        // 메서드 실행
        result = joinPoint.proceed();
        return result;
    } finally {
        // 메서드 실행 후 로직
        long endTime = System.currentTimeMillis();
        logger.info("Method execution time: " + (endTime - startTime) + "ms");
    }
}
```

## 선택 가이드라인

1. **Before Advice 선택 시**:
    - 메서드 실행 전에만 로직이 필요한 경우
    - 메서드 실행 여부를 결정해야 하는 경우

2. **After Returning Advice 선택 시**:
    - 정상적인 실행 결과에 대한 후처리가 필요한 경우
    - 반환값을 검증하거나 변환해야 하는 경우

3. **After Throwing Advice 선택 시**:
    - 예외 처리에 대한 공통 로직이 필요한 경우
    - 예외 로깅이나 알림이 필요한 경우

4. **After Advice 선택 시**:
    - 성공/실패와 관계없이 항상 실행되어야 하는 로직이 있는 경우
    - 리소스 정리가 필요한 경우

5. **Around Advice 선택 시**:
    - 메서드 실행 전후로 모두 로직이 필요한 경우
    - 메서드 실행을 완전히 제어해야 하는 경우
    - 실행 시간 측정 등 성능 모니터링이 필요한 경우