# `Java Record`

## 개요
`record`는 Java 14에서 프리뷰 기능으로 도입되었고, Java 16에서 정식 기능으로 추가된 새로운 클래스 유형입니다.
`record`는 주로 불변 데이터 모델을 간결하게 정의하기 위해 사용됩니다.

## 특징
- **불변(Immutable)**: 모든 필드는 기본적으로 `final`이며, 생성 후 값 변경이 불가능합니다.
- **자동 생성되는 메서드**:
    - 생성자(Constructor)
    - `getter` (명시적 메서드 없이 필드명으로 접근 가능)
    - `equals()` 및 `hashCode()`
    - `toString()`
- **간결한 선언**: 보일러플레이트 코드가 줄어들어 가독성이 향상됩니다.

## 기본 문법
```java
public record Person(String name, int age) {}
```
위 `record`는 아래 클래스를 자동으로 생성하는 것과 동일합니다.

```java
public final class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String name() { return name; }
    public int age() { return age; }

    @Override
    public boolean equals(Object o) { /* 구현 생략 */ }
    @Override
    public int hashCode() { /* 구현 생략 */ }
    @Override
    public String toString() { /* 구현 생략 */ }
}
```

## 생성자 활용
### 1. 기본 생성자
```java
public record Person(String name, int age) {}
```
위 `record`는 자동으로 모든 필드를 초기화하는 생성자를 제공합니다.

### 2. 사용자 정의 생성자
```java
public record Person(String name, int age) {
    public Person {
        if (age < 0) {
            throw new IllegalArgumentException("나이는 0 이상이어야 합니다.");
        }
    }
}
```
위처럼 생성자 본문을 추가하여 유효성 검사를 할 수 있습니다.

## 메서드 정의
`record`도 일반 클래스처럼 메서드를 추가할 수 있습니다.

```java
public record Person(String name, int age) {
    public String greet() {
        return "Hello, my name is " + name;
    }
}
```

## 인터페이스 구현
`record`는 `interface`를 구현할 수 있습니다.

```java
public interface Greetable {
    String greet();
}

public record Person(String name, int age) implements Greetable {
    @Override
    public String greet() {
        return "Hello, my name is " + name;
    }
}
```

## 제약 사항
1. **상속 불가**: `record`는 `final`이므로 다른 클래스를 상속할 수 없습니다.
2. **필드 변경 불가**: 모든 필드는 `final`이며, setter를 가질 수 없습니다.
3. **명시적 상속 불가**: `extends` 키워드를 사용할 수 없습니다.

## 활용 사례
### DTO (Data Transfer Object)
```java
public record UserDTO(String username, String email) {}
```
### 데이터 모델링
```java
public record Point(int x, int y) {}
```
### 응답 데이터 표현
```java
public record ApiResponse<T>(int status, String message, T data) {}
```

## 결론
`record`는 간결하고 불변성을 보장하는 데이터 구조를 쉽게 정의할 수 있도록 도와줍니다.
DTO, VO(Value Object) 및 간단한 데이터 모델링에 매우 유용합니다.
