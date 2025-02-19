# 데이터베이스 트랜잭션 고립성 (Isolation)

## 목차
1. [개요](#개요)
2. [고립성 레벨](#고립성-레벨)
3. [동시성 문제](#동시성-문제)
4. [각 고립성 레벨별 특징](#각-고립성-레벨별-특징)
5. [실제 사용 사례](#실제-사용-사례)
6. [주의사항](#주의사항)

## 개요

트랜잭션 고립성은 ACID 속성 중 하나로, 동시에 실행되는 트랜잭션들이 서로 영향을 미치지 않도록 보장하는 성질입니다. 데이터베이스는 여러 트랜잭션이 동시에 처리될 때 데이터의 일관성과 무결성을 유지해야 합니다.

## 고립성 레벨

### READ UNCOMMITTED (레벨 0)
- 가장 낮은 고립성 레벨
- 다른 트랜잭션의 커밋되지 않은 데이터를 읽을 수 있음
- Dirty Read 발생 가능

### READ COMMITTED (레벨 1)
- 대부분의 데이터베이스의 기본 고립성 레벨
- 커밋된 데이터만 읽을 수 있음
- Non-Repeatable Read 발생 가능

### REPEATABLE READ (레벨 2)
- 트랜잭션 내에서 같은 쿼리를 실행할 때 항상 같은 결과를 보장
- Phantom Read 발생 가능
- MySQL InnoDB의 기본 격리 수준

### SERIALIZABLE (레벨 3)
- 가장 높은 고립성 레벨
- 완벽한 데이터 일관성 보장
- 성능 저하가 발생할 수 있음

## 동시성 문제

### Dirty Read
- 커밋되지 않은 데이터를 다른 트랜잭션이 읽는 현상
- 데이터 정합성에 심각한 문제를 일으킬 수 있음

예시:
![Image](https://github.com/user-attachments/assets/702ec364-f4bf-44b3-920c-a8ad31a1bf99)


### Non-Repeatable Read
- 한 트랜잭션 내에서 같은 쿼리를 두 번 실행했을 때 결과가 다른 현상

![Image](https://github.com/user-attachments/assets/1961f2d0-a28c-40d6-9fe0-0a47900bc41d)
### Phantom Read
- 한 트랜잭션 내에서 조회 시 이전에 없던 레코드가 나타나는 현상

예시:
![Image](https://github.com/user-attachments/assets/45a122fd-b390-4675-8b4c-1cacc35607aa)

### Lost Update
- 두 개의 트랜잭션이 동일한 데이터를 동시에 수정하려 할 때, 한 트랜잭션의 변경사항이 다른 트랜잭션에 의해 덮어쓰이는 현상

예시:
![Image](https://github.com/user-attachments/assets/15a12101-a208-44f4-ab66-d9a62515f5bc)
## 각 고립성 레벨별 특징
![Image](https://github.com/user-attachments/assets/933010e1-1ad4-4873-b1db-f70ac9e97eb4)

## 실제 사용 사례

### READ UNCOMMITTED
- 데이터 정확성보다 속도가 중요한 경우
- 실시간 모니터링 시스템
- 정확하지 않아도 되는 통계 데이터 조회

### READ COMMITTED
- 일반적인 웹 애플리케이션
- 온라인 쇼핑몰의 상품 조회
- 대부분의 OLTP 시스템

### REPEATABLE READ
- 금융 거래 시스템
- 재고 관리 시스템
- 결제 시스템

### SERIALIZABLE
- 매우 중요한 금융 거래
- 은행 계좌 이체
- 암호화폐 거래소

## 주의사항

1. 성능과 데이터 일관성의 트레이드오프
- 고립성 레벨이 높을수록 동시성이 떨어지고 성능이 저하됨
- 애플리케이션의 요구사항에 맞는 적절한 레벨 선택 필요

2. 데이터베이스별 차이
- MySQL, PostgreSQL, Oracle 등 데이터베이스마다 구현 방식이 다름
- 기본 격리 수준이 다를 수 있음

3. 격리 수준 설정
```sql
-- MySQL에서 트랜잭션 격리 수준 설정
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- 현재 세션의 격리 수준 확인
SELECT @@transaction_isolation;
```

4. 데드락 가능성
- 높은 격리 수준에서는 데드락 발생 가능성이 높아짐
- 적절한 데드락 처리 로직 구현 필요

5. 모니터링
- 트랜잭션 격리 수준에 따른 성능 모니터링 필요
- 잠금 경합 상황 모니터링