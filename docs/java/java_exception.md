# `JAVA 예외처리`

<br>

## Error와 Exception 차이

Error와 Exception은 같다고 생각할 수도 있지만 사실 큰 차이가 있습니다.

**Error** 는 컴파일 시 문법적인 오류와 런타임 시 널포인트 참조와 같은 오류로 프로세스에 심각한 문제를 야기 시켜 프로세스를 종료 시킬 수 있습니다.

**Exception** 프로그램 실행 중 발생할 수 있는 예외를 미리 예측하여 처리하는 것을 의미합니다. 예외를 처리하지 않으면 프로그램이 비정상적으로 종료됩니다. 데이터를 백업하고 정상적으로 프로그램을 종료하기 위해 예외 처리가 필요합니다.

프로그램이 실행 중 어떤 원인에 의해서 오작동을 하거나 비정상적으로 종료되는 경우를 프로그램 오류라 하고, 프로그램 오류에는 에러(error)와 예외(exception) 두 가지로 구분할 수 있습니다. 에러는 메모리 부족이나 스택오버플로우와 같이 발생하면 복구할 수 없는 심각한 오류이고, 예외는 발생하더라도 수습할 수 있는 비교적 덜 심각한 오류입니다. 이 예외는 프로그래머가 적절히 코드를 작성해주면 비정상적인 종류를 막을 수 있습니다.

Error의 상황을 미리 미연에 방지하기 위해서 Exception 상황을 만들 수 있으며, java에서는 try-catch문으로 Exception handling을 할 수 있습니다.

### Error (에러)

<img src="https://github.com/user-attachments/assets/4d90182c-f279-4782-b9d3-fefccf5c2eae" width="500">

Error는 시스템 레벨에서 발생하여, 개발자가 어떻게 조치할 수 없는 수준을 의미합니다.

- OutOfMemoryError : JVM에 설정된 메모리의 한계를 벗어난 상황일 때 발생합니다. 힙 사이즈가 부족하거나, 너무 많은 class를 로드할때, 가용가능한 swap이 없을때, 큰 메모리의 native메소드가 호출될 때 등이 있습니다. 이를 해결하기위해 dump 파일분석, jvm 옵션 수정 등이 있습니다.

<br>

<br>

## Exception (예외)

<img src="https://github.com/user-attachments/assets/30277f2d-4f22-4a65-98c5-1effafb5b026" width="500">

예외는 개발자가 구현한 로직에서 발생하며 개발자가 다른 방식으로 처리가능한 것들로 JVM은 정상 동작합니다.

<br>

<br>

## Exception의 2가지 종류

1. Checked Exception : 반드시 예외처리를 해야하며, 처리하지 않으면 컴파일되지 않습니다. JVM 외부와 통신(네트워크, 파일시스템 등)할 때 주로 쓰입니다.

    - RuntimeException 이외에 있는 모든 예외
    - IOException, SQLException 등

2. Unchecked Exception : 컴파일 때 체크되지 않고, Runtime에 발생하는 Exception을 말합니다.

    - RuntimeException 하위의 모든 예외
    - NullPointerException, IndexOutOfBoundException 등



<br>

## Throwable 클래스

<img src="https://github.com/user-attachments/assets/7708ac96-0041-4815-9655-39441b12a2dc" width="500">

Throwable 클래스는 예외처리를 할 수 있는 최상위 클래스입니다. Exception과 Error는 Throwable의 상속을 받습니다.


<br>

## 대표적인 Exception Class

- NullPointerException : Null 레퍼런스를 참조할때 발생, 뭔가 동작시킬 때 발생합니다.
- IndexOutOfBoundsException : 배열과 유사한 자료구조(문자열, 배열, 자료구조)에서 범위를 벗어난 인덱스 번호 사용으로 발생합니다.
- FormatException : 문자열, 숫자, 날짜 변환 시 잘못된 데이터(ex. "123A" -> 123 으로 변환 시)로 발생하며, 보통 사용자의 입력, 외부 데이터 로딩, 결과 데이터의 변환 처리에서 자주 발생합니다.
- ArthmeticException : 정수를 0으로 나눌때 발생합니다.
- ClassCastException : 변환할 수 없는 타입으로 객체를 변환할 때 발생합니다.
- IllegalArgumentException : 잘못된 인자 전달 시 발생합니다.
- IOException : 입출력 동작 실패 또는 인터럽트 시 발생합니다.
- IllegalStateException : 객체의 상태가 매소드 호출에는 부적절한 경우에 발생합니다.
- ConcurrentModificationException : 금지된 곳에서 객체를 동시에 수정하는것이 감지될 경우 발생합니다.
- UnsupportedOperationException : 객체가 메소드를 지원하지 않는 경우 발생합니다.

<br>

<br>

## 주요 Method

- printStackTrace() : 발생한 Exception의 출처를 메모리상에서 추적하면서 결과를 알려줍니다. 발생한 위치를 정확히 출력해줘서 제일 많이 쓰며 void를 반환합니다.
- getMessage() : 한줄로 요약된 메세지를 String으로 반환해줍니다.
- getStackTrace() : jdk1.4 부터 지원, printStackTrace()를 보완, StackTraceElement[] 이라는 문자열 배열로 변경해서 출력하고 저장합니다.

<br>



<br>

## try-catch-finally 구문
예외 처리를 위해 try-catch-finally 구문을 사용합니다. try 블록에서 예외가 발생하면 catch 블록에서 예외를 처리하고, finally 블록에서는 예외 발생 여부와 상관없이 항상 실행되는 코드를 작성합니다.


```java
try {
    // 예외가 발생할 수 있는 코드
} catch (Exception e) {
    // 예외 처리 코드
} finally {
    // 항상 실행되는 코드(생략 가능)
    // 일반적으로 자원을 해제하는 코드를 작성 out.close();
}
```

<br>

## try-with-resources 구문

try-with-resources 구문은 자바 7부터 추가된 기능으로, 자원을 명시적으로 해제하지 않아도 자동으로 자원을 해제하는 구문입니다. try 블록에서 자원을 생성하면 자동으로 자원을 해제하며, finally 블록에서 자원을 해제하는 코드를 작성하지 않아도 됩니다.

__특징__
- try 블록 내에서 선언된 리소스(resource)는 자동으로 close()가 호출됩니다.
- catch 블록에서 예외가 발생해도 finally 블록을 사용하지 않고도 리소스가 자동으로 해제됩니다.
- AutoCloseable 또는 Closeable 인터페이스를 구현한 객체만 사용할 수 있습니다.

__사용법__
```java
try (ResourceType resource = new ResourceType()) {
    // 리소스를 사용한 작업 수행
} catch (Exception e) {
    // 예외 처리
}
```
__예시__
```java
//BufferedReader와 FileReader는 Closeable 인터페이스를 구현하고 있어 try-with-resources에서 사용 가능.
// try 블록이 종료될 때 자동으로 br.close()가 호출됨.
//명시적인 finally 블록 없이도 자원을 자동으로 해제할 수 있음.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TryWithResourcesExample {
    public static void main(String[] args) {
        // try-with-resources 사용
        try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

<br>

## 다형성과 예외
자바에서는 예외도 객체로 간주

<img src="https://github.com/user-attachments/assets/733d888e-edc0-47a5-99fa-00382c4a1f6e" width="500">

```java
try{
    getInput();
 }
 catch(Exception e) {
    //Exception의모든하위클래스를잡을수있으나분간할수없다.!
 }
```

```java
try{
    getInput();// 예외를발생하는메소드
}
catch(NumberException e) {
    // NumberException의하위클래스를모두잡을수있다.
}
```

```java
try{
    getInput();
}
catch(TooSmallException e) {
    //TooSmallException만잡힌다. 
}
catch(NumberException e) {
    //TooSmallException을제외한나머지예외들이잡힌다. 
}
```

<br>

## throws 키워드
throws는 Java에서 예외(Exception)를 선언하는 키워드로, 메서드의 시그니처에서 사용됩니다. 특정 메서드가 실행 중에 예외를 발생시킬 가능성이 있음을 호출하는 측에 알리는 역할을 합니다.


__throws 키워드 개념__
- 메서드가 호출될 때 발생할 수 있는 예외를 선언합니다.
- 예외를 직접 처리(try-catch)하는 것이 아니라, 호출한 메서드로 예외 처리를 위임합니다. 

__throws 예시__
```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ThrowsExample {
    
    // 파일을 읽는 메서드 - 예외를 호출한 쪽(main 메서드)에서 하도록 위임
    public void readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine(); // IOException 발생 가능
        System.out.println("파일 내용: " + line);
        reader.close();
    }

    // main 메서드에서 try-catch를 사용하여 예외 처리
    public static void main(String[] args) {
        ThrowsExample example = new ThrowsExample();
        try {
            example.readFile("nonexistent.txt"); // 예외 발생 가능성 있음
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류 발생: " + e.getMessage());
        }
    }
}
```
<br>

## thorw 키워드

throw는 예외를 직접 발생시키는 키워드로, 메서드에서 예외를 발생시킬 때 사용됩니다. throw 키워드를 사용하여 예외를 발생시키면, 예외를 처리하는 측에 예외를 전달할 수 있습니다.

__throw 키워드 특징__
- throw 문장은 하나의 파라미터를 요구
- 파라미터는 Throwable 클래스의 객체여야 함

__throwable 객체__
- EmptyStackException, IOException, SQLException 등

__throw 예시__
```java

public class ThrowExample {
    public void checkAge(int age) {
        if (age < 18) {
            throw new ArithmeticException("미성년자는 입장할 수 없습니다.");
        } else {
            System.out.println("성인입니다.");
        }
    }

    public static void main(String[] args) {
        ThrowExample example = new ThrowExample();
        example.checkAge(15); // 예외 발생
    }
}
```

<br>

## throws와 throw의 차이
- throws: 메서드에서 발생할 수 있는 예외를 선언하는 키워드
- throw: 예외를 직접 발생시키는 키워드
```java
void method() throws IOException {  // 예외를 선언 (throws)
    throw new IOException("예외 발생"); // 예외를 실제로 발생 (throw)
}
```


<br>

<br>

## 계층적 예외 처리
메소드 안에서 예외가 발생하면 런타임 시스템은 예외 처리기를 검색
하고 예외 처리기가 없다면 호출스택(call stack)에 있는 상위메소드를 조사

<img src="https://github.com/user-attachments/assets/388516b3-14c9-468a-9f55-236019003d17" width="500">

<img src="https://github.com/user-attachments/assets/d5b1b256-49f8-4909-a56d-fc29047ba8f5" width="500">


<br>

## 예외처리 가독성

__예외처리 사용하지 않는 경우__
```java
errorCodeType readFile() {
    interrorCode = 0;
    // 파일을오픈한다; 
    if(theFileIsOpen) {
        // 파일의크기를결정한다; 
        if(gotTheFileLength) {
            // 메모리를할당한다;
            if(gotEnoughMemory) {
                // 파일을메모리로읽는다;
                if(readFailed) {
                    errorCode = -1;
                }
            } else{
                errorCode = -2;
            }
        } else{
            errorCode = -3;
        }
        ...
    }
}

```
__예외처리 사용 하는 경우__
```java
voidreadFile() {
 try{
    //파일을오픈한다; 
    //파일의크기를결정한다; 
    //메모리를할당한다;
    //파일을메모리로읽는다;
    // 파일을닫는다;
 } catch(fileOpenFailed) {
 ...
 } catch(sizeDeterminationFailed) {
 ...
 } catch(memoryAllocationFailed) {
 ...
 } catch(readFailed) {
 ...
 } catch(fileCloseFailed) {
 ...
 }
}
 ```

<br>

## assert문

assert문은 프로그램의 상태를 검증하는 데 사용되며, 프로그램이 올바르게 동작하는지 확인하는 데 유용합니다. `assert문은 디버깅 목적으로 사용`되며, 프로그램이 실행 중에 검증할 조건을 지정하여 조건이 참이 아닌 경우 AssertionError 예외를 발생시킵니다.

__사용법__
- assert문은 JDK 1.4부터 추가되었으며, JDK 1.4 이상에서 사용 가능합니다.
- -ea 옵션을 사용하여 assert문을 활성화할 수 있으며, assert문은 기본적으로 비활성화되어 있습니다.

__문법__
```java
assert 조건식; // 조건식이 거짓인 경우 AssertionError 발생
assert 조건식 : 표현식; // 조건식이 거짓인 경우 표현식을 출력
```

__예시__
```java
public class AssertExample {
    public static void main(String[] args) {
        int x = 10;
        assert x > 0; //참이면 아무일도 일어나지 않고 거짓이면 AssertionError 발생
        System.out.println("x는 0보다 큼");
    }
}
```

# 참고자료
- [tech-refrigerator/Language/JAVA /Error & Exception.md](https://github.com/GimunLee/tech-refrigerator/blob/master/Language/JAVA/Error%20%26%20Exception.md#goal)