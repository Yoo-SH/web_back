# `Java 파일 및 디렉토리 작업 가이드`

<br>

## 📌 개요
Java에서는 `java.nio.file` 패키지를 사용하여 파일과 디렉토리를 쉽게 다룰 수 있습니다.
이 문서는 `Files` 및 `Paths` 클래스를 활용하여 파일 및 디렉토리 작업을 수행하는 방법을 설명합니다.



<br>

## 📝 스텝 01 - `Files.list()`를 통한 디렉토리 내 파일 및 폴더 목록 가져오기
`Files.list(Path dir)` 메소드는 특정 디렉토리 내의 파일 및 폴더 목록을 가져올 수 있습니다.

### ✅ 예제 코드
```java
import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;

public class FileListExample {
    public static void main(String[] args) {
        Path dir = Paths.get("C:/example");

        try (Stream<Path> paths = Files.list(dir)) {
            paths.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 📌 주요 개념
- `Files.list(dir)` → **해당 디렉토리의 1-depth 파일 및 폴더 목록을 반환**
- `forEach(System.out::println)` → 목록 출력



<br>

## 📝 스텝 02 - 재귀적 리스트, 파일 필터링
`Files.walk(Path dir)`를 사용하면 **하위 디렉토리까지 포함한 모든 파일 및 폴더 목록**을 가져올 수 있습니다.

### ✅ 예제 코드
```java
import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;

public class RecursiveFileList {
    public static void main(String[] args) {
        Path dir = Paths.get("C:/example");

        try (Stream<Path> paths = Files.walk(dir)) {
            paths.filter(Files::isRegularFile)
                 .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 📌 주요 개념
- `Files.walk(dir)` → **하위 폴더까지 포함한 전체 파일 및 디렉토리 목록을 탐색**
- `filter(Files::isRegularFile)` → **파일만 필터링**



<br>

## 📝 스텝 03 - 파일에서 내용 읽기
파일의 내용을 읽을 때 `Files.readAllLines()` 또는 `Files.lines()`를 사용할 수 있습니다.

### ✅ `Files.readAllLines()` 예제
```java
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public class FileReadExample {
    public static void main(String[] args) {
        Path filePath = Paths.get("C:/example/test.txt");

        try {
            List<String> lines = Files.readAllLines(filePath);
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### ✅ `Files.lines()` 예제 (대용량 파일 처리)
```java
import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Stream;

public class FileReadStreamExample {
    public static void main(String[] args) {
        Path filePath = Paths.get("C:/example/test.txt");

        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 📌 차이점
- `Files.readAllLines()` → **모든 내용을 한 번에 로드** (작은 파일에 적합)
- `Files.lines()` → **스트림을 이용하여 한 줄씩 읽음** (대용량 파일에 적합)



<br>

## 📝 스텝 04 - 파일에 내용 저장 (쓰기)
파일에 내용을 쓸 때는 `Files.write()` 메소드를 사용할 수 있습니다.

### ✅ 기본 파일 쓰기
```java
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public class FileWriteExample {
    public static void main(String[] args) {
        Path filePath = Paths.get("C:/example/output.txt");
        List<String> content = List.of("Hello, World!", "Java File Writing Example.");

        try {
            Files.write(filePath, content);
            System.out.println("파일이 성공적으로 작성되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### ✅ Spring Boot에서 파일 읽기 및 쓰기 예제
```java
import org.springframework.web.bind.annotation.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    private final Path filePath = Paths.get("C:/example/springfile.txt");

    @GetMapping("/read")
    public List<String> readFile() throws IOException {
        return Files.readAllLines(filePath);
    }

    @PostMapping("/write")
    public String writeFile(@RequestBody List<String> content) throws IOException {
        Files.write(filePath, content);
        return "파일이 성공적으로 작성되었습니다.";
    }
}
```



<br>

## ✅ 정리
| 단계 | 메소드 | 설명 |
|------|--------|--------------------------------|
| 01 | `Files.list(dir)` | 특정 디렉토리 내 파일 및 폴더 목록 가져오기 |
| 02 | `Files.walk(dir)` | 하위 디렉토리까지 포함한 파일 목록 가져오기 |
| 03 | `Files.readAllLines(filePath)` | 파일 내용을 한 번에 읽기 |
| 03 | `Files.lines(filePath)` | 파일을 스트림으로 한 줄씩 읽기 (대용량 파일 처리) |
| 04 | `Files.write(filePath, content)` | 파일 생성 및 내용 저장 |
| 04 | `Files.write(filePath, content, StandardOpenOption.APPEND)` | 기존 파일에 내용 추가 |

이 가이드를 통해 자바에서 파일과 디렉토리를 다루는 기본적인 방법을 익힐 수 있습니다. 🚀