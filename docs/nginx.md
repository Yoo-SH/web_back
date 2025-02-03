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


## Nginx 요약
Nginx는 가볍고 빠르며 확장성이 뛰어난 웹 서버로, 다양한 웹 애플리케이션에서 사용되고 있습니다. 기본적인 설정을 이해하고 필요에 맞게 구성하면, 더욱 효율적인 웹 서비스를 운영할 수 있습니다.



# `Nginx 보안 고려사항`


## 1. Brute Force 방지를 위한 Rate Limit 설정
Nginx에서 웹 서버에 대한 Brute Force 공격 등을 방지하기 위해 Rate Limit을 설정할 수 있습니다.

### 설정 방법:
```nginx
http {
    limit_req_zone $binary_remote_addr zone=one:10m rate=10r/s;

    server {
        location /login {
            limit_req zone=one burst=20 nodelay;
        }
    }
}
```

- 위 설정은 클라이언트 IP 기준으로 초당 10개의 요청을 허용하며, 최대 20개의 요청을 버스트(일시적 초과 허용)할 수 있습니다.



## 2. Nginx 버전 숨기기
공격자가 Nginx 버전을 알면 알려진 취약점을 이용할 수 있으므로, HTTP 응답 헤더에서 Nginx 버전을 숨기는 것이 좋습니다.

### 설정 방법:
```nginx
http {
    server_tokens off;
}
```

- 위 설정을 적용하면 응답 헤더에서 Nginx 버전이 표시되지 않습니다.



## 3. Clickjacking 방지를 위한 X-Frame-Options 설정
웹사이트가 악의적인 iframe을 통해 스푸핑(spoofing)이나 피싱(phishing) 공격을 받지 않도록 `X-Frame-Options` 헤더를 설정합니다.

### 설정 방법:
```nginx
server {
    add_header X-Frame-Options "SAMEORIGIN";
}
```

- `SAMEORIGIN` 옵션을 사용하면 동일한 도메인 내에서만 iframe이 허용됩니다.



## 4. Cross-Site Scripting (XSS) 방지를 위한 X-XSS-Protection 설정
브라우저에서 XSS 공격을 차단하도록 `X-XSS-Protection` 헤더를 활성화합니다.

### 설정 방법:
```nginx
server {
    add_header X-XSS-Protection "1; mode=block";
}
```

- 위 설정을 적용하면 브라우저에서 XSS 공격이 감지될 경우 자동으로 차단됩니다.



## 📌 추가 보안 설정
위의 설정 외에도 추가적인 보안 강화를 위해 다음과 같은 설정을 고려할 수 있습니다:

### 1. Content-Security-Policy (CSP) 설정
웹사이트에서 허용된 리소스만 로드하도록 제한하여 악성 코드 실행을 방지할 수 있습니다.
```nginx
server {
    add_header Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline'; object-src 'none';";
}
```

### 2. HTTP Strict Transport Security (HSTS) 설정
HTTPS 강제 적용을 통해 중간자 공격을 방지할 수 있습니다.
```nginx
server {
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains; preload";
}
```

### 3. MIME 스니핑 방지를 위한 X-Content-Type-Options 설정
브라우저가 선언되지 않은 MIME 타입을 추측하지 않도록 제한하여 보안성을 높입니다.
```nginx
server {
    add_header X-Content-Type-Options "nosniff";
}
```

### 4. Referrer Policy 설정
HTTP Referrer 정보를 제한하여 외부로부터의 정보 노출을 방지합니다.
```nginx
server {
    add_header Referrer-Policy "no-referrer-when-downgrade";
}
```

### 5. Permissions-Policy 설정
브라우저 기능(예: 카메라, 마이크 등)의 사용을 제한하여 보안을 강화합니다.
```nginx
server {
    add_header Permissions-Policy "geolocation=(), microphone=(), camera=()";
}
```

이러한 보안 설정을 적용하여 Nginx 웹 서버의 보안을 강화할 수 있습니다. 🚀



## 적용방법

__1. 위 설정을 nginx 설정 파일 (nginx.conf 또는 sites-enabled/default) 에 추가합니다.__

__2. nginx 설정을 테스트합니다.__
```sh
sudo nginx -t
```
__3. 설정을 적용하기 위해 nginx를 재시작합니다.__

```sh
sudo systemctl restart nginx
```



# `Nginx 추가 활용 방법`

## 1. http basic auth 설정
Nginx를 사용하여 웹 서버에 HTTP 기본 인증(Basic Auth)을 설정할 수 있습니다. 이를 통해 특정 디렉토리 또는 웹 페이지에 대한 접근을 사용자 이름과 비밀번호로 제한할 수 있습니다.  

활용 예시: 관리자 권한부여 페이지, 개발 서버 접근 제한 등



# `참고 자료`
- [유데미 NGINX Fundamentals: High Performance Servers from Scratch](https://www.udemy.com/course/nginx-fundamentals/?couponCode=KEEPLEARNING)
- [nginx와 http basic auth로 사용자 인증하기](https://ghleokim.github.io/http-basic-auth%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC/)