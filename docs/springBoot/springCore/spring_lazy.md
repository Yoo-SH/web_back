# Spring에서의 지연 초기화(Lazy Initialization)

## 목차
- [소개](#소개)
- [지연 초기화 활성화 방법](#지연-초기화-활성화-방법)
  - [`@Lazy` 애노테이션 사용](#1-lazy-애노테이션-사용)
  - [전역 설정](#2-전역-설정)
- [지연 초기화의 장점](#지연-초기화의-장점)
- [지연 초기화의 단점](#지연-초기화의-단점)
- [결론](#결론)

## 소개
Spring에서 지연 초기화(Lazy Initialization)란 빈(bean)의 생성 시점을 실제로 필요할 때까지 지연시키는 것을 의미합니다. 기본적으로 Spring은 모든 싱글톤 빈을 애플리케이션 시작 시점에 초기화(Eager Initialization)합니다. 그러나 지연 초기화를 활성화하면, 해당 빈이 처음 접근될 때 생성됩니다.

## 지연 초기화 활성화 방법
### 1. `@Lazy` 애노테이션 사용
특정 빈에 대해 지연 초기화를 활성화하려면 `@Lazy` 애노테이션을 사용할 수 있습니다:

```java
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class LazyBean {
    public LazyBean() {
        System.out.println("LazyBean 초기화됨");
    }
}
```

### 2. 전역 설정
전체 애플리케이션에서 지연 초기화를 활성화하려면 `application.properties` 파일에서 다음과 같이 설정할 수 있습니다:

```properties
spring.main.lazy-initialization=true
```

또는 Java 기반 설정에서 다음과 같이 적용할 수 있습니다:

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy // 이 설정 클래스의 모든 빈에 대해 지연 초기화 적용
public class AppConfig {
}
```

## 지연 초기화의 장점
1. **빠른 애플리케이션 시작 시간**: 빈을 시작 시점에 생성하지 않으므로 애플리케이션이 더 빠르게 실행됩니다. 특히 대규모 애플리케이션에서 유용합니다.
2. **메모리 사용 최적화**: 필요한 빈만 생성되므로 리소스를 절약할 수 있습니다.
3. **개발 환경에서 성능 향상**: 불필요한 빈 초기화를 방지하면 디버깅 및 테스트 속도가 향상됩니다.

## 지연 초기화의 단점
1. **런타임 성능 영향**: 지연 초기화된 빈을 처음 접근할 때 인스턴스화 과정에서 지연이 발생할 수 있습니다.
2. **잠재적 오류 발견 지연**: 빈의 종속성이 제대로 설정되지 않은 경우, 오류가 애플리케이션 시작 시가 아닌 런타임에 발생하여 디버깅이 어려워질 수 있습니다.
3. **필수 빈에는 적합하지 않음**: 데이터베이스 연결이나 캐시와 같은 중요한 빈은 사전 초기화하는 것이 더 나을 수 있습니다.

## 결론
지연 초기화는 Spring 애플리케이션의 시작 시간을 단축하고 메모리 사용을 최적화하는 유용한 기술입니다. 하지만 런타임 성능 저하 및 디버깅 문제를 고려하여 적절한 빈에만 적용하는 것이 중요합니다. 신중하게 선택하여 사용하면 애플리케이션 성능과 효율성을 균형 있게 유지할 수 있습니다.

