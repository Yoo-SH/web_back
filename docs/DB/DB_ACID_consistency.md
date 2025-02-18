# 데이터베이스 일관성 (Database Consistency)

## 목차
- [개요](#개요)
- [데이터베이스 일관성의 종류](#데이터베이스-일관성의-종류)
- [일관성 유지 방법](#일관성-유지-방법)
- [트랜잭션과 일관성](#트랜잭션과-일관성)
- [분산 시스템에서의 일관성](#분산-시스템에서의-일관성)
- [일관성 모델](#일관성-모델)
- [모범 사례](#모범-사례)

## 개요

데이터베이스 일관성(Database Consistency)은 데이터베이스의 데이터가 정확성, 완전성, 그리고 신뢰성을 유지하는 상태를 의미합니다. 이는 데이터베이스 관리 시스템(DBMS)의 핵심 특성 중 하나이며, ACID 속성(Atomicity, Consistency, Isolation, Durability)의 한 부분을 구성합니다.

## 데이터베이스 일관성의 종류

### 1. 도메인 일관성 (Domain Consistency)
- 모든 데이터는 정의된 도메인 내의 값만을 가져야 함
- 예: 나이 필드는 음수가 될 수 없음

### 2. 참조 일관성 (Referential Integrity)
- 외래 키는 반드시 참조하는 테이블의 기본 키와 일치해야 함
- 고아 레코드(orphaned records) 방지

### 3. 엔티티 일관성 (Entity Integrity)
- 기본 키는 NULL이 될 수 없음
- 각 레코드는 유일한 식별자를 가져야 함

### 4. 사용자 정의 일관성 (User-Defined Consistency)
- 비즈니스 규칙에 따른 제약조건
- CHECK 제약조건, 트리거 등을 통해 구현

## 일관성 유지 방법

### 1. 제약조건 (Constraints)
```sql
-- 기본 키 제약조건
PRIMARY KEY (column_name)

-- 외래 키 제약조건
FOREIGN KEY (column_name) REFERENCES table_name(column_name)

-- CHECK 제약조건
CHECK (age >= 0 AND age <= 150)
```

### 2. 트리거 (Triggers)
```sql
CREATE TRIGGER check_salary
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN
    IF NEW.salary < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Salary cannot be negative';
    END IF;
END;
```

## 트랜잭션과 일관성

### ACID 속성에서의 일관성
- 트랜잭션은 데이터베이스를 한 일관된 상태에서 다른 일관된 상태로 변경
- 트랜잭션 실행 중에는 일시적으로 일관성이 깨질 수 있음
- 트랜잭션 완료 후에는 반드시 일관성이 보장되어야 함

### 일관성 레벨
1. **Read Uncommitted**
    - 가장 낮은 격리 수준
    - Dirty Read 발생 가능

2. **Read Committed**
    - 커밋된 데이터만 읽기 가능
    - Non-Repeatable Read 발생 가능

3. **Repeatable Read**
    - 트랜잭션 내에서 동일한 결과 보장
    - Phantom Read 발생 가능

4. **Serializable**
    - 가장 높은 격리 수준
    - 완벽한 일관성 보장
    - 성능 저하 가능성

## 분산 시스템에서의 일관성

### CAP 이론
- **Consistency (일관성)**
- **Availability (가용성)**
- **Partition Tolerance (분할 내성)**

세 가지 특성 중 동시에 두 가지만 만족 가능

### 일관성 모델
1. **강한 일관성 (Strong Consistency)**
    - 모든 노드가 항상 동일한 데이터를 보여줌
    - 높은 비용과 지연 발생

2. **최종 일관성 (Eventual Consistency)**
    - 시간이 지나면 모든 노드가 동일한 데이터를 가짐
    - 일시적인 불일치 허용

3. **인과적 일관성 (Causal Consistency)**
    - 인과 관계가 있는 작업들의 순서 보장
    - 독립적인 작업은 다른 순서 가능

## 모범 사례

### 1. 설계 단계
- 적절한 정규화 수준 선택
- 명확한 제약조건 정의
- 인덱스 전략 수립

### 2. 구현 단계
- 트랜잭션 범위 최소화
- 적절한 격리 수준 선택
- 데드락 방지 전략 수립

### 3. 운영 단계
- 정기적인 무결성 검사
- 백업 및 복구 전략 수립
- 성능 모니터링

## 결론

데이터베이스 일관성은 시스템의 신뢰성과 정확성을 보장하는 핵심 요소입니다. 적절한 일관성 수준을 선택하고 유지하는 것은 시스템 설계의 중요한 부분이며, 비즈니스 요구사항과 성능 사이의 균형을 고려해야 합니다.