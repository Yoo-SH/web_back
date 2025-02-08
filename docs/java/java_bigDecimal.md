# Java BigDecimal

<br>

## 개요
`BigDecimal`은 Java에서 정밀도가 높은 부동소수점 연산을 수행하기 위해 제공되는 클래스입니다. `double`과 `float` 타입은 부동소수점 오차가 발생할 수 있기 때문에, 금융 계산과 같이 정확한 연산이 필요한 경우 `BigDecimal`을 사용합니다.

<br>

## BigDecimal 생성
`BigDecimal`은 다양한 방법으로 생성할 수 있습니다.

```java
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        BigDecimal bd1 = new BigDecimal("10.5"); // 문자열을 이용한 생성 (추천)
        BigDecimal bd2 = BigDecimal.valueOf(10.5); // valueOf() 메서드 사용
        BigDecimal bd3 = new BigDecimal(10.5); // 부동소수점 직접 사용 (권장되지 않음)

        System.out.println(bd1); // 10.5
        System.out.println(bd2); // 10.5
        System.out.println(bd3); // 10.50000000000000000055511151231257827021181583404541015625
    }
}
```

**주의:**
- `BigDecimal`을 생성할 때 `double`을 직접 사용하면 예상치 못한 부동소수점 오차가 발생할 수 있습니다.
- 문자열을 이용한 생성 방법을 권장합니다.

<br>

## 사칙 연산
`BigDecimal`은 기본 연산자(`+`, `-`, `*`, `/`)를 지원하지 않으며, 연산을 위해 메서드를 사용해야 합니다.

```java
import java.math.BigDecimal;

public class BigDecimalExample {
    public static void main(String[] args) {
        BigDecimal num1 = new BigDecimal("10.5");
        BigDecimal num2 = new BigDecimal("2.3");

        BigDecimal sum = num1.add(num2);       // 덧셈
        BigDecimal diff = num1.subtract(num2); // 뺄셈
        BigDecimal product = num1.multiply(num2); // 곱셈
        BigDecimal quotient = num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP); // 나눗셈 (소수점 2자리 반올림)

        System.out.println("Sum: " + sum);
        System.out.println("Difference: " + diff);
        System.out.println("Product: " + product);
        System.out.println("Quotient: " + quotient);
    }
}
```

<br>

## 비교
`BigDecimal`은 `compareTo()` 메서드를 사용하여 값을 비교합니다.

```java
BigDecimal a = new BigDecimal("10.5");
BigDecimal b = new BigDecimal("2.3");

if (a.compareTo(b) > 0) {
    System.out.println("a가 b보다 큽니다.");
} else if (a.compareTo(b) < 0) {
    System.out.println("a가 b보다 작습니다.");
} else {
    System.out.println("a와 b가 같습니다.");
}
```

<br>

## 소수점 자리 설정
소수점 자리를 설정할 때 `setScale()`을 사용합니다.

```java
BigDecimal num = new BigDecimal("10.56789");
BigDecimal rounded = num.setScale(2, BigDecimal.ROUND_HALF_UP); // 소수점 둘째 자리에서 반올림
System.out.println(rounded); // 10.57
```

<br>

## 정리
- `BigDecimal`은 정밀한 계산이 필요할 때 사용합니다.
- `double`을 직접 사용하여 생성하는 것은 권장되지 않으며, `String` 또는 `valueOf()`를 이용해야 합니다.
- 기본 연산자는 사용할 수 없으며, `add()`, `subtract()`, `multiply()`, `divide()` 등의 메서드를 사용해야 합니다.
- 비교는 `compareTo()` 메서드를 사용합니다.
- 소수점 처리는 `setScale()` 메서드를 이용합니다.

<br>

## 참고
자세한 내용은 [공식 문서](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/math/BigDecimal.html)를 참고하세요.

