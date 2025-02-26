# @Transactional 가이드

## 1. `@Transactional`이 하는 일

### 🔹 기본적인 원리
- `@Transactional`은 AOP(Aspect-Oriented Programming)를 이용하여 메서드 실행 전후로 **트랜잭션을 자동으로 관리**하는 역할을 함.
- 해당 메서드가 호출되면 **트랜잭션을 시작**하고, 정상적으로 실행되면 **커밋(Commit)**, 예외가 발생하면 **롤백(Rollback)** 수행.
- Spring의 `PlatformTransactionManager`가 내부적으로 트랜잭션을 관리하며, JPA, JDBC, Hibernate 등 다양한 ORM 및 DB와 연동 가능.

### 🔹 트랜잭션 시작
- `@Transactional`이 선언된 메서드가 호출되면, **트랜잭션이 시작됨**.

### 🔹 변경 감지 (Dirty Checking)
- JPA의 **영속성 컨텍스트**가 해당 엔티티의 **원본 상태**를 관리함.
- 엔티티의 필드 값이 변경되면, JPA는 이를 감지하고 **자동으로 변경 사항을 DB에 반영**함.
- `save()`를 명시적으로 호출하지 않아도, 트랜잭션이 끝날 때 변경 사항이 DB에 저장됨.

#### 🔹 Dirty Checking 비교 코드

##### `@Transactional`을 사용한 경우
```java
@Transactional
public void updateJobPost(Long id, String newTitle) {
    JobPost jobPost = jobPostRepository.findById(id).orElseThrow();
    jobPost.setTitle(newTitle); // 변경 감지로 자동 반영됨
}
```

##### `@Transactional` 없이 `save()`를 직접 호출하는 경우
```java
public void updateJobPost(Long id, String newTitle) {
    JobPost jobPost = jobPostRepository.findById(id).orElseThrow();
    jobPost.setTitle(newTitle);
    jobPostRepository.save(jobPost); // 명시적으로 저장해야 변경됨
}
```

### 🔹 트랜잭션 커밋 또는 롤백
- 메서드가 정상적으로 실행되면, **트랜잭션을 커밋**하여 변경 사항을 DB에 반영.
- 실행 중 예외가 발생하면, **트랜잭션을 롤백**하여 변경 사항을 모두 취소.

---

## 2. `@Transactional`을 써야 하는 경우 ✅

### ✅ 데이터 일관성이 중요한 경우
- 중간에 예외가 발생하면 **변경 사항이 모두 롤백되어야 하는 경우**.
- `save()` 호출 후 예외가 발생하면 이미 저장된 데이터는 롤백되지 않지만, `@Transactional`을 사용하면 **모든 변경 사항을 롤백**할 수 있음.

### ✅ JPA의 변경 감지(Dirty Checking)를 활용하는 경우
- `save()`를 명시적으로 호출하지 않아도, **자동으로 변경 사항이 저장됨**.
- 코드가 더 깔끔하고 유지보수가 쉬워짐.

### ✅ 여러 개의 DB 작업을 하나의 단위로 묶고 싶은 경우
- 한 개 이상의 엔티티를 수정해야 할 경우, **하나라도 실패하면 전체가 롤백**되어 데이터의 일관성을 유지할 수 있음.

### ✅ 서비스 레이어에서 트랜잭션을 관리해야 하는 경우
- **DAO(repository) 레이어가 아닌 서비스 레이어에서 트랜잭션을 관리하는 것이 일반적**.
- 여러 개의 DAO(repository) 메서드가 하나의 서비스 메서드에서 호출될 때, **서비스 단위로 트랜잭션을 묶어서 관리하는 것이 좋음**.

---

## 3. `@Transactional`을 쓰면 안 되는 경우 ❌

### ❌ 읽기 전용 작업일 경우
- 단순 조회하는 메서드에서 `@Transactional`을 사용하면 오버헤드 발생 가능.
- **해결 방법:** `@Transactional(readOnly = true)`를 사용하면 성능 최적화 가능.

### ❌ 외부 API 호출이 포함된 경우
- 트랜잭션 내에서 **외부 API 호출(예: 이메일 전송, SMS 전송 등)**이 포함되면, API 호출 이후 예외가 발생할 경우 트랜잭션이 롤백됨.
- **결과:** API 요청이 무효화될 수 있음.
- **해결 방법:** API 호출은 트랜잭션 **외부에서 처리**하는 것이 좋음.

### ❌ 트랜잭션이 너무 길어지는 경우
- 트랜잭션이 길어지면 **DB 락(Lock)이 오래 유지**될 수 있어 성능 저하 위험.
- **해결 방법:** 트랜잭션의 범위를 최소한으로 유지.

---

## 4. 결론
- **데이터 일관성을 보장해야 하거나, JPA의 Dirty Checking을 활용하려면 `@Transactional`을 사용하는 것이 좋음.**
- **반대로 읽기 전용 작업이거나, 외부 API 호출이 포함된 경우에는 사용을 지양해야 함.**
- **서비스 레이어에서 트랜잭션을 관리하는 것이 일반적이며, DAO(repository) 레이어에서 직접 관리하는 것은 지양하는 것이 좋음.**

