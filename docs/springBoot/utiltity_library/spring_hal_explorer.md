# Spring HAL Explorer  

## 목차  
1. [개요](#개요)  
2. [주요 목적](#주요-목적)  
3. [얻을 수 있는 정보](#얻을-수-있는-정보)  
4. [사용 방법](#사용-방법)  
5. [사용 시나리오](#사용-시나리오)  
6. [Swagger와 차이](#swagger와-차이)  

## 개요
Spring HAL Explorer는 REST API를 쉽게 탐색하고 테스트할 수 있는 웹 기반 도구입니다. Spring Data REST 프로젝트와 함께 사용되며, API 엔드포인트를 시각적으로 보고 상호작용할 수 있습니다.

## 주요 목적
- **API 탐색 용이성**: 복잡한 REST API를 직관적인 웹 인터페이스로 쉽게 탐색
- **개발 및 디버깅**: API 개발 중 빠른 테스트 및 디버깅 지원
- **문서화**: API의 사용 방법을 시각적으로 이해하기 쉽게 제공

## 얻을 수 있는 정보
- 사용 가능한 모든 API 엔드포인트 목록
- 각 엔드포인트에서 지원하는 HTTP 메서드(GET, POST, PUT, DELETE 등)
- 데이터 구조 및 관계 정보
- API 요청 시 필요한 파라미터
- API 응답 데이터의 구조와 형식

## 사용 방법
1. 의존성 추가: `spring-data-rest-hal-explorer`
2. 애플리케이션 실행
3. 브라우저에서 `http://localhost:8080/explorer/index.html` 접속

## 사용 시나리오
- 백엔드 개발자: API 테스트 및 디버깅
- 프론트엔드 개발자: 백엔드 API의 구조 이해 및 통합 테스트
- QA 엔지니어: API 기능 테스트
- 기술 문서 작성자: API 문서화 지원


## Swagger와 차이

### 목적과 초점의 차이
- **HAL Explorer**: HAL(Hypertext Application Language) 형식의 REST API에 특화되어 있어요. 링크 기반 리소스 탐색과 하이퍼미디어 중심의 API를 위해 설계되었습니다.
- **Swagger**: 더 일반적인 REST API 문서화와 테스팅에 초점을 맞추고 있어요. 다양한 API 스타일과 함께 사용할 수 있습니다.

### 기능적 차이
1. **탐색 방식**:
   - HAL Explorer: 하이퍼링크를 따라 API를 탐색하는 방식으로, 하나의 리소스에서 관련 리소스로 이동합니다.
   - Swagger: 전체 API를 한눈에 볼 수 있는 문서 형식으로 제공합니다.

2. **통합 용이성**:
   - HAL Explorer: Spring Data REST와 자동 통합되어 있어 별도 설정이 거의 필요 없습니다.
   - Swagger: 다양한 프레임워크와 함께 사용할 수 있지만, 보통 추가 설정과 어노테이션이 필요합니다.

3. **문서화 스타일**:
   - HAL Explorer: 실제 API 응답 구조를 직접 탐색하는 방식입니다.
   - Swagger: API 스펙을 상세히 문서화하고 예제를 포함할 수 있습니다.

4. **지원 포맷**:
   - HAL Explorer: HAL 포맷(JSON+HAL)에 특화되어 있습니다.
   - Swagger: 다양한 요청/응답 포맷을 지원합니다.

### 사용 시나리오

- **HAL Explorer**: Spring Data REST나 다른 HAL 호환 API를 사용할 때 가장 적합합니다.
- **Swagger**: 복잡한 API 문서화가 필요하거나, 다양한 프레임워크를 사용할 때 더 유연합니다.

간단히 말해, HAL Explorer는 하이퍼미디어 API 탐색에 초점을 맞추고, Swagger는 더 포괄적인 API 문서화와 테스팅에 초점을 맞추고 있습니다.