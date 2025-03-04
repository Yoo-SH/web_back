# Storage Cost: Postgres vs MySQL

## Overview
PostgreSQL과 MySQL의 스토리지 비용 차이를 이해하기 위한 문서입니다.

## Key Differences

### Index Structure
- **Postgres**: 보조 인덱스 → 실제 데이터 (직접 연결)
- **MySQL**: 보조 인덱스 → Primary Key → 실제 데이터 (간접 연결)

### Storage Impact
MySQL의 경우:
- Primary Key가 크면 모든 보조 인덱스도 커집니다
- 모든 리프 노드에 전체 행 데이터가 저장됩니다

## 결론
MySQL은 보조 인덱스가 Primary Key를 통해 데이터를 찾기 때문에, Primary Key가 크면 전체적인 저장 공간이 더 많이 필요할 수 있습니다.