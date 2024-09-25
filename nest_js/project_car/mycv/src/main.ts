import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';
const cookieSession = require('cookie-session');

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(cookieSession({ keys: ['asdas'] })); //cookie-session 설정. key를 이용해 쿠키에 저장된 정보를 암호화
  app.useGlobalPipes(new ValidationPipe({ whitelist: true })); //whitelist를 설정하여, DTO에 정의되지 않은 속성을 거부
  await app.listen(3000);
}
bootstrap();
