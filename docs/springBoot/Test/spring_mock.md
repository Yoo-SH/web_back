# Spring Mock 가이드

## 목차
1. [Spring Mock 소개](#spring-mock-소개)
2. [주요 Mock 프레임워크](#주요-mock-프레임워크)
3. [Mockito 활용 방법](#mockito-활용-방법)
4. [Spring Test 환경에서의 Mock](#spring-test-환경에서의-mock)
5. [Mock 사용 시 모범 사례](#mock-사용-시-모범-사례)
6. [Mock 객체 비교표](#mock-객체-비교표)

## Spring Mock 소개

Spring Mock은 Spring 프레임워크 기반 애플리케이션에서 단위 테스트와 통합 테스트를 수행할 때 실제 객체 대신 사용하는 가짜(Mock) 객체를 의미합니다. Mock 객체는 실제 객체의 행동을 모방하여 테스트를 독립적으로 실행할 수 있게 하고, 외부 의존성을 제거하여 테스트 속도를 높이고 안정성을 확보합니다.

### Mock 객체의 주요 목적
- 의존성 격리: 테스트 대상 코드를 외부 의존성으로부터 분리
- 테스트 속도 향상: 데이터베이스, 네트워크 등 느린 외부 리소스 접근 회피
- 예측 가능한 테스트 환경 구성: 특정 상황이나 에러 케이스 시뮬레이션
- 테스트 제어력 향상: 메소드 호출 여부, 호출 횟수, 인자 값 등 검증

## 주요 Mock 프레임워크

### Mockito
Java에서 가장 널리 사용되는 Mock 프레임워크로, Spring과 함께 사용하기 적합합니다.

### PowerMock
Mockito의 확장으로, static 메소드, private 메소드, final 클래스 등을 Mock 할 수 있습니다.

### EasyMock
Mockito 이전에 많이 사용되던 프레임워크입니다.

### Spring MockMvc
Spring MVC 컨트롤러를 테스트하기 위한 프레임워크입니다.

### WireMock
HTTP 기반 API를 Mock하는 도구입니다.

## Mockito 활용 방법

### 기본 설정
```java
// build.gradle
testImplementation 'org.mockito:mockito-core:5.x.x'
testImplementation 'org.mockito:mockito-junit-jupiter:5.x.x'
```

### 기본 사용법
```java
// Mock 객체 생성
UserRepository userRepository = Mockito.mock(UserRepository.class);

// 동작 정의
Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "홍길동")));

// 메소드 호출 검증
Mockito.verify(userRepository).findById(1L);
```

### 주요 애노테이션
- `@Mock`: Mock 객체 생성
- `@InjectMocks`: Mock 객체를 주입받을 대상 객체
- `@Spy`: 실제 객체의 일부 메소드만 Mock
- `@Captor`: 메소드 호출 시 전달된 인자를 캡처

### 예제 코드
```java
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void testGetUser() {
        // given
        User user = new User(1L, "홍길동");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        // when
        User result = userService.getUser(1L);
        
        // then
        assertEquals("홍길동", result.getName());
        verify(userRepository).findById(1L);
    }
}
```

## Spring Test 환경에서의 Mock

### @MockBean
Spring Boot Test에서 제공하는 애노테이션으로, Spring ApplicationContext에 Mock 객체를 등록합니다.

```java
@SpringBootTest
public class UserControllerTest {

    @MockBean
    private UserService userService;
    
    @Autowired
    private UserController userController;
    
    @Test
    void testGetUser() {
        // Mock 설정
        when(userService.getUser(1L)).thenReturn(new User(1L, "홍길동"));
        
        // 테스트 수행
        User result = userController.getUser(1L);
        
        // 검증
        assertEquals("홍길동", result.getName());
    }
}
```

### MockMvc
Spring MVC 컨트롤러를 테스트하기 위한 도구입니다.

```java
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        // Mock 설정
        when(userService.getUser(1L)).thenReturn(new User(1L, "홍길동"));
        
        // 테스트 수행 및 검증
        mockMvc.perform(get("/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("홍길동"));
    }
}
```

## Mock 사용 시 모범 사례

1. **필요한 부분만 Mock하기**: 불필요한 Mock은 테스트의 가치를 떨어뜨립니다.
2. **테스트 가독성 유지하기**: 복잡한 Mock 설정은 테스트 이해를 어렵게 합니다.
3. **너무 많은 검증 피하기**: 중요한 동작만 검증하는 것이 좋습니다.
4. **상태 검증 vs 행동 검증**: 상황에 맞게 적절한 검증 방식을 선택하세요.
5. **테스트 격리 유지하기**: 각 테스트는 독립적으로 실행되어야 합니다.

## Mock 객체 비교표



| 특성 | Mockito | PowerMock | EasyMock | Spring MockMvc | WireMock |
|------|---------|-----------|----------|---------------|----------|
| **주요 용도** | 일반 자바 객체 Mock | 정적/private 메소드 Mock | 자바 객체 Mock | Spring MVC 컨트롤러 테스트 | HTTP API Mock |
| **설정 복잡도** | 낮음 | 중간 | 중간 | 낮음 | 중간 |
| **학습 곡선** | 낮음 | 높음 | 중간 | 중간 | 중간 |
| **Spring 통합** | 우수함 | 좋음 | 좋음 | 네이티브 | 좋음 |
| **정적 메소드 Mock** | 불가능 | 가능 | 불가능 | 해당 없음 | 해당 없음 |
| **private 메소드 Mock** | 불가능 | 가능 | 불가능 | 해당 없음 | 해당 없음 |
| **final 클래스/메소드** | 제한적 | 가능 | 불가능 | 해당 없음 | 해당 없음 |
| **검증 문법** | 직관적 | 복잡함 | 명시적 | 직관적 | 직관적 |
| **스파이 지원** | 가능 | 가능 | 제한적 | 해당 없음 | 해당 없음 |
| **인자 캡처** | 가능 | 가능 | 가능 | 가능 | 가능 |
| **테스트 속도** | 빠름 | 느림 | 빠름 | 중간 | 중간 |
| **활발한 개발** | 매우 활발 | 덜 활발 | 덜 활발 | 매우 활발 | 활발 |
| **커뮤니티 지원** | 매우 강함 | 중간 | 약함 | 매우 강함 | 강함 |


Mock 객체 비교표에서 볼 수 있듯이, Mockito는 일반적인 사용 사례에 가장 적합하며 Spring과의 통합도 우수합니다. PowerMock은 정적 메소드나 private 메소드를 Mock해야 할 때 유용하지만, 학습 곡선이 높고 테스트 속도가 느립니다. Spring MockMvc는 Spring MVC 컨트롤러 테스트에 특화되어 있으며, WireMock은 외부 HTTP API를 Mock할 때 유용합니다.

각 프로젝트의 특성과 요구사항에 맞게 적절한 Mock 프레임워크를 선택하는 것이 중요합니다. 대부분의 Spring 프로젝트에서는 Mockito와 Spring MockMvc의 조합이 가장 많이 사용됩니다.