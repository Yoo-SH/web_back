import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';

@Entity() //엔터티 생성
export class Report {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  price: number;
}
