# `JShell - Java REPL (Read-Eval-Print Loop)`

JShell은 Java 9부터 도입된 **Java REPL(Read-Eval-Print Loop)** 도구로, Java 코드를 손쉽게 입력하고 실행하며 결과를 즉시 확인할 수 있도록 도와주는 도구입니다.
JShell은 간단한 테스트나 학습, 프로토타이핑에 매우 유용합니다.

__JShell은 IDE와 비교했을 때 아래와 같은 장점을 가지고 있습니다__

1. **빠르고 가벼움**  
   IDE는 설치와 설정에 시간이 걸리는 반면, JShell은 터미널에서 즉시 실행 가능하여 빠르게 사용할 수 있습니다.
2. **단순한 실행 환경**  
   프로젝트나 빌드 설정 없이 간단한 코드 조각을 즉시 테스트할 수 있습니다.
3. **낮은 자원 소모**  
   IDE는 비교적 많은 시스템 자원을 사용하는 반면, JShell은 가볍고 효율적입니다.
4. **초보자 친화적**  
   복잡한 개발 환경 설정 없이 Java 문법을 학습하고 실험하기에 적합합니다.
5. **빠른 피드백 루프**  
   코드 작성-실행-결과 확인의 루프가 IDE보다 훨씬 빠르게 수행됩니다.

<br>

## 주요 기능

1. **즉각적인 피드백 제공**  
   Java 코드를 한 줄씩 입력하여 실행 결과를 즉시 확인할 수 있습니다.

2. **완전한 Java 구문 지원**  
   클래스, 메서드, 인터페이스, 변수 등을 선언하고 사용할 수 있습니다.

3. **빠른 프로토타이핑**  
   복잡한 프로젝트를 설정하지 않고도 코드 실행 및 테스트가 가능합니다.

4. **향상된 학습 환경**  
   Java를 새로 배우는 사용자들에게 이상적인 플랫폼으로 문법과 기능을 쉽게 익힐 수 있습니다.



<br>

## 시작하기

### 1. JShell 실행

JShell은 JDK 9 이상에 포함되어 있습니다. 터미널이나 명령 프롬프트에서 다음 명령으로 JShell을 실행합니다:

```bash
jshell
```

### 2. JShell 종료

JShell에서 작업을 마치고 종료하려면 다음 명령을 입력합니다:

```javascript
/exit
```



<br>

## 주요 명령어

JShell에서 사용할 수 있는 명령어는 `/` 문자로 시작합니다. 다음은 유용한 명령어들입니다:

| 명령어           | 설명                                     |
|-------------------|------------------------------------------|
| `/help`           | JShell 도움말 보기                      |
| `/exit`           | JShell 세션 종료                       |
| `/list`           | 현재 세션에서 실행한 코드 목록 확인     |
| `/save <파일명>`  | 작성한 코드를 파일로 저장               |
| `/open <파일명>`  | 파일에 저장된 코드를 세션으로 로드       |
| `/reset`          | 현재 세션 초기화                       |



<br>

## 코드 예제

### 간단한 수학 계산

```java
jshell>1+2
$1 ==>3
```

### 변수 선언 및 출력

```java
jshell>
int x = 10;
x ==>10

jshell>System.out.

println(x);
10
```

### 메서드 정의 및 호출

```java
jshell>

int square(int n) {
   ...>return n * n;
   ...>}
|
created method

square(int)

jshell>

square(3)

$2 ==>9
```

### 클래스 작성 및 사용

```java
jshell>

class Hello {
   ...>

    void sayHello() {
   ...>System.out.println("Hello, JShell!");
   ...>}
   ...>
}
|created

class Hello

jshell>
Hello hello = new Hello();
hello ==>Hello@1a2b3c4

jshell>hello.

sayHello();

Hello,JShell!
```



<br>

## 활용 사례

1. **코드 실험**  
   작은 코드 조각을 작성하고 실행하며 동작 확인 가능.

2. **디버깅 및 학습**  
   복잡한 프로젝트 없이 문제가 되는 코드 실험 및 학습 가능.

3. **API 테스트**  
   Java의 새로운 API를 간단히 분석하고 사용법을 테스트할 수 있음.



<br>

## JShell 도움말

더 많은 정보를 얻으려면 JShell 내부에서 `/help` 명령을 입력하거나 [공식 문서](https://docs.oracle.com/javase/9/tools/jshell.htm)를 참고하세요.
