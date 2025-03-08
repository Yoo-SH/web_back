# Backend Communication Patterns: Multiplexing vs Demultiplexing

## 목차
- [개요](#개요)
- [멀티플렉싱(Multiplexing)](#멀티플렉싱multiplexing)
  - [작동 원리](#멀티플렉싱-작동-원리)
  - [사용 사례](#멀티플렉싱-사용-사례)
  - [장점](#멀티플렉싱-장점)
  - [단점](#멀티플렉싱-단점)
  - [구현 예시](#멀티플렉싱-구현-예시)
- [디멀티플렉싱(Demultiplexing)](#디멀티플렉싱demultiplexing)
  - [작동 원리](#디멀티플렉싱-작동-원리)
  - [사용 사례](#디멀티플렉싱-사용-사례)
  - [장점](#디멀티플렉싱-장점)
  - [단점](#디멀티플렉싱-단점)
  - [구현 예시](#디멀티플렉싱-구현-예시)
- [두 패턴 비교](#두-패턴-비교)
- [실제 응용 사례](#실제-응용-사례)
- [구현 시 고려사항](#구현-시-고려사항)
- [결론](#결론)

## 개요

백엔드 시스템에서 멀티플렉싱(Multiplexing)과 디멀티플렉싱(Demultiplexing)은 다중 데이터 스트림이나 채널을 효율적으로 관리하기 위한 중요한 통신 패턴입니다. 이 두 패턴은 네트워크 프로그래밍, 분산 시스템, 마이크로서비스 아키텍처 등 다양한 환경에서 활용됩니다.

## 멀티플렉싱(Multiplexing)

멀티플렉싱은 여러 개의 데이터 스트림이나 신호를 하나의 채널이나 연결을 통해 전송하는 기술입니다.

<Image src = "https://github.com/user-attachments/assets/95510f9b-bf57-4c9c-a679-d978e6c83100" width="500"/>

### 멀티플렉싱 작동 원리

1. **입력 수집**: 여러 소스에서 데이터를 수집합니다.
2. **채널 할당**: 각 데이터 스트림에 고유 식별자(채널 ID)를 할당합니다.
3. **데이터 결합**: 식별자와 함께 데이터를 단일 스트림으로 결합합니다.
4. **전송**: 결합된 데이터를 단일 채널을 통해 전송합니다.
5. **수신 후 분리**: 수신측에서는 식별자를 기반으로 데이터를 다시 분리합니다.

### 멀티플렉싱 사용 사례

- **HTTP/2 프로토콜**: 단일 TCP 연결을 통해 여러 요청/응답을 동시에 처리
- **WebSocket**: 단일 연결에서 다중 메시지 스트림 처리
- **이벤트 버스**: 여러 이벤트 생산자의 메시지를 단일 채널로 통합
- **데이터베이스 연결 풀링**: 여러 쿼리를 적은 수의 연결을 통해 전송

### 멀티플렉싱 장점

- **리소스 효율성**: 여러 연결 대신 하나의 연결 사용으로 시스템 리소스 절약
- **네트워크 오버헤드 감소**: 연결 설정/해제 비용 감소
- **대역폭 최적화**: 하나의 채널을 통한 데이터 전송으로 대역폭 효율적 사용
- **동시성 향상**: 단일 연결에서 병렬 처리 가능

### 멀티플렉싱 단점

- **복잡성 증가**: 구현 및 디버깅이 더 복잡해짐
- **헤드 오브 라인 블로킹(HOL)**: 하나의 스트림 처리 지연이 다른 스트림에 영향 가능
- **오류 전파**: 하나의 채널이 실패하면 모든 데이터 스트림에 영향
- **오버헤드**: 식별자와 메타데이터 추가로 약간의 오버헤드 발생

### 멀티플렉싱 구현 예시

```java
// Java를 사용한 간단한 멀티플렉서 예시
public class SimpleMultiplexer {
    private final Map<String, Queue<Message>> channels = new ConcurrentHashMap<>();
    private final BlockingQueue<MultiplexedMessage> outputQueue = new LinkedBlockingQueue<>();
    
    // 새 채널 등록
    public void registerChannel(String channelId) {
        channels.putIfAbsent(channelId, new ConcurrentLinkedQueue<>());
    }
    
    // 채널에 메시지 추가
    public void sendOnChannel(String channelId, Message message) {
        if (!channels.containsKey(channelId)) {
            throw new IllegalArgumentException("Channel not registered: " + channelId);
        }
        
        outputQueue.add(new MultiplexedMessage(channelId, message));
    }
    
    // 멀티플렉싱된 메시지 처리 (송신측)
    public void processOutgoingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    MultiplexedMessage message = outputQueue.take();
                    // 네트워크를 통해 메시지 전송
                    sendToRemote(message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
    
    private void sendToRemote(MultiplexedMessage message) {
        // 실제 전송 로직 구현
    }
    
    // 내부 클래스들
    private static class Message {
        private final byte[] payload;
        
        public Message(byte[] payload) {
            this.payload = payload;
        }
        
        public byte[] getPayload() {
            return payload;
        }
    }
    
    private static class MultiplexedMessage {
        private final String channelId;
        private final Message message;
        
        public MultiplexedMessage(String channelId, Message message) {
            this.channelId = channelId;
            this.message = message;
        }
        
        public String getChannelId() {
            return channelId;
        }
        
        public Message getMessage() {
            return message;
        }
    }
}
```

## 디멀티플렉싱(Demultiplexing)

디멀티플렉싱은 멀티플렉싱의 반대 과정으로, 단일 채널로 수신된 데이터 스트림을 원래의 여러 스트림으로 분리하는 기술입니다.

<Image src = "https://github.com/user-attachments/assets/bbe705f1-7361-45cb-800a-d44dd3bb77c1" width="500"/>

### 디멀티플렉싱 작동 원리

1. **데이터 수신**: 단일 채널을 통해 결합된 데이터를 수신합니다.
2. **식별자 확인**: 각 데이터 패킷의 식별자(채널 ID)를 확인합니다.
3. **분류**: 식별자에 따라 데이터를 분류합니다.
4. **라우팅**: 분류된 데이터를 해당 대상 스트림이나 핸들러로 라우팅합니다.

### 디멀티플렉싱 사용 사례

- **서버 소켓 프로그래밍**: 다중 클라이언트 연결 처리
- **패킷 필터링**: 네트워크 패킷을 프로토콜 유형별로 분류
- **메시지 큐**: 토픽별 메시지 분배
- **이벤트 처리 시스템**: 이벤트 유형별 핸들러로 분배

### 디멀티플렉싱 장점

- **격리**: 각 스트림이 독립적으로 처리되어 오류 격리 제공
- **병렬 처리**: 분리된 스트림을 병렬로 처리 가능
- **확장성**: 새로운 타입이나 채널 쉽게 추가 가능
- **전문화**: 각 스트림 유형에 맞는 최적화된 처리 가능

### 디멀티플렉싱 단점

- **상태 관리 복잡성**: 여러 스트림의 상태 동기화가 필요
- **자원 소비**: 여러 핸들러를 유지하는 데 추가 리소스 필요
- **오버헤드**: 분류 및 라우팅 로직에 따른 처리 오버헤드
- **순서 유지 어려움**: 다중 스트림 처리 시 원래 순서 유지 어려움

### 디멀티플렉싱 구현 예시

```java
// Java를 사용한 간단한 디멀티플렉서 예시
public class SimpleDemultiplexer {
    private final Map<String, MessageHandler> handlers = new ConcurrentHashMap<>();
    
    // 채널 핸들러 등록
    public void registerHandler(String channelId, MessageHandler handler) {
        handlers.put(channelId, handler);
    }
    
    // 수신된 멀티플렉싱 메시지 처리 (수신측)
    public void handleIncomingMessage(MultiplexedMessage message) {
        String channelId = message.getChannelId();
        MessageHandler handler = handlers.get(channelId);
        
        if (handler != null) {
            // 해당 채널 핸들러로 메시지 전달
            handler.handleMessage(message.getMessage());
        } else {
            // 알 수 없는 채널 처리
            System.err.println("No handler registered for channel: " + channelId);
        }
    }
    
    // 네트워크에서 메시지 수신 및 처리
    public void startReceiving() {
        new Thread(() -> {
            while (true) {
                // 네트워크에서 멀티플렉싱된 메시지 수신
                MultiplexedMessage message = receiveFromRemote();
                
                if (message != null) {
                    handleIncomingMessage(message);
                }
            }
        }).start();
    }
    
    private MultiplexedMessage receiveFromRemote() {
        // 실제 수신 로직 구현
        return null; // 예시용 더미 리턴
    }
    
    // 인터페이스 및 내부 클래스들
    public interface MessageHandler {
        void handleMessage(Message message);
    }
    
    private static class Message {
        private final byte[] payload;
        
        public Message(byte[] payload) {
            this.payload = payload;
        }
        
        public byte[] getPayload() {
            return payload;
        }
    }
    
    private static class MultiplexedMessage {
        private final String channelId;
        private final Message message;
        
        public MultiplexedMessage(String channelId, Message message) {
            this.channelId = channelId;
            this.message = message;
        }
        
        public String getChannelId() {
            return channelId;
        }
        
        public Message getMessage() {
            return message;
        }
    }
}
```

## 두 패턴 비교

| 특성 | 멀티플렉싱 | 디멀티플렉싱 |
|------|------------|--------------|
| 목적 | 여러 스트림을 하나로 결합 | 하나의 스트림을 여러 개로 분리 |
| 방향 | N → 1 | 1 → N |
| 복잡성 | 수집 및 결합에 초점 | 분류 및 라우팅에 초점 |
| 주요 이점 | 리소스 효율성 | 병렬 처리 및 격리 |
| 주요 도전 | HOL 블로킹, 오류 전파 | 상태 관리, 순서 유지 |
| 일반적 사용처 | 네트워크 통신, 연결 풀링 | 이벤트 처리, 메시지 분배 |

## 실제 응용 사례

### 1. gRPC Multiplexing

gRPC는 단일 TCP 연결을 통해 여러 RPC 호출을 멀티플렉싱합니다. HTTP/2를 기반으로 하여 스트림 ID를 사용해 여러 요청과 응답을 구분합니다.

### 2. Netty의 ChannelPipeline

Netty는 Java 기반 네트워크 프레임워크로, ChannelPipeline을 사용하여 들어오는 이벤트를 처리합니다. 이는 디멀티플렉싱 패턴의 좋은 예시입니다.

### 3. Redis Pub/Sub

Redis의 Pub/Sub 시스템은 멀티플렉싱과 디멀티플렉싱을 모두 사용합니다. 게시자는 채널에 메시지를 멀티플렉싱하고, Redis 서버는 이를 구독자에게 디멀티플렉싱합니다.

### 4. Kafka 토픽과 파티션

Kafka는 토픽과 파티션 시스템을 통해 효율적인 멀티플렉싱과 디멀티플렉싱을 구현합니다. 프로듀서는 메시지를 토픽에 멀티플렉싱하고, 컨슈머는 특정 토픽에서 메시지를 디멀티플렉싱합니다.

### 5. Reactive Streams

Reactive 프로그래밍에서 Flux나 Observable은 데이터 스트림의 멀티플렉싱과 디멀티플렉싱을 가능하게 합니다.

## 구현 시 고려사항

### 1. 효율적인 식별자 관리

- 간결하고 고유한 식별자 체계 설계
- 해시 충돌 방지 및 효율적인 룩업 지원

### 2. 버퍼링 전략

- 버퍼 크기와 타임아웃 정책 설정
- 메모리 사용량과 지연 시간 사이의 균형

### 3. 오류 처리

- 단일 스트림 오류의 격리
- 적절한 재시도 및 폴백 메커니즘

### 4. 흐름 제어

- 빠른 발신자와 느린 수신자 간의 속도 차이 관리
- 백프레셔(backpressure) 메커니즘 구현

### 5. 모니터링 및 디버깅

- 각 채널 및 스트림의 상태 추적
- 지연 시간 및 처리량 측정

## 결론

멀티플렉싱과 디멀티플렉싱은 현대 백엔드 시스템에서 효율적인 통신을 위한 핵심 패턴입니다. 이 패턴들을 적절히 활용하면 리소스 효율성, 확장성, 그리고 시스템 성능을 크게 향상시킬 수 있습니다. 다만 복잡성, 오류 처리, 순서 보장과 같은 도전 과제를 신중하게 고려해야 합니다.

최신 프레임워크와 라이브러리는 대부분 이러한 패턴을 내부적으로 구현하고 있으므로, 개발자는 이를 의식적으로 활용하여 효율적인 백엔드 시스템을 구축할 수 있습니다.

