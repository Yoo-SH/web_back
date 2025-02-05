# `JAVA의 특징`

## 2. JAVA의 특징
Java는 다양한 기능을 제공하는 강력한 프로그래밍 언어입니다. 주요 특징은 다음과 같습니다.

- **객체지향**: Java는 캡슐화, 상속, 다형성과 같은 객체지향 프로그래밍(OOP) 개념을 지원합니다.
- **플랫폼 독립적**: "Write Once, Run Anywhere" 원칙을 따라, 한 번 작성된 Java 코드는 JVM(Java Virtual Machine)이 있는 모든 환경에서 실행될 수 있습니다. JVM은 자바의실행파일.class 를 실행하는 가상의(SW) 컴퓨터이며, JVM이 존재할 경우 운영체제에 상관없이 .class(java 실행 바이트코드 파일)을 실행할 수 있습니다.
- **멀티쓰레드 지원**: Java는 멀티쓰레딩을 지원하여 병렬 처리를 쉽게 구현할 수 있습니다.
- **분산처리 지원**: RMI(Remote Method Invocation) 및 다양한 네트워크 API를 통해 분산 시스템을 개발할 수 있습니다.
- **동적 로딩 지원**: 실행 시간에 필요한 클래스를 동적으로 로드하여 유연한 애플리케이션 개발이 가능합니다.
- **예외 처리 지원**: 강력한 예외 처리 메커니즘을 제공하여 안정적인 프로그램을 작성할 수 있습니다.
- **가비지 컬렉션**: 자동 메모리 관리를 지원하여 개발자가 직접 메모리를 해제할 필요 없이 효율적으로 관리됩니다.


## JAVA 실행환경
* JVM(Java Virtual Machine)은 Java 바이트 코드를 실행하는 가상 머신입니다.
* JRE(Java Runtime Environment)는 Java 프로그램을 실행하기 위한 환경을 제공합니다.
* JDK(Java Development Kit)는 Java 프로그램을 개발하기 위한 도구들을 제공합니다.
![Image](https://github.com/user-attachments/assets/31d79b5a-4bdf-443b-bba6-ba031e5f74e3)


## JAVA 프로젝트 생성 및 실행

![Image](https://github.com/user-attachments/assets/20c152e7-c711-48fd-b6dc-dd8ee5a7100c)

## JAVA 자바소스코드실행과정

__자바프로그램실행과정의4단계__
1. .java 소스 파일 생성
2. .class 바이트 코드 파일 생성
3. javac 커맨드를 통해 컴파일
4. JVM 기반의 메모리 할당, main() 메서드 실행

__JVM 이 사용하는 메모리영역__
- 메서드 영역(클래스/정적/상수), 
- 스택 영역
- 힙 영역

![Image](https://github.com/user-attachments/assets/cd9251d8-34d3-41bc-85d1-9fb5f2a57407)

### JAVA 프로그램 세부 실행과정 
JDK -> JRE -> JVM -> OS -> Hardware

![Image](https://github.com/user-attachments/assets/c2294091-cd2b-4e75-bbe7-3c2bfb547955)


# `JAVA 패키지(package)`

## 1. 패키지(package)란?
비슷한 목적으로 생성된 클래스를 파일을 한곳에 모아둔 폴더로 src 폴더의 하위 폴더에 위치한다. 컴파일이 수행된 후 바이트 코드가 저장되는 bin 폴더에도 생성되며 없어도 문법적으로 문제가 되지 않는다.

__패키지 사용 이유__
1. 클래스 이름의 충돌을 방지할 수 있다.
2. 클래스 파일을 쉽게 찾을 수 있다.

## 2. 임포트(import)란?
다른 패키지 내의 클래스를 사용하기 위한 문법 요소로 패키지 구문 다음 줄에 위치한다.

### 임포트 사용 방법
1. 패키지명을 포함한 특정 클래스명을 사용하는 방법
    
    패키지명.클래스명
    ```java
    package package1;

    package2.MyClass myClass = new package2.MyClass();
    ```
2. 임포트를 사용하여 특정 클래스를 사용하는 방법
    
    import 패키지명.클래스명

    ```java
    package package1;

    import package2.MyClass;

    MyClass myClass = new MyClass();
    ```
3. 임포트를 사영하여 특정 패키지 내의 모든 클래스를 사용하는 방법
    
    import 패키지명.*
    ```java
    package package1;

    import package2.*;

    MyClass myClass = new MyClass();
    ```
__주의사항__
- *기호를 사용하는 경우 하위 폴더는 포함되지 않음
- 하위 폴더를 포함하려면 하위 폴더명을 포함하여 사용해야 함. 예시: import package2.subpackage.*;
- 다른 패키지의 동일한 이름의 클래스를 중복 임포트할 수 없음 => 이 경우에는 패키지명을 포함한 특정 클래스명으로 사용해야 함


### 3. 외부 클래스

1개의 .java 파일에는 1개의 public 클래스만 존재해야 하며, public 클래스의 이름은 .java 파일의 이름과 동일해야 함. 이외의 클래스는 외부 클래스로 판단되며, 외부 클래스는 public을 사용할 수 없음.

외부 클래스의 경우 public을 사용할 수 없으므로 다른 패키지에서 사용할 수 없음. 따라서 외부 클래스를 사용하려면 같은 패키지 내에 있어야 함.

```java
package package1;

public class MyClass {
   
}

class MyOtherClass {
    // 외부 클래스, 다른 패키지에서 임포트로 사용 불가
}
```
### 4. 생성자 접근 지정자

명시적인 생성자가 없을 때 자동으로 추가되는 생성자의 접근 지정자는 클래스의 접근 지정자와 동일하게 설정됨.

```java
public class MyClass {
    //생성자가 존재하지 않을 경우 자동으로 생성되는 생성자
    // => public MyClass() {} // public 생성자
}
```

__유의사항__
- 생성자를 명시적으로 선언하지 않고 클래스의 접근 지정자가 default인 경우, 생성자의 접근 지정자도 default로 설정되어 외부 패키지에서 호출할 수 없음.

