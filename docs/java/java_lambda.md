오케이! 함수형 프로그래밍과 람다를 완전 쉽게 설명해볼게.  

---

### 1. 함수형 프로그래밍이 뭐야?  
**"함수형 프로그래밍"**은 **`함수를 마치 변수처럼 사용하는 프로그래밍 스타일`**이야.  
객체지향 프로그래밍(OOP)이 "객체"를 중심으로 프로그램을 짜는 거라면,  
함수형 프로그래밍(FP)은 "함수"를 중심으로 프로그램을 짜는 거야.  

---


### 2. 람다가 뭐야?  
**람다(lambda)**는 **"이름 없는 함수"**야!  
함수를 변수처럼 만들고, 한 줄로 간단하게 쓸 수 있어.  

기존에는 메서드를 만들려면 이렇게 길게 써야 했지?  
```java
public int add(int a, int b) {
    return a + b;
}
```
하지만 **람다 표현식**을 쓰면 이렇게 짧아져!  
```java
(a, b) -> a + b
```
예를 들어, `Runnable`을 만들 때:  
#### (1) 기존 방식
```java
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello, World!");
    }
};
```
#### (2) 람다 방식
```java
Runnable r = () -> System.out.println("Hello, World!");
```
✅ `()`는 매개변수(없으니까 비어있음)  
✅ `->` 뒤에 실행할 코드  

---


### 3. 그럼, 왜 함수형 프로그래밍을 써야 해?  
함수형 프로그래밍의 장점은:  
✅ 코드가 짧고 간결해!  
✅ 코드 재사용이 쉬워!  
✅ 버그가 적어!  

예를 들어, **전통적인 방법(OOP 스타일)**과 **함수형 프로그래밍 스타일**을 비교해볼게.  

#### (1) 전통적인 방식 (for문 사용)
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// 'A'로 시작하는 이름을 찾아서 출력하기
for (String name : names) {
    if (name.startsWith("A")) {
        System.out.println(name);
    }
}
```

#### (2) 함수형 프로그래밍 (람다 + 스트림 사용)
```java
names.stream()
     .filter(name -> name.startsWith("A"))
     .forEach(System.out::println);
```
**차이점:**  
✅ `for문`을 안 써도 되고, 코드가 훨씬 깔끔해!  
✅ `filter(name -> name.startsWith("A"))` 부분이 **람다 표현식**이야.  

---

### 4. 람다 활용 예제 🚀  
#### (1) 리스트 정렬하기  
기존 방식:
```java
Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
});
```
람다 방식:
```java
Collections.sort(names, (s1, s2) -> s1.compareTo(s2));
```
#### (2) 리스트 필터링  
기존 방식:
```java
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.length() > 3) {
        result.add(name);
    }
}
```
람다 방식:
```java
List<String> result = names.stream()
                           .filter(name -> name.length() > 3)
                           .collect(Collectors.toList());
```
---

### 5. 정리! ✨  
1️⃣ **함수형 프로그래밍(FP)**: "함수를 변수처럼 활용하는 방식"  
2️⃣ **람다(lambda)**: "이름 없는 함수" (코드를 짧고 간결하게!)  
3️⃣ **스트림 API**와 같이 쓰면 코드가 깔끔해짐!  

함수형 프로그래밍을 쓰면 코드가 짧아지고, 가독성이 좋아지고, 유지보수가 편해져! 
꼭 기억해야 할 것은 함수를 변수처럼 쓴다는 것이야.! 
이제부터는 `for문`보다는 `람다`와 `스트림`을 한번 써보자! 🚀