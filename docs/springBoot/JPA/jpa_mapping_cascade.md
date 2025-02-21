# JPA Cascade (영속성 전이)

## 개요
JPA Cascade는 엔티티의 상태 변화를 연관된 엔티티에도 함께 적용하는 기능입니다. 부모 엔티티의 변경이 자식 엔티티에도 전파되도록 설정할 수 있습니다.

## Cascade 종류
- **ALL**: 모든 상태 변화를 전이
- **PERSIST**: 영속화(저장) 상태만 전이
- **MERGE**: 병합 상태만 전이
- **REMOVE**: 삭제 상태만 전이
- **REFRESH**: 새로고침 상태만 전이
- **DETACH**: 분리 상태만 전이

## 사용법 예시
```java
@Entity
public class Parent {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Child> children = new ArrayList<>();
}
```

## Cascade 사용이 권장되는 상황

1. **부모-자식 관계가 명확한 경우**
    - 게시글과 댓글
    - 주문과 주문상세항목
    - 앨범과 사진

2. **라이프사이클이 동일한 경우**
    - 부모 엔티티와 자식 엔티티의 생명주기가 같을 때
    - 함께 생성되고 함께 삭제되어야 하는 관계

3. **단일 소유자**
    - 자식 엔티티가 하나의 부모 엔티티에만 종속될 때
    - 다른 엔티티와 공유되지 않는 경우

## Cascade 사용을 피해야 하는 상황

1. **여러 엔티티가 관계를 공유하는 경우**
    - 하나의 자식 엔티티가 여러 부모 엔티티와 관계가 있는 경우
    - 예: 카테고리와 상품의 관계

2. **독립적인 라이프사이클**
    - 각 엔티티가 독립적으로 관리되어야 하는 경우
    - 예: 회원과 주문의 관계

## 주의사항

1. **성능 고려**
    - Cascade 설정으로 인한 불필요한 DB 작업이 발생할 수 있음
    - 특히 REMOVE 케이스케이드는 신중하게 사용해야 함

2. **데이터 정합성**
    - 양방향 연관관계에서는 관계 설정에 주의
    - 연관관계 편의 메서드 사용 권장

3. **트랜잭션 범위**
    - Cascade 작업은 하나의 트랜잭션 내에서 수행되어야 함

## 모범 사례

```java
@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    
    // 연관관계 편의 메서드
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }
}
```

## JPA Cascade 유형별 상세 예제

### 1. CascadeType.ALL 예제
모든 상태 변화가 전이되므로 가장 강력한 cascade 옵션입니다.

```java
@Entity
public class Order {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}

// 사용 예제
Order order = new Order();
OrderItem item1 = new OrderItem();
OrderItem item2 = new OrderItem();

order.addOrderItem(item1);
order.addOrderItem(item2);

// order만 저장해도 orderItems도 함께 저장됨
entityManager.persist(order);
```

### 2. CascadeType.PERSIST 예제
저장 시에만 cascade가 동작합니다.

```java
@Entity
public class Team {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST)
    private List<Player> players = new ArrayList<>();
}

// 사용 예제
Team team = new Team();
Player player1 = new Player();
Player player2 = new Player();

team.getPlayers().add(player1);
team.getPlayers().add(player2);
player1.setTeam(team);
player2.setTeam(team);

// team 저장 시 player들도 함께 저장됨
entityManager.persist(team);

// 하지만 team 삭제 시 player는 삭제되지 않음
entityManager.remove(team); // player는 그대로 남음
```

### 3. CascadeType.MERGE 예제
병합 시에만 cascade가 동작합니다.

```java
@Entity
public class Department {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "department", cascade = CascadeType.MERGE)
    private List<Employee> employees = new ArrayList<>();
}

// 사용 예제
Department department = entityManager.find(Department.class, 1L);
department.setName("New Department Name");

Employee newEmployee = new Employee();
department.getEmployees().add(newEmployee);
newEmployee.setDepartment(department);

// department를 merge할 때 새로운 employee도 함께 merge됨
Department mergedDepartment = entityManager.merge(department);
```

### 4. CascadeType.REMOVE 예제
삭제 시에만 cascade가 동작합니다.

```java
@Entity
public class Board {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();
}

// 사용 예제
Board board = entityManager.find(Board.class, 1L);

// board를 삭제하면 연관된 posts도 모두 삭제됨
entityManager.remove(board);
```

### 5. CascadeType.REFRESH 예제
데이터베이스로부터 다시 조회할 때 cascade가 동작합니다.

```java
@Entity
public class Author {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.REFRESH)
    private List<Book> books = new ArrayList<>();
}

// 사용 예제
Author author = entityManager.find(Author.class, 1L);
// 다른 트랜잭션에서 데이터가 변경된 후
entityManager.refresh(author); // author와 연관된 books도 함께 refresh됨
```

### 6. CascadeType.DETACH 예제
영속성 컨텍스트에서 분리할 때 cascade가 동작합니다.

```java
@Entity
public class University {
    @Id @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "university", cascade = CascadeType.DETACH)
    private List<Student> students = new ArrayList<>();
}

// 사용 예제
University university = entityManager.find(University.class, 1L);

// university를 detach하면 연관된 students도 모두 영속성 컨텍스트에서 분리됨
entityManager.detach(university);

// 이후 university나 students를 수정해도 DB에 반영되지 않음
university.setName("New Name"); // 변경 추적 안됨
university.getStudents().get(0).setName("New Student Name"); // 변경 추적 안됨
```

### 주의사항
1. 각 Cascade 타입은 조합해서 사용할 수 있습니다.
```java
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
```

2. CascadeType.ALL은 모든 Cascade 타입을 포함하므로 신중하게 사용해야 합니다.

3. 양방향 관계에서는 연관관계 편의 메서드를 사용하는 것이 좋습니다.

4. Cascade는 직접 관계가 있는 엔티티에만 적용되며, 연관된 엔티티의 연관 엔티티까지는 적용되지 않습니다.

## 결론
Cascade는 강력한 기능이지만 신중하게 사용해야 합니다. 엔티티 간의 관계와 라이프사이클을 충분히 고려한 후 적용하는 것이 중요합니다.