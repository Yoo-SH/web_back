# `Java Dates - LocalDate, LocalTime, LocalDateTime`

Java에서는 날짜 및 시간을 다루기 위해 `LocalDate`, `LocalTime`, `LocalDateTime` 클래스를 제공합니다. 이 클래스들은 `java.time` 패키지에 속하며, 불변(immutable) 객체로 동작하여 안전한 날짜 및 시간 관리를 가능하게 합니다.

<br>

## 1. LocalDate, LocalTime, LocalDateTime 소개

### LocalDate
`LocalDate`는 연도, 월, 일 정보를 포함하는 날짜 표현을 위한 클래스입니다. 시간 정보는 포함되지 않습니다.

### LocalTime
`LocalTime`은 시, 분, 초, 나노초 정보를 포함하는 시간 표현을 위한 클래스입니다. 날짜 정보는 포함되지 않습니다.

### LocalDateTime
`LocalDateTime`은 날짜(`LocalDate`)와 시간(`LocalTime`)을 함께 관리할 수 있는 클래스입니다. 날짜와 시간을 함께 처리해야 할 경우 유용합니다.



<br>

## 2. LocalDate, LocalTime, LocalDateTime 비교

| 클래스        | 포함하는 정보     | 예제 값 |
|--------------|----------------|-------------|
| `LocalDate`  | 연도, 월, 일     | `2023-02-07` |
| `LocalTime`  | 시, 분, 초, 나노초 | `14:30:15.123` |
| `LocalDateTime` | 연도, 월, 일, 시, 분, 초, 나노초 | `2023-02-07T14:30:15.123` |



<br>

## 3. LocalDate 탐색 - 생성과 실행을 위한 메서드

### LocalDate 객체 생성
```java
import java.time.LocalDate;

public class LocalDateExample {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now(); // 현재 날짜
        LocalDate specificDate = LocalDate.of(2023, 2, 7); // 특정 날짜 지정
        LocalDate parsedDate = LocalDate.parse("2023-02-07"); // 문자열을 LocalDate로 변환

        System.out.println("오늘 날짜: " + today);
        System.out.println("지정한 날짜: " + specificDate);
        System.out.println("파싱한 날짜: " + parsedDate);
    }
}
```

### 날짜 조작 메서드
```java
LocalDate tomorrow = today.plusDays(1); // 하루 뒤 날짜
LocalDate lastMonth = today.minusMonths(1); // 한 달 전 날짜
```



<br>

## 4. LocalDate 비교와 특정 날짜 생성

### 날짜 비교
```java
if (specificDate.isBefore(today)) {
    System.out.println("지정한 날짜는 오늘 이전입니다.");
} else if (specificDate.isAfter(today)) {
    System.out.println("지정한 날짜는 오늘 이후입니다.");
} else {
    System.out.println("지정한 날짜는 오늘과 같습니다.");
}
```

### 특정 날짜 정보 가져오기
```java
int year = today.getYear();
int month = today.getMonthValue();
int day = today.getDayOfMonth();
System.out.println("연도: " + year + ", 월: " + month + ", 일: " + day);
```

### 요일 정보 가져오기
```java
System.out.println("오늘의 요일: " + today.getDayOfWeek());
```



<br>

## 5. LocalTime 탐색 - 생성과 실행을 위한 메서드

### LocalTime 객체 생성
```java
import java.time.LocalTime;

public class LocalTimeExample {
    public static void main(String[] args) {
        LocalTime now = LocalTime.now(); // 현재 시간
        LocalTime specificTime = LocalTime.of(14, 30, 15); // 특정 시간 지정
        LocalTime parsedTime = LocalTime.parse("14:30:15"); // 문자열을 LocalTime으로 변환

        System.out.println("현재 시간: " + now);
        System.out.println("지정한 시간: " + specificTime);
        System.out.println("파싱한 시간: " + parsedTime);
    }
}
```

### 시간 조작 메서드
```java
LocalTime later = now.plusHours(2); // 2시간 후
LocalTime earlier = now.minusMinutes(30); // 30분 전
```



<br>

## 6. LocalDateTime 탐색 - 생성과 실행을 위한 메서드

### LocalDateTime 객체 생성
```java
import java.time.LocalDateTime;

public class LocalDateTimeExample {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간
        LocalDateTime specificDateTime = LocalDateTime.of(2023, 2, 7, 14, 30, 15); // 특정 날짜 및 시간 지정
        LocalDateTime parsedDateTime = LocalDateTime.parse("2023-02-07T14:30:15"); // 문자열을 LocalDateTime으로 변환

        System.out.println("현재 날짜와 시간: " + now);
        System.out.println("지정한 날짜와 시간: " + specificDateTime);
        System.out.println("파싱한 날짜와 시간: " + parsedDateTime);
    }
}
```

### 날짜 및 시간 조작 메서드
```java
LocalDateTime futureDateTime = now.plusDays(5).plusHours(3); // 5일 후, 3시간 후
LocalDateTime pastDateTime = now.minusWeeks(2).minusMinutes(45); // 2주 전, 45분 전
```



<br>

## 7. 추가 학습 자료
- 공식 문서: [Java LocalDate API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html)

