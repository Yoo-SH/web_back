## docker

### docker container 구성하는 방법

```
images = mini linux version <- code sanpshot + envirentment(package)

```

### docker image 공유

```
1. docker file로 공유 vs Built image로공유
(전자는 상대가 관리하지만 이미지 공유 어려울 수 있음
, 후자는 도커허브 사용를 사용해야함.)

2. docker hub에 이미지 푸쉬 vs 개인 레파지토리에 푸쉬

3.

```

### docker volumnes

```
docker volumnes list 보기

- docker volumne ls

익명 volumnes vs 명명된 volumnes

- 공통점

- 익명 volumns는 새로운 컨터에니거 생성되면 또 다시 새로운 익명 볼륨이 생성되어 데이터가 사라짐

- 명명된 volimes 컨테이너들의 삭제되는 컨테이너들의 이전 데이터를 게속 가지고 있음. 컨테이너를 삭제해도 volumes는 삭제되지 않음. 그러나 편집을 할 수 없음. 왜냐하면 볼륨이 호스트 머신 어디에 저장되어있는지 알 수 없기 때문.

명명된 volumnes 생성하는 방법

- docker run -v volumes_name:path image
cf) volumes_name에 로컬 경로가 붙으면 바인드 마운트가 생성됨.,
```

### docker Bind Mounts

```
Volumes와 차이
- volume은 docker에 의해 관리되며, BiND Mount는 사용자에 의해 관리됨
- volume은 도커에 의해 관리되는 볼륨의 위치, 즉 호스트 머신의 파일 시스템 상의 볼륨이 어디에 있는지 알지 사용자가 알 수 없지만, 반면에 바인드 마운트는 그 위치를 알 수 있음.

-로컬의 스냅샷 뿐만 아니라, 이미지 또한 생성되어 들어감.
- 영구적이고 편집 가능한 데이터에 적합함.
- 바인트마운트 데이터를 지우려면 로컬파일을 지워야함. 도커 명령으로 지울 수 없음.


생성방법
- docker run -v 로컬절대경로:이미지파일워킹디렉토리 image

read-write 제한(read only)
-docker run -v 로컬절대경로:이미지파일워킹디렉토리:ro image


주의사항
- docker가 관리하지 않으므로, 로컬 파일이 워킹디렉토리를 덮어버리면 워킹 디렉토리의 install한 것들이 전부 사라질 수 있다.
-> 대안: 익명 볼륨을 추가하여, 충돌이 날 경우 충돌난 부분만 익명 볼륨에서 데이터를 가져온다. 정확히는 덮어버려 필요한 package를 install 못하니 익명 볼륨에서 가져와서 install한다는 것. (docker가 알아서 하니 익명 볼륨을 추가만 하면 된다. 즉 외부 경로보다 컨테이너 내부 경로의 우선순위를 높이는데 사용.)
```

### docker basic command

```
- docker ps (실행 컨터이너 확인)

- docker ps -a (모든 컨테이너 확인)

- docker images (가지고 있는 images 모두 출력 )

- docker logs container (컨테이너에 의해 출력된 과거의 콘솔로그들을 볼 수 있음. backgound에서 움직이는 콘솔로그는 보이지 않을 때 사용)

- docker logs -f container (follow옵션, 콘솔로그 계속 수신 대기)

- docker cp 로컬경로+복사할 파일or폴더 container1:+경로+폴더
(로컬 호스트 머신과 실행 중인 컨테이너 간에 파일을 복사하는 방법, 제약 사항이 많아서 주로 컨테이너 내부의 로그를 로컬에서 볼 떄 사용)
(ex, docker cp dummy/. copycontainer:/test => 로컬호스트 dummy 폴더의 내용이 컨테이너 내부의 test 폴더에 복사됨)

```

### docker image command

```

- docker build . (도커 코드 + 환경변수 생성 in image)

- docker build -t name:tag . (image에 이름과 태그 지정, 태그는 주로 버전)

- docker rmi image1 image2 ... (images 삭제, iamge의 인스턴스 컨테이너가 존재하면 해당 image 삭제 불가능)

- docker image prune (사용되지 않는 이미지의 컨테이너 모두 제거, prune뜻은 가지치기)

- docker image prune -a (태그된 이미지를 포함하여 모든 이미지 제거)

- docker image insepect image_id (image_id에 대한 정보 출력)

```

### docker container command

```

- docker run image (run은 container을 creat + start(foreground))

- docker run -d image(-d는 detache 옵션, start를 backgound )

- docker run --name container_name image (contain 객체 이름 지정)

- docker run -p port1:port2 images(-p는 publish, 외부포트:내부포트, 외부포트에 내부포트를 publish함. 도커 container와 외부와 포트간 연결)

- docker run -p port1:port2 -d --rm images (background에서 실행하고, 컨테이너를 stop하면 자동으로 삭제하게함. 일회용. 보통은 코드가 images에서 수시로 바뀌니 이런식으로 하는 것이 타당함.)

- docker run -i -t images (-i는 인터렉티브 모드, -t는 가상 터미널 모드, 사용자의 값을 입력받아야할 때 사용)

- docker start -a -i container (-a로 foregound 상태로 만들고, -i는 인터렉티브 모드로 컨테이너에 값을 입력함. )

- docker start container (start는 background 즉 디폴트가 detache모드임., foregorund로 하려면 옵션 -a)

- docker stop container (실행중인 container 중지)

- docker attach container (실행 중인 컨터에너에 붙기, foreground로)

- docker rm container1 container2 ... (container 삭제, 동작중인 컨테이너는 삭제 불가능.)

```

### 환경변수

```

```

### docker 통신

```
case 1: www 외부통신
- 기본적으로 컨테이너는 월드 와이드 웹에 요청을 보낼 수 있음
- 특별한 설정없이 바로 가능.

case 2: local host와 통신
- db같은 것들은 컨테이너와 분리하여 사용하는게 모범(db 컨테이너 만들거나 호스트에 남겨두고 docker container가 db경로 인지하도록)
- path를 docker가 인지하도록 변경
- 도커 컨테이너 내부에서 알 수 있는 호스트머신의 IP주소로 변환
mongodb example) -localhost:27017 -> host.docker.internal:27017

case 3: container간 통신
-ex) 도커 컨테이너에 db를 넣는 경우
1. docker run -d --name mongodb mongo (꼭 -d 모드로 설정하도록 개발됨)
2. docker container inspect container(mongodb) - > IPAddress를 찾기 -> locahost를 IPaddress로 변경.

결국 하드코딩이라 불편 => 여러 container를 network로 하나로 묶기
- docker run --network  cotainer1 container2 -> db를 가지고 있는 컨테이너의 이름으로 주소를 변경.(컨테이너가 동일한 네트워크인 경우, 컨테이너 이름을 주소로 사용가능하기 때문.) ex) localhost->mongo_container -> db를 가지지 않은 컨테이너와 mongo_container를 하나의 네트워크로 묶기

참고로 동일한 도커 네트워크에 연결하는 것이면 포트를 지정하지 않아도 됨. 컨테이너 내부 네트워크에서는 모든 컨테이너가 서로 자유롭게 통신 가능하기 떄문.
```

### docker Three Building Blocks

```
1 Database
- 데이터가 persist 해야함.
- 데이터에 제한적으로 접근 되어야함
2.Backend
- 데이터가 persist 해야함
- 데이터가 지속적으로 업데이트 되어야함
3.Froented
-데이터가 지속적으로 업데이트 되어야함.

da(network로 back과 연결) <=>(network로 db와 연결) back(port를 local과 연결하여 front와 연결) <=> front (브라우저에서 동작 port를 local과 연결)
```
