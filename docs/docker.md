# `Docker 기본 이해`

## 1. Docker란 무엇인가?

Docker는 애플리케이션을 컨테이너라고 하는 독립된 실행 환경에서 실행할 수 있도록 해주는 플랫폼입니다. 컨테이너는 운영 체제(OS)의 커널을 공유하지만, 애플리케이션과 그 의존성, 라이브러리 등을 포함한 독립적인 실행 환경을 제공합니다. 이는 애플리케이션을 어디서나 일관되게 실행할 수 있게 해 줍니다.

![Image](https://github.com/user-attachments/assets/47bfa1c7-6dc0-4544-adea-bb55e6210648)
![Image](https://github.com/user-attachments/assets/27c71416-b8f4-46eb-af5f-fa4a222b68d7)


### Docker의 주요 특징:
- **경량화**: Docker 컨테이너는 VM(가상 머신)보다 적은 자원을 사용합니다.
- **이식성**: 개발 환경과 프로덕션 환경을 동일하게 유지할 수 있습니다.
- **빠른 시작**: 컨테이너는 가상 머신에 비해 빠르게 시작할 수 있습니다.
- **격리성**: 각 컨테이너는 독립적으로 실행되어, 다른 컨테이너나 호스트 시스템에 영향을 미치지 않습니다.

## 2. Docker의 구성 요소

Docker는 다음과 같은 주요 구성 요소로 이루어져 있습니다.

### 1. Docker 이미지 (Image)
- Docker 이미지는 실행 가능한 패키지로, 애플리케이션, 라이브러리, 설정 파일 등을 포함하고 있습니다.
- 이미지는 불변의 파일 시스템으로, 여러 컨테이너가 동일 이미지를 기반으로 실행됩니다.

- commit은 주로 백업을 위해 사용되며, build는 이미지를 만들기 위해 사용됨.

![Image](https://github.com/user-attachments/assets/56f9d23f-67f3-4c75-8554-c0be5ac89fda)

- commit를 활용한 이미지 생성

![Image](https://github.com/user-attachments/assets/64fe2b29-f8ec-4d8a-a38f-110557d70842)

### 2. Docker 컨테이너 (Container)
- 컨테이너는 실행 중인 이미지를 기반으로 생성된 실행 환경입니다.
- 컨테이너는 격리된 환경에서 애플리케이션을 실행하며, 필요한 모든 의존성을 포함합니다.

### 3. Docker 엔진 (Docker Engine)
- Docker 엔진은 Docker 컨테이너를 실행하는 클라이언트-서버 애플리케이션입니다.
- `docker daemon`(서버)은 백그라운드에서 실행되며, 클라이언트의 명령을 받아 컨테이너를 관리합니다.

### 4. Docker 허브 (Docker Hub)
- Docker Hub는 Docker 이미지를 저장하고 공유하는 리포지터리입니다.
- 공개 및 비공개 이미지를 저장할 수 있으며, 다른 사용자가 만든 이미지를 쉽게 검색하고 사용할 수 있습니다.

## 3. Docker의 작동 원리

Docker는 이미지에서 컨테이너를 생성하여 애플리케이션을 실행합니다. 이 과정은 다음과 같습니다.

1. **이미지 빌드**: 애플리케이션과 그 의존성을 포함하는 Docker 이미지를 작성합니다.
2. **컨테이너 실행**: 빌드된 이미지를 기반으로 컨테이너를 실행합니다.
3. **컨테이너 종료 및 삭제**: 컨테이너가 더 이상 필요 없으면 종료하고 삭제할 수 있습니다.

## 4. Docker 명령어 기본 사용법


### Docker 이미지 관리
- `docker build -t <image_name> <path>`: 이미지를 빌드합니다.
- `docker pull <image_name>`: Docker Hub에서 이미지를 다운로드합니다.
- `docker images`: 로컬 시스템에 존재하는 Docker 이미지를 나열합니다.
- `docker rmi image1 image2 ...`: images 삭제, iamge의 인스턴스 컨테이너가 존재하면 해당 image 삭제 불가능
- `docker image prune`: 사용되지 않는 이미지의 컨테이너 모두 제거
- `docker image insepect image_id`: image_id에 대한 정보 출력

### Docker 컨테이너 관리

- `docker ps`: 실행 중인 컨테이너 목록을 확인합니다.
- `docker stop <container_id>`: 실행 중인 컨테이너를 중지합니다.
- `docker rm <container_id>`: 중지된 컨테이너를 삭제합니다.
- `docker run -d --name container_name --rm <image_name>`: 이미지를 기반으로 새 컨테이너를 실행합니다.(-d는 detache 옵션, --name은 container 이름 지정, --rm은 컨테이너가 종료되면 자동으로 삭제)
- `docker run -p port1:port2 images`: (-p는 publish, host포트:container포트, 외부포트에 내부포트를 publish함. 포트포워딩)
![Image](https://github.com/user-attachments/assets/50d19bec-f08d-4b28-9a27-82ff7dec36a9)


- `docker run -i -t images`: (-i는 인터렉티브 모드, -t는 가상 터미널 모드, 사용자의 값을 입력받아야할 때 사용) 
- `docker logs container` : 컨테이너에 의해 출력된 과거의 콘솔로그들을 볼 수 있음. backgound에서 움직이는 콘솔로그는 보이지 않을 때 사용

    

### Docker 네트워킹 및 볼륨
- `docker network ls`: Docker 네트워크 목록을 나열합니다.
- `docker volume ls`: Docker 볼륨 목록을 나열합니다.




## 5. Volumes와 Bind Mount

### 1. Docker Volumes
Docker Volumes는 **Docker 관리 하에 있는 저장소**입니다. 볼륨은 호스트 시스템의 파일 시스템과 격리되어, Docker의 `docker volume` 명령어를 사용하여 생성하고 관리합니다. 볼륨은 **컨테이너 재시작 후에도 데이터를 유지**하며, 여러 컨테이너에서 공유할 수 있습니다.

- **특징**:
  - 호스트 시스템의 파일 시스템과 격리되어 있으므로 Docker의 관리 하에 있습니다.
  - 여러 컨테이너가 동일 볼륨을 공유할 수 있습니다.
  - 백업, 복원, 마이그레이션 등을 쉽게 할 수 있습니다.
  - **컨테이너 삭제 시 이름이 존재하는 볼륨은 삭제되지 않으며, 데이터는 유지됩니다.**

- **사용 예시**:
  - `docker volume create <volume_name>`: 새로운 볼륨을 생성합니다.
  - `docker run -v <volume_name>:<container_path> <image_name>`: 컨테이너에서 볼륨을 사용합니다.
  - `docker volume ls`: 현재 시스템에 존재하는 볼륨을 나열합니다.
  - `docker volume rm <volume_name>`: 사용하지 않는 볼륨을 삭제합니다.

### 2. Bind Mount
Bind Mount는 **호스트 시스템의 파일이나 디렉토리**를 컨테이너 내의 경로에 연결하는 방식입니다. Bind Mount는 호스트 시스템의 실제 파일 시스템 경로를 그대로 사용하기 때문에, 데이터가 변경되면 호스트 시스템과 컨테이너 모두에서 변경 사항이 반영됩니다.

- **특징**:
  - 호스트 시스템의 파일 시스템에 직접 연결됩니다.
  - 변경 사항이 즉시 반영됩니다.
  - 호스트와 컨테이너가 동일한 파일을 공유할 수 있습니다.
  - 컨테이너가 삭제되면 **연결된 데이터는 삭제되지 않으며**, 호스트에 그대로 남습니다.
  - 호스트 시스템의 경로에 대한 접근 권한을 신경 써야 합니다.

- **사용 예시**:
  - `docker run -v <host_path>:<container_path> <image_name>`: 호스트 시스템의 경로를 컨테이너 내에 마운트합니다.
  - 예시: `docker run -v /path/to/host/data:/path/in/container my_image`

### 3. Volumes vs Bind Mount

| 항목               | Volumes                           | Bind Mount                        |
|--------------------|-----------------------------------|-----------------------------------|
| 관리 방식          | Docker가 관리                     | 호스트 시스템에 의존               |
| 용도               | 데이터를 여러 컨테이너 간 공유    | 호스트 시스템과 컨테이너 간 파일 공유  |
| 사용 용이성        | 간편함                            | 경로 및 권한 관리가 필요           |
| 데이터 지속성      | 컨테이너 삭제 후에도 유지됨       | 컨테이너와 호스트 시스템에서 공유됨   |

# 6. Docker Networking

Docker 네트워킹은 컨테이너 간의 통신을 관리하고, 외부 네트워크와의 연결을 가능하게 합니다. Docker는 기본적으로 컨테이너의 네트워크 격리를 제공하지만, 필요한 경우 컨테이너 간 네트워크를 설정하여 상호 연결할 수 있습니다.

## 1. Docker 네트워크 개념

Docker 컨테이너는 기본적으로 자체적인 네트워크 인터페이스를 가지며, 컨테이너 간 또는 외부 네트워크와 통신하기 위해 Docker 네트워크를 설정할 수 있습니다.

## 2. Docker copose를 이용한 네트워크 설정

Docker Compose를 사용하면 여러 컨테이너 간의 네트워크를 쉽게 설정할 수 있습니다. `docker-compose.yml` 파일을 사용하여 컨테이너 간의 네트워크를 정의하고, `docker-compose up` 명령어로 컨테이너를 실행할 수 있습니다. docker-compose.yml 파일에 존재하는 서비스들은 동일한 네트워크에 속하게 됩니다.


## 7. Docker의 장점

- **이식성**: 동일한 Docker 이미지를 다양한 환경에서 실행할 수 있습니다.
- **효율성**: Docker는 시스템 자원을 효율적으로 사용하며, 가상 머신에 비해 가벼운 환경을 제공합니다.
- **개발과 운영 일관성**: 개발 환경, 테스팅 환경, 프로덕션 환경이 동일하게 유지됩니다.
- **자동화**: CI/CD(지속적인 통합 및 배포) 파이프라인을 Docker를 사용하여 쉽게 구현할 수 있습니다.

## 8. Docker 사용 사례

- **개발 환경 설정**: 개발자들이 동일한 환경에서 작업할 수 있도록 도와줍니다.
- **마이크로서비스 아키텍처**: 독립적으로 실행되는 여러 서비스로 구성된 시스템에서 각 서비스가 컨테이너로 관리될 수 있습니다.
- **테스트 및 배포 자동화**: Docker는 테스트 및 배포를 자동화하고 일관되게 실행할 수 있게 해줍니다.

## 9. Docker와 가상 머신 비교

| 항목         | Docker                             | 가상 머신 (VM)                       |
|--------------|------------------------------------|-------------------------------------|
| 격리 수준    | 프로세스 격리                      | 하드웨어 수준 격리                  |
| 시작 시간    | 매우 빠름                          | 상대적으로 느림                    |
| 자원 사용    | 적음                               | 많음                                |
| 실행 환경    | 동일 OS 커널 공유                  | 각 VM은 자체 운영 체제를 가짐      |
| 성능         | 우수                               | 낮음                                |

## 10. 요약

Docker는 애플리케이션 개발과 배포에서 매우 유용한 도구로, 환경에 구애받지 않고 애플리케이션을 실행할 수 있게 해줍니다. 컨테이너 기반의 가벼운 환경은 개발자와 운영팀 모두에게 유리하며, CI/CD 파이프라인을 구축하는 데 큰 도움이 됩니다. Docker를 통해 개발자들은 효율적이고 일관된 환경에서 애플리케이션을 실행할 수 있습니다.


# `🐳 Dockerfile 명령어 설명  `
Dockerfile은 Docker 이미지를 생성하기 위한 설정 파일입니다. 주요 명령어를 설명하면 다음과 같습니다.

## Dockerfile 주요 명령어  

| 명령어 | 설명 | 예제 |
|--------|------|------|
| `FROM` | 베이스 이미지 지정 | `FROM ubuntu:20.04` |
| `RUN` | 명령 실행 (이미지 빌드 시) | `RUN apt-get update && apt-get install -y curl` |
| `CMD` | 컨테이너 실행 시 기본 실행 명령어 지정 | `CMD ["nginx", "-g", "daemon off;"]` |
| `ENTRYPOINT` | 컨테이너 실행 시 기본 실행 파일 지정 | `ENTRYPOINT ["python", "app.py"]` |
| `WORKDIR` | 작업 디렉토리 설정 | `WORKDIR /app` |
| `COPY` | 파일 복사 (호스트 → 컨테이너) | `COPY . /app` |
| `ADD` | 파일 복사 및 압축 해제 기능 | `ADD app.tar.gz /app` |
| `ENV` | 환경 변수 설정 | `ENV PORT=8080` |
| `EXPOSE` | 컨테이너에서 노출할 포트 지정 | `EXPOSE 80` |
| `VOLUME` | 데이터 볼륨 생성 | `VOLUME /data` |
| `ARG` | 빌드 시 변수 설정 | `ARG VERSION=1.0` |
| `LABEL` | 메타데이터 추가 | `LABEL maintainer="user@example.com"` |
| `HEALTHCHECK` | 컨테이너 상태 확인 | `HEALTHCHECK CMD curl --fail http://localhost || exit 1` |
| `USER` | 실행 사용자 지정 | `USER node` |

---

```docker
# 1.1 Python Flask 웹 애플리케이션 Dockerfile


# 베이스 이미지 설정
FROM python:3.9

# 작업 디렉토리 설정
WORKDIR /app

# 의존성 파일 복사 및 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 애플리케이션 파일 복사
COPY . .

# 컨테이너 실행 시 실행할 명령어 설정
CMD ["python", "app.py"]
```

# `🐳 Docker Compose 명령어 설명  `
Docker Compose는 여러 개의 컨테이너를 정의하고 관리하는 도구입니다. 주요 명령어는 다음과 같습니다.

## Docker Compose 주요 명령어  

| 명령어 | 설명 |
|--------|------|
| `docker-compose up` | `docker-compose.yml`을 실행하여 컨테이너를 생성하고 실행 |
| `docker-compose up -d` | 백그라운드에서 컨테이너 실행 (detached mode) |
| `docker-compose down` | 실행 중인 모든 컨테이너 및 네트워크 정리 |
| `docker-compose build` | Dockerfile을 기반으로 이미지를 빌드 |
| `docker-compose start` | 중지된 컨테이너를 다시 시작 |
| `docker-compose stop` | 실행 중인 컨테이너 중지 |
| `docker-compose restart` | 컨테이너 재시작 |
| `docker-compose ps` | 현재 실행 중인 컨테이너 목록 확인 |
| `docker-compose logs` | 컨테이너 로그 출력 |
| `docker-compose logs -f` | 실시간 로그 출력 (follow mode) |
| `docker-compose exec <서비스명> <명령>` | 실행 중인 컨테이너 내에서 명령 실행 |
| `docker-compose run <서비스명> <명령>` | 새 컨테이너를 생성하여 명령 실행 |
| `docker-compose config` | `docker-compose.yml`의 구성을 확인 |
```yaml
#  Flask + PostgreSQL 서비스
version: '3.8'

services:
  web:
    build: .
    ports:
      - "5000:5000"
    environment:
      - DATABASE_URL=postgresql://user:password@db:5432/mydatabase
    depends_on:
      - db

  db:
    image: postgres:13
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: mydatabase
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```



# `Docker 배포 프로세스 정리`

## Docker 배포 프로세스

0. __ssh로 EC2 22 port에 연결하여 동작 및 빌드하기__ : EC2에 접속하여 도커를 설치하고, 도커 컨테이너를 실행하기 위한 명령어를 입력하기 위한 접속
1. __로컬에서 dockerfile 또는 docker compose작성__ : 도커 이미지를 만들기 위한 설정 파일
2. __로컬에서 docker image build__ : 도커 이미지를 만들기 위한 명령어
3. __로컬에서 도커 허브에 docker push__ : 도커 이미지를 도커 레지스트리에 올리기 위한 명령어(dockerignore 파일을 통해 불필요한 파일을 제외해야함. ex. node_modules, dockerfile, docker-compose.yml)
4. __EC2에서 docker pull(EC2 아웃바운드는 이미 오픈되어있음)__ : 도커 이미지를 다운로드 받기 위한 명령어
5. __EC2에서 docker run__ : 도커 컨테이너를 만들기 위한 명령어
6. __EC2에서 docker container start__ : 도커 컨테이너를 시작하기 위한 명령어
7. __EC2에서 인바운드 규칙 수정__ : 기존에 22번 포트만 열려있는 것을 80번 443번 포트를 열어줌

## Docker 배포환경 코드 수동적 업데이트 

1. __로컬에서 코드 수정__ : 코드 수정
2. __로컬에서 docker image build__ : 수정된 코드를 반영한 도커 이미지를 만들기
3. __로컬에서 도커 허브에 docker push__ : 수정된 도커 이미지를 도커 레지스트리에 올리기
4. __EC2에서 docker pull__ : 수정된 도커 이미지를 다운로드 받기 위한 명령어
5. __EC2에서 docker container stop__ : 기존 도커 컨테이너를 중지
6. __EC2에서 docker container run__ : 수정된 도커 컨테이너를 실행


**주의사항**
- 바인드 마운트는 변경된 소스코드 또는 일부 구성을 수시로 미러링하기 위한 로컬 개발용 도구로 사용되어야 함. 실제 배포 환경에서는 사용해서는 안됨.
## Docker 배포 멀티스테이지 빌드

개발환경에서는 자체적인 웹 서버를 사용하지만, 배포환경에서는 nginx를 사용하는 경우가 많음. 이때, multi stage build를 사용하면, 개발환경에서 사용하는 웹 서버를 사용하고, 배포환경에서는 nginx를 사용할 수 있음.

react나 vue의 경우, 배포환경에서는 build된 파일을 nginx에 올리는 경우가 많음. 이때, build된 파일을 올리기 위해서는 nginx를 설치하고, build된 파일을 올리는 작업을 해야함. 이때, multi stage build를 사용하면, build된 파일을 올리기 위한 작업을 하나의 이미지로 만들 수 있음.


``` docker
# dockerfile.prod

FROM node:14-alpine as build # build stage

WORKDIR /app

COPY package.json .

RUN npm install

COPY . .

RUN npm run build

FROM nginx:stable-alpine # production stage

COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]


```


**참고사항**

- DB를 컨테이너를 이용해서 사용하는 것은 어려울 수 있음(백업이나 로드밸런싱등의 문제로) 따라서, DB는 RDS등의 관리형 서비스를 이용하는 것이 좋을 수 있음.

**주의사항**
- EC2에서 업데이트를 하면, 기존의 컨테이너가 삭제되고 새로운 컨테이너가 생성됨. 따라서, 컨테이너 내부의 데이터는 모두 삭제됨. 따라서, 데이터를 보존해야하는 경우에는 EFS STORAGE을 사용해야함.(RDS를 사용하지 않는 경우)







# `참고 강의`
- [생활코딩 Docker 입문수업](https://www.youtube.com/watch?v=Ps8HDIAyPD0&list=PLuHgQVnccGMDeMJsGq2O-55Ymtx0IdKWf)
- [생활코딩 Docker 심화수업](https://www.youtube.com/watch?v=RMNOQXs-f68&list=PLhdkdG5wpxcmrAbjqo2oyfO08X2Li6Uau)
- [유데미 【한글자막】 Docker & Kubernetes : 실전 가이드](https://www.udemy.com/course/docker-kubernetes-2022/)
