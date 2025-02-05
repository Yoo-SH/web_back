# `JAVA 이벤트 처리`

## 1. 이벤트 구동 프로그래밍(Event-Driven Programming)
프로그래밍 실행이 이벤트 발생에 의해 결정되는 방식

## 2. 이벤트 리스너
이벤트 발생 감지 후 발생한 이벤트를 처리하는 객체

## 3. 이벤트 리스너 작성 방법
__1. 이벤트 리스너 클래스 작성__
```java
class MyListener implements ActionListener {
 public voidactionPerformed(ActionEvent e) {
 ... // Action 이벤트를처리하는코드가여기에들어간다.
 }
}
```
__2. 이벤트 리스너를 이벤트 소스에 등록__
```java
public classMyFrame extendsJFrame {
    public MyFrame()  // 생성자에서컴포넌트를생성하고추가한다.
    {   
        button = newJButton("동작");  // 버튼생성
        button.addActionListener(new MyListener());
        ...
    }
}
```
## 4. 이벤트 처리 방법

다음은 Java에서 이벤트를 처리하는 다양한 방법에 대해 설명합니다.

### 4-1. 독립적인 클래스로 이벤트 핸들러 작성
이벤트 처리를 위해 별도의 클래스를 생성하여 구현하는 방법입니다.

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class MyEventHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        System.out.println("버튼 클릭됨!");
    }
}

public class IndependentHandlerExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("독립적인 클래스 이벤트 처리");
        JButton button = new JButton("클릭");
        button.addActionListener(new MyEventHandler());
        
        frame.add(button);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
```

### 4-2. 내부 클래스로 이벤트 핸들러 작성
프레임 클래스 내부에 이벤트 핸들러 클래스를 작성하는 방법입니다.

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InnerClassHandlerExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("내부 클래스 이벤트 처리");
        JButton button = new JButton("클릭");
        
        class MyEventHandler implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                System.out.println("버튼 클릭됨!");
            }
        }
        
        button.addActionListener(new MyEventHandler());
        
        frame.add(button);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
```

### 4-3. 프레임 클래스에서 이벤트 처리 구현
프레임 클래스 자체에서 `ActionListener` 인터페이스를 구현하는 방법입니다.

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrameHandlerExample extends JFrame implements ActionListener {
    JButton button;
    
    public FrameHandlerExample() {
        button = new JButton("클릭");
        button.addActionListener(this);
        
        add(button);
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        System.out.println("버튼 클릭됨!");
    }
    
    public static void main(String[] args) {
        new FrameHandlerExample();
    }
}
```

### 4-4. 무명 클래스를 사용하는 방법
이벤트 핸들러를 무명 클래스로 정의하는 방법입니다.

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnonymousClassHandlerExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("무명 클래스 이벤트 처리");
        JButton button = new JButton("클릭");
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("버튼 클릭됨!");
            }
        });
        
        frame.add(button);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
```

### 4-5. 람다식을 이용하는 방법
Java 8부터 지원되는 람다식을 사용하여 간결한 이벤트 처리를 구현할 수 있습니다.

```java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LambdaHandlerExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("람다식 이벤트 처리");
        JButton button = new JButton("클릭");
        
        button.addActionListener(e -> System.out.println("버튼 클릭됨!"));
        
        frame.add(button);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
```
__특징 요약__
- **독립적인 클래스**: 코드의 재사용성과 가독성이 높음.
- **내부 클래스**: 특정 프레임과 밀접한 이벤트 처리가 필요할 때 유용.
- **프레임 클래스 구현**: 간단한 GUI 애플리케이션에서 사용 가능하지만, 유지보수성은 떨어짐.
- **무명 클래스**: 간결하지만 코드가 길어질 경우 가독성이 낮아짐.
- **람다식**: 가장 간결한 방법으로 Java 8 이상에서 사용 가능.
