# Spring Bean & Bean Scope

## 1. Bean이란?
Spring에서 **Bean**은 Spring IoC (Inversion of Control) 컨테이너가 관리하는 객체를 의미합니다. 일반적으로 `@Component`, `@Service`, `@Repository`, `@Controller` 같은 어노테이션을 사용하거나 `@Configuration` 클래스에서 `@Bean`을 통해 객체를 정의하고 관리할 수 있습니다.

```java
@Component
public class MyBean {
    public void doSomething() {
        System.out.println("Hello, Spring Bean!");
    }
}
```

위와 같이 `@Component`를 사용하면 Spring 컨테이너가 자동으로 해당 클래스를 Bean으로 등록합니다.

---

## 2. Bean Scope란?
**Bean Scope(빈의 범위)**는 Spring 컨테이너에서 Bean이 생성되고 관리되는 방식을 결정합니다. Spring은 여러 가지 Bean Scope를 제공하는데, 대표적인 것들을 살펴보겠습니다.

### 1) Singleton (기본값)
- Spring 컨테이너당 하나의 인스턴스만 생성됩니다.
- 같은 Bean을 여러 번 주입해도 동일한 객체를 반환합니다.
- 메모리를 절약할 수 있지만 상태를 공유하기 때문에 주의해야 합니다.

```java
@Component
@Scope("singleton") // 기본값이므로 생략 가능
public class SingletonBean {
}
```

### 2) Prototype
- 매번 새로운 인스턴스를 생성합니다.
- 상태를 공유할 필요가 없는 경우에 사용합니다.

```java
@Component
@Scope("prototype")
public class PrototypeBean {
}
```

### 3) Request (웹 애플리케이션 전용)
- HTTP 요청당 하나의 인스턴스를 생성합니다.
- 주로 웹 애플리케이션에서 사용됩니다.

```java
@Component
@Scope("request")
public class RequestBean {
}
```

### 4) Session (웹 애플리케이션 전용)
- HTTP 세션당 하나의 인스턴스를 생성합니다.

```java
@Component
@Scope("session")
public class SessionBean {
}
```

### 5) Application (웹 애플리케이션 전용)
- 서블릿 컨텍스트 당 하나의 인스턴스를 생성합니다.

```java
@Component
@Scope("application")
public class ApplicationBean {
}
```

### 참고
"prototype" 범위 빈의 경우, Spring은 destroy 메서드를 호출하지 않습니다. 헐!

---

다른 범위와 달리 Spring은 프로토타입 빈의 전체 수명 주기를 관리하지 않습니다 . 컨테이너는 프로토타입 객체를 인스턴스화하고, 구성하고, 그 밖의 방법으로 조립한 다음 이를 클라이언트에 전달하며, 해당 프로토타입 인스턴스에 대한 추가 기록은 없습니다.

따라서, 범위에 관계없이 모든 객체에서 초기화 라이프사이클 콜백 메서드가 호출되지만, 프로토타입의 경우 구성된 파괴 라이프사이클 콜백은 호출되지 않습니다 . 클라이언트 코드는 프로토타입 범위의 객체를 정리하고 프로토타입 빈이 보유하고 있는 값비싼 리소스를 해제해야 합니다.
