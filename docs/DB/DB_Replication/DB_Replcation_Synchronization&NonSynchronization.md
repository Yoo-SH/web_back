# DB Replication: Synchronization & Non-Synchronization

이 문서는 데이터베이스 복제(Replication)에서 동기화(Synchronization) 및 비동기화(Non-Synchronization) 설정의 역할, 차이점 및 관리에 대해 설명합니다.

## 목차
1. [개요](#개요)
2. [동기화 복제](#동기화-복제)
3. [비동기화 복제](#비동기화-복제)
4. [장점 및 고려사항](#장점-및-고려사항)
5. [문제 해결](#문제-해결)

## 개요

데이터베이스 복제는 데이터의 가용성과 안정성을 높이기 위해 사용됩니다. 동기화 복제는 데이터의 일관성을 보장하는 반면, 비동기화 복제는 성능을 향상시킬 수 있습니다.

## 동기화 복제

- **역할**: 모든 데이터 변경이 마스터와 스탠바이 데이터베이스에 동시에 적용됩니다.
- **장점**: 데이터 일관성이 보장됩니다.
- **단점**: 네트워크 지연이 발생할 수 있으며, 성능이 저하될 수 있습니다.

- 원리: 마스터에서 데이터 변경이 발생하면, 해당 변경이 스탠바이 데이터베이스에 즉시 전파되어 두 데이터베이스 간의 데이터 일관성을 유지합니다. 이 과정에서 마스터의 데이터 변경이 스탠바이에 반영될 때까지 마스터의 트랜잭션이 완료되지 않으므로, "동시에"라는 표현을 사용합니다.

## 비동기화 복제

- **역할**: 데이터 변경이 마스터에 먼저 적용되고, 이후 스탠바이 데이터베이스에 전파됩니다.
- **장점**: 성능이 향상됩니다.
- **단점**: 데이터 일관성이 보장되지 않을 수 있습니다.

 - 원리: 비동기화 복제는 마스터 데이터베이스에서 데이터 변경이 발생한 후, 이러한 변경 사항이 스탠바이 데이터베이스로 전파되는 과정에서 지연이 있을 수 있음을 의미합니다. 즉, 마스터에서 변경이 즉시 완료되지만, 스탠바이 데이터베이스에 반영되는 시점은 나중일 수 있습니다.
## 장점 및 고려사항

- **동기화 복제의 장점**:
  - 데이터 일관성 보장
  - 장애 발생 시 데이터 손실 최소화

- **비동기화 복제의 장점**:
  - 성능 향상
  - 네트워크 부하 감소

- **고려사항**:
  - 동기화 복제는 네트워크 대역폭을 많이 사용합니다.
  - 비동기화 복제는 데이터 일관성 문제를 야기할 수 있습니다.

## 문제 해결

- **동기화 복제의 지연 문제**: 네트워크 상태 및 시스템 성능을 점검합니다.
- **비동기화 복제의 데이터 불일치 문제**: 주기적인 데이터 동기화 및 모니터링을 수행합니다.

이 문서는 데이터베이스 복제 설정 및 관리에 대한 기본적인 이해를 돕기 위한 것입니다. 각 데이터베이스 시스템의 특성에 맞는 세부 설정은 공식 문서를 참조하세요.
