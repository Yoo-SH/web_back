import {
  UseInterceptors,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { plainToClass } from 'class-transformer';

interface ClassConstructor {
  new (...args: any[]): object; //클래스 생성자 시그니처, 모든 클래스를 의미
}

//Serialize 데코레이터를 사용하여 응답 데이터를 DTO로 변환 + 데코레이터는 타입스크립트에서 인자에 타입을 못 넣으니 인터페이스를 넣음
export function Serialize(dto: ClassConstructor) {
  return UseInterceptors(new SerializeInterceptor(dto));
}

export class SerializeInterceptor implements NestInterceptor {
  constructor(private dto: any) {}

  intercept(context: ExecutionContext, handler: CallHandler): Observable<any> {
    return handler.handle().pipe(
      map((data: any) => {
        return plainToClass(this.dto, data, {
          excludeExtraneousValues: true, //expose 데코레이터가 붙은 필드만 반환
        });
      }),
    );
  }
}
