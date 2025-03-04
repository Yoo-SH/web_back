# 데이터베이스 락(Lock) 구현 가이드

이 문서는 데이터베이스 시스템에서 공유(Shared) 락과 배타적(Exclusive) 락을 구현하기 위한 가이드를 제공합니다.

## 목차

1. [개요](#개요)
2. [락의 종류](#락의-종류)
3. [락 매니저 구현](#락-매니저-구현)
4. [데드락 처리](#데드락-처리)
5. [성능 최적화](#성능-최적화)
6. [예제 코드](#예제-코드)
7. [테스트 방법](#테스트-방법)

## 개요

데이터베이스 락은 동시성 제어를 위한 핵심 메커니즘입니다. 여러 트랜잭션이 동시에 같은 데이터에 접근할 때 데이터 일관성을 유지하기 위해 사용됩니다.

## 락의 종류

### 공유 락 (Shared Lock, S-Lock)

- **목적**: 읽기 작업을 위한 락
- **특징**: 여러 트랜잭션이 동시에 획득 가능
- **호환성**: 다른 공유 락과 호환됨, 배타적 락과 호환되지 않음

### 배타적 락 (Exclusive Lock, X-Lock)

- **목적**: 쓰기 작업을 위한 락
- **특징**: 하나의 트랜잭션만 획득 가능
- **호환성**: 어떤 다른 락과도 호환되지 않음

## 락 매니저 구현

### 데이터 구조

```java
class LockManager {
    // 각 리소스에 대한 락 정보를 저장
    private Map<ResourceId, LockInfo> lockTable;
    
    // 트랜잭션 대기 그래프 (데드락 감지용)
    private Graph waitForGraph;
    
    // 락 요청 대기열
    private Map<ResourceId, Queue<LockRequest>> waitingQueues;
}

class LockInfo {
    ResourceId resourceId;
    LockMode currentMode;  // NONE, SHARED, EXCLUSIVE
    Set<TransactionId> holders;  // 현재 락을 보유한 트랜잭션들
}

class LockRequest {
    TransactionId txnId;
    ResourceId resourceId;
    LockMode requestMode;
}
```

### 주요 메서드

```java
// 락 획득 요청
boolean acquireLock(TransactionId txnId, ResourceId resourceId, LockMode mode);

// 락 해제
void releaseLock(TransactionId txnId, ResourceId resourceId);

// 특정 트랜잭션의 모든 락 해제 (트랜잭션 종료 시)
void releaseAllLocks(TransactionId txnId);

// 락 호환성 확인
boolean isCompatible(LockMode existingMode, LockMode requestedMode);

// 데드락 감지
boolean detectDeadlock(TransactionId txnId, ResourceId resourceId);
```

## 락 획득 알고리즘

```
함수 acquireLock(txnId, resourceId, lockMode):
    락 테이블에서 리소스 정보 조회
    
    // 리소스에 대한 락 정보가 없으면 새로 생성
    if 락 정보가 없음:
        새 락 정보 생성
        락 모드 설정
        락 홀더에 트랜잭션 추가
        락 테이블에 저장
        return true
    
    // 이미 해당 트랜잭션이 원하는 모드로 락을 보유 중이면 성공
    if 트랜잭션이 이미 동일하거나 더 강한 락 모드로 락을 보유 중:
        return true
    
    // 락 호환성 검사
    if 요청한 락 모드가 현재 리소스의 락 모드와 호환됨:
        락 홀더에 트랜잭션 추가
        필요시 락 모드 업그레이드
        return true
    
    // 호환되지 않는 경우 대기 또는 데드락 확인
    if 데드락_감지(txnId, resourceId):
        예외 발생 (데드락)
    
    // 대기 큐에 요청 추가
    대기 큐에 락 요청 추가
    트랜잭션을 대기 상태로 설정
    
    // 타임아웃까지 대기
    대기 (타임아웃 적용)
    
    // 대기 후 처리
    if 타임아웃:
        대기 큐에서 요청 제거
        예외 발생 (타임아웃)
    
    return 락 획득 성공 여부
```

## 락 해제 알고리즘

```
함수 releaseLock(txnId, resourceId):
    락 테이블에서 리소스 정보 조회
    
    if 락 정보가 없음:
        return (아무 작업 없음)
    
    if 트랜잭션이 해당 리소스의 락을 보유하지 않음:
        return (아무 작업 없음)
    
    // 트랜잭션을 락 홀더에서 제거
    락 홀더에서 트랜잭션 제거
    
    // 락 홀더가 없으면 대기 중인 요청 처리
    if 락 홀더가 비어있음:
        대기 큐에서 호환되는 요청들을 처리
        호환되는 모든 요청에 대해 락 부여
    
    // 대기 그래프 업데이트
    대기 그래프에서 해당 트랜잭션 엣지 제거
```

## 데드락 처리

### 데드락 감지

```java
boolean detectDeadlock(TransactionId txnId, ResourceId resourceId) {
    // 대기 그래프에 새 엣지 추가
    for (TransactionId holder : getLockHolders(resourceId)) {
        waitForGraph.addEdge(txnId, holder);
    }
    
    // 사이클 감지
    return waitForGraph.hasCycle();
}
```

### 데드락 해결 전략

1. **타임아웃**: 락 요청이 특정 시간 내에 처리되지 않으면 트랜잭션 중단
2. **희생자 선택(Victim Selection)**: 데드락 감지 시 트랜잭션 중 하나를 중단
   - 최소 작업 트랜잭션 선택
   - 최소 리소스 보유 트랜잭션 선택
   - 최근 시작된 트랜잭션 선택

## 성능 최적화

### 락 세분화(Lock Granularity)

- **테이블 수준 락**: 전체 테이블에 락 적용
- **페이지 수준 락**: 데이터베이스 페이지 단위로 락 적용
- **행 수준 락**: 개별 레코드에 락 적용
- **필드 수준 락**: 특정 컬럼에만 락 적용

### 락 에스컬레이션(Lock Escalation)

너무 많은 세밀한 락이 리소스를 소모할 때 더 큰 단위로 락을 확장

```java
void checkLockEscalation(TransactionId txnId, TableId tableId) {
    int lockCount = countLocks(txnId, tableId);
    
    if (lockCount > ESCALATION_THRESHOLD) {
        // 모든 세밀한 락 해제
        releaseFineLocks(txnId, tableId);
        
        // 테이블 수준 락 획득
        acquireTableLock(txnId, tableId);
    }
}
```

## 예제 코드

### 기본 락 매니저 구현

```java
public class LockManager {
    private Map<ResourceId, LockInfo> lockTable = new ConcurrentHashMap<>();
    private Map<ResourceId, Queue<LockRequest>> waitQueues = new ConcurrentHashMap<>();
    private WaitForGraph waitForGraph = new WaitForGraph();
    
    // 락 획득
    public synchronized boolean acquireLock(TransactionId txnId, ResourceId resourceId, LockMode mode) {
        LockInfo lockInfo = lockTable.computeIfAbsent(resourceId, id -> new LockInfo(id));
        
        // 이미 호환되는 락을 보유 중이면 성공
        if (lockInfo.holders.contains(txnId)) {
            if (lockInfo.mode == mode || 
                (lockInfo.mode == LockMode.EXCLUSIVE && mode == LockMode.SHARED)) {
                return true;
            }
            
            // 락 업그레이드 (공유→배타적)
            if (lockInfo.mode == LockMode.SHARED && mode == LockMode.EXCLUSIVE) {
                if (lockInfo.holders.size() == 1) {
                    lockInfo.mode = LockMode.EXCLUSIVE;
                    return true;
                }
            }
        }
        
        // 락 호환성 검사
        if (isCompatible(lockInfo.mode, mode) && lockInfo.waiters.isEmpty()) {
            lockInfo.holders.add(txnId);
            
            // 락 모드 업데이트
            if (mode == LockMode.EXCLUSIVE) {
                lockInfo.mode = LockMode.EXCLUSIVE;
            }
            return true;
        }
        
        // 데드락 검사
        if (detectDeadlock(txnId, resourceId)) {
            throw new DeadlockException("데드락 감지: " + txnId);
        }
        
        // 대기 큐에 추가
        LockRequest request = new LockRequest(txnId, resourceId, mode);
        Queue<LockRequest> queue = waitQueues.computeIfAbsent(resourceId, id -> new LinkedList<>());
        queue.add(request);
        
        // 대기
        try {
            wait(LOCK_TIMEOUT);
        } catch (InterruptedException e) {
            // 대기 중단
        }
        
        // 타임아웃 확인
        if (queue.contains(request)) {
            queue.remove(request);
            throw new LockTimeoutException("락 타임아웃: " + resourceId);
        }
        
        return lockInfo.holders.contains(txnId);
    }
    
    // 락 해제
    public synchronized void releaseLock(TransactionId txnId, ResourceId resourceId) {
        LockInfo lockInfo = lockTable.get(resourceId);
        if (lockInfo == null || !lockInfo.holders.contains(txnId)) {
            return;
        }
        
        // 락 홀더에서 제거
        lockInfo.holders.remove(txnId);
        
        // 대기 그래프 업데이트
        waitForGraph.removeEdges(txnId);
        
        // 락 홀더가 없으면 대기 큐 처리
        if (lockInfo.holders.isEmpty()) {
            lockInfo.mode = LockMode.NONE;
            Queue<LockRequest> queue = waitQueues.get(resourceId);
            
            if (queue != null && !queue.isEmpty()) {
                processWaitQueue(queue, lockInfo);
            }
        }
        
        notifyAll();  // 대기 중인 스레드 깨우기
    }
    
    // 대기 큐 처리
    private void processWaitQueue(Queue<LockRequest> queue, LockInfo lockInfo) {
        // 공유 락 요청부터 처리
        List<LockRequest> sharedRequests = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            LockRequest request = queue.peek();
            
            if (request.mode == LockMode.SHARED && lockInfo.holders.isEmpty()) {
                // 공유 락 요청 처리
                sharedRequests.add(request);
                queue.remove();
            } else if (request.mode == LockMode.EXCLUSIVE && lockInfo.holders.isEmpty()) {
                // 배타적 락 요청 처리 (대기 큐의 첫 번째 요청만)
                lockInfo.holders.add(request.txnId);
                lockInfo.mode = LockMode.EXCLUSIVE;
                queue.remove();
                break;
            } else {
                // 호환되지 않는 요청은 큐에 유지
                break;
            }
        }
        
        // 모든 공유 락 요청 처리
        if (!sharedRequests.isEmpty()) {
            for (LockRequest request : sharedRequests) {
                lockInfo.holders.add(request.txnId);
            }
            lockInfo.mode = LockMode.SHARED;
        }
    }
    
    // 호환성 확인
    private boolean isCompatible(LockMode existing, LockMode requested) {
        if (existing == LockMode.NONE) {
            return true;
        }
        
        if (existing == LockMode.SHARED && requested == LockMode.SHARED) {
            return true;
        }
        
        return false;  // 다른 모든 조합은 호환되지 않음
    }
}
```

### 간단한 트랜잭션 사용 예제

```java
// 트랜잭션 내 데이터 읽기
public void readData(TransactionId txnId, ResourceId resourceId) {
    try {
        // 공유 락 획득
        if (lockManager.acquireLock(txnId, resourceId, LockMode.SHARED)) {
            // 데이터 읽기
            Data data = database.read(resourceId);
            // 작업 후 락을 유지 (트랜잭션 종료 시 해제)
        }
    } catch (LockException e) {
        // 락 획득 실패 처리
        abortTransaction(txnId);
    }
}

// 트랜잭션 내 데이터 쓰기
public void writeData(TransactionId txnId, ResourceId resourceId, Data newData) {
    try {
        // 배타적 락 획득
        if (lockManager.acquireLock(txnId, resourceId, LockMode.EXCLUSIVE)) {
            // 데이터 쓰기
            database.write(resourceId, newData);
            // 작업 후 락을 유지 (트랜잭션 종료 시 해제)
        }
    } catch (LockException e) {
        // 락 획득 실패 처리
        abortTransaction(txnId);
    }
}

// 트랜잭션 커밋
public void commitTransaction(TransactionId txnId) {
    // 트랜잭션 로그 기록
    logManager.writeCommitRecord(txnId);
    
    // 모든 락 해제
    lockManager.releaseAllLocks(txnId);
}

// 트랜잭션 중단
public void abortTransaction(TransactionId txnId) {
    // 변경 사항 롤백
    rollbackManager.rollback(txnId);
    
    // 트랜잭션 로그 기록
    logManager.writeAbortRecord(txnId);
    
    // 모든 락 해제
    lockManager.releaseAllLocks(txnId);
}
```

## 테스트 방법

### 단위 테스트

```java
@Test
public void testSharedLockCompatibility() {
    LockManager lockManager = new LockManager();
    ResourceId resourceId = new ResourceId("table1", 1);
    
    // 두 개의 공유 락이 호환되는지 테스트
    assertTrue(lockManager.acquireLock(new TransactionId(1), resourceId, LockMode.SHARED));
    assertTrue(lockManager.acquireLock(new TransactionId(2), resourceId, LockMode.SHARED));
}

@Test
public void testExclusiveLockIncompatibility() {
    LockManager lockManager = new LockManager();
    ResourceId resourceId = new ResourceId("table1", 1);
    
    // 배타적 락이 다른 락과 호환되지 않는지 테스트
    assertTrue(lockManager.acquireLock(new TransactionId(1), resourceId, LockMode.EXCLUSIVE));
    assertFalse(lockManager.acquireLock(new TransactionId(2), resourceId, LockMode.SHARED));
    assertFalse(lockManager.acquireLock(new TransactionId(3), resourceId, LockMode.EXCLUSIVE));
}
```

### 통합 테스트

```java
@Test
public void testConcurrentTransactions() throws InterruptedException {
    // 동시 트랜잭션 테스트
    final DatabaseSystem db = new DatabaseSystem();
    final ResourceId resourceId = new ResourceId("accounts", 1);
    
    // 초기 데이터 설정
    db.executeUpdate("INSERT INTO accounts VALUES (1, 1000)");
    
    // 동시 트랜잭션 5개 생성
    Thread[] threads = new Thread[5];
    CountDownLatch latch = new CountDownLatch(5);
    
    for (int i = 0; i < 5; i++) {
        final int txnId = i;
        threads[i] = new Thread(() -> {
            try {
                TransactionId tid = new TransactionId(txnId);
                db.beginTransaction(tid);
                
                // 읽기 작업
                int balance = db.readBalance(tid, resourceId);
                
                // 쓰기 작업
                db.updateBalance(tid, resourceId, balance + 100);
                
                // 커밋
                db.commitTransaction(tid);
            } catch (Exception e) {
                // 예외 처리
            } finally {
                latch.countDown();
            }
        });
    }
    
    // 모든 스레드 시작
    for (Thread t : threads) {
        t.start();
    }
    
    // 모든 스레드 완료 대기
    latch.await();
    
    // 최종 결과 확인
    int finalBalance = db.readBalance(new TransactionId(99), resourceId);
    assertEquals(1500, finalBalance);  // 1000 + (100 * 5)
}
```

## 주의 사항

1. **락 순서**: 데드락 방지를 위해 항상 일관된 순서로 락을 획득해야 합니다.
2. **락 세분화**: 성능과 동시성의 균형을 찾기 위해 적절한 락 세분화 수준을 선택하세요.
3. **타임아웃**: 무한 대기를 방지하기 위해 락 획득에 타임아웃을 설정하세요.
4. **락 관리 오버헤드**: 락 관리 자체가 시스템 성능에 영향을 미칠 수 있습니다.
5. **격리 수준**: 트랜잭션 격리 수준에 따라 락 전략을 조정하세요.