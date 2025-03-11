# gRPC 가이드

## 목차
- [gRPC란 무엇인가?](#grpc란-무엇인가)
- [gRPC의 핵심 특징](#grpc의-핵심-특징)
- [gRPC의 작동 방식](#grpc의-작동-방식)
- [Protocol Buffers](#protocol-buffers)
- [gRPC의 서비스 유형](#grpc의-서비스-유형)
- [gRPC vs REST](#grpc-vs-rest)
- [gRPC 사용 사례](#grpc-사용-사례)
- [지원하는 언어](#지원하는-언어)
- [시작하기](#시작하기)
- [gRPC 사용 시 고려사항](#grpc-사용-시-고려사항)
- [추가 리소스](#추가-리소스)

## gRPC란 무엇인가?
gRPC는 구글에서 개발한 고성능, 오픈소스 범용 RPC(Remote Procedure Call) 프레임워크입니다. 'g'는 원래 Google을 의미했지만, 지금은 "gRPC Remote Procedure Calls"의 재귀적 약자로 사용됩니다.gRPC는 별도의 원격 제어를 위한 코딩 없이 다른 주소 공간에서 함수나 프로시저를 실행할 수 있게하는 프로세스 간 통신 기술입니다. 쉽게 설명해서 MSA(Micro Service Architecture)구조롤 서비스를 만들게 되면, 다양한 언어와 프레임워크로 개발되는 경우가 있는데, 이러한 구조에 RPC를 이용하여 언어에 구애받지 않고, 원격에 있는 프로시저를 호출하여 고유 프로그램의 개발에 집중할 수 있게 해주는 기술입니다.


gRPC를 이용하면 원격에 있는 애플리케이션의 메서드를 로컬 메서드인 것처럼 직접 호출할 수 있기 때문에 분산 애플리케이션과 서비스를 보다 쉽게 만들 수 있다.

gRPC는 RPC 시스템에서와 마찬가지로 서비스를 정의하고, 서비스를 위한 매개변수와 반환 값을 가지는 메서드를 만든다는 아이디어를 가지고 있다.

<img src="https://velog.velcdn.com/images%2Fdojun527%2Fpost%2Fe9c54426-d17c-448e-9d9b-ba3b4e86bb98%2F%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202021-02-14%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%2011.09.45.png" width="500" >

## Protocol Buffers
Protocol Buffers(protobuf)는 gRPC의 기본 IDL(Interface Definition Language)이자 직렬화 메커니즘입니다.

어떤 언어나 플랫폼에서도 통신 프로토콜이나 데이터 저장을 사용할 때, 구조화된 데이터를 전환하게 해주는 방법입니다. (json이나 xml 등) XML의 문제점을 개선하기 위해 제안된 IDL이며, XML보다 월등한 성능을 지닙니다.

Protocol buffers는 구조화된 데이터를 직렬화(serialization)하기 위한 프로토콜로 XML보다 작고, 빠르고, 간단한니다.. XML 스키마처럼 .proto 파일에 protocol buffer 메세지 타입을 정의합니다

Protocol buffers는 구조화된 데이터를 직렬화하는데 있어서 XML보다 많은 장점들을 가지고 있습니다
- 간단하다
- 파일 크기가 3~10배 정도 작다
- 속도가 20~100배 정도 빠르다
- XML보다 가독성이 좋고 명시적이다.

프로토콜 버퍼로 작업할 때는 proto file에서 직렬화하려는 데이터 구조를 정의합니다. 프로토콜 버퍼는 하나의 프로그래밍 언어가 아니라 여러 프로그래밍 언어를 지원하기 때문에, 특정 언어에 종속성이 없는 형태로 데이터 타입을 정의하게 되는데, 이 파일을 proto file이라고 합니다.

프로토콜 버퍼 데이터는 일련의 '이름-값'의 쌍을 포함하는 작은 논리적 레코드인 메시지로 구성됩니다.

```protobuf
  message Person {
      string name = 1;
      int32 id = 2;
      bool has_ponycopter = 3;
    }
```

이렇게 작성된 proto file을 protoc 컴파일러로 컴파일 하면 데이터에 접근할 수 있는 각 언어에 맞는 형태의 데이터 클래스를 생성해줍니다. 만들어진 클래스는 각 필드를 위한 접근자 뿐 아니라 전체 구조를 바이트로 직렬화하거나 바이트로부터 전체 구조를 파싱하는 메서드들을 제공합니다.

## gRPC의 핵심 특징
- **높은 성능**: 이진 직렬화를 통한 빠른 데이터 교환
- **강력한 타입 계약**: Protocol Buffers를 사용한 API 계약
- **언어 중립적**: 다양한 프로그래밍 언어 지원
- **코드 생성**: 클라이언트/서버 코드 자동 생성
- **양방향 스트리밍**: 복잡한 통신 패턴 지원
- **인증 지원**: TLS/SSL 및 토큰 기반 인증
- **오류 처리**: 표준화된 상태 코드와 오류 메시지
- **HTTP/2 기반**: 멀티플렉싱, 헤더 압축, 서버 푸시 등 지원

## gRPC의 작동 방식
1. 서비스 정의: Protocol Buffers를 사용해 서비스 인터페이스와 메시지 구조 정의
2. 코드 생성: protoc 컴파일러로 클라이언트/서버 코드 자동 생성
3. 통신: HTTP/2 프로토콜 위에서 이진 데이터 교환
4. 직렬화/역직렬화: Protocol Buffers를 사용해 효율적인 데이터 처리

**주요 특징:**
- 언어 및 플랫폼 중립적
- 구조화된 데이터 정의를 위한 간결한 구문
- 압축된 이진 형식으로 직렬화
- JSON보다 빠르고 작은 메시지 사이즈
- 확장 가능한 스키마 설계

**예제 .proto 파일:**
```protobuf
syntax = "proto3";

package example;

service GreetingService {
  rpc SayHello (HelloRequest) returns (HelloResponse);
}

message HelloRequest {
  string name = 1;
}

message HelloResponse {
  string greeting = 1;
}
```

## gRPC의 서비스 유형
gRPC는 네 가지 통신 패턴을 지원합니다:

1. **단일 RPC (Unary RPC)**: 클라이언트가 단일 요청을 보내고 단일 응답을 받음
   ```protobuf
   rpc SayHello (HelloRequest) returns (HelloResponse);
   ```

2. **서버 스트리밍 RPC**: 클라이언트가 단일 요청을 보내고 서버에서 응답 스트림을 받음
   ```protobuf
   rpc SubscribeUpdates (SubscriptionRequest) returns (stream Update);
   ```

3. **클라이언트 스트리밍 RPC**: 클라이언트가 요청 스트림을 보내고 서버에서 단일 응답을 받음
   ```protobuf
   rpc ProcessBatch (stream DataChunk) returns (ProcessSummary);
   ```

4. **양방향 스트리밍 RPC**: 클라이언트와 서버가 독립적으로 메시지 스트림을 주고받음
   ```protobuf
   rpc Chat (stream ChatMessage) returns (stream ChatMessage);
   ```

## gRPC vs REST
| 특성 | gRPC | REST |
|------|------|------|
| 프로토콜 | HTTP/2 | HTTP |
| 페이로드 포맷 | Protocol Buffers (이진) | JSON (텍스트) |
| 계약 | 엄격한 계약 (.proto) | 느슨한 계약 (필수 아님) |
| 코드 생성 | 기본 지원 | 스웨거 등 도구 필요 |
| 브라우저 지원 | 제한적 (gRPC-Web 필요) | 네이티브 지원 |
| 스트리밍 | 양방향 지원 | 제한적 |
| 성능 | 더 빠름, 리소스 효율적 | 상대적으로 느림 |
| 학습 곡선 | 가파름 | 완만함 |

## gRPC 사용 사례
- **마이크로서비스 간 통신**: 효율적인 내부 서비스 통신
- **멀티 언어 환경**: 다양한 언어로 작성된 시스템 연결
- **리소스 제약 환경**: 모바일, IoT 애플리케이션
- **실시간 통신**: 양방향 스트리밍이 필요한 애플리케이션
- **낮은 지연시간 환경**: 통신 오버헤드 최소화 필요시
- **폴리글랏 프로그래밍**: 여러 언어 환경에서 일관된 API 제공


## 시작하기

### 1. Protocol Buffers 설치
```bash
# Linux
apt-get install protobuf-compiler

# macOS
brew install protobuf

# Windows (chocolatey)
choco install protoc
```

### 2. 언어별 gRPC 도구 설치 (예: Python)
```bash
pip install grpcio grpcio-tools
```

### 3. 서비스 정의 (.proto 파일 생성)
```protobuf
syntax = "proto3";

package helloworld;

service Greeter {
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}

message HelloRequest {
  string name = 1;
}

message HelloReply {
  string message = 1;
}
```

### 4. 코드 생성
```bash
# Python 예제
python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. helloworld.proto
```

### 5. 서버 구현 (Python 예제)
```python
import grpc
from concurrent import futures
import helloworld_pb2
import helloworld_pb2_grpc

class Greeter(helloworld_pb2_grpc.GreeterServicer):
    def SayHello(self, request, context):
        return helloworld_pb2.HelloReply(message=f"Hello, {request.name}!")

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    helloworld_pb2_grpc.add_GreeterServicer_to_server(Greeter(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    server.wait_for_termination()

if __name__ == '__main__':
    serve()
```

### 6. 클라이언트 구현 (Python 예제)
```python
import grpc
import helloworld_pb2
import helloworld_pb2_grpc

def run():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = helloworld_pb2_grpc.GreeterStub(channel)
        response = stub.SayHello(helloworld_pb2.HelloRequest(name='World'))
    print("Greeter client received: " + response.message)

if __name__ == '__main__':
    run()
```

## gRPC 사용 시 고려사항

### 장점
- 높은 성능과 효율성
- 강력한 타입 안전성
- 다양한 언어 지원
- 양방향 스트리밍 기능
- 자동 코드 생성
- 확장 가능한 메타데이터 기능

### 단점
- 웹 브라우저에서 직접 사용 어려움 (gRPC-Web 필요)
- 학습 곡선이 가파름
- 텍스트 기반 도구와의 호환성 제한
- JSON과 같은 인간 가독성 부족

### 모범 사례
- 명확한 서비스 및 메시지 설계
- API 버전 관리 전략 수립
- 오류 처리 메커니즘 구현
- 적절한 타임아웃 설정
- 보안 인증 구현
- 성능 모니터링 설정

