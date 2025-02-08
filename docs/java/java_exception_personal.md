# `Java 사용자 정의 예외 (Custom Exception)`

Java에서는 기본 제공 예외 클래스만으로는 애플리케이션 또는 비즈니스 로직에서 발생할 수 있는 모든 상황을 명확히 표현하기 어려울 수 있습니다.  
이때 **사용자 정의 예외(Custom Exception)**를 통해 더욱 직관적이고 명확한 예외 처리를 구현할 수 있습니다.



<br>

## 사용자 정의 예외란?

사용자 정의 예외는 **기본 제공 예외 클래스(Exception, RuntimeException 등)를 상속**받아,  
특정 도메인 문제나 로직에 맞게 커스텀한 예외 클래스를 생성하는 것입니다.



<br>

## 사용자 정의 예외를 사용하는 이유

1. **특정 비즈니스 로직 표현**
    - 애플리케이션 내에서 발생한 문제를 명확히 정의할 수 있습니다.
        - 예: "나이는 음수일 수 없습니다." 같은 규칙 위반.

2. **예외의 가독성 증가**
    - 기본 예외보다 문제의 근본 원인을 더 명확히 나타냅니다.

3. **표준화된 예외 처리**
    - 사실상 표준화된 도메인 별 예외를 만들어, 코드를 일관성 있게 유지할 수 있습니다.



<br>

## 사용자 정의 예외 구현

### 구현 시 선택 사항
1. **Checked Exception**  
   → `Exception`을 상속받아 구현.  
   → 처리되지 않으면 컴파일 에러가 발생하며, 반드시 `try-catch` 또는 `throws`로 처리해야 합니다.

2. **Unchecked Exception**  
   → `RuntimeException`을 상속받아 구현.  
   → 런타임 시 발생하고, 컴파일러가 처리 여부를 강제하지 않습니다.



### 기본 형식
```java
public class MyCustomException extends Exception {
    public MyCustomException() {
        super(); // 기본 생성자
    }

    public MyCustomException(String message) {
        super(message); // 메시지를 전달받는 생성자
    }

    public MyCustomException(String message, Throwable cause) {
        super(message, cause); // 메시지와 원인을 전달받는 생성자
    }

    public MyCustomException(Throwable cause) {
        super(cause); // 원인을 전달받는 생성자
    }
}
```



<br>

## 1. Checked Exception 예제

```java
// 사용자 정의 Checked Exception 클래스
public class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}
```

```java
// 사용자 정의 예외를 사용하는 코드
public class CheckedExceptionExample {
    public static void checkAge(int age) throws InvalidAgeException {
        if (age < 0) {
            throw new InvalidAgeException("Age cannot be negative");
        } else {
            System.out.println("Age is valid: " + age);
        }
    }

    public static void main(String[] args) {
        try {
            checkAge(-5); // 음수 나이로 예외 발생
        } catch (InvalidAgeException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}
```

**출력**: Exception caught: Age cannot be negative




<br>

## 2. Unchecked Exception 예제

```java
// 사용자 정의 Unchecked Exception 클래스
public class DivisionByZeroException extends RuntimeException {
    public DivisionByZeroException(String message) {
        super(message);
    }
}
```

```java
// 사용자 정의 예외를 사용하는 코드
public class UncheckedExceptionExample {
    public static int divide(int numerator, int denominator) {
        if (denominator == 0) {
            throw new DivisionByZeroException("Denominator cannot be zero");
        }
        return numerator / denominator;
    }

    public static void main(String[] args) {
        try {
            System.out.println(divide(10, 0)); // 0으로 나누기
        } catch (DivisionByZeroException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}
```

**출력**: Exception caught: Denominator cannot be zero




<br>

## 3. 사용자 정의 예외에 추가 데이터 제공

```java
// 사용자 정의 예외 클래스에서 추가 데이터
public class UserNotFoundException extends Exception {
    private int userId;

    public UserNotFoundException(String message, int userId) {
        super(message);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
```

```java
// 예외를 발생시키고 처리하는 코드
public class UserService {
    public static void findUser(int userId) throws UserNotFoundException {
        if (userId != 123) {
            throw new UserNotFoundException("User not found", userId);
        }
        System.out.println("User found: " + userId);
    }

    public static void main(String[] args) {
        try {
            findUser(456); // ID가 123이 아니므로 예외 발생
        } catch (UserNotFoundException e) {
            System.out.println("Exception caught: " + e.getMessage());
            System.out.println("User ID: " + e.getUserId());
        }
    }
}
```

**출력**: Exception caught: User not found User ID: 456




<br>

## 4. Checked와 Unchecked 예외 선택 기준

### Checked Exception:
1. **복구 가능**하며, 코드를 수정하지 않아도 상황에 적절히 대응 가능할 때.
2. 호출자가 반드시 예외를 처리하도록 강제해야 할 때.

예) `IOException`, `SQLException`

### Unchecked Exception:
1. **프로그래머의 실수**로 인해 발생할 가능성이 있는 경우.
2. 복구 불가능하며, 런타임 시만 의미가 있을 경우.

예) `NullPointerException`, `ArithmeticException`



<br>

## 5. 예외 처리 표준

### 사용자 정의 예외 설계 시 주의사항:
1. **명확한 이름 지정**
    - 예외 클래스를 본 사람이 명확히 의도를 이해할 수 있도록 이름을 지정해야 합니다.
    - 예) `InvalidAgeException`, `UserNotFoundException`.

2. **필요할 때만 정의**
    - 자바 기본 예외(`IllegalArgumentException`, `IOException` 등)로 충분히 대체 가능하다면 사용자 정의 예외를 피하는 것이 좋습니다.

3. **커스텀 데이터 제공**
    - 필요한 경우 예외 클래스에 추가 정보를 저장하고, 이를 호출자가 접근할 수 있도록 구현합니다.

4. **Exception과 RuntimeException 선택**
    - 처리 여부를 강제하고 싶다면 `Exception`을 상속받아 Checked 예외로 설계하고,  
      그렇지 않다면 `RuntimeException`을 상속받아 Unchecked 예외로 만드세요.

5. **문서화**
    - 예외 발생 조건, 전달되는 데이터, 호출자가 처리해야 하는 방법 등을 적절히 명시해야 합니다.



<br>

## 6. 최종 요약

- 사용자 정의 예외는 기본 제공 예외만으로는 표현하기 힘든 비즈니스 로직이나 도메인 문제를 명확하게 정의하고 처리할 때 사용됩니다.
- `Checked Exception`과 `Unchecked Exception` 중 하나를 선택하여 애플리케이션 요구 사항에 맞게 설계합니다.
- 잘 설계된 사용자 정의 예외는 애플리케이션의 가독성과 유지보수성을 높이는 데 기여합니다.



### 예제 핵심 구현 요약

```java
public class ExampleCustomException extends Exception {
    private int additionalData;

    public ExampleCustomException(String message, int additionalData) {
        super(message);
        this.additionalData = additionalData;
    }

    public int getAdditionalData() {
        return additionalData;
    }
}
```

```java
// 예외 발생 예시
try {
    throw new ExampleCustomException("Something went wrong", 42);
} catch (ExampleCustomException e) {
    System.out.println("Message: " + e.getMessage());
    System.out.println("Additional Data: " + e.getAdditionalData());
}
```

**출력**: Message: Something went wrong Additional Data: 42


