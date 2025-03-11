# 백엔드 커뮤니케이션 패턴: 스트림(Stream)

## 목차
- [개요](#개요)
- [스트림 패턴의 특징](#스트림-패턴의-특징)
- [스트림 vs 다른 통신 패턴](#스트림-vs-다른-통신-패턴)
- [구현 기술](#구현-기술)
- [사용 사례](#사용-사례)
- [설계 고려사항](#설계-고려사항)
- [장단점](#장단점)
- [실제 구현 예제](#실제-구현-예제)
- [모범 사례](#모범-사례)
- [자주 묻는 질문](#자주-묻는-질문)

## 개요

스트림(Stream)은 백엔드 시스템 간 또는 백엔드와 클라이언트 간의 통신에서 연속적인 데이터 전송을 위한 패턴입니다. 일반적인 요청-응답 패턴과 달리, 스트림은 데이터가 계속해서 흐르는 방식으로 작동합니다.

## 스트림 패턴의 특징

- **연속성**: 단일 요청에 대해 여러 응답 또는 연속적인 데이터 전송
- **비동기성**: 클라이언트가 응답을 기다리는 동안 차단되지 않음
- **실시간 처리**: 데이터가 생성되는 즉시 전송 가능
- **양방향 통신**: 많은 스트리밍 프로토콜이 양방향 통신 지원
- **장기 연결**: 일반적으로 장시간 유지되는 연결 사용

## 스트림 vs 다른 통신 패턴

### 요청-응답 (Request-Response)
- **단일 요청, 단일 응답**
- 일반적인 REST API 방식
- 클라이언트가 요청을 보내고 서버로부터 단일 응답을 받음

### 이벤트 기반 (Event-Based)
- **서버에서 클라이언트로 이벤트 푸시**
- 클라이언트가 특정 이벤트에 구독
- 이벤트 발생 시 서버가 클라이언트에 통지

### 스트림 (Stream)
- **연속적인 데이터 흐름**
- 단일 요청으로 여러 응답 수신
- 대용량 또는 지속적인 데이터 처리에 적합

## 구현 기술

### 1. 서버-센트 이벤트 (Server-Sent Events, SSE)
- HTTP 연결을 통한 서버에서 클라이언트로의 단방향 통신
- 텍스트 기반 이벤트 스트림 형식
- 자동 재연결 메커니즘 내장

```javascript
// 클라이언트 측 SSE 구현 예시
const eventSource = new EventSource('/events');
eventSource.onmessage = (event) => {
  console.log('New data:', event.data);
};
```

### 2. 웹소켓 (WebSockets)
- 양방향 실시간 통신
- 지속적인 연결 유지
- 이진 및 텍스트 데이터 전송 지원

```javascript
// 클라이언트 측 웹소켓 구현 예시
const socket = new WebSocket('ws://example.com/socket');
socket.onmessage = (event) => {
  console.log('Received:', event.data);
};
socket.send('Hello from client');
```

### 3. gRPC 스트리밍
- 단방향 스트리밍 (Unary streaming)
- 서버 스트리밍 (Server streaming)
- 클라이언트 스트리밍 (Client streaming)
- 양방향 스트리밍 (Bidirectional streaming)

```protobuf
// gRPC 스트리밍 서비스 정의 예시
service DataService {
  // 서버 스트리밍 RPC
  rpc Subscribe(SubscribeRequest) returns (stream DataItem) {}
  
  // 양방향 스트리밍 RPC
  rpc Chat(stream ChatMessage) returns (stream ChatMessage) {}
}
```

### 4. GraphQL Subscriptions
- 실시간 데이터 변경사항 구독
- 웹소켓 또는 다른 전송 메커니즘 활용

```graphql
# GraphQL 구독 예시
subscription {
  newMessage {
    id
    content
    sender
    timestamp
  }
}
```

### 5. 메시지 큐 및 스트리밍 플랫폼
- Apache Kafka
- RabbitMQ
- AWS Kinesis
- Google Pub/Sub
- Redis Streams

## 사용 사례

### 1. 실시간 대시보드 및 모니터링
- 시스템 메트릭 실시간 업데이트
- 로그 스트리밍
- 사용자 활동 모니터링

### 2. 채팅 및 메시징 애플리케이션
- 실시간 메시지 전송
- 사용자 상태 업데이트
- 타이핑 표시기

### 3. 협업 도구
- 실시간 문서 편집
- 화면 공유
- 공동 작업 환경

### 4. 금융 및 주식 데이터
- 실시간 가격 업데이트
- 거래 알림
- 시장 동향 분석

### 5. IoT 데이터 처리
- 센서 데이터 수집
- 장치 상태 모니터링
- 원격 제어 명령

## 설계 고려사항

### 1. 확장성
- 수많은 동시 연결 처리
- 클러스터링 및 로드 밸런싱
- 자동 스케일링

### 2. 신뢰성
- 연결 끊김 처리
- 재연결 메커니즘
- 메시지 전달 보장

### 3. 성능
- 처리량 최적화
- 지연 시간 최소화
- 데이터 압축

### 4. 보안
- 인증 및 권한 부여
- 데이터 암호화
- 속도 제한 및 남용 방지

## 장단점

### 장점
- **실시간 데이터**: 지연 시간 최소화로 최신 정보 제공
- **효율성**: 폴링 방식보다 네트워크 리소스 사용 효율적
- **반응성**: 사용자 경험 향상
- **양방향 통신**: 클라이언트와 서버 간 자유로운 통신

### 단점
- **복잡성**: 구현 및 관리가 더 복잡
- **리소스 사용**: 장기 연결 유지 비용
- **방화벽 문제**: 일부 네트워크 환경에서 제한될 수 있음
- **오류 처리**: 장기 연결에서의 오류 처리가 더 복잡

## 실제 구현 예제

### Node.js 서버 (SSE 구현)

```javascript
const express = require('express');
const app = express();

app.get('/sse-stream', (req, res) => {
  // SSE 설정
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');
  
  // 클라이언트에 초기 메시지 전송
  res.write(`data: ${JSON.stringify({message: '연결됨'})}\n\n`);
  
  // 주기적으로 데이터 전송
  const intervalId = setInterval(() => {
    const data = {
      time: new Date().toISOString(),
      value: Math.random()
    };
    res.write(`data: ${JSON.stringify(data)}\n\n`);
  }, 1000);
  
  // 클라이언트 연결 종료 시 정리
  req.on('close', () => {
    clearInterval(intervalId);
  });
});

app.listen(3000, () => {
  console.log('SSE 서버가 포트 3000에서 실행 중입니다');
});
```

### Spring Boot WebFlux (리액티브 스트림)

```java
@RestController
public class StreamController {

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(sequence -> ServerSentEvent.<String>builder()
                .id(String.valueOf(sequence))
                .event("periodic-event")
                .data("SSE Event #" + sequence)
                .build());
    }
}
```


## 모범 사례

### 1. 연결 관리
- 하트비트 메커니즘 구현
- 점진적 백오프를 통한 재연결 전략
- 정상적인 연결 종료 처리

### 2. 데이터 처리
- 배치 처리로 메시지 효율성 향상
- 이벤트 유형 및 우선순위 정의
- 메시지 형식 표준화

### 3. 오류 처리
- 실패한 메시지 재시도 메커니즘
- 단계적 오류 처리
- 클라이언트에 오류 알림

### 4. 성능 최적화
- 메시지 압축
- 연결 풀링
- 불필요한 업데이트 필터링

## 자주 묻는 질문

### Q: 스트리밍에 가장 적합한 프로토콜은 무엇인가요?
A: 사용 사례에 따라 다릅니다. 양방향 통신이 필요하면 WebSocket, 서버에서 클라이언트로의 단방향 통신이면 SSE, 복잡한 API가 필요하면 gRPC가 적합할 수 있습니다.

### Q: 스트리밍 구현 시 확장성을 어떻게 보장할 수 있나요?
A: 수평적 확장이 가능한 아키텍처 설계, 효율적인 연결 관리, 메시지 브로커 활용, 로드 밸런싱 구현이 중요합니다.

### Q: 네트워크 문제로 인한 연결 끊김을 어떻게 처리해야 하나요?
A: 자동 재연결 메커니즘, 메시지 버퍼링, 마지막으로 수신된 메시지 ID 추적, 지수 백오프 전략을 구현하세요.

### Q: 스트리밍과 폴링 중 어떤 것을 선택해야 하나요?
A: 실시간 업데이트가 중요하고 서버 부하를 줄이려면 스트리밍, 간단한 구현이 필요하고 업데이트 빈도가 낮으면 폴링이 적합합니다.

### Q: 브라우저와 서버 간 스트리밍 구현 시 고려해야 할 호환성 문제는 무엇인가요?
A: 오래된 브라우저의 WebSocket/SSE 지원 제한, 기업 방화벽 문제, 프록시 서버와의 호환성, 이동 네트워크에서의 장기 연결 문제를 고려해야 합니다.