# `JAVA 참조 자료형`

<br>

## 1.배열

배열은 같은 자료형의 데이터를 여러 개 저장할 수 있는 자료구조입니다. 배열은 **고정된 크기**를 가지며, **인덱스(index)**를 통해 각 요소에 접근할 수 있습니다.

- 한 자료형을 묶어 저장하는 참조자료형
- 생성할 때 크기를 지정해야 함
- 한번 크기를 지정하면 변경 불가함

```java 
//표현방법 (둘다 동일한 의미. 1번이 좀 더 일반적)
1. int[] a
2. int a[]
```

null은 참조 자료형의 초기값으로 사용됨

```java
int[] a = null;
```
- 스택 메모리에 위치한 참조자료형 변수의 빈공간을 초기화시 사용가능함
- 힙 메모리의특정위치(번지)를 가리키고 있지 않다는 의미를 내포함(연결된 실제데이터가없음)
  <img src="https://github.com/user-attachments/assets/80a2d1bc-f115-4be1-a7d5-05d3f2f1cea1" width="500">

### 힙 메모리에 배열 객체 생성하기

- 참조자료형의 실제데이터(객체)는 힙메모리에 생성됨
- 힙 메모리에 객체를 생성하기 위해 "new" 키워드를 사용함
- 배열의 객체를 생성할 때는 길이를 지정해야 함

변수 선언과 동시에 배열 객체 생성하기
```java
int[] a = new int[3];
```

변수 선언 후 배열 객체 생성하기(참고: 스택과 달리 힙 메모리에서는 값을 주지않는 경우 강제 초기화가 진행됨)
```java
int[] a;
a = new int[3];
```
<img src="https://github.com/user-attachments/assets/b02a09dd-eb67-4521-b6c2-70d020303936" width="500">

new 키워드 없이 배열 객체 생성하기
```java
// new 키워드 없이는 객체 대입을 분리하여 사용할 수 없음
int[] a = {1, 2, 3, 4, 5}; //ok

int[] a;
a = {1, 2, 3, 4, 5}; //error
```

### 참조 자료형 복사 시 주의사항
데이터값을 변경하면 다른 참조변수의 데이터도 변하게됨

<img src="https://github.com/user-attachments/assets/86ecbda9-092e-45da-ae06-c9ea4914692b" width="500">

### for-each문을 이용한 배열 요소 출력
배열이나 컬렉션(Collection)등의 집합 객체에서 원소들을 하나씩 꺼내는 과정을 반복하는 구문
```java
int[] a = {1, 2, 3, 4, 5};
for (int num : a) {
    System.out.println(num);
}
// 1 2 3 4 5
```

<br>

## 2. 2차원 배열
2차원 배열 선언
```java
// 2차원 배열 선언(모두 동일한 의미)
int[][] a
int a[][]
int[] a[]
```
2차원 배열 초기화
```java
int[][] a = {{1, 2, 3}, {4, 5, 6}};
int[][] a = new int[2][3];
```

2차원 배열 객체 행 성분 생성 후 열 성분 생성하여 초기화
```java
int[][] a = new int[2][];

a[0] = new int[3];
a[0][0] = 1;
a[0][1] = 2;
a[0][2] = 3;

a[1] = new int[3];
a[1][0] = 4;
a[1][1] = 5;
a[1][2] = 6;
```
<img src="https://github.com/user-attachments/assets/52ba556f-bb9a-43ab-9c07-775f20f204da" width="500">


<br>

## 3. String
String은 문자열을 저장하는 참조 자료형, 문자열은 큰 따옴표로 묶어서 표현함
- 한번 정의한 문자열을 변경할 수 없음
- 리터럴을 바로 입력한 데이터는 문자열이 같을 때 객체를 공유함
- string 객체와 + 사용하여 연산하면 다른 string 객체를 생성함


### 힙 메모리에 String 객체 생성하기
new 키워드를 사용하여 String 객체를 생성
```java
String str = new String("Hello, Java!");
```

new 키워드 없이 String 객체 생성
```java
String str = "Hello, Java!";
```

### String 객체의 메모리 구조

리터럴로 생성한 문자열은 문자열이 같을 때 객체를 공유함(메모리 절약)

```java
String str1 = new String("안녕");
String str2 = "안녕";
String str3 = "안녕";
String str4 = new String("안녕");
```

<img src="https://github.com/user-attachments/assets/cb73385f-a94a-40f5-9d84-00ae7ae3f9f1" width="500">
