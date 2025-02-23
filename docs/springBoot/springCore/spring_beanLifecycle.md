# Spring Bean LifeCycle

Spring에서 **Bean LifeCycle(빈 생명주기)** 는 스프링 컨테이너에서 관리되는 빈이 생성되고 소멸되기까지의 과정을 의미합니다.
이 과정에서 스프링이 자동으로 빈을 초기화하고 필요한 경우 정리 작업도 수행할 수 있도록 여러 가지 콜백 메서드를 제공합니다.

## 목차
- [Bean LifeCycle의 주요 과정](#1-bean-lifecycle의-주요-과정)
- [@PostConstruct와 @PreDestroy가 필요한 이유](#2-postconstruct와-predestroy가-필요한-이유)
- [Bean LifeCycle 콜백 메서드 예제](#3-bean-lifecycle-콜백-메서드-예제)
- [XML 기반 초기화 및 소멸 메서드 설정](#4-xml-기반-초기화-및-소멸-메서드-설정)
- [`InitializingBean` & `DisposableBean` 인터페이스 사용](#5-initializingbean--disposablebean-인터페이스-사용)
- [`@Bean(initMethod, destroyMethod)` 방식](#6-beaninitmethod-destroymethod-방식)
- [Bean LifeCycle을 커스텀하고 싶다면? (BeanPostProcessor)](#7-bean-lifecycle을-커스텀하고-싶다면-beanpostprocessor)
- [정리](#8-정리)


## **1. Bean LifeCycle의 주요 과정**
Spring 컨테이너에서 빈이 생성되고 사용되며 제거되는 과정은 다음과 같습니다.

1. **객체 생성 (Instantiation)**
    - `@Component`, `@Bean` 등의 방식으로 빈이 생성됨.
    - `new` 키워드를 사용하여 객체가 만들어지고, 기본 생성자가 실행됨.

2. **의존성 주입 (Dependency Injection)**
    - `@Autowired`, `@Inject` 등을 통해 의존성이 주입됨.

3. **초기화 콜백 (Initialization Callback)**
    - 빈이 생성된 후 실행할 초기화 작업을 수행할 수 있음.
    - 방법:
        1. `InitializingBean` 인터페이스의 `afterPropertiesSet()` 메서드 구현
        2. `@PostConstruct` 애노테이션 사용 (권장)
        3. `<bean init-method="메서드명"/>` 또는 `@Bean(initMethod="메서드명")` 사용

4. **사용 (Using Bean)**
    - 애플리케이션에서 빈이 사용됨.

5. **소멸 전 콜백 (Destruction Callback)**
    - 컨테이너가 종료될 때 빈이 정리 작업을 수행할 수 있음.
    - 방법:
        1. `DisposableBean` 인터페이스의 `destroy()` 메서드 구현
        2. `@PreDestroy` 애노테이션 사용 (권장)
        3. `<bean destroy-method="메서드명"/>` 또는 `@Bean(destroyMethod="메서드명")` 사용

6. **객체 소멸 (Destruction)**
    - 빈이 제거되고, GC(가비지 컬렉터)에 의해 최종적으로 소멸됨.



## **2. @PostConstruct와 @PreDestroy가 필요한 이유**
### **1) @PostConstruct (초기화 콜백)**
- 빈이 생성된 후 필요한 초기화 작업을 수행하기 위해 사용됩니다.
- 생성자만으로는 의존성 주입이 완료된 후 실행할 로직을 실행하기 어렵기 때문에 사용됩니다.
- 예를 들어, **DB 연결 설정, 외부 API 호출, 캐시 로딩** 등의 초기화 작업이 필요할 때 활용됩니다.

### **2) @PreDestroy (소멸 전 콜백)**
- 애플리케이션이 종료되거나 빈이 제거되기 전에 **리소스를 정리하는 작업**을 수행할 수 있습니다.
- 예를 들어, **DB 연결 해제, 파일 정리, 쓰레드 종료, 로그 저장** 등의 작업을 수행할 수 있습니다.
- 명확한 정리 작업을 수행하면 **메모리 누수 방지 및 리소스 낭비를 줄이는 효과**를 얻을 수 있습니다.



## **3. Bean LifeCycle 콜백 메서드 예제**
```java
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class MyBean {

    public MyBean() {
        System.out.println("1. MyBean 생성자 호출");
    }

    @PostConstruct
    public void init() {
        System.out.println("2. @PostConstruct - 빈 초기화");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("3. @PreDestroy - 빈 소멸");
    }
}
```



## **4. XML 기반 초기화 및 소멸 메서드 설정**
```xml
<bean id="myBean" class="com.example.MyBean" init-method="initMethod" destroy-method="destroyMethod"/>
```
```java
public class MyBean {
    public void initMethod() {
        System.out.println("초기화 메서드 실행");
    }
    
    public void destroyMethod() {
        System.out.println("소멸 메서드 실행");
    }
}
```



## **5. `InitializingBean` & `DisposableBean` 인터페이스 사용**
```java
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class MyBean implements InitializingBean, DisposableBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("초기화 작업 실행 (afterPropertiesSet)");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("소멸 작업 실행 (destroy)");
    }
}
```
> 하지만, 인터페이스를 직접 구현하는 방식은 강한 결합도를 초래하므로 `@PostConstruct` 및 `@PreDestroy`를 사용하는 것이 더 권장됩니다.



## **6. `@Bean(initMethod, destroyMethod)` 방식**
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    
    @Bean(initMethod = "init", destroyMethod = "cleanup")
    public MyBean myBean() {
        return new MyBean();
    }
}

class MyBean {
    public void init() {
        System.out.println("Bean 초기화 (init)");
    }
    
    public void cleanup() {
        System.out.println("Bean 소멸 (cleanup)");
    }
}
```



## **7. Bean LifeCycle을 커스텀하고 싶다면? (BeanPostProcessor)**
Spring에서는 `BeanPostProcessor`를 이용하여 빈의 초기화 과정 전후에 원하는 로직을 실행할 수 있습니다.

```java
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("Before Initialization: " + beanName);
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("After Initialization: " + beanName);
        return bean;
    }
}
```

이렇게 하면 스프링 컨테이너가 **모든 빈의 초기화 전후에 자동으로 로직을 실행**해 줍니다.



## **8. 정리**
| 단계 | 설명 | 주요 메서드 |
|||-|
| **1. 객체 생성** | 빈 객체가 생성됨 | 생성자 호출 |
| **2. 의존성 주입** | 필요한 의존성이 주입됨 | `@Autowired` 등 |
| **3. 초기화** | 빈의 초기화 작업 수행 | `@PostConstruct`, `afterPropertiesSet()` |
| **4. 사용** | 빈이 애플리케이션에서 사용됨 | - |
| **5. 소멸 전 작업** | 빈이 제거되기 전 정리 작업 수행 | `@PreDestroy`, `destroy()` |
| **6. 소멸** | 빈이 컨테이너에서 제거됨 | - |

스프링에서 Bean LifeCycle을 이해하고 적절한 초기화 및 정리 작업을 수행하면 **안정적인 애플리케이션을 개발**할 수 있습니다! 😊

**참고**
프로토타입의 경우 구성된 파괴 라이프사이클 콜백은 호출되지 않습니다 . 클라이언트 코드는 프로토타입 범위의 객체를 정리하고 프로토타입 빈이 보유하고 있는 값비싼 리소스를 해제해야 합니다.

