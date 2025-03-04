# 데이터베이스 샤딩 구현 가이드

## 목차
1. [개요](#개요)
2. [샤딩이란?](#샤딩이란)
3. [샤딩이 필요한 경우](#샤딩이-필요한-경우)
4. [샤딩 전략](#샤딩-전략)
5. [샤딩 구현 단계](#샤딩-구현-단계)
6. [샤딩 키 선택 방법](#샤딩-키-선택-방법)
7. [샤딩 아키텍처 패턴](#샤딩-아키텍처-패턴)
8. [주요 데이터베이스별 샤딩 구현](#주요-데이터베이스별-샤딩-구현)
9. [샤딩 구현 시 고려사항](#샤딩-구현-시-고려사항)
10. [모니터링 및 유지보수](#모니터링-및-유지보수)
11. [결론](#결론)

## 개요
이 가이드는 데이터베이스 샤딩(Database Sharding)을 구현하기 위한 단계별 지침을 제공합니다. 데이터베이스 성능과 확장성을 향상시키기 위한 수평적 파티셔닝 기법인 샤딩의 개념, 전략, 구현 방법 및 주의사항을 다룹니다.

## 샤딩이란?
샤딩은 대규모 데이터베이스를 더 작고 관리하기 쉬운 부분(샤드)으로 분할하는 수평적 파티셔닝 방법입니다. 각 샤드는 동일한 스키마를 가지지만 서로 다른 데이터 세트를 포함하며, 일반적으로 서로 다른 서버에 위치합니다.
샤딩은 가장 마지막에 고려해야할 DB 엔지니어링 요소입니다. 왜냐하면 샤딩 이전에 해결할 수 있는 여러 엔지니어링 기법들이 많고 샤딩은 여러 복잡성을 초래하기 때문입니다.

![Image](https://github.com/user-attachments/assets/bcd6dfb1-cac9-4574-ab35-39f8e169feb3)

### 샤딩의 장점
- **성능 향상**: 쿼리 부하가 여러 서버로 분산됨
- **확장성 개선**: 데이터베이스 용량을 쉽게 확장 가능
- **가용성 증가**: 한 샤드의 장애가 전체 시스템에 영향을 미치지 않음

### 샤딩의 단점
- **복잡성 증가**: 구현 및 관리가 더 복잡함
- **조인 연산 어려움**: 여러 샤드에 걸친 조인 연산이 어려움
- **트랜잭션 관리**: 여러 샤드에 걸친 트랜잭션 처리가 복잡함

## 샤딩이 필요한 경우
다음 상황에서 데이터베이스 샤딩을 고려해볼 수 있습니다:

- 데이터베이스 크기가 단일 서버 용량에 근접하는 경우
- 쿼리 처리량이 단일 서버의 처리 능력을 초과하는 경우
- 지리적으로 분산된 사용자를 지원해야 하는 경우
- 고가용성 및 내결함성이 중요한 경우

## 샤딩 전략

### 1. 키 기반 샤딩 (Hash Sharding)
특정 컬럼 값(샤딩 키)에 해시 함수를 적용하여 데이터를 샤드에 분배합니다.

```
샤드 번호 = 해시(샤딩 키) % 샤드 수
```

**장점**:
- 데이터가 균등하게 분배됨
- 구현이 비교적 간단함

**단점**:
- 샤드 수 변경 시 재샤딩 필요
- 특정 샤드만 타겟팅하는 범위 쿼리가 어려움

### 2. 범위 기반 샤딩 (Range Sharding)
특정 컬럼 값의 범위에 따라 데이터를 샤드에 분배합니다.

예: 사용자 ID 1-1000000은 샤드1, 1000001-2000000은 샤드2...

**장점**:
- 범위 쿼리 성능이 좋음
- 데이터 지역성(locality) 활용 가능

**단점**:
- 데이터 분포가 불균등할 수 있음
- 핫스팟(hot spot) 발생 가능성

### 3. 디렉토리 기반 샤딩 (Directory Sharding)
샤딩 키와 샤드 위치를 매핑하는 룩업 테이블(또는 디렉토리 서비스)을 사용합니다.

**장점**:
- 유연한 샤딩 스키마 적용 가능
- 샤드 수 변경 시 일부 데이터만 이동 가능

**단점**:
- 룩업 테이블이 단일 장애점이 될 수 있음
- 추가적인 룩업 오버헤드 발생

### 4. 지리적 샤딩 (Geo Sharding)
사용자의 지리적 위치에 따라 데이터를 샤드에 분배합니다.

**장점**:
- 지리적으로 가까운 사용자에게 더 나은 성능 제공
- 데이터 규정 준수(예: GDPR) 용이

**단점**:
- 사용자 이동 시 데이터 마이그레이션 필요
- 글로벌 쿼리 성능이 저하될 수 있음

## 샤딩 구현 단계

### 1. 요구사항 분석
- 현재 및 예상 데이터 양 파악
- 쿼리 패턴 및 성능 요구사항 분석
- 확장 목표 설정

### 2. 샤딩 전략 선택
- 위에서 설명한 전략 중 적합한 것 선택
- 데이터 특성과 쿼리 패턴에 기반하여 결정

### 3. 샤딩 키 선택
- 데이터 분산이 균등하게 이루어질 수 있는 컬럼 선택
- 자주 쿼리되는 컬럼이 이상적
- 시간에 따라 변하지 않는 값이 좋음

### 4. 샤딩 아키텍처 설계
- 샤드 수 결정
- 샤드 관리 메커니즘 설계
- 쿼리 라우팅 로직 개발

### 5. 데이터 마이그레이션 계획
- 다운타임 최소화 전략 수립
- 점진적 마이그레이션 계획
- 롤백 계획 준비

### 6. 구현 및 테스트
- 샤딩 로직 구현
- 테스트 환경에서 검증
- 성능 및 확장성 벤치마킹

### 7. 모니터링 및 최적화
- 샤드 부하 모니터링
- 불균형 발생 시 재조정
- 성능 지속적 최적화

## 샤딩 키 선택 방법
좋은 샤딩 키는 다음 특성을 가져야 합니다:

1. **높은 카디널리티**: 데이터를 고르게 분산시킬 수 있도록 충분히 많은 고유 값
2. **불변성**: 시간이 지나도 변하지 않는 값
3. **단순성**: 계산이 간단하고 직관적
4. **쿼리 패턴 일치**: 자주 사용되는 쿼리에서 사용되는 컬럼

### 일반적인 샤딩 키 예시
- 사용자 ID
- 계정 ID
- 지리적 위치
- 시간 기반 ID (타임스탬프)
- 해시 값

## 샤딩 아키텍처 패턴

### 1. 클라이언트 사이드 샤딩
애플리케이션이 직접 적절한 샤드로 쿼리를 라우팅합니다.

```java
// 예시 코드 (Java)
public class ShardingRouter {
    private static final int SHARD_COUNT = 4;
    
    public static Connection getConnectionForUser(int userId) {
        int shardId = userId % SHARD_COUNT;
        return getConnectionForShard(shardId);
    }
    
    private static Connection getConnectionForShard(int shardId) {
        // 샤드별 연결 반환 로직
        String jdbcUrl = "jdbc:mysql://shard" + shardId + ".example.com:3306/mydb";
        return DriverManager.getConnection(jdbcUrl, "username", "password");
    }
}
```

### 2. 프록시 기반 샤딩
프록시 서버가 쿼리를 분석하고 적절한 샤드로 라우팅합니다.

![프록시 기반 샤딩](https://via.placeholder.com/600x300?text=Proxy+Based+Sharding)

### 3. 샤딩 미들웨어
특수 미들웨어가 샤딩 로직을 처리합니다. (예: Vitess for MySQL, Citus for PostgreSQL)

## 주요 데이터베이스별 샤딩 구현

### MySQL 샤딩

#### 1. 애플리케이션 레벨 샤딩
```java
// Java 예시
public class MySQLShardingExample {
    private static final String[] SHARD_JDBC_URLS = {
        "jdbc:mysql://shard0.example.com:3306/mydb",
        "jdbc:mysql://shard1.example.com:3306/mydb",
        "jdbc:mysql://shard2.example.com:3306/mydb",
        "jdbc:mysql://shard3.example.com:3306/mydb"
    };
    
    public Connection getShardConnection(int userId) {
        int shardIndex = userId % SHARD_JDBC_URLS.length;
        return DriverManager.getConnection(
            SHARD_JDBC_URLS[shardIndex], "username", "password");
    }
    
    public void insertUser(User user) {
        Connection conn = getShardConnection(user.getId());
        // 데이터 삽입 로직
    }
}
```

#### 2. MySQL Cluster
MySQL 내장 클러스터링 솔루션 사용

#### 3. Vitess 활용
Vitess는 MySQL용 데이터베이스 클러스터링 시스템으로, 효율적인 샤딩을 지원합니다.

```yaml
# Vitess 샤딩 구성 예시
keyspaces:
  - name: user_keyspace
    sharded: true
    vindexes:
      - name: hash
        type: hash
    tables:
      - name: users
        column_vindexes:
          - column: user_id
            name: hash
```

### PostgreSQL 샤딩

#### 1. 외부 테이블 사용
```sql
-- PostgreSQL 외부 테이블 샤딩 예시
CREATE SERVER shard1 FOREIGN DATA WRAPPER postgres_fdw
OPTIONS (host 'shard1.example.com', port '5432', dbname 'mydb');

CREATE USER MAPPING FOR current_user SERVER shard1
OPTIONS (user 'postgres', password 'password');

CREATE FOREIGN TABLE users_shard1 (
    id INT,
    name VARCHAR(100),
    email VARCHAR(100)
)
SERVER shard1
OPTIONS (schema_name 'public', table_name 'users');
```

#### 2. Citus 확장 사용
Citus는 PostgreSQL 확장으로, 분산 테이블을 쉽게 생성할 수 있습니다.

```sql
-- Citus 확장 활성화
CREATE EXTENSION citus;

-- 테이블 생성
CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);

-- 분산 테이블로 변환
SELECT create_distributed_table('users', 'id');
```

### MongoDB 샤딩

MongoDB는 기본적으로 샤딩을 지원합니다.

```js
// mongos에 연결 후 샤딩 구성
sh.enableSharding("mydb")

// 샤딩 키를 사용하여 컬렉션 샤딩
sh.shardCollection("mydb.users", { "user_id": 1 })
```

### Redis 샤딩

#### 1. Redis Cluster 사용
```bash
# Redis 클러스터 설정 예시
redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 \
  127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 \
  --cluster-replicas 1
```

#### 2. 클라이언트 측 샤딩 구현
```java
// Java 예시 (Jedis 클라이언트 사용)
public class RedisShardingExample {
    private ShardedJedis shardedJedis;
    
    public RedisShardingExample() {
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("redis1.example.com", 6379));
        shards.add(new JedisShardInfo("redis2.example.com", 6379));
        shards.add(new JedisShardInfo("redis3.example.com", 6379));
        shardedJedis = new ShardedJedis(shards);
    }
    
    public void set(String key, String value) {
        shardedJedis.set(key, value);
    }
    
    public String get(String key) {
        return shardedJedis.get(key);
    }
}
```

## 샤딩 구현 시 고려사항

### 1. 트랜잭션 관리
여러 샤드에 걸친 트랜잭션은 어렵습니다. 다음 방법을 고려하세요:

- **샤딩 키 설계 최적화**: 관련 데이터가 같은 샤드에 있도록 설계
- **보상 트랜잭션 구현**: 롤백을 위한 보상 메커니즘 준비
- **분산 트랜잭션 매니저**: 2단계 커밋 프로토콜 활용

### 2. 조인 연산
샤드 간 조인은 성능이 저하됩니다. 다음 접근법을 고려하세요:

- **데이터 비정규화**: 필요한 데이터를 한 샤드에 복제
- **애플리케이션 레벨 조인**: 애플리케이션에서 조인 수행
- **글로벌 테이블 활용**: 작고 자주 참조되는 테이블은 모든 샤드에 복제

### 3. 샤드 재조정
데이터 불균형이 발생하면 재조정이 필요합니다:

- **점진적 재조정**: 서비스 중단 없이 점진적으로 데이터 이동
- **일관성 유지**: 재조정 중 데이터 일관성 보장
- **이중 쓰기**: 마이그레이션 중 새 샤드와 기존 샤드에 모두 쓰기

### 4. 데이터 일관성
샤딩 환경에서는 일관성 관리가 복잡합니다:

- **CAP 이론 이해**: 일관성, 가용성, 분할 내성 중 우선순위 결정
- **최종 일관성 활용**: 일부 상황에서는 최종 일관성이 적합할 수 있음
- **버전 관리**: 데이터 버전 관리로 충돌 해결

### 5. 샤드 장애 대응
샤드 장애 시 대응 방안:

- **복제본 활용**: 각 샤드에 복제본 구성
- **자동 장애 조치**: 장애 발생 시 자동으로 복제본으로 전환
- **정기적 백업**: 모든 샤드 정기 백업

## 모니터링 및 유지보수

### 1. 모니터링 지표
샤딩 환경에서 모니터링해야 할 주요 지표:

- **샤드별 데이터 분포**: 데이터가 균등하게 분산되는지 확인
- **쿼리 처리량 및 지연시간**: 샤드별 성능 모니터링
- **리소스 사용률**: CPU, 메모리, 디스크, 네트워크 사용률
- **복제 지연**: 복제본이 있는 경우 복제 지연 모니터링

### 2. 모니터링 도구
샤딩 환경에 적합한 모니터링 도구:

- **Prometheus + Grafana**: 메트릭 수집 및 시각화
- **데이터베이스별 모니터링 도구**: MySQL Enterprise Monitor, MongoDB Atlas 등
- **커스텀 모니터링 스크립트**: 샤딩 특화 지표를 위한 스크립트

### 3. 정기적 유지보수 작업
샤딩 환경의 건강을 유지하기 위한 작업:

- **정기적 리밸런싱**: 데이터 불균형 방지를 위한 재조정
- **인덱스 최적화**: 샤드별 인덱스 분석 및 최적화
- **스키마 업데이트 계획**: 스키마 변경 시 모든 샤드에 적용 계획

## 결론
데이터베이스 샤딩은 대규모 애플리케이션의 확장성과 성능을 향상시키는 강력한 기술입니다. 하지만 복잡성이 증가하고 여러 도전 과제가 있으므로 신중한 계획과 설계가 필요합니다. 이 가이드에서 제시한 전략과 방법을 활용하여 효과적인 샤딩 구현을 위한 기반을 마련하시기 바랍니다.

## 참고 자료
- [MongoDB 샤딩 문서](https://docs.mongodb.com/manual/sharding/)
- [MySQL 샤딩 가이드](https://dev.mysql.com/doc/)
- [PostgreSQL 파티셔닝](https://www.postgresql.org/docs/current/ddl-partitioning.html)
- [Redis 클러스터 문서](https://redis.io/topics/cluster-tutorial)
- [Vitess 문서](https://vitess.io/docs/overview/)
- [Citus 데이터 문서](https://docs.citusdata.com/)