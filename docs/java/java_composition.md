# Java Composition

## 개요
Composition(구성)은 객체 지향 프로그래밍에서 코드 재사용성을 높이고, 유연성을 제공하는 중요한 개념입니다. 이는 하나의 클래스가 다른 클래스의 인스턴스를 포함하여 기능을 확장하는 방식입니다.

Composition은 상속(Inheritance)의 대안으로 사용되며, 상속보다 더 유연하고 유지보수하기 쉬운 구조를 제공합니다.

---

## 1. Composition이란?
Composition은 "has-a" 관계를 나타내며, 한 클래스가 다른 클래스의 객체를 포함하여 기능을 확장하는 기법입니다.

예를 들어, `Car` 클래스가 `Engine` 객체를 포함하고 있다면, 이는 "Car has an Engine" 관계를 나타냅니다.

```java
class Engine {
    void start() {
        System.out.println("Engine starting...");
    }
}

class Car {
    private Engine engine;

    public Car() {
        this.engine = new Engine();
    }

    public void startCar() {
        engine.start();
        System.out.println("Car started!");
    }
}

public class Main {
    public static void main(String[] args) {
        Car myCar = new Car();
        myCar.startCar();
    }
}
```

위 코드에서 `Car` 클래스는 `Engine` 객체를 포함하여 구성(Composition)되고 있습니다.

---

## 2. Composition vs Inheritance
| 비교 항목 | Composition | Inheritance |
|-----------|------------|-------------|
| 관계 유형 | has-a 관계 | is-a 관계 |
| 코드 재사용성 | 높음 | 높음 (단, 제한적) |
| 유연성 | 높음 (런타임 변경 가능) | 낮음 (컴파일타임 결정) |
| 유지보수성 | 쉬움 (의존성 낮음) | 어려움 (강한 결합) |
| 다중 사용 | 다중 구성 가능 | 다중 상속 불가 (Java는 단일 상속만 지원) |

Composition은 상속보다 결합도가 낮아, 변경이 용이하고 유지보수가 쉬운 장점이 있습니다.

---

## 3. Composition의 장점
1. **유연성 증가**: 객체 간 결합도가 낮아 유지보수가 쉬움
2. **코드 재사용성 증가**: 여러 클래스에서 같은 기능을 사용 가능
3. **다중 상속 문제 해결**: Java는 다중 상속을 지원하지 않지만, Composition을 사용하면 유사한 효과를 낼 수 있음
4. **런타임 변경 가능**: 객체의 구성을 동적으로 변경할 수 있음

---

## 4. Composition의 예제

### (1) 생성자를 통한 객체 주입
```java
class Engine {
    void start() {
        System.out.println("Engine is starting...");
    }
}

class Car {
    private Engine engine;

    public Car(Engine engine) { // 외부에서 엔진을 주입받음
        this.engine = engine;
    }

    public void startCar() {
        engine.start();
        System.out.println("Car is running...");
    }
}

public class Main {
    public static void main(String[] args) {
        Engine myEngine = new Engine();
        Car myCar = new Car(myEngine); // 생성자 주입
        myCar.startCar();
    }
}
```

### (2) Setter 메서드를 통한 객체 주입
```java
class Engine {
    void start() {
        System.out.println("Engine is starting...");
    }
}

class Car {
    private Engine engine;

    public void setEngine(Engine engine) { // Setter 주입 방식
        this.engine = engine;
    }

    public void startCar() {
        if (engine != null) {
            engine.start();
            System.out.println("Car is running...");
        } else {
            System.out.println("No engine found!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Engine myEngine = new Engine();
        Car myCar = new Car();
        myCar.setEngine(myEngine); // Setter 주입
        myCar.startCar();
    }
}
```

---

## 5. 요약
Composition은 객체 간 관계를 유연하게 만들고 유지보수를 쉽게 하며, 코드 재사용성을 높이는 강력한 기법입니다. Java에서 다중 상속을 피하면서도 객체 간 협력을 가능하게 하는 중요한 개념입니다. 따라서, 객체 간 관계를 설계할 때 상속보다 Composition을 우선적으로 고려하는 것이 좋습니다.

---

