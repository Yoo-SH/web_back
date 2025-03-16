# 데이터베이스 시스템

데이터베이스 시스템이란 데이터베이스에 데이터를 저장하고, 이를 관리하여 조직에 필요한 정보를 생성해주는 시스템이다. 

__구성 요소__

- 데이터베이스
- 데이터베이스 관리 시스템
- 데이터 언어
- 컴퓨터
- 데이터베이스 사용자

__데이터베이스 시스템 구성요소 이미지__

<Image src= "https://github.com/user-attachments/assets/07d8ef33-ff39-4f50-832e-8abe72227f8e" width=200px>

<br>

## 데이터베이스 사용자
데이터베이스를 이용하기 위해 접근하는 모든 사람을 의미하며 이용 목적에 따라 데이터베이스 관리자, 최종 사용자, 응용 프로그래머로 구분한다.

- 데이터베이스 관리자 : 데이터베이스 시스템을 관리하고 운영한다.
- 최종 사용자(일반 사용자) : 데이터베이스에 접근하여 데이터를 조작한다. (데이터를 조작(삽입, 삭제, 수정, 검색)하기 위해 데이터베이스에 접근하는 사람.)
- 응용 프로그래머 : 데이터 언어를 삽입하여 응용 프로그램을 작성한다. (응용 프로그램을 작성할 때 데이터베이스에 접근하는 데이터 조작어를 삽입하는 사용자)

## 데이터 언어

사용자가 데이터베이스를 구축하고 이에 접근하기 위해 데이터베이스 관리 시스템과 통신하는 수단이다.  사용 목적에 따라 데이터 정의어(DDL), 데이터 조작어(DML), 데이터 제어어(DCL)로 구분된다.

### 데이터 언어 데이터 정의어 (DDL. Data Definition Language)
새로운 데이터베이스를 구축하기 위해 스키마를 정의하거나 기존 스키마의 정의를 삭제 또는 수정하기 위해 사용하는 언어이며 데이터 사전에 저장된다.

```SQL
-- 데이터베이스 생성
CREATE DATABASE my_database;

-- 테이블 생성
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 테이블 변경 (컬럼 추가)
ALTER TABLE users ADD COLUMN age INT;

-- 테이블 삭제
DROP TABLE users;
```

### 데이터 언어 데이터 조작어 (DML. Data Manipulation Language)
사용자가 데이터의 삽입, 삭제, 수정, 검색 등의 처리를 데이터베이스 관리 시스템에 요구하기 위해 사용하는 언어이며 사용자가 실제 데이터 값을 활용하기 위해 사용하는 언어이다.

```SQL
-- 데이터 삽입
INSERT INTO users (name, email, age) VALUES ('홍길동', 'hong@example.com', 30);

-- 데이터 조회
SELECT * FROM users WHERE age >= 20;

-- 데이터 수정
UPDATE users SET age = 35 WHERE name = '홍길동';

-- 데이터 삭제
DELETE FROM users WHERE name = '홍길동';
```

### 데이터 언어 데이터 제어어 (DCL. Data Control Language)
데이터베이스에 저장된 데이터를 여러 사용자가 무결성과 일관성을 유지하며 문제없이 공유할 수 있도록, 내부적으로 필요한 규칙이나 기법을 정의하는 데 사용하는 언어이다.

• 사용 목적
  - 무결성 (integrity) : 데이터베이스에 정확하고 유효한 데이터만 유지.
  - 보안 (security) : 허가받지 않은 사용자의 데이터 접근 차단, 허가된 사용자에게 접근 권한 부여.
  - 회복 (recovery) : 장애가 발생해도 데이터 일관성 유지
  - 동시성 제어 (concurrency) : 데이터 동시 공유 지원

```SQL
-- 사용자에게 권한 부여
GRANT SELECT, INSERT ON my_database.users TO 'user1'@'localhost';

-- 사용자 권한 회수
REVOKE INSERT ON my_database.users FROM 'user1'@'localhost';

-- 트랜잭션 제어 (무결성 유지)
START TRANSACTION;
UPDATE users SET age = 40 WHERE name = '홍길동';
COMMIT;  -- 변경 사항 저장

-- 만약 오류 발생 시 롤백
ROLLBACK;

```

### 데이터베이스 관리 시스템 주요 구성 요소

- 질의 처리기 (query processor) : 사용자의 데이터 처리 요구를 해석하여 처리.
  DDL 컴파일러, DML 프리컴파일러, DML 컴파일러, 런타임 데이터베이스 처리기, 트랜잭션 관리자 등을 포함.
  
- 저장 데이터 관리자 (stored data manager) : 디스크에 저장된 데이터베이스와 데이터 사전을 관리하고 접근함.

### __데이터 사전 (data dictionary)__

데이터베이스에 저장된 데이터에 관한 정보를 저장하는 곳이다. 데이터베이스에 저장되어 있는 데이터를 정확하고 효율적으로 이용하기 위해 참고해야 하는 스키마, 사상 정보, 다양한 제약 조건 등을 저장하고 있다.

즉 데이터베이스에 저장된 데이터에 관한 정보, 즉 메타데이터를 유지하는 시스템 데이터베이스이다. 
 
또한, 데이터베이스 관리 시스템이 스스로 생성하고 유지하며 데이터베이스 관리 시스템이 주로 접근. 데이터 사전에 내용을 새로 추가하거나 수정하지만 일반 사용자도 검색에 한에서 접근 가능하다.

### 데이터 디렉토리 (data directory)

데이터 사전에 있는 데이터에 실제로 접근하는 데 필요한 위치 정보를 관리하며, 시스템만 접근 가능하다. (일반 사용자의 접근은 허용되지 않음)



## 데이터베이스 관리 시스템의 구성 요약
<Image src= "https://github.com/user-attachments/assets/be2354b2-4903-41d7-8423-4d2c1980e939" width=500px>




## 참고자료 
- Medical AI & Computational Science (MACS lab), JBNU