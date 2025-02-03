# `웹 서버`

## 소개
웹 서버는 인터넷 또는 인트라넷을 통해 클라이언트에게 웹 콘텐츠를 제공하는 소프트웨어 또는 하드웨어입니다. 네트워크 요청을 처리하고 적절한 웹 페이지, 파일 또는 기타 리소스를 응답합니다.

## 동작 방식
1. 클라이언트(일반적으로 웹 브라우저)가 웹 서버에 HTTP 요청을 보냅니다.
2. 웹 서버는 요청을 처리하고, 필요한 경우 요청된 콘텐츠를 검색하거나 애플리케이션 서버로 전달합니다.
3. 서버는 요청된 콘텐츠(예: HTML, CSS, JavaScript, 이미지 등)를 포함한 HTTP 응답을 클라이언트에 반환합니다.

## 주요 웹 서버
- **Apache HTTP Server**: Apache 소프트웨어 재단에서 개발한 오픈소스 웹 서버.
- **Nginx**: 로드 밸런싱 및 리버스 프록시 기능으로 유명한 고성능 웹 서버.
- **Microsoft IIS (Internet Information Services)**: Windows Server 환경에서 사용되는 웹 서버 소프트웨어.
- **LiteSpeed**: 내장 캐싱 기능을 갖춘 고성능 웹 서버.
- **Tomcat**: 주로 Java 기반 애플리케이션을 제공하는 웹 서버.

## 주요 기능
- HTTP 요청 및 응답 처리
- 정적 및 동적 콘텐츠 제공 지원
- SSL/TLS 암호화와 같은 보안 기능 구현
- 로드 밸런싱 및 리버스 프록시 기능
- 로깅 및 모니터링 기능

## 기본 설정 (Nginx 예제)
1. Nginx 설치:
   ```sh
   sudo apt update
   sudo apt install nginx -y
   ```
2. 웹 서버 시작:
   ```sh
   sudo systemctl start nginx
   ```
3. 부팅 시 자동 시작 활성화:
   ```sh
   sudo systemctl enable nginx
   ```
4. 웹 브라우저에서 `http://localhost` 또는 `http://your-server-ip`를 열어 설치 확인.

## 웹 서버 요약
웹 서버는 웹 인프라의 기본 구성 요소로, 웹 콘텐츠의 호스팅 및 제공을 가능하게 합니다. 웹 개발 및 관리에 있어 그 기능, 설정, 보안 실무를 이해하는 것이 중요합니다.


