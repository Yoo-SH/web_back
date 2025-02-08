# `Java Wrapper 클래스`

<br>

## 1. 개요
Java의 Wrapper 클래스는 기본 자료형(Primitive Type)을 객체(Object)로 다룰 수 있도록 해주는 클래스입니다. 기본 자료형을 객체로 감싸는 역할을 하기 때문에 "Wrapper(래퍼)" 클래스라고 불립니다.

<br>

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

<img src="https://camo.githubusercontent.com/c049004bacaf2b26f86c5ab5e948e299bde94c820a98ed2e3dff80c7bb8d8d4d/687474703a2f2f7463707363686f6f6c2e636f6d2f6c656374757265732f696d675f6a6176615f626f78696e675f756e626f78696e672e706e67" width="500">
<br>

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

<br>

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


<br>

## 5. 장점 및 단점
### ✅ 장점
- 기본 자료형을 객체로 다룰 수 있어 객체 지향적인 프로그래밍이 가능
- 컬렉션 및 제네릭 사용이 가능
- 유틸리티 메서드를 활용하여 형 변환이 편리함

### ❌ 단점
- 기본 자료형보다 메모리를 더 많이 사용
- 성능 저하 가능 (Boxing/Unboxing 과정에서 추가적인 연산 발생)

<br>

## 6. 결론
Wrapper 클래스는 Java에서 객체로 데이터를 다뤄야 하는 경우 필수적입니다. 성능이 중요한 경우 기본 자료형을 직접 사용하는 것이 좋지만, 객체로 다루어야 하는 경우에는 Wrapper 클래스를 활용하여 편리한 프로그래밍이 가능합니다.
