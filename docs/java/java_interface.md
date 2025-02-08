# `JAVA 인터페이스`

<br>

## 1. 인터페이스(Interface)란?
인터페이스(Interface)는 추상 메서드와 상수로만 구성된 클래스로, 객체의 사용 방법을 정의합니다. 인터페이스는 다중 상속을 지원하며, 클래스와 클래스 간의 관계를 정의할 때 사용되며 필요한 메서드 변수에 대하여 서로 합의하는 것입니다.

__인터페이스의 특징__
- 추상 메서드와 상수로만 구성된 클래스(abstract keyword 필요없음), JDK8 이후 부터는 디폴트 메소드 및 정적메소드 추가가능


```java
public interface RemoteControl {
    public void turnOn();
    public void turnOff();
}
```

- 인터페이스만으로 객체를 생성할 수 없음, 다른 클래스에서 구현(implements)되어야함

```java
public class Television implements RemoteControl {
    public void turnOn() {
        System.out.println("TV를 켭니다.");
    }
    public void turnOff() {
        System.out.println("TV를 끕니다.");
    }
}
```
<br>

## 2. 추상 클래스 vs 인터페이스

- 클래스는 오로지 하나의 추상 클래스만 상속받을 수 있지만, 인터페이스는 여러개 구현할 수 있음
- 추상 클래스는 Is-A 관계를 표현할 때 사용되고, 인터페이스는 Has-A 관계를 표현할 때 사용됨
- 추상 클래스는 일반 맴버 변수와 메서드를 가질 수 있지만, 인터페이스는 상수와 추상 메서드만 가질 수 있음
- 인터페이스 모든 메서드는 public abstract로 선언되어야 함.

<br>

## 3. 인터페이스와 타입
인터페이스는 타입으로 사용될 수 있으며, 인터페이스 타입으로 선언된 변수는 인터페이스를 구현한 객체를 참조할 수 있습니다.

```java
/* rc는 인터페이스 타입으로 선언되었지만, Television 객체를 참조할 수 있음. rc tv 둘다 동일한 결과*/
RemoteControl rc = new Television();
rc.turnOn(); 
rc.turnOff(); 

Television tv = new Television();
tv.turnOn();
tv.turnOff();
```

<br>

## 4. 인터페이스의 상속
기존 인터페이스를 수정하는 경우 기존 클래스 사용 불가. 따라서   인터페이스도 인터페이스를 상속할 수 있도록 허용

```java
public interface RemoteControl {
    void turnOn();
    void turnOff();
}

public interface advancedRemoteControl extends RemoteControl {
    void setVolume(int volume);
}
```

<br>

## 5. 인터페이스의 다중 상속
인터페이스는 다중 상속을 지원하며, 여러 개의 인터페이스를 구현할 수 있습니다.

```java

public interface RemoteControl {
    void turnOn();
    void turnOff();
}

public interface Searchable {
    void search(String url);
}

public class SmartTelevision implements RemoteControl, Searchable {
    public void turnOn() {
        System.out.println("TV를 켭니다.");
    }
    public void turnOff() {
        System.out.println("TV를 끕니다.");
    }
    public void search(String url) {
        System.out.println(url + "을 검색합니다.");
    }
}
```

<br>

## 6. 디폴트 메서드(Default Method)
JDK8 이후부터 인터페이스에 디폴트 메서드를 추가할 수 있습니다. 디폴트 메서드는 인터페이스를 구현한 클래스에서 메서드를 재정의하지 않아도 사용할 수 있습니다. 또한 static 메서드도 추가할 수 있습니다.

```java
public interface RemoteControl {
    void turnOn();
    void turnOff();
    default void setMute(boolean mute) {
        if (mute) {
            System.out.println("무음 처리합니다.");
        } else {
            System.out.println("무음 해제합니다.");
        }
    }
    static void changeBattery() {
        System.out.println("건전지를 교환합니다.");
    }
}
```

<br>

## 7. 상수 정의
변수 정의시 자동으로 public static final 속성이 부여되어 상수가 됨
```java
public interface RemoteControl {
    int MAX_VOLUME = 10;
    int MIN_VOLUME = 0;
}
```

<br>

## 8. 무명 클래스(Anonymous Class)

클래스 몸체는 정의되지만 이름이 없는 클래스, 클래스를 정의하면서 동시에 객체를 생성하고 이름이 없기 때문에 한번만 사용할 수 있음

__사용하는 이유__
1. 코드 간결화: 별도의 클래스 정의 없이 한 번만 사용할 클래스를 직접 생성 가능.
2. GUI 이벤트 리스너: 이벤트 핸들러 등을 간편하게 구현 가능.
3. 일회성 상속: 특정 클래스를 확장하여 필요한 기능을 간단히 구현 가능.
4. 익명 객체 생성: 코드 가독성을 높이고 불필요한 클래스 파일 생성을 줄일 수 있음.


__특징__
- 참조하는 것이 클래스라면
  - 클래스의 기능을 사용하지 않고 해당 클래스를 상속받은 무명 클래스의 객체 생성 후 참조
- 참조하는 것이 인터페이스라면
  - 클래스의 기능을 사용하지 않고 해당 인터페이스를 구현한 무명 클래스의 객체 생성 후 참조


__인터페이스와 추상클래스를 이용하여 무명 클래스를 구현예제__
```java
//인터페이스
public interface InterfaceK {
 public void printName();
 public void printAge();
}

//추상클래스
public abstract class ClassK {
    public abstract void dummy(); 
}


public class Test {
 public static void main(String[] args) {

    
    ClassK classK = new ClassK() {
        public void dummy() {
            System.out.println("I'm a dummy.");
        }
    };

    InterfaceK intK = new InterfaceK() {
        public void printName() {
            System.out.println("My name is KDK.");
        }
 
    public void printAge() {
        System.out.println("I'm 60 years old.");
    }
    };

    classK.dummy();
    intK.printName();
    intK.printAge();
    }

}

```
<br>

## 9. 람다식
람다식(Lambda Expression)은 함수형 프로그래밍을 위해 자바 8부터 추가된 기능으로, 메서드를 하나의 식으로 표현한 것입니다. 람다식은 익명 함수로 메서드를 간단히 표현할 수 있으며, 함수형 인터페이스를 구현할 때 사용됩니다.

__사용하는 이유__
1. 코드를 간결하게 만들고 가독성을 향상시킨다.
2. 익명 클래스를 줄여 불필요한 코드 작성을 방지한다.
3. 함수형 프로그래밍을 지원하여 코드 재사용성을 높인다.
4. 스트림 API와 결합하여 병렬 처리를 쉽게 수행할 수 있다.

__람다식의 특징__
- 익명 함수: 이름이 없는 함수
- 나중에 실행될 목적으로 다른 곳에 전달될 수 있는 코드 블록
- 메서드가 필요한 곳에 간단히 메서드를 보낼 수 있음
- 함수를 하나의 객체인것처럼 간주하여 "함수형 프로그래밍"방식을 간접도입

__사용하면 좋은 경우__
1. 간결한 코드가 필요한 경우
2. 콜백 함수 또는 이벤트 핸들러를 사용할 때
3. 컬렉션(List, Map 등)의 데이터 처리(Stream API 활용)
4. 멀티스레드 환경에서 병렬 처리를 할 때

__람다식 형식__
```java
 () -> System.out.println("Hello World");
 (String s) -> { System.out.println(s); }
 () -> 69
 () -> { return 3.141592; };
```

<img src="https://github.com/user-attachments/assets/32bee5c2-f4e8-49a4-b6e4-34554e9f3a06" width="500">

### 함수 인터페이스(Functional Interface)와 람다식

함수 인터페이스(Functional Interface)는 하나의 추상 메서드를 가지고 있는 인터페이스로, 람다식을 사용하기 위한 인터페이스입니다.

__함수 인터페이스와 람다식 사용 예시__
```java
@FunctionalInterface
interface Calculator {
    int operate(int a, int b);
}

public class Main {
    public static void main(String[] args) {
        // 람다식 사용
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;

        System.out.println(add.operate(5, 3));     // 8
        System.out.println(multiply.operate(5, 3)); // 15
    }
}

```
