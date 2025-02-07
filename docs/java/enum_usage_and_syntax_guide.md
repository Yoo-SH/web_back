# 자바 팁 14: 왜 Enum을 사용하는가? Enum 기초 - ordinal, values, name

---

## 1. 왜 Enum을 사용하는가?

Java에서 **Enum(열거형)**은 **상수들의 집합**을 나타내는 특별한 데이터 타입입니다.  
즉, **한정된 개수의 값들 중에서 하나를 선택할 때 적합한 데이터 타입**입니다. Enum을 사용하면 코드 가독성이 향상되고, 실수를 줄이는 데 도움을 줍니다.

---

## 2. Enum 기본 문법 및 작성법

### Enum 기본 구조
Java에서 Enum은 클래스처럼 선언할 수 있으며, 각 요소는 고유한 상수값을 가집니다.

```java
enum Color {
    RED, GREEN, BLUE
}
```

위 예제는 `Color`라는 열거형을 정의하며, `RED`, `GREEN`, `BLUE`가 각각의 상수입니다.

---

## 3. Enum의 기본 제공 메서드 및 사용 예제

Enum에는 유용한 메서드들이 포함되어 있으며, 이 메서드들을 활용해 Enum 값을 더 효과적으로 사용할 수 있습니다. 대표적으로 `ordinal`, `values()`, `name()`을 사용할 수 있습니다.

### 3.1 `ordinal()` 예제
`ordinal()` 메서드는 Enum 상수의 **순서를 반환**합니다.  
순서는 **0부터 시작**하며, Enum에 정의된 순서에 따라 값이 결정됩니다.

#### 예제 코드
```java
enum Color {
    RED, GREEN, BLUE
}

class EnumOrdinalExample {
    public static void main(String[] args) {
        System.out.println("RED ordinal: " + Color.RED.ordinal());
        System.out.println("GREEN ordinal: " + Color.GREEN.ordinal());
        System.out.println("BLUE ordinal: " + Color.BLUE.ordinal());
    }
}
```

**출력**: