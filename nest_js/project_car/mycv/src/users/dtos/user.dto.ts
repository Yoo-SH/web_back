import { Expose } from 'class-transformer';

//인터셉트하여 사용자에게 일부 가공하여 보여줄 정보를 정의한 DTO
export class UserDto {
  @Expose()
  id: number;

  @Expose()
  email: string;
}
