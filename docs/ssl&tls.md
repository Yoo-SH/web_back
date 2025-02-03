# SSL/TLS 인증서 정리

## 🔹 SSL/TLS 개요
SSL(Secure Sockets Layer)과 TLS(Transport Layer Security)는 인터넷에서 데이터를 안전하게 전송하기 위한 암호화 프로토콜입니다. SSL은 TLS의 이전 버전이며, 현재는 보안상의 이유로 TLS가 권장됩니다.

## 🔹 SSL/TLS 인증서란?
SSL/TLS 인증서는 웹사이트와 사용자의 데이터 암호화를 보장하는 디지털 인증서입니다. 주로 웹사이트에서 HTTPS를 활성화하는 데 사용됩니다.


## 🔹 SSL과 TLS의 차이점
| 항목 | SSL | TLS |
|------|-----|-----|
| 최신 버전 | SSL 3.0 (더 이상 사용되지 않음) | TLS 1.3 |
| 보안성 | 취약점이 많음 | 보안성이 향상됨 |
| 속도 | 상대적으로 느림 | 성능 최적화로 속도가 빠름 |
| 지원 여부 | 대부분 폐기됨 | 최신 브라우저 및 서버에서 지원 |

## 🔹 TLS 버전별 특징
| 버전 | 주요 특징 |
|------|----------|
| TLS 1.0 | SSL 3.0을 기반으로 개선, 현재는 보안상 비추천 |
| TLS 1.1 | 일부 보안 개선, 현재는 대부분 폐기됨 |
| TLS 1.2 | 주요 보안 강화, 현재도 많이 사용됨 |
| TLS 1.3 | 속도 및 보안 강화, 최신 표준 |


## 🔹 SSL/TLS 인증서 종류
| 종류 | 설명 |
|------|------|
| DV(Domain Validation) | 도메인 소유권만 인증하는 기본 인증서 |
| OV(Organization Validation) | 조직의 신원까지 검증하는 인증서 |
| EV(Extended Validation) | 가장 신뢰도가 높은 인증서로, 엄격한 검증 절차 필요 |
| Wildcard | 특정 도메인과 그 하위 도메인에 적용 가능 |
| Multi-Domain (SAN) | 여러 개의 도메인을 하나의 인증서로 보호 |

## 🔹 SSL/TLS 인증서 작동 원리
1. **클라이언트가 서버에 접속** (예: 웹 브라우저가 웹사이트에 연결)
2. **서버가 SSL/TLS 인증서를 제공**
3. **클라이언트가 인증서를 검증** (신뢰할 수 있는 CA 발급 여부 확인)
4. **암호화된 세션 키 생성 및 교환**
5. **안전한 데이터 전송 시작**
![Image](https://github.com/user-attachments/assets/f9a8d30a-d3b3-47ca-beb0-60b88686dfa8)
## 🔹 SSL/TLS 인증서 설치 및 확인 방법
1. **인증서 발급 받기** (CA에서 신청 후 발급)
2. **웹 서버에 설치** (Apache, Nginx, IIS 등)
3. **브라우저에서 확인** (HTTPS 활성화 및 인증서 정보 확인)
4. **SSL Labs 등에서 테스트** (https://www.ssllabs.com/ssltest/)

## 🔹 SSL/TLS 인증서 발급 기관 (CA)
- DigiCert
- GlobalSign
- Let's Encrypt (무료 SSL 제공)
- Sectigo (구 Comodo)
- GoDaddy 등



# 참고문서
[ssl이란 웹툰1](https://minix.tistory.com/395)
[ssl이란 웹툰2](https://blog.naver.com/weekamp/221494419179)
[생활코딩 HTTPS와 SSL 인증서](https://opentutorials.org/course/228/4894)
[Nginx에서 SSL 인증서 갱신하기](https://coor.tistory.com/47)
