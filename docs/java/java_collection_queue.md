# Java Queue 종류와 Spring Boot 사용 예제

Spring Boot에서 `Queue` 인터페이스와 다양한 구현 클래스들을 사용하는 방법을 정리합니다. 특히 `PriorityQueue`와 `LinkedList`에 대한 사용 예제를 중심으로 설명합니다.

---

## 1. **`PriorityQueue`를 사용하는 예제**
`PriorityQueue`는 **우선순위 큐**를 제공하며, 요소들이 우선순위에 따라 자동으로 정렬됩니다. 기본적으로는 자연 순서를 따르며, 필요에 따라 `Comparator`를 제공하여 커스텀 정렬도 가능합니다.

### 예제: 우선순위 기반의 작업 처리
```java
import org.springframework.web.bind.annotation.*;
import java.util.PriorityQueue;
import java.util.Queue;

@RestController
@RequestMapping("/priorityqueue")
public class PriorityQueueController {

    private final Queue<Task> tasks = new PriorityQueue<>();

    @PostMapping("/add")
    public Queue<Task> addTask(@RequestParam String description, @RequestParam int priority) {
        tasks.add(new Task(description, priority));
        return tasks;
    }

    @GetMapping("/process")
    public Task processTask() {
        return tasks.poll(); // 우선순위가 가장 높은 작업 처리
    }

    @GetMapping("/all")
    public Queue<Task> getAllTasks() {
        return tasks;
    }

    static class Task implements Comparable<Task> {
        private final String description;
        private final int priority;

        public Task(String description, int priority) {
            this.description = description;
            this.priority = priority;
        }

        public String getDescription() {
            return description;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(Task other) {
            return Integer.compare(this.priority, other.priority); // 우선순위 정렬 (작은 값이 높은 우선순위)
        }

        @Override
        public String toString() {
            return "Task{" +
                    "description='" + description + '\'' +
                    ", priority=" + priority +
                    '}';
        }
    }
}
```

- **특징**:
    - 우선순위 기반 정렬 제공.
    - 정렬 기준 변경 시 `Comparator`를 활용 가능.
    - 순서 보장이 없어 FIFO가 아닌 우선순위 기준으로 동작.

---

## 2. **`LinkedList`를 `Queue`로 사용하는 예제**
`LinkedList`는 `Queue` 인터페이스를 구현하며, FIFO(First In First Out) 구조로 사용할 수 있습니다.

### 예제: 요청 대기열 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.Queue;

@RestController
@RequestMapping("/linkedlistqueue")
public class LinkedListQueueController {

    private final Queue<String> requestQueue = new LinkedList<>();

    @PostMapping("/enqueue")
    public Queue<String> enqueueRequest(@RequestParam String request) {
        requestQueue.add(request); // 큐에 요청 추가
        return requestQueue;
    }

    @GetMapping("/dequeue")
    public String dequeueRequest() {
        return requestQueue.poll(); // 큐에서 요청 제거 및 반환 (FIFO)
    }

    @GetMapping("/peek")
    public String peekRequest() {
        return requestQueue.peek(); // 큐의 첫 번째 요소 조회
    }

    @GetMapping("/all")
    public Queue<String> getAllRequests() {
        return requestQueue;
    }
}
```

- **특징**:
    - FIFO 기반 큐로 동작.
    - 삽입/삭제 작업이 빈번할 경우 `ArrayList`보다 효율적.

---

## 3. **`ArrayDeque`를 사용하는 예제**
`ArrayDeque`는 큐와 덱(Deque)을 모두 지원하며, 크기 제한 없이 사용이 가능합니다.

### 예제: 양방향 작업 대기열 처리
```java
import org.springframework.web.bind.annotation.*;
import java.util.ArrayDeque;
import java.util.Deque;

@RestController
@RequestMapping("/arraydeque")
public class ArrayDequeController {

    private final Deque<String> deque = new ArrayDeque<>();

    @PostMapping("/addFirst")
    public Deque<String> addFirst(@RequestParam String element) {
        deque.addFirst(element); // 큐의 맨 앞에 삽입
        return deque;
    }

    @PostMapping("/addLast")
    public Deque<String> addLast(@RequestParam String element) {
        deque.addLast(element); // 큐의 맨 뒤에 삽입
        return deque;
    }

    @GetMapping("/removeFirst")
    public String removeFirst() {
        return deque.pollFirst(); // 큐의 맨 앞 요소 제거
    }

    @GetMapping("/removeLast")
    public String removeLast() {
        return deque.pollLast(); // 큐의 맨 뒤 요소 제거
    }

    @GetMapping("/all")
    public Deque<String> getAllElements() {
        return deque;
    }
}
```

- **특징**:
    - 양방향 삽입/삭제 지원.
    - 스레드 안전하지 않음(멀티스레드 환경에서는 `ConcurrentLinkedDeque` 사용 권장).

---

## 주요 `Queue` 구현 클래스 요약

| Queue 종류              | 특징                                                                 |
|-------------------------|----------------------------------------------------------------------|
| `PriorityQueue`         | 우선순위 기반 정렬 제공. FIFO 대신 우선순위대로 처리.                |
| `LinkedList`            | FIFO 구조를 지원하는 기본적인 큐. 삽입/삭제가 빈번한 경우 적합.      |
| `ArrayDeque`            | 양방향 삽입/삭제를 지원하는 배열 기반 큐.                            |
| `ConcurrentLinkedQueue` | 비블로킹(non-blocking) 큐. 멀티스레드 환경에서 적합.                 |

---