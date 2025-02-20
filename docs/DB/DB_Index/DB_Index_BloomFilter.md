# 데이터베이스 조회에서의 블룸 필터

## 소개
블룸 필터는 공간 효율적인 확률적 데이터 구조로, 특정 요소가 집합에 존재하는지를 판단하는 데 사용됩니다. 블룸 필터는 오탐(false positive)을 반환할 수 있지만, 누락(false negative)은 발생하지 않습니다. 따라서 불필요한 데이터베이스 조회를 줄이는 데 유용합니다.

## 문제점
Express와 Node.js를 사용하여 사용자 이름이 존재하는지 확인하는 웹 서비스를 구축할 때, 단순한 방법은 데이터베이스에 직접 질의하는 것입니다. 하지만 이 방법은 다음과 같은 문제를 발생시킵니다.
- 과도한 데이터베이스 조회로 인해 성능 저하
- 불필요한 I/O 및 연산 리소스 사용
- 디스크 조회로 인한 응답 속도 저하

## 해결책: 블룸 필터 사용
블룸 필터는 데이터베이스를 조회하기 전에 메모리 내에서 빠르게 존재 여부를 확인하는 역할을 합니다.

### 동작 방식
1. **비트 배열 초기화:**
    - 블룸 필터는 고정된 크기의 비트 배열(예: 64비트)로 구성됩니다.
    - 초기에는 모든 비트가 0으로 설정됩니다.

2. **요소 추가:**
    - 사용자 이름을 추가할 때, 문자열을 해시 함수에 입력하여 특정 비트 위치를 결정합니다.
    - 결정된 비트 위치를 1로 설정합니다.
    - 충돌을 줄이기 위해 여러 개의 해시 함수를 사용할 수도 있습니다.
    - 이후 데이터베이스에도 사용자 이름을 저장합니다.

3. **요소 조회:**
    - 사용자 이름의 존재 여부를 확인할 때:
        - 문자열을 해싱하여 비트 배열의 특정 위치를 확인합니다.
        - 해당 비트가 0이면, 사용자 이름이 존재하지 않음을 보장할 수 있습니다.
        - 해당 비트가 1이면, 사용자 이름이 존재할 가능성이 있지만 확실하지 않습니다.
        - 이 경우, 실제로 데이터베이스에서 조회하여 확인합니다.


__볼륨 필터 비트 존재하지 않는 경우 -> DB 접근 X__
![Image](https://github.com/user-attachments/assets/e3a5dadb-90ba-489e-9bce-4616832c02ee)

__불륨 필터 비트 존재하는 경우 -> DB 접근 O__
![Image](https://github.com/user-attachments/assets/95297c37-5e8d-4912-bef4-430d26969692)

## 구현 개요
### 블룸 필터를 적용한 Express 서버

1. **필요한 패키지 설치:**
   ```sh
   npm install express bloom-filter
   ```
2. **블룸 필터 인스턴스 생성:**
   ```javascript
   const BloomFilter = require('bloom-filter');
   const express = require('express');
   const app = express();

   const filter = new BloomFilter({ size: 64, hashes: 3 });
   const database = new Set(); // 간단한 데이터베이스 시뮬레이션
   ```

3. **사용자 추가:**
   ```javascript
   app.post('/add', (req, res) => {
       const username = req.body.username;
       filter.add(username);
       database.add(username);
       res.send(`${username} 추가됨`);
   });
   ```

4. **사용자 존재 여부 확인:**
   ```javascript
   app.get('/exists/:username', (req, res) => {
       const username = req.params.username;
       if (!filter.test(username)) {
           return res.send(`${username} 존재하지 않음`);
       }
       const existsInDB = database.has(username);
       res.send(existsInDB ? `${username} 존재함` : `${username} 존재하지 않음`);
   });
   ```

## 블룸 필터의 장점
- **데이터베이스 조회 감소**: 존재하지 않는 사용자에 대한 불필요한 질의를 줄임
- **성능 최적화**: 높은 트래픽에서도 빠른 응답 가능
- **메모리 효율성**: 모든 항목을 RAM에 저장하는 것보다 공간 절약 가능

## 한계점
- **오탐 가능성**: 잘못된 긍정 결과(false positive) 발생 가능
- **삭제 불가**: 기본 블룸 필터는 요소 삭제를 지원하지 않음. 요소가 모두 1로 꽉 찰경우, 있는 것이 의미가 없음
- **메모리 크기 조정 필요**: 비트 배열 크기를 조정해야 정확도와 메모리 사용량 균형 조절 가능


## 결론
블룸 필터는 데이터베이스 조회 최적화를 위한 강력한 도구입니다. 특히, 대규모 데이터 세트와 높은 조회 빈도를 가진 애플리케이션에서 불필요한 데이터베이스 접근을 줄이고 성능을 향상시키는 데 유용합니다. 완벽한 솔루션은 아니지만, 적절히 사용하면 데이터베이스 부하를 효과적으로 줄일 수 있습니다.

