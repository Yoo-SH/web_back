# `Nginx 기본 이해`

## 소개
Nginx(Nginx)는 고성능 [웹 서버](https://github.com/Yoo-SH/web_back/blob/main/docs/webServer.md)이자 [리버스 프록시 서버](https://github.com/Yoo-SH/web_back/blob/main/docs/proxy.md), 그리고 `로드 밸런서 및 HTTP 캐시 역할을 수행하는 오픈 소스 소프트웨어`입니다. 높은 동시 접속 처리 능력을 갖추고 있으며, 가벼운 리소스 사용과 빠른 응답 속도로 널리 사용됩니다.

## 특징
- **고성능 및 확장성**: 이벤트 기반 비동기 처리 모델을 사용하여 높은 트래픽을 효율적으로 처리합니다.
- **리버스 프록시 및 로드 밸런싱**: 여러 서버로 트래픽을 분산하여 부하를 줄이고 성능을 향상시킵니다.
- **정적 콘텐츠 제공 최적화**: 정적 파일(HTML, CSS, JavaScript, 이미지 등)에 대한 빠른 응답 속도를 제공합니다.
- **보안 강화**: DDoS 공격 방어, SSL/TLS 지원 및 인증 기능을 제공합니다.
- **다양한 프로토콜 지원**: HTTP, HTTPS, TCP, UDP, WebSocket 등의 프로토콜을 지원합니다.

## 설치 방법
### Ubuntu/Debian 기반 시스템
```sh
sudo apt update
sudo apt install nginx
```

### CentOS/RHEL 기반 시스템
```sh
sudo yum install epel-release
sudo yum install nginx
```

### macOS (Homebrew 사용)
```sh
brew install nginx
```

## 기본 사용법
### Nginx 서비스 시작 및 관리
```sh
# Nginx 시작
sudo systemctl start nginx

# Nginx 중지
sudo systemctl stop nginx

# Nginx 재시작
sudo systemctl restart nginx

# Nginx 상태 확인
sudo systemctl status nginx
```
## 기본 설정 예제

아래는 보편적으로 사용되는 Nginx 설정 예제입니다.

### 1. 기본 웹 서버 설정
```nginx
server {
    listen 80;
    server_name example.com;

    root /var/www/html;
    index index.html index.htm;

    location / {
        try_files $uri $uri/ =404;
    }
}
```

### 2. 리버스 프록시 설정
```nginx
server {
    listen 80;
    server_name api.example.com;

    location / {
        proxy_pass http://127.0.0.1:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 3. HTTPS 설정 (Let's Encrypt 사용 예제)
```nginx
server {
    listen 80;
    server_name example.com;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name example.com;

    ssl_certificate /etc/letsencrypt/live/example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/example.com/privkey.pem;

    location / {
        root /var/www/html;
        index index.html index.htm;
    }
}
```

### 4. 캐싱 설정 (정적 파일 캐싱)
```nginx
server {
    listen 80;
    server_name example.com;

    location /static/ {
        root /var/www/html;
        expires 30d;
        add_header Cache-Control "public, max-age=2592000";
    }
}
```

### 5. 로드 밸런서 설정
```nginx
upstream backend_servers {
    server 192.168.1.101;
    server 192.168.1.102;
}

server {
    listen 80;
    server_name example.com;

    location / {
        proxy_pass http://backend_servers;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

## Nginx 설정 파일 위치
- 기본 설정 파일: `/etc/nginx/nginx.conf`
- 사이트 설정 파일: `/etc/nginx/sites-available/`
- 활성화된 사이트 설정: `/etc/nginx/sites-enabled/`

## 주요 명령어
```sh
# Nginx 설정 테스트
nginx -t

# Nginx 재시작
systemctl restart nginx

# Nginx 설정 다시 불러오기 (무중단 반영)
systemctl reload nginx
```

## 설정 파일 구조
Nginx의 기본 설정 파일은 `/etc/nginx/nginx.conf`에 위치하며, 주요 디렉터리는 다음과 같습니다:
- `/etc/nginx/nginx.conf` : 메인 설정 파일
- `/etc/nginx/sites-available/` : 사용 가능한 가상 호스트 설정
- `/etc/nginx/sites-enabled/` : 활성화된 가상 호스트 설정
- `/var/log/nginx/access.log` : 액세스 로그 파일
- `/var/log/nginx/error.log` : 에러 로그 파일

## 간단한 리버스 프록시 설정 예제
아래 설정을 통해 Nginx를 리버스 프록시 서버로 동작하도록 구성할 수 있습니다:
```nginx
server {
    listen 80;
    server_name example.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

## Nginx 요약
Nginx는 가볍고 빠르며 확장성이 뛰어난 웹 서버로, 다양한 웹 애플리케이션에서 사용되고 있습니다. 기본적인 설정을 이해하고 필요에 맞게 구성하면, 더욱 효율적인 웹 서비스를 운영할 수 있습니다.



## 참고 자료
- [유데미 NGINX Fundamentals: High Performance Servers from Scratch](https://www.udemy.com/course/nginx-fundamentals/?couponCode=KEEPLEARNING)
