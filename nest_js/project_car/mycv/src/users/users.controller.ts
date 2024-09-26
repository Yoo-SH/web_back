import {
  Body,
  Controller,
  Post,
  Get,
  Delete,
  Param,
  Query,
  Session,
  Patch,
} from '@nestjs/common';
import { CreateUserDto } from './dtos/create-user.dto';
import { UpdateUserDto } from './dtos/update-user.dto';
import { UsersService } from './users.service';
import { Serialize } from '../interceptors/serialize.interceptor';
import { UserDto } from './dtos/user.dto';
import { AuthService } from './auth.service';
import { CurrentUser } from '../decorator/current-user.decoraotr';
import { User } from './user.entity';

@Serialize(UserDto)
@Controller('auth')
export class UsersController {
  constructor(
    private usersService: UsersService,
    private authService: AuthService,
  ) {}

  @Post('/signup')
  async createUser(@Body() body: CreateUserDto, @Session() session: any) {
    const user = await this.authService.signUp(body.email, body.password);
    session.userId = user.id;
    return user;
  }

  @Post('/signin')
  async signIn(@Body() body: CreateUserDto, @Session() session: any) {
    const user = await this.authService.signIn(body.email, body.password);
    session.userId = user.id;
    console.log('Session after sign in:', session); // 세션 로그 확인
    return user;
  }

  @Post('/signout')
  signOut(@Session() session: any) {
    console.log('Signout route called'); // 로그아웃 라우트 호출 확인

    session.userId = null;
    console.log('Session after signout:', session); // 로그아웃 후 세션 확인
  }

  @Get('/whoami')
  whoAmI(@CurrentUser() user: User) {
    console.log('whoAmI route called'); // 메서드가 호출되는지 확인하기 위한 로그 추가

    return user;
  }
  /* 
  @Get('/whoami')
  whoAmI(@Session() session: any) {
    console.log('whoAmI route called'); // 메서드가 호출되는지 확인하기 위한 로그 추가
    console.log('Session UserId:', session.userId); // session.userId 확인

    return this.usersService.findOne(session.userId);
  } */

  @Get('/:id')
  findUser(@Param('id') id: string) {
    return this.usersService.findOne(parseInt(id));
  }

  @Get()
  findAllUsers(@Query('email') email: string) {
    return this.usersService.find(email);
  }

  @Delete('/:id')
  removeUser(@Param('id') id: string) {
    return this.usersService.remove(parseInt(id));
  }

  @Patch('/:id')
  updateUser(@Param('id') id: string, @Body() body: UpdateUserDto) {
    return this.usersService.update(parseInt(id), body);
  }
}
