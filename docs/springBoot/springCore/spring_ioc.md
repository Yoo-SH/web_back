# Spring Inversion of Control (IoC)

## 개요
Spring Framework의 핵심 개념 중 하나인 **Inversion of Control(IoC)** 는 객체의 생성 및 관리 권한을 개발자가 아닌 **Spring Container** 가 담당하도록 하는 디자인 패턴입니다.  
이를 통해 **객체 간의 결합도를 낮추고**, 유지보수성과 테스트 용이성을 높일 수 있습니다.

---

## IoC란?
**Inversion of Control (제어의 역전)** 은 애플리케이션의 흐름을 개발자가 직접 제어하는 것이 아니라, 프레임워크나 컨테이너가 대신 제어하는 것을 의미합니다.

기존의 객체 생성 방식에서는 개발자가 직접 객체를 생성하고 관리해야 했습니다.

```java
class Car {
    private Engine engine;

    public Car() {
        this.engine = new Engine(); // 직접 객체 생성
    }
}
```

그러나 **IoC 컨테이너** 를 사용하면 객체의 생성과 관리를 Spring이 대신 수행합니다.

```java
@Component
class Engine {
}

@Component
class Car {
    private final Engine engine;

    @Autowired
    public Car(Engine engine) { // Spring이 Engine 객체를 주입
        this.engine = engine;
    }
}
```

---

## Spring Container와 IoC
Spring에서 IoC를 구현하는 핵심 요소는 **Spring Container** 입니다.  
Spring Container는 애플리케이션에서 사용할 객체(Bean)를 생성하고, 의존성을 관리하며, 필요할 때 Bean을 제공하는 역할을 합니다.

### Spring Container의 역할
1. **객체(Bean) 생성**: 애플리케이션 실행 시 필요한 객체를 생성합니다.
2. **의존성 주입 (Dependency Injection, DI) 수행**: Bean 간의 관계를 자동으로 연결해줍니다.
3. **Bean의 생명주기 관리**: 객체 생성부터 소멸까지 관리합니다.

### 대표적인 Spring Container
1. **BeanFactory**
    - 가장 기본적인 IoC 컨테이너
    - Lazy Initialization(지연 초기화) 지원
    - 리소스가 적을 때 사용

2. **ApplicationContext** (일반적으로 가장 많이 사용)
    - BeanFactory의 확장판
    - 다양한 부가 기능 제공 (AOP, 이벤트 리스너, 국제화 지원 등)

```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
Car car = context.getBean(Car.class);
```

---

## IoC와 Dependency Injection (DI)의 관계
Spring에서 IoC를 구현하는 방식 중 하나가 바로 **Dependency Injection(DI, 의존성 주입)** 입니다.

### Dependency Injection(DI)란?
- 객체가 직접 의존성을 생성하는 것이 아니라, **외부에서 주입받도록 설계** 하는 방식입니다.
- DI를 통해 **객체 간의 결합도를 낮추고**, 유연성과 테스트 용이성을 높일 수 있습니다.

#### DI의 주요 방식
1. **필드 주입**
   ```java
   @Component
   class Car {
       @Autowired
       private Engine engine;
   }
   ```
    - 간단하지만 **테스트 및 유지보수에 불리** 하므로 지양하는 것이 좋습니다.

2. **생성자 주입 (권장)**
   ```java
   @Component
   class Car {
       private final Engine engine;

       @Autowired
       public Car(Engine engine) {
           this.engine = engine;
       }
   }
   ```
    - **불변성 보장** 및 **테스트 용이성 증가** 등의 장점이 있어 가장 권장되는 방식입니다.

3. **Setter 주입**
   ```java
   @Component
   class Car {
       private Engine engine;

       @Autowired
       public void setEngine(Engine engine) {
           this.engine = engine;
       }
   }
   ```
    - 선택적으로 의존성을 주입할 때 유용하지만, 객체의 상태가 변경될 수 있어 주의가 필요합니다.

---

```java
// 사용 권고 x
@Component
   class Car {
       @Autowired
       private Engine engine;

   }
```

## IoC + DI의 동작 과정
1. **Spring Container가 실행되면서 필요한 Bean을 생성**
2. **의존성이 필요한 객체를 찾고, DI(Dependency Injection) 방식에 따라 주입**
3. **애플리케이션에서 Bean을 사용**

```java
@Configuration
class AppConfig {
    @Bean
    public Engine engine() {
        return new Engine();
    }

    @Bean
    public Car car(Engine engine) {
        return new Car(engine);
    }
}

ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
Car car = context.getBean(Car.class);
```

---

## IoC & DI의 장점
1. **객체 간의 결합도 감소** → 유지보수 용이
2. **Spring Container가 객체의 생성과 관리를 담당** → 메모리 관리 효율성 증가
3. **DI(Dependency Injection)와 함께 사용 가능** → 의존성 관리 강화
4. **테스트 용이성 증가** → Mock 객체 활용 가능

---

## 결론
Spring IoC는 **Spring Container가 객체를 생성 및 관리하는 개념** 으로, 애플리케이션의 결합도를 낮추고 유지보수성과 확장성을 높입니다.  
이러한 IoC를 구현하는 방식으로 **Dependency Injection(DI)** 를 활용하며, DI를 통해 객체 간의 관계를 자동으로 설정하여 코드의 가독성과 유연성을 높일 수 있습니다.

