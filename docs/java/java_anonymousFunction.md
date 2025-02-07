# `Java에서의 익명 함수 (Anonymous Function)`

Java에서 익명 함수(Anonymous Function)는 이름이 없는 함수로, 주로 람다 표현식(Lambda Expression)과 익명 클래스(Anonymous Class) 형태로 사용됩니다. 이는 간결한 코드 작성과 함수형 프로그래밍 스타일을 지원하는 데 유용합니다.

## 1. 익명 클래스 (Anonymous Class)

익명 클래스는 클래스를 선언하지 않고 즉석에서 인스턴스를 생성하는 방법입니다. 주로 인터페이스나 추상 클래스를 구현할 때 사용됩니다.

```java
interface Greeting {
    void sayHello();
}

public class Main {
    public static void main(String[] args) {
        Greeting greeting = new Greeting() {
            @Override
            public void sayHello() {
                System.out.println("Hello, Anonymous Class!");
            }
        };
        
        greeting.sayHello();
    }
}
```

### 특징
- 익명 클래스를 사용하면 불필요한 클래스 정의를 줄일 수 있습니다.
- 여러 메서드를 가진 인터페이스를 구현할 때 유용합니다.
- 다소 코드가 장황할 수 있습니다.

## 2. 람다 표현식 (Lambda Expression)

Java 8부터 도입된 람다 표현식은 익명 함수의 보다 간결한 형태입니다. 함수형 인터페이스(Functional Interface, 단 하나의 추상 메서드를 가지는 인터페이스)와 함께 사용됩니다.

```java
@FunctionalInterface
interface Greeting {
    void sayHello();
}

public class Main {
    public static void main(String[] args) {
        Greeting greeting = () -> System.out.println("Hello, Lambda!");
        greeting.sayHello();
    }
}
```

### 특징
- 코드가 간결하고 가독성이 높습니다.
- 불필요한 익명 클래스 정의를 줄일 수 있습니다.
- 함수형 프로그래밍 스타일을 지원합니다.

## 3. 람다 표현식의 다양한 활용

### 3.1 매개변수가 있는 람다 표현식
```java
@FunctionalInterface
interface Sum {
    int add(int a, int b);
}

public class Main {
    public static void main(String[] args) {
        Sum sum = (a, b) -> a + b;
        System.out.println("Sum: " + sum.add(5, 10));
    }
}
```

### 3.2 여러 코드 블록이 있는 람다 표현식
```java
@FunctionalInterface
interface Printer {
    void print(String message);
}

public class Main {
    public static void main(String[] args) {
        Printer printer = message -> {
            System.out.println("Printing message:");
            System.out.println(message);
        };
        printer.print("Hello, Lambda with multiple statements!");
    }
}
```

### 3.3 Java 내장 함수형 인터페이스 활용
Java는 `java.util.function` 패키지에서 제공하는 함수형 인터페이스를 활용하여 익명 함수를 쉽게 사용할 수 있습니다.

```java
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        Function<Integer, Integer> square = x -> x * x;
        System.out.println("Square of 5: " + square.apply(5));
    }
}
```

## 4. 익명 함수 사용 시 주의점
- 익명 클래스와 람다 표현식은 적절한 상황에서 사용해야 하며, 과도한 사용은 코드의 가독성을 해칠 수 있습니다.
- 람다 표현식은 함수형 인터페이스에서만 사용할 수 있습니다.
- 익명 클래스는 다중 인터페이스를 구현할 수 없지만, 람다 표현식은 단 하나의 메서드만 표현할 수 있습니다.

## 5. 결론
Java에서 익명 함수는 주로 익명 클래스와 람다 표현식으로 구현되며, 람다 표현식은 보다 간결한 방식으로 함수형 프로그래밍을 가능하게 합니다. 적절한 상황에서 익명 함수를 활용하면 코드의 가독성을 높이고 유지보수를 용이하게 할 수 있습니다.
