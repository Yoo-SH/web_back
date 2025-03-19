# Spring Boot 로깅 가이드

## 목차
- [개요](#개요)
- [로깅 레벨](#로깅-레벨)
- [로깅 레벨 설정 방법](#로깅-레벨-설정-방법)
  - [application.properties를 통한 설정](#applicationproperties를-통한-설정)
  - [application.yml을 통한 설정](#applicationyml을-통한-설정)
  - [환경별 설정](#환경별-설정)
  - [로그백 설정 파일(logback.xml)을 통한 설정](#로그백-설정-파일logbackxml을-통한-설정)
  - [런타임에 로깅 레벨 변경](#런타임에-로깅-레벨-변경)
- [로깅 패턴 설정](#로깅-패턴-설정)
- [환경별 로깅 전략](#환경별-로깅-전략)
- [모범 사례](#모범-사례)

## 개요

Spring Boot는 기본적으로 Commons Logging을 통해 로깅 추상화를 제공하지만, 실제 구현체로는 Logback을 기본으로 사용합니다. 로깅은 애플리케이션의 동작 상태를 모니터링하고 문제를 해결하는 데 필수적인 요소입니다.

## 로깅 레벨

Spring Boot에서 사용하는 표준 로깅 레벨은 다음과 같습니다(심각도 높은 순):

| 레벨 | 설명 | 사용 시기 |
|------|------|-----------|
| **ERROR** | 애플리케이션이 더 이상 작동하지 않을 수 있는 심각한 문제 | 데이터베이스 연결 실패, 중요 서비스 불능 등 즉각적인 조치가 필요한 상황 |
| **WARN** | 잠재적인 문제 상황이지만 당장 애플리케이션 실행에는 영향이 없음 | 잘못된 API 사용, 대체 경로로 실행 중, 향후 ERROR가 될 가능성 있는 상황 |
| **INFO** | 중요한 비즈니스 프로세스의 시작과 종료 같은 일반적인 정보 | 애플리케이션 시작/종료, 주요 상태 변경, 비즈니스 이벤트 성공, 사용자 로그인 등 |
| **DEBUG** | 개발자에게 도움이 되는 상세한 정보 | 변수값, 처리 흐름, 메서드 호출 결과 등 문제 해결에 도움이 되는 세부 정보 |
| **TRACE** | DEBUG보다 더 상세한 정보 | 매우 상세한 진단 정보(모든 HTTP 요청/응답, SQL 쿼리 파라미터 등) |

각 로깅 레벨은 자신과 그보다 심각한 모든 레벨의 로그를 포함합니다. 예를 들어, INFO 레벨을 설정하면 INFO, WARN, ERROR 로그가 모두 출력됩니다.

## 로깅 레벨 설정 방법

### application.properties를 통한 설정

가장 일반적인 방법은 `application.properties` 파일에서 로깅 레벨을 설정하는 것입니다:

```properties
# 루트 레벨 설정
logging.level.root=INFO

# 특정 패키지 레벨 설정
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR
logging.level.com.mycompany.myapp=DEBUG
```

### application.yml을 통한 설정

YAML 형식을 선호한다면 `application.yml` 파일에서도 설정 가능합니다:

```yaml
logging:
  level:
    root: INFO
    org.springframework.web: DEBUG
    org.hibernate: ERROR
    com.mycompany.myapp: DEBUG
```

### 환경별 설정

Spring Boot의 프로필 기능을 활용하여 환경별로 다른 로깅 레벨을 설정할 수 있습니다:

**application-dev.properties**:
```properties
logging.level.root=DEBUG
logging.level.com.mycompany.myapp=TRACE
```

**application-prod.properties**:
```properties
logging.level.root=INFO
logging.level.com.mycompany.myapp=WARN
```

그런 다음 애플리케이션 실행 시 프로필을 지정합니다:
```bash
java -jar myapp.jar --spring.profiles.active=dev
```

### 로그백 설정 파일(logback.xml)을 통한 설정

더 세밀한 제어가 필요한 경우, `logback-spring.xml` 파일을 `src/main/resources` 디렉토리에 추가하여 로그백을 직접 설정할 수 있습니다:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 스프링 부트 기본 설정 포함 -->
    <include resource="org/springframework/boot/logging/logback/base.xml"/>
    
    <!-- 패키지별 로깅 레벨 설정 -->
    <logger name="org.springframework.web" level="DEBUG"/>
    <logger name="org.hibernate" level="ERROR"/>
    <logger name="com.mycompany.myapp" level="DEBUG"/>
    
    <!-- 환경별 설정 -->
    <springProfile name="dev">
        <logger name="com.mycompany.myapp" level="DEBUG"/>
    </springProfile>
    
    <springProfile name="prod">
        <logger name="com.mycompany.myapp" level="WARN"/>
    </springProfile>
</configuration>
```

### 런타임에 로깅 레벨 변경

Spring Boot Actuator를 사용하면 애플리케이션 실행 중에도 로깅 레벨을 동적으로 변경할 수 있습니다:

1. 의존성 추가:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. Actuator 엔드포인트 노출:
```properties
management.endpoints.web.exposure.include=loggers
management.endpoint.loggers.enabled=true
```

3. 로깅 레벨 변경 API 호출 (HTTP POST):
```bash
curl -X POST 'http://localhost:8080/actuator/loggers/com.mycompany.myapp' \
     -H 'Content-Type: application/json' \
     -d '{"configuredLevel": "DEBUG"}'
```

## 로깅 패턴 설정

로그 메시지의 형식을 사용자 정의할 수 있습니다:

```properties
# 콘솔 출력 패턴
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# 파일 출력 패턴
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

## 환경별 로깅 전략

### 개발 환경 (Development)
- **로깅 레벨**: DEBUG 또는 TRACE
- **출력 대상**: 주로 콘솔 (빠른 피드백)
- **목적**: 개발자가 애플리케이션 동작을 상세히 이해하고 디버깅하기 위함
- **설정 예**:
  ```properties
  logging.level.root=INFO
  logging.level.com.mycompany.myapp=DEBUG
  logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %highlight(%-5level) %logger{36} - %msg%n
  ```

### 테스트 환경 (Testing)
- **로깅 레벨**: DEBUG
- **출력 대상**: 콘솔 및 파일
- **목적**: 테스트 실행 흐름 파악 및 문제 진단
- **설정 예**:
  ```properties
  logging.level.root=INFO
  logging.level.com.mycompany.myapp=DEBUG
  logging.file.name=logs/test.log
  ```

### 스테이징 환경 (Staging)
- **로깅 레벨**: INFO
- **출력 대상**: 파일 (로테이션 적용)
- **목적**: 실제 프로덕션과 유사한 환경에서의 동작 검증
- **설정 예**:
  ```properties
  logging.level.root=INFO
  logging.level.com.mycompany.myapp=INFO
  logging.file.name=logs/application.log
  logging.logback.rollingpolicy.max-file-size=10MB
  logging.logback.rollingpolicy.max-history=10
  ```

### 프로덕션 환경 (Production)
- **로깅 레벨**: INFO 또는 WARN
- **출력 대상**: 파일 (로테이션 적용) + 외부 로깅 시스템(ELK, Graylog 등)
- **목적**: 성능 영향 최소화, 중요 이벤트와 오류 기록
- **설정 예**:
  ```properties
  logging.level.root=WARN
  logging.level.com.mycompany.myapp=INFO
  logging.file.name=logs/application.log
  logging.logback.rollingpolicy.max-file-size=100MB
  logging.logback.rollingpolicy.max-history=30
  ```

## 모범 사례

1. **로깅 레벨 적절히 사용하기**
   - ERROR: 실제 오류 상황에만 사용
   - WARN: 잠재적 문제 알림용
   - INFO: 주요 작업의 시작/종료 및 중요 비즈니스 이벤트
   - DEBUG: 디버깅용 세부 정보
   - TRACE: 극도로 상세한 진단 정보

2. **민감 정보 로깅 금지**
   - 비밀번호, 토큰, 개인식별정보(PII) 등은 절대 로깅하지 않음
   - 민감 정보 마스킹 및 필터링 패턴 적용

3. **구조화된 로깅 고려**
   - JSON 형식의 로그를 사용하면 분석 및 검색이 용이함
   - MDC(Mapped Diagnostic Context)를 활용한 상관관계 ID 추적

4. **예외 처리 시 적절한 로깅**
   ```java
   try {
       // 작업 수행
   } catch (Exception e) {
       log.error("작업 처리 중 오류 발생: {}", operationName, e);
   }
   ```

5. **환경별 로깅 설정 분리**
   - 개발/테스트/스테이징/프로덕션 환경에 맞는 로깅 설정 적용
   - 프로덕션에서는 불필요한 DEBUG 로그 비활성화

6. **로그 집계 및 모니터링 시스템 연동**
   - ELK Stack(Elasticsearch, Logstash, Kibana)
   - Graylog, Splunk 등 로그 분석 플랫폼 활용