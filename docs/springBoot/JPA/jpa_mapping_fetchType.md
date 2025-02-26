# JPA Fetch Types 완벽 가이드

## 목차
- [개요](#개요)
- [Fetch Type 종류](#fetch-type-종류)
- [매핑 타입별 기본값](#매핑-타입별-기본값)
- [성능 최적화 전략](#성능-최적화-전략)
- [실제 활용 사례](#실제-활용-사례)
- [주의사항](#주의사항)

## 개요

JPA의 Fetch Type은 연관된 엔티티를 어떤 시점에 가져올지를 결정하는 중요한 설정입니다. 적절한 Fetch Type의 선택은 애플리케이션의 성능과 직결됩니다.

## Fetch Type 종류

### LAZY (지연 로딩)
- 연관 엔티티를 실제 사용하는 시점에 로딩
- 프록시 객체를 사용하여 필요한 시점까지 데이터베이스 조회를 미룸
```java
@Entity
public class Member {
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;
}
```

### EAGER (즉시 로딩)
- 엔티티를 조회할 때 연관된 엔티티도 함께 로딩
- JOIN을 사용하여 한 번에 데이터를 가져옴
```java
@Entity
public class Member {
    @ManyToOne(fetch = FetchType.EAGER)
    private Team team;
}
```

## 매핑 타입별 기본값

| 연관관계         | 기본 Fetch Type |
|--------------|-|
| @ManyToOne   | EAGER |
| @OneToOne    | EAGER |
| @OneToMany   | LAZY |
| @ManyToMany  | LAZY |

### 기본값 설정 이유
- **@ManyToOne, @OneToOne**: 단일 객체 참조이므로 즉시 로딩이 기본값
- **@OneToMany, @ManyToMany**: 컬렉션을 즉시 로딩하면 성능 문제가 발생할 수 있어 지연 로딩이 기본값

## 성능 최적화 전략

### LAZY 사용이 권장되는 경우
1. 컬렉션 연관관계(@OneToMany, @ManyToMany)
2. 자주 사용되지 않는 연관관계
3. 실시간 조회가 필요하지 않은 경우

### EAGER 사용이 권장되는 경우
1. 항상 함께 사용되는 엔티티 관계
2. 단일 엔티티 조회(@ManyToOne, @OneToOne)에서 연관 데이터가 반드시 필요한 경우
3. 성능 상 큰 영향이 없는 소규모 데이터

## 실제 활용 사례

### 회원-팀 관계 예시
```java
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)  // 팀 정보가 항상 필요하지 않은 경우
    private Team team;
    
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)  // 주문 목록은 필요할 때만
    private List<Order> orders = new ArrayList<>();
}
```

### N+1 문제 해결 방법
```java
// JPQL에서 fetch join 사용
@Query("SELECT m FROM Member m JOIN FETCH m.team")
List<Member> findAllWithTeam();

// EntityGraph 사용
@EntityGraph(attributePaths = {"team"})
List<Member> findAll();
```

## 주의사항

### LAZY 로딩 사용 시 주의점
1. LazyInitializationException 발생 가능성
    - 트랜잭션 범위 밖에서 지연 로딩 시도 시 발생
    - Open Session in View 설정 고려 필요

2. N+1 문제 발생 가능성
    - 연관된 엔티티를 개별적으로 조회하면서 발생
    - fetch join 또는 EntityGraph로 해결

### EAGER 로딩 사용 시 주의점
1. 불필요한 조인 발생
    - 연관된 데이터가 필요하지 않은 경우에도 항상 조회

2. 성능 저하 가능성
    - 다중 연관관계에서 카테시안 곱 발생 가능
    - 여러 EAGER 로딩이 겹치면 성능 저하 심화

## 권장 사항

1. 기본적으로 모든 연관관계에 LAZY 로딩 사용
2. 필요한 경우에만 fetch join으로 최적화
3. 실제 비즈니스 로직에 따라 필요한 경우에만 EAGER 로딩 고려
4. 성능 테스트를 통한 검증 필수