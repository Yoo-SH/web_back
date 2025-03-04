# DB Index Scan vs Index Only Scan

## 개요
데이터베이스에서 쿼리 최적화를 위해 인덱스를 활용하는 방식에는 여러 가지가 있습니다. 그중에서 **Index Scan**과 **Index Only Scan**은 자주 등장하는 개념입니다. 이 문서는 두 개념의 차이점, 동작 방식, 장단점을 설명합니다.

---

## 1. Index Scan
### 정의
`Index Scan`은 인덱스를 사용하여 데이터를 검색하지만, 실제 테이블 페이지도 접근해야 하는 방식입니다. 즉, 인덱스를 탐색한 후 필요한 데이터를 가져오기 위해 테이블을 추가로 조회해야 합니다.

### 동작 방식
1. 인덱스를 검색하여 원하는 조건을 만족하는 레코드의 위치(ROWID 또는 TID)를 찾음
2. 해당 위치를 기반으로 테이블에서 실제 데이터를 가져옴

### 장점
- 테이블의 전체 검색(Sequential Scan)보다는 빠를 수 있음
- 적절한 조건이 있는 경우 테이블 데이터를 랜덤 액세스하여 필요한 데이터만 가져올 수 있음

### 단점
- 인덱스를 조회한 후에도 테이블을 접근해야 하므로 추가적인 I/O 비용이 발생
- 많은 행을 조회해야 할 경우 비효율적일 수 있음

### 예제 (PostgreSQL 기준)
```sql
EXPLAIN ANALYZE SELECT * FROM users WHERE name = 'Alice';
```
위 쿼리에서 `users` 테이블의 `name` 컬럼에 인덱스가 존재한다고 가정하면, `Index Scan`이 수행될 가능성이 있습니다.

---

## 2. Index Only Scan
### 정의
`Index Only Scan`은 인덱스만을 이용하여 쿼리를 처리하고 테이블 데이터를 조회하지 않는 방식입니다. 즉, 인덱스에 저장된 데이터만으로 결과를 반환할 수 있을 때 발생합니다.

### 동작 방식
1. 인덱스를 검색하여 원하는 데이터를 찾음
2. 테이블을 조회하지 않고 인덱스에서 직접 결과를 반환

### 조건
- 조회하려는 모든 컬럼이 인덱스에 포함되어 있어야 함 (Covering Index 필요)
- PostgreSQL의 경우, Visibility Map을 사용하여 테이블을 조회할 필요가 없는 경우에만 `Index Only Scan`이 가능함

### 장점
- 테이블에 접근하지 않으므로 `Index Scan`보다 빠름
- 불필요한 I/O를 줄여 성능을 향상시킴

### 단점
- 인덱스에 없는 컬럼을 조회할 경우 `Index Scan`으로 대체됨
- 인덱스 크기가 커질 수 있음 (모든 필요한 컬럼을 포함해야 하므로)

### 예제 (PostgreSQL 기준)
```sql
EXPLAIN ANALYZE SELECT id, name FROM users WHERE name = 'Alice';
```
`users` 테이블의 `(name, id)` 컬럼에 대한 인덱스가 있다면, `Index Only Scan`이 수행될 수 있습니다.

---

## 3. Index Scan vs Index Only Scan 비교

| 비교 항목        | Index Scan | Index Only Scan |
|----------------|------------|----------------|
| 테이블 조회 여부 | O (필요)   | X (불필요)     |
| 성능           | 상대적으로 느림 | 더 빠름 (테이블 접근 없음) |
| 활용 조건      | 인덱스를 사용하지만, 추가적인 컬럼이 필요할 때 | 인덱스에 필요한 모든 데이터가 있을 때 |
| I/O 비용       | 비교적 높음  | 낮음 |

---

## 4. 결론
- **Index Scan**은 인덱스를 사용하지만, 테이블을 추가로 조회해야 하는 방식
- **Index Only Scan**은 인덱스만으로 데이터를 가져올 수 있는 방식으로, 더 효율적
- `Index Only Scan`을 활용하려면 **Covering Index**를 적절히 설계하는 것이 중요

성능을 최적화하려면, 실행 계획(`EXPLAIN ANALYZE`)을 분석하고 **인덱스를 적절히 구성하는 것**이 핵심입니다.

---

## 5. 참고 문서
- [PostgreSQL 공식 문서 - Index Scan](https://www.postgresql.org/docs/current/indexes-index-only-scans.html)
- [MySQL 공식 문서 - Indexes](https://dev.mysql.com/doc/refman/8.0/en/mysql-indexes.html)
