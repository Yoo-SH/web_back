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

