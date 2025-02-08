# `Java Thread 스케줄링 관련 메서드`

Java에서 스레드의 실행 순서와 타이밍을 제어하는 주요 메서드는 다음과 같습니다.

## 1. `sleep(milliseconds)`
- 현재 실행 중인 스레드를 지정된 시간(ms) 동안 일시 정지(Timed Waiting) 상태로 만듭니다.
- 시간이 지나면 다시 실행 가능(Ready) 상태로 돌아갑니다.
- `InterruptedException`을 처리해야 합니다.

### **사용 예시**
```java
try {
    Thread.sleep(2000); // 2초 동안 멈춤
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // 인터럽트 상태를 복구
}
```

### **Spring Boot에서의 활용**
- 특정 작업(예: API 요청, 배치 작업)의 실행을 일정 시간 동안 지연할 때 사용
- `@Scheduled`에서 일정 간격으로 실행되는 작업에 유용

```java
@Scheduled(fixedDelay = 5000) // 5초마다 실행
public void scheduledTask() throws InterruptedException {
    System.out.println("Task 실행 중...");
    Thread.sleep(2000); // 2초 동안 지연
    System.out.println("Task 완료");
}
```

---

## 2. `join()`
- 특정 스레드가 종료될 때까지 현재 스레드를 대기시키는 역할
- 실행 중인 스레드가 다른 스레드의 작업이 완료될 때까지 기다려야 할 때 유용

### **사용 예시**
```java
Thread thread = new Thread(() -> {
    try {
        Thread.sleep(3000); // 3초 동안 작업 수행
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println("작업 완료!");
});

thread.start();

try {
    thread.join(); // thread가 끝날 때까지 현재 스레드는 대기
} catch (InterruptedException e) {
    e.printStackTrace();
}
System.out.println("모든 작업 완료!");
```

### **Spring Boot에서의 활용**
- 여러 개의 비동기 작업 중 특정 작업이 완료된 후 실행해야 하는 경우 유용
- `@Async` 메서드 호출 후 특정 순서로 실행이 필요할 때 활용 가능

```java
@Async
public CompletableFuture<String> asyncMethod() {
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    return CompletableFuture.completedFuture("비동기 작업 완료");
}

public void executeTasks() throws Exception {
    CompletableFuture<String> future = asyncMethod();
    future.join(); // 해당 비동기 작업이 완료될 때까지 대기
    System.out.println("모든 작업 완료");
}
```

---

## 3. `yield()`
- 현재 실행 중인 스레드의 실행을 양보하여 다른 스레드가 실행될 기회를 줌
- 단, OS의 스케줄러에 의해 다시 현재 스레드가 실행될 수도 있음
- CPU 집약적인 작업을 수행할 때 다른 스레드가 실행될 기회를 주는 용도로 사용

### **사용 예시**
```java
Thread thread = new Thread(() -> {
    for (int i = 0; i < 5; i++) {
        System.out.println(Thread.currentThread().getName() + " 실행 중");
        Thread.yield(); // 다른 스레드에게 실행 기회 제공
    }
});

thread.start();
```

### **Spring Boot에서의 활용**
- 거의 사용되지 않지만, CPU 리소스를 효율적으로 사용하고 싶은 경우 고려할 수 있음
- 다만, Spring Boot에서는 보통 `ExecutorService`나 `@Async`를 사용하여 비동기 작업을 관리하기 때문에 `yield()`를 직접 사용할 일은 많지 않음

---

## 4. `interrupt()`
- 실행 중인 스레드에 인터럽트 신호를 보내서 중단할 수 있도록 함
- `sleep()`이나 `join()` 같은 대기 상태에서 `InterruptedException`을 발생시킴
- 직접 `interrupt` 플래그를 체크하여 실행 중인 루프를 중단하는 데 활용 가능

### **사용 예시**
```java
Thread thread = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        try {
            Thread.sleep(1000);
            System.out.println("작업 실행 중...");
        } catch (InterruptedException e) {
            System.out.println("스레드가 인터럽트됨!");
            Thread.currentThread().interrupt(); // 인터럽트 상태를 다시 설정
            break;
        }
    }
});

thread.start();
Thread.sleep(3000); // 3초 후 인터럽트
thread.interrupt();
```

### **Spring Boot에서의 활용**
- 배치 작업이나 장기 실행 프로세스를 중단할 때 활용
- `@Async` 작업이 일정 조건에서 중단되도록 할 수 있음

```java
@Async
public void asyncTask() {
    while (!Thread.currentThread().isInterrupted()) {
        try {
            Thread.sleep(1000);
            System.out.println("작업 실행 중...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("작업이 중단됨!");
            break;
        }
    }
}

public void stopTask(Thread thread) {
    thread.interrupt(); // 실행 중인 작업을 중단
}
```

---

## thread 메서드 정리

| 메서드 | 설명 | Spring Boot 활용 |
|--------|--------|----------------|
| `sleep(ms)` | 일정 시간 동안 현재 스레드를 멈춤 | `@Scheduled`에서 일정 시간 동안 실행 지연 |
| `join()` | 특정 스레드가 종료될 때까지 현재 스레드 대기 | 비동기 작업이 완료된 후 실행될 작업을 조정 |
| `yield()` | 현재 스레드의 실행을 양보하여 다른 스레드가 실행될 수 있도록 함 | 거의 사용되지 않음 |
| `interrupt()` | 스레드를 중단하는 인터럽트 신호를 보냄 | 장기 실행 작업을 중단할 때 활용 |

### **Spring Boot에서 활용 예시**
- `@Scheduled` + `sleep()`을 사용하여 배치 작업 수행
- `@Async` + `join()`을 사용하여 특정 비동기 작업이 완료된 후 실행
- `interrupt()`를 사용하여 실행 중인 작업을 안전하게 중단

**Spring Boot에서는 `ExecutorService`나 `@Async` 기반의 비동기 처리가 기본이므로, 직접 스레드를 생성하는 것보다 Spring의 기능을 활용하는 것이 권장됩니다.**


<br>
