# 사이드카 패턴 (Sidecar Pattern)

## 개요

사이드카 패턴(일명 사이드킥 패턴)은 분산 시스템 아키텍처에서 사용되는 디자인 패턴으로, 메인 애플리케이션을 수정하지 않고도 그 기능을 확장하거나 강화하기 위해 별도의 컴포넌트를 메인 애플리케이션 옆에 배치하는 방식입니다. 이 패턴은 특히 마이크로서비스 및 클라우드 네이티브 아키텍처에서 인기가 있습니다.

<Image src= https://github.com/user-attachments/assets/254a03b6-ed2a-42ed-ad14-d68873b4f597 width="500"/>
<Image src = "https://github.com/user-attachments/assets/acf8f61a-86fc-490b-8c68-fbf7c07f1bf6" width="500"/>

## 목차

- [개요](#개요)
- [핵심 개념](#핵심-개념)
- [이점](#이점)
- [일반적인 사용 사례](#일반적인-사용-사례)
- [구현 방식](#구현-방식)
- [예제](#예제)
- [모범 사례](#모범-사례)
- [도전 과제 및 고려사항](#도전-과제-및-고려사항)
- [다른 패턴과의 비교](#다른-패턴과의-비교)
- [참고 자료](#참고-자료)

## 핵심 개념

사이드카 패턴은 두 가지 주요 구성 요소로 이루어져 있습니다:

1. **메인 애플리케이션(주 컨테이너)**: 핵심 비즈니스 기능을 제공하는 주요 애플리케이션입니다.
2. **사이드카(보조 컨테이너)**: 메인 애플리케이션 옆에서 실행되어 추가 지원이나 기능을 제공하는 보조 컴포넌트입니다.

사이드카는 메인 애플리케이션과 동일한 생명주기, 리소스, 네트워크 네임스페이스를 공유하지만 자체 프로세스나 컨테이너에서 실행됩니다.

## 이점

- **관심사 분리**: 메인 애플리케이션은 비즈니스 로직에 집중하고, 사이드카는 횡단 관심사(cross-cutting concerns)를 처리합니다.
- **기술 독립성**: 사이드카는 메인 애플리케이션과 다른 프로그래밍 언어로 작성될 수 있습니다.
- **재사용성**: 사이드카는 다양한 서비스에서 재사용될 수 있습니다.
- **독립적 배포**: 사이드카는 메인 애플리케이션과 독립적으로 업데이트될 수 있습니다.
- **기능 강화**: 애플리케이션 코드를 수정하지 않고도 기능을 추가할 수 있습니다.
- **관찰 가능성 향상**: 사이드카는 애플리케이션 변경 없이 메트릭, 로그, 트레이스를 수집할 수 있습니다.

## 일반적인 사용 사례

### 1. 서비스 메시 컴포넌트

Istio, Linkerd, Consul과 같은 서비스 메시에서:
- 트래픽 관리(라우팅, 로드 밸런싱)
- 서비스 디스커버리
- 보안(mTLS, 인증)
- 관찰 가능성(메트릭 수집)

### 2. 로깅 및 모니터링

- 로그 수집 및 전달
- 메트릭 수집 및 노출
- 분산 트레이싱

### 3. 보안

- TLS 종료(termination)
- 인증 및 권한 부여
- 통신 암호화/복호화
- 인증서 관리

### 4. 구성 관리

- 동적 구성 업데이트
- 기능 플래그 관리
- 환경별 구성

### 5. API 게이트웨이 기능

- 속도 제한(Rate limiting)
- 요청/응답 변환
- API 버전 관리

## 구현 방식

### 1. 컨테이너 기반 사이드카 (쿠버네티스 파드)

쿠버네티스에서 사이드카는 동일한 파드 내의 컨테이너로 구현됩니다:

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: application-with-sidecar
spec:
  containers:
  - name: main-application
    image: main-application:1.0
  - name: sidecar
    image: sidecar-image:1.0
    # 사이드카 구성
```

### 2. 프로세스 기반 사이드카

전통적인 환경에서 사이드카는 동일한 호스트에서 별도의 프로세스로 구현될 수 있습니다:

- 애플리케이션 옆에서 실행되는 시스템 데몬
- 수퍼바이저 관리 프로세스
- 초기화 시스템 관리 프로세스(systemd, upstart)

### 3. 라이브러리/SDK 통합

일부 경우에는 사이드카 패턴이 라이브러리 통합을 통해 구현될 수 있습니다:

- 애플리케이션이 외부 사이드카 프로세스와 통신하는 라이브러리를 포함
- 외부 프로세스가 횡단 관심사를 처리

## 예제

### 예제 1: Fluentd 사이드카를 이용한 로그 수집

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: app-with-logging
spec:
  containers:
  - name: app
    image: my-app:1.0
    volumeMounts:
    - name: log-storage
      mountPath: /var/log/app
  - name: log-collector
    image: fluentd:latest
    volumeMounts:
    - name: log-storage
      mountPath: /var/log/app
      readOnly: true
  volumes:
  - name: log-storage
    emptyDir: {}
```

### 예제 2: 서비스 메시를 위한 Envoy 프록시 사이드카

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: service-with-proxy
spec:
  containers:
  - name: service
    image: my-service:1.0
    ports:
    - containerPort: 8080
  - name: envoy-proxy
    image: envoyproxy/envoy:latest
    ports:
    - containerPort: 9901  # 관리 포트
    - containerPort: 10000 # 프록시 포트
    volumeMounts:
    - name: envoy-config
      mountPath: /etc/envoy
  volumes:
  - name: envoy-config
    configMap:
      name: envoy-config
```

### 예제 3: 앰배서더 패턴 (API 게이트웨이 사이드카)

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: app-with-ambassador
spec:
  containers:
  - name: app
    image: legacy-app:1.0
    ports:
    - containerPort: 8080
  - name: ambassador
    image: ambassador:1.0
    ports:
    - containerPort: 80
    env:
    - name: SERVICE_PORT
      value: "8080"
```

## 모범 사례

1. **리소스 할당**
   - 메인 애플리케이션과 사이드카 모두에 CPU 및 메모리 리소스를 적절히 할당
   - 쿠버네티스에서 리소스 제한 및 요청 사용 고려

2. **통신**
   - 가능한 경우 프로세스 간 통신에 localhost 또는 유닉스 소켓 사용
   - 메인 애플리케이션과 사이드카 간 명확한 API 계약 수립

3. **구성**
   - 구성을 구현에서 분리
   - 구성 파일, 환경 변수 또는 ConfigMaps(쿠버네티스) 사용

4. **생명주기 관리**
   - 적절한 시작 및 종료 순서 보장
   - 두 구성 요소 모두에 대한 헬스 체크 구현

5. **모니터링**
   - 메인 애플리케이션과 사이드카를 독립적으로 모니터링
   - 사이드카 오류에 대한 경고 설정

6. **보안**
   - 최소 권한 원칙 적용
   - 프로세스 간 통신 채널 보안

## 도전 과제 및 고려사항

- **증가된 리소스 소비**: 추가 컨테이너는 리소스 사용량 증가를 의미
- **복잡성**: 배포에 운영 복잡성 추가
- **디버깅 도전 과제**: 문제가 여러 컨테이너/프로세스에 걸쳐 발생할 수 있음
- **잠재적 성능 영향**: 프로세스 간 통신이 지연 시간을 유발할 수 있음
- **버전 호환성**: 애플리케이션과 사이드카 간 호환성 보장

## 다른 패턴과의 비교

| 패턴 | 사이드카 | 앰배서더 | 어댑터 |
|---------|---------|------------|---------|
| **목적** | 기능 확장 | 외부 세계에 메인 앱 대표 | 인터페이스 변환 |
| **위치** | 메인 앱 옆 | 앱과 외부 서비스 사이 | 앱과 외부 서비스 사이 |
| **예시** | 로깅 에이전트 | API 게이트웨이 | 프로토콜 변환기 |

