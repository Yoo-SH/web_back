# `Java에서 Call by Reference? Call by Value of Reference!`

Java는 **Call by Value(값에 의한 호출)**만 지원하며, **Call by Reference(참조에 의한 호출)**는 직접적으로 지원하지 않습니다. 그러나 **참조 타입(Reference Type)**을 사용하면 마치 Call by Reference처럼 동작하는 것처럼 보일 수 있습니다.



<br>

## 1. Call by Value vs. Call by Reference

| 구분 | 설명 |
|------|------|
| **Call by Value (값에 의한 호출)** | 메서드에 전달된 인자의 **복사본**이 사용됨. 원본 값은 변경되지 않음. |
| **Call by Reference (참조에 의한 호출)** | 메서드에 객체의 참조(주소)가 전달되어, 원본 객체의 값이 변경될 수 있음. |



<br>

## 2. Java는 Call by Value만 지원

Java에서 기본 데이터 타입(`int`, `double`, `boolean` 등)은 Call by Value로 동작하여 메서드에서 값을 변경해도 원본 변수에는 영향을 주지 않습니다.

```java
public class CallByValueExample {
    public static void changeValue(int x) {
        x = 10;  // x의 값만 변경됨 (원본 값에는 영향 없음)
    }

    public static void main(String[] args) {
        int num = 5;
        changeValue(num);
        System.out.println(num);  // 5 (변경되지 않음)
    }
}
```

**출력 결과:**
```
5
```

`changeValue(num)`에서 `num`의 값이 변경된 것처럼 보이지만, 이는 **값이 복사되어 전달**된 것이므로 원본 변수에는 영향을 주지 않습니다.



<br>

## 3. 참조 타입을 이용한 Call by Reference처럼 보이는 동작

Java에서는 객체를 메서드 인자로 넘길 때 **참조값(주소 값)의 복사본**이 전달됩니다. 따라서 객체 내부의 필드는 변경될 수 있지만, 참조 자체를 변경해도 원본 변수에는 영향을 주지 않습니다.

```java
class Person {
    String name;
    
    Person(String name) {
        this.name = name;
    }
}

public class CallByReferenceExample {
    public static void changeName(Person p) {
        p.name = "Charlie";  // 참조된 객체의 필드를 변경 (원본 객체가 변경됨)
    }

    public static void main(String[] args) {
        Person person = new Person("Alice");
        changeName(person);
        System.out.println(person.name);  // "Charlie" (변경됨)
    }
}
```

**출력 결과:**
```
Charlie
```

위 코드에서는 `Person` 객체의 **참조값이 복사**되어 전달되지만, 참조된 객체의 필드를 변경할 수 있기 때문에 Call by Reference처럼 동작하는 것처럼 보입니다.



<br>

## 4. 참조 자체를 변경하려고 하면?

객체의 **참조 자체를 변경하려고 해도 원본 변수에는 영향이 없음**을 확인할 수 있습니다.

```java
public class ReferenceTest {
    public static void changeReference(Person p) {
        p = new Person("David");  // 새로운 객체를 할당 (원본 변수에는 영향 없음)
    }

    public static void main(String[] args) {
        Person person = new Person("Alice");
        changeReference(person);
        System.out.println(person.name);  // "Alice" (변경되지 않음)
    }
}
```

**출력 결과:**
```
Alice
```

메서드 내에서 `p`가 새로운 `Person` 객체를 참조하게 되었지만, 이는 `p`라는 **로컬 변수**가 변경된 것이고, 원본 변수 `person`에는 영향을 주지 않습니다.



<br>

## 5. 결론
- Java는 **Call by Value**만 지원한다.
- 기본 데이터 타입은 값이 복사되므로 원본 변수에 영향을 주지 않는다.
- 참조 타입(객체)은 **참조값이 복사되어 전달**되므로 객체의 내부 상태를 변경할 수 있다.
- 그러나 **참조 자체를 변경해도 원본 변수에는 영향을 주지 않는다**.

즉, Java에서는 Call by Reference처럼 보이는 동작이 가능하지만, 엄밀히 말하면 **참조의 값(Call by Value of Reference)을 전달하는 방식**입니다.


# 참고자료
- [tech-refrigerator/Language/JAVA/Promotion & Casting.md](https://github.com/GimunLee/tech-refrigerator/blob/master/Language/JAVA/Promotion%20%26%20Casting.md#promotion--casting)