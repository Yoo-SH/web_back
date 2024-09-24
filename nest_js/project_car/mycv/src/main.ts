import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(new ValidationPipe({ whitelist: true })); //whitelist를 설정하여, DTO에 정의되지 않은 속성을 거부
  await app.listen(3000);
}
bootstrap();
