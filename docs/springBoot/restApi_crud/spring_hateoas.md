# Spring HATEOAS

## 목차
- [개요](#개요)
- [특징](#특징)
- [의존성 추가](#의존성-추가)
- [주요 컴포넌트](#주요-컴포넌트)
  - [RepresentationModel](#representationmodel)
  - [EntityModel](#entitymodel)
  - [CollectionModel](#collectionmodel)
- [페이징과 정렬 기능](#페이징과-정렬-기능)
    - [PagedModel 사용](#pagedmodel-사용)
    - [페이징 요청 예시](#페이징-요청-예시)
    - [응답 예시](#응답-예시)
- [경로 변경 설정](#경로-변경-설정)
    - [기본 경로 설정](#기본-경로-설정)
    - [개별 리소스 경로 설정](#개별-리소스-경로-설정)
    - [관계 경로 설정](#관계-경로-설정)
- [정렬 설정](#정렬-설정)
    - [기본 정렬 설정](#기본-정렬-설정)
    - [다중 정렬 설정](#다중-정렬-설정)
    - [정렬 가능 필드 제한](#정렬-가능-필드-제한)
- [페이지 크기 설정](#페이지-크기-설정)
    - [전역 페이지 크기 설정](#전역-페이지-크기-설정)
    - [컨트롤러 레벨 페이지 크기 설정](#컨트롤러-레벨-페이지-크기-설정)
- [장점](#장점)
- [모범 사례](#모범-사례)
- [주의사항](#주의사항)
- [참고 자료](#참고-자료)


## 개요
Spring HATEOAS(Hypermedia As The Engine Of Application State)는 Spring Framework에서 REST API를 개발할 때 하이퍼미디어를 쉽게 생성할 수 있도록 도와주는 라이브러리입니다. HATEOAS를 통해 클라이언트는 서버로부터 동적으로 다음 작업에 필요한 URI 정보를 받을 수 있습니다.

## 특징
- RESTful API의 성숙도 모델 중 Level 3 단계 구현 지원
- 하이퍼미디어 기반의 동적 API 제공
- 링크 생성 및 리소스 표현의 간편화
- Spring MVC와의 완벹한 통합

## 의존성 추가

### Maven
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

### Gradle
```groovy
implementation 'org.springframework.boot:spring-boot-starter-hateoas'
```

## 주요 컴포넌트

### RepresentationModel
- HATEOAS를 지원하는 리소스의 기본 클래스
- 링크 정보를 포함하는 컨테이너 역할

```java
public class UserModel extends RepresentationModel<UserModel> {
    private String name;
    private String email;
    
    // 생성자, getter, setter
}
```

### EntityModel
- 단일 리소스를 감싸는 모델
- 개별 리소스에 대한 링크 정보 추가 가능

```java
@GetMapping("/users/{id}")
public EntityModel<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    
    return EntityModel.of(user,
        linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
        linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")
    );
}
```

### CollectionModel
- 리소스 컬렉션을 감싸는 모델
- 목록에 대한 링크 정보 추가 가능

```java
@GetMapping("/users")
public CollectionModel<EntityModel<User>> getAllUsers() {
    List<User> users = userService.findAll();
    
    List<EntityModel<User>> userModels = users.stream()
        .map(user -> EntityModel.of(user,
            linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel())
        )
        .collect(Collectors.toList());
    
    return CollectionModel.of(userModels,
        linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
}
```

## 페이징과 정렬 기능

### PagedModel 사용
- 페이징된 결과를 표현하는 특별한 모델
- 페이지 메타데이터 포함

```java
@GetMapping("/users")
public PagedModel<EntityModel<User>> getUsers(
        @PageableDefault(size = 20) Pageable pageable) {
    Page<User> users = userService.findAll(pageable);
    
    List<EntityModel<User>> userModels = users.stream()
        .map(user -> EntityModel.of(user,
            linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel())
        )
        .collect(Collectors.toList());
    
    PagedModel<EntityModel<User>> pagedModel = PagedModel.of(
        userModels,
        new PagedModel.PageMetadata(
            users.getSize(),
            users.getNumber(),
            users.getTotalElements(),
            users.getTotalPages()
        )
    );
    
    return pagedModel;
}
```

### 페이징 요청 예시
```
GET /users?page=0&size=20&sort=name,desc
GET /users?page=1&size=10&sort=email,asc&sort=name,desc
```

### 응답 예시
```json
{
    "_embedded": {
        "users": [
            {
                "name": "John Doe",
                "email": "john@example.com",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/users/1"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/users?page=0&size=20"
        },
        "next": {
            "href": "http://localhost:8080/users?page=1&size=20"
        },
        "last": {
            "href": "http://localhost:8080/users?page=5&size=20"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 100,
        "totalPages": 5,
        "number": 0
    }
}
```

## 경로 변경 설정

### 기본 경로 설정
application.properties 또는 application.yml에서 기본 경로 설정:

```yaml
spring:
  data:
    rest:
      base-path: /api/v1
```

### 개별 리소스 경로 설정
```java
@RepositoryRestResource(path = "members")  // /users 대신 /members로 경로 변경
public interface UserRepository extends JpaRepository<User, Long> {
}
```

### 관계 경로 설정
```java
@Entity
public class User {
    @ManyToMany
    @RestResource(path = "user-groups")  // /groups 대신 /user-groups로 경로 변경
    private Set<Group> groups;
}
```

## 정렬 설정

### 기본 정렬 설정
```java
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    // 기본 정렬 설정
    @Override
    @SortDefault(sort = "name", direction = Sort.Direction.ASC)
    Page<User> findAll(Pageable pageable);
}
```

### 다중 정렬 설정
```java
@GetMapping("/users")
public PagedModel<EntityModel<User>> getUsers(
        @SortDefault.SortDefaults({
            @SortDefault(sort = "lastName", direction = Sort.Direction.DESC),
            @SortDefault(sort = "firstName", direction = Sort.Direction.ASC)
        }) Pageable pageable) {
    // ... 구현 내용
}
```

### 정렬 가능 필드 제한
```java
@Entity
public class User {
    @Id
    private Long id;
    
    @QuerySortDefault.SortDefaults({  // 정렬 가능한 필드 지정
        @QuerySortDefault(sort = "name", direction = Sort.Direction.ASC)
    })
    private String name;
    
    @JsonIgnore  // 정렬 불가능한 필드
    private String password;
}
```

## 페이지 크기 설정

### 전역 페이지 크기 설정
application.properties 또는 application.yml:

```yaml
spring:
  data:
    web:
      pageable:
        default-page-size: 20
        max-page-size: 100
```

### 컨트롤러 레벨 페이지 크기 설정
```java
@GetMapping("/users")
public PagedModel<EntityModel<User>> getUsers(
        @PageableDefault(size = 15, page = 0) Pageable pageable) {
    // ... 구현 내용
}
```

## 장점
1. 클라이언트와 서버의 느슨한 결합
2. API 변경에 대한 유연한 대응
3. API 탐색 가능성 향상
4. 자동화된 API 문서화 지원
5. 페이징과 정렬의 유연한 처리

## 모범 사례
1. 일관된 링크 관계 사용
2. 의미 있는 링크 관계 이름 지정
3. HAL 스펙 준수
4. 적절한 HTTP 메서드 사용
5. 페이지 크기 제한 설정
6. 정렬 가능한 필드 명시적 정의

## 주의사항
- 불필요한 링크 정보 추가 지양
- 성능 고려 (링크 생성에 따른 오버헤드)
- 버전 관리 전략 수립
- 보안 고려사항 검토
- 과도한 페이지 크기 요청 제한
- 정렬 필드 검증

## 참고 자료
- [Spring HATEOAS 공식 문서](https://docs.spring.io/spring-hateoas/docs/current/reference/html/)
- [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html)
- [Understanding HATEOAS](https://spring.io/understanding/HATEOAS)
- [Spring Data REST 레퍼런스](https://docs.spring.io/spring-data/rest/docs/current/reference/html/)