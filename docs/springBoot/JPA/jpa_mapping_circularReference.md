# JPA 순환참조 (Circular Reference) 완벽 가이드

## 목차
- [개요](#개요)
- [JPA 순환참조 문제점](#jpa-순환참조-문제점-)
- [해결 방안](#해결-방안)
- [Best Practices](#best-practices)
- [실제 구현 예시](#실제-구현-예시)

## 개요

JPA에서 순환참조는 두 개 이상의 엔티티가 서로를 참조하는 상황을 말합니다. 양방향 연관관계에서 주로 발생하며, 특히 객체를 JSON으로 직렬화할 때 심각한 문제를 일으킬 수 있습니다.

### 일반적인 순환참조 발생 상황

```java
@Entity
public class Post {
    @Id @GeneratedValue
    private Long id;
    
    private String title;
    
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}

@Entity
public class Comment {
    @Id @GeneratedValue
    private Long id;
    
    private String content;
    
    @ManyToOne
    private Post post;
}
```

## JPA 순환참조 문제점 

### 1. 무한 재귀 호출 발생 원리

#### JSON 직렬화 과정에서의 무한 루프
```java
Post {
    id: 1,
    comments: [
        Comment {
            id: 1,
            post: Post {
                id: 1,
                comments: [
                    Comment {
                        id: 1,
                        post: Post {
                            // 무한 반복...
                        }
                    }
                ]
            }
        }
    ]
}
```

객체를 JSON으로 직렬화할 때 Jackson 라이브러리는 다음과 같은 과정을 거칩니다:

1. Post 객체의 모든 필드를 순회
2. comments 리스트를 만나면 각 Comment 객체의 필드를 순회
3. Comment 객체 안의 post 필드를 다시 직렬화 시도
4. 다시 Post 객체의 모든 필드를 순회하는 과정 반복

이 과정이 무한히 반복되면서:
- 스택 메모리가 계속 쌓임
- 결국 StackOverflowError 발생
- JVM의 메모리 사용량이 급격히 증가

#### 실제 스택 추적 예시
```
java.lang.StackOverflowError
    at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:166)
    at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:688)
    at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:158)
    at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:688)
    // 계속 반복...
```

### 2. 성능 저하 원인 분석

#### 불필요한 데이터 로딩 메커니즘
```java
@Entity
public class Post {
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments;
}

@Entity
public class Comment {
    @ManyToOne(fetch = FetchType.EAGER)  // 기본값
    private Post post;
}
```

##### 데이터 로딩 시나리오:
1. Post 엔티티 조회
2. Post의 comments 접근 시 추가 쿼리 실행
3. 각 Comment의 post 필드는 EAGER로 설정되어 있어 자동 로딩
4. 이미 로딩된 데이터를 다시 로딩하는 중복 발생

#### N+1 문제 발생 원리
예를 들어 10개의 게시글을 조회하는 경우:
```sql
-- 최초 게시글 목록 조회
SELECT * FROM post;  -- 1번 쿼리

-- 각 게시글의 댓글 조회
SELECT * FROM comment WHERE post_id = 1;  -- 2번 쿼리
SELECT * FROM comment WHERE post_id = 2;  -- 3번 쿼리
SELECT * FROM comment WHERE post_id = 3;  -- 4번 쿼리
... (게시글 수만큼 반복)
```

순환참조 구조에서는:
1. 각 엔티티를 조회할 때마다 연관된 엔티티도 함께 조회
2. 이미 메모리에 있는 데이터도 다시 조회
3. 데이터베이스 부하 증가

### 3. API 응답 문제의 기술적 원인

#### 불필요하게 큰 JSON 응답이 생성되는 과정
예시 데이터 구조:
```json
{
    "post": {
        "id": 1,
        "title": "제목",
        "comments": [
            {
                "id": 1,
                "content": "댓글1",
                "post": {
                    "id": 1,
                    "title": "제목",
                    "comments": [
                        // 반복...
                    ]
                }
            }
        ]
    }
}
```

##### 발생 원인:
1. 모든 연관 관계가 JSON에 포함됨
2. 동일한 데이터가 중복해서 포함됨
3. 데이터 크기가 기하급수적으로 증가

#### 클라이언트 성능 저하 메커니즘

1. **네트워크 부하**
    - 큰 JSON 응답으로 인한 전송 시간 증가
    - 대역폭 낭비
    - 모바일 환경에서 특히 심각한 문제 발생

2. **클라이언트 처리 부하**
    - 브라우저나 모바일 기기에서 큰 JSON 파싱 필요
    - 메모리 사용량 증가
    - UI 렌더링 지연

3. **캐시 효율성 저하**
    - 불필요하게 큰 데이터로 인한 캐시 히트율 감소
    - 캐시 메모리 낭비

## 해결을 위한 기술적 접근 방법

1. **즉시 로딩(EAGER)을 지양하고 지연 로딩(LAZY) 사용**
   ```java
   @ManyToOne(fetch = FetchType.LAZY)
   private Post post;
   ```

2. **필요한 데이터만 조회하는 JPQL 사용**
   ```java
   @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :id")
   Optional<Post> findByIdWithComments(@Param("id") Long id);
   ```

3. **DTO를 통한 데이터 제어**
   ```java
   public class PostDTO {
       private Long id;
       private String title;
       private List<CommentDTO> comments;
       
       // comments에서 post 정보는 제외
   }
   ```

이러한 문제들은 모두 순환참조의 특성에서 비롯되며, 특히 JPA의 영속성 컨텍스트와 JSON 직렬화 과정에서 더욱 두드러지게 나타납니다. 따라서 설계 단계에서부터 이러한 문제들을 고려하여 적절한 해결 방안을 적용하는 것이 중요합니다.

4. **@JsonIgnore 사용**
```java
@Entity
public class Comment {
    @ManyToOne
    @JsonIgnore
    private Post post;
}
```

5. **@JsonManagedReference와 @JsonBackReference 사용**
```java
@Entity
public class Post {
    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}

@Entity
public class Comment {
    @JsonBackReference
    @ManyToOne
    private Post post;
}
```


5. **@JsonIgnoreProperties 사용**
```java
@Entity
@JsonIgnoreProperties({"comments"})
public class Post {
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
```

## Best Practices

1. **엔티티 설계 시 고려사항**
    - 양방향 연관관계가 정말 필요한지 검토
    - 단방향 관계를 우선적으로 고려
    - 연관관계의 주인을 신중하게 결정

2. **성능 최적화**
    - 필요한 데이터만 조회하도록 JPQL 작성
    - fetch join 적절히 활용
    - DTO 변환 시점 최적화

3. **API 설계**
    - API 스펙에 맞는 DTO 설계
    - 계층별 명확한 책임 분리
    - 비즈니스 요구사항에 따른 적절한 조회 방식 선택

## 실제 구현 예시

### DTO 패턴을 사용한 완전한 예시

```java
@RestController
@RequestMapping("/api/posts")
public class PostApiController {
    
    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException());
        
        return new PostDTO(post);
    }
}

@Service
@Transactional(readOnly = true)
public class PostService {
    
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException());
            
        return new PostDTO(post);
    }
}

public class PostDTO {
    private Long id;
    private String title;
    private List<CommentDTO> comments;
    
    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.comments = post.getComments().stream()
            .map(CommentDTO::new)
            .collect(Collectors.toList());
    }
}
```

### 성능 최적화된 조회 쿼리

```java
@Repository
public class PostRepository extends JpaRepository<Post, Long> {
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :id")
    Optional<Post> findByIdWithComments(@Param("id") Long id);
}
```

## 마무리

순환참조 문제는 JPA를 사용할 때 흔히 마주치는 문제이지만, 적절한 전략과 패턴을 적용하면 효과적으로 해결할 수 있습니다. 프로젝트의 요구사항과 상황에 맞는 해결 방안을 선택하여 적용하는 것이 중요합니다.

### 참고사항

- 항상 비즈니스 요구사항을 먼저 고려하세요.
- 성능과 유지보수성을 모두 고려한 설계를 하세요.
- 필요한 경우 여러 해결방안을 조합하여 사용하세요.