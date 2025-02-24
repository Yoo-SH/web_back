# SaramIn Crawling Project

## 프로젝트 설명
이 프로젝트는 SaramIn 채용 정보를 크롤링하여 저장하고, 이를 REST API를 통해 제공하는 SpringBoot 기반 웹 애플리케이션입니다.

---

## 빌드 및 실행 방법

### 1. 필수 요구 사항
- Java 17
- Gradle 7.6 이상
- PostgreSQL 15.x
- Swagger UI를 통한 API 테스트 가능

### 2. 빌드 및 실행 명령어
아래 명령어를 실행하여 프로젝트를 빌드하고 실행할 수 있습니다.

#### Gradle 기반 빌드 및 실행
```bash
# 의존성 설치 및 빌드
./gradlew build

# 애플리케이션 실행
java -jar build/libs/saramIn-crawling-0.0.1-SNAPSHOT.jar

```
# **API 엔드포인트**

> 이 문서에서는 프로젝트에서 사용되는 API 엔드포인트와 간단한 설명을 제공합니다.

---

## **인증 관련**

| HTTP 메서드 | 엔드포인트        | 설명                              | 요청 예시                            |
|-------------|-------------------|-----------------------------------|---------------------------------------|
| POST        | `/auth/register` | 회원가입                         | `{ "username": "user1", "password": "pass123" }` |
| POST        | `/auth/login`    | 로그인 및 JWT 발급                | `{ "username": "user1", "password": "pass123" }` |
| POST        | `/auth/refresh`  | 리프레시 토큰을 이용한 액세스 갱신 | 쿠키에 `REFRESH_TOKEN` 포함 필요       |

---

## **채용 공고**

| HTTP 메서드 | 엔드포인트        | 설명                      | 요청 예시                                                              |
|-------------|-------------------|---------------------------|--------------------------------------------------------------------|
| GET         | `/jobs`          | 채용 공고 목록 조회       | `http://localhost:8080/jobs?page=0&size=10&location=서울&keyword=자바` |
| GET         | `/jobs/{id}`     | 특정 채용 공고 상세 조회  | `http://localhost:8080/jobs/1`                                     |

---

## **북마크**

| HTTP 메서드 | 엔드포인트               | 설명                      | 요청 예시                           |
|-------------|--------------------------|---------------------------|-------------------------------------|
| POST        | `/bookmarks/{jobId}`     | 특정 공고 북마크 추가/삭제 | `http://localhost:8080/bookmarks/1` |
| GET         | `/bookmarks`            | 북마크 목록 조회           | `http://localhost:8080/bookmarks`  |

---

## **지원 관리**

| HTTP 메서드 | 엔드포인트              | 설명                          | 요청 예시                                |
|-------------|-------------------------|-------------------------------|------------------------------------------|
| POST        | `/applications`        | 특정 공고 지원 등록           | `http://localhost:8080/applications?jobId=1` |
| GET         | `/applications`        | 지원 내역 조회                | `http://localhost:8080/applications`    |
| DELETE      | `/applications/{id}`   | 특정 지원 취소                | `http://localhost:8080/applications/1`  |

---

## **Swagger 문서**
Swagger를 통해 API 문서를 제공하며, API 테스트도 가능합니다.

| 설명       | URL                                                |
|------------|----------------------------------------------------|
| Swagger UI | `http://113.198.66.75:10043/swagger-ui/index.html` |
| Swagger JSON | `http://113.198.66.75:10043/v3/api-docs`                |
