# Java Executor 서비스란?

**Executor Framework**는 자바 5에서 도입된 **병렬 실행 관리** 프레임워크로, 스레드 생성 및 관리를 간단하고 효과적으로 처리하기 위해 설계되었습니다.  
`ExecutorService`는 이 프레임워크의 핵심 인터페이스로, **스레드 풀(thread pool)**을 사용하여 작업을 실행하는 데 도움을 줍니다.

---

## 1. 왜 ExecutorService를 사용하는가?

### 기존의 스레드 생성 방식의 문제점
- 스레드를 직접 생성(`new Thread()`)하고 관리하는 것은 비효율적이고 복잡할 수 있습니다.
- 많은 요청을 처리할 때 스레드가 무분별하게 생성되면 **시스템 자원 고갈** 및 **성능 저하**를 야기할 수 있습니다.
- 직접 생성된 스레드는 재사용되지 않으므로 리소스 낭비가 발생합니다.

### ExecutorService의 장점
- **스레드 풀 재사용**: 스레드가 재사용되어 관리 효율성을 높이고 성능을 최적화합니다.
- **시간 초과 및 스레드 종료**: 작업 제출 시 시간 초과 값을 지정하거나 특정 조건에서 스레드를 종료할 수 있습니다.
- **작업 완료 여부 관리**: 작업 결과를 추적하고 필요한 경우 `Future` 객체를 사용하여 결과를 확인할 수 있습니다.

---

## 2. 주요 인터페이스 및 클래스

| 클래스/인터페이스  | 설명                                                                                           |
|--------------------|-----------------------------------------------------------------------------------------------|
| `Executor`         | 단일 작업 실행을 위한 간단한 인터페이스. (예: `execute`)                                        |
| `ExecutorService`  | `Executor`를 확장하며, 작업 관리를 위한 더 많은 기능 제공 (예: `submit`, `shutdown` 등).        |
| `ScheduledExecutorService` | 주기적인 작업 스케줄링 가능한 서비스.                                                   |
| `Executors`        | `ExecutorService`의 구현체를 쉽게 생성할 수 있는 정적 팩토리 클래스.                             |

---

## 3. 주요 메서드 설명

### (1) ExecutorService의 주요 메서드
| 메서드                  | 설명                                                                                           |
|-------------------------|-----------------------------------------------------------------------------------------------|
| `execute(Runnable)`     | Runnable 작업을 실행합니다. (결과 반환 없음)                                                   |
| `submit(Runnable/Callable)` | 작업을 제출하고 결과를 반환받기 위한 `Future` 객체를 리턴합니다.                             |
| `shutdown()`            | 모든 작업의 실행을 완료한 후 Executor를 종료합니다.                                            |
| `shutdownNow()`         | 진행 중인 작업을 중단하고 즉시 스레드를 종료합니다.                                            |
| `awaitTermination(timeout)` | 종료 상태를 기다리며 지정된 시간 초과 시 종료됩니다.                                         |

### (2) ScheduledExecutorService의 추가 메서드
| 메서드                          | 설명                                                                                       |
|---------------------------------|-------------------------------------------------------------------------------------------|
| `schedule()`                    | 작업을 일정 시간 후 실행.                                                                  |
| `scheduleAtFixedRate()`         | 작업을 고정된 간격으로 반복 실행.                                                          |
| `scheduleWithFixedDelay()`      | 이전 작업 완료 후 일정 지연 시간 후 작업을 실행.                                            |

---

## 4. 예제 코드

### (1) ExecutorService를 사용한 Runnable 작업 실행
```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorExample {
    public static void main(String[] args) {
        // Single-thread Executor 생성
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Runnable 작업 제출
        Runnable task = () -> {
            System.out.println("Task is running on thread: " + Thread.currentThread().getName());
        };
        executorService.execute(task);

        // ExecutorService 종료
        executorService.shutdown();
    }
}
```

**출력 예시**: