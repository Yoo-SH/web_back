# `Java char 자료형과 관련된 메서드`

## 1. `isVowel(char c)` - 모음 판별
영어 알파벳에서 주어진 문자가 모음(A, E, I, O, U, a, e, i, o, u)인지 확인하는 메서드입니다.

### **구현 예시:**
```java
public static boolean isVowel(char c) {
    c = Character.toLowerCase(c);
    return "aeiou".indexOf(c) != -1;
}
```
### **사용 예시:**
```java
System.out.println(isVowel('A')); // true
System.out.println(isVowel('b')); // false
```

---

## 2. `isDigit(char c)` - 숫자 판별
주어진 문자가 숫자(0~9)인지 확인하는 메서드입니다.

### **구현 예시:**
```java
public static boolean isDigit(char c) {
    return Character.isDigit(c);
}
```
### **사용 예시:**
```java
System.out.println(isDigit('5')); // true
System.out.println(isDigit('A')); // false
```

---

## 3. `isConsonant(char c)` - 자음 판별
영어 알파벳에서 주어진 문자가 자음인지 확인하는 메서드입니다. (A, E, I, O, U를 제외한 알파벳)

### **구현 예시:**
```java
public static boolean isConsonant(char c) {
    return Character.isLetter(c) && !isVowel(c);
}
```
### **사용 예시:**
```java
System.out.println(isConsonant('B')); // true
System.out.println(isConsonant('e')); // false
```

---

## 4. `List Upper Case`
알파벳 대문자(A~Z)를 리스트로 반환하는 메서드입니다.

### **구현 예시:**
```java
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public static List<Character> listUpperCase() {
    return IntStream.rangeClosed('A', 'Z')
            .mapToObj(c -> (char) c)
            .collect(Collectors.toList());
}
```
### **사용 예시:**
```java
System.out.println(listUpperCase());
// 출력: [A, B, C, D, E, ..., Z]
```

---

## 5. `L`에 대한 판별
문자가 'L'인지 확인하는 간단한 메서드입니다.

### **구현 예시:**
```java
public static boolean isL(char c) {
    return c == 'L' || c == 'l';
}
```
### **사용 예시:**
```java
System.out.println(isL('L')); // true
System.out.println(isL('l')); // true
System.out.println(isL('M')); // false
```

