# Java Set 종류와 Spring Boot 사용 예제

Spring Boot에서 `Set` 인터페이스와 다양한 구현 클래스들을 사용하는 방법을 정리합니다. 특히 `HashSet`과 `TreeSet`에 대한 사용 예제를 중심으로 설명합니다.

---

## 1. **`HashSet`을 사용하는 예제**
`HashSet`은 **순서를 보장하지 않는** 컬렉션으로, 중복되지 않는 고유한 값을 저장합니다. 내부적으로 해시 테이블(Hash Table)을 사용하여 요소를 관리합니다.

### 예제: 사용자 이메일 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/hashset")
public class HashSetController {

    private final Set<String> emailSet = new HashSet<>();

    @PostMapping("/add")
    public Set<String> addEmail(@RequestParam String email) {
        emailSet.add(email); // 이메일 추가
        return emailSet;
    }

    @DeleteMapping("/remove")
    public Set<String> removeEmail(@RequestParam String email) {
        emailSet.remove(email); // 이메일 제거
        return emailSet;
    }

    @GetMapping("/all")
    public Set<String> getAllEmails() {
        return emailSet; // 모든 이메일 반환
    }
}
```

- **특징**:
    - 중복된 값은 자동으로 제거됨.
    - 삽입 순서를 보장하지 않음.
    - `null` 값을 허용.

---

## 2. **`TreeSet`을 사용하는 예제**
`TreeSet`은 **정렬된 순서**로 요소를 저장하며, 내부적으로 **이진 검색 트리**를 사용하여 관리합니다. 기본적으로는 요소의 자연 순서를 따르며, 사용자 정의 `Comparator`도 지원합니다.

### 예제: 정렬된 사용자 이름 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/treeset")
public class TreeSetController {

    private final Set<String> nameSet = new TreeSet<>();

    @PostMapping("/add")
    public Set<String> addName(@RequestParam String name) {
        nameSet.add(name); // 이름 추가
        return nameSet; // 정렬된 이름 반환
    }

    @DeleteMapping("/remove")
    public Set<String> removeName(@RequestParam String name) {
        nameSet.remove(name); // 이름 제거
        return nameSet; // 정렬된 이름 반환
    }

    @GetMapping("/all")
    public Set<String> getAllNames() {
        return nameSet; // 정렬 유지된 이름 반환
    }
}
```

- **특징**:
    - 요소가 자동으로 정렬된 상태로 저장됨.
    - 삽입이 느릴 수 있지만, 데이터 검색과 탐색을 효율적으로 처리.
    - `null` 값을 허용하지 않음.

---

## 3. **`LinkedHashSet`을 사용하는 예제**
`LinkedHashSet`은 **삽입된 순서를 보장**하는 컬렉션입니다. 내부적으로는 `HashSet`의 해싱 기능과 이중 연결 리스트를 조합하여 순서를 유지합니다.

### 예제: 순서가 중요한 태그 관리
```java
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@RequestMapping("/linkedhashset")
public class LinkedHashSetController {

    private final Set<String> tagSet = new LinkedHashSet<>();

    @PostMapping("/add")
    public Set<String> addTag(@RequestParam String tag) {
        tagSet.add(tag); // 태그 추가
        return tagSet; // 삽입된 순서 유지
    }

    @DeleteMapping("/remove")
    public Set<String> removeTag(@RequestParam String tag) {
        tagSet.remove(tag); // 태그 제거
        return tagSet;
    }

    @GetMapping("/all")
    public Set<String> getAllTags() {
        return tagSet; // 삽입된 순서대로 태그 반환
    }
}
```

- **특징**:
    - 삽입 순서를 유지.
    - `HashSet`보다 메모리를 더 사용하며, 약간 느릴 수 있음.

---

## 주요 `Set` 구현 클래스 요약

| Set 종류               | 특징                                                                 |
|------------------------|----------------------------------------------------------------------|
| `HashSet`              | 해시 기반으로 저장. 순서 보장하지 않으며, 중복된 값 방지.            |
| `TreeSet`              | 이진 검색 트리 기반으로 저장. 정렬된 순서를 보장.                   |
| `LinkedHashSet`        | 삽입된 순서를 보장하며, `HashSet`의 기능을 확장.                    |

---

### 추가 참고 사항:
- 기본적으로 `Set`은 중복 데이터를 허용하지 않습니다.
- 새로운 데이터가 지속적으로 추가되거나 삭제되는 경우 구현체의 선택에 따라 성능 차이가 발생할 수 있습니다. 데이터를 추가/제거하거나 유지하려는 방식에 따라 적합한 `Set` 구현체를 선택하세요.