import globals from 'globals'; // window, document 같은 전역변수 인식.
import pluginJs from '@eslint/js'; //JavaScript 규칙 세트를 가져옴. 이 패키지는 JavaScript를 검증하기 위한 기본적인 규칙들을 제공
import tseslint from 'typescript-eslint'; //TypeScript 파일에서 ESLint를 사용하기 위해 필요한 규칙들과 플러그인들을 제공. TypeScript 코드도 ESLint를 통해 검증할 수 있게 도와줌.

export default [
  { languageOptions: { globals: globals.browser } }, //브라우저 환경에서 사용할 수 있는 전역 객체들(window, document 등)을 ESLint가 인식하도록 합니다. 브라우저 환경에서 코드를 작성할 때 유용합니다.
  /*JavaScript 파일에만 적용*/
  {
    files: ['**/*.js'],
    languageOptions: { sourceType: 'commonjs' }, // Node.js 환경에서 사용할 수 있는 require, module.exports 같은 CommonJS 모듈 시스템을 ESLint가 인식하도록 합니다. Node.js 환경에서 코드를 작성할 때 유용합니다.
    ...pluginJs.configs.recommended,
    rules: {
      ...pluginJs.configs.recommended.rules, // ESLint에서 제공하는 권장 규칙들을 가져옵니다.
      'no-undef': 'warn', // 'var' 키워드 사용 허용
      'no-require-imports': 'off', // 'exports'와 'require' 관련 오류 무시, 컴파일된 JavaScript 파일은 nodejs사용시 CommonJS 모듈 시스템(require, exports)을, 사용하고 있습니다. 그러나 ESLint는 기본적으로 ES 모듈 방식을 가정하고 있으며, require와 exports를 알지 못합니다.
      'no-unused-vars': 'warn', // 사용하지 않는 변수 경고로 변경
    },
  },
  /*TypeScript 파일에만 적용*/
  {
    files: ['**/*.{ts,tsx}'], // TypeScript 파일에만 적용
    languageOptions: { parser: tseslint.parser }, // ESlint가 TS 언어를 인식하도록 함. ex)  pulic 같은 TS 키워드를 인식
    ...tseslint.configs.recommended.rules, // TypeScript 파일에서 사용할 수 있는 권장 규칙들을 가져옵니다.
    rules: {
      'no-undef': 'warn', // 'var' 키워드 사용 허용
      'no-unused-vars': 'warn', // 사용하지 않는 변수 경고로 변경
    },
  },
];
