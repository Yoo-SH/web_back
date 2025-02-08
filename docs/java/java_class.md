# `JAVA 클래스(Class)`

<br>

## 1. 클래스
클래스는 객체를 생성하기 위한 설계도로, 객체의 속성과 기능을 정의합니다. 클래스는 **필드(Field)**와 **메서드(Method)**로 구성되며, 객체를 생성하여 사용할 수 있습니다.

__장점__
1. 데이터와함수(메서드)가같은그룹으로묶이므로코드관리, 확장, 디버깅이용이해짐.
2. 객체지향프로그래밍(OOP)의 기본개념으로 코드재사용성, 유지보수성, 확장성이증가함.

__클래스의 구성요소__
1. 필드(Field): 객체의 속성을 나타내는 변수
    - 필드는 명시적으로 초기화 하지 않으면 기본값으로 초기화됨(힙 영역에는 빈공간으로 저장될 수 없음)
    - 참조 자료형(객체)는 null, 기본 자료형은 0으로 초기화됨
2. 메서드(Method): 객체의 기능을 나타내는 함수
    - 내부에서는 객체 생성없이 사용 가능
    - 외부에서는 객체 생성 후 사용 가능
3. 생성자(Constructor): 객체 생성 시 초기화를 위한 메서드
4. 내부 클래스(Inner Class): 클래스 내부에 선언된 클래스

<br>

## 2. 클래스를 포함하는 java 소스 파일 구조
* __패키지(package)__: 여러 클래스를 묶은 라이브러리 단위
* __임포트(import)__: 다른 패키지의 클래스를 사용하기 위한 선언

    ```java
    package com.easyspub;
    import java.util.Date;
    public class MyClass {
    // 클래스 내용
    }
    ```
* <br>

## 3. 객체(Object) 생성하기
모든 클래스는 객체를 생성하여 사용할 수 있습니다. 객체를 생성하기 위해서는 **new 키워드**를 사용합니다.

```java
// 클래스명 객체명 = new 생성자();
ClassName obj = new ClassName();
```

<br>

## 4. 객체 생성에 따른 메모리 구조
객체 생성 시 메모리 구조 및 객체 참조변수의 위치
* 객체 참조변수는 스택
* 객체는 힙 메모리에 생성됨
* 메서드 구현 코드는 인스턴스 메서드(클래스 영역)에 저장됨


<img src="https://github.com/user-attachments/assets/5760589f-99a0-49c0-8047-c3a003a4d5ce" width="500">

__자동차 클래스 예시__

<img src="https://github.com/user-attachments/assets/27b61366-512c-4b13-9bc6-13bfb2a802dd" width="500">

<br>

## 5. 포인트 연산자를 이용한 객체 접근
객체의 필드와 메서드에 접근하기 위해서는 **포인트 연산자(.)**를 사용합니다. (힙 영역에 접근하기 위해서는 포인트 연산자를 사용해야 함)


```java
Car myCar = new Car();
myCar.color = "Red";
```
<br>

## 6. 접근 제어자(Access Modifier)

접근 제어자는 클래스, 필드, 메서드 등의 접근 권한을 제어하는 키워드입니다. 접근 제어자는 다음과 같이 사용됩니다.

- **public**: 모든 패키지에서 접근 가능
- **protected**: 클래스, 하위 클래스에서 접근 가능
- **private**: 클래스에서만 접근 가능 

<br>

## 7. setter, getter 메서드

setter, getter 메서드는 객체의 필드에 접근하여 값을 설정하거나 반환하는 메서드입니다. setter 메서드는 객체의 필드 값을 설정하고, getter 메서드는 객체의 필드 값을 반환합니다.


- setter 메서드: 객체의 필드 값을 설정하는 메서드
    - setXXX()형식
    - 예시: setAge(), setName()
- getter 메서드: 객체의 필드 값을 반환하는 메서드
    - getXXX()형식
    - 예시: getAge(), getName()

__장점__
1. 필드에 직접 접근하지 않고 메서드를 통해 접근하므로 데이터를 보호할 수 있음. 읽기만 가능하게 할 수도 있음
2. 필드의 값을 제한하거나 조건을 검사할 수 있음

<br>

## 8. 메서드 오버로딩(Method Overloading)

매서드 오버로딩은 같은 이름의 메서드를 여러 개 정의하는 것을 말합니다. `매개변수의 타입`이나 `메서드 이름`이 다르면 같은 이름의 메서드를 여러 개 정의할 수 있습니다.

```java
public static void print(){}
public static void print(int a){}
public static void print(double a){}
public static void print(String a){}

//주의: 리턴타입이 다른 경우는 오버로딩이 아님. 
//리턴타입은 메서드 시그니처(메서드 이름, 매개변수 타입과 개수)가 아님
public static int test(){} 
public static void test(){} //error
```

<br>

## 9. 가변길이 매개변수 매서드

가변길이 매개변수 메서드는 매개변수의 개수가 정해지지 않은 메서드를 의미합니다. 가변길이 매개변수 메서드는 `타입... 변수명` 형식으로 선언합니다.

```java
public static int sum(int... values) {
    int sum = 0;
    for (int value : values) {
        sum += value;
    }
    return sum;
}

public static void main(String[] args) {
    System.out.println(sum(1, 2, 3)); // 6
    System.out.println(sum(1, 2, 3, 4, 5)); // 15
}
```
<br>

## 10. 생성자(Constructor)
객체가 생성될 때에 필드에게 초기값을 제공하고 그 외 필요한 초기화 절차를 실행하는 메소드

__생성자의 특징__
1. 객체 생성를 생성하고 객체내의 필드를 초기화하는 역할을 함
2. 클래스명과 동일한 이름을 가짐
3. 리턴타입이 없음

__생성자의 종류(생성자 오버로딩으로 여러개 생성 가능)__
1. 기본 생성자: 매개변수가 없는 생성자
2. 매개변수가 있는 생성자: 필드 초기화를 위한 매개변수를 가지는 생성자

<br>

## 11. this 키워드
모든 메서드에는 자신이 포함된 클래스의 객체를 가리키는
this 참조 변수가 존재함. this는 객체 자신을 가리키는 참조 변수로, 객체 내부에서 필드나 메서드를 참조할 때 사용합니다.

```java
public class Car {
    String color;
    int speed;

    public Car(String color, int speed) {
        this.color = color;
        this.speed = speed;
    }
}
```
<br>

## 12. this() 생성자
this() 생성자는 같은 클래스 내의 다른 생성자를 호출하는 역할을 합니다. this() 생성자는 다른 생성자를 호출할 때 사용하며, 반드시 생성자의 첫 줄에 위치해야 합니다.

```java
public class Car {
    String color;
    int speed;

    public Car() {
        this("Red", 100); // this() 생성자 호출
    }

    public Car(String color, int speed) {
        this.color = color;
        this.speed = speed;
    }
}
```
<br>

## 13. static 키워드
Java에서 static은 정적이라는 뜻을 가지고 있습니다. static 키워드는 클래스 멤버 (인스턴스 변수, 메소드)를 정적 멤버로 만듭니다.

static 멤버는 객체를 생성하지 않고도 클래스 이름으로 접근할 수 있으며, 모든 객체가 공유하는 값 또는 기능을 표현하는 데 사용됩니다. 즉, static 멤버는 클래스의 인스턴스에 속하는 멤버가 아닌 클래스 자체에 속하는 멤버입니다.

static 변수는 객체가 생성되기 이전에 클래스 로딩 시점에서 초기화됩니다. 따라서 static 변수는 모든 객체에서 동일한 값을 가지게 됩니다.

static 메소드는 객체가 아닌 클래스에 속한 메소드로, 클래스 이름으로 직접 호출할 수 있습니다. 따라서, static 메소드는 객체를 생성하지 않고도 클래스의 기능을 사용할 수 있습니다.

Java에서 static 키워드는 다음과 같은 용도로 사용됩니다.

클래스 변수(static 변수) 선언
클래스 메소드(static 메소드) 선언
static import를 사용하여 클래스의 static 멤버를 직접 import
하지만, static 키워드의 남발은 객체 지향적인 설계와는 맞지 않고 메모리를 많이 차지할 경우 효율성을 떨어트릴 수 있으니, 적절한 사용을 유지하는 것이 좋습니다.

### static 필드
<img src="https://github.com/user-attachments/assets/69c6fbca-819a-4ae6-b95b-2d2268c03160" width="500">


### static 필드와 instance 필드 메모리 구조
<img src="https://github.com/user-attachments/assets/c2b89143-3703-458a-a30f-371316a798cb" width="500"> 


### static 메서드
static 필드와 마찬가지로 객체 생성 없이 클래스 이름으로 직접 호출할 수 있습니다. static 메서드는 객체의 상태에 영향을 주지 않는 기능을 구현할 때 사용합니다.
```java
public class Calculator {
    public static int add(int a, int b) {
        return a + b;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println(Calculator.add(10, 20)); // 30
    }
}
```

### static 메서드 사용시 주의사항
메모리에 한번 할당되면 프로그램이 종료되기까지 지속되는 메모리라는 점에 주목하면서 이해. 즉 메모리 공유목적으로 사용되기 때문에 모든 영역에서 접근 가능하고 그로 인해 객체 자체를 생성할 필요도 없어진다. 또한, static 메서드는 객체 자체를 생성을 안하니 다른 인스턴스 메서드를 호출할 수 없고 this 키워드 또한 사용할 필요가 없어진다. 단, 다른 정적 메서드와 정적 필드는 사용이 가능하며 자기 안에 속한 지역 변수 또한 사용 가능하다.

```java
 package main;

 public class StaticTest {
    int fieldA;
    int fieldB;
    static int sFieldA;
    
    public staticvoid main(String[]args) {
        fieldA = 10; // error!
        fieldB = 20; // error!
        sFieldA = 30; // OK

        add(10, 20); // error!
        sub(10, 20); // OK
    }

    int add (int a, int b) {
        return a + b;
    }

    static int sub(int a, int b) {
        return a - b;
    }
 }

```

### static 초기화 블록
- static 필드를 초기화하기 위한 별도의 문법. 클래스가 로딩될 때 단 한 번 실행.

```java
 class Protagonist{
    static String name;
    static int age; staticint HP; static int MP;
    static String job; static String[] skills;
    
    // static 초기화 블록
    static {
        name = "KDK"; 
        age = 60; HP = 30; MP = 50;
        job = "magician";
    } 
}
```

<br>

## 14. 상속(Inheritance)

상속은 부모 클래스의 속성과 기능을 자식 클래스가 물려받는 것을 의미합니다. 상속을 통해 코드의 재사용성을 높이고, 유지보수성을 향상시킬 수 있습니다.

### 상속 구조
```java
public class Parent {
    int a;
    int b;
    public void print() {
        System.out.println("a: " + a + ", b: " + b);
    }
}

public class Child extends Parent {
    int c;
    public void print() {
        System.out.println("a: " + a + ", b: " + b + ", c: " + c);
    }
}
```

### 상속과 접근 제어
<img src="https://github.com/user-attachments/assets/beafe096-fcdb-4330-85f8-1020c47277df" width="500">

### 부모 클래스의 필드 상속
1. private 필드를 protected으로 바꿔서 상속
2. setter, getter 메서드를 protected으로 사용하여 접근

### 메서드 오버라이딩(Overriding)
부모 클래스의 메서드를 자식 클래스가 자신의 필요에 맞춰서 변경하는 것.

__메서드 오버라이딩 조건__
- 접근 지정자와 리턴타입 동일 
- 파라미터가 동일 해야함.(파라미터의 타입, 개수, 순서가 동일해야함. 다르면 오버로딩이 적용됨)

```java
public class Parent {
    public void print() {
        System.out.println("I'm Parent");
    }
}

public class Child extends Parent {
    @Override
    public void print() {
        System.out.println("I'm Child");
    }
}

public class Main {
    public static void main(String[] args) {
        Parent parent = new Parent();
        Child child = new Child();
        
        parent.print(); // I'm Parent
        child.print(); // I'm Child
    }
}
```
__주의__
- 정적 메서드(static)는 오버라이딩이 불가능함(정적 메서드는 호출시 참조 객체가 아닌 컴파일 시 결정된 클래스 타입을 고려)

### super 키워드
super 키워드는 부모 클래스의 객체를 가리키는 참조 변수로, 자식 클래스에서 부모 클래스의 필드나 메서드에 접근할 때 사용합니다.
super() 키워드는 부모 클래스의 생성자를 호출하는 역할을 합니다. super() 키워드는 자식 클래스의 생성자에서 부모 클래스의 생성자를 호출할 때 사용합니다.
cf) this, this()

__참고__ 
- this.~ , super.~은 객체 메모리 접근으로 직접적인 호출.
this(), super()은 생성자 호출. 
- super()과 this()는 생성자안에서만, 그리고 첫번째 문장으로만 사용 가능함
- 자식 클래스 객체 생성시,자식 클래스의 안의 부모클래스 부분을
 초기화하기 위하여 부모 클래스의 생성자가 호출(자식 클래스 생성 -> 부모 클래스 실행 -> 자식 클래스 실행)


__자식 생성자에 super() 키워드를 사용하지 않고 부모 클래스의 생성자를 호출 하는 경우__
```java
public class Parent {
    public Parent() {
        System.out.println("Parent 생성자 호출");
    }
    public Parent(int a) {
        System.out.println("Parent 생성자 호출 + a");
    }
}

public class Child extends Parent {
    public Child() {
        System.out.println("Child 생성자 호출");
    }
}

public class Main {
    public static void main(String[] args) {
        Child child = new Child(); 
        // Parent 생성자 호출, Child 생성자 호출
    }
}

```
__자식 생성자에 super() 키워드를 사용하여 부모 클래스의 생성자를 호출 하는 경우__

```java
public class Parent {
    public Parent() {
        System.out.println("Parent 생성자 호출");
    }
    public Parent(int a) {
        System.out.println("Parent 생성자 호출 + a");
    }
}

public class Child extends Parent {
    public Child() {
        super(10)
        System.out.println("Child 생성자 호출");
    }
}

public class Main {
    public static void main(String[] args) {
        Child child = new Child(); 
        // Parent 생성자 호출 + 10, Child 생성자 호출
    }
}

```
<br>

## 15. 다형성(Polymorphism)

다형성은 하나의 객체가 여러 가지 타입을 가질 수 있는 것을 의미합니다. 다형성은 상속과 함께 사용되며, 부모 클래스 타입의 참조 변수로 자식 클래스 객체를 참조할 수 있습니다.


### 다형성의 특징
- 부모 클래스 타입의 참조 변수로 자식 클래스 객체를 참조할 수 있음(업캐스팅), 반대로는 불가능함
- 자식 클래스의 타입이 부모보다 크기 떄문에 자식 클래스가 부모 객체를 참조하면 에러가 발생함
```java
Parent parent = new Child(); // OK
Child child = new Parent(); // error
```
- 자식 클래스 객체라하더라도 부모클래스 객체를 형변환 후 참조하는 경우는 가능
```java
Parent parent = new Child();
Child child = (Child)parent;
```



### 추상 클래스

추상 클래스는 추상 메서드를 포함하는 클래스로, 객체를 생성할 수 없습니다. 추상 클래스는 상속 전용으로 사용되며, 상속을 통해 자식 클래스에서 추상 메서드를 구현하도록 강제할 수 있습니다.

__추상 클래스의 특징__
- 상속 전용으로 사용됨
- 추상적인 개념을 포함하는데 활용되며 객체 생성이 불가능함
- abstract 키워드를 class 앞에 붙여서 선언
- abstract 메서드를 포함할 수 있음


```java
package main;
 abstract public class Shape { //추상 클래스
    int x, y;
   
    public void move(int x, inty) {
        this.x= x;
    this.y= y;
    }
   
    public abstract void draw(); //추상 메서드
 }

 class Rectangle extends Shape {
    int width, height;

    public void draw() {
        System.out.println("draw a rectangle");
    }
 }

 class Circle extends Shape {
    int radius;

    public void draw() {
        System.out.println("draw a circle");
    }
 };
 ```

### Object 클래스
Object 클래스는 모든 클래스의 최상위 클래스로, 모든 클래스는 Object 클래스를 상속받습니다. 따라서, 모든 클래스는 Object 클래스의 메서드를 사용할 수 있고 상황에 따라서 오버라이딩할 수 있다. 

__(extends Object) 어느 클래스에서나 상속하고 있으니 언제나 가지고 있는 함수!!__ 
<img src="https://github.com/user-attachments/assets/653c0983-31ad-4745-ad39-1cc750e29b7c" width="500">

### Is-A 관계 vs Has-A 관계

- Is-A 관계: 상속을 통해 부모 클래스의 속성과 기능을 물려받는 관계
    - 상속으로 모델링
    - 예시: 자동차는 탈 것이다(Car is a Vehicle)
    - 강아지는 동물이다(Dog is an Animal)
- Has-A 관계: 클래스가 다른 클래스를 포함하는 관계
    - 내부에 다른 클래스를 포함하여 
    - 예시: 자동차는 엔진을 가지고 있다(Car has an Engine)
    - 사람은 머리를 가지고 있다(Person has a Head)


<br>

### final class(종단 클래스)
final 클래스는 더 이상 상속할 수 없는 클래스로, final 클래스를 상속할 수 없습니다. final 클래스는 상속을 통해 클래스의 기능을 변경하거나 확장하는 것을 방지하기 위해 사용됩니다.

```java
final class Parent {
    // 클래스 내용
}

class Child extends Parent { // error
    // 클래스 내용
}
```
<br>

### final method(종단 메서드)
final 메서드는 더 이상 오버라이딩할 수 없는 메서드로, final 메서드를 오버라이딩할 수 없습니다. final 메서드는 부모 클래스에서 변경되지 않고 고정된 기능을 제공하기 위해 사용됩니다.

```java
class Parent {
    public final void print() {
        System.out.println("I'm Parent");
    }
}

class Child extends Parent {
    public void print() { // error
        System.out.println("I'm Child");
    }
}
```
