# Message Queue 가이드

## 개요
메시지 큐(Message Queue)는 프로세스나 서비스 간에 비동기적으로 메시지를 전달하는 통신 방식입니다. 메시지 큐 시스템은 분산 시스템에서 컴포넌트 간 느슨한 결합(loose coupling)을 가능하게 하여 확장성, 안정성, 탄력성을 높이는 핵심 기술입니다.

<img src="https://velog.velcdn.com/images/choidongkuen/post/b208dfbc-edd7-40ee-90b4-7fbd9f0f7166/image.png" width="500">

즉, 메세지 큐(Message Queue) 란 Queue 라는 자료구조를 채택해서 메세지를 전달하는 시스템이며, 메세지 지향 미들웨어(MOM) 을 구현한 시스템입니다.


## 메세지 지향 미들웨어(MOM)

메세지 지향 미들웨어란 응용 소프트웨어 간의 비동기적 데이터 통신을 위한 소프트웨어입니다.즉, 비동기적(Asynchronous) 한 방식을 이용해서 프로세스간의 데이터를 주고 받는 기능을 위한 시스템입니다.

 메세지 지향 미들웨어는 메세지를 전달하는 과정에서 보관하거나 라우팅 및 변환할 수 있다는 장점을 가집니다.

- 메세지의 백업 기능을 유지함으로 써 지속성을 제공하며 이 덕분에 송수신 측은 동시에 네트워크 연결을 유지할 필요가 없습니다 (보관)
-  미들웨어 계층 자신이 직접 메세지 라우팅을 수행하기 때문에, 하나의 메세지를 여러 수신자에게 배포가 가능합니다 (라우팅)
-  송수신 측의 요구에 따라 전달하는 메세지를 변환할 수 있습니다.(변환)


## 작동 원리
1. **생산자(Producer)**: 메시지를 생성하여 큐에 전송합니다
2. **큐(Queue)**: 메시지를 저장하고 순서대로 관리합니다
3. **소비자(Consumer)**: 큐에서 메시지를 가져와 처리합니다

## 메세지 브로커 VS 이벤트 브로커 

메세지 큐에서 데이터를 운반하는 방식에 따라 메세지 브로커 와 이벤트 브로커 로 나눌 수 있습니다.

- 메세지 브로커 (ex) RabbitMQ, ActiveMQ, AWS SQS, Redis)

        메세지 브로커는 Producer 가 생산한 메세지를 메세지 큐에 저장하고, 저장된 메세지를 Consumer가 가져갈 수 있도록 합니다.
        메세지 브로커는 Consumer 가 메세지 큐에서 데이터를 가져가게 되면 짧은 시간 내에 메세지 큐에서 삭제되는 특징이 있습니다❗️




- 이벤트 브로커 (ex) Kafka)

        이벤트 브로커 또한 기본적으로 메세지 브로커의 역할을 할 수 있습니다.
        하지만 반대로 메세지 브로커는 이벤트 브로커의 기능을 하지 못합니다 🚫
        이벤트 브로커가 관리하는 데이터를 이벤트라고 하며 Consumer 가 메세지 큐에서 데이터를 가져가게 되면 짧은 시간 내에 메세지가 삭제되는 것과 달리, 이벤트 브로커 방식에서는 Consumer 가 소비한 데이터를 필요한 경우 다시 소비할 수 있습니다 ❗️

        또한 메세지 브로커 보다 대용향 데이터를 처리할 수 있는 능력이 있습니다 🔥


## 주요 기능 및 특징
- **비동기(Asynchronous)**: Queue에 넣어두기 때문에 나중에 처리할 수 있다.
- **낮은 결합도(Decoupling)**: 애플리케이션과 분리할 수 있다.
- **탄력성(Resilience)**: 일부가 실패 시 전체에 영향을 받지 않는다.
- **과잉(Redundancy)**: 실패 할 경우 재실행이 가능하다.
- **신뢰성(Guarantees)**: 작업이 처리된 걸 확인할 수 있다.
- **확장성(Scalable)**: 다수의 프로세스들이 큐에 메시지를 보낼 수 있다.

### 비동기(Asynchronous)
메세지큐가 없다고 가정해보겠습니다.
메세지를 발행하는 Producer 역할을 하는 애플리케이션은 자신의 메세지를 전달 받는 Consumer 역할을 하는 애플리케이션에게 직접적으로 메세지를 보내야합니다.
즉 End-To-End 통신을 통해 메세지 전달이 이루어집니다. 이 때문에 해당 과정이 완료되기 전까지는 다른 메세지 전달 과정을 이루어지지 못합니다.

이러한 방식을 동기(Synchronous) 라고 하며 전송속도가 빠르고 전송 결과를 신속하게 알 수 있는 장점이 있는 반면에, 대용량 트래픽이 발생하는 서버에서 이러한 방식은 매우 비효율적입니다🚫

<img src = "https://velog.velcdn.com/images/choidongkuen/post/c1e3b8c1-4bcd-4771-b0f9-6cd71dde03ad/image.png" width = "500">

이를 해결하기 위해 메세지큐를 중간에 배치한다면 Producer 는 메세지를 Consumer 에게 바로 보내지 않고 Queue 에 메세지를 넣어 관리합니다. 이를 통해 Consumer 는 비동기적으로 메세지를 처리할 수 있습니다 ❗️

### 낮은 결합도(Decoupling)
메세지큐를 통해 하나의 서비스를 구성하는 애플리케이션끼리의 결합도를 낮출 수 있습니다.만약, 애플리케이션끼리의 결합도가 높으면 어떨까요?
물론 결합도가 높을 때 얻을 수 있는 장점도 있지만 확장성,유연성,효율적인 유지 보수,장애 전파 방지 등에 결합도가 낮을 때 얻을 수 있는 장점이 많습니다.낮은 결합성은 특히 현재 가장 핫한 MSA 아키텍쳐의 핵심 특징입니다.

<img src = "https://velog.velcdn.com/images/choidongkuen/post/fa59413a-3854-4b5a-88cb-9bc130ca08ee/image.png" width = "500">

### 탄력성(Resilience)

여기서 탄력성(Resilience) 란 시스템이 예기치 않은 상황 또는 장애에 대응하고 유연하게 대처할 수 있는 능력을 의미합니다.
이는 앞서 살펴보았던 낮은 결합도(Decoupling) 을 통해 실현될 수 있습니다.
이해를 위해 한가지 예시를 들어보겠습니다.

은행 송금 시스템에서 A,B 라는 2가지의 프로세스가 있다고 가정해보겠습니다.
A 프로세스는 회원이 요청한 송금을 처리하는 프로세스이며, B 프로세스는 회원이 보낸 송금을 받아 계좌에 반영하는 프로세스입니다

|Process|TasK|
|---|---|
|A|송금 처리|
|B|송금 반영|

A 프로세스는 회원이 요청한 송금을 처리하는 프로세스이며, B 프로세스는 회원이 보낸 송금을 받아 계좌에 반영하는 프로세스입니다

여기서 메세지 큐가 없다고 가정해보겠습니다.
이때, 어떠한 이유로 B 프로세스에 장애가 발생했을 때 해당 장애는 A 프로세스에게도 전파되어 장애가 복구되는 시간동안 A,B 두 프로세스 모두 정상적으로 동작하지 못한채 Blocking 됩니다.
이러한 결과는 당연한 결과인데 B 프로세스가 더이상 A 프로세스에서 보내는 송금을 받아 처리할 수 없으니 A 프로세스도 덩달아 송금을 처리하는 작업을 하지 못하게 되는 것입니다 🥲

하지만 메세지 큐가 있다면 이러한 문제를 효율적으로 해결할 수 있습니다.
위와 같이 동일한 상황일 때 A 프로세스는 B 프로세스의 장애 여부와 상관 없이 자신이 보내는 송금(메세지) 을 메세지 큐에게 전달만 해주면 됩니다.
송금(메세지) 을 받은 메세지 큐는 B 프로세스의 장애가 해결될 때까지 큐 내부에 A 에서 받아오는 송금(메세지) 를 보관할 수 있습니다 ❗️
이로써, B 서비스의 일시적인 불능 상태가 A 서비스에 직접적인 영향을 주지 않고, 시스템의 기능을 유지할 수 있습니다 👍

정리하자면 메세지 큐의 이러한 특성 덕분에 Producer 프로세스는 Consumer 프로세스가 다운되어 있어도 메세지를 정상적으로 발행할 수 있고, Consumer 는 구독한 메세지를 발행하는 Producer 프로세스가 다운되어 있어도 정상적으로 수신할 수 있습니다❗️

### 과잉(Redundancy)

A 프로세스와 B 프로세스는 End-to-End 통신을 하는 데 만약 B 프로세스에서 장애가 발생해 A 프로세스에서 장애 기간동안 정상적으로 메세지를 송신할 수 없다면 A 프로세스를 사용하는 클라이언트는 장애 기간동안 큰 어려움을 겪을 수 있습니다.
또한 여기에는 애플리케이션 수준에서 큰 문제가 될 수 있는데 시스템 응답성 저하,데이터 불일치 ,메세지 유실 등 다양한 문제가 발생할 수 있습니다 😱

하지만 메세지 큐를 사용한다면 이러한 문제를 완화할 수 있습니다.
작업을 메세지로 메세지 큐에 넣어두면 일정 장애 기간동안 송신된 메세지는 큐에 남아있어 추후 장애 복구 시 정상적으로 재시도 및 복구가 가능합니다 👍


### 신뢰성(Guarantees)
여기서 신뢰성(Guarantees) 이란 송신된 메세지의 안전하고 확실한 전달을 의미합니다.
앞에 살펴보았던 장점들과 비슷한 맥락인데, 결국 메세지 큐라는 시스템 덕분에 장애가 발생하더라도 송신되는 메세지를 안전하고 확실하게 Consumer 가 수신할 수 있습니다 👍


### 확장성(Scalable)
여기서 확장성(Scalable) 은 수평확장을 의미합니다.
만약 기존 메시지 큐를 이용한 통신에서 부하가 증가하거나 클라이언트의 동시다발적인 요청이 증가할 때, 메세지 큐에 Producer 와 Consumer 을 추가함으로 비교적 간단하고 쉽게 확장을 할 수 있습니다.


## 일반적인 패턴
- **Point-to-Point**: 하나의 메시지는 하나의 소비자만 처리
- **Publish-Subscribe**: 하나의 메시지를 여러 소비자가 수신
- **Request-Reply**: 요청과 응답 패턴 구현
- **Dead Letter Queue**: 처리할 수 없는 메시지를 별도 저장

## 주요 메시지 큐 시스템
- **RabbitMQ**: AMQP 프로토콜 기반의 강력한 범용 메시지 브로커
- **Apache Kafka**: 고성능 분산 이벤트 스트리밍 플랫폼
- **Amazon SQS**: AWS의 완전 관리형 메시지 큐 서비스
- **Redis**: 인메모리 데이터 구조 저장소로 간단한 메시지 큐 기능 제공
- **ActiveMQ**: Apache의 오픈소스 메시지 브로커
- **IBM MQ**: 엔터프라이즈급 메시지 큐 솔루션

## 구현 예시 (Java with RabbitMQ)

### 의존성 설정 (Maven)
```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.16.0</version>
</dependency>
```

### 생산자 코드
```java
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Producer {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            
            // 큐 선언 (durable = true로 설정하여 RabbitMQ 재시작 시에도 큐가 유지됨)
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            
            String message = "Hello World!";
            
            // 메시지를 지속성 있게 발행(persistent message)
            channel.basicPublish("", QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
```

### 소비자 코드
```java
import com.rabbitmq.client.*;

public class Consumer {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 큐 선언 (생산자와 동일한 설정)
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        
        // 한 번에 하나의 메시지만 가져오도록 설정
        channel.basicQos(1);

        // 메시지 소비 콜백 정의
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            
            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println(" [x] Done");
                // 메시지 처리 완료 확인 (수동 ACK)
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        
        // 자동 ACK를 false로 설정하여 수동으로 처리 완료를 확인
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
    
    private static void doWork(String task) {
        try {
            // 메시지당 . 개수만큼 처리 시간이 소요된다고 가정
            for (char ch : task.toCharArray()) {
                if (ch == '.') {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### 스프링 부트 예시 (Spring AMQP)

#### 의존성 설정 (Maven)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

#### RabbitMQ 설정
```java
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue taskQueue() {
        return new Queue("task_queue", true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
```

#### 생산자 서비스
```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;

    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("task_queue", message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
```

#### 소비자 서비스
```java
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiver {

    @RabbitListener(queues = "task_queue")
    public void receiveMessage(String message) {
        System.out.println(" [x] Received '" + message + "'");
        try {
            doWork(message);
        } finally {
            System.out.println(" [x] Done");
        }
    }
    
    private void doWork(String message) {
        try {
            for (char ch : message.toCharArray()) {
                if (ch == '.') {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

## 고려사항
- **메시지 형식**: JSON, XML, 바이너리 등
- **전송 보장**: At-least-once, At-most-once, Exactly-once
- **순서 보장**: 순서가 중요한 경우 적절한 큐 선택
- **성능과 확장성**: 처리량, 지연 시간, 확장 방법
- **모니터링 및 관리**: 큐 상태, 처리율, 오류 모니터링

## 응용 사례
- **마이크로서비스 통신**: 서비스 간 데이터 교환
- **이메일 전송**: 비동기 이메일 처리
- **결제 처리**: 트랜잭션 처리 및 기록
- **로그 처리**: 대량의 로그 데이터 처리
- **작업 분배**: 백그라운드 작업 처리
- **이벤트 기반 아키텍처**: 이벤트 전달 및 처리

## 장점
- 시스템 컴포넌트 간 결합도 감소
- 부하 관리 및 피크 처리 용이
- 시스템 안정성 및 복원력 향상
- 비동기 처리로 응답 시간 개선

## 단점
- 시스템 복잡성 증가
- 디버깅 및 추적이 어려울 수 있음
- 추가적인 인프라 관리 필요
- 일관성 보장이 어려울 수 있음


## 참고 자료
- [[서버] 메세지 큐(Message Queue) 을 알아보자](https://velog.io/@choidongkuen/%EC%84%9C%EB%B2%84-%EB%A9%94%EC%84%B8%EC%A7%80-%ED%81%90Message-Queue-%EC%9D%84-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90)
- [[Apache Kafka] 카프카란 무엇인가?](https://velog.io/@holicme7/Apache-Kafka-%EC%B9%B4%ED%94%84%EC%B9%B4%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80#2-rabbitmq-%EC%9D%98-%EB%8F%99%EC%9E%91-%EB%B0%A9%EC%8B%9D--%EB%B0%8F-%ED%8A%B9%EC%A7%95)
