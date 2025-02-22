# DB 수직 파티셔닝 구현 가이드

## 목차
- [개요](#개요)
- [수직 파티셔닝이란?](#수직-파티셔닝이란)
- [구현 예제](#구현-예제)
- [성능 비교](#성능-비교)
- [주의사항](#주의사항)

## 개요
이 문서는 데이터베이스 수직 파티셔닝의 실제 구현 예제를 다룹니다. 대용량 데이터를 효율적으로 관리하기 위한 수직 파티셔닝 전략과 실제 구현 방법을 설명합니다.

## 수직 파티셔닝이란?
수직 파티셔닝은 테이블의 열(column)을 기준으로 데이터를 분할하는 기법입니다. 자주 사용하는 컬럼과 덜 사용하는 컬럼을 분리하여 성능을 최적화합니다.

### 장점
- 자주 접근하는 데이터에 대한 I/O 감소
- 데이터 압축률 향상
- 캐시 효율성 증가

## 구현 예제

### 1. 기존 테이블 구조 (파티셔닝 전)

```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY,
    username VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(100),
    profile_image BLOB,
    bio TEXT,
    address TEXT,
    created_at TIMESTAMP,
    last_login TIMESTAMP
);
```

### 2. 수직 파티셔닝 적용

#### 자주 접근하는 데이터 테이블
```sql
CREATE TABLE users_main (
    user_id INT PRIMARY KEY,
    username VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(100),
    created_at TIMESTAMP,
    last_login TIMESTAMP
);
```

#### 덜 자주 접근하는 데이터 테이블
```sql
CREATE TABLE users_extra (
    user_id INT PRIMARY KEY,
    profile_image BLOB,
    bio TEXT,
    address TEXT,
    FOREIGN KEY (user_id) REFERENCES users_main(user_id)
);
```

### 3. 데이터 이관 스크립트
```sql
-- 메인 테이블로 데이터 이관
INSERT INTO users_main 
SELECT user_id, username, email, password, created_at, last_login
FROM users;

-- 추가 정보 테이블로 데이터 이관
INSERT INTO users_extra
SELECT user_id, profile_image, bio, address
FROM users;
```

### 4. 조회 쿼리 예제

#### 기본 정보만 조회 (성능 향상)
```sql
SELECT user_id, username, email
FROM users_main
WHERE created_at > '2024-01-01';
```

#### 전체 정보 조회 (조인 필요)
```sql
SELECT m.*, e.profile_image, e.bio, e.address
FROM users_main m
LEFT JOIN users_extra e ON m.user_id = e.user_id
WHERE m.user_id = 1;
```

## 성능 비교

### 테스트 환경
- 데이터베이스: PostgreSQL 13
- 총 레코드 수: 1,000,000
- 테스트 서버: 8GB RAM, 4 Core CPU

### 성능 측정 결과

| 쿼리 유형 | 파티셔닝 전 | 파티셔닝 후 | 성능 향상 |
|-----------|------------|------------|-----------|
| 기본 정보 조회 | 500ms | 150ms | 70% |
| 전체 정보 조회 | 500ms | 600ms | -20% |

## 주의사항

1. 파티셔닝 기준 선정
    - 접근 빈도 분석 필요
    - 데이터 크기 고려
    - 업무 특성 반영

2. 조인 오버헤드
    - 전체 데이터 조회 시 성능 저하 가능성
    - 적절한 인덱스 설계 필요

3. 데이터 정합성
    - 외래 키 제약 조건 관리
    - 트랜잭션 처리 주의

4. 운영 고려사항
    - 백업/복구 전략 수립
    - 모니터링 체계 구축

## 참고 자료
- [PostgreSQL Partitioning Documentation](https://www.postgresql.org/docs/current/ddl-partitioning.html)
- [MySQL Partitioning Guide](https://dev.mysql.com/doc/refman/8.0/en/partitioning.html)