# `JAVA 자료형`

<br>

## 이름 규칙

- 변수명, 메서드명
    - 영문 소문자로시작
    - 2단어 이상 결합시에는새단어첫글자를대문자로(camel case)
    -  myWork, maxValue, bestPosition, ourClassNum
- 상수명
    - 대문자료 표기, 단어 여러 개결합시각각밑줄(_) 사용
    - MY_DATA




<br>

## JAVA 자료형 구분

- 기본 자료형(Primitive Type)
    - 정수형: byte, short, int, long
    - 실수형: float, double (참고: [bigDecimal class-부동소수점연산](https://github.com/Yoo-SH/web_back/blob/main/docs/java/java_bigdecimal.md))
  - 문자형: char(참고:[char 자료형과 관련된 메서드](https://github.com/Yoo-SH/web_back/blob/main/docs/java/java_char.md))
  - 논리형: boolean
- 참조 자료형(Reference Type)
    - 클래스, 인터페이스, 배열 등

<img src="https://github.com/user-attachments/assets/bce2e7b8-65cf-4a23-b030-5d0f20fd4521" width="500">
<br>

## 자료형과 메모
- 메모리의3가지영역
    - 클래스(class)/정적(static)/상수(final)/메서드(method) 영역
    - 스택(stack) 영역: 변수 저장
    - 힙(heap) 영역: 객체 저장리

<img src="https://github.com/user-attachments/assets/a14d0b8b-842c-4dcb-8a25-887385757386" width="500">

<br>

## 기본자료형의 메모리크기 및 저장값의 범위

<img src="https://github.com/user-attachments/assets/f7ef5978-9cfb-4d64-a6d0-6281d2f53e81" width="500">

참고 사항
- byte: -128 ~ 127  
- float: 대략 7자리까지 표현 가능
- double: 대략 15자리까지 표현 가능

<br>

## 리터럴(literal)과 변수(variable)

__1. 리터럴(Literal)__
- "고정된 값" 그 자체를 의미함.
- 메모리의 특정 공간을 차지하지 않음.
- 변하지 않는 값이며, 코드에서 직접 사용됨.
- 사용자가 직접 입력한 값이나, 프로그램에서 생성한 값이 될 수 있음.

__2. 변수(Variable)__
- 값을 저장할 수 있는 메모리 공간 (즉, 리터럴을 담는 그릇).
- 값을 변경할 수 있음.
- 특정 데이터 타입(int, double, String 등)이 있어야 함.

```java
int a = 10; // 10은 리터럴, a는 변수
```

<br>

## JAVA 명시적 타입 변환 (Explicit Type Conversion, Casting)

명시적 형변환은 리터럴 자료형을 개발자가 특정 자료형으로 직접 변환을 지정하는 것을 말합니다.

- float : (float), f, F
- long : (long), l, L 

* 참고
    - (float) 캐스팅을 쓰면 기존 자료형으로 인식했다가 변환하는 과정이 추가됨.
    - 3.14f처럼 f를 붙이면 애초부터 float 리터럴로 인식되므로 변환이 필요 없음.
    - f, F는 서로 동일한 의미를 지님. l, L도 마찬가지.

특정 조건을 갖추지 못했지만, 형변환을 하고 싶을때 사용하는 것이 Casting (강제 형변환)입니다.

```java
int intValue = 1;
byte byteValue = intValue;
```

위의 경우 intValue에 저장된 1이라는 값은 byte 데이터 타입에도 저장 가능한 값입니다. 그렇지만, 위 코드를 실행하면 컴파일 에러가 발생합니다. 그 이유는 저장될 값 1에 상관없이 int 데이터 타입이 byte 데이터 타입보다 메모리 크기가 크기 때문입니다. 그림으로 보자면,

<img src="https://github.com/user-attachments/assets/e91e7aca-bb3c-46f9-bd08-c9c5fcb55604" width="500">

이와 같은 그림이 나옵니다. int 데이터 타입에 저장된 정수 1의 값은 실제 메모리에 저장될 때 00000000 00000000 00000000 00000001 의 값을 가집니다. 이 값을 byte 데이터 타입에 끝에 1byte(00000001) 영역만 넣자니 앞에있는 3byte (00000000 00000000 00000000) 값이 날아갑니다. 그림으로 보면 이렇습니다.

<img src="https://github.com/user-attachments/assets/8905c3ae-0a33-4490-86a2-3db3e89a5921" width="500">

앞에 3byte의 공간을 삭제하는 시점에서 많은 데이터가 날아가 정상적인 값이 저장될 수 없을 것입니다. 이와 같이 메모리 크기가 큰 int 데이터 타입에서 메모리 크기가 작은 byte 데이터 타입으로 자동 형변환(Promotion)이 된다면, 정상적이지 않은 값이 나올 수 있기 때문에 Java에서 자동 형변환(Promotion)을 하지 않습니다. 하지만, 우리가 형변환 하려는 정수 값은 1 이므로 byte 데이터 타입 범위 안에 충분히 들어가는 값입니다. 우린 그걸 머릿속으로 알고 있기 때문에 byte 데이터 타입으로 변환된다 하더라도 값이 정상적일 거라고 판단할 수 있습니다. 이럴 때 강제 형변환은 아래와 같이 해주시면 됩니다.

```java
int intValue = 1;
byte byteValue = (byte) intValue;
```

<br>


<br>

## Java 자동 타입 변환 (Implicit Type Conversion, Promotion)

Java에서는 작은 크기의 데이터 타입에서 더 큰 크기의 데이터 타입으로 변환될 때 **자동 타입 변환(Implicit Type Conversion, Type Promotion)**이 발생합니다. 이는 **데이터 손실 없이** 안전하게 변환될 수 있을 때만 이루어집니다.

### 1. 자동 타입 변환 규칙
자동 타입 변환이 가능한 기본 데이터 타입 간의 변환 순서는 다음과 같습니다.

```text
byte → short → int → long → float → double
       char → int → long → float → double
```

즉, **왼쪽에서 오른쪽으로 변환될 때는 자동 변환**이 일어나지만, 반대 방향은 **명시적 타입 변환(Casting)**이 필요합니다.


### 2. 자동 타입 변환 예제
#### (1) 정수형 변환
```java
public class TypeConversion {
    public static void main(String[] args) {
        byte byteVal = 10;
        short shortVal = byteVal;  // byte → short 자동 변환
        int intVal = shortVal;     // short → int 자동 변환
        long longVal = intVal;     // int → long 자동 변환

        System.out.println("byte: " + byteVal);
        System.out.println("short: " + shortVal);
        System.out.println("int: " + intVal);
        System.out.println("long: " + longVal);
    }
}
```

#### (2) 정수에서 실수로 변환
```java
public class TypeConversion {
    public static void main(String[] args) {
        int intVal = 100;
        float floatVal = intVal;   // int → float 자동 변환
        double doubleVal = floatVal; // float → double 자동 변환

        System.out.println("int: " + intVal);
        System.out.println("float: " + floatVal);
        System.out.println("double: " + doubleVal);
    }
}
```

### 3. char 타입의 자동 변환
`char` 타입은 내부적으로 **유니코드(Unicode) 값을 저장하는 2바이트(0~65535) 정수형**이므로 `int`로 변환될 수 있습니다.

```java
public class TypeConversion {
    public static void main(String[] args) {
        char charVal = 'A';  // 'A'의 유니코드 값: 65
        int intVal = charVal; // char → int 자동 변환

        System.out.println("char: " + charVal);
        System.out.println("int: " + intVal);  // 65 출력
    }
}
```

### 4. 자동 타입 변환이 일어나는 상황
#### (1) 연산 과정에서 발생하는 자동 변환
산술 연산에서 작은 크기의 데이터 타입은 큰 크기의 타입으로 자동 변환됩니다.
```java
public class TypeConversion {
    public static void main(String[] args) {
        byte a = 10;
        byte b = 20;
        int sum = a + b; // byte + byte → int 자동 변환

        System.out.println("sum: " + sum);
    }
}
```
> **주의!**  
> `byte`, `short`, `char` 타입은 연산 시 **int** 타입으로 자동 변환됩니다.

#### (2) 메서드 호출 시 발생하는 자동 변환
메서드 호출 시 인수(argument)의 타입이 매개변수(parameter)의 타입보다 작은 경우 자동 변환이 발생합니다.
```java
public class TypeConversion {
    public static void printDouble(double value) {
        System.out.println("double: " + value);
    }

    public static void main(String[] args) {
        int intVal = 42;
        printDouble(intVal); // int → double 자동 변환
    }
}
```

### 5. 자동 변환이 불가능한 경우 (명시적 변환 필요)
자동 변환이 **불가능한 경우**, 개발자가 **명시적 타입 변환(강제 캐스팅, Explicit Casting)**을 해야 합니다.

```java
public class TypeConversion {
    public static void main(String[] args) {
        double doubleVal = 10.99;
        int intVal = (int) doubleVal;  // 명시적 형변환 (소수점 이하 손실)
        
        System.out.println("double: " + doubleVal);
        System.out.println("int: " + intVal); // 10 출력
    }
}
```
> **강제 변환 시 데이터 손실 가능!**  
> `double`을 `int`로 변환하면 소수점 이하 값이 **손실**됩니다.


### 6. 자동 타입 변환 정리
✅ 작은 크기의 데이터 타입 → 큰 크기의 데이터 타입 변환 시 자동 변환 발생  
✅ 연산 과정에서 작은 데이터 타입은 큰 데이터 타입으로 변환됨  
✅ `char`는 `int`로 변환될 수 있음  
✅ `byte`, `short`, `char`는 연산 시 **int**로 변환됨  
✅ [큰 크기의 데이터 타입 → 작은 크기의 데이터 타입 변환 시 **명시적 캐스팅 필요**]()  

