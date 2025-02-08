# Java Stream API

<br>

## 1. Stream이란?
Java의 **Stream API**는 컬렉션(List, Set 등) 데이터를 효율적으로 처리하기 위한 기능으로, Java 8에서 도입되었습니다. 함수형 프로그래밍 스타일을 지원하여 가독성이 높고 유지보수가 쉬운 코드를 작성할 수 있습니다.



<br>

## 2. Stream vs Collection
| 비교 항목 | Collection | Stream |
|-----------|-----------|--------|
| 데이터 저장 여부 | 데이터를 저장 | 데이터를 저장하지 않음 (데이터의 흐름) |
| 요소 접근 방식 | 외부 반복(반복문 사용) | 내부 반복(자동 처리) |
| 연산 방식 | 즉시 수행 | 지연 수행(Lazy Evaluation) |
| 원본 변경 여부 | 가능 | 불가능 (Immutable) |
| 병렬 처리 | 직접 구현해야 함 | `parallelStream()` 지원 |

### 차이가 발생하는 이유
- **데이터 저장 여부**: Collection은 메모리에 데이터를 보관하지만, Stream은 데이터를 **즉시 소비하면서 처리**하므로 저장하지 않습니다.
- **요소 접근 방식**: Collection은 사용자가 직접 반복문을 사용하여 데이터를 순회하지만, Stream은 **내부적으로 자동 처리**하여 더 최적화된 방식으로 데이터를 처리할 수 있습니다.
- **연산 방식**: Collection의 연산은 즉시 수행되며 변경된 상태를 저장하지만, Stream은 연산이 **필요할 때만 실행(Lazy Evaluation)** 되어 불필요한 계산을 줄일 수 있습니다.
- **원본 변경 여부**: Collection은 데이터를 직접 수정할 수 있지만, Stream은 원본 데이터를 변경하지 않고 **새로운 데이터를 생성**하여 반환하는 **불변(Immutable)** 방식으로 동작합니다.



<br>

## 3. Stream 생성 방법
### (1) 컬렉션에서 생성
```java
List<String> list = Arrays.asList("apple", "banana", "cherry");
Stream<String> stream = list.stream();
```
### (2) 배열에서 생성
```java
String[] arr = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(arr);
```
### (3) 직접 생성
```java
Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
```
### (4) 무한 스트림
```java
Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 2);
```



<br>

## 4. Stream 연산 종류

### (1) 중간 연산 (Intermediate Operation)
- 결과가 또 다른 Stream이므로 여러 개의 중간 연산을 **연결(Chaining)** 가능
- 최종 연산이 실행되기 전까지 실행되지 않는 **지연 연산**

| 연산 | 설명 |
|------|------|
| `filter(Predicate<T>)` | 조건을 만족하는 요소만 필터링 |
| `map(Function<T, R>)` | 각 요소를 다른 값으로 변환 |
| `flatMap(Function<T, Stream<R>>)` | 중첩 구조를 평탄화(flatten) |
| `distinct()` | 중복 제거 |
| `sorted()` | 정렬 (Comparator 사용 가능) |
| `peek(Consumer<T>)` | 중간 처리(디버깅용) |

#### **예제**
```java
List<String> words = Arrays.asList("apple", "banana", "avocado");
List<String> result = words.stream()
        .filter(s -> s.startsWith("a"))   // 'a'로 시작하는 단어만 선택
        .map(String::toUpperCase)         // 대문자로 변환
        .sorted()                          // 정렬
        .collect(Collectors.toList());     // 리스트로 변환

System.out.println(result); // [APPLE, AVOCADO]
```



### (2) 최종 연산 (Terminal Operation)
- 스트림을 닫고 결과를 반환
- 최종 연산이 수행되면 더 이상 스트림을 사용할 수 없음

| 연산 | 설명 |
|------|------|
| `forEach(Consumer<T>)` | 각 요소를 소비(출력 등) |
| `count()` | 요소 개수 반환 |
| `collect(Collector<T, A, R>)` | 결과를 컬렉션으로 변환 |
| `reduce(BinaryOperator<T>)` | 모든 요소를 하나로 결합(누적) |
| `allMatch(Predicate<T>)` | 모든 요소가 조건을 만족하는지 검사 |
| `anyMatch(Predicate<T>)` | 하나라도 조건을 만족하면 `true` |
| `noneMatch(Predicate<T>)` | 모든 요소가 조건을 만족하지 않으면 `true` |
| `findFirst()` | 첫 번째 요소 반환 |
| `findAny()` | 아무 요소나 반환 (병렬 스트림 시 최적화 가능) |

#### **예제**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// 요소 합산 (reduce 사용)
int sum = numbers.stream()
        .reduce(0, Integer::sum); // (0은 초기값)

System.out.println(sum); // 15
```



<br>

## 5. 병렬 스트림 (Parallel Stream)
- `parallelStream()`을 사용하면 멀티코어 CPU에서 병렬 처리 가능
- 데이터가 많을 때 성능 최적화 가능하지만, **순서가 보장되지 않을 수도 있음**

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");

// 병렬 처리
words.parallelStream()
     .forEach(System.out::println);
```

⚠ **주의**: 병렬 스트림은 순서가 중요한 작업에서는 적절하지 않을 수 있음



<br>

## 6. Collector를 사용한 결과 변환
- `collect()` 메서드는 결과를 리스트, 세트, 맵 등의 컬렉션으로 변환 가능
- `Collectors.joining()`, `Collectors.groupingBy()` 등 다양한 기능 제공

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");

// 리스트로 변환
List<String> list = words.stream()
        .filter(s -> s.length() > 5)
        .collect(Collectors.toList());

// 문자열 합치기
String result = words.stream()
        .collect(Collectors.joining(", "));

System.out.println(result); // apple, banana, cherry
```



<br>

## 7. 정리
✅ `Stream`은 **반복문을 대체**하는 강력한 API  
✅ **중간 연산(filter, map, sorted 등)**과 **최종 연산(collect, forEach, reduce 등)**이 있음  
✅ **지연 실행**을 이용하여 최적화 수행  
✅ **병렬 처리**를 지원하지만 순서가 중요하면 `parallelStream()` 사용에 주의

# 참고자료
- [자바 스트림 설명부터 사용하는 이유 파헤쳐보기](https://zangzangs.tistory.com/171)