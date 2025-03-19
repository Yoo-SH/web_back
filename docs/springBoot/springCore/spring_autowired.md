# @Autowired in Spring
## 목차
- [개요](#개요)
- [사용 방법](#사용-방법)
  - [1. 필드 주입 (Field Injection)](#1-필드-주입-field-injection)
  - [2. 생성자 주입 (Constructor Injection)](#2-생성자-주입-constructor-injection)
  - [3. 세터 주입 (Setter Injection)](#3-세터-주입-setter-injection)
- [주입 방식 비교](#주입-방식-비교)
  - [불변성 (Immutability)](#불변성-immutability)
  - [필수 의존성 (Required Dependencies)](#필수-의존성-required-dependencies)
  - [순환 참조 (Circular Dependencies)](#순환-참조-circular-dependencies)
  - [테스트 용이성 (Testability)](#테스트-용이성-testability)
  - [코드 가독성 (Readability)](#코드-가독성-readability)
- [@Autowired 동작 방식](#autowired-동작-방식)
    - [@Qualifier와 함께 사용](#qualifier와-함께-사용)
    - [@Primary와 함께 사용](#primary와-함께-사용)
- [주의 사항](#주의-사항)
- [결론](#결론)

## 개요
`@Autowired`는 Spring Framework에서 의존성을 자동으로 주입하는 데 사용하는 어노테이션입니다. 이를 통해 개발자는 객체를 수동으로 생성하고 관리하는 번거로움을 줄이고, 보다 간결하고 유지보수하기 쉬운 코드를 작성할 수 있습니다.

## 사용 방법
### 1. 필드 주입 (Field Injection)
```java
@Component
public class ExampleService {
   @Autowired
   private ExampleRepository exampleRepository;
}
```
**장점**: 코드가 간결하고, 간단한 경우 쉽게 사용할 수 있음.  
**단점**: 테스트하기 어려우며, 의존성이 명확하지 않음.

**참고**
- 과거 field injection이 존재했지만, 테스트를 어렵게 만들기에 spring.io 팀에서 더 이상 권고하지 않는다.
- 하지만, 과거 블로그 등 게시물에서 종종 보이기도 한다.

### 2. 생성자 주입 (Constructor Injection)
```java
@Component
public class ExampleService {
    private final ExampleRepository exampleRepository;
    
    @Autowired
    public ExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }
}
```
**장점**: 의존성이 명확하게 드러나며, 테스트가 용이함.  
**단점**: 코드가 조금 길어질 수 있음.

### 3. 세터 주입 (Setter Injection)
```java
@Component
public class ExampleService {
    private ExampleRepository exampleRepository;
    
    @Autowired
    public void setExampleRepository(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }
}
```
**장점**: 선택적 의존성을 주입할 때 유용함.  
**단점**: 의존성이 명확하지 않을 수 있음.

### 4. 객체 생명주기와 관련된 타이밍 차이

- 생성자 주입: 객체 생성 시점에 의존성 주입 (가장 먼저)

- 필드/세터 주입: 객체 생성 후 의존성 주입 (나중에)

- Spring은 Bean 생성 시 먼저 생성자를 호출한 후, 필드와 세터 주입을 처리합니다.

- Bean 생성 단계에서 순환 참조를 감지하는 메커니즘이 있어 생성자 주입에서만 이를 초기에 감지할 수 있습니다.

## 주입 방식 비교
각 주입 방식은 서로 다른 특성을 가지고 있으며, 상황에 따라 적절한 방식을 선택해야 합니다.

### 불변성 (Immutability)
- **생성자 주입**: `final` 키워드를 사용하여 불변 객체를 만들 수 있어 Thread-safe한 코드 작성이 가능합니다.
```java
private final ExampleRepository exampleRepository; // 한번 할당 후 변경 불가능
```
- **필드 주입/세터 주입**: 의존성이 언제든지 변경될 수 있어 불변성을 보장할 수 없습니다.
```java
@Autowired
private ExampleRepository exampleRepository; // 언제든지 다른 코드에서 변경 가능
```

### 필수 의존성 (Required Dependencies)
- **생성자 주입**: 객체 생성 시점에 모든 의존성이 주입되므로, 필수 의존성을 명확하게 표현할 수 있습니다.
```java
// 객체 생성 시 exampleRepository가 반드시 필요
public ExampleService(ExampleRepository exampleRepository) {
    this.exampleRepository = exampleRepository;
}
```
- **세터 주입**: `required = false` 옵션을 통해 선택적 의존성을 쉽게 표현할 수 있습니다.
```java
@Autowired(required = false)
public void setOptionalDependency(OptionalService service) {
    this.optionalService = service;
}
```
- **필드 주입**: 필수/선택적 의존성 구분이 불명확하며, `NullPointerException` 위험이 있습니다.

### 순환 참조 (Circular Dependencies)
- **생성자 주입**: 순환 참조가 있을 경우 애플리케이션 구동 시점에 `BeanCurrentlyInCreationException` 예외가 발생하여 문제를 빠르게 발견할 수 있습니다.
```java
// A가 B를 의존하고, B가 다시 A를 의존하는 경우
public class A {
    private final B b;
    public A(B b) { this.b = b; }
}

public class B {
    private final A a;
    public B(A a) { this.a = a; }
}
// 컴파일은 되지만 Spring 컨테이너 초기화 시 예외 발생
```
- **필드/세터 주입**: 순환 참조를 허용하여 런타임에 예상치 못한 문제가 발생할 수 있습니다.

### 테스트 용이성 (Testability)
- **생성자 주입**: 테스트 코드에서 의존성을 쉽게 주입할 수 있어 단위 테스트가 용이합니다.
```java
// 테스트 코드
@Test
public void testService() {
    ExampleRepository mockRepository = Mockito.mock(ExampleRepository.class);
    ExampleService service = new ExampleService(mockRepository);
    // 테스트 로직
}
```
- **필드 주입**: `ReflectionTestUtils`와 같은 도구를 사용해야 하므로 테스트가 복잡해집니다.
```java
@Test
public void testService() {
    ExampleService service = new ExampleService();
    ExampleRepository mockRepository = Mockito.mock(ExampleRepository.class);
    ReflectionTestUtils.setField(service, "exampleRepository", mockRepository);
    // 테스트 로직
}
```
- **세터 주입**: 생성자 주입보다는 덜하지만, 필드 주입보다는 테스트하기 쉽습니다.

### 코드 가독성 (Readability)
- **필드 주입**: 코드가 간결하여 의존성이 적은 경우 읽기 쉽습니다.
- **생성자 주입**: 의존성이 명시적으로 드러나지만, 의존성이 많을 경우 코드가 길어질 수 있습니다. Lombok의 `@RequiredArgsConstructor`를 사용하면 이 문제를 해결할 수 있습니다.
```java
@Service
@RequiredArgsConstructor
public class ExampleService {
    private final ExampleRepository exampleRepository;
    private final AnotherService anotherService;
    // 생성자가 자동으로 생성됨
}
```
- **세터 주입**: 여러 메서드로 인해 코드가 길어질 수 있으며, 의존성 파악이 어려울 수 있습니다.

## @Autowired 동작 방식
Spring 컨테이너는 `@Autowired`가 붙은 필드 또는 생성자를 탐색하여 해당 타입의 빈(Bean)을 자동으로 주입합니다.
- 동일한 타입의 빈이 여러 개 존재하는 경우, `@Qualifier`를 사용하여 특정 빈을 선택할 수 있습니다.
- 주입할 빈이 없을 경우, `@Autowired(required = false)`를 설정하면 예외를 방지할 수 있습니다.

### @Qualifier와 함께 사용
```java
@Component
public class ExampleService {
    private final ExampleRepository exampleRepository;
    
    @Autowired
    public ExampleService(@Qualifier("specificExampleRepository") ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }
}
```

### @Primary와 함께 사용
```java
@Component
@Primary
public class PrimaryExampleRepository implements ExampleRepository {
   // 구현 내용
}

@Component
public class AnotherExampleRepository implements ExampleRepository {
   // 구현 내용
}

@Component
public class ExampleService {
   private final ExampleRepository exampleRepository;
   
   @Autowired 
   public ExampleService(ExampleRepository exampleRepository) { 
      this.exampleRepository = exampleRepository; //Quarifier로 명시하지 않았으므로 Primary로 지정된 클래스 객체가 의존성 주입됨
   }
}
```

## 주의 사항
1. **필드 주입보다는 생성자 주입을 권장**합니다.
2. **Lombok과 함께 사용**하면 더욱 간결한 코드 작성이 가능합니다.
   ```java
   @Service
   @RequiredArgsConstructor
   public class ExampleService {
       private final ExampleRepository exampleRepository;
   }
   ```
3. **순환 참조(Circular Dependency) 문제**가 발생할 수 있으므로 구조를 잘 설계해야 합니다.

## 결론
- `@Autowired`는 Spring의 강력한 DI(Dependency Injection) 기능을 활용할 수 있도록 도와줍니다.
- 생성자 주입을 기본으로 사용하며, 필요한 경우 `@Qualifier` 등을 활용해 빈을 명확하게 지정할 수 있습니다.
- 각 주입 방식은 서로 다른 장단점이 있으며, Spring에서는 일반적으로 생성자 주입을 권장합니다.
- Lombok과 함께 사용하면 더욱 깔끔한 코드 작성이 가능합니다.

### 📌 참고 자료
- [Spring 공식 문서](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-autowired)