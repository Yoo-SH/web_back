# Java List 종류와 Spring Boot 사용 예제

Spring Boot에서 Java의 다양한 `List` 종류들을 사용하는 방법과 각각의 주요 구현 클래스(`ArrayList`, `LinkedList`, `CopyOnWriteArrayList`)의 특징 및 예제를 정리합니다.

---

## 1. **`ArrayList`를 사용하는 예제**
`ArrayList`는 크기가 가변적인 배열로 구현된 `List`입니다. 읽기 속도가 빠르고, 순차적인 데이터 추가/삭제에 적합합니다.

### 예제: 기본 CRUD 기능 구현
```java
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/arraylist")
public class ArrayListController {

    private final List<String> items = new ArrayList<>();

    @PostMapping("/add")
    public List<String> addItem(@RequestParam String item) {
        items.add(item);
        return items;
    }

    @GetMapping("/get")
    public List<String> getItems() {
        return items;
    }

    @DeleteMapping("/delete")
    public List<String> deleteItem(@RequestParam String item) {
        items.remove(item);
        return items;
    }
}
```

- **특징**:
    - 순차적인 데이터 추가/삭제에 적합.
    - 임의 접근이 빠르지만, 중간 삽입/삭제에는 더 많은 연산이 필요합니다.

---

## 2. **`LinkedList`를 사용하는 예제**
`LinkedList`는 이중 연결 리스트로 구현된 `List`입니다. 삽입/삭제 작업이 빈번한 경우 적합합니다.

### 예제: 사용자 요청 데이터 처리
```java
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/linkedlist")
public class LinkedListController {

    private final List<String> logs = new LinkedList<>();

    @PostMapping("/log")
    public List<String> addLog(@RequestParam String log) {
        logs.add(0, log); // 최근 로그를 맨 앞에 추가
        return logs;
    }

    @GetMapping("/log")
    public List<String> getLogs() {
        return logs;
    }

    @DeleteMapping("/log")
    public List<String> removeLastLog() {
        if (!logs.isEmpty()) {
            logs.remove(logs.size() - 1);
        }
        return logs;
    }
}
```

- **특징**:
    - 삽입/삭제가 빈번할 경우 효율적.
    - 데이터를 탐색하는 경우, 배열 기반 처리보다 느릴 수 있음.

---

## 3. **`CopyOnWriteArrayList`를 사용하는 예제**
`CopyOnWriteArrayList`는 멀티스레드 환경에서 안전한 배열 기반 리스트입니다. 동시성 이슈가 있을 때 사용할 수 있습니다.

### 예제: 동시성 처리
```java
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/copyonwrite")
public class CopyOnWriteController {

    private final CopyOnWriteArrayList<String> messages = new CopyOnWriteArrayList<>();

    @PostMapping("/message")
    public List<String> addMessage(@RequestParam String message) {
        messages.add(message);
        return messages;
    }

    @GetMapping("/message")
    public List<String> getMessages() {
        return messages;
    }
}
```

- **특징**:
    - 읽기 작업이 많고, 쓰기 작업이 드문 경우 적합.
    - 쓰기 작업 시 새로운 배열을 복사하기 때문에 성능이 저하될 수 있음.

---

## 4. **`List`로 주입받아 사용하기**
Spring Boot에서는 `@Autowired`를 통해 여러 빈(Bean)을 리스트로 주입받아 사용할 수도 있습니다. 이는 Spring의 DI(의존성 주입)의 강력한 기능 중 하나입니다.

### 예제: `@Service` 빈 수집
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ListService {
    private final List<CustomService> services;

    @Autowired
    public ListService(List<CustomService> services) {
        this.services = services;
    }

    public void executeAllServices() {
        for (CustomService service : services) {
            service.performTask();
        }
    }
}

@Service
class CustomService {
    public void performTask() {
        System.out.println("Task performed by " + this.getClass().getSimpleName());
    }
}
```

- **특징**:
    - 여러 구현체를 자동으로 주입받아 처리 가능.
    - 스프링의 DI 컨테이너를 활용하여 생산성 향상.

---

## 5. **`List`와 데이터베이스 연동**
Spring Data JPA와 함께 `List`를 이용하여 데이터를 저장하거나 불러올 수 있습니다.

### 예제: 데이터베이스에서 `List`로 조회
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import java.util.List;

@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Getters and Setters
}

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}

@RestController
@RequestMapping("/users")
class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsers(@RequestParam String name) {
        return userRepository.findByName(name);
    }
}
```

- **특징**:
    - Spring Data JPA를 활용해 데이터베이스 연동 시 `List`를 바로 반환 가능.

---

---

## 6. **`Vector`를 사용하는 예제**
`Vector`는 `ArrayList`와 비슷한 방식으로 동작하지만 **스레드 안전(thread-safe)**합니다. 내부적으로 동기화를 제공하기 때문에 멀티스레드 환경에서 사용됩니다.

### 예제: 멀티스레드 환경에서 데이터 처리
```java
import org.springframework.web.bind.annotation.*;
import java.util.Vector;

@RestController
@RequestMapping("/vector")
public class VectorController {

    private final Vector<String> notifications = new Vector<>();

    @PostMapping("/add")
    public Vector<String> addNotification(@RequestParam String notification) {
        notifications.add(notification);
        return notifications;
    }

    @GetMapping("/all")
    public Vector<String> getAllNotifications() {
        return notifications;
    }

    @DeleteMapping("/remove")
    public Vector<String> removeNotification(@RequestParam String notification) {
        notifications.remove(notification);
        return notifications;
    }
}
```

- **특징**:
    - 멀티스레드 환경에서 안전(synchronized).
    - 스레드 동기화를 기본적으로 지원하지만, 성능이 상대적으로 느릴 수 있음.

---

## 7. **`Stack`을 사용하는 예제**
`Stack`은 **LIFO(Last In First Out)** 구조를 제공하는 클래스로, `Vector`를 확장하여 구현됩니다. 데이터의 후입선출(Last-In-First-Out) 방식으로 작업할 때 사용됩니다.

### 예제: 요청 기록 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.Stack;

@RestController
@RequestMapping("/stack")
public class StackController {

    private final Stack<String> history = new Stack<>();

    @PostMapping("/push")
    public Stack<String> pushHistory(@RequestParam String action) {
        history.push(action);
        return history;
    }

    @GetMapping("/pop")
    public String popHistory() {
        if (history.isEmpty()) {
            return "No actions in history.";
        }
        return history.pop();
    }

    @GetMapping("/peek")
    public String peekHistory() {
        if (history.isEmpty()) {
            return "No actions in history.";
        }
        return history.peek();
    }

    @GetMapping("/all")
    public Stack<String> getAllHistory() {
        return history;
    }
}
```

- **특징**:
    - LIFO 구조로 데이터 관리.
    - `push()`, `pop()`, `peek()` 등 스택 연산 메서드를 제공.

---

## 주요 `Vector`와 `Stack` 비교

| 클래스      | 특징                                                                               |
|-------------|------------------------------------------------------------------------------------|
| `Vector`    | 동기화된 배열 기반 리스트. 멀티스레드 환경에서 사용 가능.                          |
| `Stack`     | `Vector`를 확장하여 구현된 LIFO 구조. 데이터의 후입선출 작업에 적합.               |

---

### 예제 요약
Spring Boot에서는 `Vector`와 `Stack`을 활용하여 간단히 멀티스레드 작업이나 LIFO 작업을 처리할 수 있습니다. 하지만, 필요하지 않은 경우 최신 컬렉션(`ArrayList`, `Deque`, `ConcurrentLinkedQueue` 등)을 사용하는 것을 권장합니다.
## 주요 `List` 구현 클래스 요약

| List 종류              | 특징                                                                 |
|------------------------|----------------------------------------------------------------------|
| `ArrayList`            | 배열 기반. 탐색 속도가 빠르고, 순차 삽입/삭제에 적합.               |
| `LinkedList`           | 노드 기반. 중간 삽입/삭제가 빈번한 경우 효율적.                    |
| `CopyOnWriteArrayList` | 동기화된 리스트. 읽기가 많은 멀티스레드 환경에서 적합.              |
| 주입받는 `List`         | Spring DI를 통해 여러 `Bean`을 리스트로 주입받을 때 사용.           |
| `Vector`               | 동기화된 배열 기반 리스트. 멀티스레드 환경에서 사용 가능.            |
| `Stack`                | `Vector`를 확장하여 구현된 LIFO 구조. 데이터의 후입선출 작업에 적합. |0