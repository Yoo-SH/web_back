# URI 설계 가이드

## 목차
1. [개요](#개요)
2. [URI의 기본 개념](#uri의-기본-개념)
3. [URI 설계 원칙](#uri-설계-원칙)
4. [REST URI 설계 모범 사례](#rest-uri-설계-모범-사례)
5. [URI 설계 패턴](#uri-설계-패턴)
6. [URI 구조화 전략](#uri-구조화-전략)
7. [URI 설계 시 주의사항](#uri-설계-시-주의사항)
8. [실제 예시](#실제-예시)
9. [결론](#결론)

## 개요

URI(Uniform Resource Identifier)는 웹에서 리소스를 식별하는 문자열입니다. 잘 설계된 URI는 웹 서비스의 사용성, 확장성 및 유지 관리성을 크게 향상시킬 수 있습니다. 이 문서는 효과적인 URI 설계를 위한 가이드라인과 모범 사례를 제공합니다.

## URI의 기본 개념

### URI vs URL vs URN

- **URI(Uniform Resource Identifier)**: 리소스를 식별하는 통합 방식
- **URL(Uniform Resource Locator)**: 리소스의 위치를 지정하는 URI의 하위 집합
- **URN(Uniform Resource Name)**: 위치에 상관없이 리소스의 이름을 지정하는 URI의 하위 집합

### URI 구성 요소

```
scheme:[//authority]path[?query][#fragment]
```

- **scheme**: URI의 유형(http, https, ftp 등)
- **authority**: 선택적 권한 구성 요소(일반적으로 'host[:port]')
- **path**: 리소스 경로
- **query**: 선택적 쿼리 문자열(key=value 형식의 매개변수)
- **fragment**: 선택적 프래그먼트 식별자(문서 내 특정 섹션 참조)

## URI 설계 원칙

### 1. 명확성(Clarity)
URI는 그 자체로 의미를 전달할 수 있어야 합니다. 단어 선택과 구조는 URI가 어떤 리소스를 식별하는지 명확히 나타내야 합니다.

### 2. 예측 가능성(Predictability)
URI는 일관된 패턴을 따라야 합니다. 사용자가 특정 리소스의 URI를 추측할 수 있을 정도로 예측 가능해야 합니다.

### 3. 표현성(Expressiveness)
URI는 식별하는 리소스의 특성이나 속성을 나타내야 합니다.

### 4. 일관성(Consistency)
URI 설계 패턴은 전체 애플리케이션에서 일관되게 적용되어야 합니다.

### 5. 안정성(Stability)
URI는 가능한 한 오래 유지되어야 합니다. 리소스 이동이나 삭제 시 적절한 리디렉션이 구현되어야 합니다.

## REST URI 설계 모범 사례

### 1. 명사 사용하기
리소스를 나타낼 때는 명사를 사용하세요.
- 좋은 예: `/users`, `/products`, `/categories`
- 나쁜 예: `/getUsers`, `/listProducts`

### 2. 복수형 명사 사용하기
일관성을 위해 리소스 컬렉션을 나타낼 때 복수형 명사를 사용하세요.
- 좋은 예: `/users`, `/products`
- 나쁜 예: `/user`, `/product`

### 3. 계층 구조 활용하기
리소스 간의 관계를 표현하기 위해 계층 구조를 사용하세요.
- `/users/{id}/orders`
- `/products/{id}/reviews`

### 4. 소문자 사용하기
URI는 대소문자를 구분하므로 일관성을 위해 소문자를 사용하세요.
- 좋은 예: `/api/products`
- 나쁜 예: `/API/Products`

### 5. 하이픈(-) 사용하기
단어 구분을 위해 하이픈을 사용하세요. 밑줄(_)이나 공백은 피하세요.
- 좋은 예: `/blog-posts`
- 나쁜 예: `/blog_posts` 또는 `/blogPosts`

### 6. 파일 확장자 피하기
URI에 `.html`, `.php` 등의 파일 확장자를 포함하지 마세요. 대신 HTTP의 콘텐츠 협상 메커니즘을 사용하세요.
- 좋은 예: `/articles/123`
- 나쁜 예: `/articles/123.html`

### 7. CRUD 작업에 HTTP 메서드 사용하기
URI에 작업을 포함하지 말고 HTTP 메서드를 사용하세요.

- **GET**: 리소스 검색
- **POST**: 새 리소스 생성
- **PUT**: 기존 리소스 업데이트
- **DELETE**: 리소스 삭제
- **PATCH**: 리소스 부분 업데이트

좋은 예:
- 사용자 목록 가져오기: `GET /users`
- 사용자 생성하기: `POST /users`
- 특정 사용자 가져오기: `GET /users/{id}`
- 특정 사용자 업데이트하기: `PUT /users/{id}`
- 특정 사용자 삭제하기: `DELETE /users/{id}`

나쁜 예:
- `/getUsers`
- `/createUser`
- `/updateUser/{id}`
- `/deleteUser/{id}`

## URI 설계 패턴

### 1. 리소스 컬렉션 패턴
컬렉션을 표현할 때 복수형 명사 사용:
- `/users`: 모든 사용자
- `/users/{id}`: 특정 사용자

### 2. 하위 리소스 패턴
관계를 표현하기 위한 중첩 리소스:
- `/users/{id}/posts`: 특정 사용자의 모든 게시물
- `/users/{userId}/posts/{postId}`: 특정 사용자의 특정 게시물

### 3. 필터 패턴
쿼리 매개변수를 사용한 컬렉션 필터링:
- `/products?category=electronics`: 전자제품 카테고리의 제품
- `/users?status=active&role=admin`: 활성 상태인 관리자 역할의 사용자

### 4. 페이지네이션 패턴
대량의 데이터 처리를 위한 페이지네이션:
- `/products?page=2&limit=20`: 페이지당 20개 항목의 2페이지

### 5. 버전 관리 패턴
API 버전 관리:
- `/v1/users`: API 버전 1의 사용자 엔드포인트
- `/v2/users`: API 버전 2의 사용자 엔드포인트

다른 버전 관리 전략:
- 쿼리 매개변수: `/users?version=1`
- 헤더: `Accept: application/vnd.company.api+json;version=1`

## URI 구조화 전략

### 1. 도메인 기반 구조
각 도메인이나 비즈니스 영역에 따라 URI 구조화:
- `/customers/...`: 고객 관련 리소스
- `/inventory/...`: 재고 관련 리소스
- `/billing/...`: 청구 관련 리소스

### 2. 기능 기반 구조
기능이나 작업에 따라 URI 구조화:
- `/search/...`: 검색 관련 리소스
- `/admin/...`: 관리 관련 리소스
- `/reports/...`: 보고서 관련 리소스

### 3. 리소스 기반 구조 (REST 스타일)
리소스 유형에 따라 URI 구조화:
- `/users/...`: 사용자 리소스
- `/products/...`: 제품 리소스
- `/orders/...`: 주문 리소스

## URI 설계 시 주의사항

### 1. 너무 깊은 계층 구조 피하기
URI 경로가 너무 깊으면 복잡하고 사용하기 어려워집니다. 일반적으로 3-4단계를 넘지 않는 것이 좋습니다.

- 좋은 예: `/users/{id}/preferences`
- 나쁜 예: `/accounts/{accountId}/users/{userId}/profiles/{profileId}/preferences/{preferenceId}`

### 2. 동사 대신 리소스 기반 설계하기
동사보다는 명사를 사용해 리소스를 중심으로 설계하세요.

- 좋은 예: `POST /orders` (주문 생성)
- 나쁜 예: `/createOrder`

### 3. 구현 세부 정보 노출 피하기
내부 구현 세부 정보는 URI에 노출하지 마세요.

- 좋은 예: `/articles/{id}`
- 나쁜 예: `/mysql-data/articles-table/{id}`

### 4. 민감한 데이터 포함 피하기
URI는 로그에 기록되고 브라우저 기록에 저장될 수 있으므로 민감한 데이터를 포함하지 마세요.

- 좋은 예: `/users/{id}` (POST 요청 본문에 비밀번호 포함)
- 나쁜 예: `/users?password=secret123`

### 5. URI 길이 제한 고려하기
일부 클라이언트와 서버는 URI 길이를 제한합니다. URI를 합리적인 길이로 유지하세요.

## 실제 예시

### 전자상거래 API

**제품 관리:**
- 모든 제품 가져오기: `GET /products`
- 특정 제품 가져오기: `GET /products/{id}`
- 제품 생성하기: `POST /products`
- 제품 업데이트하기: `PUT /products/{id}`
- 제품 삭제하기: `DELETE /products/{id}`
- 카테고리별 제품 필터링: `GET /products?category={category}`
- 특정 제품의 리뷰: `GET /products/{id}/reviews`

**사용자 관리:**
- 사용자 등록: `POST /users`
- 사용자 로그인: `POST /auth/login`
- 사용자 프로필 가져오기: `GET /users/{id}`
- 사용자 프로필 업데이트: `PUT /users/{id}`

**주문 관리:**
- 주문 생성: `POST /orders`
- 특정 주문 가져오기: `GET /orders/{id}`
- 사용자의 모든 주문 가져오기: `GET /users/{id}/orders`
- 주문 상태 업데이트: `PATCH /orders/{id}`

### 블로그 API

**게시물 관리:**
- 모든 게시물 가져오기: `GET /posts`
- 특정 게시물 가져오기: `GET /posts/{id}`
- 게시물 생성하기: `POST /posts`
- 게시물 업데이트하기: `PUT /posts/{id}`
- 게시물 삭제하기: `DELETE /posts/{id}`
- 태그별 게시물 필터링: `GET /posts?tag={tag}`

**댓글 관리:**
- 특정 게시물의 모든 댓글 가져오기: `GET /posts/{id}/comments`
- 댓글 생성하기: `POST /posts/{id}/comments`
- 댓글 업데이트하기: `PUT /comments/{id}`
- 댓글 삭제하기: `DELETE /comments/{id}`

## 결론

효과적인 URI 설계는 웹 서비스의 사용성, 유지 관리성 및 확장성에 중요한 역할을 합니다. 좋은 URI는 직관적이고, 일관되며, 예측 가능하고, 리소스 중심적이어야 합니다. 이 가이드에 설명된 원칙과 모범 사례를 따르면 사용자와 개발자 모두에게 친숙한 API를 만들 수 있습니다.

명심할 점은 URI 설계는 절대적인 규칙보다 일관성과 명확성에 중점을 두어야 한다는 것입니다. 귀하의 특정 애플리케이션 요구 사항에 맞는 규칙과 패턴을 선택하고 그것을 일관되게 적용하세요.