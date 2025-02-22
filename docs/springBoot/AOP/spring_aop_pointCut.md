# Spring AOP Pointcuts 사용 가이드

## 목차
- [개요](#개요)
- [Pointcut 표현식 종류](#pointcut-표현식-종류)
- [주요 사용 시나리오](#주요-사용-시나리오)
- [Best Practices](#best-practices)
- [예제 코드](#예제-코드)

## 개요
Spring AOP Pointcut은 어드바이스(Advice)를 적용할 조인 포인트(Join Point)를 선별하는 표현식입니다. 효과적인 Pointcut 사용은 AOP 구현의 핵심입니다.

## Pointcut 표현식 종류

### 1. execution()
- **사용 시기**: 메서드 실행 조인 포인트를 매칭할 때
- **문법**: `execution([접근제어자] 리턴타입 [패키지.클래스.]메서드이름(파라미터))`
- **예시**:
```java
execution(public * com.example.service.*.*(..))
```

### 2. within()
- **사용 시기**: 특정 타입에 속한 모든 메서드를 매칭할 때
- **문법**: `within(타입)`
- **예시**:
```java
within(com.example.service.*)
```

### 3. @annotation
- **사용 시기**: 특정 어노테이션이 적용된 메서드를 매칭할 때
- **문법**: `@annotation(어노테이션타입)`
- **예시**:
```java
@annotation(org.springframework.transaction.annotation.Transactional)
```

### 4. args()
- **사용 시기**: 특정 파라미터 타입을 가진 메서드를 매칭할 때
- **문법**: `args(파라미터타입)`
- **예시**:
```java
args(String, ..)
```

### 5. this()
- **사용 시기**: 프록시 객체를 매칭할 때
- **문법**: `this(타입)`
- **예시**:
```java
this(com.example.service.UserService)
```

### 6. target()
- **사용 시기**: 대상 객체를 매칭할 때
- **문법**: `target(타입)`
- **예시**:
```java
target(com.example.service.UserService)
```

## 주요 사용 시나리오

### 1. 서비스 계층 로깅
```java
@Pointcut("execution(* com.example.service.*.*(..))")
public void serviceLayer() {}
```

### 2. 트랜잭션 관리
```java
@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
public void transactionalMethod() {}
```

### 3. 성능 모니터링
```java
@Pointcut("execution(* com.example.*.*(..)) && @annotation(com.example.annotation.Monitor)")
public void monitoredMethod() {}
```

## Best Practices

1. **명명 규칙**
    - Pointcut 표현식에 의미있는 이름 부여
    - 재사용 가능한 Pointcut은 별도 클래스로 분리

2. **성능 고려사항**
    - within()은 execution()보다 성능이 좋음
    - 너무 광범위한 Pointcut 사용 지양

3. **조합 사용**
    - && (AND), || (OR), ! (NOT) 연산자를 활용하여 정교한 Pointcut 정의

## 예제 코드

```java
@Aspect
@Component
public class LoggingAspect {
    
    // 서비스 계층 메서드 실행
    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceLayer() {}
    
    // 특정 어노테이션이 붙은 메서드
    @Pointcut("@annotation(com.example.annotation.Loggable)")
    public void loggableMethod() {}
    
    // 두 Pointcut 조합
    @Around("serviceLayer() && loggableMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Advice 로직
    }
}
```