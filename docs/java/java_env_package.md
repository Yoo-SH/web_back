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

