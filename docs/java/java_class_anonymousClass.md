# **Java 무명 클래스 (Anonymous Class)**


<br>

## **1. 무명 클래스란?**
무명 클래스(Anonymous Class)는 **이름이 없는 클래스**로, 한 번만 사용할 객체를 정의할 때 사용됩니다.  
**추상 클래스나 인터페이스를 즉석에서 구현할 때 유용**하며, 코드의 간결성을 높여줍니다. 




<br>

## **2. 무명 클래스의 문법**
```java
클래스 또는 인터페이스 참조변수 = new 클래스 또는 인터페이스() {
    // 클래스의 구현 내용
};
```
- `new` 키워드 뒤에 오는 것은 일반적으로 **인터페이스나 추상 클래스**입니다.
- `{ ... }` 내부에서 해당 인터페이스나 추상 클래스를 구현합니다.





<br>

## **3. 사용 예시**

### **(1) 인터페이스 구현**
```java
interface Greeting {
    void sayHello();
}

public class AnonymousClassExample {
    public static void main(String[] args) {
        Greeting greeting = new Greeting() {
            @Override
            public void sayHello() {
                System.out.println("안녕하세요! 무명 클래스입니다.");
            }
        };

        greeting.sayHello();  // 출력: 안녕하세요! 무명 클래스입니다.
    }
}
```

### **(2) 추상 클래스 구현**
```java
abstract class Animal {
    abstract void makeSound();
}

public class AnonymousClassExample {
    public static void main(String[] args) {
        Animal dog = new Animal() {
            @Override
            void makeSound() {
                System.out.println("멍멍! 나는 강아지야.");
            }
        };

        dog.makeSound();  // 출력: 멍멍! 나는 강아지야.
    }
}
```

### **(3) 스레드 구현**
```java
public class AnonymousClassThreadExample {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("스레드가 실행됩니다.");
            }
        });

        thread.start();  // 출력: 스레드가 실행됩니다.
    }
}
```



<br>

## **4. 무명 클래스와 람다식**
- Java 8부터는 **람다식(Lambda Expression)**을 사용하여 무명 클래스를 대체할 수 있습니다.
- **단, 함수형 인터페이스(추상 메서드가 1개인 인터페이스)만 가능**합니다.

```java
// 기존 무명 클래스 방식
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("무명 클래스로 스레드 실행");
    }
};
new Thread(r).start();
```

```java
// 람다식 방식
Runnable r = () -> System.out.println("람다식으로 스레드 실행");
new Thread(r).start();
```



<br>

## **5. 무명 클래스의 장단점**
### ✅ **장점**
1. **코드 간결화**  
   → 클래스를 별도로 정의할 필요 없음.
2. **클래스 캡슐화**  
   → 특정 코드 블록에서만 사용되는 일회성 클래스를 정의 가능.
3. **유지보수 용이**  
   → 불필요한 클래스 파일 생성 없이 간단한 구현 가능.

### ❌ **단점**
1. **재사용 불가**  
   → 동일한 기능을 여러 번 사용하려면 중복 코드 발생.
2. **가독성 저하 가능성**  
   → 코드가 길어지면 이해하기 어려울 수 있음.
3. **디버깅 어려움**  
   → 무명 클래스의 예외 발생 시 추적이 어려움.



<br>

## **6. 결론**
| 항목 | 내용 |
|------|------|
| **정의** | 이름이 없는 클래스, 한 번만 사용할 객체를 위해 사용 |
| **사용 목적** | 인터페이스 또는 추상 클래스의 구현을 단순화 |
| **문법** | `new 인터페이스() { 구현 }` 또는 `new 추상클래스() { 구현 }` |
| **주요 사용처** | 이벤트 핸들러, 스레드, 콜백 처리 등 |
| **대체 가능 기술** | Java 8부터는 람다식으로 대체 가능 (단, 함수형 인터페이스만 가능) |

무명 클래스는 코드 간결화에 도움을 주지만, 필요 이상으로 남용하면 코드 가독성을 해칠 수 있습니다.  
따라서 **적절한 상황에서만 사용하는 것이 중요합니다!** 😊
