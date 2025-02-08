# `JAVA 제네릭`

<br>

## 1. 제네릭(Generic)이란?
다양한 종류의 데이터를 처리할 수 있는 클래스와 메소드를 작성하는 방법. 클래스를 정의할 때 자료형을 T로 표현

### 제네릭 기반 vs object 기반

### Object
- Java의 최상위 클래스 (`java.lang.Object`).
- 모든 클래스는 `Object`를 상승받아 사용 가능.
- `Object` 타입으로 선언하면 어떤 타입의 객체도 저장 가능하지만, 타입 변환 (Casting)이 필요.

### Generic
- Java 5에서 등장한 기능.
- 컨파일 시점에 타입을 결정할 수 있도록 하는 기능.
- 타입 안정성을 높이고 빠추적인 카시트를 막을 수 있음.

__Object 기반__
- Object 참조변수를 기반으로 일반적인 객체처리 가능
```java
package main;
 
public class Box{
    private Object data;  //자료형 Object로 선언
    publicvoidset(Object data){ //파라미터로 Object를 받음
        this.data = data;
    }
    public Object get() { //반환형이 Object
        return data;
    }
 }


public class Test {
 public static void main(String[] args) {
    Box b= new Box();
 
    b.set("Hello World!");
    String s = (String)b.get();
    
    b.set(Integer.valueOf(10));
    Integer i = (Integer)b.get();
 }
 }

 ```

__제네릭 기반__

- 제네릭을 사용하면 컴파일러가 자료형을 체크하여 오류를 미리 방지
```java
package main;

public class Box<T> {
    private T data;  //자료형 T로 선언
    public void set(T data) { //파라미터로 T를 받음
        this.data = data;
    }
    public T get() { //반환형이 T
        return data;
    }
}

public class Test {
    public static void main(String[] args) {
        Box<String> b = new Box<String>();
        b.set("Hello World!");
        String s = b.get();
        
        Box<Integer> b2 = new Box<Integer>();
        b2.set(Integer.valueOf(10));
        Integer i = b2.get();
    }
}
```

<br>

## 차이점 정보
| 비교 항목 | Object | Generic |
|-----------|--------|---------|
| **타입 안정성** | 런타임에 타입 체크 (Casting 필요) | 컨파일 시점에 타입 체크 |
| **형 변환 필요 여부** | 필요 (Downcasting) | 복귀 시 변환 필요 없음 |
| **컨파일 체크** | 런타임 오류 발생 가능 (`ClassCastException`) | 컨파일 시점에 오류 확인 가능 |
| **유용성** | 다양한 타입을 저장 가능 | 특정 타입만 저장 가능 |


###  결론
- **Object**는 모든 객체를 저장할 수 있지만, 누규 때 타입 변환이 필요하며 런타임 오류 위해 조절이 필요.
- **Generic**은 컨파일 시점에 타입을 정하여 주기 때문에, 타입 안정성이 높아지고 반환시 형 변환이 필요 없음.
- **가능하면 Generic을 사용하는 것이 더 안전하고 유지보수에 유리**



<br>

## Generic 메서드 (Generic Method)
Generic 메서드는 메서드 레벨에서 타입을 제네릭하게 정의할 수 있도록 해줍니다.

### Generic 메서드 예제
```java
public class GenericMethodExample {
    // 제네릭 메서드 정의
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] intArray = {1, 2, 3, 4, 5};
        String[] strArray = {"Hello", "World"};

        printArray(intArray); // 1 2 3 4 5
        printArray(strArray); // Hello World
    }
}
```

<br>

## 한정된 제네릭 (Bounded Generics)
한정된 제네릭은 제네릭 타입을 특정 클래스의 하위 클래스로 제한하는 방법입니다.

### 한정된 제네릭 예제
```java
public class BoundedGenericsExample {
    // Number 클래스의 하위 클래스만 받을 수 있는 제네릭 메서드
    public static <T extends Number> double sum(T[] array) {
        double sum = 0.0;
        for (T element : array) {
            sum += element.doubleValue();
        }
        return sum;
    }

    public static void main(String[] args) {
        Integer[] intArray = {1, 2, 3, 4, 5};
        Double[] doubleArray = {1.1, 2.2, 3.3, 4.4, 5.5};

        System.out.println(sum(intArray)); // 15.0
        System.out.println(sum(doubleArray)); // 16.5
    }
}
```

<br>

## Java Generic의 상속

Java에서 **제네릭(Generic)과 상속**을 함께 사용할 때, 중요한 개념과 제약사항이 있습니다. 아래 내용을 이해하면 Java의 제네릭을 더 깊이 있게 활용할 수 있습니다.

### 1. **제네릭 클래스에서의 상속**
제네릭 클래스는 일반 클래스처럼 상속이 가능하며, 부모 클래스의 타입 파라미터를 자식 클래스에서 확장하거나 고정할 수 있습니다.

#### 1.1 부모 클래스의 제네릭을 유지하는 경우
```java
class Parent<T> {
    T data;
    
    public void setData(T data) {
        this.data = data;
    }
    
    public T getData() {
        return data;
    }
}

class Child<T> extends Parent<T> { // 부모의 제네릭 타입을 그대로 유지
}
```

#### 1.2 자식 클래스에서 특정 타입으로 고정하는 경우
```java
class StringChild extends Parent<String> { // 부모의 T를 String으로 고정
}
```



<br>

## 2. **제네릭 타입 경계(Bounded Type)**
제네릭 타입에 **상속 관계를 강제**하려면 `extends` 키워드를 사용합니다.

### 2.1 상위 타입을 제한하기 (`T extends SomeClass`)
```java
class NumberContainer<T extends Number> { // T는 Number 또는 그 하위 타입만 가능
    private T value;
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
}
```

### 3. **와일드카드 (`? extends` vs `? super`)**
#### 3.1 `? extends T` → 상한 제한 (Upper Bound)
```java
class Box<T> {
    private T value;

    public void setValue(T value) { this.value = value; }
    public T getValue() { return value; }
}

void process(Box<? extends Number> box) {
    Number num = box.getValue(); // ✅ 가능 (Number 또는 하위 타입 반환)
    // box.setValue(10); ❌ 오류 발생 (컴파일러가 안전성을 보장할 수 없음)
}
```

#### 3.2 `? super T` → 하한 제한 (Lower Bound)
```java
void addNumber(Box<? super Integer> box) {
    box.setValue(10); // ✅ 가능 (Integer 또는 그 부모 타입에 저장 가능)
    // Integer val = box.getValue(); ❌ 오류 발생 (Object만 안전하게 반환 가능)
}
```

### 4. **제네릭 클래스에서 다중 상속**
Java는 다중 상속을 지원하지 않지만, 제네릭 인터페이스는 여러 개를 구현할 수 있습니다.

```java
interface Readable<T> {
    T read();
}

interface Writable<T> {
    void write(T data);
}

class FileManager<T> implements Readable<T>, Writable<T> {
    private T data;

    @Override
    public T read() {
        return data;
    }

    @Override
    public void write(T data) {
        this.data = data;
    }
}
```



### 5. **제네릭과 배열**
제네릭 타입의 배열은 만들 수 없습니다.
```java
T[] array = new T[10]; // ❌ 컴파일 오류
```
대신 `Array.newInstance()`를 사용해야 합니다.
```java
T[] array = (T[]) Array.newInstance(clazz, 10);
```



### 6. **제네릭과 다형성**
제네릭 타입은 **다형성(Polymorphism)과 다르게 동작**합니다.

```java
List<String> stringList = new ArrayList<>();
List<Object> objectList = stringList; // ❌ 컴파일 오류
```
- `List<String>`은 `List<Object>`로 변환될 수 없음.
- 이 문제를 해결하려면 **와일드카드 (`? extends Object`)** 를 사용해야 함.

```java
List<? extends Object> objectList = stringList; // ✅ 가능
```

<br>

## 7. **정리**
| 개념 | 설명 |
|------|------|
| `T extends Class` | `T`는 `Class` 또는 그 하위 타입만 허용 |
| `T super Class` | `T`는 `Class` 또는 그 상위 타입만 허용 |
| `? extends T` | `T`의 하위 타입만 허용 (읽기 전용) |
| `? super T` | `T`의 상위 타입만 허용 (쓰기 전용) |
| 배열 | `new T[]`는 불가능, `Array.newInstance()` 사용 |
