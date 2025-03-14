# 데이터베이스 인덱싱 장단점 가이드

## 목차
- [키 인덱싱 (Key Indexing)](#키-인덱싱-key-indexing)
    - [장점](#키-인덱싱-장점)
    - [단점](#키-인덱싱-단점)
- [논키 인덱싱 (Non-Key Indexing)](#논키-인덱싱-non-key-indexing)
    - [장점](#논키-인덱싱-장점)
    - [단점](#논키-인덱싱-단점)
- [실제 사용 시 주의사항](#실제-사용-시-주의사항)

## 키 인덱싱 (Key Indexing)

### 키 인덱싱 장점

1. **빠른 데이터 검색**
    - 이유: B-tree 구조로 인해 로그 시간 복잡도(O(log n))로 검색 가능
    - 이유: 물리적으로 정렬된 상태를 유지하여 디스크 I/O 최소화

2. **데이터 무결성 보장**
    - 이유: 중복 값을 허용하지 않는 구조
    - 이유: NULL 값 제한으로 데이터 일관성 유지

3. **효율적인 범위 검색**
    - 이유: 데이터가 물리적으로 정렬되어 있어 연속된 데이터 접근이 빠름
    - 이유: 디스크 시퀀셜 액세스 활용 가능

4. **조인 성능 향상**
    - 이유: 외래 키 관계에서 참조 무결성 보장
    - 이유: 인덱스를 통한 빠른 매칭으로 조인 최적화

### 키 인덱싱 단점

1. **DML 작업 성능 저하**
    - 이유: 데이터 변경 시 인덱스 재구성 필요
    - 이유: 물리적 데이터 재배치로 인한 오버헤드

2. **저장 공간 증가**
    - 이유: 인덱스 구조 유지를 위한 추가 공간 필요
    - 이유: 인덱스 페이지 관리를 위한 메모리 사용

3. **제한된 사용**
    - 이유: 테이블당 하나의 클러스터형 인덱스만 생성 가능
    - 이유: 물리적 정렬 구조의 한계

## 논키 인덱싱 (Non-Key Indexing)

### 논키 인덱싱 장점

1. **유연한 인덱스 구성**
    - 이유: 필요한 컬럼에 자유롭게 인덱스 생성 가능
    - 이유: 검색 패턴에 따른 최적화 용이

2. **복합 인덱스 지원**
    - 이유: 여러 컬럼을 조합한 인덱스 생성 가능
    - 이유: 다양한 검색 조건 최적화 가능

3. **선택적 인덱싱**
    - 이유: 데이터 특성에 따라 필요한 부분만 인덱스 적용
    - 이유: 리소스 효율적 사용 가능

### 논키 인덱싱 단점

1. **상대적으로 느린 검색 속도**
    - 이유: 논리적 포인터를 통한 추가 조회 필요
    - 이유: 데이터 페이지 랜덤 액세스 발생

2. **성능 저하 위험**
    - 이유: 과도한 인덱스 생성 시 전체 시스템 부하 증가
    - 이유: 각 인덱스마다 추가적인 유지보수 필요

3. **리소스 사용 증가**
    - 이유: 여러 인덱스 동시 관리로 인한 오버헤드
    - 이유: 데이터 변경 시 모든 관련 인덱스 업데이트 필요

## 실제 사용 시 주의사항

1. **상황에 맞는 인덱스 선택**
    - 테이블 크기와 사용 패턴 분석
    - 검색 vs 입력/수정 비율 고려

2. **인덱스 수 최적화**
    - 과도한 인덱스 생성 지양
    - 실제 사용되는 인덱스만 유지

3. **정기적인 관리**
    - 인덱스 단편화 모니터링
    - 사용되지 않는 인덱스 제거

4. **컬럼 특성 고려**
    - 갱신이 빈번한 컬럼은 인덱스 최소화
    - 선택도가 높은 컬럼 우선 인덱싱