# `JAVA 예외 처리`
예외처리란 프로그램 실행 중 발생할 수 있는 예외를 미리 예측하여 처리하는 것을 의미합니다. 예외를 처리하지 않으면 프로그램이 비정상적으로 종료됩니다. 데이터를 백업하고 정상적으로 프로그램을 종료하기 위해 예외 처리가 필요합니다.


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

## 다형성과 예외
자바에서는 예외도 객체로 간주

![Image](https://github.com/user-attachments/assets/733d888e-edc0-47a5-99fa-00382c4a1f6e)

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

## throws와 throw의 차이
- throws: 메서드에서 발생할 수 있는 예외를 선언하는 키워드
- throw: 예외를 직접 발생시키는 키워드
```java
void method() throws IOException {  // 예외를 선언 (throws)
    throw new IOException("예외 발생"); // 예외를 실제로 발생 (throw)
}
```
## 계층적 예외 처리
메소드 안에서 예외가 발생하면 런타임 시스템은 예외 처리기를 검색
하고 예외 처리기가 없다면 호출스택(call stack)에 있는 상위메소드를 조사

![Image](https://github.com/user-attachments/assets/388516b3-14c9-468a-9f55-236019003d17)

![Image](https://github.com/user-attachments/assets/d5b1b256-49f8-4909-a56d-fc29047ba8f5)


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

