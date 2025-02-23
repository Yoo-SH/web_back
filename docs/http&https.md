# `HTTP(HyperText Transfer Protocol)`

## 목차
- [HTTP(HyperText Transfer Protocol)](#httphypertext-transfer-protocol)
  - [개요](#개요)
  - [HTTP의 특징](#http의-특징)
  - [HTTP 요청(Request) 구조](#http-요청request-구조)
  - [HTTP 응답(Response) 구조](#http-응답response-구조)
  - [HTTP와 HTTPS 차이점](#http와-https-차이점)
  - [HTTP 버전별 차이](#http-버전별-차이)
  - [추가 개념](#추가-개념)
- [HTTPS란?](#https란)
- [참고자료](#참고자료)
## 개요
HTTP(HyperText Transfer Protocol)는 웹에서 하이퍼텍스트 문서를 전송하기 위한 프로토콜입니다. 클라이언트와 서버 간의 요청(Request)과 응답(Response)을 처리하는 방식으로 동작하며, 주로 HTML 문서, 이미지, 동영상 등의 리소스를 전송하는 데 사용됩니다.


## HTTP의 특징
- **비연결성(Connectionless)**: 요청과 응답이 완료되면 연결을 끊습니다.
- **무상태성(Stateless)**: 이전 요청과 현재 요청 사이에 연관이 없습니다.
- **확장성(Extensible)**: 다양한 확장 기능을 추가할 수 있습니다.
- **텍스트 기반 프로토콜**: 사람이 읽을 수 있는 형태의 메시지를 사용합니다.

## HTTP 요청(Request) 구조
HTTP 요청은 다음과 같은 구조로 이루어집니다.
```
METHOD 요청-URI HTTP/버전
헤더(Header)

바디(Body)
```
### 주요 요청 메서드
| 메서드 | 설명 |
|--------|---------------------------------|
| GET | 데이터를 요청할 때 사용 |
| POST | 데이터를 전송할 때 사용 |
| PUT | 데이터를 갱신할 때 사용 |
| DELETE | 데이터를 삭제할 때 사용 |
| PATCH | 부분적으로 데이터를 수정할 때 사용 |
| OPTIONS | 지원하는 메서드 확인 |

### 요청 헤더 예시
```
GET /index.html HTTP/1.1
Host: www.example.com
User-Agent: Mozilla/5.0
Accept: text/html
```


## HTTP 응답(Response) 구조
HTTP 응답은 다음과 같은 구조로 이루어집니다.
```
HTTP/버전 상태 코드(Status Code) 상태 메시지
헤더(Header)

바디(Body)
```
### 주요 상태 코드
| 상태 코드 | 설명 |
|----------|---------------------------------|
| 200 OK | 요청이 성공적으로 수행됨 |
| 201 Created | 요청이 성공적으로 처리되고 새로운 리소스 생성 |
| 204 No Content | 요청 성공했지만 반환할 콘텐츠 없음 |
| 301 Moved Permanently | 영구적으로 이동됨 |
| 302 Found | 일시적으로 이동됨 |
| 400 Bad Request | 잘못된 요청 |
| 401 Unauthorized | 인증 필요 |
| 403 Forbidden | 접근 권한 없음 |
| 404 Not Found | 요청한 리소스를 찾을 수 없음 |
| 500 Internal Server Error | 서버 내부 오류 |

### 응답 헤더 예시
```
HTTP/1.1 200 OK
Content-Type: text/html
Content-Length: 1024
```



## HTTP와 HTTPS 차이점
| 구분 | HTTP | HTTPS |
|------|------|--------|
| 보안 | 암호화되지 않음 | [SSL/TLS]( [SSL/TLS](https://github.com/Yoo-SH/web_back/blob/main/docs/ssl&tls.md))로 암호화됨 |
| 포트 | 기본적으로 80번 사용 | 기본적으로 443번 사용 |
| 속도 | 상대적으로 빠름 | 암호화로 인해 약간 느림 |
| 신뢰성 | 중간자 공격 가능성 높음 | 보안 인증서를 통한 신뢰성 제공 |


## HTTP 버전별 차이
| 버전 | 특징 |
|------|---------------------------|
| HTTP/1.0 | 요청마다 새로운 연결 생성 |
| HTTP/1.1 | 지속적 연결(Persistent Connection) 지원 |
| HTTP/2 | 멀티플렉싱 지원, 성능 개선 |
| HTTP/3 | QUIC 프로토콜 기반, 속도 향상 |


## 추가 개념
### 1. 쿠키(Cookie)
- 클라이언트에 저장되는 작은 데이터 조각으로, 상태 정보를 유지하는 데 사용됨

### 2. 세션(Session)
- 서버에서 상태를 관리하기 위한 기술로, 클라이언트의 요청을 식별하는 데 사용됨

### 3. CORS(Cross-Origin Resource Sharing)
- 다른 출처(도메인, 프로토콜, 포트) 간의 리소스 공유를 제어하는 메커니즘



# `HTTPS란?`
HTTPS(HyperText Transfer Protocol Secure)는 HTTP에 [SSL/TLS](https://github.com/Yoo-SH/web_back/blob/main/docs/ssl&tls.md)암호화를 추가한 보안 프로토콜입니다. 이를 통해 웹사이트와 사용자 간의 통신이 안전하게 보호됩니다.

### 🔸 HTTPS의 장점
- **데이터 보안 강화**: 데이터가 암호화되어 전송 중 도청 및 조작 방지
- **신뢰성 향상**: 브라우저 주소창에 자물쇠 아이콘이 표시되어 사용자 신뢰도 증가
- **SEO(검색 엔진 최적화) 향상**: Google 등 검색 엔진에서 HTTPS 사용 웹사이트를 우선적으로 노출
- **데이터 무결성 보장**: 데이터가 변조되지 않고 안전하게 전달

# `참고자료`
  - [[개념 정리] http content-type 관한 정리](https://yunzema.tistory.com/186)
  - [Content-Type](https://velog.io/@eunhye_k/Content-Type%EC%9D%98-%EC%9D%B4%ED%95%B4)
  