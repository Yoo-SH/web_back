# 자바 팁 14: 왜 Enum을 사용하는가? Enum 기초 - ordinal, values, name



<br>

## 1. 왜 Enum을 사용하는가?

Java에서 **Enum(열거형)**은 **상수들의 집합**을 나타내는 특별한 데이터 타입입니다.  
즉, **한정된 개수의 값들 중에서 하나를 선택할 때 적합한 데이터 타입**입니다. Enum을 사용하면 코드 가독성이 향상되고, 실수를 줄이는 데 도움을 줍니다.



<br>

## 2. Enum 기본 문법 및 작성법

### Enum 기본 구조
Java에서 Enum은 클래스처럼 선언할 수 있으며, 각 요소는 고유한 상수값을 가집니다.

```java
enum Color {
    RED, GREEN, BLUE
}
```

위 예제는 `Color`라는 열거형을 정의하며, `RED`, `GREEN`, `BLUE`가 각각의 상수입니다.



<br>

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

**출력**: RED ordinal: 0 GREEN ordinal: 1 BLUE ordinal: 2


> **주의**: `ordinal()`의 반환값은 Enum 정의에서 선언된 순서에 의존하므로, 순서를 변경하면 결과값도 변경됩니다.



### 3.2 `values()` 예제
`values()` 메서드는 Enum의 **모든 상수를 배열 형태**로 반환합니다. 이를 이용해 Enum 상수 값을 순회하거나 활용할 수 있습니다.

#### 예제 코드
```java
enum Color {
    RED, GREEN, BLUE
}

class EnumValuesExample {
    public static void main(String[] args) {
        for (Color color : Color.values()) {
            System.out.println(color + " ordinal: " + color.ordinal());
        }
    }
}
```

**출력**: RED ordinal: 0 GREEN ordinal: 1 BLUE ordinal: 2




### 3.3 `name()` 예제
`name()` 메서드는 각 Enum 상수의 **이름(문자열 표현)**을 반환합니다.  
Enum의 상수를 문자열로 출력하거나 관리할 때 유용합니다.

#### 예제 코드
```java
enum Color {
    RED, GREEN, BLUE
}

class EnumNameExample {
    public static void main(String[] args) {
        System.out.println("RED name: " + Color.RED.name());
        System.out.println("GREEN name: " + Color.GREEN.name());
        System.out.println("BLUE name: " + Color.BLUE.name());
    }
}
```

**출력**: Color from valueOf: RED




<br>

## 4. 설명 및 활용

### `ordinal`, `values()`, `name()` 활용하기
- `ordinal()`: Enum 순서값을 알고 싶을 때 사용.
- `values()`: 모든 Enum 상수를 순회하거나 처리할 때 유용.
- `name()`: 문자열 값으로 상수를 표시할 때 활용.

이 모든 메서드는 Enum의 **상수를 관리하고 다루는 데 필수적인 도구**로, 단순히 상수를 정의하는 것 이상의 확장된 기능을 제공합니다.

### 추가 활용 예제
```java
enum Status {
    STARTED, IN_PROGRESS, COMPLETED
}

class AdvancedEnumExample {
    public static void main(String[] args) {
        // 모든 Status 상수를 출력
        for (Status status : Status.values()) {
            System.out.println("Status: " + status.name() + ", Ordinal: " + status.ordinal());
        }

        // 특정 Status 처리
        String input = "COMPLETED";
        Status status = Status.valueOf(input);
        switch (status) {
            case STARTED:
                System.out.println("Task has started.");
                break;
            case IN_PROGRESS:
                System.out.println("Task is in progress.");
                break;
            case COMPLETED:
                System.out.println("Task is completed.");
                break;
        }
    }
}
```

**출력**: Status: STARTED, Ordinal: 0 Status: IN_PROGRESS, Ordinal: 1 Status: COMPLETED, Ordinal: 2 Task is completed




<br>

## 5. 요약

### 주요 메서드
1. **`ordinal()`**
    - Enum 상수의 순서(0부터 시작)를 반환.
    - 예: `Color.RED.ordinal()` → 0

2. **`values()`**
    - Enum의 모든 상수를 배열로 반환.
    - 예: `for (Color color : Color.values())`

3. **`name()`**
    - Enum 상수의 이름(원래 선언된 이름)을 문자열로 반환.
    - 예: `Color.RED.name()` → "RED"

4. **`valueOf(String name)`**
    - Enum 상수 이름을 문자열로 받아 해당 상수를 반환.
    - 예: `Color.valueOf("RED")` → `Color.RED`



### Enum 사용으로 얻는 이점:
1. **코드 가독성 증가**: 상수 값이 Enum으로 명확히 표현됨.
2. **타입 안정성**: Enum으로 정의된 값만 사용할 수 있도록 제한.
3. **변경 용이성**: Enum 값 또는 메서드를 변경할 때 코드 관리가 쉽고 직관적임.

다양한 경우에 Enum을 적절히 사용하면 **가독성과 유지보수성**이 뛰어난 코드를 작성할 수 있습니다.


