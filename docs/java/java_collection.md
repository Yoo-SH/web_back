# `JAVA 컬렉션 프레임워크`

## 1. 개요
Java Collection Framework는 데이터를 효율적으로 저장하고 조작할 수 있도록 설계된 표준화된 자료구조 라이브러리입니다. `java.util` 패키지에 포함되어 있으며, List, Set, Map 등의 다양한 인터페이스와 클래스를 제공합니다.

## 2. Collection 계층 구조
```
Iterable
  └─ Collection
      ├─ List
      |   ├─ ArrayList
      |   └─ LinkedList
      ├─ Set
      |   ├─ HashSet
      |   └─ TreeSet
      └─ Queue
          ├─ LinkedList
          └─ PriorityQueue

Map (Key-Value 구조, Collection 상속 X)
  ├─ HashMap
  ├─ LinkedHashMap
  └─ TreeMap
```


---

## 3. 주요 인터페이스 및 구현 클래스
### 3.1 List (순서 보장, 중복 허용)
| 클래스 | 특징 |
|--------|------|
| `ArrayList<E>` | 동적 배열, 빠른 검색(O(1)), 추가/삭제 느림(O(n)) |
| `LinkedList<E>` | 이중 연결 리스트, 빠른 추가/삭제(O(1)), 검색 느림(O(n)) |
| `Vector<E>` | `ArrayList`와 유사하지만 동기화(Synchronized) 지원 |
| `Stack<E>` | LIFO(Last In First Out) 구조 |

![Image](https://github.com/user-attachments/assets/927f289d-d1fc-4b7d-a74e-7e082fad08a1)

#### 사용 예제
```java
List<String> list = new ArrayList<>();
list.add("Apple");
list.add("Banana");
System.out.println(list.get(1)); // 출력: Banana
```

### 3.2 Set (중복 불가, 순서 보장 X)
| 클래스 | 특징 |
|--------|------|
| `HashSet<E>` | 중복 불가, 순서 없음, 빠른 검색(O(1)) |
| `LinkedHashSet<E>` | 입력 순서 유지 |
| `TreeSet<E>` | 정렬된 상태 유지, O(log n) |

#### 사용 예제
```java
Set<String> set = new HashSet<>();
set.add("Apple");
set.add("Banana");
System.out.println(set); // 출력 예시: [Apple, Banana]
```

### 3.3 Queue (FIFO 구조)
| 클래스 | 특징 |
|--------|------|
| `LinkedList<E>` | Queue로 사용 가능 (FIFO) |
| `PriorityQueue<E>` | 우선순위 큐, Comparator 설정 가능 |

#### 사용 예제
```java
Queue<Integer> queue = new LinkedList<>();
queue.offer(10);
queue.offer(20);
System.out.println(queue.poll()); // 출력: 10
```

### 3.4 Map (Key-Value 구조)
| 클래스 | 특징 |
|--------|------|
| `HashMap<K, V>` | Key 중복 불가, 순서 없음 |
| `LinkedHashMap<K, V>` | 입력 순서 유지 |
| `TreeMap<K, V>` | Key 기준 오름차순 정렬 |

#### 사용 예제
```java
Map<String, Integer> map = new HashMap<>();
map.put("Apple", 1000);
map.put("Banana", 1500);
System.out.println(map.get("Apple")); // 출력: 1000
```

---

## 4. Collections 클래스 (유틸리티 기능)
`java.util.Collections` 클래스는 Collection을 다룰 때 유용한 메서드를 제공합니다.

### 4.1 Collection 섞기 (Shuffling)
- `Collections.shuffle(List<T>)`를 사용하여 리스트의 요소를 랜덤하게 섞을 수 있습니다.
#### 사용 예제
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
Collections.shuffle(numbers);
System.out.println(numbers); // 출력: [3, 1, 5, 2, 4] (랜덤 순서)
```

### 4.2 Collection 탐색 (Searching)
- `Collections.binarySearch(List<T>, key)`을 사용하면 정렬된 리스트에서 이진 탐색을 수행할 수 있습니다.
#### 사용 예제
```java
List<Integer> numbers = Arrays.asList(1, 3, 5, 7, 9);
int index = Collections.binarySearch(numbers, 5);
System.out.println(index); // 출력: 2 (5의 인덱스 위치)
```

---

## 5. Stream API 활용
Java 8부터 제공되는 **Stream API**를 사용하면 컬렉션을 더욱 효율적으로 처리할 수 있습니다.

#### 사용 예제: 리스트에서 짝수만 필터링
```java
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
list.stream()
    .filter(n -> n % 2 == 0)
    .forEach(System.out::println); // 출력: 2 4 6
```

---

## 6. 정리
| 컬렉션 유형 | 중복 허용 | 순서 보장 | 대표 클래스 |
|------------|----------|----------|------------|
| List | O | O | `ArrayList`, `LinkedList` |
| Set | X | X (일부 제외) | `HashSet`, `TreeSet` |
| Queue | O | O | `LinkedList`, `PriorityQueue` |
| Map | Key: X, Value: O | X (일부 제외) | `HashMap`, `TreeMap` |

Java의 Collection Framework는 다양한 자료구조와 유틸리티 기능을 제공하여 데이터를 효율적으로 관리할 수 있도록 돕습니다. 🚀

