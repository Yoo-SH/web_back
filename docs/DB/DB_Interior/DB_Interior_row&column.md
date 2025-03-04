# 데이터베이스 저장 모델: Row-Oriented vs Column-Oriented

## 목차
- [개요](#개요)
- [Row-Oriented 데이터베이스](#row-oriented-데이터베이스)
- [Column-Oriented 데이터베이스](#column-oriented-데이터베이스)
- [비교 분석](#비교-분석)
- [사용 사례](#사용-사례)
- [주요 데이터베이스 제품](#주요-데이터베이스-제품)

## 개요
데이터베이스 저장 모델은 데이터를 물리적으로 저장하고 관리하는 방식을 결정합니다. Row-oriented와 Column-oriented는 각각 다른 접근 방식을 사용하여 서로 다른 사용 사례에 최적화되어 있습니다.

## Row-Oriented 데이터베이스

### 정의
Row-oriented 데이터베이스는 데이터를 행 단위로 저장하고 처리합니다. 각 행은 디스크에 연속적으로 저장되며, 하나의 레코드에 대한 모든 필드가 함께 저장됩니다.

### 특징
- 한 번에 전체 레코드를 읽고 쓰는 데 최적화
- 레코드 단위의 트랜잭션에 효율적
- 디스크 공간 활용이 효율적
- 새로운 레코드 삽입이 빠름

### 장점
- OLTP(Online Transaction Processing) 워크로드에 적합
- 단일 레코드 조회/수정이 빠름
- 트랜잭션 처리가 효율적
- 레코드 단위의 작업이 많은 애플리케이션에 적합
  ![Image](https://github.com/user-attachments/assets/cceff8d6-e8b3-496c-88e5-ed622361c3d3)
-
### 단점
- 특정 컬럼만 필요한 분석 쿼리에서 비효율적
- 대량의 데이터 집계 시 성능 저하
- 컬럼 단위 연산이 상대적으로 느림
  ![Image](https://github.com/user-attachments/assets/53d347ba-b52f-4b5a-9232-e6ca3368f4c8)
## Column-Oriented 데이터베이스

### 정의
Column-oriented 데이터베이스는 데이터를 열 단위로 저장하고 처리합니다. 각 컬럼의 데이터는 연속적으로 저장되며, 같은 타입의 데이터가 함께 저장됩니다.

### 특징
- 컬럼 단위로 데이터를 저장하고 처리
- 높은 압축률 (같은 타입의 데이터가 연속적으로 저장되므로)
- 분석 쿼리에 최적화된 구조
- 컬럼 단위의 집계 연산이 빠름

### 장점
- OLAP(Online Analytical Processing) 워크로드에 적합
- 대용량 데이터 분석에 효율적
- 높은 데이터 압축률
- 특정 컬럼만 조회하는 쿼리가 빠름
  ![Image](https://github.com/user-attachments/assets/690b018a-f722-4486-9468-de5694aab694)

### 단점
- 레코드 단위 삽입/수정이 상대적으로 느림
- 전체 레코드를 조회할 때 성능 저하
- 트랜잭션 처리가 상대적으로 복잡
  ![Image](https://github.com/user-attachments/assets/bb8eb76a-f8a3-4537-b401-06a0caa32af8)
## 비교 분석

### 성능 비교
| 작업 유형 | Row-Oriented | Column-Oriented |
|----------|--------------|-----------------|
| 단일 레코드 조회 | 빠름 | 느림 |
| 대량 데이터 분석 | 느림 | 빠름 |
| 레코드 삽입 | 빠름 | 느림 |
| 컬럼 기반 집계 | 느림 | 빠름 |

### 데이터 처리 특성
| 특성 | Row-Oriented | Column-Oriented |
|-----|--------------|-----------------|
| 데이터 압축 | 보통 | 우수 |
| 스캔 효율성 | 레코드 단위 | 컬럼 단위 |
| I/O 효율성 | 레코드 접근 시 높음 | 컬럼 접근 시 높음 |
| 메모리 사용 | 전체 레코드 로드 | 필요한 컬럼만 로드 |

## 사용 사례

### Row-Oriented 적합 사례
- 온라인 트랜잭션 처리 시스템
- 실시간 데이터 처리
- 웹 애플리케이션
- 사용자 프로필 관리
- 주문 처리 시스템

### Column-Oriented 적합 사례
- 데이터 웨어하우스
- 비즈니스 인텔리전스
- 로그 분석
- 대규모 데이터 마이닝
- 시계열 데이터 분석

## 주요 데이터베이스 제품

### Row-Oriented 데이터베이스
- MySQL
- PostgreSQL
- Oracle
- SQL Server
- SQLite

### Column-Oriented 데이터베이스
- Apache Cassandra
- Apache HBase
- ClickHouse
- Vertica
- Amazon Redshift

## 결론
데이터베이스 저장 모델 선택은 애플리케이션의 요구사항, 워크로드 특성, 데이터 접근 패턴에 따라 달라집니다. Row-oriented는 트랜잭션 처리에, Column-oriented는 분석 작업에 각각 최적화되어 있으므로, 사용 목적에 맞는 적절한 선택이 중요합니다.