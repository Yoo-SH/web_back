# Spring Database 수직 파티셔닝 예제

## 목차
- [개요](#개요)
- [구현 예제](#구현-예제)
    - [사용자 정보 파티셔닝](#사용자-정보-파티셔닝)
    - [상품 정보 파티셔닝](#상품-정보-파티셔닝)
    - [게시글 컨텐츠 파티셔닝](#게시글-컨텐츠-파티셔닝)

## 개요

데이터베이스 수직 파티셔닝은 테이블의 컬럼을 기준으로 데이터를 분할하는 방식입니다. 자주 접근하는 데이터와 덜 접근하는 데이터를 분리하여 성능을 최적화할 수 있습니다.

## 구현 예제

### 사용자 정보 파티셔닝

자주 조회되는 기본 사용자 정보와 덜 조회되는 상세 정보를 분리합니다.

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String email;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_detail_id")
    private UserDetail userDetail;
}

@Entity
@Table(name = "user_details")
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String address;
    private String phoneNumber;
    private LocalDate birthDate;
    @Lob
    private byte[] profileImage;
}
```

Repository 구현:
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findBasicInfo(@Param("id") Long id);
    
    @Query("SELECT u FROM User u JOIN FETCH u.userDetail WHERE u.id = :id")
    Optional<User> findWithDetails(@Param("id") Long id);
}
```

### 상품 정보 파티셔닝

기본 상품 정보와 상세 설명을 분리합니다.

```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private BigDecimal price;
    private Integer stock;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
}

@Entity
@Table(name = "product_details")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    private String description;
    @Lob
    private byte[] image;
    private String specifications;
}
```

Service 구현:
```java
@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    
    public Product getBasicInfo(Long id) {
        return productRepository.findBasicInfo(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }
    
    public Product getFullInfo(Long id) {
        return productRepository.findWithDetails(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
```

### 게시글 컨텐츠 파티셔닝

게시글 메타데이터와 실제 컨텐츠를 분리합니다.

```java
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String author;
    private LocalDateTime createdAt;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_content_id")
    private PostContent content;
}

@Entity
@Table(name = "post_contents")
public class PostContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    private String content;
    @Lob
    private byte[] attachments;
}
```

Configuration 예제:
```java
@Configuration
public class DatabaseConfig {
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
```

## 장점

1. 성능 최적화
    - 자주 접근하는 데이터만 빠르게 조회 가능
    - 불필요한 데이터 로딩 방지

2. 리소스 효율성
    - 큰 용량의 데이터를 별도 저장소에 보관
    - 메인 DB의 부하 감소

3. 유지보수성
    - 데이터 특성에 따른 관리 용이
    - 독립적인 백업 및 복구 가능

## 주의사항

1. 조인 비용
    - 데이터 결합 시 추가적인 조인 발생
    - 적절한 인덱싱 필요

2. 트랜잭션 관리
    - 분산 트랜잭션 처리 고려
    - 데이터 일관성 유지 필요

3. 복잡성
    - 구현 및 유지보수 복잡도 증가
    - 적절한 문서화 필요