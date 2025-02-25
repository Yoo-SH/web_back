# JPA에서 Fetch Type을 사용하지 않고 Fetch Join을 사용하는 경우

## 목차
- [개요](#개요)
- [Fetch Type의 한계](#fetch-type의-한계)
- [Fetch Join이란?](#fetch-join이란)
- [Fetch Join을 사용해야 하는 이유](#fetch-join을-사용해야-하는-이유)
- [JPA Fetch Join 사용 가이드](#jpa-fetch-join-사용-가이드)
    - [N+1 문제 해결](#n1-문제-해결)
    - [불필요한 데이터 로딩 방지](#불필요한-데이터-로딩-방지)
    - [연관 데이터 한 번에 조회 가능](#연관-데이터-한-번에-조회-가능)
- [Fetch Join 사용 시 주의할 점](#fetch-join-사용-시-주의할-점)
- [결론](#결론)

## 1. 개요
JPA에서 연관 관계를 가져올 때 `FetchType`을 지정할 수 있지만, `fetch join`을 사용하는 것이 필수적인 경우가 많습니다. `FetchType`만으로 해결할 수 없는 문제들이 존재하며, 이를 해결하기 위해 `fetch join`이 필요합니다. 본 문서에서는 그 이유를 설명합니다.

## 2. Fetch Type의 한계
JPA에서 연관 관계를 로딩하는 방식에는 두 가지 `FetchType`이 있습니다.

- **즉시 로딩 (EAGER)**: 연관된 엔터티를 즉시 조회합니다.
- **지연 로딩 (LAZY)**: 연관된 엔터티를 실제로 사용할 때 조회합니다.

그러나 `FetchType`만으로는 다음과 같은 문제를 해결할 수 없습니다.

1. **N+1 문제 발생**: `LAZY` 로딩을 사용할 경우, 연관된 엔터티가 개별적으로 조회되면서 다수의 추가적인 쿼리가 실행됩니다.
2. **불필요한 데이터 로딩**: `EAGER` 로딩을 사용할 경우, 필요하지 않은 데이터까지 조회될 가능성이 높습니다.
3. **복잡한 연관 관계 처리 어려움**: `FetchType`만으로 여러 개의 연관 관계를 한 번의 쿼리로 조회하기 어려운 경우가 있습니다.

```java
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 기본적으로 LAZY를 권장
    private Team team;
}
```

## 3. Fetch Join이란?
`fetch join`은 JPQL에서 `join fetch` 키워드를 사용하여 연관된 엔터티를 한 번의 쿼리로 함께 조회하는 방법입니다.

```java
String query = "SELECT m FROM Member m JOIN FETCH m.team";
List<Member> members = em.createQuery(query, Member.class).getResultList();
```

## 4. Fetch Join을 사용해야 하는 이유
### 4.1 N+1 문제 해결
- `LAZY` 로딩을 사용할 경우, 연관된 엔터티를 개별적으로 조회하면서 다수의 추가적인 쿼리가 발생할 수 있습니다.
- `fetch join`을 사용하면 단일 쿼리로 필요한 데이터를 모두 가져올 수 있어 성능을 크게 개선할 수 있습니다.

### 4.2 불필요한 데이터 로딩 방지
- `EAGER` 로딩을 사용하면 모든 연관 데이터를 무조건 불러오지만, `fetch join`을 사용하면 필요한 시점에 필요한 데이터만 조회할 수 있습니다.

### 4.3 연관 데이터 한 번에 조회 가능
- 여러 개의 연관 관계를 한 번의 쿼리로 효율적으로 가져올 수 있습니다.
- 복잡한 연관 관계가 있는 경우에도 성능 저하 없이 데이터를 조회할 수 있습니다.


## 5. JPA Fetch Join 사용 가이드

### 5.1. N+1 문제가 발생하는 상황

#### 상황 설명
게시글(Post)과 댓글(Comment)이 일대다 관계일 때, 게시글 목록과 각 게시글의 댓글 수를 함께 보여주는 상황

#### 문제가 되는 코드
```java
@Entity
public class Post {
    @Id
    private Long id;
    
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}

// 조회 코드
List<Post> posts = em.createQuery("SELECT p FROM Post p", Post.class)
    .getResultList();

// 각 게시글마다 댓글 수를 확인할 때마다 추가 쿼리 발생
for (Post post : posts) {
    post.getComments().size(); // 각 게시글마다 추가 쿼리 발생
}
```

#### Fetch Join을 사용한 해결
```java
List<Post> posts = em.createQuery(
    "SELECT p FROM Post p JOIN FETCH p.comments", Post.class)
    .getResultList();
```

### 5.2. 연관 엔티티 정보가 항상 필요한 상황

#### 상황 설명
주문(Order)과 회원(Member) 정보를 함께 보여주는 주문 목록 화면

#### 일반적인 코드
```java
@Entity
public class Order {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}

// 조회할 때마다 회원 정보를 위한 추가 쿼리 발생
List<Order> orders = em.createQuery("SELECT o FROM Order o", Order.class)
    .getResultList();

for (Order order : orders) {
    System.out.println("주문자: " + order.getMember().getName()); // 추가 쿼리 발생
}
```

#### Fetch Join을 사용한 최적화
```java
List<Order> orders = em.createQuery(
    "SELECT o FROM Order o JOIN FETCH o.member", Order.class)
    .getResultList();
```

### 5.3. 특정 비즈니스 로직에서 연관 데이터가 필수인 상황

#### 상황 설명
팀(Team)과 소속 멤버(Member)의 급여를 계산하는 급여 정산 처리

#### 비효율적인 코드
```java
@Entity
public class Team {
    @OneToMany(mappedBy = "team")
    private List<Member> members;
    
    public int calculateTotalSalary() {
        // members가 지연 로딩이면 여기서 쿼리 발생
        return members.stream()
            .mapToInt(Member::getSalary)
            .sum();
    }
}

// 전체 팀 급여 계산
List<Team> teams = em.createQuery("SELECT t FROM Team t", Team.class)
    .getResultList();

for (Team team : teams) {
    team.calculateTotalSalary(); // 각 팀마다 멤버 조회 쿼리 발생
}
```

#### Fetch Join으로 최적화
```java
List<Team> teams = em.createQuery(
    "SELECT t FROM Team t JOIN FETCH t.members", Team.class)
    .getResultList();

// 추가 쿼리 없이 급여 계산 가능
for (Team team : teams) {
    team.calculateTotalSalary();
}
```

### 5.4. 실시간 데이터 조회가 필요한 상황

#### 상황 설명
실시간 재고 관리 시스템에서 상품(Product)과 재고(Stock) 정보를 함께 조회

#### Fetch Join 활용
```java
// 재고 정보까지 즉시 조회
List<Product> products = em.createQuery(
    "SELECT p FROM Product p JOIN FETCH p.stock", Product.class)
    .getResultList();
```

### 주의사항

1. **페이징이 필요한 경우**
    - 일대다 관계에서는 페치 조인과 페이징을 함께 사용하면 안됨
    - 대안: ToOne 관계만 페치 조인하고, 컬렉션은 배치 사이즈로 조절

2. **데이터 중복이 발생하는 경우**
    - 일대다 조인으로 인한 데이터 증가 주의
    - DISTINCT 사용 고려

3. **메모리 사용량**
    - 대량의 데이터를 조회할 때는 메모리 사용량 고려
    - 필요한 경우 적절한 WHERE 조건 추가


## 6. Fetch Join 사용 시 주의할 점
- `fetch join`을 사용할 때 `distinct`를 추가하지 않으면 중복된 데이터가 조회될 가능성이 있습니다.
- `fetch join`은 `@OneToMany` 관계에서 사용할 경우 조인으로 인해 데이터가 중복될 수 있으므로 `DISTINCT`를 고려해야 합니다.
- `fetch join`을 남용하면 조인 테이블이 많아져 성능이 저하될 수 있으므로 필요할 때만 사용해야 합니다.

```java
String query = "SELECT DISTINCT m FROM Member m JOIN FETCH m.team";
```

## 7. 결론

- FetchType만으로는 N+1 문제를 해결할 수 없으므로 fetch join을 적극적으로 활용해야 합니다.

- fetch join을 사용하면 필요한 데이터만 한 번의 쿼리로 가져올 수 있어 성능 최적화에 유리합니다.

- 하지만, 무분별한 사용은 성능 저하를 초래할 수 있으므로 신중하게 적용해야 합니다.

