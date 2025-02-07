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

## 코드 예제

### 1. 여러 작업 병렬 실행 (`invokeAll` 사용)
`invokeAll` 메서드를 사용해 여러 `Callable` 작업을 병렬 실행하고, 모든 결과를 얻을 수 있습니다.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InvokeAllExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Callable<String>> taskList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            taskList.add(() -> {
                TimeUnit.SECONDS.sleep(taskId); 
                return "Task " + taskId + " completed";
            });
        }

        executorService.invokeAll(taskList).forEach(future -> {
            try {
                System.out.println(future.get()); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
    }
}
```

---

### 2. 빠르게 완료된 작업의 결과만 가져오기 (`invokeAny` 사용)
`invokeAny`는 가장 **빠르게 완료된 작업 결과**만 반환합니다.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InvokeAnyExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Callable<String>> taskList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            taskList.add(() -> {
                Thread.sleep(taskId * 1000L); 
                return "Task " + taskId + " completed";
            });
        }

        String result = executorService.invokeAny(taskList);
        System.out.println(result);

        executorService.shutdown();
    }
}
```

---

### 3. 작업 실행 중 취소하기 (`Future.cancel` 사용)

`Future.cancel` 메서드를 사용하여 실행 중인 작업을 중단할 수 있습니다.

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CancelExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<String> task = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("Task is running... " + i);
                Thread.sleep(500);
            }
            return "Task completed";
        };

        Future<String> future = executorService.submit(task);

        try {
            Thread.sleep(2000); // 2초 후 작업 취소
            boolean cancelled = future.cancel(true);
            System.out.println("Task cancelled: " + cancelled);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}
```

---

### 4. 작업 완료 시간 초과 처리

제출된 작업이 일정 시간 초과 시 작업을 취소하도록 설정할 수 있습니다.

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TimeoutExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<String> task = () -> {
            Thread.sleep(5000); 
            return "Task finished";
        };

        Future<String> future = executorService.submit(task);

        try {
            String result = future.get(3, TimeUnit.SECONDS);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Task timed out and was cancelled.");
            future.cancel(true); 
        }

        executorService.shutdown();
    }
}
```

---

### 5. 스케줄링 작업 (`scheduleWithFixedDelay` 사용)

`ScheduledExecutorService`를 사용해 이전 작업 종료 후 일정 지연 시간으로 주기적인 작업을 실행합니다.

```java
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleWithFixedDelayExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleWithFixedDelay(() -> {
            System.out.println("Task executed at: " + System.currentTimeMillis());
        }, 1, 2, TimeUnit.SECONDS);

        scheduler.schedule(() -> scheduler.shutdown(), 10, TimeUnit.SECONDS);
    }
}
```

---

### 6. 동적으로 스레드 풀 관리 (`newCachedThreadPool` 사용)

`newCachedThreadPool`을 사용하면 필요에 따라 동적으로 스레드가 생성되고 재사용됩니다.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            executorService.execute(() -> {
                System.out.println("Task " + taskId + " is running on thread: " + Thread.currentThread().getName());
            });
        }

        executorService.shutdown();
    }
}
```

---

### 7. 예외 처리 (`submit` 과 `execute`의 차이점)

`submit`은 작업 실행 중 발생한 예외를 `Future` 객체로 감싸 관리할 수 있지만, `execute`는 그렇지 않습니다.

```java
import java.util.concurrent.*;

public class ExceptionHandlingExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Callable<Integer> task = () -> {
            throw new RuntimeException("Task failed with exception!");
        };

        Future<Integer> future = executorService.submit(task);

        try {
            future.get(); 
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getCause());
        }

        executorService.shutdown();
    }
}
```

출력: Exception caught: java.lang.RuntimeException: Task failed with exception!


---

## FAQ

### `shutdown()`과 `shutdownNow()`의 차이점
- `shutdown()`: 기존에 제출된 작업만 모두 완료한 후 스레드풀을 종료합니다.
- `shutdownNow()`: 실행 중인 작업을 강제로 중단하고 즉시 종료합니다.

---

## 결론

- **ExecutorService**는 병렬 처리를 단순화하고 성능 최적화에 도움을 줍니다.
- `Runnable` 또는 `Callable`과 함께 사용하여 작업 실행 및 결과 수집을 효율적으로 처리할 수 있습니다.
- **스레드풀**을 통해 스레드 관리와 재사용이 용이하며, **스케줄링**과 **작업 타임아웃** 같은 고급 기능도 제공합니다.
- 다양한 구현체(`newFixedThreadPool`, `newCachedThreadPool`, `newSingleThreadExecutor`)와 요구사항에 따라 적합한 방식의 스레드풀을 선택하세요.
