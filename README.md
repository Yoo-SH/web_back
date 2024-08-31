# Backend Roadmap

- [2024년 현대적 백엔드 개발자가 되기 위한 단계별 가이드](https://roadmap.sh/backend)




## web deployment 필수 확인사항 
 
* 테스트 코드 
    - [테스트 코드? 어느 범위까지?](https://velog.io/@city7310/%EB%B0%B1%EC%97%94%EB%93%9C%EA%B0%80-%EC%9D%B4%EC%A0%95%EB%8F%84%EB%8A%94-%ED%95%B4%EC%A4%98%EC%95%BC-%ED%95%A8-16.-%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B3%A0%EB%AF%BC-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C-%EC%9E%91%EC%84%B1-1)
    - [테스트 코드 작성의 기본 원칙](https://f-lab.kr/insight/backend-test-code-writing-20240716), 
    - [Node.js에서 Unit Test 활용](https://velog.io/@yenicall/Nodejs%EC%97%90%EC%84%9C-Unit-Test-%ED%99%9C%EC%9A%A9) 
    -  [Node.js에서의 테스팅 전략](https://www.deviantceblog.com/node-js%EC%97%90%EC%84%9C%EC%9D%98-%ED%85%8C%EC%8A%A4%ED%8C%85-%EC%A0%84%EB%9E%B5/)
*  환경변수 
    - [환경 변수(PATH)란?](https://gliver.tistory.com/43)
    - [환경변수란? 환경변수와 path](https://m.blog.naver.com/zxwnstn/221521038431)
    - [Node 개발 및 배포용 환경변수 관리하기](https://seungtaek95.github.io/node/node_env/)
    - [[react] 실무 개발 환경/배포 환경 설정(.env)](https://han-py.tistory.com/441)
*  브라우저간 지원 평가(최신 vesion - ex) flex box)
    - [css 브라우저 호환 확인 사이트](https://caniuse.com/?search=flex%20box)

* 검색엔진 최적화
    - [검색엔진 최적화(SEO) 기본 가이드](https://imweb.me/faq?mode=view&category=29&category2=35&idx=2471)
    - [SEO (검색엔진 최적화)란? – 구글, 네이버 가이드 총정리
](https://seo.tbwakorea.com/blog/seo-guide-2022/)
    - [SEO(검색엔진최적화)가이드북 총정리 2024](https://www.hedleyonline.com/ko/blog/seo-guide/)
    - [자바스크립트 검색엔진 최적화(SEO) 가이드 총 정리](https://helloinyong.tistory.com/308)
* 성능 항상 & 자산 축소(JS, 이미지)
    - [Javascript 코드를 축소하는 이유는?
](https://www.cloudflare.com/ko-kr/learning/performance/why-minify-javascript-code/)
    - [[자바스크립트] 압축(Minify) / 난독화(Uglify) 
			](https://12bme.tistory.com/357)
    - [js minify 사이트](https://www.toptal.com/developers/javascript-minifier)
* 동적 웹사이트, DB 배포
    - [클라우드 서버에 배포하기(AWS)](https://junvelee.tistory.com/55)

# more info


## JS

```

-자바스크립트 싱글스레드인데 왜 비동기적 일까?
https://velog.io/@eamon3481/%EC%9E%90%EB%B0%94%EC%8A%A4%ED%81%AC%EB%A6%BD%ED%8A%B8-%EC%8B%B1%EA%B8%80%EC%8A%A4%EB%A0%88%EB%93%9C%EC%9D%B8%EB%8D%B0-%EC%99%9C-%EB%B9%84%EB%8F%99%EA%B8%B0%EC%A0%81-%EC%9D%BC%EA%B9%8C

-비동기 처리와 callback 함수
https://velog.io/@jaeung5169/%EB%B9%84%EB%8F%99%EA%B8%B0-%EC%B2%98%EB%A6%AC%EC%99%80-callback-%ED%95%A8%EC%88%98
```

## Router

```
-get과 post 사용구분 및 기능적 특성
https://whales.tistory.com/120

-redirect와 render 사용구분
https://dev-mht.tistory.com/73
https://ssungkang.tistory.com/entry/Django-render-%EC%99%80-redirect-%EC%9D%98-%EC%B0%A8%EC%9D%B4

```

## MiddleWear

```

-node.js, express, 미들웨어란?
https://velog.io/@wjddnjswjd12/node.js-express-%EB%AF%B8%EB%93%A4%EC%9B%A8%EC%96%B4%EB%9E%80


-미들웨어 개념 및 기능
https://markme-inur.tistory.com/73
https://velog.io/@unyoi/%EC%9D%B8%ED%94%84%EB%9D%BC-%EB%BF%8C%EC%8B%9C%EA%B8%B01-%EB%AF%B8%EB%93%A4%EC%9B%A8%EC%96%B4-%EA%B0%9C%EB%85%90%EC%9D%84-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90
https://medium.com/twolinecode/13-%EB%AF%B8%EB%93%A4%EC%9B%A8%EC%96%B4-middleware-%EB%9E%80-23abb33ec4f4

-로컬 전역변수로 이용
https://junspapa-itdev.tistory.com/13
https://expressjs.com/en/5x/api.html#res.locals


```

## multer

```
-[Node.js] express + multer로 이미지 업로드 구현하기
https://velog.io/@mainfn/Node.js-express-multer%EB%A1%9C-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0

-Node.js Multer - 사용자의 파일 업로드를 처리하고 서버에 저장하기
https://luvris2.tistory.com/756
```

## Cookie와 Session

```
-쿠키와 세션의 개념
https://velog.io/@octo__/%EC%BF%A0%ED%82%A4Cookie-%EC%84%B8%EC%85%98Session

-기능적 특성
https://dev-coco.tistory.com/61

```

## 권한과 인증부여

```
-Authentication (인증) VS Authorization (권한부여/인가)
https://velog.io/@10000ji_/Spring-Security-Authorization%EA%B6%8C%ED%95%9C%EB%B6%80%EC%97%AC%EC%9D%B8%EA%B0%80%EC%9D%98-%EA%B0%9C%EB%85%90-%EC%9D%B4%ED%95%B4-%EB%B0%8F-%EA%B5%AC%ED%98%84
```

## Security

```
-크로스 사이트 요청 위조란?
https://nordvpn.com/ko/blog/csrf/?srsltid=AfmBOooxWPL8mAlzMXcstihlZKtWyKOzo2evvWnlUiTAiCTVCHoC0HUH

-CSRF란, CSRF 동작원리, CSRF 방어방법
https://devscb.tistory.com/123

-웹 취약점과 해킹 매커니즘#7 XSS(Cross-Site Scripting)
https://www.fis.kr/ko/major_biz/cyber_safety_oper/attack_info/security_news?articleSeq=3408

-SQL 인젝션 공격을 방지하는 5가지 팁
https://powerdmarc.com/ko/sql-injection-prevention/
https://velog.io/@k4minseung/DB-SQL-Injection-%EA%B3%B5%EA%B2%A9%EA%B3%BC-%EB%B0%A9%EC%96%B4-%EB%B0%A9%EB%B2%95

-NoSQL Injection
https://velog.io/@m200is/NoSQL-Injection

-Node.js secure 코딩 규칙 10가지
https://openeg.tistory.com/752


-Node.js에서의 Error Handling 기본 예시
https://velog.io/@yenicall/Node.js%EC%97%90%EC%84%9C%EC%9D%98-Error-Handling-%EA%B8%B0%EB%B3%B8-%EC%98%88%EC%8B%9C

-정적파일을 express static으로 express할시에는 코드가 모두 공개됨.
```

## 참고사항

```
- nodejs 디버깅 방법
https://medium.com/@imjuni/node-js-%EB%94%94%EB%B2%84%EA%B9%85-%EB%8F%84%EA%B5%AC-devtool-5e8ec01ba71e
https://velog.io/@younoah/nodejs-debug
https://velog.io/@moon-yerim/node.js-%ED%99%98%EA%B2%BD%EC%97%90%EC%84%9C-%EB%94%94%EB%B2%84%EA%B9%85%ED%95%98%EA%B8%B0


-유데미 스타터스 취업 부트캠프 2기 - 백엔드(java, 자바) 합격 후기 및 1주차 학습 일지
https://velog.io/@inhalin/%EC%9C%A0%EB%8D%B0%EB%AF%B8-%EC%8A%A4%ED%83%80%ED%84%B0%EC%8A%A4-%EC%B7%A8%EC%97%85-%EB%B6%80%ED%8A%B8%EC%BA%A0%ED%94%84-2%EA%B8%B0-%EB%B0%B1%EC%97%94%EB%93%9Cjava-%EC%9E%90%EB%B0%94-%ED%95%A9%EA%B2%A9-%ED%9B%84%EA%B8%B0-%EB%B0%8F-1%EC%A3%BC%EC%B0%A8-%ED%95%99%EC%8A%B5-%EC%9D%BC%EC%A7%80
```