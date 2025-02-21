# JPA 단방향/양방향 관계 완벽 가이드

## 목차
- [개요](#개요)
- [단방향 관계](#단방향-관계)
- [양방향 관계](#양방향-관계)
- [관계 선택 가이드](#관계-선택-가이드)
- [캐스케이딩 전략](#캐스케이딩-전략)
- [베스트 프랙티스](#베스트-프랙티스)

## 개요
JPA에서 엔티티 간의 관계는 크게 단방향(Unidirectional)과 양방향(Bidirectional) 관계로 나눌 수 있습니다. 각각의 특징과 사용법을 자세히 알아보겠습니다.

## 단방향 관계

### 특징
- 한쪽 엔티티만 다른쪽 엔티티를 참조
- 더 단순한 구조
- 관리가 용이
- 필요한 경우에만 참조를 추가

### 예시 코드
```java
@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;
    private String title;
}

@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne
    private Post post;  // 단방향 관계
}
```

## 양방향 관계

### 특징
- 양쪽 엔티티가 서로를 참조
- 복잡한 구조이지만 더 유연한 탐색 가능
- 관계의 주인(Owner) 설정 필요
- 양쪽 동기화 필요

### 예시 코드
```java
@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}

@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne
    private Post post;
    
    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);  // 양방향 관계 설정
    }
}
```

## 관계 선택 가이드

### 단방향을 선택해야 하는 경우
1. **단순한 참조만 필요할 때**
    - 댓글에서 게시글을 참조하지만, 게시글에서 댓글 목록이 필요 없는 경우
    - 주문에서 회원 정보만 필요하고, 회원의 주문 내역은 별도로 조회하는 경우

2. **성능 최적화가 중요한 경우**
    - 불필요한 객체 그래프 탐색을 방지하고 싶을 때
    - 메모리 사용량을 최소화하고 싶을 때

3. **참조가 단방향으로 충분한 경우**
    - 단순한 통계 데이터 수집
    - 일회성 참조 관계

### 양방향을 선택해야 하는 경우
1. **양쪽에서 탐색이 필요한 경우**
    - 게시글에서 댓글 목록을 자주 조회해야 하는 경우
    - 회원의 주문 내역을 자주 조회해야 하는 경우

2. **복잡한 비즈니스 로직 처리가 필요한 경우**
    - 양쪽 엔티티 간의 상호작용이 빈번한 경우
    - 캐스케이드 작업이 필요한 경우

3. **데이터 정합성이 중요한 경우**
    - 부모-자식 관계에서 양쪽의 데이터가 항상 동기화되어야 하는 경우

## 캐스케이딩 전략

### 단방향 관계의 캐스케이딩

1. **`@OneToMany` 단방향 + `CascadeType.ALL`**
```java
@Entity
public class Order {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
}
```
- 적합한 상황:
    - 부모 엔티티가 자식의 전체 라이프사이클을 관리해야 할 때
    - 주문-주문상품과 같이 강한 종속관계
    - 자식 엔티티가 다른 엔티티와 공유되지 않을 때

2. **`@ManyToOne` 단방향 + `CascadeType.PERSIST`**
```java
@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Post post;
}
```
- 적합한 상황:
    - 자식 엔티티 저장 시 부모도 함께 저장하고 싶을 때
    - 단, 삭제는 별도로 관리하고 싶을 때

### 양방향 관계의 캐스케이딩

1. **`@OneToMany` + `@ManyToOne` 양방향 + `CascadeType.ALL` + `orphanRemoval = true`**
```java
@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}

@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
```
- 적합한 상황:
    - 게시글-댓글처럼 부모 엔티티가 자식의 생명주기를 완전히 관리할 때
    - 자식 엔티티가 부모 없이 존재할 수 없을 때
    - 부모 삭제 시 자식도 모두 삭제해야 할 때

2. **`@OneToMany` + `@ManyToOne` 양방향 + 부분 캐스케이딩**
```java
@Entity
public class Team {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Member> members = new ArrayList<>();
}
```
- 적합한 상황:
    - 저장과 수정만 전파하고 싶을 때
    - 삭제는 별도로 관리하고 싶을 때
    - 자식 엔티티가 다른 엔티티와 연관될 수 있을 때

### 주의사항과 팁
1. 순환참조 주의
```java
// 양방향 관계에서 순환참조 방지
@JsonManagedReference  // 부모 쪽
@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
private List<Comment> comments;

@JsonBackReference     // 자식 쪽
@ManyToOne(fetch = FetchType.LAZY)
private Post post;
```

2. 캐스케이드 전파 범위 제한
```java
// 필요한 작업만 선택적으로 전파
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
private List<OrderItem> orderItems;
```

## 베스트 프랙티스

### 단방향 관계
1. 최초 설계는 단방향으로 시작
2. 필요한 경우에만 양방향으로 확장
3. `@ManyToOne`은 즉시 로딩(EAGER)이 기본값이므로, 성능 최적화를 위해 지연 로딩(LAZY)으로 설정

```java
@ManyToOne(fetch = FetchType.LAZY)
private Post post;
```

### 양방향 관계
1. 관계의 주인은 외래 키가 있는 쪽으로 설정
2. 양방향 관계 설정 시 편의 메서드 작성
3. `toString()`, `JSON 직렬화` 시 무한 루프 주의

```java
public void addComment(Comment comment) {
    comments.add(comment);
    comment.setPost(this);
}
```

### 성능 고려사항
1. N+1 문제 방지를 위한 fetch join 사용
2. 양방향 관계에서 불필요한 컬렉션 초기화 주의
3. 지연 로딩 활용으로 성능 최적화





