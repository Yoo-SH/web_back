# DB 트랜잭션 원자성 (Atomicity) 가이드

## 목차
- [개요](#개요)
- [원자성의 정의](#원자성의-정의)
- [원자성이 중요한 이유](#원자성이-중요한-이유)
- [원자성 구현 방법](#원자성-구현-방법)
- [원자성 보장을 위한 DBMS 기능](#원자성-보장을-위한-dbms-기능)
- [실제 사용 예시](#실제-사용-예시)
- [주의사항](#주의사항)

## 개요

데이터베이스의 원자성(Atomicity)은 ACID 속성(Atomicity, Consistency, Isolation, Durability) 중 하나로, 트랜잭션의 가장 기본적이고 중요한 특성입니다.

## 원자성의 정의

원자성이란 트랜잭션과 관련된 작업들이 모두 성공하거나 모두 실패하는 특성을 의미합니다. 즉, 트랜잭션 내의 모든 연산은 "all-or-nothing"의 원칙을 따릅니다.

### 핵심 특징
- 트랜잭션은 분할할 수 없는 최소 단위입니다
- 부분적 실행이나 부분적 실패를 허용하지 않습니다
- 중간 상태가 존재하지 않습니다

## 원자성이 중요한 이유

1. **데이터 일관성 유지**
    - 시스템 장애 발생 시에도 데이터 정합성 보장
    - 비즈니스 로직의 안전한 실행 보장

2. **시스템 신뢰성**
    - 금융 거래나 중요 데이터 처리에서 필수적
    - 장애 상황에서의 복구 가능성 제공

3. **버그 방지**
    - 복잡한 연산에서 일부만 실행되는 문제 방지
    - 예측 가능한 시스템 동작 보장

## 원자성 구현 방법

### 1. 트랜잭션 경계 설정
```sql
BEGIN TRANSACTION;
    -- 실행할 SQL 구문들
COMMIT;  -- 또는 ROLLBACK;
```

### 2. 프로그래밍 언어에서의 구현
```java
@Transactional
public void transferMoney(Account from, Account to, BigDecimal amount) {
    from.withdraw(amount);
    to.deposit(amount);
}
```

## 원자성 보장을 위한 DBMS 기능

### 1. 로깅 메커니즘
- Undo 로그: 트랜잭션 실패 시 복구용
- Redo 로그: 시스템 장애 시 복구용

### 2. 체크포인트
- 주기적인 데이터베이스 상태 저장
- 복구 시간 단축

### 3. 롤백 메커니즘
- 트랜잭션 실패 시 이전 상태로 복원
- 부분적 실행 방지

## 실제 사용 예시

### 은행 송금 시나리오
```sql
BEGIN TRANSACTION;

UPDATE accounts 
SET balance = balance - 1000
WHERE account_id = 'A';

UPDATE accounts
SET balance = balance + 1000
WHERE account_id = 'B';

COMMIT;
```

### 재고 관리 시나리오
```sql
BEGIN TRANSACTION;

UPDATE inventory
SET quantity = quantity - 1
WHERE product_id = 'P1';

INSERT INTO orders (product_id, quantity)
VALUES ('P1', 1);

COMMIT;
```

## 주의사항

1. **트랜잭션 범위 설정**
    - 너무 큰 트랜잭션은 성능 저하 유발
    - 너무 작은 트랜잭션은 데이터 일관성 위험

2. **데드락 고려**
    - 적절한 타임아웃 설정
    - 데드락 발생 가능성 최소화

3. **성능 최적화**
    - 트랜잭션 실행 시간 최소화
    - 불필요한 트랜잭션 제거

4. **예외 처리**
    - 모든 예외 상황 고려
    - 적절한 롤백 처리

---

## 기여 방법

이 문서에 기여하고 싶으시다면 다음 절차를 따라주세요:
1. 이슈 생성
2. 풀 리퀘스트 제출
3. 코드 리뷰 진행

## 라이선스

이 문서는 MIT 라이선스 하에 제공됩니다.