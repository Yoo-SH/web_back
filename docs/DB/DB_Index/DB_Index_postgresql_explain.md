# PostgreSQL EXPLAIN 가이드

## 목차
- [개요](#개요)
- [기본 문법](#기본-문법)
- [실행 계획 읽는 방법](#실행-계획-읽는-방법)
- [주요 노드 타입](#주요-노드-타입)
- [비용 분석](#비용-분석)
- [성능 최적화 팁](#성능-최적화-팁)
- [실전 예제](#실전-예제)

## 개요
PostgreSQL의 EXPLAIN 명령어는 쿼리 실행 계획을 분석하고 성능을 최적화하는데 사용되는 강력한 도구입니다. 이 가이드에서는 EXPLAIN의 사용법과 실행 계획 분석 방법을 상세히 다룹니다.

## 기본 문법

### 기본 EXPLAIN
```sql
EXPLAIN SELECT * FROM users;
```

### 실제 실행 결과 확인
```sql
EXPLAIN ANALYZE SELECT * FROM users;
```

### 추가 옵션
```sql
EXPLAIN (
    ANALYZE true,    -- 실제 실행 결과 포함
    VERBOSE true,    -- 상세 정보 표시
    COSTS true,      -- 비용 정보 표시
    BUFFERS true,    -- 버퍼 사용량 표시
    FORMAT JSON      -- 출력 형식 지정 (TEXT, XML, JSON, YAML)
) SELECT * FROM users;
```

## 실행 계획 읽는 방법

### 기본 구조
- 실행 계획은 트리 구조로 표시됨
- 들여쓰기로 계층 구조 표현
- 안쪽에서 바깥쪽으로 실행됨
- 각 노드는 실행 단계를 나타냄

### 주요 정보
1. **실행 비용 (cost)**
    - startup cost: 첫 번째 행을 반환하기까지의 비용
    - total cost: 모든 행을 반환하는데 필요한 총 비용

2. **행 수 예측**
    - rows: 예상되는 결과 행 수
    - actual rows: 실제 반환된 행 수 (ANALYZE 사용 시)

3. **실행 시간**
    - actual time: 실제 실행 시간 (ANALYZE 사용 시)
    - Planning time: 쿼리 계획 수립 시간
    - Execution time: 실제 실행 시간

## 주요 노드 타입

### Scan 노드
1. **Seq Scan**
    - 테이블 전체를 순차적으로 스캔
   ```sql
   Seq Scan on users  (cost=0.00..433.00 rows=10000 width=244)
   ```

2. **Index Scan**
    - 인덱스를 사용하여 특정 행을 검색
   ```sql
   Index Scan using users_pkey on users  (cost=0.42..8.44 rows=1 width=244)
   ```

3. **Bitmap Scan**
    - 인덱스를 통해 비트맵을 생성하고 이를 기반으로 테이블 접근
   ```sql
   Bitmap Heap Scan on users  (cost=4.29..13.39 rows=5 width=244)
   ```

### Join 노드
1. **Nested Loop**
    - 중첩 루프 방식의 조인
   ```sql
   Nested Loop  (cost=0.00..16.97 rows=10 width=488)
   ```

2. **Hash Join**
    - 해시 테이블을 사용한 조인
   ```sql
   Hash Join  (cost=10.00..20.00 rows=10 width=488)
   ```

3. **Merge Join**
    - 정렬된 데이터를 병합하는 방식의 조인
   ```sql
   Merge Join  (cost=0.00..16.97 rows=10 width=488)
   ```

## 비용 분석

### 비용 구성 요소
1. **시퀀셜 페이지 비용**: seq_page_cost (기본값: 1.0)
2. **랜덤 페이지 비용**: random_page_cost (기본값: 4.0)
3. **CPU 튜플 비용**: cpu_tuple_cost (기본값: 0.01)
4. **CPU 연산자 비용**: cpu_operator_cost (기본값: 0.0025)

### 비용 계산 예시
```sql
Seq Scan on users  (cost=0.00..433.00 rows=10000 width=244)
                          └─ 총 비용: 433.00
                          └─ 시작 비용: 0.00
```

## 성능 최적화 팁

### 실행 계획 분석 시 주의점
1. 높은 비용의 노드 식별
2. 예상 행 수와 실제 행 수의 차이 확인
3. 테이블 스캔 방식 검토
4. 조인 방식의 적절성 평가

### 성능 개선 방법
1. **인덱스 활용**
    - 적절한 인덱스 생성
    - 복합 인덱스 고려
    - 인덱스 사용 여부 확인

2. **쿼리 최적화**
    - WHERE 절 조건 순서 조정
    - 서브쿼리 최적화
    - 조인 순서 검토

3. **테이블 최적화**
    - 테이블 통계 정보 업데이트
   ```sql
   ANALYZE table_name;
   ```
    - 적절한 파티셔닝 적용

## 실전 예제

### 1. 단순 쿼리 분석
```sql
EXPLAIN ANALYZE
SELECT * FROM users WHERE age > 30;

-- 실행 계획
Seq Scan on users  (cost=0.00..433.00 rows=1000 width=244)
  Filter: (age > 30)
```

### 2. 조인 쿼리 분석
```sql
EXPLAIN ANALYZE
SELECT u.name, o.order_date
FROM users u
JOIN orders o ON u.id = o.user_id
WHERE o.total > 1000;

-- 실행 계획
Hash Join  (cost=10.00..20.00 rows=10 width=488)
  Hash Cond: (o.user_id = u.id)
  ->  Seq Scan on orders o  (cost=0.00..11.50 rows=100 width=244)
        Filter: (total > 1000)
  ->  Hash  (cost=6.50..6.50 rows=100 width=244)
        ->  Seq Scan on users u
```

### 3. 성능 개선 예제
```sql
-- 인덱스 생성 전
EXPLAIN ANALYZE
SELECT * FROM users WHERE email = 'test@example.com';

-- 인덱스 생성
CREATE INDEX idx_users_email ON users(email);

-- 인덱스 생성 후
EXPLAIN ANALYZE
SELECT * FROM users WHERE email = 'test@example.com';
```

## 참고 사항
- EXPLAIN 결과는 예상치이며, 실제 실행 결과와 다를 수 있습니다.
- 데이터베이스의 통계 정보가 정확해야 신뢰성 있는 실행 계획이 생성됩니다.
- 주기적인 ANALYZE 명령어 실행으로 통계 정보를 최신화하는 것이 중요합니다.