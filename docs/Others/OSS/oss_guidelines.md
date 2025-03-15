# OSS 가이드 라인

## 목차  
1. [OSS란?](#OSS란)  
2. [OSS를 사용하는 이유](#OSS를-사용하는-이유)  
   - 개발자  
   - 기업  
3. [주요 OSS 재단](#주요-OSS-재단)  
4. [OSS list 예시](#OSS-list-예시)  
5. [OSI에서 제시하는 OSS의 10가지 조건](#OSI에서-제시하는-OSS의-10가지-조건)  
6. [오픈소스 라이센스 정리](#오픈소스-라이센스-정리)  
7. [OSS의 보안성](#OSS의-보안성)  
8. [참고자료](#참고자료)  


## OSS란? 
소프트웨어의 내용을 프로그래밍 언어로 나타낸 ‘소스코드’를 공개하여 
누구나 개량·재배포할 수 있는 소프트웨어

<Image src = "https://github.com/user-attachments/assets/77d8edd1-2c91-4257-a495-78ade9432949" width=500px>

<br>


## OSS를 사용하는 이유

### 개발자
- 배움
    - 실제 기업에서 사용되는 수준의 코드를 얼마든지 보고배울 수 있음
    - 어떤 개발자도 누구에게나 배울점이 있음
- 협력
    - 어떤 개발자와도 협력할 수 있음
- 홍보
    - 나의 역량을 인증해주는 society를 가질 수 있고,또한 다양한 홍보의 기회를 가질 수 있음

### 기업 
- Freedom from vendor lock-in
    - 특정 기업의 서비스나 솔루션에 자사서비스가 의존성을 갖지않음
- QualityofSolutions
    - 전세계 개발자들이 함께만들고, 유지하는 고품질의 SW를 즉각 도입할 수 있음
    - OSS커뮤니티에서활동하는검증된인력을고용할수있음
- Abilitytocustomizeandfix
    - 그럼에도 불구하고 내 필요에 따라 개작하거나 수정할 수 있음
    - (핵심역량은“우리서비스”를만드는데집중)
- LowTCO
    - 초기 개발비용,유지관리 비용이 적음(Really?Notyetproved, I think)
- 이미성공적인 “Business&SWdevelopmentmodel”로검증됨

<br>

## 주요 OSS 재단  

### **Apache Software Foundation**  
- 가장 대표적인 오픈소스 재단. 350개 이상의 프로젝트 관리  
### **Free Software Foundation**  
- 무료 운영 시스템 구현을 목표로 둔 GNU 프로젝트만 관리  
### **Linux Foundation**  
- Linux Kernel 관리. 최근 SDx, IoT, Embedded, Cloud 등의 다양한 기술 협력

<br>

## OSS list 예시

<Image src= "https://github.com/user-attachments/assets/24fd3da5-29f1-49b3-94b7-0cf3a1506edc" width=500px>


<br>

## OSI (Open Source Initiative)에서 제시하고 있는 OSS의 10가지 조건

1. **자유 배포 (Free Redistribution)**  
   - 해당 SW를 여러 SW와 함께 모아 판매하거나 무상 배포 가능해야 하고, 이때 비용을 요구할 수 없음.

2. **소스코드 공개 (Source Code Open)**  
   - SW에는 이용하기 용이한 소스코드를 포함해야만 하며, 배포도 허용해야 함. 무료 다운로드 형태로 제공하는 것도 가능함.

3. **2차적 저작물 (Derived Works) 허용**  
   - 개작과 2차 저작물의 창작을 허용해야 하고, 그 결과물이 기존과 동일한 이용 허락(라이선스)을 사용해 배포되는 것을 허용해야 함.

4. **소스코드 수정 제한 (Integrity of The Author's Source Code)**  
   - 빌드 시에 적용되는 패치 파일과 그 소스코드를 함께 배포하는 것을 허용하며, 이때는 원저작자의 소스코드를 수정하는 것을 제한할 수 있음.

5. **개인이나 단체에 대한 차별 금지 (No Discrimination Against Persons or Groups)**  
   - (해설: 미국 등 몇몇 국가는 특정 종류의 SW 수출이 제한됨. 라이선스에 이러한 사항에 대해 주의를 줄 수 있지만, 라이선스 자체에서 제한하는 것은 금지.)

6. **사용 분야에 대한 제한 금지 (No Discrimination Against Fields of Endeavor)**  
   - (해설: 상업적 이용을 방해하는 조항이 들어가는 것을 금지함.)

7. **라이선스의 배포 (Distribution of License)**  
   - 배포가 이루어지면, 자동으로 SW에 부속된 권리가 재배포받은 모든 사람에게 적용되어야 한다.  
   - (해설: 기밀 유지 계약 같은 간접적인 수단으로 SW의 폐쇄를 금지함.)

8. **라이선스 적용상의 동일성 유지 (License Must Not Be Specific to a Product)**  
   - 특정 배포판에 소속된 경우에만 허용하거나, SW의 일부분을 분리해 사용하는 것을 제한하는 행위를 금지함.

9. **다른 라이선스의 포괄적 수용 (License Must Not Contaminate Other Software)**  
   - 해당 SW와 함께 배포하는 다른 SW에 대해 제한을 포함해서는 안 됨.  
   - (해설: 하나의 배포판으로 묶여 배포되더라도 각각의 SW는 서로 다른 라이선스를 가질 수 있음을 보장함.)

10. **라이선스의 기술적 중립성 (License Must Be Technology-Neutral)**  
   - 라이선스는 개별 기술이나 인터페이스 형태를 단정해서는 안 됨.  
   - (해설: Click-wrap 같은 강제 조항을 사용하는 것을 방지. 동의 여부에 대해 팝업창을 띄워 명시적으로 승낙을 받는 행위 금지.)

<br>

## 오픈소스 라이센스 정리(Apache, MIT, GPL, LGPL, BSD,  MPL, Eclipse)


### `Apache License`
 - 아파치 소프트웨어 재단에서 제정. `소스코드 공개 의무 없음. 단, 아파치 라이선스 사용을 밝혀야 함.` BSD보다 좀더 완화된 내용. 

- ex) 안드로이드, 하둡 등

### `MIT License`
 - BSD 라이선스를 기초로 MIT 대학에서 제정. `MIT 라이선스를 따르는 소프트웨어 사용하여 개발 시, 만든 개발품을 꼭 오픈소스로 해야 할 필요는 없음. 물론 소스코드 공개 의무도 없음`. 

- ex) X 윈도 시스템, Jsoup

### GNU GPL License
 - 자유소프트웨어 재단(FSF)에서 제정. GPL라이선스를 이용하여 개발 시 개인적, 내부적 이용에 한해서는 소스코드를 공개하지 않아도 되나, 외부 배포 시 해당 소프트웨어의 전체 소스코드를 공개해야 함. (3.0버전은 아파치 라이선스와 같이 사용 가능) 

- ex) 파이어폭스(2.0), 리눅스 커널, 깃, 마리아DB 등

### LGPL License
 - 기존 GPL의 높은 제약을 완화시키기 위해 탄생. LGPL로 작성된 소스코드를 라이브러리(정적, 동적)로만 사용하는 경우엔 소스코드를 공개하지 않아도 됨. 그 이외 사항은 GPL과 동일. 

- ex) 파이어폭스(2.1)

### BSD License
 - 버클리 캘리포니아 대학에서 제정. BSD 자체가 공공공기관에 만든 것이므로 공공환원의 의도가 강해서 저작권 및 라이선스 명시 이외엔 아무 제약이 없이 사용 가능한 자유로운 라이선스 

- ex) OpenCV


### MPL License
 - 1.0 버전은 넷스케이프 변호사였던 미첼 베이커가 작성, 1.1과 2.0버전은 모질라 재단에서 제정. 소스코드와 실행파일의 저작권 분리가 특징. MPL라이선스의 소스코드를 사용하여 개발했을 시, 수정한 소스코드는 MPL로 공개하고 원저작자에게 수정한 부분에 대해 알려야 하지만, 실행파일은 독점 라이선스로 배포 가능. 또한 MPL와 무관하게 작성된 소스코드는 공개할 필요 없음. 
- ex) 파이어폭스(1.1)

### Eclipse License
 - 이클립스 재단에서 제정. CPL을 대체하며, GPL보다 약한 수준으로 기업 친화적인 특징.

- ex) Eclipse



<br>

## OSS의 보안성: 항상 유의해야 함!  

### **OSS의 보안성 논의**  
- 아주 많은 리뷰어가 살펴보는 코드는 보안성이 높지만, 소수의 리뷰어만 관심을 갖는 코드, 많이 사용되지만 간과한 부분 등 언제나 보안 취약점의 위험은 존재할 수 있음. 실제 OSS에 다양한 보안 취약점 문제가 있을 수 있음.  
- 2016년 1,071개 SW에 대한 조사 결과, 96%가 OSS였으며, 그중 60%가 보안 취약점을 갖고 있었음.  
- 또한 67%의 OSS 개발업체가 보안 취약점에 대한 모니터링을 수행하지 않음.

<br>

## 참고자료
- [카카오 오픈소스](https://github.com/kakao)
- [네이버 오픈소스](https://github.com/naver)
- [Revolution OS 2001 Korean subtitle](https://www.youtube.com/watch?v=4ZHloJVhcRY)
- [오픈소스 라이센스 정리(GPL, LGPL, BSD, Apache, MIT, MPL, Eclipse)](https://blog.naver.com/occidere/220850682345)








