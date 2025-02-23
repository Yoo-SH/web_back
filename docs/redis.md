# `Redis`

## 목차
- [개요](#개요)
- [주요 특징](#주요-특징)
- [redis design 방법론](#redis-design-방법론)
- [redis design 고려사항](#redis-design-고려사항)
- [설치 및 실행](#설치-및-실행)
- [기본 명령어](#기본-명령어)
- [다양한 자료 형태와 사용 사례](#다양한-자료-형태와-사용-사례)
- [Redis Key Naming Convention](#redis-key-naming-convention)
- [Redis에서 직렬화와 역직렬화가 필요한 상황](#redis에서-직렬화와-역직렬화가-필요한-상황)
- [Redis 모듈 가이드](#redis-모듈-가이드)
- [참고자료](#참고자료)

## 개요
Redis(Remote Dictionary Server)는 오픈 소스, `인메모리 데이터 저장소`로, 높은 성능과 `다양한 데이터 구조를 지원하는 NoSQL 데이터베이스`입니다. 주로 캐싱, 세션 관리, 실시간 분석 및 메시지 브로커 역할을 수행합니다.

## 주요 특징
- **인메모리 데이터 저장**: 모든 데이터를 메모리에 저장하여 높은 속도를 제공
- **다양한 데이터 구조 지원**: 문자열(String), 리스트(List), 셋(Set), 정렬된 셋(Sorted Set), 해시(Hash), 비트맵(Bitmap), 하이퍼로그로그(HyperLogLog) 등 지원
- **영속성(Persistence) 지원**: RDB(Snapshot) 및 AOF(Append-Only File) 방식을 통해 데이터 보존 가능
- **고성능**: 낮은 레이턴시와 높은 처리량 제공
- **복제 및 클러스터링 지원**: 마스터-슬레이브 복제 및 Redis Cluster를 통한 확장성 확보
- **트랜잭션 지원**: MULTI, EXEC, DISCARD, WATCH 명령어를 통한 원자적 실행 가능
- **Pub/Sub 기능**: 메시지 브로커 역할 수행 가능

## redis design 방법론
1. 데이터에 어떤 쿼리를 할 것인지 먼저 생각한다.
2. 해당 쿼리에 베스트인 데이터 구조를 선택한다.

__비교__

- SQL에서는 데이터 구조를 먼저 정하고, 그에 맞는 쿼리를 작성한다.

## redis design 고려사항
1. 어떤 타입의 데이터를 저장할 것인가?
2. 데이터 사이즈에 대해서 고려해야할 것인가?
3. 데이터가 만료되는 시간이 있는가?
4. 키의 이름을 어떻게 정할 것인가?
5. 비즈니스 로직에 대한 고려사항이 있는가?




## 설치 및 실행
### Redis 설치 (Ubuntu 예시)
```sh
sudo apt update
sudo apt install redis-server
```

### Redis 실행
```sh
redis-server
```

### Redis CLI 접속
```sh
redis-cli
```

## 기본 명령어
```sh
SET key value  # 키-값 저장
GET key        # 값 조회
DEL key        # 키 삭제
EXPIRE key 60  # 키의 만료 시간 설정 (초 단위)
INCR key       # 숫자 증가
LPUSH list val # 리스트에 값 추가
LRANGE list 0 -1 # 리스트 값 조회
```

## 다양한 자료 형태와 사용 사례
## 다양한 자료 형태와 사용 사례
### 문자열(String)
- **특징**: 단순한 키-값 저장이 필요할 때 유용
- **사용 사례**: 사용자 세션 저장, 설정 값 저장, 캐싱
- **예제**:
  ```sh
  SET username "user1"
  GET username
  ```

### 리스트(List)
- **특징**: 순서가 중요한 데이터 저장에 적합
- **사용 사례**: 로그 저장, 작업 큐, 채팅 메시지 관리
- **예제**:
  ```sh
  LPUSH messages "Hello"
  LPUSH messages "World"
  LRANGE messages 0 -1
  ```

### 셋(Set)
- **특징**: 중복을 자동으로 제거해야 할 때 유용
- **사용 사례**: 중복 없는 데이터 저장 (예: 태그, 추천 시스템)
- **예제**:
  ```sh
  SADD unique_users user1
  SADD unique_users user2
  SMEMBERS unique_users
  ```

### 정렬된 셋(Sorted Set)
- **특징**: 점수 기반으로 데이터를 정렬할 필요가 있을 때 적합
- **사용 사례**: 순위 시스템, 리더보드
- **예제**:
  ```sh
  ZADD leaderboard 100 user1
  ZADD leaderboard 200 user2
  ZRANGE leaderboard 0 -1 WITHSCORES
  ```

### 해시(Hash)
- **특징**: 구조화된 데이터를 효율적으로 저장하고 조회할 때 유용
- **사용 사례**: 사용자 프로필 저장, 설정 값 관리
- **예제**:
  ```sh
  HSET user:1001 name "Alice"
  HSET user:1001 age 30
  HGET user:1001 name
  ```

### 비트맵(Bitmap)
- **특징**: 특정 비트 위치를 이용하여 빠르고 효율적으로 상태를 저장 가능
- **사용 사례**: 사용자 로그인 추적, 플래그 저장
- **예제**:
  ```sh
  SETBIT user:logins 10 1
  GETBIT user:logins 10
  ```

### 하이퍼로그로그(HyperLogLog)
- **특징**: 대량의 데이터를 처리하면서 메모리 사용량을 최소화할 때 적합
- **사용 사례**: 대량의 고유 방문자 수 계산
- **예제**:
  ```sh
  PFADD visitors user1 user2 user3
  PFCOUNT visitors
  ```

## 사용 사례
- 웹 애플리케이션 캐싱 (예: 세션 저장, 페이지 캐싱)
- 실시간 데이터 분석
- 메시지 큐 및 Pub/Sub 시스템
- 리더보드 및 순위 시스템
- 분산 락 구현



# `Redis Key Naming Convention`

Redis를 효과적으로 사용하려면 일관된 키 명명 규칙을 따르는 것이 중요합니다. 이 문서는 Redis 키 네이밍 컨벤션을 정의하고, 최적의 활용 방법을 제공합니다.

## 1. 기본 원칙

1. **일관성 유지**: 모든 키는 일관된 형식을 따라야 합니다.
2. **가독성 향상**: 키를 보면 어떤 데이터를 의미하는지 쉽게 이해할 수 있어야 합니다.
3. **네임스페이스 활용**: 콜론(`:`)을 사용하여 계층 구조를 만들고 키 충돌을 방지합니다.
4. **최소한의 길이 유지**: 불필요하게 긴 키를 피하고, 필요한 정보를 간결하게 표현합니다.
5. **키 검색 최적화**: 패턴 검색 및 와일드카드 사용을 고려하여 명명합니다.

## 2. redis 키 네이밍 패턴

### 2.1 네임스페이스 사용

키 이름을 구조적으로 유지하기 위해 네임스페이스를 사용합니다. 일반적인 형식은 다음과 같습니다:

```
<시스템>:<도메인>:<서브도메인>:<구체적 키>
```

예시:

```
app:user:1001  # 특정 사용자의 정보
app:session:xyz123  # 특정 세션 정보
app:order:20240101:9999  # 특정 주문 정보
```

### 2.2 데이터 유형 명시

키 이름에 데이터 유형을 포함하면 데이터의 목적을 쉽게 파악할 수 있습니다.

```
<시스템>:<도메인>:<서브도메인>:<구체적 키>:<데이터 유형>
```

예시:

```
app:user:1001:string  # 단일 값
app:user:1001:hash  # 해시 값
app:user:1001:orders:list  # 리스트
```

### 2.3 시간 기반 키

시간이 중요한 데이터는 날짜 또는 타임스탬프를 포함할 수 있습니다.

```
log:2024:01:01:access  # 2024년 1월 1일 접속 로그
cache:product:1234:1706789123  # 특정 상품의 캐시 데이터 (Unix timestamp)
```

### 2.4 환경 구분

다양한 환경(개발, 스테이징, 프로덕션)에서 동일한 키를 사용할 경우, 환경을 구분하여 충돌을 방지합니다.

```
dev:app:user:1001  # 개발 환경
staging:app:user:1001  # 스테이징 환경
prod:app:user:1001  # 프로덕션 환경
```

## 3. 안티패턴 (피해야 할 사례)

1. **무작위 키 사용**
   - `user1001`, `order9999` (의미가 명확하지 않음)
   
2. **구분자 일관성 부족**
   - `app-user-1001`, `app.user.1001` (일관된 구분자 사용 필요)
   
3. **너무 긴 키 사용**
   - `application:module:submodule:feature:entity:identifier:property` (불필요하게 긴 키는 메모리 낭비)
   
4. **공통 접두어 생략**
   - `1001:name`, `1001:email` 대신 `user:1001:name`, `user:1001:email` 사용

## 4. 예제

```plaintext
app:user:1001:name = "Alice"
app:user:1001:email = "alice@example.com"
app:order:20240101:9999:total = 120.50
app:session:xyz123:expires = 1706789123
log:2024:02:01:access = "User 1001 accessed the system"
```

## 5. 결론

Redis 키 네이밍 규칙을 잘 정의하면 데이터 관리가 쉬워지고 성능 최적화가 가능합니다.
위 원칙을 따라 일관된 키 네이밍을 유지하는 것이 중요합니다.


# `Redis에서 직렬화와 역직렬화가 필요한 상황`

Redis는 기본적으로 문자열(String) 데이터를 저장하지만, 복잡한 데이터 구조(딕셔너리, 리스트, 객체 등)를 저장하거나 다양한 환경에서 데이터를 주고받을 때 **직렬화(Serialization)와 역직렬화(Deserialization)** 과정이 필요합니다.

## 1. 복잡한 데이터 구조 저장

Redis는 문자열을 기본 저장 형식으로 사용하므로, Python의 딕셔너리, 리스트, 객체 같은 자료형을 직접 저장할 수 없습니다. 따라서 직렬화를 사용해 문자열 또는 바이트 형식으로 변환해야 합니다.

### 해결 방법
- **JSON 직렬화**: `json.dumps()` / `json.loads()`
- **Pickle 직렬화 (Python 전용)**: `pickle.dumps()` / `pickle.loads()`
- **MessagePack, Protobuf (바이너리 포맷)** 사용

### 예제 (Python)
```python
import json
import redis

r = redis.Redis()

# 저장할 데이터
user_data = {"id": 1, "name": "Alice", "age": 25}

# JSON 직렬화 후 Redis에 저장
r.set("user:1", json.dumps(user_data))

# Redis에서 가져와 역직렬화
data = json.loads(r.get("user:1"))
print(data["name"])  # Alice
```

---

## 2. Redis를 캐시(Cache)로 사용할 때

데이터베이스 조회 결과를 Redis에 캐싱하려면 직렬화를 사용해야 합니다.

### 예제
```python
import json
import redis

r = redis.Redis()

# 데이터베이스 조회 결과 (예제 데이터)
user_info = {"id": 2, "name": "Bob", "email": "bob@example.com"}

# Redis에 JSON 직렬화하여 저장
r.setex("user:2", 3600, json.dumps(user_info))  # 1시간 캐시 유지

# Redis에서 가져와 역직렬화
cached_data = json.loads(r.get("user:2"))
print(cached_data)
```

---

## 3. 여러 프로그래밍 언어 간 데이터 공유

Redis를 Python, Java, Go 등 다양한 언어에서 사용할 경우, **JSON, MessagePack, Protobuf** 등의 공통 포맷을 사용해야 합니다.

### 예제: Python에서 JSON으로 저장 → Java에서 사용 가능
```python
import json
import redis

r = redis.Redis()

# Python에서 JSON 직렬화하여 저장
r.set("product:1", json.dumps({"name": "Laptop", "price": 1200}))
```
Java에서 JSON을 역직렬화하여 사용하면 문제없이 데이터를 읽을 수 있습니다.

## 4. Redis Pub/Sub 메시지 시스템에서 데이터 전송

Redis의 Pub/Sub 기능을 사용할 때, JSON이나 MessagePack으로 직렬화하여 메시지를 보내고 구독자는 역직렬화하여 사용합니다.

### 예제 (Python)
```python
import redis
import json

r = redis.Redis()

# 발행 (Publisher)
message = {"event": "order_created", "order_id": 123}
r.publish("orders", json.dumps(message))

# 구독 (Subscriber)
p = r.pubsub()
p.subscribe("orders")

for message in p.listen():
    if message["type"] == "message":
        data = json.loads(message["data"])
        print(f"새 주문 생성: {data['order_id']}")
```

## 5. 바이너리 데이터(이미지, 파일 등) 저장

Redis는 기본적으로 텍스트 기반이지만, 바이너리 데이터(이미지, 오디오, 동영상 등)도 저장할 수 있습니다. 보통 `Base64` 인코딩을 사용합니다.

### 예제 (Python)
```python
import redis
import base64

r = redis.Redis()

# 이미지 파일을 Base64로 인코딩하여 저장
with open("image.png", "rb") as f:
    encoded = base64.b64encode(f.read())

r.set("image:1", encoded)

# Redis에서 가져와 디코딩
decoded = base64.b64decode(r.get("image:1"))

# 파일로 저장
with open("output.png", "wb") as f:
    f.write(decoded)
```



## 📌 정리
| 상황 | 직렬화 방식 |
|------|----------------|
| 복잡한 데이터 저장 | JSON, Pickle, MessagePack |
| 캐시 저장 | JSON, Pickle |
| 여러 언어 간 데이터 공유 | JSON, Protobuf, MessagePack |
| Pub/Sub 메시지 전송 | JSON, MessagePack |
| 이미지/파일 저장 | Base64, 바이너리 데이터 |

Redis에서 직렬화와 역직렬화가 필요한 이유는 **데이터를 문자열 또는 바이트 형태로 저장해야 하기 때문**입니다. 상황에 맞게 JSON, Pickle, Protobuf 등을 활용하면 효과적으로 Redis를 사용할 수 있습니다! 🚀



# `Redis 모듈 가이드`

## 소개
Redis는 강력한 인메모리 데이터 구조 저장소로, 모듈을 통해 기능을 확장할 수 있습니다. 이 문서는 두 가지 주요 Redis 모듈인 `RediSearch`와 `RedisJSON`에 대해 설명합니다.

## RediSearch
### 개요
RediSearch는 Redis 모듈로 구축된 전문 검색 엔진으로, 고급 검색 기능, 인덱싱 및 구조화된 데이터와 비구조화된 데이터를 효율적으로 조회할 수 있도록 지원합니다.

* 비교
    * [Elasticsearch](https://velog.io/@emily2307/ElasticSearch%EB%9E%80) 

### 기능
- 고급 쿼리를 지원하는 전문 검색 (예: 퍼지 검색, 정확한 문구 검색)
- 효율적인 조회를 위한 보조 인덱싱 기능
- 자동완성과 동의어 지원
- JSON 및 해시 문서 인덱싱
- 집계 및 필터링 기능 제공

### 설치 방법
Docker를 사용하여 RediSearch 설치:
```sh
docker run -d --name redisearch -p 6379:6379 redislabs/redisearch
```
또는 `redis-cli`를 사용하여 로드:
```sh
MODULE LOAD /path/to/redisearch.so
```

### 사용법
#### 인덱스 생성
```sh
FT.CREATE myIndex ON HASH PREFIX 1 doc: SCHEMA title TEXT WEIGHT 5.0 description TEXT
```

#### 문서 추가
```sh
HSET doc:1 title "Redis Search" description "강력한 검색 엔진"
```

#### 문서 검색
```sh
FT.SEARCH myIndex "Redis"
```

## RedisJSON
### 개요
RedisJSON은 Redis에 네이티브 JSON 지원을 추가하는 고성능 JSON 문서 저장소입니다.

### 기능
- JSON 문서를 저장, 조회 및 조작 가능
- JSON 필드의 효율적인 인덱싱 및 쿼리 지원
- RediSearch와 완벽한 호환
- JSON 객체의 부분 업데이트 지원

### 설치 방법
Docker를 사용하여 RedisJSON 설치:
```sh
docker run -d --name redisjson -p 6379:6379 redislabs/rejson
```
또는 `redis-cli`를 사용하여 로드:
```sh
MODULE LOAD /path/to/rejson.so
```

### 사용법
#### JSON 문서 저장
```sh
JSON.SET user:1 . '{"name": "Alice", "age": 25, "city": "New York"}'
```

#### JSON 문서 조회
```sh
JSON.GET user:1
```

#### JSON 필드 업데이트
```sh
JSON.SET user:1 .age 26
```

#### JSON 필드 삭제
```sh
JSON.DEL user:1 .city
```

## RediSearch와 RedisJSON 결합
RediSearch와 RedisJSON을 결합하면 JSON 문서를 효율적으로 인덱싱하고 검색할 수 있습니다.

### JSON 문서에 대한 인덱스 생성
```sh
FT.CREATE myJsonIndex ON JSON PREFIX 1 user: SCHEMA $.name AS name TEXT $.age AS age NUMERIC
```

### JSON 문서 검색
```sh
FT.SEARCH myJsonIndex "@name:Alice"
```

## Redis 모듈 가이드 요약
RediSearch와 RedisJSON은 Redis의 인덱싱, 검색 및 문서 저장 기능을 확장합니다. 이러한 모듈을 활용하면 고성능 애플리케이션을 구축할 수 있으며, 강력한 쿼리 및 저장 기능을 사용할 수 있습니다.


# 참고자료
- [Redis : 개발자를 위한 Redis 완벽 가이드](https://www.udemy.com/course/redis-the-complete-developers-guide-korean/?couponCode=KEEPLEARNING)
- [RedisSearch vs Elasticsearch](https://2mukee.tistory.com/780)



