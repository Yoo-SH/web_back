# `JAVA 자료형`

## 이름 규칙

- 변수명, 메서드명
    - 영문 소문자로시작
    - 2단어 이상 결합시에는새단어첫글자를대문자로(camel case)
    -  myWork, maxValue, bestPosition, ourClassNum
- 상수명
    - 대문자료 표기, 단어 여러 개결합시각각밑줄(_) 사용
    - MY_DATA


## JAVA 자료형 구분

- 기본 자료형(Primitive Type)
    - 정수형: byte, short, int, long
    - 실수형: float, double
    - 문자형: char
    - 논리형: boolean
- 참조 자료형(Reference Type)
    - 클래스, 인터페이스, 배열 등

![Image](https://github.com/user-attachments/assets/bce2e7b8-65cf-4a23-b030-5d0f20fd4521)


## 자료형과 메모
- 메모리의3가지영역
    - 클래스(class)/정적(static)/상수(final)/메서드(method) 영역
    - 스택(stack) 영역: 변수 저장
    - 힙(heap) 영역: 객체 저장리

![Image](https://github.com/user-attachments/assets/a14d0b8b-842c-4dcb-8a25-887385757386)

## 기본자료형의 메모리크기 및 저장값의 범위

![Image](https://github.com/user-attachments/assets/f7ef5978-9cfb-4d64-a6d0-6281d2f53e81)

참고 사항
- byte: -128 ~ 127  
- float: 대략 7자리까지 표현 가능
- double: 대략 15자리까지 표현 가능

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

## JAVA 명시적 타입 변환 (Explicit Type Conversion)

명시적 형변환은 리터럴 자료형을 개발자가 특정 자료형으로 직접 변환을 지정하는 것을 말합니다.

- float : (float), f, F
- long : (long), l, L 

* 참고
    - (float) 캐스팅을 쓰면 기존 자료형으로 인식했다가 변환하는 과정이 추가됨.
    - 3.14f처럼 f를 붙이면 애초부터 float 리터럴로 인식되므로 변환이 필요 없음.
    - f, F는 서로 동일한 의미를 지님. l, L도 마찬가지.


## Java 자동 타입 변환 (Implicit Type Conversion)

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
✅ 큰 크기의 데이터 타입 → 작은 크기의 데이터 타입 변환 시 **명시적 캐스팅 필요**  


# `JAVA 참조 자료형`

## 1.배열

배열은 같은 자료형의 데이터를 여러 개 저장할 수 있는 자료구조입니다. 배열은 **고정된 크기**를 가지며, **인덱스(index)**를 통해 각 요소에 접근할 수 있습니다.

- 한 자료형을 묶어 저장하는 참조자료형
- 생성할 때 크기를 지정해야 함
- 한번 크기를 지정하면 변경 불가함

```java 
//표현방법 (둘다 동일한 의미. 1번이 좀 더 일반적)
1. int[] a
2. int a[]
```

null은 참조 자료형의 초기값으로 사용됨

```java
int[] a = null;
```
- 스택 메모리에 위치한 참조자료형 변수의 빈공간을 초기화시 사용가능함
- 힙 메모리의특정위치(번지)를 가리키고 있지 않다는 의미를 내포함(연결된 실제데이터가없음)
![Image](https://github.com/user-attachments/assets/80a2d1bc-f115-4be1-a7d5-05d3f2f1cea1)

### 힙 메모리에 배열 객체 생성하기

- 참조자료형의 실제데이터(객체)는 힙메모리에 생성됨 
- 힙 메모리에 객체를 생성하기 위해 "new" 키워드를 사용함 
- 배열의 객체를 생성할 때는 길이를 지정해야 함

변수 선언과 동시에 배열 객체 생성하기
```java
int[] a = new int[3];
```

변수 선언 후 배열 객체 생성하기(참고: 스택과 달리 힙 메모리에서는 값을 주지않는 경우 강제 초기화가 진행됨)
```java
int[] a;
a = new int[3];
```
![Image](https://github.com/user-attachments/assets/b02a09dd-eb67-4521-b6c2-70d020303936)
new 키워드 없이 배열 객체 생성하기
```java
// new 키워드 없이는 객체 대입을 분리하여 사용할 수 없음
int[] a = {1, 2, 3, 4, 5}; //ok

int[] a;
a = {1, 2, 3, 4, 5}; //error
```

### 참조 자료형 복사 시 주의사항
데이터값을 변경하면 다른 참조변수의 데이터도 변하게됨

![Image](https://github.com/user-attachments/assets/86ecbda9-092e-45da-ae06-c9ea4914692b)

### for-each문을 이용한 배열 요소 출력
배열이나 컬렉션(Collection)등의 집합 객체에서 원소들을 하나씩 꺼내는 과정을 반복하는 구문
```java
int[] a = {1, 2, 3, 4, 5};
for (int num : a) {
    System.out.println(num);
}
// 1 2 3 4 5
```

## 2. 2차원 배열
2차원 배열 선언
```java
// 2차원 배열 선언(모두 동일한 의미)
int[][] a
int a[][]
int[] a[]
```
2차원 배열 초기화
```java
int[][] a = {{1, 2, 3}, {4, 5, 6}};
int[][] a = new int[2][3];
```

2차원 배열 객체 행 성분 생성 후 열 성분 생성하여 초기화
```java
int[][] a = new int[2][];

a[0] = new int[3];
a[0][0] = 1;
a[0][1] = 2;
a[0][2] = 3;

a[1] = new int[3];
a[1][0] = 4;
a[1][1] = 5;
a[1][2] = 6;
```
![Image](https://github.com/user-attachments/assets/52ba556f-bb9a-43ab-9c07-775f20f204da)


## 3. String 
String은 문자열을 저장하는 참조 자료형, 문자열은 큰 따옴표로 묶어서 표현함
- 한번 정의한 문자열을 변경할 수 없음
- 리터럴을 바로 입력한 데이터는 문자열이 같을 때 객체를 공유함
- string 객체와 + 사용하여 연산하면 다른 string 객체를 생성함

### 힙 메모리에 String 객체 생성하기
new 키워드를 사용하여 String 객체를 생성
```java
String str = new String("Hello, Java!");
```

new 키워드 없이 String 객체 생성
```java
String str = "Hello, Java!";
```

### String 객체의 메모리 구조

리터럴로 생성한 문자열은 문자열이 같을 때 객체를 공유함(메모리 절약)

```java
String str1 = new String("안녕");
String str2 = "안녕";
String str3 = "안녕";
String str4 = new String("안녕");
```

![Image](https://github.com/user-attachments/assets/cb73385f-a94a-40f5-9d84-00ae7ae3f9f1)


### String 객체 문자열 숫자 변환
래퍼 클래스(Wrapper Class)를 사용하여 문자열을 숫자로 변환할 수 있음

![Image](https://github.com/user-attachments/assets/b85ef39e-c4c8-4aa4-9a55-4cf07fa02437)

래퍼 클래스를 활용하여 문자열을 기초 자료형으로 변환
```java
int num = Integer.parseInt("100");
double dNum = Double.parseDouble("3.14");
```
