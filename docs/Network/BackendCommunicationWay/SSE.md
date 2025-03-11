# 서버 전송 이벤트(Server-Sent Events, SSE) - 백엔드 통신 패턴

## 목차
- [소개](#소개)
- [SSE 작동 방식](#sse-작동-방식)
- [SSE의 장점](#sse의-장점)
- [SSE의 한계](#sse의-한계)
- [다른 통신 패턴과의 비교](#다른-통신-패턴과의-비교)
- [구현 가이드](#구현-가이드)
  - [서버 측 구현](#서버-측-구현)
  - [클라이언트 측 구현](#클라이언트-측-구현)
- [모범 사례](#모범-사례)
- [사용 사례](#사용-사례)
- [문제 해결](#문제-해결)
- [참고 자료](#참고-자료)

## 소개

서버 전송 이벤트(Server-Sent Events, SSE)는 서버가 클라이언트에 업데이트를 푸시할 수 있게 해주는 표준 HTTP 기반 기술입니다. 서버에서 클라이언트로의 단방향 통신 채널을 제공하여, 클라이언트가 새로운 정보를 지속적으로 폴링하지 않고도 실시간 업데이트를 받을 수 있게 합니다.

<Image src = "https://github.com/user-attachments/assets/a3a840ff-006d-4348-9ad4-9ccdf30805d8" width="500"/>

SSE는 클라이언트와 서버 간에 지속적인 연결을 설정하여, 새로운 정보가 있을 때마다 서버가 클라이언트에 데이터를 보낼 수 있도록 합니다. 이 패턴은 서버에서 클라이언트로 실시간 업데이트가 필요하지만, 같은 연결을 통해 클라이언트에서 서버로 데이터를 보낼 필요가 없는 시나리오에 이상적입니다.

## SSE 작동 방식

SSE는 표준 HTTP 연결을 통해 작동하며 다음 원칙을 따릅니다:

1. **연결 설정**: 클라이언트가 표준 HTTP 요청을 사용하여 서버에 연결을 시작합니다.
2. **콘텐츠 타입**: 서버는 `Content-Type: text/event-stream` 헤더로 응답합니다.
3. **영구 연결**: 연결은 열린 상태로 유지되어 서버가 클라이언트에 이벤트를 보낼 수 있습니다.
4. **이벤트 형식**: 데이터는 특정 형식의 텍스트 메시지로 전송됩니다:
   ```
   event: eventName
   id: eventId
   data: eventData

   ```
5. **자동 재연결**: 연결이 끊어지면 클라이언트는 자동으로 재연결을 시도합니다.

## SSE의 장점

- **단순성**: 표준 HTTP 기반으로, WebSocket보다 구현이 쉽습니다.
- **자동 재연결**: 내장된 재연결 처리 기능이 있습니다.
- **호환성**: HTTP/1.1 및 HTTP/2와 함께 작동합니다.
- **방화벽 친화적**: 표준 HTTP를 사용하므로 대부분의 방화벽에서 작동합니다.
- **이벤트 타입**: 이름이 있는 이벤트 지원.
- **메시지 ID**: 마지막으로 받은 이벤트부터 연결을 재개할 수 있는 기능.
- **낮은 오버헤드**: 단방향 통신에 대해 WebSocket보다 가벼움.
- **자연스러운 백프레셔**: HTTP 흐름 제어 메커니즘이 부하 관리를 도움.

## SSE의 한계

- **단방향 통신**: 서버에서 클라이언트로만 가능 (양방향 통신은 WebSocket 고려).
- **연결 제한**: 브라우저는 일반적으로 도메인당 동시 연결을 제한합니다(일반적으로 6개).
- **헤더 크기 제한**: 일부 프록시는 초기 응답 헤더를 버퍼링합니다.
- **IE/Edge 레거시 지원**: 인터넷 익스플로러나 구형 Edge(크로미움 기반 Edge 이전)에서는 네이티브 지원 없음.
- **최대 오픈 연결**: 서버는 잠재적으로 많은 동시 오픈 연결을 처리해야 합니다.

## 다른 통신 패턴과의 비교

| 기능 | SSE | WebSocket | Long Polling | HTTP Polling |
|---------|-----|------------|--------------|--------------|
| 연결 | 지속적 | 지속적 | 응답마다 새 연결 | 요청마다 새 연결 |
| 방향 | 서버 → 클라이언트 | 양방향 | 서버 → 클라이언트 | 서버 → 클라이언트 |
| 실시간 | 예 | 예 | 거의 실시간 | 아니오 (폴링 간격에 따라 다름) |
| 프로토콜 | HTTP | WS/WSS | HTTP | HTTP |
| 헤더 오버헤드 | 연결당 한 번 | 연결당 한 번 | 모든 응답마다 | 모든 요청/응답마다 |
| 구현 복잡성 | 낮음 | 중간 | 중간 | 낮음 |
| 방화벽 친화적 | 예 | 문제가 될 수 있음 | 예 | 예 |
| 자동 재연결 | 예 (내장) | 수동 | 수동 | 해당 없음 (새 요청) |

## 구현 가이드

### 서버 측 구현

#### Node.js (Express) 예제

```javascript
const express = require('express');
const app = express();

app.get('/events', (req, res) => {
  // SSE를 위한 헤더 설정
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');
  
  // 초기 메시지 전송
  res.write('data: 이벤트 스트림에 연결됨\n\n');
  
  // 업데이트를 보내기 위한 인터벌 설정
  const intervalId = setInterval(() => {
    const data = JSON.stringify({ time: new Date().toISOString() });
    res.write(`data: ${data}\n\n`);
  }, 5000);
  
  // 종료 시 정리
  req.on('close', () => {
    clearInterval(intervalId);
  });
});

app.listen(3000, () => {
  console.log('SSE 서버가 3000번 포트에서 실행 중');
});
```

#### Java (Spring) 예제

```java
@RestController
public class SSEController {
    
    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        // 초기 이벤트 전송
        try {
            emitter.send(SseEmitter.event()
                .name("connect")
                .data("이벤트 스트림에 연결됨"));
        } catch (IOException e) {
            emitter.completeWithError(e);
            return emitter;
        }
        
        // 주기적 이벤트 예약
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            () -> {
                try {
                    emitter.send(SseEmitter.event()
                        .name("update")
                        .id(String.valueOf(System.currentTimeMillis()))
                        .data(Map.of("time", new Date().toString())));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            },
            0, 5, TimeUnit.SECONDS
        );
        
        return emitter;
    }
}
```


### 클라이언트 측 구현

```javascript
// EventSource 인스턴스 생성
const eventSource = new EventSource('/events');

// 오픈 이벤트 리스닝
eventSource.onopen = (event) => {
  console.log('연결 열림');
};

// 메시지 리스닝
eventSource.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('데이터 수신:', data);
  // 수신된 데이터로 UI 업데이트
  document.getElementById('updates').innerHTML = data.time;
};

// 특정 이벤트 타입 리스닝
eventSource.addEventListener('update', (event) => {
  const data = JSON.parse(event.data);
  console.log('업데이트 이벤트:', data);
});

// 에러 처리
eventSource.onerror = (error) => {
  console.error('EventSource 에러:', error);
  // 연결이 끊어졌으며, EventSource는 자동으로 재연결을 시도함
};

// 필요할 때 연결 종료
function closeConnection() {
  eventSource.close();
  console.log('연결 종료');
}
```

## 모범 사례

1. **이벤트를 작게 유지**: 효율적인 전달을 위해 최소한의 데이터만 전송.
2. **이벤트 ID 사용**: 연결 끊김 후 재개를 위해 각 이벤트에 ID 포함.
3. **재연결 처리**: 자동이지만, 복잡한 시나리오에 대한 사용자 정의 로직 구현.
4. **타임아웃 구현**: 클라이언트와 서버 모두에 적절한 타임아웃 설정.
5. **연결 상태 모니터링**: 연결 상태를 추적하여 사용자에게 연결 끊김 알림.
6. **오류 처리**: 클라이언트와 서버 모두에 강력한 오류 처리 구현.
7. **로드 밸런싱 고려사항**: 장기 지속 연결을 지원하도록 로드 밸런서 구성.
8. **크로스 도메인 지원**: 필요한 경우 CORS 헤더 사용.
9. **로깅**: 문제 해결을 위한 연결 이벤트 로깅.
10. **점진적 저하**: 지원되지 않는 브라우저를 위한 대체 메커니즘 제공.

## 사용 사례

- **실시간 대시보드**: 통계 및 메트릭 업데이트.
- **뉴스 피드**: 최신 기사나 업데이트 푸시.
- **주식 시세 표시기**: 실시간 가격 업데이트.
- **소셜 미디어 업데이트**: 알림 및 피드 업데이트.
- **실시간 스포츠 스코어**: 실시간 점수 및 이벤트 업데이트.
- **시스템 모니터링**: 서버 상태 및 알림.
- **채팅 애플리케이션**: 단방향 알림.
- **로그 스트리밍**: 실시간 로그 보기.
- **진행 상황 업데이트**: 장기 실행 작업 진행 상황 보고.
- **경매 시스템**: 입찰 업데이트.

## 문제 해결

### 일반적인 문제와 해결책

1. **연결 제한 도달**
   - 문제: 브라우저가 동시 연결을 제한함
   - 해결책: 여러 이벤트 타입으로 단일 SSE 연결 사용

2. **프록시 타임아웃**
   - 문제: 프록시가 유휴 연결을 닫음
   - 해결책: 15-30초마다 하트비트/연결 유지 메시지 보내기

3. **메시지 전달 빈도**
   - 문제: 너무 많은 메시지가 클라이언트를 과부하시킴
   - 해결책: 업데이트를 일괄 처리하거나 조절 구현

4. **연결이 닫히지 않음**
   - 문제: 리소스가 제대로 해제되지 않음
   - 해결책: 클라이언트와 서버 모두에서 적절한 정리 보장

5. **교차 출처 문제**
   - 문제: CORS가 연결을 차단함
   - 해결책: 서버에 적절한 CORS 헤더 구성

6. **IE 호환성**
   - 문제: 네이티브 지원 없음
   - 해결책: 폴리필 또는 대체 메커니즘 사용

