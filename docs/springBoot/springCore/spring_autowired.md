# @Autowired in Spring

## 개요
`@Autowired`는 Spring Framework에서 의존성을 자동으로 주입하는 데 사용하는 어노테이션입니다. 이를 통해 개발자는 객체를 수동으로 생성하고 관리하는 번거로움을 줄이고, 보다 간결하고 유지보수하기 쉬운 코드를 작성할 수 있습니다.

---

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


__참고__
- 과거 field injection이 존재했지만, 테스트를 어렵게 만들기에 spring.io 팀에서 더 이상 권고하지 않는다.
- 하지만, 과거 블로그 등 게시물에서 종종 보이기도 한다.
---

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

---

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

---

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

---

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

---

## 결론
- `@Autowired`는 Spring의 강력한 DI(Dependency Injection) 기능을 활용할 수 있도록 도와줍니다.
- 생성자 주입을 기본으로 사용하며, 필요한 경우 `@Qualifier` 등을 활용해 빈을 명확하게 지정할 수 있습니다.
- Lombok과 함께 사용하면 더욱 깔끔한 코드 작성이 가능합니다.

---

### 📌 참고 자료
- [Spring 공식 문서](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-autowired)

