# Spring @Qualifier 애노테이션

## 소개
Spring에서 `@Qualifier` 애노테이션은 동일한 유형의 여러 빈이 존재할 때, 어떤 빈을 주입할지 명확하게 지정하는 데 사용됩니다. 이는 `@Autowired`와 함께 사용되어 다중 빈 충돌 문제를 해결합니다.

## 왜 @Qualifier를 사용할까?
동일한 유형의 여러 빈이 존재할 경우, Spring의 `@Autowired` 애노테이션은 어떤 빈을 주입해야 하는지 알 수 없기 때문에 `NoUniqueBeanDefinitionException` 예외가 발생할 수 있습니다. `@Qualifier`를 사용하면 특정 빈을 명확하게 지정할 수 있습니다.

## 사용 예제
### 1. 여러 개의 빈 정의하기
```java
import org.springframework.stereotype.Component;

@Component("beanA")
public class ServiceA implements MyService {
    @Override
    public void execute() {
        System.out.println("ServiceA 실행");
    }
}

@Component("beanB")
public class ServiceB implements MyService {
    @Override
    public void execute() {
        System.out.println("ServiceB 실행");
    }
}
```

### 2. @Qualifier를 사용하여 특정 빈 지정하기
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Client {
    private final MyService myService;

    @Autowired
    public Client(@Qualifier("beanA") MyService myService) {
        this.myService = myService;
    }

    public void run() {
        myService.execute();
    }
}
```

## 대체 사용 방법
`@Component("beanA")`와 같이 빈 이름을 지정하는 대신, `@Bean` 메서드와 함께 `@Qualifier`를 사용할 수도 있습니다.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    @Qualifier("beanA")
    public MyService serviceA() {
        return new ServiceA();
    }

    @Bean
    @Qualifier("beanB")
    public MyService serviceB() {
        return new ServiceB();
    }
}
```

## 결론
- `@Qualifier`는 동일한 유형의 여러 빈이 있을 때 유용합니다.
- 생성자, 세터, 필드 주입에 적용할 수 있습니다.
- 모호성을 방지하고 올바른 빈이 주입되도록 보장합니다.

`@Qualifier`를 적절히 사용하면 Spring 애플리케이션의 의존성 주입을 더욱 깔끔하고 관리하기 쉽게 만들 수 있습니다.