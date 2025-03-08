# 백엔드 통신 패턴: 동기 vs 비동기

## 목차
- [개요](#개요)
- [동기식 vs 비동기식 통신](#동기식-vs-비동기식-통신)
- [Spring에서의 동기/비동기 처리](#spring에서의-동기비동기-처리)
- [Node.js에서의 동기/비동기 처리](#nodejs에서의-동기비동기-처리)
- [Spring vs Node.js 비교](#spring-vs-nodejs-비교)
- [사용 사례 및 적합한 상황](#사용-사례-및-적합한-상황)
- [성능 비교](#성능-비교)
- [결론](#결론)

## 개요

백엔드 시스템에서 데이터 처리와 통신 방식은 애플리케이션의 성능, 확장성 및 응답성에 직접적인 영향을 미칩니다. 이 문서에서는 두 가지 주요 백엔드 통신 패턴인 **동기식(Synchronous)** 및 **비동기식(Asynchronous)** 접근 방식에 대해 살펴보고, Spring과 Node.js 프레임워크에서 이러한 패턴이 어떻게 구현되는지 비교합니다.

## 동기식 vs 비동기식 통신

### 동기식 통신
- **정의**: 요청 후 응답을 받을 때까지 다음 작업을 진행하지 않고 대기하는 방식 (전자레인지 돌리고 앞에서 기다리기만 하는 경우)
- **특징**:
  - 순차적 실행
  - 블로킹(Blocking) 방식으로 작동
  - 직관적이고 이해하기 쉬운 코드 흐름
  - 스레드 리소스가 응답을 기다리는 동안 차단됨

### 비동기식 통신
- **정의**: 요청 후 응답을 기다리지 않고 다음 작업을 즉시 진행하는 방식 (전자레인지 돌리고 내 할일 하러 가는 경우)
- **특징**:
  - 병렬 실행 가능
  - 논블로킹(Non-blocking) 방식으로 작동
  - 콜백, 프로미스, async/await 등의 구조 사용
  - 리소스를 효율적으로 사용 가능

## Spring에서의 동기/비동기 처리

### 동기식 처리

```java
// 전통적인 동기식 RestTemplate 사용 예제
@Service
public class SynchronousService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public UserDto getUserInfo(Long userId) {
        // 동기식 호출: 응답이 올 때까지 현재 스레드 차단
        return restTemplate.getForObject(
            "https://api.example.com/users/" + userId, 
            UserDto.class
        );
    }
}
```

### 비동기식 처리

```java
// WebClient를 사용한 비동기식 처리 (Spring 5.0 이상)
@Service
public class AsynchronousService {
    
    private final WebClient webClient = WebClient.create("https://api.example.com");
    
    public Mono<UserDto> getUserInfo(Long userId) {
        // 비동기식 호출: 응답을 기다리지 않고 즉시 Mono를 반환
        return webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
    
    // CompletableFuture 사용 예제
    public CompletableFuture<UserDto> getUserInfoAsync(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(
                "https://api.example.com/users/" + userId, 
                UserDto.class
            );
        });
    }
}
```

### 비동기 컨트롤러

```java
@RestController
@RequestMapping("/api")
public class AsyncController {

    @Autowired
    private AsynchronousService service;
    
    // 기본 WebFlux 비동기 엔드포인트
    @GetMapping("/users/{id}")
    public Mono<UserDto> getUser(@PathVariable Long id) {
        return service.getUserInfo(id);
    }
    
    // CompletableFuture 사용
    @GetMapping("/users/{id}/async")
    public CompletableFuture<UserDto> getUserAsync(@PathVariable Long id) {
        return service.getUserInfoAsync(id);
    }
    
    // DeferredResult 사용 (Spring MVC)
    @GetMapping("/users/{id}/deferred")
    public DeferredResult<UserDto> getUserDeferred(@PathVariable Long id) {
        DeferredResult<UserDto> result = new DeferredResult<>();
        service.getUserInfoAsync(id).thenAccept(result::setResult);
        return result;
    }
}
```

## Node.js에서의 동기/비동기 처리

### 기본 구조: 이벤트 루프
Node.js는 기본적으로 비동기 및 이벤트 기반 모델을 사용하며, 단일 스레드 이벤트 루프 기반으로 작동합니다.

### 콜백 패턴

```javascript
// 전통적인 콜백 패턴 예제
const http = require('http');

function getUserInfo(userId, callback) {
    http.get(`https://api.example.com/users/${userId}`, (res) => {
        let data = '';
        
        // 데이터 청크를 받을 때마다 실행
        res.on('data', (chunk) => {
            data += chunk;
        });
        
        // 모든 데이터를 받은 후 실행
        res.on('end', () => {
            callback(null, JSON.parse(data));
        });
    }).on('error', (err) => {
        callback(err, null);
    });
}

// 사용 예제
getUserInfo(123, (err, user) => {
    if (err) {
        console.error(err);
        return;
    }
    console.log(user);
});
```

### Promise 패턴

```javascript
// Promise를 사용한 비동기 처리
const axios = require('axios');

function getUserInfo(userId) {
    return axios.get(`https://api.example.com/users/${userId}`)
        .then(response => response.data)
        .catch(error => {
            throw error;
        });
}

// 사용 예제
getUserInfo(123)
    .then(user => console.log(user))
    .catch(err => console.error(err));
```

### Async/Await 패턴

```javascript
// async/await를 사용한 비동기 처리 (ES2017+)
const axios = require('axios');

async function getUserInfo(userId) {
    try {
        const response = await axios.get(`https://api.example.com/users/${userId}`);
        return response.data;
    } catch (error) {
        throw error;
    }
}

// 사용 예제
async function main() {
    try {
        const user = await getUserInfo(123);
        console.log(user);
    } catch (err) {
        console.error(err);
    }
}

main();
```

### Express.js 비동기 라우트 핸들러

```javascript
const express = require('express');
const axios = require('axios');
const app = express();

// Promise 기반 라우트 핸들러
app.get('/api/users/:id', (req, res) => {
    axios.get(`https://api.example.com/users/${req.params.id}`)
        .then(response => {
            res.json(response.data);
        })
        .catch(error => {
            res.status(500).json({ error: error.message });
        });
});

// async/await 기반 라우트 핸들러
app.get('/api/users/:id/async', async (req, res) => {
    try {
        const response = await axios.get(`https://api.example.com/users/${req.params.id}`);
        res.json(response.data);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

app.listen(3000, () => {
    console.log('Server running on port 3000');
});
```

## Spring vs Node.js 비교

### 실행 모델 비교

| 항목 | Spring | Node.js |
|------|--------|---------|
| 기본 실행 모델 | 멀티 스레드, 스레드 풀 사용 | 단일 스레드, 이벤트 루프 기반 |
| 기본 통신 방식 | 전통적으로 동기식, 최근 비동기식 지원 강화 | 태생적으로 비동기식 (이벤트 드리븐) |
| 블로킹 처리 | 기본적으로 블로킹, WebFlux로 논블로킹 지원 | 기본적으로 논블로킹 |
| CPU 집약적 작업 | 멀티 스레드로 효율적 처리 가능 | 이벤트 루프 블로킹 우려 |
| I/O 집약적 작업 | 전통적 모델에서는 스레드 낭비 가능성 | 논블로킹 I/O로 효율적 처리 |

### 비동기 구현 방식 비교

| 항목 | Spring | Node.js |
|------|--------|---------|
| 비동기 선언 | `@Async`, `Mono/Flux`, `CompletableFuture` | 기본적으로 비동기 (콜백, Promise, async/await) |
| 반환 타입 | `Mono<T>`, `Flux<T>`, `CompletableFuture<T>` | `Promise<T>` |
| 에러 처리 | `onError()`, `exceptionally()` | `catch()`, try/catch + async/await |
| 병렬 처리 | `Flux.parallel()`, `CompletableFuture.allOf()` | `Promise.all()` |
| 반응형 지원 | Project Reactor (WebFlux) | RxJS, Observable 라이브러리 |

### 성능 특성 비교

| 항목 | Spring | Node.js |
|------|--------|---------|
| 초기 로딩 시간 | 일반적으로 더 길다 | 더 짧다 |
| 메모리 사용량 | 일반적으로 더 높다 | 더 낮다 (단, 메모리 누수 주의) |
| 동시 처리 | 멀티 스레드로 CPU 코어 활용 | 단일 스레드 제한, 클러스터로 확장 |
| 장시간 실행 연산 | 다른 요청 처리에 영향 적음 | 이벤트 루프 블로킹 가능성 |
| 확장성 | 수직적 확장에 유리 | 수평적 확장에 유리 |

## 사용 사례 및 적합한 상황

### Spring이 적합한 경우
- CPU 집약적인 작업이 많은 애플리케이션
- 복잡한 비즈니스 로직과 엔터프라이즈 통합이 필요한 경우
- 대규모 트랜잭션 처리가 필요한 경우
- 레거시 시스템과의 통합이 필요한 경우
- 강력한 타입 안전성이 요구되는 경우

### Node.js가 적합한 경우
- I/O 집약적인 작업이 많은 애플리케이션
- 실시간 통신이 필요한 경우 (WebSocket, Socket.io)
- 가벼운 마이크로서비스 구현
- JSON API 서버로서의 역할
- 프론트엔드와 백엔드 개발자가 동일한 언어(JavaScript)를 사용하고 싶은 경우

## 성능 비교

### 동시성 처리 벤치마크 비교

```
[가상 벤치마크 결과 - 초당 처리량]
- I/O 집약적 작업:
  - Node.js: ~15,000 요청/초
  - Spring WebFlux: ~12,000 요청/초
  - Spring MVC: ~5,000 요청/초

- CPU 집약적 작업:
  - Node.js: ~2,000 요청/초
  - Spring WebFlux: ~5,000 요청/초
  - Spring MVC: ~7,000 요청/초
```

### 메모리 사용량 비교

```
[가상 메모리 사용량 - 10,000 동시 연결 처리]
- Node.js: ~300MB
- Spring WebFlux: ~500MB
- Spring MVC: ~1.2GB
```

## 결론

### 동기식 vs 비동기식 선택 기준
- **동기식**이 적합한 경우:
  - 코드의 가독성과 유지보수성이 중요한 경우
  - 순차적 실행이 필요한 단순한 작업
  - 응답 시간이 매우 짧은 작업
  - 요청 볼륨이 적은 시스템

- **비동기식**이 적합한 경우:
  - 고성능과 확장성이 중요한 경우
  - I/O 작업이 많은 애플리케이션
  - 동시 연결 수가 많은 시스템
  - 실시간 처리가 필요한 서비스

동기식과 비동기식 접근 방식은 각각 장단점을 가지고 있으며, 현대 백엔드 시스템에서는 두 방식을 혼합하여 사용하는 것이 일반적입니다. Spring과 Node.js 모두 두 가지 패턴을 지원하지만, 각 프레임워크의 기본 설계 철학과 강점을 이해하고 프로젝트 요구사항에 맞게 선택하는 것이 중요합니다.