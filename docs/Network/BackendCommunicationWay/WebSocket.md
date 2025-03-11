# WebSocket 완벽 가이드

## 목차
- [소개](#소개)
- [작동 원리](#작동-원리)
- [HTTP와의 차이점](#http와의-차이점)
- [주요 특징](#주요-특징)
- [WebSocket API](#websocket-api)
- [사용 사례](#사용-사례)
- [보안 고려사항](#보안-고려사항)
- [프레임워크 및 라이브러리](#프레임워크-및-라이브러리)
- [자주 발생하는 문제 및 해결 방법](#자주-발생하는-문제-및-해결-방법)
- [참고 자료](#참고-자료)

## 소개

WebSocket은 클라이언트와 서버 간의 양방향, 실시간 통신을 제공하는 웹 기술입니다. 2011년에 HTML5의 일부로 표준화되었으며, 기존의 HTTP 통신 방식의 한계를 극복하기 위해 개발되었습니다.

기존 HTTP 프로토콜은 클라이언트가 요청을 보내고 서버가 응답하는 단방향 통신 모델을 사용합니다. 이로 인해 실시간 애플리케이션을 구현하는 데 제한이 있었습니다. WebSocket은 이러한 문제를 해결하기 위해 설계되었으며, 한 번 연결이 수립되면 클라이언트와 서버 모두 언제든지 메시지를 보낼 수 있습니다.


<img src= "https://blog.kakaocdn.net/dn/cVTDc7/btqMApTOK2x/a26hJAfvAYi6mLxgz8bKX0/img.jpg" width="500" >

## 작동 원리

WebSocket 통신은 다음과 같은 단계로 이루어집니다:

1. **핸드셰이크**: 클라이언트가 HTTP 프로토콜을 사용하여 서버에 연결을 요청합니다. 이 요청에는 WebSocket 프로토콜로 업그레이드하겠다는 의사가 포함됩니다.

```
GET /chat HTTP/1.1
Host: server.example.com
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==
Sec-WebSocket-Version: 13
```


2. **연결 수립**: 서버가 이 요청을 수락하면, HTTP 상태 코드 101(Switching Protocols)로 응답합니다.

```
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
```

3. **양방향 통신**: 핸드셰이크가 성공적으로 완료되면, HTTP 연결은 WebSocket 연결로 업그레이드되고, 클라이언트와 서버 간에 양방향 통신 채널이 수립됩니다.

4. **데이터 전송**: 클라이언트와 서버는 WebSocket 프로토콜을 사용하여 프레임(frames)이라는 형태로 데이터를 주고받습니다.

5. **연결 종료**: 통신이 완료되면, 클라이언트나 서버 중 하나가 연결을 종료할 수 있습니다.

## HTTP와의 차이점

| 특성 | HTTP | WebSocket |
|------|------|-----------|
| 통신 방식 | 단방향(클라이언트 요청 → 서버 응답) | 양방향(동시에 데이터 교환 가능) |
| 연결 상태 | 비연결성(요청마다 새로운 연결) | 지속적 연결 |
| 오버헤드 | 매 요청마다 헤더 정보 포함 | 초기 핸드셰이크 후에는 최소화됨 |
| 실시간성 | 제한적(폴링 방식) | 뛰어남(실시간 데이터 전송) |
| 프로토콜 | HTTP/HTTPS (TCP 기반) | ws/wss (TCP 기반) |
| 포트 | 일반적으로 80/443 | 일반적으로 80/443 |

## 주요 특징

- **양방향 통신**: 클라이언트와 서버가 독립적으로 메시지를 주고받을 수 있습니다.
- **낮은 지연시간**: 한 번 연결이 수립되면, 추가적인 핸드셰이크 없이 데이터를 전송할 수 있습니다.
- **실시간 통신**: 지연시간이 짧아 실시간 애플리케이션에 적합합니다.
- **효율적인 리소스 사용**: 연결이 수립된 후에는 헤더 정보가 최소화되어 대역폭을 절약합니다.
- **프로토콜 지원**: `ws://`(비암호화)와 `wss://`(SSL/TLS 암호화) 프로토콜을 지원합니다.
- **크로스 오리진 통신**: 웹브라우저의 CORS(Cross-Origin Resource Sharing) 정책을 따릅니다.

## WebSocket 응답과 요청

<img src= "https://media.vlpt.us/images/rainbowweb/post/5a28097a-db1a-409d-afe2-a7c31356042f/image.png" width="500" >

### webSocket 요청
HTTP 버전은 1.1이상
반드시 GET메서드를 사용해야 한다
Upgrade정보는 서버, 전송, 프로토콜 연결에서 다른 프로토콜로 업그레이드 또는 변경하기 위한 규칙이다
Sec-Websocket-Key는 클라이언트가 요청하는 여러 서브 프로토콜을 의미한다

### webSocket 응답
101 Switching Protocols가 Response로 오면 웹소켓이 연결 된 것이다.

Sec-Websocket-Accept는 요청에서의 Key값을 계산한 값으로 신원 인증에 필요한 헤더이다


### WebSocket 요청 응답 특징 
- 최초 접속시에만 http프로토콜 위에서 handshaking을 하기 때문에 http header를 사용한다
- 핸드 쉐이크가 완료되면 프로토콜이 ws로 변경된다. 또는 wss와 같이 데이터 보안을 위해 SSL을 적용한 프로토콜로 변경된다
- 웹소켓을 위한 별도의 포트는 없고, 기존 포트를 사용한다
- 프레임으로 구성된 메시지라는 논리적 단위로 송수신 한다
- 메시지에 포함될 수 있는 교환 가능한 메시지는 텍스트와 바이너리 뿐이다

## WebSocket API

자바스크립트에서 WebSocket을 사용하는 기본 문법은 다음과 같습니다:

```javascript
// WebSocket 객체 생성
const socket = new WebSocket('ws://example.com/socket');

// 연결이 수립되었을 때 이벤트
socket.onopen = (event) => {
    console.log('WebSocket 연결이 수립되었습니다.');
    socket.send('안녕하세요! 서버!');
};

// 메시지를 수신했을 때 이벤트
socket.onmessage = (event) => {
    console.log('서버로부터 메시지를 받았습니다:', event.data);
};

// 오류가 발생했을 때 이벤트
socket.onerror = (error) => {
    console.error('WebSocket 오류가 발생했습니다:', error);
};

// 연결이 종료되었을 때 이벤트
socket.onclose = (event) => {
    console.log('WebSocket 연결이 종료되었습니다. 코드:', event.code, '이유:', event.reason);
};

// 메시지 전송
socket.send('안녕하세요!');

// 연결 종료
// socket.close();
```
### WebSocket 이벤트

| 이벤트 | 설명 |
|--------|------|
| `open` | WebSocket 연결이 수립되었을 때 발생 |
| `message` | 서버로부터 메시지를 수신했을 때 발생 |
| `error` | 에러가 발생했을 때 발생 |
| `close` | 연결이 종료되었을 때 발생 |

### WebSocket 메서드

| 메서드 | 설명 |
|--------|------|
| `send()` | 서버에 데이터를 전송 |
| `close()` | WebSocket 연결을 종료 |

### WebSocket 속성

| 속성 | 설명 |
|------|------|
| `readyState` | 현재 연결 상태 (0: CONNECTING, 1: OPEN, 2: CLOSING, 3: CLOSED) |
| `bufferedAmount` | 아직 서버로 전송되지 않은 데이터의 바이트 수 |
| `extensions` | 서버가 선택한 확장 기능 |
| `protocol` | 서버가 선택한 하위 프로토콜 |

## 사용 사례

WebSocket은 다음과 같은 애플리케이션에 특히 유용합니다:

1. **실시간 채팅 애플리케이션**
   - 메시지가 지연 없이 즉시 전달되어야 하는 채팅 서비스

2. **게임 플랫폼**
   - 실시간 멀티플레이어 게임에서 플레이어 간 상호작용

3. **주식 거래 및 금융 애플리케이션**
   - 실시간 주가 업데이트 및 거래 정보

4. **소셜 미디어 피드**
   - 새로운 게시물, 댓글, 좋아요 등의 실시간 업데이트

5. **협업 도구**
   - 공동 문서 편집, 화이트보드 공유 등 실시간 협업 기능

6. **IoT(사물인터넷) 애플리케이션**
   - 센서 데이터 실시간 모니터링 및 제어

7. **실시간 모니터링 대시보드**
   - 시스템 성능, 트래픽, 로그 등의 실시간 모니터링

## 보안 고려사항

WebSocket을 사용할 때 고려해야 할 주요 보안 사항:

1. **암호화**: 민감한 데이터를 전송할 때는 `wss://` 프로토콜을 사용하여 SSL/TLS 암호화를 적용합니다.

2. **인증 및 권한 부여**: WebSocket 연결을 수립하기 전에 사용자 인증을 수행하고, 연결 후에도 메시지별로 권한을 검증합니다.

3. **입력 검증**: 클라이언트로부터 받은 모든 데이터는 서버 측에서 철저히 검증해야 합니다.

4. **연결 제한**: DoS(Denial of Service) 공격을 방지하기 위해 클라이언트당 연결 수와 메시지 빈도를 제한합니다.

5. **Origin 검증**: `Sec-WebSocket-Origin` 헤더를 확인하여 허가된 출처에서만 연결을 수락합니다.

6. **하트비트 메커니즘**: 연결 상태를 모니터링하고 좀비 연결을 방지하기 위한 주기적인 핑/퐁 메시지를 구현합니다.

## 프레임워크 및 라이브러리

### 클라이언트 측 라이브러리

| 이름 | 언어/플랫폼 | 설명 |
|------|-------------|------|
| **Native Browser API** | JavaScript | 모든 현대 브라우저에서 지원하는 기본 WebSocket API |
| **Socket.IO Client** | JavaScript | 다양한 폴백 메커니즘을 제공하는 라이브러리 |
| **SockJS Client** | JavaScript | 다양한 브라우저 환경에서 일관된 API를 제공 |
| **SignalR Client** | .NET/JavaScript | .NET 기반 SignalR 서비스와 통신하기 위한 클라이언트 |

### 서버 측 라이브러리

| 이름 | 언어/플랫폼 | 설명 |
|------|-------------|------|
| **ws** | Node.js | 빠르고 가벼운 WebSocket 서버 및 클라이언트 구현 |
| **Socket.IO** | Node.js | 실시간 애플리케이션을 위한 완전한 솔루션 |
| **Spring WebSocket** | Java | Spring 프레임워크의 WebSocket 지원 |

## 자주 발생하는 문제 및 해결 방법

1. **연결 종료**
   - 문제: 일정 시간 후 WebSocket 연결이 자동으로 종료됨
   - 해결: 핑/퐁 메시지를 주기적으로 교환하여 연결 유지

2. **프록시 및 방화벽 문제**
   - 문제: 일부 프록시와 방화벽은 오랫동안 열려 있는 연결을 차단
   - 해결: wss:// 프로토콜 사용 및 표준 포트(443) 활용

3. **확장성 문제**
   - 문제: 많은 수의 동시 연결 처리 시 서버 리소스 부족
   - 해결: 클러스터링, 로드 밸런싱, 웹소켓 프록시 서버 활용

4. **재연결 전략**
   - 문제: 네트워크 불안정으로 인한 연결 끊김
   - 해결: 지수 백오프 알고리즘을 사용한 자동 재연결 구현

5. **메시지 순서 및 신뢰성**
   - 문제: 메시지 순서 보장과 전송 확인 필요
   - 해결: 메시지 시퀀스 번호 및 ACK 메커니즘 구현

## 참고 자료
- [Web Socket 이란?](https://velog.io/@codingbotpark/Web-Socket-%EC%9D%B4%EB%9E%80)
