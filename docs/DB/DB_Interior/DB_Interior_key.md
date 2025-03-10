# 데이터베이스 키(Keys) 완벽 가이드

## 목차
- [1. 기본키(Primary Key)](#1-기본키primary-key)
- [2. 보조키(Secondary Key/Alternate Key)](#2-보조키secondary-key)
- [3. 키의 특성 비교](#3-키의-특성-비교)
- [4. 실제 사용 사례](#4-실제-사용-사례)
- [5. 모범 사례](#5-모범-사례)
- [6. 클러스터링](#6-클러스터링)

## 1. 기본키(Primary Key)

### 1.1 정의
기본키는 데이터베이스 테이블에서 각 레코드를 고유하게 식별하는 열 또는 열의 조합입니다.

### 1.2 특성
- **고유성**: 테이블 내에서 중복될 수 없음
- **Not Null**: NULL 값을 가질 수 없음
- **불변성**: 한번 설정된 값은 변경하지 않는 것이 좋음
- **최소성**: 식별에 필요한 최소한의 필드로 구성

### 1.3 종류
1. **자연키(Natural Key)**
    - 업무적으로 의미가 있는 데이터를 키로 사용
    - 예: 주민등록번호, 사원번호

2. **대리키(Surrogate Key)**
    - 임의로 생성된 식별자
    - 예: auto_increment ID, UUID

## 2. 보조키(Secondary Key)

### 2.1 정의
보조키는 테이블에서 레코드를 식별할 수 있는 후보키 중 기본키로 선택되지 않은 키입니다.

### 2.2 특성
- **고유성**: 중복되지 않는 값을 가짐
- **Null 허용**: 상황에 따라 NULL 값 허용 가능
- **검색 최적화**: 데이터 검색 시 인덱스로 활용

### 2.3 용도
- 대체 식별자로 활용
- 데이터 검색 성능 향상
- 데이터 무결성 보장

## 3. 키의 특성 비교

| 특성 | 기본키 | 보조키 |
|------|--------|--------|
| 유일성 | 필수 | 필수 |
| NULL 허용 | 불가 | 가능 |
| 테이블당 개수 | 1개 | 다수 가능 |
| 값 변경 | 권장하지 않음 | 가능 |
| 인덱스 생성 | 자동 | 선택적 |

## 4. 실제 사용 사례

### 4.1 회원 테이블 예시
```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,  -- 기본키
    email VARCHAR(255) UNIQUE,              -- 보조키
    phone VARCHAR(20) UNIQUE,               -- 보조키
    username VARCHAR(50) NOT NULL
);
```

### 4.2 주문 테이블 예시
```sql
CREATE TABLE orders (
    order_id UUID PRIMARY KEY,              -- 기본키
    order_number VARCHAR(50) UNIQUE,        -- 보조키
    user_id INT,
    order_date TIMESTAMP NOT NULL
);
```

## 5. 모범 사례

### 5.1 기본키 설계 원칙
- 업무적 의미가 없는 대리키 사용 권장
- 짧은 데이터 타입 선택
- 변경 가능성이 없는 값 선택

### 5.2 보조키 설계 원칙
- 자주 검색되는 필드에 적용
- 업무적으로 의미 있는 필드 선택
- 적절한 인덱스 전략 수립

### 5.3 성능 최적화
- 기본키는 클러스터형 인덱스로 구성
- 보조키는 적절한 인덱스 전략 적용
- 복합 인덱스 고려

## 주의사항
- 기본키와 보조키의 개수는 테이블의 크기와 사용 패턴을 고려하여 설정
- 과도한 보조키는 입력/수정/삭제 성능에 영향을 줄 수 있음
- 기본키는 업무적 변경에 영향을 받지 않는 값으로 설정


## 6. 클러스터링

### 6.1 클러스터드 인덱스
클러스터드 인덱스는 테이블의 물리적 데이터 저장 순서를 결정하는 인덱스입니다.

#### 특징
- 테이블당 하나만 존재 가능
- 기본키에 자동으로 생성됨
- 데이터 페이지가 인덱스 순서대로 정렬됨
- 디스크 상의 실제 데이터 순서를 결정

#### 장점
- 범위 검색 성능이 우수
- 인접한 데이터 접근이 빠름
- 정렬된 결과를 빠르게 얻을 수 있음

#### 단점
- 데이터 입력/수정 시 물리적 재정렬 필요
- 페이지 분할이 발생할 수 있음

### 6.2 논클러스터드 인덱스
보조키에 생성되는 일반적인 인덱스 유형입니다.

#### 특징
- 테이블당 여러 개 생성 가능
- 물리적 데이터 순서와 무관
- 포인터를 통해 실제 데이터에 접근

#### 장점
- 여러 개의 인덱스 생성 가능
- 데이터 입력/수정이 상대적으로 빠름
- 다양한 검색 조건 지원

#### 단점
- 추가적인 저장 공간 필요
- 클러스터드 인덱스에 비해 범위 검색이 느림

### 6.3 클러스터링 전략

#### 기본키 클러스터링
```sql
-- MySQL에서의 클러스터드 인덱스 (기본키)
CREATE TABLE users (
    user_id INT PRIMARY KEY,  -- 자동으로 클러스터드 인덱스 생성
    username VARCHAR(50),
    email VARCHAR(255)
);

-- 보조 인덱스 추가 (논클러스터드)
CREATE INDEX idx_email ON users(email);
```

#### 최적화 전략
1. **기본키 선택 시 고려사항**
    - 순차적으로 증가하는 값 선택
    - 데이터 크기가 작은 컬럼 선택
    - 업데이트가 적은 컬럼 선택

2. **보조키 인덱스 전략**
    - 자주 사용되는 검색 조건에 생성
    - 선택도(Selectivity)가 높은 컬럼에 생성
    - 복합 인덱스 활용

### 6.4 데이터베이스별 클러스터링 특징

#### MySQL (InnoDB)
- 기본키가 자동으로 클러스터드 인덱스가 됨
- 기본키가 없는 경우 첫 번째 UNIQUE NOT NULL 인덱스 사용
- 위 둘 다 없는 경우 숨겨진 클러스터드 인덱스 생성

#### SQL Server
- 명시적으로 클러스터드 인덱스 지정 가능
- 기본키는 기본적으로 클러스터드 인덱스로 생성
- CLUSTERED 키워드로 명시적 지정 가능

#### Oracle
- Index-Organized Table (IOT)로 클러스터링 구현
- 일반 테이블은 힙 구조로 저장
- 클러스터링 방식이 다른 DBMS와 다름

#### PostgreSQL

- 물리적 클러스터링을 위한 CLUSTER 명령어 제공
- 인덱스 기반 클러스터링 지원
- 자동 클러스터링 유지는 지원하지 않음
- 수동으로 CLUSTER 명령을 실행해야 데이터 재정렬됨


```sql
-- PostgreSQL에서 클러스터링 인덱스 생성 및 적용
CREATE INDEX idx_users_email ON users(email);
CLUSTER users USING idx_users_email;

-- 특정 테이블 재클러스터링
CLUSTER users;

-- 데이터베이스 전체 재클러스터링
CLUSTER;
```


#### PostgreSQL 클러스터링 특징
1. **일회성 작업**
    - CLUSTER 명령은 테이블을 일회성으로 재정렬
    - 이후 INSERT나 UPDATE는 클러스터링 순서를 유지하지 않음

2. **성능 최적화**
    - 범위 검색에 효과적
    - 자주 함께 접근되는 데이터를 물리적으로 가깝게 배치
    - 디스크 I/O 감소 효과

3. **운영 고려사항**
    - 클러스터링 작업 중 테이블 잠금 발생
    - 정기적인 재클러스터링 필요
    - 대용량 테이블의 경우 시간이 오래 걸릴 수 있음

### 6.5 성능 모니터링
```sql
-- MySQL에서 인덱스 사용 현황 확인
SHOW INDEX FROM users;

-- 실행 계획 확인
EXPLAIN SELECT * FROM users WHERE email = 'example@email.com';
```

### 6.6 클러스터링 주의사항
- 클러스터드 인덱스 열의 크기는 작게 유지
- 랜덤 값을 클러스터드 인덱스로 사용 시 성능 저하 가능
- 과도한 논클러스터드 인덱스는 성능 저하 유발
- 데이터 변경이 빈번한 열은 클러스터드 인덱스로 부적합



