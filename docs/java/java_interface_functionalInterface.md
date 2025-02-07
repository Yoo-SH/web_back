# 자바에서 Functional Interface란?

**Functional Interface**란, 단 하나의 **추상 메서드(abstract method)** 만을 가지는 인터페이스를 의미합니다.  
자바 8에서 도입되었으며, 람다 표현식(lambda expression)과 함께 사용되어 **코드의 간결성**과 **가독성**을 높이는 데 도움을 줍니다.

---

## 1. Functional Interface의 특징
- 단 하나의 **추상 메서드**만 가질 수 있음.
- 여러 개의 **default** 또는 **static** 메서드는 가질 수 있음.
- `@FunctionalInterface` 어노테이션을 사용하면 컴파일러가 **단일 추상 메서드 여부**를 체크함.

---

## 2. Functional Interface 예제

### (1) 기본적인 Functional Interface
```java
@FunctionalInterface
interface MyFunctionalInterface {
    void doSomething(); // 단 하나의 추상 메서드
}
```
위와 같은 인터페이스는 단 하나의 추상 메서드만 가지므로 Functional Interface로 사용할 수 있습니다.

---

## 3. Functional Interface 사용 예제

### (1) 익명 클래스 방식
Functional Interface는 익명 클래스로 구현할 수 있습니다.

```java
public class Main {
    public static void main(String[] args) {
        MyFunctionalInterface func = new MyFunctionalInterface() {
            @Override
            public void doSomething() {
                System.out.println("Hello, Functional Interface!");
            }
        };
        func.doSomething();
    }
}
```

### (2) 람다 표현식을 사용한 구현
자바 8부터는 람다 표현식을 사용하여 Functional Interface를 간결히 표현할 수 있습니다.

```java
public class Main {
    public static void main(String[] args) {
        MyFunctionalInterface func = () -> System.out.println("Hello, Lambda!");
        func.doSomething();
    }
}
```
✅ 람다 표현식을 사용하면 코드가 훨씬 짧아지고 가독성이 좋아집니다.

---

## 4. 자바에서 제공하는 주요 Functional Interface

자바 8에서는 자주 사용하는 Functional Interface들이 **`java.util.function`** 패키지에서 제공됩니다.

| Functional Interface | 추상 메서드         | 설명                                                  |
|-----------------------|---------------------|-------------------------------------------------------|
| `Runnable`           | `void run()`       | 매개변수 없이 실행하는 작업                           |
| `Supplier<T>`        | `T get()`          | 매개변수 없이 값을 반환                               |
| `Consumer<T>`        | `void accept(T t)` | 매개변수를 받아서 소비(처리)                         |
| `Function<T, R>`     | `R apply(T t)`     | 입력을 받아서 출력 값을 반환                         |
| `Predicate<T>`       | `boolean test(T t)`| 입력 값을 조건으로 검사 후 `true/false`를 반환       |

---

## 5. 주요 Functional Interface 사용 예제

### (1) `Consumer<T>` 예제
```java
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        Consumer<String> printer = message -> System.out.println("Message: " + message);
        printer.accept("Hello, Consumer!"); 
    }
}
```

**출력 결과**:# 자바에서 Functional Interface란?

**Functional Interface**란, 단 하나의 **추상 메서드(abstract method)** 만을 가지는 인터페이스를 의미합니다.  
자바 8에서 도입되었으며, 람다 표현식(lambda expression)과 함께 사용되어 **코드의 간결성**과 **가독성**을 높이는 데 도움을 줍니다.

---

## 1. Functional Interface의 특징
- 단 하나의 **추상 메서드**만 가질 수 있음.
- 여러 개의 **default** 또는 **static** 메서드는 가질 수 있음.
- `@FunctionalInterface` 어노테이션을 사용하면 컴파일러가 **단일 추상 메서드 여부**를 체크함.

---

## 2. Functional Interface 예제

### (1) 기본적인 Functional Interface
```java
@FunctionalInterface
interface MyFunctionalInterface {
    void doSomething(); // 단 하나의 추상 메서드
}
```
위와 같은 인터페이스는 단 하나의 추상 메서드만 가지므로 Functional Interface로 사용할 수 있습니다.

---

## 3. Functional Interface 사용 예제

### (1) 익명 클래스 방식
Functional Interface는 익명 클래스로 구현할 수 있습니다.

```java
public class Main {
    public static void main(String[] args) {
        MyFunctionalInterface func = new MyFunctionalInterface() {
            @Override
            public void doSomething() {
                System.out.println("Hello, Functional Interface!");
            }
        };
        func.doSomething();
    }
}
```

### (2) 람다 표현식을 사용한 구현
자바 8부터는 람다 표현식을 사용하여 Functional Interface를 간결히 표현할 수 있습니다.

```java
public class Main {
    public static void main(String[] args) {
        MyFunctionalInterface func = () -> System.out.println("Hello, Lambda!");
        func.doSomething();
    }
}
```
✅ 람다 표현식을 사용하면 코드가 훨씬 짧아지고 가독성이 좋아집니다.

---

## 4. 자바에서 제공하는 주요 Functional Interface

자바 8에서는 자주 사용하는 Functional Interface들이 **`java.util.function`** 패키지에서 제공됩니다.

| Functional Interface | 추상 메서드         | 설명                                                  |
|-----------------------|---------------------|-------------------------------------------------------|
| `Runnable`           | `void run()`       | 매개변수 없이 실행하는 작업                           |
| `Supplier<T>`        | `T get()`          | 매개변수 없이 값을 반환                               |
| `Consumer<T>`        | `void accept(T t)` | 매개변수를 받아서 소비(처리)                         |
| `Function<T, R>`     | `R apply(T t)`     | 입력을 받아서 출력 값을 반환                         |
| `Predicate<T>`       | `boolean test(T t)`| 입력 값을 조건으로 검사 후 `true/false`를 반환       |

---

## 5. 주요 Functional Interface 사용 예제

### (1) `Consumer<T>` 예제
```java
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        Consumer<String> printer = message -> System.out.println("Message: " + message);
        printer.accept("Hello, Consumer!"); 
    }
}
```

**출력 결과**: Message: Hello, Consumer!

➡️ `Consumer`는 입력을 받아 출력하지만 반환 값이 없습니다.

### (2) `Function<T, R>` 예제
```java
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<Integer, String> intToString = num -> "Number: " + num;
        System.out.println(intToString.apply(10));
    }
}
```

**출력 결과**: Number: 10

➡️ `Function<T, R>`는 입력을 받아 변환 후 반환하는 역할을 합니다.

### (3) `Predicate<T>` 예제
```java
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Predicate<Integer> isEven = num -> num % 2 == 0;
        System.out.println(isEven.test(10)); // true
        System.out.println(isEven.test(7));  // false
    }
}
```
➡️ `Predicate<T>`는 조건을 검사하여 `true` 또는 `false`를 반환합니다.

---

## 6. Functional Interface와 메서드 참조
Functional Interface는 **메서드 참조(Method Reference)** 와 함께 사용할 수 있습니다.

### (1) 스태틱 메서드 참조
```java
import java.util.function.Consumer;

public class Main {
    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        Consumer<String> printer = Main::printMessage;
        printer.accept("Hello, Method Reference!");
    }
}
```

### (2) 인스턴스 메서드 참조
```java
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<String, Integer> stringToLength = String::length;
        System.out.println(stringToLength.apply("Hello")); // 5
    }
}
```

---

## 7. 결론
- **Functional Interface**는 단 하나의 추상 메서드를 가지는 인터페이스로, 람다 표현식과 함께 사용하기 적합합니다.
- `@FunctionalInterface` 어노테이션을 사용하면 컴파일 타임에 단일 추상 메서드 여부를 강제할 수 있습니다.
- 자바에서 제공하는 `java.util.function` 패키지의 **Functional Interface**들을 활용하면 가독성과 생산성을 높일 수 있습니다.
- **메서드 참조(Method Reference)** 를 사용하면 더욱 간결하고 깔끔한 코드를 작성할 수 있습니다.

✍️ **이제 Functional Interface와 람다를 적극 활용해 보세요!**