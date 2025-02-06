# `Java에서 스레드 동기화 (Synchronization)`

멀티스레드 환경에서는 여러 스레드가 동시에 공유 자원에 접근하면서 예상치 못한 문제가 발생할 수 있습니다. 이를 방지하기 위해 **임계 영역(Critical Section)과 잠금(Lock)**을 활용하여 동기화를 수행할 수 있습니다.

## 1. 임계 영역 (Critical Section)
임계 영역은 여러 스레드가 동시에 접근하면 안 되는 공유 자원을 보호하는 코드 블록입니다. Java에서는 `synchronized` 키워드를 사용하여 임계 영역을 설정할 수 있습니다.

### 임계 영역을 활용한 자원 보호 방법
- `synchronized` 키워드를 사용하면 한 번에 하나의 스레드만 해당 블록에 접근할 수 있도록 보장합니다.
- 여러 스레드가 동시에 실행되더라도 `synchronized` 블록을 통해 자원이 일관된 상태를 유지할 수 있습니다.

### 예제: `synchronized` 메서드를 활용한 동기화
```java
class SharedResource {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}

public class SynchronizedExample {
    public static void main(String[] args) throws InterruptedException {
        SharedResource resource = new SharedResource();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                resource.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                resource.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final count: " + resource.getCount());
    }
}
```
이 코드에서 `increment()` 메서드는 `synchronized` 키워드를 사용하여 임계 영역을 보호합니다. 따라서 여러 스레드가 동시에 접근하더라도 `count` 값이 일관되게 유지됩니다.

## 2. [Lock을 활용한 동기화](https://github.com/gyoogle/tech-interview-for-developer/blob/master/Language/%5BJava%5D%20Intrinsic%20Lock.md)
Java의 `ReentrantLock`을 사용하면 보다 세밀한 동기화 제어가 가능합니다.

### Lock을 활용한 자원 보호 방법
- `ReentrantLock`을 사용하면 보다 정교한 잠금 메커니즘을 구현할 수 있습니다.
- `tryLock()` 메서드를 이용하면 특정 시간 동안 잠금을 시도하고 실패 시 다른 작업을 수행하도록 설정할 수 있습니다.
- 명시적으로 `lock.lock()`과 `lock.unlock()`을 호출해야 하므로, `finally` 블록을 사용하여 잠금을 안전하게 해제해야 합니다.

### 예제: `ReentrantLock`을 활용한 동기화((Lock의 획득이 '호출 단위'가 아닌 **Thread 단위**로 일어나는 것))
```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SharedResourceWithLock {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}

public class LockExample {
    public static void main(String[] args) throws InterruptedException {
        SharedResourceWithLock resource = new SharedResourceWithLock();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                resource.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                resource.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final count: " + resource.getCount());
    }
}
```
위 코드에서는 `ReentrantLock`을 사용하여 `increment()` 메서드의 임계 영역을 보호합니다. `lock.lock()`을 호출하면 다른 스레드는 해당 임계 영역에 접근할 수 없으며, `finally` 블록에서 `lock.unlock()`을 호출하여 잠금을 해제합니다.

## 3. Java의 고유 락 (Intrinsic Lock)
Java의 `synchronized` 키워드를 사용하면 해당 객체의 **고유 락(Intrinsic Lock)** 이 자동으로 관리됩니다.

### 고유 락을 활용한 자원 보호 방법
- 모든 Java 객체는 자체적으로 고유 락을 가지고 있으며, `synchronized` 키워드를 사용하면 자동으로 해당 객체의 락을 획득합니다.
- `synchronized` 메서드나 블록이 실행되는 동안, 다른 스레드는 해당 객체의 락을 얻을 수 없습니다.
- 스레드가 메서드 실행을 완료하면 자동으로 락을 해제합니다.

### 예제: 고유 락을 활용한 동기화
```java
class IntrinsicLockExample {
    private int count = 0;
    
    public void increment() {
        synchronized (this) {
            count++;
        }
    }
    
    public int getCount() {
        return count;
    }
}
```
위 코드에서는 `synchronized (this)`를 사용하여 특정 객체(`this`)의 고유 락을 획득하고 보호합니다.

## 4. `synchronized` vs `ReentrantLock` vs 고유 락
| 비교 항목       | `synchronized` | `ReentrantLock` | 고유 락 (Intrinsic Lock) |
|----------------|---------------|----------------|----------------|
| 기본 제공 여부 | 키워드 제공 | 클래스 제공 (`java.util.concurrent.locks.ReentrantLock`) | 자동 제공 |
| 성능 | 단순한 경우 성능 우수 | 복잡한 경우 성능 우수 | 단순한 경우 적합 |
| 기능 | 자동 해제 | 수동 해제 필요 (`unlock()`) | 자동 해제 |
| 조건 대기 | `wait() / notify()` | `await() / signal()` 지원 | `wait() / notify()` 사용 |
| 시도적 잠금 | 지원하지 않음 | `tryLock()` 지원 | 지원하지 않음 |

## 결론
- 단순한 동기화가 필요하다면 `synchronized`를 사용하면 코드가 간결해집니다.
- 보다 정교한 동기화가 필요하거나 `tryLock()` 등의 기능이 필요하다면 `ReentrantLock`을 사용하는 것이 좋습니다.
- Java의 고유 락(Intrinsic Lock)은 `synchronized` 키워드와 함께 사용되며, 기본적인 동기화를 자동으로 처리하는 데 유용합니다.

이와 같은 동기화 기법을 활용하면 멀티스레드 환경에서도 안전한 프로그램을 작성할 수 있으며, 공유 자원의 일관성을 유지할 수 있습니다.

# 참고자료
- [tech-interview-for-developer/Language /Java Intrinsic Lock.md](https://github.com/gyoogle/tech-interview-for-developer/blob/master/Language/%5BJava%5D%20Intrinsic%20Lock.md)