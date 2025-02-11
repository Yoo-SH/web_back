# Spring Boot Application Properties Guide

Spring Boot 프로젝트에서 사용되는 주요 `application.properties` 설정을 영역별로 정리했습니다.

## 1. Core 설정
Spring Boot의 핵심 설정 값들입니다.
```properties
# 애플리케이션 이름 및 실행 포트
spring.application.name=my-app
server.port=8080

# 기본 인코딩 설정
spring.messages.encoding=UTF-8

# TimeZone 설정
spring.jackson.time-zone=Asia/Seoul
```

## 2. Web 설정
Spring MVC 및 웹 서버 관련 설정입니다.
```properties
# 톰캣의 최대 스레드 및 요청 설정
server.tomcat.max-threads=200
server.tomcat.min-spare-threads=10

# 요청 최대 크기 설정 (10MB)
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# CORS 설정
spring.web.cors.allowed-origins=http://localhost:3000
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE
spring.web.cors.allowed-headers=*

# WebClient 설정
spring.web.resources.static-locations=classpath:/static/
```

## 3. Security 설정
Spring Security 관련 설정입니다.
```properties
# 기본 보안 설정
spring.security.user.name=admin
spring.security.user.password=secret
spring.security.user.roles=USER

# CORS 및 CSRF 설정
spring.security.cors.enabled=true
spring.security.csrf.enabled=false
```

## 4. Data (JPA, Database) 설정
Spring Data 및 JPA 관련 설정입니다.
```properties
# 데이터베이스 연결 설정 (H2 예제)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Hibernate 설정
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## 5. Actuator 설정
Actuator를 활용한 애플리케이션 모니터링 설정입니다.
```properties
# Actuator 기본 설정
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
management.endpoint.info.enabled=true

# Prometheus 연동
management.metrics.export.prometheus.enabled=true
```

## 6. Integration 설정
Spring Cloud 및 REST 통신 관련 설정입니다.
```properties
# REST Template Timeout 설정
spring.rest.client.connect-timeout=5000
spring.rest.client.read-timeout=10000

# Kafka 설정 예제
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=my-group
spring.kafka.consumer.auto-offset-reset=earliest
```

## 7. DevTools 설정
Spring Boot 개발 도구 관련 설정입니다.
```properties
# DevTools 자동 리스타트 활성화
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
```

## 8. Testing 설정
Spring Boot 테스트 환경 설정입니다.
```properties
# H2 인메모리 데이터베이스 사용
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# 테스트용 JPA 설정
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Logging Level 설정
logging.level.org.springframework=DEBUG
```

이 설정들을 프로젝트의 필요에 따라 선택적으로 조정하면 됩니다! 🚀

