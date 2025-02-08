# `Java에서 스레드 동기화 (Synchronization)`

멀티스레드 환경에서는 여러 스레드가 동시에 공유 자원에 접근하면서 예상치 못한 문제가 발생할 수 있습니다. 이를 방지하기 위해 **임계 영역(Critical Section)과 잠금(Lock)**을 활용하여 동기화를 수행할 수 있습니다.

<br>

<br>

## 스레드 동기화 방법

- 임계 영역(critical section) : 공유 자원에 단 하나의 스레드만 접근하도록(하나의 프로세스에 속한 스레드만 가능)
- 뮤텍스(mutex) : 공유 자원에 단 하나의 스레드만 접근하도록(서로 다른 프로세스에 속한 스레드도 가능)
- 이벤트(event) : 특정한 사건 발생을 다른 스레드에게 알림
- 세마포어(semaphore) : 한정된 개수의 자원을 여러 스레드가 사용하려고 할 때 접근 제한
- 대기 가능 타이머(waitable timer) : 특정 시간이 되면 대기 중이던 스레드 깨움

<br>


<br>

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

<br>

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

<br>

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

<br>

## 4. `synchronized` vs `ReentrantLock` vs 고유 락
| 비교 항목       | `synchronized` | `ReentrantLock` | 고유 락 (Intrinsic Lock) |
|----------------|---------------|----------------|----------------|
| 기본 제공 여부 | 키워드 제공 | 클래스 제공 (`java.util.concurrent.locks.ReentrantLock`) | 자동 제공 |
| 성능 | 단순한 경우 성능 우수 | 복잡한 경우 성능 우수 | 단순한 경우 적합 |
| 기능 | 자동 해제 | 수동 해제 필요 (`unlock()`) | 자동 해제 |
| 조건 대기 | `wait() / notify()` | `await() / signal()` 지원 | `wait() / notify()` 사용 |
| 시도적 잠금 | 지원하지 않음 | `tryLock()` 지원 | 지원하지 않음 |
# `Java에서 스레드 동기화 (Synchronization)`

멀티스레드 환경에서는 여러 스레드가 동시에 공유 자원에 접근하면서 예상치 못한 문제가 발생할 수 있습니다. 이를 방지하기 위해 **임계 영역(Critical Section)과 잠금(Lock)**을 활용하여 동기화를 수행할 수 있습니다.



<br>

## 1. 임계 영역 (Critical Section)
임계 영역은 여러 스레드가 동시에 접근하면 안 되는 공유 자원을 보호하는 코드 블록입니다. Java에서는 `synchronized` 키워드를 사용하여 임계 영역을 설정할 수 있습니다.

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
```



<br>

## 2. Synchronized 문제점 - **적은 병행성**
`synchronized`는 선언만으로 간단하게 사용할 수 있지만, 다음의 단점이 있습니다:
- `synchronized` 메서드나 블록 내에서는 한 번에 하나의 스레드만 접근 가능하기 때문에 **병렬 실행이 제한**됩니다.
- 특정 조건에서는 대기 시간이 길어지고 성능 저하가 발생할 수 있습니다.



<br>

## 3. ReEntrantLock를 통한 Enter Locks
Java에서는 보다 세밀한 제어가 필요할 경우 `java.util.concurrent.locks.ReentrantLock`을 이용할 수 있습니다.

### 예제: `ReentrantLock`을 사용한 동기화
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
```



<br>

## 4. 원자 클래스 소개 - `AtomicInteger`
Java의 `java.util.concurrent.atomic` 패키지는 원자적(Atomic) 연산을 제공하여 동기화 없이도 스레드 안전성을 보장합니다.

### 예제: `AtomicInteger`
```java
import java.util.concurrent.atomic.AtomicInteger;

class AtomicExample {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
```

**장점**: `AtomicInteger`는 락이 필요 없으며, 성능적으로 효율성이 높습니다.



<br>

## 5. ConcurrentMap의 필요성
기존의 `HashMap`이나 `Hashtable`은 멀티스레드 환경에서 제한적입니다:
- `HashMap`: 스레드 안전하지 않아 동기화가 필요합니다.
- `Hashtable`: 스레드 안전하지만, 모든 메서드가 동기화되어 병렬 처리 성능이 낮아집니다.

### 해결책: `ConcurrentMap`
Java에서는 `java.util.concurrent.ConcurrentMap` 인터페이스를 통해 병렬 안전성을 제공하는 컬렉션을 구현합니다.



<br>

## 6. ConcurrentHashMap을 통한 예시 구현
`ConcurrentHashMap`은 `HashMap`과 유사하지만, 멀티스레드 환경에서 병렬 처리를 지원하기 때문에 성능과 스레드 안전성을 모두 제공합니다.

### 예제: `ConcurrentHashMap`
```java
import java.util.concurrent.ConcurrentHashMap;

class ConcurrentHashMapExample {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        map.put("Key1", 1);
        map.put("Key2", 2);

        map.putIfAbsent("Key3", 3); // 키가 없을 경우에만 추가
        System.out.println(map);
    }
}
```



<br>

## 7. ConcurrentHashMap은 다른 lock과 다른 부분에서 쓰임
`ConcurrentHashMap`은 **분할된 락 메커니즘(Segment Locking)**을 사용합니다:
- 전체 맵이 아닌, **각 버킷** 수준에서 락을 사용하여 동시성을 향상시킵니다.
- 읽기 작업은 락을 사용하지 않으므로 성능이 크게 향상됩니다.

### 장점
- 읽기 작업(`get`)은 거의 제한 없이 병렬 실행됩니다.
- 쓰기 작업(`put`, `remove`)은 맵의 특정 부분에만 락을 걸어, 전체 락을 피합니다.



<br>

## 8. CopyOnWrite Concurrent 컬렉션 - 읽기가 쓰기보다 많을 때
Java에는 읽기 작업이 월등히 많은 환경에서 쓰기 작업보다 높은 성능을 보이는 `CopyOnWrite` 컬렉션이 존재합니다.

### 주요 클래스
- `CopyOnWriteArrayList`
- `CopyOnWriteArraySet`

### 예제: `CopyOnWriteArrayList`
```java
import java.util.concurrent.CopyOnWriteArrayList;

class CopyOnWriteExample {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        list.add("Element1");
        list.add("Element2");

        for (String element : list) {
            System.out.println(element);
        }
    }
}
```

**장점**:
- 쓰기 시 전체 데이터를 복사하여 새로운 데이터를 추가합니다.
- 대신 **읽기 중 Lock을 사용하지 않으므로 매우 높은 스레드 안전성**이 제공됩니다.



<br>

## 요약
Java는 다양한 방법으로 멀티스레드 환경에서의 동기화를 지원합니다:
1. `synchronized` 키워드: 간단하지만 병렬성에 제한.
2. `ReentrantLock`: 더 정교한 동기화 제어.
3. 원자적 클래스(`AtomicInteger`): 락이 없는 스레드 안전성을 제공.
4. `ConcurrentHashMap`: 병렬 처리를 위한 맵 컬렉션.
5. `CopyOnWrite` 컬렉션: 읽기 중심 작업 환경에서 성능 우위.

최적의 동기화 방법을 선택하려면, **동시성 요구사항과 성능이 중요한 작업 패턴**을 고려해야 합니다.
<br>

## 결론
- 단순한 동기화가 필요하다면 `synchronized`를 사용하면 코드가 간결해집니다.
- 보다 정교한 동기화가 필요하거나 `tryLock()` 등의 기능이 필요하다면 `ReentrantLock`을 사용하는 것이 좋습니다.
- Java의 고유 락(Intrinsic Lock)은 `synchronized` 키워드와 함께 사용되며, 기본적인 동기화를 자동으로 처리하는 데 유용합니다.

이와 같은 동기화 기법을 활용하면 멀티스레드 환경에서도 안전한 프로그램을 작성할 수 있으며, 공유 자원의 일관성을 유지할 수 있습니다.

# 참고자료
- [tech-interview-for-developer/Language /Java Intrinsic Lock.md](https://github.com/gyoogle/tech-interview-for-developer/blob/master/Language/%5BJava%5D%20Intrinsic%20Lock.md)