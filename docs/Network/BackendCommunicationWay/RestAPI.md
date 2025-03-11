# REST API 완벽 가이드

## 목차
- [소개](#소개)
- [REST 아키텍처](#rest-아키텍처)
- [REST API 디자인 원칙](#rest-api-디자인-원칙)
  - [리소스 중심 설계](#리소스-중심-설계)
  - [리소스 표현](#리소스-표현)
  - [자원 조작 방법](#자원-조작-방법)
- [HTTP 메소드](#http-메소드)
- [상태 코드](#상태-코드)
- [URL 디자인](#url-디자인)
- [요청과 응답 형식](#요청과-응답-형식)
- [인증과 권한](#인증과-권한)
- [버전 관리](#버전-관리)
- [문서화](#문서화)
- [모범 사례](#모범-사례)
- [일반적인 오류](#일반적인-오류)
- [도구 및 리소스](#도구-및-리소스)

## 소개

REST(Representational State Transfer)는 웹 서비스를 위한 아키텍처 스타일로, 분산 환경에서 시스템 간의 통신을 위한 가벼운 인터페이스를 제공합니다. 이 문서는 효과적인 REST API를 설계하고 구현하는 데 필요한 모든 정보를 제공합니다.

## REST 아키텍처

1. **클라이언트-서버 구조**: 관심사 분리 원칙에 따라 클라이언트와 서버의 역할을 명확히 구분합니다.
2. **무상태(Stateless)**: 각 요청은 이전 요청과 독립적이며, 서버는 클라이언트의 상태를 저장하지 않습니다.
3. **캐시 가능(Cacheable)**: 응답은 캐시 가능 여부를 명시해야 합니다.
4. **계층화 시스템(Layered System)**: 클라이언트는 중간 계층의 존재 여부를 알 필요가 없습니다.
5. **통일된 인터페이스(Uniform Interface)**: 시스템 아키텍처를 단순화하고 가시성을 향상시킵니다.
6. **자체 서술적 메시지**: 각 메시지는 자신을 처리하는 방법에 대한 충분한 정보를 포함해야 합니다.

## REST API 디자인 원칙

### 리소스 중심 설계
REST API는 리소스를 중심으로 설계되며, 각 리소스는 고유한 URI(Uniform Resource Identifier)로 식별됩니다.

```
https://api.example.com/users           // 사용자 컬렉션
https://api.example.com/users/123       // 특정 사용자
https://api.example.com/users/123/posts // 특정 사용자의 게시물
```

### 리소스 표현
리소스는 다양한 형식(JSON, XML, HTML 등)으로 표현될 수 있으며, 클라이언트는 Accept 헤더를 통해 원하는 표현 형식을 요청할 수 있습니다.

### 자원 조작 방법
HTTP 메서드를 사용하여 리소스를 조작합니다.

## HTTP 메소드

REST API는 다음과 같은 HTTP 메소드를 사용하여 CRUD(Create, Read, Update, Delete) 작업을 수행합니다:

| 메소드 | 설명 | 특징 |
|--------|------|------|
| GET | 리소스 조회 | 멱등성(idempotent), 캐시 가능, 안전함 |
| POST | 리소스 생성 | 비 멱등성, 캐시 불가능, 안전하지 않음 |
| PUT | 리소스 전체 수정/교체 | 멱등성, 캐시 불가능, 안전하지 않음 |
| PATCH | 리소스 부분 수정 | 비 멱등성, 캐시 불가능, 안전하지 않음 |
| DELETE | 리소스 삭제 | 멱등성, 캐시 불가능, 안전하지 않음 |

> **멱등성(Idempotent)**: 동일한 요청을 여러 번 수행해도 결과가 달라지지 않는 성질

## 상태 코드

REST API는 응답 상태를 표현하기 위해 표준 HTTP 상태 코드를 사용합니다:

### 1xx: 정보 응답
- 100 Continue: 요청의 첫 부분이 수신되었으며 나머지를 계속해서 보내도 된다는 의미

### 2xx: 성공
- 200 OK: 요청이 성공적으로 처리됨
- 201 Created: 리소스 생성 성공
- 204 No Content: 요청은 성공했지만 반환할 콘텐츠가 없음

### 3xx: 리다이렉션
- 301 Moved Permanently: 리소스의 URI가 변경됨
- 304 Not Modified: 클라이언트의 캐시된 리소스가 최신 상태임

### 4xx: 클라이언트 오류
- 400 Bad Request: 잘못된 요청 구문, 유효하지 않은 요청 메시지
- 401 Unauthorized: 인증이 필요함
- 403 Forbidden: 권한 부족
- 404 Not Found: 리소스를 찾을 수 없음
- 405 Method Not Allowed: 허용되지 않은 HTTP 메서드
- 429 Too Many Requests: 너무 많은 요청

### 5xx: 서버 오류
- 500 Internal Server Error: 서버 내부 오류
- 502 Bad Gateway: 게이트웨이 오류
- 503 Service Unavailable: 서비스 일시적 사용 불가

## URL 디자인

### 모범 사례

1. **명사 사용**: URL은 리소스를 표현하는 명사를 사용합니다.
   ```
   좋음: /users, /articles
   나쁨: /getUsers, /createArticle
   ```

2. **계층 관계 표현**: URL은 리소스 간의 계층 관계를 표현합니다.
   ```
   /users/123/posts
   /departments/987/employees
   ```

3. **소문자 사용**: URL은 소문자로 작성합니다.
   ```
   좋음: /users/123
   나쁨: /Users/123
   ```

4. **하이픈 사용**: 단어 구분은 하이픈(-)을 사용합니다.
   ```
   좋음: /blog-posts
   나쁨: /blog_posts, /blogPosts
   ```

5. **파일 확장자 사용하지 않기**: 대신 HTTP Accept 헤더를 사용합니다.
   ```
   좋음: /users/123
   나쁨: /users/123.json
   ```

6. **쿼리 파라미터 활용**: 필터링, 정렬, 페이징에 쿼리 파라미터를 사용합니다.
   ```
   /users?role=admin&status=active
   /products?sort=price_asc&page=2
   ```

## 요청과 응답 형식

### 콘텐츠 타입
일반적으로 REST API는 JSON 형식을 사용하며, `Content-Type` 헤더로 명시합니다:
```
Content-Type: application/json
```

### 요청 예시
```http
POST /users HTTP/1.1
Host: api.example.com
Content-Type: application/json
Authorization: Bearer {token}

{
  "name": "홍길동",
  "email": "hong@example.com",
  "age": 30
}
```

### 응답 예시
```http
HTTP/1.1 201 Created
Content-Type: application/json
Location: https://api.example.com/users/123

{
  "id": "123",
  "name": "홍길동",
  "email": "hong@example.com",
  "age": 30,
  "created_at": "2025-03-10T09:00:00Z"
}
```

## 인증과 권한

### 인증 방식
1. **기본 인증(Basic Authentication)**: 사용자 이름과 비밀번호를 Base64로 인코딩
2. **API 키**: 요청 헤더나 쿼리 파라미터에 API 키 포함
3. **OAuth 2.0**: 토큰 기반의 인증 프로토콜
4. **JWT(JSON Web Token)**: 자체 포함된 토큰 기반 인증

### OAuth 2.0 흐름
1. **인증 코드 부여**: 웹 애플리케이션에 적합
2. **암시적 부여**: 브라우저 기반 애플리케이션에 적합
3. **리소스 소유자 비밀번호 자격 증명**: 신뢰할 수 있는 애플리케이션에 적합
4. **클라이언트 자격 증명**: 서버 간 통신에 적합

## 버전 관리

API 변경 시 기존 클라이언트를 중단시키지 않기 위해 버전 관리가 필요합니다.

### 버전 관리 방법
1. **URL 경로**: `/api/v1/users`
2. **쿼리 파라미터**: `/api/users?version=1`
3. **헤더**: `Accept: application/vnd.example.v1+json`
4. **Content-Type**: `Content-Type: application/vnd.example.v1+json`

## 문서화

API 문서화는 개발자가 API를 쉽게 이해하고 사용할 수 있도록 돕습니다.

### 문서화 도구
1. **Swagger/OpenAPI**: RESTful API를 설계, 빌드, 문서화하기 위한 도구
2. **API Blueprint**: 마크다운 기반 API 문서화 언어
3. **RAML(RESTful API Modeling Language)**: YAML 기반의 RESTful API 모델링 언어
4. **Postman**: API 테스트 및 문서화 도구

## 모범 사례

1. **적절한 HTTP 메서드 사용**: 각 작업에 맞는 HTTP 메서드를 사용합니다.
2. **HATEOAS(Hypermedia as the Engine of Application State)**: 응답에 관련 리소스 링크를 포함합니다.
   ```json
   {
     "id": "123",
     "name": "홍길동",
     "_links": {
       "self": { "href": "/users/123" },
       "posts": { "href": "/users/123/posts" }
     }
   }
   ```
3. **페이지네이션**: 대량의 데이터를 처리할 때 페이지네이션을 적용합니다.
   ```
   /users?page=2&per_page=20
   ```
4. **필드 필터링**: 클라이언트가 필요한 필드만 요청할 수 있도록 합니다.
   ```
   /users?fields=id,name,email
   ```
5. **에러 처리**: 일관된 형식의 에러 응답을 제공합니다.
   ```json
   {
     "error": {
       "code": "INVALID_PARAMETER",
       "message": "이메일 형식이 올바르지 않습니다.",
       "details": {
         "field": "email"
       }
     }
   }
   ```
6. **요청 제한(Rate Limiting)**: API 호출 횟수를 제한하고 헤더로 정보를 제공합니다.
   ```
   X-RateLimit-Limit: 100
   X-RateLimit-Remaining: 87
   X-RateLimit-Reset: 1425858205
   ```

## HATEOAS 활용법

HATEOAS(Hypermedia As The Engine Of Application State)는 RESTful API의 중요한 개념 중 하나로, 클라이언트가 서버와 상호작용할 수 있는 동적 링크를 포함하는 응답을 제공합니다. 이를 통해 클라이언트는 서버의 응답을 기반으로 다음 작업을 결정할 수 있습니다.

### HATEOAS의 구성 요소

1. **링크(Link)**: 리소스와 관련된 다른 리소스의 URL을 제공합니다.
2. **관계(Rel)**: 링크가 나타내는 관계를 설명합니다.
3. **메서드(Method)**: 링크를 통해 수행할 수 있는 HTTP 메서드를 나타냅니다.

### 예시

사용자 리소스에 대한 HATEOAS 응답 예시는 다음과 같습니다.
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "links": [
    {
      "rel": "self",
      "href": "/users/1",
      "method": "GET"
    },
    {
      "rel": "update",
      "href": "/users/1",
      "method": "PUT"
    },
    {
      "rel": "delete",
      "href": "/users/1",
      "method": "DELETE"
    },
    {
      "rel": "posts",
      "href": "/users/1/posts",
      "method": "GET"
    }
  ]
}
```

### HATEOAS를 활용하는 예시

1. **리소스 생성**: 클라이언트는 서버에 새로운 리소스를 생성하기 위해 POST 요청을 보냅니다. 서버는 생성된 리소스의 URL을 포함한 응답을 반환합니다.
2. **리소스 조회**: 클라이언트는 서버에 GET 요청을 보내 특정 리소스를 조회합니다. 서버는 리소스의 현재 상태와 함께 관련 링크를 포함한 응답을 반환합니다.
3. **리소스 업데이트**: 클라이언트는 서버에 PUT 또는 PATCH 요청을 보내 리소스를 업데이트합니다. 서버는 업데이트된 리소스의 URL을 포함한 응답을 반환합니다.
4. **리소스 삭제**: 클라이언트는 서버에 DELETE 요청을 보내 리소스를 삭제합니다. 서버는 삭제된 리소스의 URL을 포함한 응답을 반환합니다.

### 예시 코드

다음은 HATEOAS를 사용하여 사용자 리소스를 생성, 조회, 업데이트 및 삭제하는 예시 코드입니다.


```python
import requests

# 서버 URL
base_url = "http://example.com/api"

# 사용자 생성
def create_user(name, email):
    url = f"{base_url}/users"
    payload = {"name": name, "email": email}
    response = requests.post(url, json=payload)
    if response.status_code == 201:
        print("사용자 생성 성공:", response.json())
    else:
        print("사용자 생성 실패:", response.status_code, response.text)

# 사용자 조회
def get_user(user_id):
    url = f"{base_url}/users/{user_id}"
    response = requests.get(url)
    if response.status_code == 200:
        print("사용자 조회 성공:", response.json())
    else:
        print("사용자 조회 실패:", response.status_code, response.text)

# 사용자 업데이트
def update_user(user_id, name=None, email=None):
    url = f"{base_url}/users/{user_id}"
    payload = {}
    if name:
        payload["name"] = name
    if email:
        payload["email"] = email
    response = requests.put(url, json=payload)
    if response.status_code == 200:
        print("사용자 업데이트 성공:", response.json())
    else:
        print("사용자 업데이트 실패:", response.status_code, response.text)

# 사용자 삭제
def delete_user(user_id):
    url = f"{base_url}/users/{user_id}"
    response = requests.delete(url)
    if response.status_code == 204:
        print("사용자 삭제 성공")
    else:
        print("사용자 삭제 실패:", response.status_code, response.text)

# 예시 사용
if __name__ == "__main__":
    # 사용자 생성
    create_user("John Doe", "john.doe@example.com")

    # 사용자 조회
    get_user(1)

    # 사용자 업데이트
    update_user(1, name="Jane Doe")

    # 사용자 삭제
    delete_user(1)

```

## 일반적인 오류

1. **적절하지 않은 HTTP 메서드 사용**: 각 작업에 적합한 HTTP 메서드를 사용해야 합니다.
2. **비일관적인 엔드포인트 네이밍**: 일관된 명명 규칙을 적용해야 합니다.
3. **캐싱 미활용**: 적절한 캐싱 헤더를 설정하여 성능을 향상시켜야 합니다.
4. **상태 코드 오용**: 상황에 맞는 HTTP 상태 코드를 사용해야 합니다.
5. **과도한 중첩**: URL 경로의 중첩 수준을 최소화해야 합니다.

## 도구 및 리소스

### 개발 도구
1. **Postman**: API 개발 및 테스트 도구
2. **Insomnia**: REST 및 GraphQL API 클라이언트
3. **Swagger/OpenAPI**: API 설계 및 문서화 도구
4. **cURL**: 명령줄 HTTP 클라이언트

### 프레임워크 및 라이브러리
1. **Node.js**: Express, Fastify, NestJS
2. **Python**: Django REST Framework, Flask-RESTful
3. **Java**: Spring Boot, Jersey
4. **PHP**: Laravel, Symfony
5. **Ruby**: Rails API

### 모니터링 및 분석
1. **Prometheus**: 메트릭 수집 및 모니터링
2. **Grafana**: 데이터 시각화 및 모니터링
3. **New Relic**: 애플리케이션 성능 모니터링
4. **Datadog**: 인프라 및 애플리케이션 모니터링

