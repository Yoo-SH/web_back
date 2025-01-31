module.exports = {
  // TypeScript 코드를 파싱하는 데 사용할 파서 설정
  parser: '@typescript-eslint/parser', // TypeScript 파일을 분석하기 위해 @typescript-eslint의 파서를 사용
  
  parserOptions: {
    project: 'tsconfig.json',          // TypeScript 설정 파일(tsconfig.json)을 참조
    tsconfigRootDir: __dirname,        // tsconfig.json 파일의 경로를 현재 디렉토리로 지정
    sourceType: 'module',              // ES6 모듈 구문을 사용할 것임을 명시
  },
  
  // 추가적인 ESLint 플러그인 설정
  plugins: ['@typescript-eslint/eslint-plugin'], // TypeScript용 ESLint 플러그인 사용

  // 기본적으로 확장할 규칙 세트 설정
  extends: [
    'plugin:@typescript-eslint/recommended',  // TypeScript에서 권장하는 기본 규칙을 사용
    'plugin:prettier/recommended',            // Prettier와 ESLint를 통합하여 코드 포맷팅을 강제
  ],

  // 이 설정이 프로젝트의 루트임을 명시
  root: true,

  // ESLint가 어떤 환경에서 동작할지를 정의 (Node.js와 Jest 테스트 환경에서 사용)
  env: {
    node: true,  // Node.js 환경에 맞게 설정
    jest: true,  // Jest 테스트 환경 지원
  },

  // 특정 파일이나 패턴을 무시할 수 있도록 설정
  ignorePatterns: ['.eslintrc.js'], // 이 ESLint 설정 파일 자체는 ESLint 검사에서 제외

  // 프로젝트 내에서 사용할 규칙을 정의 (모든 규칙을 warn 수준으로 설정)
  rules: {
    '@typescript-eslint/interface-name-prefix': 'warn',  // 인터페이스 이름에 'I' 접두사를 강제하지 않음
    '@typescript-eslint/explicit-function-return-type': 'warn',  // 함수의 명시적인 반환 타입을 요구하지 않음
    '@typescript-eslint/explicit-module-boundary-types': 'warn',  // 모듈 경계에서 반환 타입을 명시적으로 정의할 필요가 없음
    '@typescript-eslint/no-explicit-any': 'warn',  // 'any' 타입 사용을 허용 (보통 'any' 사용을 권장하지 않지만 프로젝트에 따라 허용)
  },

   // 추가적으로 @typescript-eslint/recommended 플러그인의 기본 규칙을 전부 warn으로 재정의
   overrides: [
    {
      files: ['*.ts', '*.tsx'], // TypeScript 파일에 적용
      rules: Object.fromEntries(
        Object.entries(require('@typescript-eslint/eslint-plugin/dist/configs/recommended').rules)
        .map(([rule, value]) => [rule, 'warn']) // 모든 규칙을 warn으로 설정
      ),
    },
  ],
};
