# Backend Communication Patterns: Request vs Response

## 개요
백엔드 통신 패턴에서 요청(request)과 응답(response) 모델은 클라이언트와 서버 간의 데이터 교환의 기본 메커니즘입니다. 이 문서에서는 이러한 패턴의 기본 개념, 구현 방법, 장단점을 설명합니다.

## 목차
- [기본 개념](#기본-개념)
- [통신 패턴 유형](#통신-패턴-유형)
- [요청-응답 모델의 구성 요소](#요청-응답-모델의-구성-요소)
- [구현 방법](#구현-방법)
- [일반적인 문제 및 해결책](#일반적인-문제-및-해결책)
- [최적화 전략](#최적화-전략)
- [보안 고려사항](#보안-고려사항)
- [모범 사례](#모범-사례)

## 기본 개념

### 요청(Request)
클라이언트가 서버에 특정 작업이나 정보를 요청하는 메시지입니다. 요청은 일반적으로 다음 요소를 포함합니다:
- 메서드/동작(GET, POST, PUT, DELETE 등)
- 리소스 식별자(URL, URI)
- 헤더(메타데이터)
- 본문(body)/페이로드(필요한 경우)

### 응답(Response)
서버가 클라이언트의 요청을 처리한 후 보내는 메시지입니다. 응답은 일반적으로 다음을 포함합니다:
- 상태 코드(200 OK, 404 Not Found 등)
- 헤더(메타데이터)
- 본문(요청된 데이터 또는 상태 정보)

## 통신 패턴 유형

### 1. 동기식 요청-응답
- **특징**: 클라이언트가 요청을 보내고 응답을 받을 때까지 대기
- **장점**: 간단한 구현, 직관적인 흐름
- **단점**: 응답 지연 시 클라이언트 차단
- **사용 사례**: 간단한 데이터 조회, CRUD 작업

### 2. 비동기식 요청-응답
- **특징**: 클라이언트가 요청을 보내고 나중에 응답을 받음
- **장점**: 클라이언트 차단 방지, 리소스 효율적 사용
- **단점**: 복잡한 구현, 상태 관리 필요
- **사용 사례**: 시간이 오래 걸리는 작업, 대용량 데이터 처리

### 3. 스트리밍
- **특징**: 서버가 데이터를 연속적인 흐름으로 전송
- **장점**: 실시간 데이터 처리, 부분적 데이터 사용 가능
- **단점**: 연결 관리 복잡성, 리소스 집약적
- **사용 사례**: 실시간 업데이트, 대용량 파일 전송

### 4. 이벤트 기반 통신
- **특징**: 서버가 특정 이벤트 발생 시 클라이언트에 알림
- **장점**: 실시간 업데이트, 리소스 효율적 사용
- **단점**: 복잡한 아키텍처, 메시지 순서 관리
- **사용 사례**: 채팅 애플리케이션, 알림 시스템

## 요청-응답 모델의 구성 요소

### HTTP 요청 구성요소
```
POST /api/users HTTP/1.1
Host: example.com
Content-Type: application/json
Authorization: Bearer token123

{
  "name": "John Doe",
  "email": "john@example.com"
}
```

### HTTP 응답 구성요소
```
HTTP/1.1 201 Created
Content-Type: application/json
Location: /api/users/123

{
  "id": 123,
  "name": "John Doe",
  "email": "john@example.com",
  "created_at": "2025-03-07T10:00:00Z"
}
```

## 구현 방법

### RESTful API
```java
// 서버 측 코드 (Spring Boot)
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.created(URI.create("/api/users/" + user.getId()))
                            .body(user);
    }
}

// 클라이언트 측 코드 (JavaScript)
async function createUser(userData) {
    const response = await fetch('https://example.com/api/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(userData)
    });
    
    if (!response.ok) {
        throw new Error('Failed to create user');
    }
    
    return response.json();
}
```

### GraphQL
```graphql
# 서버 측 스키마 정의
type User {
    id: ID!
    name: String!
    email: String!
    createdAt: String!
}

type Mutation {
    createUser(name: String!, email: String!): User!
}

# 클라이언트 측 쿼리
mutation {
    createUser(name: "John Doe", email: "john@example.com") {
        id
        name
        email
        createdAt
    }
}
```

### gRPC
```protobuf
// 프로토콜 버퍼 정의
syntax = "proto3";

service UserService {
    rpc CreateUser (CreateUserRequest) returns (User);
}

message CreateUserRequest {
    string name = 1;
    string email = 2;
}

message User {
    int32 id = 1;
    string name = 2;
    string email = 3;
    string created_at = 4;
}
```

## 일반적인 문제 및 해결책

### 1. 네트워크 지연
- **문제**: 느린 응답 시간으로 사용자 경험 저하
- **해결책**: 
  - 캐싱 구현
  - 비동기 통신 사용
  - 로드 밸런싱

### 2. 오류 처리
- **문제**: 예외 상황 관리 실패
- **해결책**:
  - 표준화된 오류 응답 형식
  - 재시도 메커니즘
  - 장애 격리 패턴

### 3. 확장성
- **문제**: 트래픽 증가에 따른 성능 저하
- **해결책**:
  - 마이크로서비스 아키텍처
  - 수평적 확장
  - 비동기 처리

## 최적화 전략

### 1. 응답 압축
```
// 서버 측 (Node.js Express)
app.use(compression());

// 클라이언트 측 (HTTP 헤더)
Accept-Encoding: gzip, deflate
```

### 2. 부분 응답
```
// REST API 구현
GET /api/users/123?fields=id,name,email
```

### 3. 배치 처리
```json
// 배치 요청
POST /api/batch
{
  "operations": [
    { "method": "GET", "path": "/users/1" },
    { "method": "GET", "path": "/users/2" },
    { "method": "POST", "path": "/products", "body": {"name": "Product"} }
  ]
}
```

## 보안 고려사항

### 1. 인증 및 권한 부여
```
// JWT 인증 예시
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 2. HTTPS 사용
모든 요청과 응답은 TLS/SSL을 통해 암호화되어야 합니다.

### 3. 입력 검증
```java
// 서버 측 입력 검증 (Java)
@PostMapping
public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
    // ...
}

public class UserRequest {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;
    
    @NotBlank
    @Email
    private String email;
    // ...
}
```

## 모범 사례

### 1. 버전 관리
```
// URL에 버전 포함
GET /api/v1/users

// 헤더에 버전 정보
Accept: application/json; version=1.0
```

### 2. 응답 상태 코드 표준화
- 200: 성공
- 201: 생성됨
- 400: 잘못된 요청
- 401: 인증 필요
- 403: 권한 부족
- 404: 리소스 없음
- 500: 서버 오류

### 3. 명확한 API 문서화
```yaml
# OpenAPI/Swagger 예시
paths:
  /users:
    post:
      summary: 새 사용자 생성
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/UserRequest'
      responses:
        '201':
          description: 사용자 생성됨
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
```

### 4. 응답/요청 형식 표준화
```json
// 표준 응답 형식
{
  "status": "success",
  "data": {
    "user": {
      "id": 123,
      "name": "John Doe",
      "email": "john@example.com"
    }
  },
  "meta": {
    "timestamp": "2025-03-07T10:00:00Z",
    "version": "1.0"
  }
}

// 오류 응답 형식
{
  "status": "error",
  "error": {
    "code": "INVALID_INPUT",
    "message": "이메일 형식이 올바르지 않습니다",
    "details": [
      {"field": "email", "message": "유효한 이메일 주소를 입력하세요"}
    ]
  },
  "meta": {
    "timestamp": "2025-03-07T10:00:00Z",
    "version": "1.0"
  }
}
```
