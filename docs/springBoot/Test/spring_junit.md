# JUnit 테스트 종합 가이드

JUnit은 자바용 단위 테스트 프레임워크로, 코드의 작은 단위가 예상대로 작동하는지 확인하는 데 사용됩니다. 아래에서 JUnit에 관한 주요 개념과 사용법을 설명해 드리겠습니다.

# JUnit 테스트 종합 가이드

## 목차
1. [JUnit 기본 개념](#junit-기본-개념)
2. [JUnit 5 주요 특징](#junit-5-주요-특징)
3. [프로젝트 설정](#프로젝트-설정)
4. [기본 테스트 작성](#기본-테스트-작성)
5. [@BeforeEach와 다른 라이프사이클 어노테이션](#beforeeach와-다른-라이프사이클-어노테이션)
6. [다양한 단언(Assertions) 메서드](#다양한-단언assertions-메서드)
7. [매개변수화된 테스트](#매개변수화된-테스트)
8. [테스트 비활성화 및 조건부 테스트](#테스트-비활성화-및-조건부-테스트)
9. [중첩 테스트](#중첩-테스트)
10. [테스트 순서 지정](#테스트-순서-지정)
11. [Display 이름 사용하기](#display-이름-사용하기)
12. [모킹 프레임워크와 통합](#모킹-프레임워크와-통합)
13. [JUnit 5 확장 모델](#junit-5-확장-모델)
14. [JUnit 주요 개념 요약 표](#junit-주요-개념-요약-표)
15. [JUnit 5 vs JUnit 4 비교표](#junit-5-vs-junit-4-비교표)
16. [라이프사이클 흐름도](#라이프사이클-흐름도)
17. [테스트 실행 구성 옵션](#테스트-실행-구성-옵션)
18. [JUnit과 다른 프레임워크 통합](#junit과-다른-프레임워크-통합)
29. [마무리](#마무리)

JUnit은 자바용 단위 테스트 프레임워크로, 코드의 작은 단위가 예상대로 작동하는지 확인하는 데 사용됩니다. 아래에서 JUnit에 관한 주요 개념과 사용법을 설명해 드리겠습니다.

## JUnit 기본 개념

JUnit은 자바 프로그래밍 언어를 위한 단위 테스트 프레임워크입니다. 현재 가장 널리 사용되는 버전은 JUnit 5이며, 이는 JUnit Platform, JUnit Jupiter, JUnit Vintage 세 가지 하위 프로젝트로 구성되어 있습니다.

## JUnit 5 주요 특징
## JUnit 기본 개념

JUnit은 자바 프로그래밍 언어를 위한 단위 테스트 프레임워크입니다. 현재 가장 널리 사용되는 버전은 JUnit 5이며, 이는 JUnit Platform, JUnit Jupiter, JUnit Vintage 세 가지 하위 프로젝트로 구성되어 있습니다.

## JUnit 5 주요 특징

- 자바 8 이상 지원
- 람다 표현식을 활용한 향상된 테스트 표현
- 확장성이 뛰어난 아키텍처
- 매개변수화된 테스트 지원 강화
- 테스트 그룹화 및 필터링 기능

## 프로젝트 설정

Maven을 사용하는 경우 `pom.xml`에 다음 의존성을 추가합니다:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>
```

Gradle을 사용하는 경우 `build.gradle`에 다음을 추가합니다:

```gradle
testImplementation 'org.junit.jupiter:junit-jupiter:5.9.2'
testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.2'
```

## 기본 테스트 작성

간단한 JUnit 테스트는 다음과 같이 작성합니다:

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    
    @Test
    void testAddition() {
        Calculator calculator = new Calculator();
        assertEquals(5, calculator.add(2, 3), "2 + 3 should equal 5");
    }
}
```

## @BeforeEach와 다른 라이프사이클 어노테이션

JUnit에서는 테스트 실행 전후에 특정 작업을 수행할 수 있는 여러 라이프사이클 어노테이션을 제공합니다.

### @BeforeEach

`@BeforeEach` 어노테이션이 붙은 메서드는 각 테스트 메서드가 실행되기 전에 실행됩니다. 이는 테스트마다 필요한 준비 작업(셋업)을 수행하는 데 유용합니다.

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {
    private BankAccount account;
    
    @BeforeEach
    void setUp() {
        account = new BankAccount(1000); // 각 테스트 전에 1000원이 들어있는 계좌 생성
    }
    
    @Test
    void testDeposit() {
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }
    
    @Test
    void testWithdraw() {
        account.withdraw(300);
        assertEquals(700, account.getBalance());
    }
}
```

위 예제에서 `setUp()` 메서드는 각 테스트 메서드(`testDeposit`, `testWithdraw`)가 실행되기 전에 매번 실행됩니다. 따라서 각 테스트는 항상 1000원이 들어있는 새 계좌로 시작합니다.

### 다른 주요 라이프사이클 어노테이션

- **@AfterEach**: 각 테스트 메서드가 실행된 후에 실행됩니다. 테스트에 사용된 리소스를 정리하는 데 유용합니다.
- **@BeforeAll**: 모든 테스트 메서드가 실행되기 전에 한 번만 실행됩니다. 정적(static) 메서드여야 합니다.
- **@AfterAll**: 모든 테스트 메서드가 실행된 후에 한 번만 실행됩니다. 정적(static) 메서드여야 합니다.

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private static Database db;
    
    @BeforeAll
    static void initAll() {
        db = new Database("jdbc:mysql://localhost/testdb");
        db.connect();
    }
    
    @BeforeEach
    void init() {
        db.clearTables();
        db.initTestData();
    }
    
    @Test
    void testQuery() {
        Result result = db.execute("SELECT * FROM users");
        assertEquals(3, result.size());
    }
    
    @AfterEach
    void tearDown() {
        db.rollback();
    }
    
    @AfterAll
    static void tearDownAll() {
        db.close();
    }
}
```

## 다양한 단언(Assertions) 메서드

JUnit은 다양한 단언 메서드를 제공합니다:

```java
// 기본 단언
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);
assertTrue(condition);
assertFalse(condition);
assertNull(object);
assertNotNull(object);

// 예외 검증
assertThrows(NullPointerException.class, () -> service.process(null));

// 그룹화된 단언
assertAll(
    () -> assertEquals(4, calculator.add(2, 2)),
    () -> assertEquals(0, calculator.subtract(2, 2)),
    () -> assertEquals(4, calculator.multiply(2, 2))
);

// 시간 제한 설정
assertTimeout(Duration.ofMillis(100), () -> timeConsumingOperation());
```

## 매개변수화된 테스트

동일한 테스트를 다양한 입력 값으로 실행하려면 매개변수화된 테스트를 사용할 수 있습니다:

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsTest {
    
    @ParameterizedTest
    @CsvSource({
        "hello, HELLO",
        "JUnit, JUNIT",
        "Java, JAVA"
    })
    void testToUpperCase(String input, String expected) {
        assertEquals(expected, input.toUpperCase());
    }
}
```

## 테스트 비활성화 및 조건부 테스트

특정 조건에서만 테스트를 실행하도록 설정할 수 있습니다:

```java
// 테스트 비활성화
@Disabled("해당 기능 구현 중")
@Test
void testUnfinishedFeature() {
    // ...
}

// 조건부 테스트
@EnabledOnOs(OS.WINDOWS)
@Test
void testWindowsOnly() {
    // Windows에서만 실행
}

@EnabledIfSystemProperty(named = "env", matches = "staging")
@Test
void testOnlyOnStagingEnv() {
    // 시스템 속성 env=staging인 경우에만 실행
}
```

## 중첩 테스트

`@Nested` 어노테이션을 사용하여 테스트를 그룹화할 수 있습니다:

```java
public class CustomerServiceTest {
    private CustomerService service;
    
    @BeforeEach
    void setUp() {
        service = new CustomerService();
    }
    
    @Nested
    class WhenCustomerExists {
        private Customer existingCustomer;
        
        @BeforeEach
        void setUpCustomer() {
            existingCustomer = new Customer("John");
            service.addCustomer(existingCustomer);
        }
        
        @Test
        void shouldReturnCustomerWhenSearchingById() {
            assertEquals(existingCustomer, service.findById(existingCustomer.getId()));
        }
    }
    
    @Nested
    class WhenCustomerDoesNotExist {
        @Test
        void shouldReturnNullWhenSearchingById() {
            assertNull(service.findById("unknown"));
        }
    }
}
```

## 테스트 순서 지정

기본적으로 JUnit은 테스트 메서드 실행 순서를 보장하지 않습니다. 특정 순서로 테스트를 실행하려면:

```java
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderedTests {
    
    @Test
    @Order(3)
    void testC() {
        // 세 번째로 실행
    }
    
    @Test
    @Order(1)
    void testA() {
        // 첫 번째로 실행
    }
    
    @Test
    @Order(2)
    void testB() {
        // 두 번째로 실행
    }
}
```

## Display 이름 사용하기

테스트 결과를 더 읽기 쉽게 만들기 위해 표시 이름을 사용할 수 있습니다:

```java
@DisplayName("문자열 유틸리티 테스트")
public class StringUtilsTest {
    
    @Test
    @DisplayName("문자열이 비어있는지 확인")
    void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty("hello"));
    }
}
```

## 모킹 프레임워크와 통합

JUnit은 Mockito와 같은 모킹 프레임워크와 함께 사용하면 더 강력해집니다:

```java
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    
    @Test
    void testCreateUser() {
        // 모의 객체 생성
        UserRepository mockRepository = mock(UserRepository.class);
        
        // 모의 객체 동작 정의
        when(mockRepository.save(any(User.class))).thenReturn(true);
        
        // 테스트 대상 객체 생성 및 모의 객체 주입
        UserService service = new UserService(mockRepository);
        
        // 테스트 실행
        boolean result = service.createUser("john@example.com", "password");
        
        // 검증
        assertTrue(result);
        verify(mockRepository).save(any(User.class));
    }
}
```

## JUnit 5 확장 모델

JUnit 5는 확장 모델을 통해 기능을 확장할 수 있습니다:

```java
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SpringIntegrationTest {
    // 스프링 통합 테스트
}
```



## JUnit 주요 개념 요약 표

| 구분 | 내용 | 예시/사용법 |
|------|------|-------------|
| **기본 어노테이션** | | |
| `@Test` | 테스트 메서드 지정 | `@Test void testAddition() { ... }` |
| `@BeforeEach` | 각 테스트 메서드 실행 전 호출 | `@BeforeEach void setUp() { ... }` |
| `@AfterEach` | 각 테스트 메서드 실행 후 호출 | `@AfterEach void tearDown() { ... }` |
| `@BeforeAll` | 모든 테스트 실행 전 한 번만 호출 (static) | `@BeforeAll static void initAll() { ... }` |
| `@AfterAll` | 모든 테스트 실행 후 한 번만 호출 (static) | `@AfterAll static void closeAll() { ... }` |
| `@Disabled` | 테스트 메서드/클래스 비활성화 | `@Disabled("미구현") @Test void test() { ... }` |
| **단언 메서드** | | |
| `assertEquals` | 두 값이 같은지 확인 | `assertEquals(expected, actual)` |
| `assertTrue` | 조건이 참인지 확인 | `assertTrue(condition)` |
| `assertFalse` | 조건이 거짓인지 확인 | `assertFalse(condition)` |
| `assertNull` | 객체가 null인지 확인 | `assertNull(object)` |
| `assertNotNull` | 객체가 null이 아닌지 확인 | `assertNotNull(object)` |
| `assertThrows` | 예외 발생 여부 확인 | `assertThrows(ExceptionType.class, () -> { ... })` |
| `assertAll` | 여러 단언을 그룹화 | `assertAll(() -> {...}, () -> {...})` |
| `assertTimeout` | 제한 시간 내 실행 확인 | `assertTimeout(Duration.ofMillis(100), () -> {...})` |
| **매개변수화 테스트** | | |
| `@ParameterizedTest` | 매개변수화된 테스트 지정 | `@ParameterizedTest` |
| `@ValueSource` | 단일 값 배열 제공 | `@ValueSource(strings = {"a", "b"})` |
| `@CsvSource` | CSV 형식 값 제공 | `@CsvSource({"input, expected", "1, 2"})` |
| `@MethodSource` | 메서드에서 인자 제공 | `@MethodSource("generateInputs")` |
| **조건부 테스트** | | |
| `@EnabledOnOs` | 특정 OS에서만 실행 | `@EnabledOnOs(OS.WINDOWS)` |
| `@DisabledOnOs` | 특정 OS에서 실행 안 함 | `@DisabledOnOs(OS.LINUX)` |
| `@EnabledIfSystemProperty` | 시스템 속성 조건부 실행 | `@EnabledIfSystemProperty(named = "env", matches = "dev")` |
| `@EnabledIf` | 조건식 기반 실행 | `@EnabledIf("customCondition")` |
| **테스트 구성** | | |
| `@Nested` | 중첩 테스트 클래스 | `@Nested class WhenUserExists { ... }` |
| `@DisplayName` | 테스트 표시 이름 지정 | `@DisplayName("사용자 생성 테스트")` |
| `@TestMethodOrder` | 테스트 메서드 실행 순서 지정 | `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` |
| `@Order` | 테스트 메서드 순서 번호 지정 | `@Order(1)` |
| **확장 모델** | | |
| `@ExtendWith` | JUnit 확장 기능 적용 | `@ExtendWith(SpringExtension.class)` |
| `@RegisterExtension` | 필드 기반 확장 등록 | `@RegisterExtension Timer timer = new Timer()` |

## JUnit 5 vs JUnit 4 비교표

| 기능 | JUnit 5 | JUnit 4 |
|------|---------|---------|
| 패키지 | `org.junit.jupiter.api` | `org.junit` |
| 테스트 어노테이션 | `@Test` | `@Test` |
| 전처리 | `@BeforeEach` | `@Before` |
| 후처리 | `@AfterEach` | `@After` |
| 전체 전처리 | `@BeforeAll` | `@BeforeClass` |
| 전체 후처리 | `@AfterAll` | `@AfterClass` |
| 테스트 비활성화 | `@Disabled` | `@Ignore` |
| 예외 테스트 | `assertThrows(Exception.class, () -> {})` | `@Test(expected = Exception.class)` |
| 타임아웃 | `assertTimeout(Duration.ofMillis(100), () -> {})` | `@Test(timeout = 100)` |
| 매개변수화 테스트 | `@ParameterizedTest` | `@RunWith(Parameterized.class)` |
| 확장 모델 | `@ExtendWith` | `@RunWith` |
| 자바 버전 | Java 8 이상 | Java 5 이상 |
| 중첩 테스트 | 기본 지원 (`@Nested`) | 미지원 |
| 람다 표현식 | 지원 | 제한적 지원 |
| 태깅 및 필터링 | `@Tag` | `@Category` |

## 라이프사이클 흐름도

| 실행 순서 | 어노테이션/메서드 | 설명 |
|----------|------------------|-----|
| 1 | `@BeforeAll` | 클래스의 모든 테스트 시작 전 한 번만 실행 |
| 2 | `@BeforeEach` | 각 테스트 메서드 실행 전 실행 |
| 3 | `@Test` | 실제 테스트 메서드 실행 |
| 4 | `@AfterEach` | 각 테스트 메서드 실행 후 실행 |
| 5 | (2-4 단계 반복) | 모든 테스트 메서드에 대해 2-4 반복 |
| 6 | `@AfterAll` | 클래스의 모든 테스트 완료 후 한 번만 실행 |

## 테스트 실행 구성 옵션

| 옵션 | 설명 | 예시 |
|------|------|------|
| 태그 기반 실행 | 특정 태그가 있는 테스트만 실행 | `-tags "integration"` |
| 클래스 기반 실행 | 특정 클래스의 테스트만 실행 | `-c "com.example.UserServiceTest"` |
| 패키지 기반 실행 | 특정 패키지의 테스트만 실행 | `-p "com.example.service"` |
| 테스트 반복 | 테스트를 여러 번 반복 실행 | `@RepeatedTest(5)` |
| 병렬 실행 | 테스트를 병렬로 실행 | `junit.jupiter.execution.parallel.enabled=true` |

## JUnit과 다른 프레임워크 통합

| 프레임워크 | 통합 방법 | 용도 |
|-----------|-----------|------|
| Mockito | 의존성 추가 + `@ExtendWith(MockitoExtension.class)` | 모의 객체 생성 및 검증 |
| Spring | `@ExtendWith(SpringExtension.class)` | 스프링 통합 테스트 |
| AssertJ | 의존성 추가 후 `assertThat()` 사용 | 가독성 높은 단언문 작성 |
| Hamcrest | 의존성 추가 후 `assertThat(value, matcher)` 사용 | 매처 기반 단언 |
| Testcontainers | `@Testcontainers` + `@Container` | 테스트용 컨테이너 관리 |


## 마무리

JUnit은 자바 애플리케이션의 품질을 보장하는 데 필수적인 도구입니다. `@BeforeEach`와 같은 라이프사이클 어노테이션을 활용하면 테스트 코드를 더 깔끔하고 유지보수하기 쉽게 만들 수 있습니다. JUnit 5는 이전 버전보다 더 강력하고 유연한 기능을 제공하므로, 최신 자바 프로젝트에서는 JUnit 5를 사용하는 것이 좋습니다.
