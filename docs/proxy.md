# `proxy(프록시)란?`
__클라이언트와 서버 사이에 중계 역할을 하는 서버__

## forward proxy(포워드 프록시)
- 클라이언트들을 보호
- 클라이언트들이 인터넷에 접근할 때 중간에 위치하여 클라이언트들의 요청을 받아서 인터넷에 접근하는 서버
- 장점
    - 클라이언트들의 IP를 숨길 수 있다.(익명성 유지)
    - 캐싱을 통해 빠른 속도를 제공할 수 있다.
    - 콘텐츠를 필터링 할 수 있다.
![Image](https://github.com/user-attachments/assets/c0daeaf4-73b6-49f7-940d-684e93c832b0)

## reverse proxy(리버스 프록시)
- 서버들을 보호
- 클라이언트들이 서버에 접근할 때 중간에 위치하여 서버들의 요청을 받아서 서버에 전달하는 서버
- 장점
    - 서버들의 IP를 숨길 수 있다.(보안 강화)
    - 캐싱을 통해 빠른 속도를 제공할 수 있다.
    - 부하 분산을 통해 서버의 안정성을 높일 수 있다.(로드 밸런싱)
![Image](https://github.com/user-attachments/assets/c3d07205-7b42-40fe-93d5-096edfde9c08)

## Load Balancer(로드 밸런서)
__여러대의 서버가 요청을 분산 처리할 수 있도록 나눠주는 서비스__

- L4 로드 밸런서: IP & 포트 번호를 기반으로 분산(TCP, UDP)
    - https://yoo-sh.com로 접근시 서버 A, 서버 B 중 하나로 분산
- L7 로드 밸런서: Application layer(user request) level에서 분산 (HTTP,HTTPS 헤더 URL 등의 정보를 기반)
    - https://yoo-sh.com로 접근시 /category, /search 등의 URL에 따라 담당 서버들로 분산



## 참고 자료
- [[10분 테코톡] 🐿 제이미의 Forward Proxy, Reverse Proxy, Load Balancer](https://www.youtube.com/watch?v=YxwYhenZ3BE&list=LL&index=37)
- [Proxy(프록시)란?? Forward vs Reverse Proxy 차이점은 무엇일까? [시스템 디자인]](https://www.youtube.com/watch?v=Rt-KdCpsmdc&list=LL&index=38)