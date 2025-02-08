# `Java String`

## 개요
`String` 클래스는 Java에서 문자열을 다루기 위한 불변(immutable) 객체입니다. 문자열 조작을 위한 다양한 메서드를 제공합니다.

---
<br>

## 1. String 선언 및 생성
```java
// 문자열 리터럴 방식 (추천)
String str1 = "Hello, Java";

// new 키워드를 사용한 방식 (비효율적)
String str2 = new String("Hello, Java");
```

> **Tip:** 문자열 리터럴 방식은 String Pool을 활용하여 메모리를 절약할 수 있습니다.

---
<br>

## 2. 주요 메서드

### 2.1 문자열 길이 확인
```java
String str = "Hello";
int length = str.length(); // 5
```

### 2.2 문자열 연결 

__`+` 연산자__ 가장 간단한 방식, 작은 문자열 작업에 적합

```java
String result = first + " " + second;
```

__String.concat()__ 문자열만 연결 가능, 비교적 덜 사용됨
```java
String first = "Hello";
String second = "World";
String result = first.concat(" ").concat(second); // "Hello World"
```

_String.join()__ 배열이나 리스트 데이터를 특정 구분자로 연결할 때 적합
```java
String result = String.join(", ", "Apple", "Banana", "Cherry");
System.out.println(result); // 출력: Apple, Banana, Cherry
```

__String.format()__ 포매팅이 필요한 문자열 작업에서 사용
```java
String firstName = "John";
String lastName = "Doe";

String fullName = String.format("%s %s", firstName, lastName);
System.out.println(fullName); // 출력: John Doe
```

__StringBuilder__ 성능이 뛰어나며 대량 작업에 적합
```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" ").append("World!");
System.out.println(sb.toString()); // 출력: Hello World!
```

__StringBuffer__ 스레드 안전하지만, `StringBuilder`보다 느림
```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" ").append("World!");
System.out.println(sb.toString()); // 출력: Hello World!
 ```


### 2.4 부분 문자열 (Substring)
```java
String str = "Hello, Java";
String sub = str.substring(7); // "Java"
String sub2 = str.substring(0, 5); // "Hello"
```

### 2.5 문자열 비교
```java
String a = "Hello";
String b = "hello";

boolean isEqual = a.equals(b); // false
boolean isIgnoreCaseEqual = a.equalsIgnoreCase(b); // true
```

### 2.6 문자열 포함 여부 확인
```java
boolean contains = "Hello, Java".contains("Java"); // true
```

### 2.7 문자열 시작 및 끝 검사
```java
boolean starts = "Hello, Java".startsWith("Hello"); // true
boolean ends = "Hello, Java".endsWith("Java"); // true
```

### 2.8 문자열 치환
```java
String str = "Java is fun";
String replaced = str.replace("Java", "Python"); // "Python is fun"
```

### 2.9 문자열 분할
```java
String str = "apple,banana,grape";
String[] fruits = str.split(","); // ["apple", "banana", "grape"]
```

### 2.10 공백 제거
```java
String str = "  Hello World  ";
String trimmed = str.trim(); // "Hello World"
```

### 2.11 문자열 대소문자 변환
```java
String upper = "hello".toUpperCase(); // "HELLO"
String lower = "WORLD".toLowerCase(); // "world"
```

### 2.12 문자열을 숫자로 변환
```java
String numberStr = "123";
int number = Integer.parseInt(numberStr); // 123
```

### 2.13 숫자를 문자열로 변환
```java
int num = 123;
String str = String.valueOf(num); // "123"
```

### 2.14 문자열을 문자 배열로 변환
```java
String str = "Hello";
char[] charArray = str.toCharArray(); // ['H', 'e', 'l', 'l', 'o']
```

---
<br>

## 3. StringBuilder 및 StringBuffer
`String` 객체는 불변이므로 문자열 변경이 잦다면 `StringBuilder` 또는 `StringBuffer`를 사용하는 것이 성능상 유리합니다.

### 3.1 StringBuilder 사용
```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
String result = sb.toString(); // "Hello World"
```

### 3.2 StringBuffer 사용 (멀티스레드 환경에서 안전)
```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World");
String result = sb.toString(); // "Hello World"
```

---
<br>

## 4. 문자열 비교 (`==` vs `equals()`)
```java
String str1 = "Hello";
String str2 = new String("Hello");

System.out.println(str1 == str2); // false (참조 비교)
System.out.println(str1.equals(str2)); // true (값 비교)
```

> **Tip:** 문자열 값 비교는 항상 `equals()`를 사용하세요.

---
<br>

## 5. String.format() 활용
```java
String name = "Java";
int version = 17;
String formatted = String.format("%s version is %d", name, version);
// "Java version is 17"
```

---
<br>

## 6. 문자열 조작을 위한 유용한 메서드 정리
| 메서드 | 설명 |
|--------|------|
| `length()` | 문자열 길이 반환 |
| `charAt(int index)` | 특정 위치 문자 반환 |
| `substring(int beginIndex, int endIndex)` | 부분 문자열 추출 |
| `contains(CharSequence s)` | 문자열 포함 여부 확인 |
| `startsWith(String prefix)` | 특정 문자열로 시작하는지 확인 |
| `endsWith(String suffix)` | 특정 문자열로 끝나는지 확인 |
| `toUpperCase()` | 대문자로 변환 |
| `toLowerCase()` | 소문자로 변환 |
| `trim()` | 앞뒤 공백 제거 |
| `replace(CharSequence target, CharSequence replacement)` | 문자열 치환 |
| `split(String regex)` | 문자열 분할 |
| `equals(Object anObject)` | 문자열 비교 (대소문자 구분) |
| `equalsIgnoreCase(String anotherString)` | 문자열 비교 (대소문자 무시) |
| `concat(String str)` | 문자열 연결 |
| `format(String format, Object... args)` | 형식화된 문자열 생성 |
| `valueOf(Object obj)` | 객체를 문자열로 변환 |
| `toCharArray()` | 문자열을 문자 배열로 변환 |

---
<br>

## 7. string 정리
- `String`은 불변 객체이므로, 빈번한 변경이 필요하면 `StringBuilder` 또는 `StringBuffer`를 사용하세요.
- 문자열 비교는 항상 `equals()`를 사용하세요.
- 문자열 조작 시 다양한 메서드를 적절히 활용하면 성능을 최적화할 수 있습니다.

<br>
<br>

## Java String, StringBuffer, StringBuilder

### 개요
Java에서 문자열을 다루는 주요 클래스는 `String`, `StringBuffer`, `StringBuilder`입니다. 각 클래스는 문자열을 처리하는 방식이 다르며, 사용 목적에 따라 적절한 클래스를 선택해야 합니다.

### 1. String
`String` 클래스는 **불변(Immutable)** 객체로, 한 번 생성되면 변경할 수 없습니다. 문자열을 변경하는 연산을 수행하면 새로운 객체가 생성됩니다.
# 특징
- **불변 객체**: 문자열 변경 시 새로운 객체가 생성됨.
- **리터럴 풀 사용**: 동일한 문자열 리터럴을 재사용하여 메모리 절약 가능.
- **멀티스레드 환경에서 안전**: 불변 객체이므로 동기화 문제가 없음.

#### 예제
```java
public class StringExample {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = str1 + " World"; // 새로운 객체 생성
        
        System.out.println(str1); // Hello
        System.out.println(str2); // Hello World
    }
}
```

---
<br>

### 2. StringBuffer
`StringBuffer` 클래스는 **가변(Mutable)** 객체로, 문자열을 변경할 때 새로운 객체를 생성하지 않고 기존 객체를 변경합니다. 따라서 문자열을 자주 변경하는 경우 `StringBuffer`를 사용하는 것이 성능상 유리합니다.
<br>
# 특징
- **가변 객체**: 문자열을 직접 변경할 수 있음.
- **스레드 안전(Thread-Safe)**: 내부적으로 `synchronized` 키워드를 사용하여 동기화 지원.
- **성능**: `synchronized` 사용으로 다중 스레드 환경에서는 안전하지만, 단일 스레드 환경에서는 속도가 느릴 수 있음.

#### 예제
```java
public class StringBufferExample {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("Hello");
        sb.append(" World"); // 기존 객체에 문자열 추가
        
        System.out.println(sb); // Hello World
    }
}
```

---
<br>

### 3. StringBuilder
`StringBuilder` 클래스는 `StringBuffer`와 유사하지만, **동기화(synchronized)가 지원되지 않음**. 따라서 단일 스레드 환경에서 `StringBuffer`보다 성능이 더 뛰어납니다.
<br>
# 특징
- **가변 객체**: 문자열을 직접 변경할 수 있음.
- **비동기 처리(Non-thread safe)**: 동기화 지원이 없어서 단일 스레드 환경에서 더 빠름.
- **성능**: `StringBuffer`보다 속도가 빠름(동기화 비용 없음).

#### 예제
```java
public class StringBuilderExample {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Hello");
        sb.append(" World"); // 기존 객체에 문자열 추가
        
        System.out.println(sb); // Hello World
    }
}
```

---
<br>

### 4. String vs StringBuffer vs StringBuilder 비교

| 특성          | String      | StringBuffer   | StringBuilder  |
|--------------|------------|---------------|---------------|
| **가변성**   | 불변(Immutable) | 가변(Mutable) | 가변(Mutable) |
| **스레드 안전성** | 안전(Thread-Safe) | 안전(Thread-Safe) | 비안전(Non-Thread-Safe) |
| **성능**     | 가장 느림 | 다소 빠름 | 가장 빠름 |
| **사용 예시** | 변경이 거의 없는 문자열 | 멀티스레드 환경에서 문자열 변경 | 단일 스레드 환경에서 문자열 변경 |

---
<br>

### 5. 어떤 것을 선택해야 할까?
- **변경이 거의 없는 문자열** → `String`
- **멀티스레드 환경에서 문자열을 자주 변경** → `StringBuffer`
- **단일 스레드 환경에서 문자열을 자주 변경** → `StringBuilder`

#### Java String, StringBuffer, StringBuilder 정리
- `String`은 **불변 객체**로 메모리 낭비가 있을 수 있음.
- `StringBuffer`는 **멀티스레드 환경**에서 안정적이지만 동기화로 인해 성능이 다소 낮음.
- `StringBuilder`는 **단일 스레드 환경**에서 가장 빠름.
