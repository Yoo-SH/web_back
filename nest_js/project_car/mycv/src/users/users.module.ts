import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { UsersController } from './users.controller';
import { UsersService } from './users.service';
import { AuthService } from './auth.service';
import { User } from './user.entity';

@Module({
  imports: [TypeOrmModule.forFeature([User])], //모듈에 TypeOrmModule.forFeature()를 사용하여 엔티티를 가져옴, 모듈에 레파지토리 생성
  controllers: [UsersController],
  providers: [UsersService, AuthService],
})
export class UsersModule {}
