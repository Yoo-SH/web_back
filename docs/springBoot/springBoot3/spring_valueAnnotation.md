# Spring Boot `@Value` vs `Environment` (env) 차이점

Spring Boot에서 설정값을 가져오는 방법으로 `@Value` 애너테이션과 `Environment` 객체를 사용하는 방식이 있습니다. 두 방식의 차이점을 정리합니다.

## 목차
- [`@Value` 애너테이션](#1-value-애너테이션)
- [`Environment` 객체](#2-environment-객체)
- [주요 차이점 비교](#3-주요-차이점-비교)
- [언제 사용해야 할까?](#4-언제-사용해야-할까)
- [결론](#5-결론)

## 1. `@Value` 애너테이션

`@Value` 애너테이션은 `application.properties` 또는 `application.yml` 등의 설정 파일에서 값을 주입받을 때 사용됩니다.

### 사용 방법
```java
@Value("${property.name}")
private String propertyValue;
```

### 특징
- 컴포넌트 스캔을 통해 Bean 생성 시점에 값이 주입됨
- 기본값 설정이 가능 (`@Value("${property.name:defaultValue}")`)
- SpEL (Spring Expression Language) 표현식 사용 가능
- Type-Safe 하지 않음 (String 기반 주입)

## 2. `Environment` 객체

`Environment` 객체는 Spring에서 제공하는 인터페이스로, 환경 변수 및 설정 값을 동적으로 가져올 수 있습니다.

### 사용 방법
```java
@Autowired
private Environment environment;

public String getPropertyValue() {
    return environment.getProperty("property.name");
}
```

### 특징
- 실행 시점에 값을 가져올 수 있음 (동적 접근 가능)
- 기본값 설정이 불가능 (`environment.getProperty("property.name", "defaultValue")`와 같이 직접 처리해야 함)
- Type-Safe 하지 않음 (String 기반 반환)
- 시스템 환경 변수 및 프로파일 기반 설정도 접근 가능

## 3. 주요 차이점 비교

| 항목 | `@Value`                          | `Environment` |
|---|-----------------------------------|-|
| 주입 시점 | Bean 생성 시점                        | 실행 시점 |
| 기본값 설정 | 가능 (`@Value("${key:default}")`)   | 불가능 (`getProperty("key", "default")`으로 처리 필요) |
| 동적 접근 | 불가능 (컴파일 시점)                      | 가능 (런타임 시점) |
| Type-Safe | X                                 | X |
| SpEL 지원 | O                                 | X |
| 시스템 환경 변수 접근 | X                                 | O |

## 4. 언제 사용해야 할까?
- **`@Value`**: 간단한 설정값을 주입할 때, SpEL(spring expression language)을 사용할 때 유용
- **`Environment`**: 동적으로 값을 가져와야 하거나, 시스템 환경 변수까지 접근해야 할 때 유용

## 5. 결론
- 두 방식 모두 Spring에서 설정값을 가져오는 방법이지만, `@Value`는 정적인 방식이고 `Environment`는 동적인 접근이 가능함.
- 상황에 따라 적절한 방법을 선택하여 사용하면 됨.

