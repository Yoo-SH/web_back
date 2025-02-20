# Database Index Guide

## 목차
- [개요](#개요)
- [인덱스의 기본 개념](#인덱스의-기본-개념)
- [인덱스의 종류](#인덱스의-종류)
- [인덱스 생성 및 관리](#인덱스-생성-및-관리)
- [인덱스 성능 상세 분석](#인덱스-성능-상세-분석)
- [인덱스 설계 전략](#인덱스-설계-전략)
- [성능 고려사항](#성능-고려사항)
- [모범 사례](#모범-사례)
- [주의사항](#주의사항)

## 개요
데이터베이스 인덱스는 데이터베이스 테이블의 검색 속도를 향상시키기 위한 자료구조입니다. 책의 색인과 유사하게, 데이터베이스 인덱스는 특정 데이터의 위치를 빠르게 찾을 수 있도록 도와줍니다.

## 인덱스의 기본 개념

### 작동 원리
- B-Tree 구조를 기반으로 데이터를 정렬된 상태로 유지
- 원본 테이블의 데이터를 참조하는 포인터를 포함
- 검색 조건에 해당하는 데이터를 빠르게 찾을 수 있도록 지원

### 장점
- 검색 속도 향상
- ORDER BY 절의 성능 개선
- 중복 값 제거 효율화

### 단점
- 추가적인 저장 공간 필요
- INSERT, UPDATE, DELETE 작업 시 성능 저하
- 인덱스 유지보수에 따른 오버헤드

## 인덱스의 종류

### 클러스터형 인덱스 (Clustered Index)
- 테이블당 하나만 생성 가능
- 물리적으로 데이터를 정렬
- 보통 프라이머리 키에 자동 생성

### 비클러스터형 인덱스 (Non-Clustered Index)
- 여러 개 생성 가능
- 별도의 인덱스 페이지에 저장
- 원본 데이터의 포인터를 포함

### 특수 인덱스
- 복합 인덱스 (Composite Index)
- 부분 인덱스 (Partial Index)
- 커버링 인덱스 (Covering Index)
- 비트맵 인덱스 (Bitmap Index)

## 인덱스 생성 및 관리

### 인덱스 생성 구문
```sql
CREATE INDEX index_name
ON table_name (column1, column2, ...);
```

### 인덱스 삭제 구문
```sql
DROP INDEX index_name;
```

### 인덱스 재구성
```sql
ALTER INDEX index_name
ON table_name REBUILD;
```


## 인덱스 성능 상세 분석

### 쿼리 패턴별 성능 특성

#### 1. Primary Key 검색 (where id = ?)
- **최고 성능**
- 클러스터형 인덱스 직접 접근
- 힙(데이터 페이지) 접근 불필요
- 단일 레코드 반환으로 I/O 최소화
- 예시:
```sql
SELECT * FROM users WHERE id = 1;
```

#### 2. 일반 컬럼 정확일치 검색 (where name = ?)
- **중간 성능**
- 비클러스터형 인덱스 사용
- 인덱스 검색 후 힙 접근 필요
- 레코드 룩업 발생으로 추가 I/O 필요
- 예시:
```sql
SELECT * FROM users WHERE name = 'John';
```

#### 3. Like 검색 (where name like '%keyword%')
- **최저 성능**
- 패턴 매칭으로 인한 전체 테이블 스캔
- 인덱스 활용 불가
- CPU 집약적 연산
- 예시:
```sql
SELECT * FROM users WHERE name LIKE '%John%';
```

### 성능 최적화 전략

#### Like 검색 최적화
1. 전방 일치 활용
```sql
-- 좋은 예시 (인덱스 활용 가능)
WHERE name LIKE 'John%'
-- 나쁜 예시 (인덱스 활용 불가)
WHERE name LIKE '%John%'
```

2. 전문 검색 인덱스 사용
```sql
-- PostgreSQL의 경우
CREATE INDEX idx_users_name_gin ON users USING gin(to_tsvector('english', name));
```

#### PostgreSQL 병렬 처리
1. 병렬 스캔 설정
```sql
SET max_parallel_workers_per_gather = 4;
```

2. 병렬 처리 가능한 쿼리 작성
```sql
SELECT * FROM large_table 
WHERE some_column LIKE '%pattern%'
PARALLEL WORKER_NUMBER 4;
```

### 인덱스 성능 모니터링

#### 성능 지표 확인
```sql
-- PostgreSQL 인덱스 사용량 확인
SELECT schemaname, tablename, indexname, idx_scan, idx_tup_read, idx_tup_fetch
FROM pg_stat_user_indexes;
```

#### 실행 계획 분석
```sql
EXPLAIN ANALYZE
SELECT * FROM users WHERE name = 'John';
```

### 성능 최적화 모범 사례

1. **복합 인덱스 최적화**
    - 선행 컬럼 활용 보장
    - 선택도 높은 컬럼 우선 배치
   ```sql
   CREATE INDEX idx_users_name_email ON users(name, email);
   ```

2. **부분 인덱스 활용**
    - 조건부 인덱싱으로 크기 최적화
   ```sql
   CREATE INDEX idx_users_active ON users(last_login) 
   WHERE active = true;
   ```

3. **인덱스 커버링 활용**
    - 힙 접근 최소화
   ```sql
   CREATE INDEX idx_users_cover ON users(name, email, phone)
   INCLUDE (created_at);
   ```

### 데이터베이스별 특화 기능

#### PostgreSQL
1. **병렬 스캔 최적화**
    - 테이블 크기가 min_parallel_table_scan_size 이상
    - max_parallel_workers_per_gather 설정 조정
    - parallel_setup_cost 및 parallel_tuple_cost 파라미터 튜닝

2. **인덱스 타입 선택**
    - B-tree: 일반적인 용도
    - GiST: 지리 데이터, 전문 검색
    - GIN: 배열 데이터, 전문 검색
    - BRIN: 대용량 순차 데이터

3. **VACUUM 관리**
    - 정기적인 VACUUM ANALYZE 수행
    - autovacuum 파라미터 최적화

## 인덱스 설계 전략

### 인덱스 생성이 필요한 경우
- WHERE 절에서 자주 사용되는 컬럼
- JOIN 조건으로 사용되는 컬럼
- ORDER BY, GROUP BY에 사용되는 컬럼
- UNIQUE 제약조건이 있는 컬럼

### 인덱스 생성을 피해야 하는 경우
- 데이터가 적은 테이블
- 갱신이 빈번한 컬럼
- 선택도(Selectivity)가 낮은 컬럼

## 성능 고려사항

### 인덱스 성능에 영향을 미치는 요소
- 테이블 크기
- 데이터 분포도
- 인덱스 키의 선택도
- 동시성 처리량
- 하드웨어 리소스

### 성능 모니터링
- 실행 계획 분석
- 인덱스 사용률 모니터링
- 인덱스 단편화 검사
- 캐시 히트율 확인

## 모범 사례

### 인덱스 설계
- 선택도가 높은 컬럼 우선 선정
- 복합 인덱스의 컬럼 순서 최적화
- 적절한 인덱스 개수 유지
- 정기적인 인덱스 유지보수

### 쿼리 최적화
- 인덱스를 활용하는 쿼리 작성
- 불필요한 인덱스 스캔 방지
- 인덱스 힌트 적절히 사용
- 실행 계획 주기적 검토

## 주의사항

### 일반적인 주의사항
- 과도한 인덱스 생성 지양
- 미사용 인덱스 정기적 제거
- 대량 데이터 작업 시 인덱스 영향 고려
- 인덱스 단편화 관리

### 성능 관련 주의사항
- 인덱스 스캔 vs 테이블 스캔 비용 분석
- 인덱스 업데이트 오버헤드 고려
- 메모리 사용량 모니터링
- 동시성 처리 영향 평가