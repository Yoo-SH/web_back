# BackendCommunicationPattern: 게시-구독(Pub-Sub) 패턴

## 목차
- [개요](#개요)
- [핵심 개념](#핵심-개념)
- [작동 방식](#작동-방식)
- [구현 방법](#구현-방법)
- [사용 사례](#사용-사례)
- [장점](#장점)
- [단점](#단점)
- [주요 기술 및 도구](#주요-기술-및-도구)
- [모범 사례](#모범-사례)
- [결론](#결론)

## 개요

게시-구독(Publish-Subscribe) 패턴은 메시지 발신자(게시자)가 특정 수신자를 지정하지 않고 메시지를 발행하고, 수신자(구독자)는 관심 있는 메시지만 수신하는 비동기 통신 패턴입니다. 이 패턴은 게시자와 구독자 간의 느슨한 결합(loose coupling)을 제공하여 확장성과 유연성이 뛰어난 시스템 아키텍처를 구현할 수 있게 해줍니다.

```
+----------+      +----------------+      +------------+
|          |      |                |      |            |
| Publisher|----->| Message Broker |----->| Subscriber |
|          |      |                |      |            |
+----------+      +----------------+      +------------+
                          |
                          |                +------------+
                          |                |            |
                          +--------------->| Subscriber |
                          |                |            |
                                           +------------+
                                                 .
                                                 .
                                                 .
                          |                +------------+
                          |                |            |
                          +--------------->| Subscriber |
                                           |            |
                                           +------------+
```


## 핵심 개념

1. **게시자(Publisher)**: 메시지를 생성하고 발행하는 주체
2. **구독자(Subscriber)**: 특정 유형의 메시지를 수신하기로 등록한 주체
3. **토픽(Topic)/채널(Channel)**: 메시지가 분류되는 카테고리 또는 주제
4. **메시지 브로커(Message Broker)**: 게시자와 구독자 사이에서 메시지 라우팅을 관리하는 중개자
5. **이벤트(Event)**: 시스템 내에서 발생하는 상태 변화나 활동을 나타내는 데이터 패킷

## 작동 방식

1. 구독자는 특정 토픽이나 채널에 대한 구독을 등록합니다.
2. 게시자는 토픽에 메시지를 발행합니다.
3. 메시지 브로커는 해당 토픽을 구독한 모든 구독자에게 메시지를 전달합니다.
4. 각 구독자는 수신한 메시지를 자신의 방식으로 처리합니다.

<Image src = "https://github.com/user-attachments/assets/80054f5d-afe4-428d-977f-30aeab7127e1" width="500"/>

## 구현 방법

```javascript
// 간단한 Pub-Sub 패턴 JavaScript 구현
class PubSub {
  constructor() {
    this.subscribers = {};
  }

  // 구독 메소드
  subscribe(topic, callback) {
    if (!this.subscribers[topic]) {
      this.subscribers[topic] = [];
    }
    const index = this.subscribers[topic].push(callback) - 1;
    
    // 구독 취소 함수 반환
    return {
      unsubscribe: () => {
        this.subscribers[topic].splice(index, 1);
        // 구독자가 없으면 토픽 정리
        if (this.subscribers[topic].length === 0) {
          delete this.subscribers[topic];
        }
      }
    };
  }

  // 발행 메소드
  publish(topic, data) {
    if (!this.subscribers[topic]) {
      return;
    }
    
    // 모든 구독자에게 메시지 전달
    this.subscribers[topic].forEach(callback => {
      // setTimeout을 사용한 비동기 실행으로 블로킹 방지
      setTimeout(() => {
        callback(data);
      }, 0);
    });
  }
  
  // 특정 토픽의 구독자 수 확인
  getSubscribersCount(topic) {
    if (!this.subscribers[topic]) {
      return 0;
    }
    return this.subscribers[topic].length;
  }
  
  // 모든 토픽 목록 반환
  getTopics() {
    return Object.keys(this.subscribers);
  }
}

// 사용 예시
const pubsub = new PubSub();

// 'userLoggedIn' 토픽 구독
const subscription1 = pubsub.subscribe('userLoggedIn', (data) => {
  console.log('User logged in notification 1:', data);
});

const subscription2 = pubsub.subscribe('userLoggedIn', (data) => {
  console.log('User logged in notification 2:', data);
});

// 'dataUpdated' 토픽 구독
const subscription3 = pubsub.subscribe('dataUpdated', (data) => {
  console.log('Data updated:', data);
});

// 메시지 발행
pubsub.publish('userLoggedIn', { userId: 'user123', time: new Date() });
pubsub.publish('dataUpdated', { entity: 'product', id: 'p456', changes: { price: 99.99 } });

// 구독 취소
subscription1.unsubscribe();

// 구독 취소 후 발행
pubsub.publish('userLoggedIn', { userId: 'user456', time: new Date() });

// 토픽 정보 확인
console.log('구독자 수:', pubsub.getSubscribersCount('userLoggedIn'));
console.log('현재 토픽 목록:', pubsub.getTopics());

```

## 사용 사례

1. **실시간 알림 시스템**
   - 소셜 미디어 알림
   - 모바일 푸시 알림
   - 실시간 업데이트

2. **마이크로서비스 아키텍처**
   - 서비스 간 비동기 통신
   - 이벤트 기반 상태 업데이트

3. **IoT(사물인터넷) 애플리케이션**
   - 센서 데이터 수집 및 처리
   - 디바이스 상태 모니터링

4. **금융 시스템**
   - 주식 시세 업데이트
   - 트랜잭션 처리

5. **채팅 애플리케이션**
   - 메시지 전달
   - 온라인 상태 관리

## 장점

1. **느슨한 결합(Loose Coupling)**
   - 게시자와 구독자는 서로를 직접 알 필요가 없음
   - 시스템 컴포넌트 간의 의존성 감소

2. **확장성(Scalability)**
   - 새로운 구독자를 쉽게 추가할 수 있음
   - 게시자와 구독자를 독립적으로 확장 가능

3. **유연성(Flexibility)**
   - 시스템의 한 부분을 변경해도 다른 부분에 영향을 최소화
   - 다양한 통신 패턴 지원 (1:N, N:M)

4. **비동기 처리(Asynchronous Processing)**
   - 게시자는 구독자의 처리를 기다릴 필요 없음
   - 시스템 응답성 향상

5. **이벤트 기반 아키텍처 지원**
   - 이벤트 중심 설계 촉진
   - 반응형 시스템 구현 용이

## 단점

1. **메시지 전달 보장의 복잡성**
   - 메시지 손실 가능성
   - 중복 전달 문제 해결 필요

2. **디버깅 어려움**
   - 느슨한 결합으로 인한 실행 흐름 추적의 어려움
   - 문제 발생 시 원인 파악이 복잡

3. **시스템 복잡성 증가**
   - 메시지 브로커 추가로 인한 인프라 복잡성
   - 장애 지점 증가

4. **지연시간(Latency)**
   - 브로커를 통한 통신으로 약간의 지연 발생
   - 실시간성이 매우 중요한 시스템에서 고려 필요

5. **메시지 순서 보장의 어려움**
   - 기본적으로 순서 보장이 어려움
   - 추가적인 메커니즘 필요

## 주요 기술 및 도구

### 메시지 브로커

1. **Kafka**
   - 고성능, 내구성이 뛰어난 분산 이벤트 스트리밍 플랫폼
   - 대용량 데이터 처리와 실시간 스트리밍에 적합

2. **RabbitMQ**
   - AMQP 기반의 메시지 브로커
   - 다양한 메시징 패턴 지원

3. **Google Cloud Pub/Sub**
   - 완전 관리형 실시간 메시징 서비스
   - 글로벌 규모의 메시지 처리 지원

4. **AWS SNS/SQS**
   - SNS(Simple Notification Service)와 SQS(Simple Queue Service)를 함께 사용
   - 클라우드 기반 메시징

5. **Redis Pub/Sub**
   - 인메모리 데이터 구조 저장소를 활용한 메시지 브로커
   - 가벼운 메시징 요구사항에 적합

### 프로토콜

1. **MQTT(Message Queuing Telemetry Transport)**
   - 경량 메시징 프로토콜
   - IoT 기기 간 통신에 널리 사용됨

2. **AMQP(Advanced Message Queuing Protocol)**
   - 기업용 메시징을 위한 개방형 표준 프로토콜
   - 신뢰성 높은 메시지 전달

3. **WebSockets**
   - 실시간 양방향 통신 지원
   - 웹 브라우저와 서버 간 통신에 사용

## 모범 사례

1. **메시지 내구성 확보**
   - 중요 메시지의 경우 영구 저장소에 저장
   - 브로커 장애 시 복구 메커니즘 구현

2. **적절한 토픽 설계**
   - 너무 세분화되거나 너무 광범위한 토픽 피하기
   - 명확한 토픽 네이밍 규칙 적용

3. **에러 처리 및 재시도 메커니즘**
   - 실패한 메시지 처리를 위한 데드 레터 큐(DLQ) 사용
   - 지수 백오프와 같은 재시도 전략 구현

4. **흐름 제어**
   - 백프레셔(backpressure) 메커니즘 도입
   - 과부하 시 처리 속도 조절

5. **모니터링 및 로깅**
   - 메시지 흐름 추적을 위한 상관 ID 사용
   - 메시지 처리 상태 및 성능 모니터링

6. **보안**
   - 메시지 암호화
   - 인증 및 권한 부여 메커니즘 구현

## 결론

게시-구독 패턴은 현대 분산 시스템과 마이크로서비스 아키텍처에서 필수적인 통신 패턴입니다. 이 패턴은 시스템 컴포넌트 간의 느슨한 결합을 제공하고, 확장성과 유연성을 높이며, 비동기 처리를 가능하게 합니다. 하지만 메시지 전달 보장, 디버깅 어려움, 시스템 복잡성 증가 등의 도전 과제가 있습니다.

적절한 도구와 프로토콜을 선택하고 모범 사례를 따르면, 게시-구독 패턴은 다양한 도메인에서 강력하고 효율적인 통신 메커니즘을 제공할 수 있습니다. 특히 실시간 데이터 업데이트, 이벤트 기반 아키텍처, 대규모 분산 시스템에서 그 가치를 발휘합니다.

시스템 요구사항과 성능 목표에 맞는 구현 방법을 선택하고, 적절한 모니터링과 오류 처리 메커니즘을 갖추는 것이 성공적인 게시-구독 시스템 구축의 핵심입니다.