# Backend Communication Patterns: Stateful vs Stateless

## 목차
- [개요](#개요)
- [상태란 무엇인가?](#상태란-무엇인가)
- [Stateful 패턴](#stateful-패턴)
  - [특징](#stateful-특징)
  - [장점](#stateful-장점)
  - [단점](#stateful-단점)
  - [사용 사례](#stateful-사용-사례)
- [Stateless 패턴](#stateless-패턴)
  - [특징](#stateless-특징)
  - [장점](#stateless-장점)
  - [단점](#stateless-단점)
  - [사용 사례](#stateless-사용-사례)
- [비교 분석](#비교-분석)
- [구현 방법](#구현-방법)
  - [Stateful 구현](#stateful-구현)
  - [Stateless 구현](#stateless-구현)
- [최신 트렌드](#최신-트렌드)
- [결론](#결론)

## 개요

백엔드 통신 패턴은 크게 'Stateful(상태 유지)' 및 'Stateless(무상태)' 두 가지 주요 방식으로 분류됩니다. 이 두 패턴은 서버가 클라이언트와의 상호작용에서 상태 정보를 어떻게 처리하는지에 따라 구분됩니다. 이 문서에서는 두 패턴의 특징, 장단점, 사용 사례 및 구현 방법을 자세히 설명합니다.

## 상태란 무엇인가?

'상태(State)'란 클라이언트와 서버 간의 통신에서 이전 요청과 관련된 정보를 말합니다. 이는 세션 정보, 사용자 설정, 통신 컨텍스트 또는 진행 중인 트랜잭션 데이터 등을 포함할 수 있습니다.

## Stateful 패턴

### Stateful 특징

- 서버가 클라이언트의 상태 정보를 저장하고 관리합니다.
- 각 클라이언트마다 서버에 세션이 생성되며, 이 세션은 요청 간에 유지됩니다.
- 클라이언트는 일반적으로 세션 ID를 통해 서버에 저장된 상태에 접근합니다.
- 연속된 요청 간에 컨텍스트가 유지됩니다.

### Stateful 장점

1. **간편한 클라이언트 구현**: 클라이언트는 매 요청마다 모든 상태 정보를 전송할 필요가 없습니다.
2. **낮은 대역폭 요구사항**: 요청 크기가 작아서 네트워크 트래픽이 감소합니다.
3. **트랜잭션 관리 용이**: 복잡한 다단계 프로세스를 쉽게 관리할 수 있습니다.
4. **성능 최적화**: 자주 사용되는 데이터를 메모리에 캐시하여 빠른 접근이 가능합니다.

### Stateful 단점

1. **확장성 제한**: 세션 정보가 특정 서버에 저장되므로 수평적 확장이 어렵습니다.
2. **높은 서버 부하**: 서버가 많은 클라이언트의 상태를 유지해야 하므로 메모리 사용량이 증가합니다.
3. **장애 복구 어려움**: 서버 장애 시 세션 정보가 손실될 수 있습니다.
4. **세션 일관성 문제**: 로드 밸런싱 환경에서 동일 서버로 요청을 라우팅해야 합니다(세션 고정 필요).

### Stateful 사용 사례

- 복잡한 워크플로우가 있는 웹 애플리케이션
- 다단계 마법사 또는 체크아웃 프로세스
- 실시간 게임 서버
- 채팅 애플리케이션
- 기존 레거시 시스템

## Stateless 패턴

### Stateless 특징

- 서버가 클라이언트의 상태 정보를 저장하지 않습니다.
- 각 요청은 자체적으로 완전하며 이전 요청과 독립적입니다.
- 필요한 모든 정보는 요청 자체에 포함되어야 합니다.
- 서버는 요청을 처리한 후 클라이언트에 대한 정보를 "잊어버립니다".

### Stateless 장점

1. **높은 확장성**: 서버 인스턴스를 쉽게 추가하거나 제거할 수 있습니다.
2. **유연한 로드 밸런싱**: 어떤 서버로든 요청을 라우팅할 수 있습니다.
3. **서버 장애 복원력**: 한 서버가 다운되어도 다른 서버로 쉽게 전환할 수 있습니다.
4. **단순한 서버 아키텍처**: 상태 관리 로직이 필요하지 않습니다.
5. **마이크로서비스에 적합**: 독립적인 서비스 간 통신에 이상적입니다.

### Stateless 단점

1. **증가된 요청 크기**: 각 요청에 필요한 모든 정보가 포함되므로 페이로드가 커집니다.
2. **반복적인 데이터 전송**: 동일한 정보가 여러 요청에서 반복될 수 있습니다.
3. **추가 인증 오버헤드**: 각 요청마다 인증이 필요합니다.
4. **클라이언트 복잡성 증가**: 클라이언트가 상태를 관리해야 합니다.

### Stateless 사용 사례

- RESTful API
- 마이크로서비스 아키텍처
- 클라우드 네이티브 애플리케이션
- 대규모 분산 시스템
- 모바일 앱 백엔드
- 서버리스 함수 (AWS Lambda, Azure Functions 등)

## 비교 분석

| 측면 | Stateful | Stateless |
|------|----------|-----------|
| 상태 저장 위치 | 서버 | 클라이언트 또는 외부 저장소 |
| 요청 크기 | 작음 | 큼 |
| 확장성 | 제한적 | 우수함 |
| 서버 메모리 사용 | 높음 | 낮음 |
| 장애 복구 | 어려움 | 쉬움 |
| 로드 밸런싱 | 세션 고정 필요 | 유연함 |
| 구현 복잡성 | 서버 측 복잡성 | 클라이언트 측 복잡성 |
| 대역폭 사용 | 낮음 | 높음 |
| 보안 관리 | 서버 중심 | 토큰 또는 인증 기반 |

## 구현 방법

### Stateful 구현

**1. 세션 관리:**
```java
@Controller
public class LoginController {
    @PostMapping("/login")
    public String login(HttpServletRequest request) {
        // 인증 로직...
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Long userId = (Long) session.getAttribute("userId");
        // 사용자 데이터 로드...
        return "dashboard";
    }
}
```

**2. 세션 복제 설정 (Tomcat):**
```xml
<Cluster className="org.apache.catalina.ha.tcp.SimpleTcpCluster"
         channelSendOptions="8">
    <Manager className="org.apache.catalina.ha.session.DeltaManager"
             expireSessionsOnShutdown="false"
             notifyListenersOnReplication="true"/>
    <!-- 기타 설정... -->
</Cluster>
```

### Stateless 구현

**1. JWT 기반 인증:**
```java
@RestController
public class AuthController {
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 인증 로직...
        String token = tokenProvider.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    
    @GetMapping("/api/data")
    public ResponseEntity<?> getData(@RequestHeader("Authorization") String header) {
        String token = header.substring(7); // "Bearer " 제거
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = tokenProvider.getUserIdFromToken(token);
        // 데이터 로드...
        return ResponseEntity.ok(data);
    }
}
```

**2. 상태 관리를 위한 Redis 사용:**
```java
@Service
public class CartService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void addToCart(String userId, CartItem item) {
        String key = "cart:" + userId;
        redisTemplate.opsForHash().put(key, item.getProductId().toString(), item);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }
    
    public List<CartItem> getCartItems(String userId) {
        String key = "cart:" + userId;
        return redisTemplate.opsForHash().values(key)
                .stream()
                .map(o -> (CartItem) o)
                .collect(Collectors.toList());
    }
}
```

## 최신 트렌드

1. **하이브리드 접근 방식**: 특정 기능에 대해서는 stateful, 다른 기능에 대해서는 stateless 패턴을 사용하는 혼합 아키텍처가 증가하고 있습니다.

2. **토큰 기반 인증**: JWT(JSON Web Tokens)와 같은 토큰 기반 인증은 stateless 통신을 가능하게 하면서도 인증 정보를 안전하게 유지합니다.

3. **외부 상태 저장소**: Redis, Memcached, DynamoDB 등의 분산 캐시 시스템을 사용하여 상태를 관리하는 방식이 인기를 얻고 있습니다.

4. **서버리스 아키텍처**: 클라우드 서비스 공급자가 제공하는 서버리스 함수는 본질적으로 stateless이며, 필요에 따라 확장됩니다.

5. **CQRS 패턴**: Command Query Responsibility Segregation 패턴은 읽기 작업과 쓰기 작업을 분리하여 최적의 상태 관리를 가능하게 합니다.

## 결론

백엔드 통신에서 Stateful과 Stateless 패턴은 각각 고유한 장단점을 가지고 있습니다. 최적의 선택은 애플리케이션의 요구사항, 확장성 목표, 개발 복잡성 및 성능 요구사항에 따라 달라집니다.

현대적인 시스템은 점점 더 Stateless 아키텍처로 이동하는 추세이지만, 특정 사용 사례에서는 Stateful 방식이 여전히 가치가 있습니다. 중요한 것은 각 프로젝트의 특성을 고려하여 적절한 패턴을 선택하거나, 필요에 따라 두 패턴을 효과적으로 결합하는 것입니다.

많은 시스템에서 실용적인 접근법은 기본적으로 Stateless 설계를 채택하되, 분산 캐시나 데이터베이스를 통해 필요한 상태를 관리하는 것입니다. 이는 확장성의 이점을 최대화하면서도 상태 관리의 복잡성을 통제할 수 있게 해줍니다.