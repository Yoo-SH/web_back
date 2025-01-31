import {
  BadRequestException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { UsersService } from './users.service';
import { randomBytes, scrypt as _scrypt } from 'crypto';
import { promisify } from 'util';

const scrypt = promisify(_scrypt);

@Injectable()
export class AuthService {
  constructor(private usersService: UsersService) {} //usersService 의존성 주입

  async signUp(email: string, password: string) {
    //이메일 중복 확인
    const users = await this.usersService.find(email);
    if (users.length) {
      throw new BadRequestException('Email in use');
    }

    //해쉬된 패스워드 생성

    //솔트 생성
    const salt = randomBytes(8).toString('hex');
    //비밀번호와 솔트를 해쉬화하여 저장
    const hash = (await scrypt(password, salt, 32)) as Buffer; //타입스크립트는 scrypt가 뭐하는 지 인식할 수 있으니 buffer로 지정
    //해쉬된 비밀번호와 솔트를 조인
    const result = salt + '.' + hash.toString('hex'); //hash는 buffer이므로 16진수 string으로 변환

    // 유저 생성 및 저장
    const user = await this.usersService.create(email, result);

    //
    return user;
  }

  async signIn(email: string, password: string) {
    const [user] = await this.usersService.find(email);
    if (!user) {
      throw new NotFoundException('Bad credentials');
    }

    const [salt, storedHash] = user.password.split('.');
    const hash = (await scrypt(password, salt, 32)) as Buffer;

    if (storedHash !== hash.toString('hex')) {
      throw new BadRequestException('Bad credentials');
    }

    return user;
  }
}
