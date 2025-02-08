# Java 람다식 (Lambda Expressions)

<br>

## 소개
람다식(Lambda Expression)은 자바 8(Java 8)에서 도입된 기능으로, 익명 함수를 보다 간결하게 표현할 수 있도록 해줍니다. 이를 통해 코드의 가독성을 높이고 간결한 함수형 프로그래밍 스타일을 사용할 수 있습니다.

<br>

## 람다식 기본 문법

람다식의 기본적인 형태는 다음과 같습니다:

```java
(parameters) -> expression
```

또는 여러 문장을 포함할 경우:

```java
(parameters) -> {
    statements;
}
```

<br>

## 예제

### 1. 기본적인 람다식 예제
```java
// 기존 익명 클래스 방식
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello, Lambda!");
    }
};

// 람다식 사용
Runnable r2 = () -> System.out.println("Hello, Lambda!");

r1.run();
r2.run();
```

### 2. 함수형 인터페이스와 람다식
람다식은 주로 **함수형 인터페이스**(Functional Interface)와 함께 사용됩니다.

#### 함수형 인터페이스 예제
```java
@FunctionalInterface
interface MyFunctionalInterface {
    int add(int a, int b);
}

public class LambdaExample {
    public static void main(String[] args) {
        // 람다식 사용
        MyFunctionalInterface sum = (a, b) -> a + b;
        System.out.println(sum.add(10, 20));
    }
}
```

### 3. 자바 내장 함수형 인터페이스 활용
자바 8에서는 `java.util.function` 패키지에 여러 가지 함수형 인터페이스가 제공됩니다.

#### 주요 내장 함수형 인터페이스
- `Consumer<T>` : 입력을 받고 반환값이 없음 (`void accept(T t)`)
- `Supplier<T>` : 입력값 없이 결과 반환 (`T get()`)
- `Function<T, R>` : 입력을 받고 변환하여 반환 (`R apply(T t)`)
- `Predicate<T>` : 입력을 받고 boolean 반환 (`boolean test(T t)`)

#### 예제
```java
import java.util.function.*;

public class LambdaFunctionalInterfaces {
    public static void main(String[] args) {
        // Consumer 사용 예제
        Consumer<String> printer = message -> System.out.println("Message: " + message);
        printer.accept("Hello, Java!");

        // Supplier 사용 예제
        Supplier<Double> randomSupplier = () -> Math.random();
        System.out.println("Random Value: " + randomSupplier.get());

        // Function 사용 예제
        Function<String, Integer> stringLength = str -> str.length();
        System.out.println("Length: " + stringLength.apply("Lambda"));

        // Predicate 사용 예제
        Predicate<Integer> isEven = number -> number % 2 == 0;
        System.out.println("Is 10 even? " + isEven.test(10));
    }
}
```

<br>

## 람다식의 장점
1. **코드 간결성**: 익명 클래스를 사용할 필요 없이 간단한 코드로 대체 가능
2. **가독성 향상**: 불필요한 코드 제거로 가독성이 좋아짐
3. **함수형 프로그래밍 지원**: 스트림 API와 결합하여 강력한 데이터 처리 가능

<br>

## 주의사항
- 람다식은 **오직 하나의 메서드만 포함하는 함수형 인터페이스**에서만 사용 가능
- 변수 캡처링 시 지역 변수는 **final** 또는 **사실상 final(Effectively Final)** 이어야 함

<br>

## 결론
람다식을 활용하면 보다 간결하고 가독성 높은 코드를 작성할 수 있습니다. 특히, 자바 8 이후의 스트림 API 및 컬렉션 프레임워크와 함께 사용할 때 강력한 기능을 발휘합니다.
