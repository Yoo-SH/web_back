# JPA Entity Relationship Guide

## 1. 관계 매핑 종류

### OneToOne (1:1)
```java
@OneToOne
@JoinColumn(name = "user_id")
private User user;
```
**사용 시나리오:**
- 사용자와 사용자 상세정보
- 직원과 근로계약서
- 주문과 배송정보

**특징:**
- 양쪽 엔티티 모두 하나의 관계만 가짐
- 주 테이블이나 대상 테이블 중에 외래 키 선택 가능
- `@JoinColumn`을 사용하여 외래 키 지정

### OneToMany (1:N)
```java
@OneToMany(mappedBy = "team")
private List<Member> members = new ArrayList<>();
```
**사용 시나리오:**
- 팀과 소속 팀원들
- 게시판과 게시글들
- 주문과 주문상품들

**특징:**
- 일대다 관계는 여러 건과 연관관계 가능
- `mappedBy`를 통해 양방향 관계 설정
- `cascade` 옵션으로 영속성 전이 설정 가능

### ManyToOne (N:1)
```java
@ManyToOne
@JoinColumn(name = "team_id")
private Team team;
```
**사용 시나리오:**
- 팀원과 소속 팀
- 게시글과 작성자
- 주문상품과 상품

**특징:**
- 다대일 관계의 반대편
- 외래 키가 있는 쪽이 연관관계의 주인
- 항상 `@JoinColumn`을 사용하여 외래 키 매핑

### ManyToMany (N:M)
```java
@ManyToMany
@JoinTable(
    name = "member_product",
    joinColumns = @JoinColumn(name = "member_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id")
)
private List<Product> products = new ArrayList<>();
```
**사용 시나리오:**
- 학생과 수강과목
- 사용자와 권한그룹
- 상품과 카테고리

**특징:**
- 실무에서는 사용을 지양함
- 중간 테이블이 자동으로 생성됨
- 대신 중간 엔티티를 만들어 OneToMany, ManyToOne으로 풀어내는 것을 권장

## 2. 연관관계 설정 가이드

### 단방향 vs 양방향
- **단방향:** 한쪽 엔티티만 참조하는 경우
- **양방향:** 양쪽 엔티티 모두가 서로 참조하는 경우
  ```java
  // 양방향 예시
  @Entity
  public class Team {
      @OneToMany(mappedBy = "team")
      private List<Member> members;
  }

  @Entity
  public class Member {
      @ManyToOne
      @JoinColumn(name = "team_id")
      private Team team;
  }
  ```

### 연관관계의 주인 선정
1. **외래 키가 있는 곳을 주인으로 정해야 함**
2. `mappedBy` 속성은 주인이 아닌 쪽에서 설정
3. 주인만이 외래 키 값을 변경할 수 있음

### Cascade 옵션 선택 가이드
```java
@OneToMany(cascade = CascadeType.ALL)
private List<OrderItem> orderItems;
```

- **ALL:** 모든 상황에서 전파
- **PERSIST:** 저장 시에만 전파
- **MERGE:** 병합 시에만 전파
- **REMOVE:** 삭제 시에만 전파

**사용 시나리오:**
- 부모 엔티티와 자식 엔티티의 라이프사이클이 동일할 때
- 단일 소유자일 때
- 다른 엔티티가 자식 엔티티를 참조하지 않을 때

### 페치 전략 선정
```java
@ManyToOne(fetch = FetchType.LAZY)  // 지연 로딩
@ManyToOne(fetch = FetchType.EAGER) // 즉시 로딩
```

**권장사항:**
- `@ManyToOne`, `@OneToOne`: 기본이 EAGER이므로 LAZY로 설정
- `@OneToMany`, `@ManyToMany`: 기본이 LAZY이므로 그대로 사용

## 3. 실무 Best Practices

1. **`@ManyToMany` 대신 중간 엔티티 사용**
```java
@Entity
public class OrderProduct {
    @ManyToOne
    private Order order;
    
    @ManyToOne
    private Product product;
    
    private int quantity;
}
```

2. **연관관계 편의 메서드 작성**
```java
public void setTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
}
```

3. **지연 로딩 사용**
- N+1 문제 방지를 위해 필요한 경우에만 fetch join 사용

4. **컬렉션은 필드에서 초기화**
```java
@OneToMany(mappedBy = "team")
private List<Member> members = new ArrayList<>();
```

5. **양방향 관계에서는 toString() 무한루프 주의**
- `@ToString.Exclude` 사용