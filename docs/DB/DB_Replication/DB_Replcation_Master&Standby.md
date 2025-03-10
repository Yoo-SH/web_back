# DB Replication: Master & Standby

이 문서는 데이터베이스 복제(Replication)에서 마스터와 스탠바이의 역할, 설정 및 관리에 대해 설명합니다.

## 목차
1. [개요](#개요)
2. [마스터 데이터베이스](#마스터-데이터베이스)
3. [스탠바이 데이터베이스](#스탠바이-데이터베이스)
4. [복제 설정](#복제-설정)
5. [장점 및 고려사항](#장점-및-고려사항)
6. [문제 해결](#문제-해결)

## 개요

데이터베이스 복제는 데이터의 가용성과 안정성을 높이기 위해 사용됩니다. 마스터 데이터베이스는 모든 쓰기 작업을 처리하며, 스탠바이 데이터베이스는 마스터의 데이터를 복제하여 읽기 작업을 처리하거나 장애 발생 시 마스터 역할을 대신할 수 있습니다.

__INSERT/UPDATE/CREATE__
![Image](https://github.com/user-attachments/assets/be8bb1b0-c96b-4b0d-a87d-0f3f5e419018)

__SELECT__
![Image](https://github.com/user-attachments/assets/11cb5e3c-6627-46d5-96d7-5db7d2bc42f8)

## 마스터 데이터베이스

- **역할**: 모든 데이터 변경 작업(INSERT, UPDATE, DELETE)을 처리합니다.
- **설정**: 
  - 로그 전송을 통해 스탠바이 데이터베이스에 변경 사항을 전달합니다.
  - 고가용성을 위해 주기적인 백업이 필요합니다.

## 스탠바이 데이터베이스

- **역할**: 마스터 데이터베이스의 변경 사항을 수신하고 적용합니다.
- **설정**:
  - 마스터로부터 로그를 수신하여 데이터를 동기화합니다.
  - 읽기 전용 쿼리를 처리하여 마스터의 부하를 줄입니다.
  - 장애 발생 시 마스터로 승격될 수 있습니다.

## 복제 설정

1. **로그 전송 설정**: 마스터에서 스탠바이로 로그를 전송하는 설정을 구성합니다.
2. **데이터 동기화**: 초기 데이터베이스 상태를 동기화하고 지속적으로 업데이트합니다.
3. **모니터링**: 복제 상태를 모니터링하여 문제를 조기에 발견합니다.

## 장점 및 고려사항

- **장점**:
  - 데이터 가용성 향상
  - 읽기 성능 향상
  - 장애 복구 시간 단축

- **고려사항**:
  - 네트워크 대역폭 요구
  - 데이터 일관성 문제
  - 복제 지연 가능성

## 문제 해결

- **복제 지연**: 네트워크 상태 및 시스템 성능을 점검합니다.
- **데이터 불일치**: 로그 전송 및 적용 상태를 확인하고, 필요 시 수동 동기화를 수행합니다.

이 문서는 데이터베이스 복제 설정 및 관리에 대한 기본적인 이해를 돕기 위한 것입니다. 각 데이터베이스 시스템의 특성에 맞는 세부 설정은 공식 문서를 참조하세요.
