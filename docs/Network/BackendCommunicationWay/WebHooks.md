# 웹훅(Webhook)에 대한 모든 것

## 목차
1. [웹훅이란?](#웹훅이란)
2. [웹훅의 작동 방식](#웹훅의-작동-방식)
3. [웹훅 vs API](#웹훅-vs-api)
4. [웹훅의 장점](#웹훅의-장점)
5. [웹훅의 단점](#웹훅의-단점)
6. [웹훅 구현 방법](#웹훅-구현-방법)
7. [웹훅 보안](#웹훅-보안)
8. [웹훅 사용 사례](#웹훅-사용-사례)
9. [대표적인 웹훅 제공 서비스](#대표적인-웹훅-제공-서비스)
10. [웹훅 테스트 도구](#웹훅-테스트-도구)
11. [자주 묻는 질문](#자주-묻는-질문)

## 웹훅이란?

웹훅(Webhook)은 실시간 데이터 통신을 위한 HTTP 기반의 콜백 메커니즘입니다. 특정 이벤트가 발생했을 때, 해당 이벤트에 대한 정보를 HTTP POST 요청을 통해 지정된 URL로 전송하는 방식입니다. 

즉,  데이터가 변경되었을 때 실시간으로 알림을 받을 수 있는 기능입니다. 웹 서비스의 이벤트 데이터를 전달하는 HTTP 기반 콜백 함수입니다. 특정 이벤트가 발생하면 웹훅이 클라이언트에게 이벤트 데이터를 보내요. 웹훅이라는 단어는 2007년에 Jeff Lindsay에 의해 처음 사용되었어요. HTTP 기반의 웹 특징과 훅(Hook) 기능을 합친 용어죠.


웹훅은 "역방향 API", "HTTP 콜백", "사용자 정의 콜백" 등으로도 불립니다.

<img src= "https://static.tosspayments.com/docs/glossary/webhook-polling-difference.png" width="500">

API 폴링(Polling)을 사용하면 클라이언트가 서버 API를 호출해서 이벤트가 발생했는지 확인해야 됩니다. 친구와 전화하는 비유를 들자면 API 폴링은 친구가 받을 때까지 계속 전화하는 것과 같고, 웹훅은 친구에게 "시간 나면 전화 줘"라고 문자를 남기는 것과 같습니다.

이벤트가 발생했다면 폴링으로 필요한 응답을 바로 받겠지만 이벤트가 발생하기 전까지는 클라이언트가 주기적으로 API를 호출해야 됩니다. 반면 웹훅은 한 번 설정하면 클라이언트가 추가 요청을 보내지 않아도 됩니다

또 API 폴링은 주기를 60초에서 120초로 설정하는 것을 가장 추천하는데요. 그렇다면 실시간으로 이벤트 데이터를 받기 어렵습니다. 웹훅을 사용하면 이벤트가 발생한 즉시 데이터를 받을 수 있습니다.

## 웹훅의 작동 방식

웹훅은 다음과 같은 주요 단계로 작동합니다:

1. **등록**: 클라이언트가 이벤트를 받고자 하는 URL을 서비스에 등록합니다.
2. **이벤트 발생**: 서비스 내에서 관련 이벤트가 발생합니다.
3. **알림**: 서비스는 등록된 URL로 HTTP POST 요청을 보냅니다.
4. **처리**: 클라이언트는 수신된 데이터를 처리합니다.
5. **응답**: 클라이언트는 HTTP 상태 코드로 처리 결과를 응답합니다.


## 웹훅 vs API

| 특성 | 웹훅 | API |
|------|------|-----|
| 통신 방향 | 서버 → 클라이언트 (푸시) | 클라이언트 → 서버 (풀) |
| 데이터 전달 시점 | 이벤트 발생 시 즉시 | 요청 시 |
| 실시간성 | 높음 | 낮음 (폴링 필요) |
| 구현 복잡성 | 중간 | 낮음 |
| 리소스 효율성 | 높음 | 낮음 (주기적 요청 필요) |

## 웹훅의 장점

1. **실시간 데이터**: 이벤트 발생 즉시 데이터를 수신할 수 있습니다.
2. **효율적인 리소스 사용**: 폴링 방식보다 서버 및 네트워크 리소스를 효율적으로 사용합니다.
3. **느슨한 결합**: 시스템 간의 의존성을 줄이고 마이크로서비스 아키텍처에 적합합니다.
4. **확장성**: 다수의 시스템에 동일한 이벤트 알림을 쉽게 전달할 수 있습니다.
5. **자동화**: 이벤트 기반의 워크플로우 자동화에 이상적입니다.

## 웹훅의 단점

1. **신뢰성 문제**: 네트워크 문제로 인해 전송 실패 가능성이 있습니다.
2. **보안 위험**: 인증 미흡 시 악의적인 HTTP 요청에 취약할 수 있습니다.( 웹훅을 사용하기 위해서는 보안, 데이터 유효성 검증을 필수적으로 수행해야 합니다.)
3. **배달 보장 없음**: 기본적으로 메시지 전달을 보장하지 않습니다.
4. **디버깅 어려움**: 비동기적 특성으로 인해 문제 추적이 어려울 수 있습니다.
5. **방화벽 이슈**: 일부 환경에서는 인바운드 HTTP 요청이 차단될 수 있습니다.

## 웹훅 구현 방법

### 서버 측 (웹훅 제공자)

```javascript
// Node.js 예제 (Express 사용)
const express = require('express');
const app = express();

// 웹훅 등록 저장소
const webhooks = [];

// 웹훅 등록 엔드포인트
app.post('/register-webhook', (req, res) => {
  const { url, event } = req.body;
  webhooks.push({ url, event });
  res.status(201).send({ message: '웹훅이 등록되었습니다.' });
});

// 이벤트 발생 시 웹훅 트리거
function triggerWebhook(event, payload) {
  const relevantHooks = webhooks.filter(hook => hook.event === event);
  
  relevantHooks.forEach(async (hook) => {
    try {
      await fetch(hook.url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
    } catch (error) {
      console.error(`웹훅 전송 실패: ${hook.url}`, error);
    }
  });
}

app.listen(3000, () => {
  console.log('웹훅 서버가 포트 3000에서 실행 중입니다.');
});
```

### 클라이언트 측 (웹훅 수신자)

```javascript
// Node.js 예제 (Express 사용)
const express = require('express');
const app = express();
app.use(express.json());

// 웹훅 수신 엔드포인트
app.post('/webhook', (req, res) => {
  const payload = req.body;
  
  // 페이로드 처리 로직
  console.log('웹훅 수신:', payload);
  
  // 성공적으로 처리되었음을 응답
  res.status(200).send({ message: '웹훅이 처리되었습니다.' });
});

app.listen(4000, () => {
  console.log('웹훅 수신 서버가 포트 4000에서 실행 중입니다.');
});
```

## 웹훅 보안

웹훅 보안을 위한 주요 방법:

1. **서명 검증**: HMAC 서명을 사용하여 페이로드 무결성 검증
2. **IP 제한**: 특정 IP 주소 또는 범위만 허용
3. **HTTPS 사용**: 전송 중 데이터 암호화
4. **시크릿 토큰**: 요청 헤더에 공유된 시크릿 포함
5. **타임스탬프 검증**: 재생 공격 방지를 위한 타임스탬프 확인
6. **속도 제한**: 과도한 요청에 대한 제한 설정

### 서명 검증 예제

```javascript
// 웹훅 서명 생성 (제공자 측)
const crypto = require('crypto');
const secret = 'your_webhook_secret';

function createSignature(payload) {
  return crypto
    .createHmac('sha256', secret)
    .update(JSON.stringify(payload))
    .digest('hex');
}

// 서명 확인 (수신자 측)
function verifySignature(payload, signature) {
  const expectedSignature = crypto
    .createHmac('sha256', secret)
    .update(JSON.stringify(payload))
    .digest('hex');
    
  return crypto.timingSafeEqual(
    Buffer.from(expectedSignature),
    Buffer.from(signature)
  );
}
```

## 웹훅 사용 사례

1. **결제 처리**: 결제 게이트웨이에서 결제 상태 변경 시 알림
2. **CI/CD 파이프라인**: 코드 변경 시 빌드 및 배포 자동화
3. **IoT 이벤트**: 장치 상태 변경 시 알림
4. **소셜 미디어 통합**: 새 게시물, 댓글 등의 알림
5. **이메일 서비스**: 이메일 전송, 개봉, 클릭 이벤트 추적
6. **채팅 애플리케이션**: 새 메시지 알림
7. **CRM 시스템**: 고객 활동 업데이트
8. **배송 추적**: 배송 상태 변경 시 알림
9. **데이터베이스 변경**: 데이터 변경 시 캐시 무효화
10. **감시 및 알림**: 시스템 지표가 임계값을 초과할 때 알림

## 대표적인 웹훅 제공 서비스

1. **GitHub**: 저장소 이벤트 (푸시, PR, 이슈 등)
2. **Stripe**: 결제 및 구독 이벤트
3. **PayPal**: 결제 상태 변경
4. **Slack**: 메시지 및 채널 이벤트
5. **Twilio**: SMS 상태 변경
6. **SendGrid**: 이메일 이벤트 (전송, 개봉, 클릭)
7. **Shopify**: 상점 이벤트 (주문, 제품 등)
8. **IFTTT**: 다양한 서비스 간 이벤트 연결
9. **Zapier**: 애플리케이션 간 워크플로우 자동화
10. **Discord**: 서버 및 채널 이벤트

## 웹훅 테스트 도구

1. **[Webhook.site](https://webhook.site)**: 웹훅 테스트를 위한 임시 URL 생성
2. **[RequestBin](https://requestbin.com)**: 웹훅 요청 캡처 및 검사
3. **[Postman](https://www.postman.com)**: API 및 웹훅 테스트
4. **[ngrok](https://ngrok.com)**: 로컬 서버를 인터넷에 노출
5. **[Beeceptor](https://beeceptor.com)**: 웹훅 모의 응답 생성

## 자주 묻는 질문

### 웹훅과 Websocket의 차이점은 무엇인가요?
웹훅은 단방향 HTTP 요청으로, 이벤트가 발생할 때 한 번만 전송됩니다. Websocket은 양방향 지속적 연결로, 실시간 양방향 통신이 필요할 때 사용합니다.

### 웹훅이 실패하면 어떻게 해야 하나요?
재시도 메커니즘을 구현하는 것이 좋습니다. 지수 백오프(exponential backoff) 알고리즘을 사용하여 일정 횟수 재시도하고, 실패 시 로깅하거나 대체 알림을 설정합니다.

### 웹훅의 성능 영향은 어떤가요?
웹훅은 폴링 방식보다 리소스를 효율적으로 사용하지만, 고부하 상황에서는 대기열 시스템을 사용하여 처리를 분산시키는 것이 좋습니다.

### 여러 이벤트에 대해 하나의 웹훅을 사용할 수 있나요?
예, 하나의 웹훅 URL로 여러 이벤트를 수신할 수 있습니다. 요청 본문에 이벤트 유형을 포함하여 구분하면 됩니다.

### 웹훅과 서버리스 아키텍처를 함께 사용할 수 있나요?
네, 웹훅은 서버리스 함수(AWS Lambda, Azure Functions 등)와 잘 작동합니다. 이벤트 기반 아키텍처에 적합합니다.

