# StudentNotFoundException 설명

이 `StudentNotFoundException` 클래스는 사용자 정의 예외(Custom Exception)를 만들기 위한 코드입니다. Spring Boot에서 특정 상황에서만 발생하는 예외를 만들고 싶을 때, 이렇게 예외 클래스를 직접 정의할 수 있습니다.

## 목차
- [StudentNotFoundException의 역할](#1-studentnotfoundexception의-역할)
- [RuntimeException을 상속받는 이유](#2-runtimeexception을-상속받는-이유)
- [생성자 3개가 존재하는 이유](#3-생성자-3개가-존재하는-이유)
- [`super(message)`가 하는 역할](#4-supermessage가-하는-역할)
- [실제 사용 예시](#5-실제-사용-예시)
- [정리](#6-정리)


## 1. StudentNotFoundException의 역할
📌 존재하지 않는 학생을 조회할 때 발생하는 예외

- `RuntimeException`을 상속받아서 실행 중(Runtime) 예외를 정의한 것
- `super(message)`를 호출하여 예외 메시지를 전달

### 예제 코드 (예외 발생 과정)
```java
@GetMapping("/students/{studentId}")
public Student getStudent(@PathVariable int studentId) {
    if ((studentId >= theStudents.size()) || (studentId < 0)) {
        throw new StudentNotFoundException("Student id not found - " + studentId);
    }
    return theStudents.get(studentId);
}
```
💡 이 코드에서 `throw new StudentNotFoundException(...)`를 호출하면 예외가 발생합니다!
즉, `studentId`가 존재하지 않는 경우, 예외를 강제로 발생시키는 것입니다.

## 2. RuntimeException을 상속받는 이유
✅ 예외를 명시적으로 선언하지 않아도 됨

- `RuntimeException`을 상속하면 `throws` 선언 없이 예외를 던질 수 있습니다.
- 만약 `Exception`을 상속받으면 모든 메서드에서 `throws`를 붙여야 합니다.

### 예시 비교
```java
// RuntimeException을 상속받은 경우 (throws 선언 불필요)
public class StudentNotFoundException extends RuntimeException { }

// Exception을 상속받은 경우 (throws 선언 필요)
public class StudentNotFoundException extends Exception { }

public Student getStudent(int studentId) throws StudentNotFoundException {
    throw new StudentNotFoundException("Error!");
}
```
따라서, Spring Boot에서는 `RuntimeException`을 상속받아 예외를 처리하는 경우가 많습니다! 🚀

## 3. 생성자 3개가 존재하는 이유
📌 예외를 발생시킬 때 다양한 방식으로 메시지를 전달할 수 있도록 여러 개의 생성자를 제공한 것입니다.

| 생성자 | 설명 | 예제 |
|--|||
| `StudentNotFoundException(String message)` | 에러 메시지만 포함 | `throw new StudentNotFoundException("학생 없음!");` |
| `StudentNotFoundException(String message, Throwable cause)` | 메시지 + 원인 예외 포함 | `throw new StudentNotFoundException("DB 연결 오류", e);` |
| `StudentNotFoundException(Throwable cause)` | 원인 예외만 포함 | `throw new StudentNotFoundException(e);` |

## 4. `super(message)`가 하는 역할

- `super(message)`는 부모 클래스(`RuntimeException`)의 생성자를 호출하는 코드입니다.
- 즉, 예외가 발생했을 때, `message`가 자동으로 `RuntimeException` 내부에 저장됩니다.

### 예제
```java
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);  // RuntimeException에 메시지를 넘김
    }
}
```

```java
throw new StudentNotFoundException("Student ID 10 not found!");
```
이렇게 하면 예외 발생 시 메시지가 자동으로 저장되고, 나중에 `getMessage()`로 꺼낼 수 있습니다.

## 5. 실제 사용 예시
📌 예외 발생 시 Spring Boot의 `StudentRestExceptionHandler`가 예외를 감지하고 JSON 응답을 반환합니다.

```java
@ExceptionHandler
public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
    StudentErrorResponse error = new StudentErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        exc.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}
```

### 요청 & 응답 예시
✅ 존재하지 않는 학생 조회 요청
```http
GET /api/students/10
```

❌ 발생한 예외 (`StudentNotFoundException` 실행)
```java
throw new StudentNotFoundException("Student id not found - 10");
```

📌 클라이언트에 반환되는 JSON 응답
```json
{
    "status": 404,
    "message": "Student id not found - 10",
    "timeStamp": 1700000000000
}
```

## 정리
✔️ `StudentNotFoundException`은 사용자 정의 예외 (특정 상황에서만 발생)
✔️ `RuntimeException`을 상속받아 `throws` 없이 사용할 수 있음
✔️ `super(message)`를 통해 예외 메시지를 저장하고 전달
✔️ 예외 발생 시 `@ExceptionHandler`가 감지하여 JSON 응답을 반환


