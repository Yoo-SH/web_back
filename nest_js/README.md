## setting

nest cli
```
1.npm install -g @nestjs/cli
2.nest new project_name
3.nest generate(g) module module_name
4.nest generate(g) controller project_name/controller_name [--flat] (flat 옵션 안 넣으면 모듈 프로젝트에 종속적으로 들어감.)
5.nest generate(g) service service_name
5.npm install class-validator
6,npm install class-transformer
```

## package.json
```
{
  "name": "messages", // 프로젝트의 이름 (프로젝트 식별용)
  "version": "0.0.1", // 현재 프로젝트의 버전
  "description": "", // 프로젝트에 대한 설명 (현재는 비어 있음)
  "author": "", // 프로젝트의 작성자 정보 (현재는 비어 있음)
  "private": true, // true로 설정하면 npm에 이 패키지를 퍼블리시하지 않음
  "license": "UNLICENSED", // 프로젝트가 라이센스되지 않았음을 명시 (기본적으로 오픈소스가 아님)

  "scripts": {
    "build": "nest build", // NestJS 프로젝트 빌드 (컴파일)
    "format": "prettier --write \"src/**/*.ts\" \"test/**/*.ts\"", // Prettier로 코드 포맷팅
    "start": "nest start", // NestJS 애플리케이션을 시작
    "start:dev": "nest start --watch", // 개발 모드로 애플리케이션 시작 (파일 변경 시 자동 재시작)
    "start:debug": "nest start --debug --watch", // 디버그 모드로 애플리케이션 시작 (watch 모드 포함)
    "start:prod": "node dist/main", // 프로덕션 모드로 컴파일된 애플리케이션을 시작 (dist 폴더의 main.js 실행)
    "lint": "eslint \"{src,apps,libs,test}/**/*.ts\" --fix", // ESLint를 실행하여 코드를 검사하고 자동 수정
    "test": "jest", // Jest를 사용하여 테스트 실행
    "test:watch": "jest --watch", // Jest 테스트를 watch 모드로 실행 (파일 변경 시 자동 테스트)
    "test:cov": "jest --coverage", // 테스트 커버리지를 생성
    "test:debug": "node --inspect-brk -r tsconfig-paths/register -r ts-node/register node_modules/.bin/jest --runInBand", // 디버그 모드로 테스트 실행
    "test:e2e": "jest --config ./test/jest-e2e.json" // e2e (엔드 투 엔드) 테스트 실행
  },

  // 애플리케이션에서 사용하는 필수 종속성
  "dependencies": {
    "@nestjs/common": "^10.0.0", // NestJS의 공통 모듈
    "@nestjs/core": "^10.0.0", // NestJS의 핵심 모듈
    "@nestjs/platform-express": "^10.0.0", // Express 플랫폼과의 통합을 위한 NestJS 모듈
    "reflect-metadata": "^0.2.0", // 데코레이터를 지원하기 위한 메타데이터 라이브러리
    "rxjs": "^7.8.1" // Reactive Extensions for JavaScript (NestJS에서 비동기 스트림 처리를 위해 사용)
  },

  // 개발 환경에서만 사용하는 종속성
  "devDependencies": {
    "@nestjs/cli": "^10.0.0", // NestJS CLI 도구 (프로젝트 생성, 관리, 빌드 등에 사용)
    "@nestjs/schematics": "^10.0.0", // NestJS 스키마틱 (CLI에서 코드 생성에 도움)
    "@nestjs/testing": "^10.0.0", // NestJS의 테스트 유틸리티
    "@types/express": "^4.17.17", // Express.js에 대한 TypeScript 타입 정의
    "@types/jest": "^29.5.2", // Jest에 대한 TypeScript 타입 정의
    "@types/node": "^20.3.1", // Node.js에 대한 TypeScript 타입 정의
    "@types/supertest": "^6.0.0", // Supertest에 대한 TypeScript 타입 정의 (HTTP 요청 테스트 도구)
    "@typescript-eslint/eslint-plugin": "^8.0.0", // TypeScript용 ESLint 플러그인
    "@typescript-eslint/parser": "^8.0.0", // ESLint에서 TypeScript를 파싱하는 데 사용
    "eslint": "^8.42.0", // ESLint: 코드 스타일 및 품질 검사 도구
    "eslint-config-prettier": "^9.0.0", // Prettier와 ESLint 규칙을 충돌 없이 통합
    "eslint-plugin-prettier": "^5.0.0", // Prettier를 ESLint와 통합하는 플러그인
    "jest": "^29.5.0", // JavaScript 테스트 프레임워크
    "prettier": "^3.0.0", // 코드 포맷팅 도구
    "source-map-support": "^0.5.21", // 소스맵 지원을 위한 라이브러리 (디버깅 시 유용)
    "supertest": "^7.0.0", // HTTP 요청을 테스트할 수 있는 라이브러리
    "ts-jest": "^29.1.0", // Jest와 TypeScript 통합을 위한 도구
    "ts-loader": "^9.4.3", // Webpack과 TypeScript 통합을 위한 로더
    "ts-node": "^10.9.1", // TypeScript 파일을 직접 실행할 수 있게 해주는 도구
    "tsconfig-paths": "^4.2.0", // TypeScript 경로 별칭을 런타임에서 사용할 수 있게 해줌
    "typescript": "^5.1.3" // TypeScript 컴파일러
  },

  // Jest 테스트 환경 설정
  "jest": {
    "moduleFileExtensions": [
      "js",   // JavaScript 파일을 인식
      "json", // JSON 파일을 인식
      "ts"    // TypeScript 파일을 인식
    ],
    "rootDir": "src", // 테스트 파일의 기본 디렉토리를 src로 설정
    "testRegex": ".*\\.spec\\.ts$", // .spec.ts로 끝나는 파일을 테스트 파일로 인식
    "transform": {
      "^.+\\.(t|j)s$": "ts-jest" // ts-jest를 사용하여 TypeScript와 JavaScript를 변환
    },
    "collectCoverageFrom": [
      "**/*.(t|j)s" // 커버리지 보고서를 생성할 파일 설정 (ts, js 파일 포함)
    ],
    "coverageDirectory": "../coverage", // 커버리지 결과를 저장할 디렉토리
    "testEnvironment": "node" // 테스트 환경을 Node.js로 설정
  }
}


```


## tsconfig.jso
```
{
  "compilerOptions": {
    // 모듈 시스템을 commonjs로 설정 (Node.js 환경에서 사용되는 표준 모듈 시스템)
    "module": "commonjs",

    // TypeScript에서 타입 정의 파일(.d.ts)을 생성하도록 설정
    "declaration": true,

    // 컴파일된 코드에서 주석을 제거
    "removeComments": true,

    // 컴파일 시 데코레이터의 메타데이터를 생성하도록 설정 (NestJS에서 데코레이터 사용 시 필수)
    "emitDecoratorMetadata": true,

    // 실험적인 데코레이터 기능을 사용할 수 있도록 활성화 (TypeScript에서 데코레이터 사용을 가능하게 함)
    "experimentalDecorators": true,

    // 기본 모듈 내보내기를 사용하도록 설정 (ES6 모듈의 기본 내보내기 구문 지원)
    "allowSyntheticDefaultImports": true,

    // 컴파일 타겟을 ES2021로 설정 (ES2021 기능을 포함한 코드로 컴파일)
    "target": "ES2021",

    // 소스맵 파일을 생성하여 디버깅을 쉽게 할 수 있도록 설정
    "sourceMap": true,

    // 컴파일된 파일을 저장할 디렉토리를 설정 (dist 디렉토리로 설정)
    "outDir": "./dist",

    // 상대 경로의 기준이 되는 기본 경로를 현재 프로젝트 루트로 설정
    "baseUrl": "./",

    // 컴파일 속도를 높이기 위해 이전 컴파일 결과를 활용할 수 있도록 설정
    "incremental": true,

    // 라이브러리 파일(.d.ts) 검사를 생략하여 컴파일 속도를 높임
    "skipLibCheck": true,

    // 엄격한 null 체크를 비활성화 (null과 undefined를 좀 더 자유롭게 사용할 수 있게 함)
    "strictNullChecks": false,

    // any 타입을 사용했을 때 경고를 하지 않도록 설정
    "noImplicitAny": false,

    // 함수의 call, bind, apply 메서드를 엄격하게 검사하지 않도록 설정
    "strictBindCallApply": false,

    // 파일 이름의 대소문자 일관성을 강제하지 않음 (대소문자 차이로 인한 오류 방지)
    "forceConsistentCasingInFileNames": false,

    // switch 문의 case에서 누락된 경우 fallthrough를 허용 (컴파일러가 fallthrough 오류를 경고하지 않음)
    "noFallthroughCasesInSwitch": false
  }
}

```

