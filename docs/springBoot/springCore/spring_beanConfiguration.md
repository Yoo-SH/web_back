# Spring에서 @Configuration과 @Bean

## 1. @Configuration과 @Bean이란?
Spring 프레임워크에서는 객체를 생성하고 관리하는 방법으로 다양한 빈(Bean) 설정 방식이 존재합니다. 그중에서 `@Configuration`과 `@Bean` 어노테이션을 사용하면 자바 기반 설정(Java-based Configuration) 방식으로 스프링 컨테이너에 빈을 등록할 수 있습니다.

### 1.1 @Configuration
- `@Configuration`은 해당 클래스가 하나 이상의 `@Bean` 메서드를 포함하고 있으며, Spring IoC 컨테이너에서 빈 정의를 구성하는 데 사용됨을 나타냅니다.
- 설정 파일 역할을 하며, XML 설정을 대체할 수 있습니다.

### 1.2 @Bean
- `@Bean`은 개발자가 직접 제어할 수 없는 외부 라이브러리 객체나 복잡한 객체를 스프링 컨테이너에 등록할 때 사용됩니다.
- 메서드 레벨에서 선언되며, 해당 메서드가 반환하는 객체가 Spring 빈으로 등록됩니다.

---

## 2. @Configuration과 @Bean이 필요한 이유
1. **명시적인 빈 등록**: XML 기반 설정보다 가독성이 좋고 타입 안전성이 보장됩니다.
2. **외부 라이브러리 사용 시 유용**: XML이 필요 없는 간결한 설정으로 빈을 등록할 수 있습니다.
3. **컴포넌트 스캔(@Component) 방식과 혼용 가능**: 프로젝트의 요구사항에 맞게 설정 방법을 조합할 수 있습니다.
4. **유지보수 용이**: 코드 기반 설정은 IDE의 지원을 받을 수 있어 유지보수가 편리합니다.

---

## 3. 사용 예시
### 3.1 기본적인 @Configuration과 @Bean 예제
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Bean
    public MyService myService() {
        return new MyService();
    }
}
```
위 코드에서 `AppConfig` 클래스는 `@Configuration`을 사용하여 스프링 설정 클래스임을 나타냅니다. 그리고 `myService()` 메서드는 `@Bean`을 사용하여 `MyService` 객체를 스프링 빈으로 등록합니다.

### 3.2 빈을 주입하는 예제
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Bean
    public MyRepository myRepository() {
        return new MyRepository();
    }
    
    @Bean
    public MyService myService(MyRepository myRepository) {
        return new MyService(myRepository);
    }
}
```
위 코드에서는 `MyService`가 `MyRepository`를 필요로 하므로, `@Bean` 메서드의 매개변수로 `MyRepository`를 전달하여 스프링이 자동으로 주입하도록 설정했습니다.

### 3.3 스프링 컨테이너에서 빈 가져오기
```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MyService myService = context.getBean(MyService.class);
        myService.doSomething();
    }
}
```
`AnnotationConfigApplicationContext`를 사용하여 `AppConfig` 클래스를 읽고, `getBean()`을 통해 등록된 빈을 가져올 수 있습니다.

---

## 4. 정리
- `@Configuration`은 스프링 설정 클래스임을 나타냅니다.
- `@Bean`은 개발자가 직접 제어할 수 없는 객체를 스프링 빈으로 등록할 때 사용됩니다.
- XML 설정을 대체할 수 있으며, 명시적인 빈 등록이 가능하여 유지보수가 용이합니다.
- `@Bean`을 활용하여 의존성을 관리하고, 스프링 컨테이너에서 객체를 가져와 사용할 수 있습니다.

