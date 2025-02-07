# `JAVA 스레드`

## 1. 스레드(Thread)란?
스레드(Thread)는 프로세스 내에서 실행되는 세부 작업 단위로, 하나의 프로세스에는 여러 개의 스레드가 존재할 수 있습니다. 스레드는 프로세스 내의 주소 공간이나 자원을 공유하며, 동시에 여러 작업을 수행할 수 있습니다.

## 프로세스와 스레드의 차이
__프로세스__
 - 메모리가할당된프로그램
 - 프로세스제어블록(process control block, PCB) 내에 데이터 저장

__스레드__
 - 프로세스를구성하는sub-프로세스
 - PCB 데이터를공유

![Image](https://github.com/user-attachments/assets/fd7e9f81-394e-4f33-bbf1-bb845179a589)
## 2. 멀티 스레드를 사용하는 이유
1. 웹 브라우저에서 웹페이지를 보면서 동시에 파일을 다운로드할 수 있음
2. 워드 프로세서에서 문서를 편집하면서 동시에 인쇄
3. GUI에서는 마우스와 키보드 입력을 다른 스레드를 생성하여 처리
4. 프로세스 문맥교환(context switching) 보다 스레드 문맥 교환의 비용이 작음

## 3. 스레드 생성과 실행

### 3-1. Thread 클래스를 상속받아 스레드 생성
1. Thread 클래스를 상속받기 
2. run 메서드를 오버라이딩하여 재정의하여 실행할 코드 작성
3. start() 메서드를 호출하여 스레드 시작

__스레드 생성 예시__
```java
 package main;

 class MyThread extends Thread {
    public void run() {
        for (int i = 10; i >= 0; i--)
            System.out.print(i + " ");
    }
 }
```
__스레드 실행 예시__
```java
 package main;

 public class Test {
    public static void main(String args[]) {
        MyThread t = new MyThread();
        t.start(); // 10 9 8 7 6 5 4 3 2 1 0
    }
 }
```

### 3-2. Runnable 인터페이스를 구현하여 스레드 생성
1. Runnable 인터페이스를 구현
2. run 메서드를 오버라이딩하여 메서드를 구현
3.  Thread 객체를 생성하고  Runnable 인터페이스를 구현한 객체를 인수로 전달
4. start() 메서드를 호출하여 스레드 시작

__스레드 생성 예시__
```java
 package main;

 class MyRunnable implements Runnable {
    public void run() {
        for (int i = 10; i >= 0; i--)
            System.out.print(i + " ");
    }
 }
```
__스레드 실행 예시__
```java
 package main;

 public class Test {
    public static void main(String args[]) {
        Thread m = new Thread(new MyRunnable);
        t.start(); // 10 9 8 7 6 5 4 3 2 1 0
    }
 }
```

## 4. Thread 클래스 상속 vs Runnable 인터페이스 구현
자바에서는 다중상속이 불가능 하므로 일반적으로 Runnable  인터페이스를 사용하는 것을 권장(Runnable 인터페이스를 사용하면 고수준의 스레드관리 API도 사용가능)

## 5.스레드의 실행제어

> 스레드의 상태는 5가지가 있다

- NEW : 스레드가 생성되고 아직 start()가 호출되지 않은 상태
- RUNNABLE : 실행 중 또는 실행 가능 상태
- BLOCKED : [동기화]((https://github.com/Yoo-SH/web_back/blob/main/docs/java/java_synchronization.md)) 블럭에 의해 일시정지된 상태(lock이 풀릴 때까지 기다림)
- WAITING, TIME_WAITING : 실행가능하지 않은 일시정지 상태
- TERMINATED : 스레드 작업이 종료된 상태

<br>

스레드로 구현하는 것이 어려운 이유는 바로 동기화와 스케줄링 때문이다.

스케줄링과 관련된 메소드는 sleep(), join(), yield(), interrupt()와 같은 것들이 있다.

start() 이후에 join()을 해주면 main 스레드가 모두 종료될 때까지 기다려주는 일도 해준다.

<br>

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

# `Thread 동기화`

멀티스레드로 구현을 하다보면, 동기화는 필수적이다.

동기화가 필요한 이유는, **여러 스레드가 같은 프로세스 내의 자원을 공유하면서 작업할 때 서로의 작업이 다른 작업에 영향을 주기 때문**이다.

스레드의 동기화를 위해선, 임계 영역(critical section)과 잠금(lock)을 활용한다.

임계영역을 지정하고, 임계영역을 가지고 있는 lock을 단 하나의 스레드에게만 빌려주는 개념으로 이루어져있다.

따라서 임계구역 안에서 수행할 코드가 완료되면, lock을 반납해줘야 한다.

<br>

# 스레드 동기화 방법

- 임계 영역(critical section) : 공유 자원에 단 하나의 스레드만 접근하도록(하나의 프로세스에 속한 스레드만 가능)
- 뮤텍스(mutex) : 공유 자원에 단 하나의 스레드만 접근하도록(서로 다른 프로세스에 속한 스레드도 가능)
- 이벤트(event) : 특정한 사건 발생을 다른 스레드에게 알림
- 세마포어(semaphore) : 한정된 개수의 자원을 여러 스레드가 사용하려고 할 때 접근 제한
- 대기 가능 타이머(waitable timer) : 특정 시간이 되면 대기 중이던 스레드 깨움

<br>

## synchronized 활용

> synchronized를 활용해 임계영역을 설정할 수 있다.

서로 다른 두 객체가 동기화를 하지 않은 메소드를 같이 오버라이딩해서 이용하면, 두 스레드가 동시에 진행되므로 원하는 출력 값을 얻지 못한다.

이때 오버라이딩되는 부모 클래스의 메소드에 synchronized 키워드로 임계영역을 설정해주면 해결할 수 있다.

```java
//synchronized : 스레드의 동기화. 공유 자원에 lock
public synchronized void saveMoney(int save){    // 입금
    int m = money;
    try{
        Thread.sleep(2000);    // 지연시간 2초
    } catch (Exception e){

    }
    money = m + save;
    System.out.println("입금 처리");

}

public synchronized void minusMoney(int minus){    // 출금
    int m = money;
    try{
        Thread.sleep(3000);    // 지연시간 3초
    } catch (Exception e){

    }
    money = m - minus;
    System.out.println("출금 완료");
}
```

<br>

## wait()과 notify() 활용

> 스레드가 서로 협력관계일 경우에는 무작정 대기시키는 것으로 올바르게 실행되지 않기 때문에 사용한다.

- wait() : 스레드가 lock을 가지고 있으면, lock 권한을 반납하고 대기하게 만듬

- notify() : 대기 상태인 스레드에게 다시 lock 권한을 부여하고 수행하게 만듬

이 두 메소드는 동기화 된 영역(임계 영역)내에서 사용되어야 한다.

동기화 처리한 메소드들이 반복문에서 활용된다면, 의도한대로 결과가 나오지 않는다. 이때 wait()과 notify()를 try-catch 문에서 적절히 활용해 해결할 수 있다.

```java
/**
* 스레드 동기화 중 협력관계 처리작업 : wait() notify()
* 스레드 간 협력 작업 강화
*/

public synchronized void makeBread(){
    if (breadCount >= 10){
        try {
            System.out.println("빵 생산 초과");
            wait();    // Thread를 Not Runnable 상태로 전환
        } catch (Exception e) {

        }
    }
    breadCount++;    // 빵 생산
    System.out.println("빵을 만듦. 총 " + breadCount + "개");
    notify();    // Thread를 Runnable 상태로 전환
}

public synchronized void eatBread(){
    if (breadCount < 1){
        try {
            System.out.println("빵이 없어 기다림");
            wait();
        } catch (Exception e) {

        }
    }
    breadCount--;
    System.out.println("빵을 먹음. 총 " + breadCount + "개");
    notify();
}
```

조건 만족 안할 시 wait(), 만족 시 notify()를 받아 수행한다.