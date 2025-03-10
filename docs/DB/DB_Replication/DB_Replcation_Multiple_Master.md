# DB Replication: Multiple Master

이 문서는 데이터베이스 복제(Replication)에서 다중 마스터 설정의 역할, 설정 및 관리에 대해 설명합니다.

## 목차
1. [개요](#개요)
2. [다중 마스터 데이터베이스](#다중-마스터-데이터베이스)
3. [복제 설정](#복제-설정)
4. [마스터/스탠바이 구조와의 비교](#마스터-스탠바이-구조와의-비교)
5. [문제 해결](#문제-해결)

## 개요

다중 마스터 복제는 여러 마스터 데이터베이스가 동시에 쓰기 작업을 처리할 수 있도록 하여, 데이터 가용성과 쓰기 성능을 향상시킵니다. 각 마스터는 다른 마스터와 데이터를 동기화하여 일관성을 유지합니다.

## 다중 마스터 데이터베이스

- **역할**: 모든 마스터가 독립적으로 데이터 변경 작업(INSERT, UPDATE, DELETE)을 처리합니다.
- **설정**: 
  - 각 마스터는 다른 마스터와 데이터를 동기화하여 일관성을 유지합니다.
  - 충돌 해결 메커니즘이 필요합니다.

## 복제 설정

1. **데이터 동기화 설정**: 각 마스터 간의 데이터 동기화를 설정합니다.
2. **충돌 해결**: 데이터 충돌을 해결하기 위한 정책을 수립합니다.
3. **모니터링**: 복제 상태를 모니터링하여 문제를 조기에 발견합니다.

## 마스터/스탠바이 구조와의 비교

### 장점
- **동시 쓰기 성능**: 다중 마스터 구조는 여러 마스터가 동시에 쓰기 작업을 처리할 수 있어, 쓰기 성능이 향상됩니다.
- **고가용성**: 하나의 마스터에 장애가 발생해도 다른 마스터가 계속해서 쓰기 작업을 처리할 수 있습니다.

### 단점
- **데이터 충돌**: 여러 마스터가 동시에 데이터를 변경할 수 있어, 데이터 충돌 가능성이 높습니다.
- **복잡성**: 설정 및 관리가 복잡하며, 충돌 해결 메커니즘이 필요합니다.
- **네트워크 부하**: 모든 마스터 간의 동기화로 인해 네트워크 부하가 증가할 수 있습니다.


## 문제 해결

- **데이터 충돌**: 충돌 해결 정책을 검토하고 필요 시 수동 조정을 수행합니다.
- **복제 지연**: 네트워크 상태 및 시스템 성능을 점검합니다.



이 문서는 데이터베이스 다중 마스터 복제 설정 및 관리에 대한 기본적인 이해를 돕기 위한 것입니다. 각 데이터베이스 시스템의 특성에 맞는 세부 설정은 공식 문서를 참조하세요.
