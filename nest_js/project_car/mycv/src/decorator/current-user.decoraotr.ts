import { createParamDecorator, ExecutionContext } from '@nestjs/common';

export const CurrentUser = createParamDecorator(
  (data: never, context: ExecutionContext) => {
    console.log('CurrentUser decorator has been called'); // 데코레이터 호출 로그 추가

    const request = context.switchToHttp().getRequest();
    const userId = request.session?.userId;

    if (!userId) {
      console.log('No userId found in session'); // userId가 없으면 출력
      return null;
    }

    console.log('UserId from session:', userId); // userId 로그 출력
    return userId;
  },
);
