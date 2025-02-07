# Java Map 종류와 Spring Boot 사용 예제

Spring Boot에서 자주 사용하는 `Map` 인터페이스와 다양한 구현체(`HashMap`, `LinkedHashMap`, `TreeMap`)를 활용한 예제를 정리합니다.

---

## 1. **`HashMap`을 사용하는 예제**
`HashMap`은 **순서를 보장하지 않는** 키-값 쌍을 저장하는 컬렉션입니다. 내부적으로 해시 테이블(Hash Table)을 기반으로 빠른 검색, 삽입, 삭제를 제공합니다.

### 예제: 제품 정보 관리# Java Map 종류와 Spring Boot 사용 예제

Spring Boot에서 자주 사용하는 `Map` 인터페이스와 다양한 구현체(`HashMap`, `LinkedHashMap`, `TreeMap`)를 활용한 예제를 정리합니다.

---

## 1. **`HashMap`을 사용하는 예제**
`HashMap`은 **순서를 보장하지 않는** 키-값 쌍을 저장하는 컬렉션입니다. 내부적으로 해시 테이블(Hash Table)을 기반으로 빠른 검색, 삽입, 삭제를 제공합니다.

### 예제: 제품 정보 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hashmap")
public class HashMapController {

    private final Map<String, String> productMap = new HashMap<>();

    @PostMapping("/add")
    public Map<String, String> addProduct(@RequestParam String productId, @RequestParam String productName) {
        productMap.put(productId, productName); // 제품 추가
        return productMap;
    }

    @DeleteMapping("/remove")
    public Map<String, String> removeProduct(@RequestParam String productId) {
        productMap.remove(productId); // 제품 삭제
        return productMap;
    }

    @GetMapping("/all")
    public Map<String, String> getAllProducts() {
        return productMap; // 모든 제품 반환
    }
}
```

- **특징**:
    - 키와 값 모두 `null` 허용.
    - 순서를 보장하지 않음.
    - 빠른 삽입, 삭제 및 검색.

---

## 2. **`LinkedHashMap`을 사용하는 예제**
`LinkedHashMap`은 **삽입 순서**를 유지하는 해시 테이블 기반 Map입니다. 삽입된 순서나 접근된 순서를 기준으로 데이터를 관리할 수 있습니다.

### 예제: 요청 로그 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/linkedhashmap")
public class LinkedHashMapController {

    private final Map<String, String> requestLog = new LinkedHashMap<>();

    @PostMapping("/log")
    public Map<String, String> logRequest(@RequestParam String requestId, @RequestParam String requestDetails) {
        requestLog.put(requestId, requestDetails); // 요청 로그 추가
        return requestLog;
    }

    @DeleteMapping("/remove")
    public Map<String, String> removeLog(@RequestParam String requestId) {
        requestLog.remove(requestId); // 특정 로그 삭제
        return requestLog;
    }

    @GetMapping("/all")
    public Map<String, String> getAllLogs() {
        return requestLog; // 삽입된 순서대로 로그 반환
    }
}
```

- **특징**:
    - 삽입된 순서를 유지.
    - 키와 값 모두 `null` 허용.
    - 정렬이 필요하지 않고 순서가 중요한 경우 사용.

---

## 3. **`TreeMap`을 사용하는 예제**
`TreeMap`은 **정렬된 순서**로 키를 관리하는 Map입니다. 내부적으로 **레드-블랙 트리**를 기반으로 구현되며, 키는 자연 순서 또는 사용자 정의 `Comparator`에 따라 정렬됩니다.

### 예제: 사용자 점수 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/treemap")
public class TreeMapController {

    private final Map<String, Integer> userScores = new TreeMap<>();

    @PostMapping("/add")
    public Map<String, Integer> addScore(@RequestParam String username, @RequestParam int score) {
        userScores.put(username, score); // 사용자 점수 추가
        return userScores; // 키의 정렬된 순서 유지
    }

    @DeleteMapping("/remove")
    public Map<String, Integer> removeUser(@RequestParam String username) {
        userScores.remove(username); // 사용자 점수 삭제
        return userScores;
    }

    @GetMapping("/all")
    public Map<String, Integer> getAllScores() {
        return userScores; // 정렬된 사용자 점수 반환
    }
}
```

- **특징**:
    - 키를 기준으로 정렬된 순서를 유지.
    - `null` 키를 허용하지 않으며, `null` 값은 허용.
    - 검색과 정렬 작업이 효율적.

---

## 주요 `Map` 구현 클래스 요약

| Map 종류               | 특징                                                                 |
|------------------------|----------------------------------------------------------------------|
| `HashMap`              | 순서를 보장하지 않는 키-값 저장. 빠른 검색, 삽입, 삭제를 제공.       |
| `LinkedHashMap`        | 삽입된 순서 또는 접근된 순서를 유지.                                 |
| `TreeMap`              | 키를 기준으로 정렬된 순서를 유지. 레드-블랙 트리 기반.              |

---

### 추가 참고 사항:
- **`HashMap` vs `LinkedHashMap`**: 데이터 순서가 중요한 경우 `LinkedHashMap`을, 순서가 필요 없으면 `HashMap`을 사용.
- **`TreeMap`**: 정렬된 데이터를 유지하거나, 키 기반 탐색 연산이 중요한 경우 적합.
```java
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hashmap")
public class HashMapController {

    private final Map<String, String> productMap = new HashMap<>();

    @PostMapping("/add")
    public Map<String, String> addProduct(@RequestParam String productId, @RequestParam String productName) {
        productMap.put(productId, productName); // 제품 추가
        return productMap;
    }

    @DeleteMapping("/remove")
    public Map<String, String> removeProduct(@RequestParam String productId) {
        productMap.remove(productId); // 제품 삭제
        return productMap;
    }

    @GetMapping("/all")
    public Map<String, String> getAllProducts() {
        return productMap; // 모든 제품 반환
    }
}
```

- **특징**:
    - 키와 값 모두 `null` 허용.
    - 순서를 보장하지 않음.
    - 빠른 삽입, 삭제 및 검색.

---

## 2. **`LinkedHashMap`을 사용하는 예제**
`LinkedHashMap`은 **삽입 순서**를 유지하는 해시 테이블 기반 Map입니다. 삽입된 순서나 접근된 순서를 기준으로 데이터를 관리할 수 있습니다.

### 예제: 요청 로그 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/linkedhashmap")
public class LinkedHashMapController {

    private final Map<String, String> requestLog = new LinkedHashMap<>();

    @PostMapping("/log")
    public Map<String, String> logRequest(@RequestParam String requestId, @RequestParam String requestDetails) {
        requestLog.put(requestId, requestDetails); // 요청 로그 추가
        return requestLog;
    }

    @DeleteMapping("/remove")
    public Map<String, String> removeLog(@RequestParam String requestId) {
        requestLog.remove(requestId); // 특정 로그 삭제
        return requestLog;
    }

    @GetMapping("/all")
    public Map<String, String> getAllLogs() {
        return requestLog; // 삽입된 순서대로 로그 반환
    }
}
```

- **특징**:
    - 삽입된 순서를 유지.
    - 키와 값 모두 `null` 허용.
    - 정렬이 필요하지 않고 순서가 중요한 경우 사용.

---

## 3. **`TreeMap`을 사용하는 예제**
`TreeMap`은 **정렬된 순서**로 키를 관리하는 Map입니다. 내부적으로 **레드-블랙 트리**를 기반으로 구현되며, 키는 자연 순서 또는 사용자 정의 `Comparator`에 따라 정렬됩니다.

### 예제: 사용자 점수 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/treemap")
public class TreeMapController {

    private final Map<String, Integer> userScores = new TreeMap<>();

    @PostMapping("/add")
    public Map<String, Integer> addScore(@RequestParam String username, @RequestParam int score) {
        userScores.put(username, score); // 사용자 점수 추가
        return userScores; // 키의 정렬된 순서 유지
    }

    @DeleteMapping("/remove")
    public Map<String, Integer> removeUser(@RequestParam String username) {
        userScores.remove(username); // 사용자 점수 삭제
        return userScores;
    }

    @GetMapping("/all")
    public Map<String, Integer> getAllScores() {
        return userScores; // 정렬된 사용자 점수 반환
    }
}
```

- **특징**:
    - 키를 기준으로 정렬된 순서를 유지.
    - `null` 키를 허용하지 않으며, `null` 값은 허용.
    - 검색과 정렬 작업이 효율적.

---

## 주요 `Map` 구현 클래스 요약

| Map 종류               | 특징                                                                 |
|------------------------|----------------------------------------------------------------------|
| `HashMap`              | 순서를 보장하지 않는 키-값 저장. 빠른 검색, 삽입, 삭제를 제공.       |
| `LinkedHashMap`        | 삽입된 순서 또는 접근된 순서를 유지.                                 |
| `TreeMap`              | 키를 기준으로 정렬된 순서를 유지. 레드-블랙 트리 기반.              |

---

### 추가 참고 사항:
- **`HashMap` vs `LinkedHashMap`**: 데이터 순서가 중요한 경우 `LinkedHashMap`을, 순서가 필요 없으면 `HashMap`을 사용.
- **`TreeMap`**: 정렬된 데이터를 유지하거나, 키 기반 탐색 연산이 중요한 경우 적합.