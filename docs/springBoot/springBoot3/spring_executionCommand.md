# Spring Boot 애플리케이션 실행 방법 (IDE 없이 실행)

Spring Boot 애플리케이션을 **IDE 없이 실행하는 방법**에 대해 설명합니다.

## 1. JAR 파일로 실행하기

Spring Boot 애플리케이션을 빌드하여 실행 가능한 **JAR 파일**로 만든 후 실행할 수 있습니다.

### (1) JAR 파일 생성
#### Maven 사용 시
```sh
mvn clean package
```
실행 후 `target/` 디렉토리에 `.jar` 파일이 생성됩니다.

#### Gradle 사용 시
```sh
./gradlew bootJar
```
실행 후 `build/libs/` 디렉토리에 `.jar` 파일이 생성됩니다.

### (2) JAR 파일 실행
JAR 파일이 생성된 후 다음 명령을 실행합니다.
```sh
java -jar target/myapp.jar
```
※ `myapp.jar`는 실제 생성된 JAR 파일명으로 변경하세요.

---

## 2. `mvnw` (Maven Wrapper) 활용

### (1) `mvnw`란?
`mvnw`는 **Maven Wrapper**로, 별도의 Maven 설치 없이도 프로젝트를 빌드하고 실행할 수 있도록 돕는 스크립트입니다.

### (2) `mvnw`를 이용한 실행
```sh
./mvnw spring-boot:run
```
(Windows에서는 `mvnw.cmd spring-boot:run` 사용)

---

## 3. 실행 방법 요약

| 방법 | 명령어 |
|------|------|
| JAR 파일 실행 | `java -jar target/myapp.jar` |
| Maven으로 빌드 후 실행 | `mvn clean package` → `java -jar target/myapp.jar` |
| Maven Wrapper로 실행 | `./mvnw spring-boot:run` |

이제 IDE 없이도 Spring Boot 애플리케이션을 실행할 수 있습니다! 🚀
