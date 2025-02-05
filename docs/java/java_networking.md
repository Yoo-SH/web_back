# `Java Network Programming`

Java 네트워크 프로그래밍에 대한 개요 및 주요 개념을 설명하는 문서입니다.

## 📌 개요
Java에서는 `java.net` 패키지를 사용하여 네트워크 프로그래밍을 수행할 수 있습니다. 소켓 프로그래밍, HTTP 통신, 원격 메소드 호출(RMI) 등의 기능을 제공합니다.

## 📚 주요 개념

### 1. 소켓 프로그래밍 (Socket Programming)
소켓(Socket)은 네트워크에서 데이터를 주고받기 위한 양쪽 끝(endpoint)입니다. Java에서는 `Socket` 클래스와 `ServerSocket` 클래스를 사용하여 TCP 기반의 네트워크 통신을 구현할 수 있습니다.

#### 🔹 TCP 소켓 통신
```java
// 서버 코드
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("서버가 시작되었습니다.");
        
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        String message = in.readLine();
        System.out.println("클라이언트: " + message);
        out.println("서버에서 받은 메시지: " + message);
        
        socket.close();
        serverSocket.close();
    }
}
```

```java
// 클라이언트 코드
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        out.println("안녕하세요, 서버!");
        System.out.println("서버 응답: " + in.readLine());
        
        socket.close();
    }
}
```

### 2. UDP 소켓 통신
UDP는 비연결형 프로토콜로 속도가 빠르지만 신뢰성이 낮습니다. `DatagramSocket`과 `DatagramPacket`을 사용하여 구현할 수 있습니다.

#### 🔹 UDP 서버
```java
import java.net.*;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(9876);
        byte[] buffer = new byte[1024];
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("클라이언트: " + received);
        
        socket.close();
    }
}
```

#### 🔹 UDP 클라이언트
```java
import java.net.*;

public class UDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = "안녕하세요, 서버!".getBytes();
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 9876);
        socket.send(packet);
        
        socket.close();
    }
}
```

### 3. HTTP 통신
Java에서는 `HttpURLConnection` 클래스를 사용하여 HTTP 요청을 보낼 수 있습니다.

```java
import java.io.*;
import java.net.*;

public class HttpExample {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
    }
}
```

### 4. 원격 메소드 호출 (RMI)
Java RMI(Remote Method Invocation)는 원격 객체를 호출하는 기술입니다.

- 서버에서 원격 객체를 등록하고 클라이언트에서 이를 호출하여 사용합니다.
- `java.rmi.*` 패키지를 활용합니다.

### 5. 멀티스레드 서버
여러 클라이언트의 요청을 동시에 처리하려면 멀티스레딩이 필요합니다.

```java
import java.io.*;
import java.net.*;

class ClientHandler extends Thread {
    private Socket socket;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            String message = in.readLine();
            System.out.println("클라이언트: " + message);
            out.println("서버에서 받은 메시지: " + message);
            
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("멀티스레드 서버가 시작되었습니다.");
        
        while (true) {
            Socket socket = serverSocket.accept();
            new ClientHandler(socket).start();
        }
    }
}
```

## 📌 정리
Java 네트워크 프로그래밍의 핵심 내용은 다음과 같습니다:
- `Socket` 및 `ServerSocket`을 활용한 TCP 통신
- `DatagramSocket`을 이용한 UDP 통신
- `HttpURLConnection`을 활용한 HTTP 요청 및 응답 처리
- `java.rmi`를 사용한 원격 메소드 호출
- 멀티스레드 서버를 활용한 동시성 처리

Java 네트워크 프로그래밍을 활용하면 다양한 분산 애플리케이션을 개발할 수 있습니다. 🚀


# `참고자료`
- [do it 자바 완전정복(진짜 개발자자가 되는 Java 프로그래밍 입문서)](https://www.easyspub.co.kr/20_Menu/BookView/456/PUB)



