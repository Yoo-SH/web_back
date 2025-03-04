# DB Replication 실전 가이드

## 목차
- [소개](#소개)
- [DB Replication의 개념](#db-replication의-개념)
- [Replication 종류](#replication-종류)
- [MySQL Master-Slave 복제 구성](#mysql-master-slave-복제-구성)
- [PostgreSQL Streaming Replication 구성](#postgresql-streaming-replication-구성)
- [MongoDB Replica Set 구성](#mongodb-replica-set-구성)
- [Redis Master-Replica 구성](#redis-master-replica-구성)
- [Elasticsearch 클러스터 및 복제 구성](#elasticsearch-클러스터-및-복제-구성)
- [실전 DB 복제 모범 사례](#실전-db-복제-모범-사례)
- [참고 자료](#참고-자료)

## 소개
데이터베이스 복제(DB Replication)는 데이터베이스의 가용성, 성능, 안정성을 높이기 위해 데이터를 여러 서버에 분산시키는 기술입니다. 이 문서는 DB Replication의 개념부터 실전 구성 방법, 모니터링 및 장애 대응까지 전반적인 내용을 다룹니다.

## DB Replication의 개념
데이터베이스 복제는 하나의 데이터베이스(Master/Primary)에서 다른 데이터베이스(Slave/Secondary)로 데이터를 복사하는 과정입니다. 이를 통해 다음과 같은 이점을 얻을 수 있습니다:

- **고가용성(High Availability)**: 주 서버에 장애가 발생해도 복제 서버로 전환하여 서비스 지속 가능
- **부하 분산(Load Balancing)**: 읽기 작업을 여러 서버로 분산하여 성능 향상
- **데이터 백업**: 실시간으로 데이터가 복제되어 백업 역할 수행
- **지리적 분산**: 서로 다른 지역에 데이터를 복제하여 지연시간 감소

## Replication 종류
1. **동기식(Synchronous) vs 비동기식(Asynchronous) 복제**

   - **동기식 복제**: Master DB의 트랜잭션이 Slave DB에도 적용되었을 때 완료로 간주
     - **장점**: 데이터 일관성 보장
     - **단점**: 성능 저하 가능성

   - **비동기식 복제**: Master DB의 변경사항이 Slave DB에 즉시 전송되지 않고 지연될 수 있음
     - **장점**: 높은 성능
     - **단점**: 데이터 손실 가능성

2. **주요 복제 아키텍처**

   - **Master-Slave**: 하나의 Master와 여러 Slave 구성
   - **Master-Master**: 여러 서버가 모두 Master 역할 수행
   - **Circular Replication**: 여러 서버가 원형으로 복제
   - **Multi-Source Replication**: 여러 Master에서 하나의 Slave로 복제

## MySQL Master-Slave 복제 구성

### 사전 요구사항
- MySQL 5.7 이상 설치된 두 개 이상의 서버
- 서버 간 네트워크 통신 가능
- 관리자 권한

### 1. Master 서버 구성

#### 1.1 MySQL 설정 파일(my.cnf) 수정
```ini
[mysqld]
# 서버 식별자 (유니크한 값)
server-id = 1

# 바이너리 로그 활성화
log_bin = mysql-bin
binlog_format = ROW

# 데이터베이스 지정 (선택사항)
binlog_do_db = example_db

# 복제에서 제외할 데이터베이스 (선택사항)
binlog_ignore_db = information_schema
binlog_ignore_db = performance_schema

# 바이너리 로그 만료 시간 (일)
expire_logs_days = 7
```

#### 1.2 MySQL 재시작
```bash
sudo systemctl restart mysql
```

#### 1.3 복제용 사용자 생성
```sql
CREATE USER 'repl_user'@'%' IDENTIFIED BY 'password';
GRANT REPLICATION SLAVE ON *.* TO 'repl_user'@'%';
FLUSH PRIVILEGES;
```

#### 1.4 현재 바이너리 로그 위치 확인
```sql
SHOW MASTER STATUS;
```
결과 예시:
```
+------------------+----------+--------------+------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB |
+------------------+----------+--------------+------------------+
| mysql-bin.000001 | 120      | example_db   | information_schema,performance_schema |
+------------------+----------+--------------+------------------+
```
이 정보(File과 Position)를 기록해 둡니다.

### 2. Slave 서버 구성

#### 2.1 MySQL 설정 파일(my.cnf) 수정
```ini
[mysqld]
# 서버 식별자 (Master와 다른 값)
server-id = 2

# 읽기 전용 (선택사항)
read_only = 1

# 릴레이 로그 설정
relay_log = mysql-relay-bin
relay_log_purge = 1

# 바이너리 로그 활성화 (Slave를 Master로 변경 가능성 고려)
log_bin = mysql-bin
binlog_format = ROW
```

#### 2.2 MySQL 재시작
```bash
sudo systemctl restart mysql
```

#### 2.3 복제 설정
Master에서 확인한 File과 Position 값을 사용합니다.
```sql
STOP SLAVE;

CHANGE MASTER TO
  MASTER_HOST='master_server_ip',
  MASTER_USER='repl_user',
  MASTER_PASSWORD='password',
  MASTER_LOG_FILE='mysql-bin.000001',
  MASTER_LOG_POS=120;

START SLAVE;
```

#### 2.4 복제 상태 확인
```sql
SHOW SLAVE STATUS\G
```

복제가 정상적으로 작동하는지 확인:
```
Slave_IO_Running: Yes
Slave_SQL_Running: Yes
```
둘 다 "Yes"면 정상적으로 복제가 설정된 것입니다.

## PostgreSQL Streaming Replication 구성

### 사전 요구사항
- PostgreSQL 10 이상 설치된 두 개 이상의 서버
- 서버 간 네트워크 통신 가능
- 관리자 권한

### 1. Master(Primary) 서버 구성

#### 1.1 PostgreSQL 설정 파일(postgresql.conf) 수정
```ini
# 복제 설정
wal_level = replica
max_wal_senders = 10
wal_keep_segments = 64
max_replication_slots = 10

# 접속 허용 설정
listen_addresses = '*'
```

#### 1.2 접속 제어 설정(pg_hba.conf)
```
# 복제 연결 허용
host    replication     repl_user    slave_server_ip/32    md5
```

#### 1.3 복제용 사용자 생성
```sql
CREATE ROLE repl_user WITH REPLICATION LOGIN PASSWORD 'password';
```

#### 1.4 PostgreSQL 재시작
```bash
sudo systemctl restart postgresql
```

#### 1.5 복제 슬롯 생성 (선택사항이지만 권장)
```sql
SELECT pg_create_physical_replication_slot('replica_1');
```

### 2. Slave(Standby) 서버 구성

#### 2.1 PostgreSQL 서비스 중지
```bash
sudo systemctl stop postgresql
```

#### 2.2 데이터 디렉토리 백업 (기존 데이터가 있는 경우)
```bash
sudo mv /var/lib/postgresql/12/main /var/lib/postgresql/12/main_old
```

#### 2.3 베이스 백업 생성
Master 서버에서 실행:
```bash
pg_basebackup -h master_server_ip -U repl_user -D /var/lib/postgresql/12/main -P -Xs -R
```

#### 2.4 복구 설정 파일 생성 (postgresql.auto.conf에 자동 생성됨)
파일 내용 확인 (아래와 유사해야 함):
```
primary_conninfo = 'host=master_server_ip port=5432 user=repl_user password=password'
primary_slot_name = 'replica_1'
```

#### 2.5 Standby 신호 파일 생성
```bash
touch /var/lib/postgresql/12/main/standby.signal
```

#### 2.6 소유권 설정
```bash
chown -R postgres:postgres /var/lib/postgresql/12/main
```

#### 2.7 PostgreSQL 시작
```bash
sudo systemctl start postgresql
```

#### 2.8 복제 상태 확인
Master에서:
```sql
SELECT * FROM pg_stat_replication;
```

Slave에서:
```sql
SELECT pg_is_in_recovery();
```
결과가 `t`(true)면 정상 작동 중입니다.

## MongoDB Replica Set 구성

### 사전 요구사항
- MongoDB 4.0 이상 설치된 세 개 이상의 서버 (최소 3개 권장)
- 서버 간 네트워크 통신 가능
- 관리자 권한

### 1. 각 서버의 MongoDB 설정

#### 1.1 설정 파일(mongod.conf) 수정 (각 서버마다)
```yaml
# 네트워크 설정
net:
  port: 27017
  bindIp: 0.0.0.0

# 복제 설정
replication:
  replSetName: "rs0"

# 보안 설정 (선택사항)
security:
  authorization: enabled
```

#### 1.2 MongoDB 재시작 (각 서버마다)
```bash
sudo systemctl restart mongod
```

### 2. Replica Set 초기화

#### 2.1 기본 노드에 연결
```bash
mongo --host primary_server_ip
```

#### 2.2 Replica Set 구성 초기화
```javascript
rs.initiate({
  _id: "rs0",
  members: [
    { _id: 0, host: "server1_ip:27017", priority: 2 },
    { _id: 1, host: "server2_ip:27017", priority: 1 },
    { _id: 2, host: "server3_ip:27017", priority: 1 }
  ]
})
```

#### 2.3 Replica Set 상태 확인
```javascript
rs.status()
```

### 3. 관리자 계정 생성 (선택사항)
```javascript
use admin
db.createUser({
  user: "admin",
  pwd: "password",
  roles: [ { role: "root", db: "admin" } ]
})
```

## Redis Master-Replica 구성

### 사전 요구사항
- Redis 5.0 이상 설치된 두 개 이상의 서버
- 서버 간 네트워크 통신 가능
- 관리자 권한

### 1. Master 서버 구성

#### 1.1 Redis 설정 파일(redis.conf) 수정
```
# 바인딩 설정
bind 0.0.0.0

# 보안 설정 (권장)
requirepass "master_password"

# 복제 인증 (권장)
masterauth "master_password"
```

#### 1.2 Redis 재시작
```bash
sudo systemctl restart redis
```

### 2. Replica 서버 구성

#### 2.1 Redis 설정 파일(redis.conf) 수정
```
# 바인딩 설정
bind 0.0.0.0

# Master 서버 정보
replicaof master_server_ip 6379

# 보안 설정 (권장)
requirepass "replica_password"

# Master 인증 정보
masterauth "master_password"
```

#### 2.2 Redis 재시작
```bash
sudo systemctl restart redis
```

### 3. 복제 상태 확인
Master 서버에서:
```bash
redis-cli -a "master_password" INFO replication
```

## Elasticsearch 클러스터 및 복제 구성

### 사전 요구사항
- Elasticsearch 7.x 이상 설치된 세 개 이상의 서버
- 서버 간 네트워크 통신 가능
- 관리자 권한

### 1. 각 노드 설정

#### 1.1 elasticsearch.yml 설정 (노드 1 - Master)
```yaml
# 클러스터 설정
cluster.name: my-cluster
node.name: node-1

# 역할 설정
node.master: true
node.data: true

# 네트워크 설정
network.host: 0.0.0.0
http.port: 9200

# 디스커버리 설정
discovery.seed_hosts: ["node1_ip", "node2_ip", "node3_ip"]
cluster.initial_master_nodes: ["node-1"]
```

#### 1.2 elasticsearch.yml 설정 (노드 2, 3 - Data)
```yaml
# 클러스터 설정
cluster.name: my-cluster
node.name: node-2  # 각 노드마다 다르게 설정

# 역할 설정
node.master: false
node.data: true

# 네트워크 설정
network.host: 0.0.0.0
http.port: 9200

# 디스커버리 설정
discovery.seed_hosts: ["node1_ip", "node2_ip", "node3_ip"]
```

#### 1.3 Elasticsearch 재시작 (각 노드)
```bash
sudo systemctl restart elasticsearch
```

### 2. 인덱스 복제 설정
```
PUT /my_index
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  }
}
```

### 3. 클러스터 상태 확인
```
GET /_cluster/health
```

## 실전 DB 복제 모범 사례

### 성능 최적화
1. **네트워크 대역폭 고려**: 복제는 네트워크 리소스를 소비하므로 충분한 대역폭 확보
2. **디스크 I/O 최적화**: SSD 사용, RAID 구성 고려
3. **버퍼 및 캐시 설정**: 각 DB에 맞게 버퍼 풀, 캐시 크기 최적화

### 보안 강화
1. **전용 복제 계정 사용**: 최소 권한 원칙 적용
2. **네트워크 암호화**: SSL/TLS 사용
3. **방화벽 설정**: 복제 포트만 허용

### 모니터링 설정
1. **복제 지연(Lag) 모니터링**: 임계값 설정 및 알림 구성
2. **디스크 공간 모니터링**: 로그 파일로 인한 디스크 공간 부족 방지
3. **연결 상태 모니터링**: 복제 연결 끊김 즉시 감지

### 장애 대응 계획
1. **자동 장애 조치(Failover) 구성**: 감시 도구를 이용한 자동 전환
2. **복구 절차 문서화**: 문제 발생 시 단계별 대응 방법 준비
3. **정기적인 장애 훈련**: 실제 장애 상황을 가정한 모의 훈련 실시

## 모니터링 및 유지보수

효과적인 DB Replication 관리를 위해서는 지속적인 모니터링이 필수적입니다:

### 모니터링 항목

- **복제 지연(Replication Lag)**
- **복제 오류**
- **디스크 공간**
- **네트워크 대역폭 사용량**
- **서버 부하**

### 모니터링 도구

- **Prometheus + Grafana**
- **Nagios/Zabbix**
- **데이터베이스 자체 모니터링 도구**

### 정기적인 유지보수 작업

- **로그 파일 정리**
- **인덱스 최적화**
- **백업 검증**
- **복제 구성 테스트**

## 장애 대응 시나리오

### 시나리오 1: Master 서버 장애

- **장애 감지**: 모니터링 시스템을 통한 즉각적인 알림
- **Slave 서버를 Master로 승격**
- **애플리케이션 연결 정보 변경**
- **기존 Master 서버 복구 후 새 구성에 Slave로 추가**

### 시나리오 2: 복제 지연 증가

- **지연 원인 파악**: 네트워크 문제, 디스크 I/O 이슈, 부하 증가 등
- **단기 대응**: 읽기 쿼리 일부를 Master로 전환
- **장기 대응**: 하드웨어 업그레이드, 쿼리 최적화, 인덱스 추가

### 시나리오 3: 데이터 불일치

- **불일치 데이터 범위 확인**
- **복제 중지 후 데이터 동기화 진행**
- **근본 원인 파악 및 재발 방지 대책 수립**

## 성능 최적화 팁

### 네트워크 최적화

- **전용 네트워크 인터페이스 사용**
- **압축 설정 활성화**

### 하드웨어 최적화

- **SSD 사용**
- **충분한 메모리 할당**

### 데이터베이스 설정 최적화

- **트랜잭션 처리 방식 조정**
- **로그 버퍼 크기 최적화**
- **복제 병렬화 설정**

### 애플리케이션 수준 최적화

- **읽기/쓰기 분리**
- **읽기 부하 분산 알고리즘 개선**

## 참고 자료

- **MySQL Replication 공식 문서**
- **PostgreSQL Replication 공식 문서**
- **MongoDB Replica Sets 공식 문서**
- **Redis Replication 공식 문서**
- **Elasticsearch 클러스터 공식 문서**

데이터베이스 복제는 데이터 가용성과 성능을 높이는 중요한 기술입니다. 이 가이드에서 제공하는 정보와 예제를 참고하여 실제 환경에 맞는 최적의 복제 구성을 구축하시기 바랍니다.