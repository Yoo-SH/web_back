
# **Spring 주요 어노테이션 정리 (의존성 주입, 빈 관리, 생명주기, AOP, 설정 등 포함)**  

## **1. 의존성 주입 관련**  
| 어노테이션 | 설명 | 대상 | 사용 예시 |
|-----------|------|------|-----------|
| `@Autowired` | 타입 기반 자동 의존성 주입 | 필드, 생성자, 메서드 | `@Autowired private Service service;` |
| `@Qualifier("beanName")` | 동일한 타입의 여러 빈 중 특정 빈 지정 | 필드, 생성자, 메서드 | `@Autowired @Qualifier("myBean") private Service service;` |
| `@Primary` | 동일 타입의 여러 빈 중 기본 선택 빈 지정 | 클래스 | `@Primary @Component class PrimaryService implements Service {}` |
| `@Resource(name="beanName")` | 이름 기반으로 빈 주입 (JDK 제공) | 필드, 메서드 | `@Resource(name="myBean") private Service service;` |
| `@Inject` | `@Autowired`와 유사, Java 표준(자카르타) 어노테이션 | 필드, 생성자, 메서드 | `@Inject private Service service;` |

---

## **2. 빈 등록 및 설정 관련**  
| 어노테이션 | 설명 | 대상 | 사용 예시 |
|-----------|------|------|-----------|
| `@Component` | 빈으로 등록할 클래스 선언 | 클래스 | `@Component class MyService {}` |
| `@Service` | `@Component`의 특수화, 서비스 클래스 지정 | 클래스 | `@Service class MyService {}` |
| `@Repository` | `@Component`의 특수화, DAO(데이터 액세스) 클래스 지정 | 클래스 | `@Repository class MyRepository {}` |
| `@Controller` | `@Component`의 특수화, MVC 컨트롤러 지정 | 클래스 | `@Controller class MyController {}` |
| `@RestController` | `@Controller` + `@ResponseBody`, RESTful API 컨트롤러 | 클래스 | `@RestController class ApiController {}` |
| `@Configuration` | 설정 클래스 선언, `@Bean` 등록 가능 | 클래스 | `@Configuration class AppConfig {}` |
| `@Bean` | 수동으로 빈을 등록할 때 사용 | 메서드 | `@Bean public MyService myService() { return new MyService(); }` |
| `@ComponentScan("package.name")` | 특정 패키지를 스캔하여 빈 등록 | 클래스 | `@ComponentScan("com.example.services")` |
| `@Import({ConfigClass.class})` | 다른 설정 클래스를 불러와서 사용 | 클래스 | `@Import(DatabaseConfig.class)` |
| `@ImportResource("classpath:config.xml")` | XML 기반 빈 설정을 가져옴 | 클래스 | `@ImportResource("classpath:spring-config.xml")` |

---

## **3. 빈의 생명주기 관련**  
| 어노테이션 | 설명 | 대상 | 사용 예시 |
|-----------|------|------|-----------|
| `@PostConstruct` | 빈 생성 후 초기화 작업 실행 | 메서드 | `@PostConstruct public void init() {}` |
| `@PreDestroy` | 빈 소멸 전에 정리 작업 실행 | 메서드 | `@PreDestroy public void cleanup() {}` |
| `@DependsOn("beanName")` | 해당 빈이 주입되기 전에 특정 빈이 먼저 생성되도록 지정 | 클래스, 메서드 | `@DependsOn("dataSource") public class MyService {}` |
| `@Lazy` | 빈을 지연 초기화(필요할 때 생성)하도록 설정 | 클래스, 필드 | `@Lazy @Component class LazyService {}` |

---

## **4. AOP 관련 (관점 지향 프로그래밍)**  
| 어노테이션 | 설명 | 대상 | 사용 예시 |
|-----------|------|------|-----------|
| `@Aspect` | AOP에서 횡단 관심사를 모듈화할 클래스 지정 | 클래스 | `@Aspect class LoggingAspect {}` |
| `@Before("execution(* com.example..*(..))")` | 대상 메서드 실행 전에 실행됨 | 메서드 | `@Before("execution(* MyService.*(..))") public void logBefore() {}` |
| `@After("execution(* com.example..*(..))")` | 대상 메서드 실행 후 실행됨 | 메서드 | `@After("execution(* MyService.*(..))") public void logAfter() {}` |
| `@AfterReturning("execution(* com.example..*(..))")` | 메서드가 정상적으로 실행된 후 실행됨 | 메서드 | `@AfterReturning("execution(* MyService.*(..))") public void afterReturning() {}` |
| `@AfterThrowing("execution(* com.example..*(..))")` | 메서드에서 예외가 발생한 후 실행됨 | 메서드 | `@AfterThrowing("execution(* MyService.*(..))") public void afterException() {}` |
| `@Around("execution(* com.example..*(..))")` | 대상 메서드 실행 전후 모두 실행됨 | 메서드 | `@Around("execution(* MyService.*(..))") public Object logAround(ProceedingJoinPoint joinPoint) {}` |

---

## **5. 트랜잭션 관련**  
| 어노테이션 | 설명 | 대상 | 사용 예시 |
|-----------|------|------|-----------|
| `@Transactional` | 메서드 또는 클래스 단위의 트랜잭션 관리 | 클래스, 메서드 | `@Transactional public void updateData() {}` |

---

## **6. 스코프(Scope) 관련**  
| 어노테이션 | 설명 | 대상 | 사용 예시 |
|-----------|------|------|-----------|
| `@Scope("singleton")` | (기본값) 애플리케이션당 하나의 인스턴스 유지 | 클래스 | `@Scope("singleton") @Component class SingletonService {}` |
| `@Scope("prototype")` | 요청할 때마다 새로운 인스턴스 생성 | 클래스 | `@Scope("prototype") @Component class PrototypeService {}` |
| `@RequestScope` | HTTP 요청 동안 유지 | 클래스 | `@RequestScope @Component class RequestService {}` |
| `@SessionScope` | 사용자 세션 동안 유지 | 클래스 | `@SessionScope @Component class SessionService {}` |
| `@ApplicationScope` | 애플리케이션 전체 생명주기 동안 유지 | 클래스 | `@ApplicationScope @Component class AppService {}` |

---

## **7. 이벤트(Event) 관련**  
| 어노테이션 | 설명 | 대상 | 사용 예시 |
|-----------|------|------|-----------|
| `@EventListener` | 특정 이벤트 발생 시 실행됨 | 메서드 | `@EventListener(ApplicationReadyEvent.class) public void onStartup() {}` |
| `@ApplicationListener` | 특정 이벤트를 감지하여 처리 | 클래스 | `public class MyListener implements ApplicationListener<ApplicationEvent> {}` |

---

## **8. 인터셉터(Interceptor)와 데코레이터(Decorator)**  
| 어노테이션 | 설명 | 대상 | 사용 예시 |
|-----------|------|------|-----------|
| `@Interceptor` | 횡단 관심사(로깅, 보안 등)를 적용할 클래스 선언 | 클래스 | `@Interceptor public class LoggingInterceptor {}` |
| `@AroundInvoke` | 인터셉터에서 메서드 실행 전후 로직을 정의 | 메서드 | `@AroundInvoke public Object log(InvocationContext ctx) throws Exception {}` |
| `@Decorator` | 기존 빈의 기능을 확장하는 클래스 선언 | 클래스 | `@Decorator public class CustomDecorator extends SomeService {}` |

---