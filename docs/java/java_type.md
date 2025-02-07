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
    - 실수형: float, double (참고: [bigDecimal class-부동소수점연산](https://github.com/Yoo-SH/web_back/blob/main/docs/java/java_bigdecimal.md))
  - 문자형: char(참고:[char 자료형과 관련된 메서드](https://github.com/Yoo-SH/web_back/blob/main/docs/java/java_char.md))
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

![Image](https://github.com/user-attachments/assets/e91e7aca-bb3c-46f9-bd08-c9c5fcb55604)

이와 같은 그림이 나옵니다. int 데이터 타입에 저장된 정수 1의 값은 실제 메모리에 저장될 때 00000000 00000000 00000000 00000001 의 값을 가집니다. 이 값을 byte 데이터 타입에 끝에 1byte(00000001) 영역만 넣자니 앞에있는 3byte (00000000 00000000 00000000) 값이 날아갑니다. 그림으로 보면 이렇습니다.

![Image](https://github.com/user-attachments/assets/8905c3ae-0a33-4490-86a2-3db3e89a5921)

앞에 3byte의 공간을 삭제하는 시점에서 많은 데이터가 날아가 정상적인 값이 저장될 수 없을 것입니다. 이와 같이 메모리 크기가 큰 int 데이터 타입에서 메모리 크기가 작은 byte 데이터 타입으로 자동 형변환(Promotion)이 된다면, 정상적이지 않은 값이 나올 수 있기 때문에 Java에서 자동 형변환(Promotion)을 하지 않습니다. 하지만, 우리가 형변환 하려는 정수 값은 1 이므로 byte 데이터 타입 범위 안에 충분히 들어가는 값입니다. 우린 그걸 머릿속으로 알고 있기 때문에 byte 데이터 타입으로 변환된다 하더라도 값이 정상적일 거라고 판단할 수 있습니다. 이럴 때 강제 형변환은 아래와 같이 해주시면 됩니다.

```java
int intValue = 1;
byte byteValue = (byte) intValue;
```

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
# `Java Wrapper 클래스`

## 1. 개요
Java의 Wrapper 클래스는 기본 자료형(Primitive Type)을 객체(Object)로 다룰 수 있도록 해주는 클래스입니다. 기본 자료형을 객체로 감싸는 역할을 하기 때문에 "Wrapper(래퍼)" 클래스라고 불립니다.

## 2. 기본 자료형과 Wrapper 클래스의 대응
Java에서는 8개의 기본 자료형에 대해 각각 대응되는 Wrapper 클래스를 제공합니다.

| 기본 자료형 | Wrapper 클래스 |
|------------|---------------|
| `byte`     | `Byte`        |
| `short`    | `Short`       |
| `int`      | `Integer`     |
| `long`     | `Long`        |
| `float`    | `Float`       |
| `double`   | `Double`      |
| `char`     | `Character`   |
| `boolean`  | `Boolean`     |

![images](https://camo.githubusercontent.com/c049004bacaf2b26f86c5ab5e948e299bde94c820a98ed2e3dff80c7bb8d8d4d/687474703a2f2f7463707363686f6f6c2e636f6d2f6c656374757265732f696d675f6a6176615f626f78696e675f756e626f78696e672e706e67)
## 3. 왜 Wrapper 클래스를 사용하는가?
### (1) 객체가 필요한 경우
- Java의 컬렉션 프레임워크(List, Set, Map 등)는 기본 자료형을 저장할 수 없고, 객체만 저장할 수 있습니다.
- 기본 자료형을 직접 사용하면 컬렉션에 저장할 수 없기 때문에, Wrapper 클래스를 활용하여 객체로 변환합니다.

### (2) 제네릭(Generic) 지원
- Java의 제네릭(Generics)은 기본 자료형을 직접 사용할 수 없습니다.
- Wrapper 클래스를 사용하여 기본 자료형을 제네릭 타입으로 다룰 수 있습니다.

### (3) 유틸리티 메서드 활용
- Wrapper 클래스는 형 변환, 문자열 변환 등의 유용한 메서드를 제공합니다.
  ```java
  int num = Integer.parseInt("100"); // 문자열을 정수로 변환
  double d = Double.valueOf("3.14"); // 문자열을 Double 객체로 변환
  ```
- 기본 타입이 필요하면 parseXxx() 사용
  - 예: int a = Integer.parseInt("100");
- 객체(Integer, Double 등)가 필요하면 valueOf() 사용
  - 예: Integer b = Integer.valueOf("100");
  
### (4) [Boxing & Unboxing](https://github.com/gyoogle/tech-interview-for-developer/blob/master/Language/%5BJava%5D%20Auto%20Boxing%20%26%20Unboxing.md)
- Java 5부터는 **Autoboxing**(기본 자료형 → Wrapper 클래스 자동 변환)과 **Unboxing**(Wrapper 클래스 → 기본 자료형 자동 변환)을 지원하여 편리하게 사용할 수 있습니다.
  ```java
  Integer num = 10;  // Autoboxing (int → Integer)
  int value = num;   // Unboxing (Integer → int)
  ```

## 4. Wrapper 클래스의 주요 사용처
### (1) 컬렉션 프레임워크 (List, Set, Map 등)
```java
import java.util.*;

public class WrapperExample {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        
        for (Integer num : numbers) {
            System.out.println(num);
        }
    }
}
```

### (2) 제네릭(Generic) 타입 사용 시
```java
class Box<T> {
    private T value;
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
}

public class GenericExample {
    public static void main(String[] args) {
        Box<Integer> intBox = new Box<>();
        intBox.setValue(100);
        System.out.println("Value: " + intBox.getValue());
    }
}
```

### (3) 객체로 저장 및 전달이 필요한 경우
```java
public class WrapperStorage {
    public static void main(String[] args) {
        Object obj = Integer.valueOf(50);
        System.out.println("Stored object: " + obj);
    }
}
```

### (4) 문자열 변환 및 형 변환 작업
```java
public class StringConversion {
    public static void main(String[] args) {
        String str = "123";
        int number = Integer.parseInt(str);
        System.out.println("Converted number: " + number);
        
        double pi = Double.parseDouble("3.1415");
        System.out.println("Converted double: " + pi);
    }
}
```


## 5. 장점 및 단점
### ✅ 장점
- 기본 자료형을 객체로 다룰 수 있어 객체 지향적인 프로그래밍이 가능
- 컬렉션 및 제네릭 사용이 가능
- 유틸리티 메서드를 활용하여 형 변환이 편리함

### ❌ 단점
- 기본 자료형보다 메모리를 더 많이 사용
- 성능 저하 가능 (Boxing/Unboxing 과정에서 추가적인 연산 발생)

## 6. 결론
Wrapper 클래스는 Java에서 객체로 데이터를 다뤄야 하는 경우 필수적입니다. 성능이 중요한 경우 기본 자료형을 직접 사용하는 것이 좋지만, 객체로 다루어야 하는 경우에는 Wrapper 클래스를 활용하여 편리한 프로그래밍이 가능합니다.



# `Java에서 by Reference? Call by Value of Reference!`

Java는 **Call by Value(값에 의한 호출)**만 지원하며, **Call by Reference(참조에 의한 호출)**는 직접적으로 지원하지 않습니다. 그러나 **참조 타입(Reference Type)**을 사용하면 마치 Call by Reference처럼 동작하는 것처럼 보일 수 있습니다.

---

## 1. Call by Value vs. Call by Reference

| 구분 | 설명 |
|------|------|
| **Call by Value (값에 의한 호출)** | 메서드에 전달된 인자의 **복사본**이 사용됨. 원본 값은 변경되지 않음. |
| **Call by Reference (참조에 의한 호출)** | 메서드에 객체의 참조(주소)가 전달되어, 원본 객체의 값이 변경될 수 있음. |

---

## 2. Java는 Call by Value만 지원

Java에서 기본 데이터 타입(`int`, `double`, `boolean` 등)은 Call by Value로 동작하여 메서드에서 값을 변경해도 원본 변수에는 영향을 주지 않습니다.

```java
public class CallByValueExample {
    public static void changeValue(int x) {
        x = 10;  // x의 값만 변경됨 (원본 값에는 영향 없음)
    }

    public static void main(String[] args) {
        int num = 5;
        changeValue(num);
        System.out.println(num);  // 5 (변경되지 않음)
    }
}
```

**출력 결과:**
```
5
```

`changeValue(num)`에서 `num`의 값이 변경된 것처럼 보이지만, 이는 **값이 복사되어 전달**된 것이므로 원본 변수에는 영향을 주지 않습니다.

---

## 3. 참조 타입을 이용한 Call by Reference처럼 보이는 동작

Java에서는 객체를 메서드 인자로 넘길 때 **참조값(주소 값)의 복사본**이 전달됩니다. 따라서 객체 내부의 필드는 변경될 수 있지만, 참조 자체를 변경해도 원본 변수에는 영향을 주지 않습니다.

```java
class Person {
    String name;
    
    Person(String name) {
        this.name = name;
    }
}

public class CallByReferenceExample {
    public static void changeName(Person p) {
        p.name = "Charlie";  // 참조된 객체의 필드를 변경 (원본 객체가 변경됨)
    }

    public static void main(String[] args) {
        Person person = new Person("Alice");
        changeName(person);
        System.out.println(person.name);  // "Charlie" (변경됨)
    }
}
```

**출력 결과:**
```
Charlie
```

위 코드에서는 `Person` 객체의 **참조값이 복사**되어 전달되지만, 참조된 객체의 필드를 변경할 수 있기 때문에 Call by Reference처럼 동작하는 것처럼 보입니다.

---

## 4. 참조 자체를 변경하려고 하면?

객체의 **참조 자체를 변경하려고 해도 원본 변수에는 영향이 없음**을 확인할 수 있습니다.

```java
public class ReferenceTest {
    public static void changeReference(Person p) {
        p = new Person("David");  // 새로운 객체를 할당 (원본 변수에는 영향 없음)
    }

    public static void main(String[] args) {
        Person person = new Person("Alice");
        changeReference(person);
        System.out.println(person.name);  // "Alice" (변경되지 않음)
    }
}
```

**출력 결과:**
```
Alice
```

메서드 내에서 `p`가 새로운 `Person` 객체를 참조하게 되었지만, 이는 `p`라는 **로컬 변수**가 변경된 것이고, 원본 변수 `person`에는 영향을 주지 않습니다.

---

## 5. 결론
- Java는 **Call by Value**만 지원한다.
- 기본 데이터 타입은 값이 복사되므로 원본 변수에 영향을 주지 않는다.
- 참조 타입(객체)은 **참조값이 복사되어 전달**되므로 객체의 내부 상태를 변경할 수 있다.
- 그러나 **참조 자체를 변경해도 원본 변수에는 영향을 주지 않는다**.

즉, Java에서는 Call by Reference처럼 보이는 동작이 가능하지만, 엄밀히 말하면 **참조의 값(Call by Value of Reference)을 전달하는 방식**입니다.


# 참고자료
- [tech-refrigerator/Language/JAVA/Promotion & Casting.md](https://github.com/GimunLee/tech-refrigerator/blob/master/Language/JAVA/Promotion%20%26%20Casting.md#promotion--casting)