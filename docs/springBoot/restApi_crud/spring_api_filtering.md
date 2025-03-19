# REST API JSON 필터링 가이드

## 목차
- [정적 필터링](#정적-필터링)
  - [Jackson 어노테이션: @JsonIgnore](#jsonignore)
  - [Jackson 어노테이션: @JsonProperty](#jsonproperty)
- [동적 필터링](#동적-필터링)
  - [MappingJacksonValue](#mappingjacksonvalue)
  - [SimpleBeanPropertyFilter](#simplebeanpropertyfilter)

## 정적 필터링

### @JsonIgnore

`@JsonIgnore`는 특정 필드를 JSON 직렬화 과정에서 제외하고 싶을 때 사용합니다.

#### 사용해야 하는 상황:
- 민감한 정보(비밀번호, 보안 키 등)를 API 응답에서 제외할 때
- 불필요한 정보를 응답에서 제외하고 싶을 때
- 모든 API 응답에서 일관되게 특정 필드를 제외하고 싶을 때

#### 예제:

```java
public class User {
    private Long id;
    private String username;
    
    @JsonIgnore
    private String password;
    
    // getters and setters
}
```

이 경우, `password` 필드는 JSON 응답에 포함되지 않습니다.

### @JsonProperty

`@JsonProperty`는 필드 이름을 JSON에서 다른 이름으로 표현하고 싶을 때 사용합니다.

#### 사용해야 하는 상황:
- Java 네이밍 규칙과 JSON 규칙이 다를 때 (e.g., camelCase vs snake_case)
- 레거시 시스템과 호환성을 유지해야 할 때
- API 문서에 정의된 규칙을 따라야 할 때

#### 예제:

```java
public class Product {
    @JsonProperty("product_id")
    private Long id;
    
    @JsonProperty("product_name")
    private String name;
    
    @JsonProperty("unit_price")
    private BigDecimal price;
    
    // getters and setters
}
```

이 경우, JSON 응답은 다음과 같이 나타납니다:

```json
{
    "product_id": 1,
    "product_name": "노트북",
    "unit_price": 1500.00
}
```

## 동적 필터링

### MappingJacksonValue

`MappingJacksonValue`는 런타임에 동적으로 필드를 필터링하고 싶을 때 사용합니다.

#### 사용해야 하는 상황:
- 같은 엔티티에 대해 다양한 API 엔드포인트가 다른 필드를 보여줘야 할 때
- 사용자 권한에 따라 다른 필드를 보여줘야 할 때
- 요청 파라미터에 따라 응답 필드를 조정해야 할 때

#### 전체 예제:

1. 먼저 필터를 사용할 클래스에 `@JsonFilter` 어노테이션을 추가합니다:

```java
@JsonFilter("UserFilter")
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String address;
    
    // getters and setters
}
```

2. 컨트롤러에서 `MappingJacksonValue`를 사용하여 동적으로 필터를 적용합니다:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}/basic")
    public MappingJacksonValue getUserBasicInfo(@PathVariable Long id) {
        User user = userService.findById(id);
        
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
            .filterOutAllExcept("id", "username");
            
        FilterProvider filters = new SimpleFilterProvider()
            .addFilter("UserFilter", filter);
            
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        
        return mapping;
    }
    
    @GetMapping("/{id}/detail")
    public MappingJacksonValue getUserDetailInfo(@PathVariable Long id) {
        User user = userService.findById(id);
        
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
            .filterOutAllExcept("id", "username", "email", "address");
            
        FilterProvider filters = new SimpleFilterProvider()
            .addFilter("UserFilter", filter);
            
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        
        return mapping;
    }
    
    @GetMapping("/{id}/admin")
    @Secured("ROLE_ADMIN")
    public MappingJacksonValue getUserAdminInfo(@PathVariable Long id) {
        User user = userService.findById(id);
        
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
            .serializeAll();
            
        FilterProvider filters = new SimpleFilterProvider()
            .addFilter("UserFilter", filter);
            
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        
        return mapping;
    }
}
```

#### 결과:

1. `/api/users/{id}/basic` 엔드포인트:
```json
{
    "id": 1,
    "username": "user123"
}
```

2. `/api/users/{id}/detail` 엔드포인트:
```json
{
    "id": 1,
    "username": "user123",
    "email": "user@example.com",
    "address": "서울시 강남구"
}
```

3. `/api/users/{id}/admin` 엔드포인트(관리자만 접근 가능):
```json
{
    "id": 1,
    "username": "user123",
    "password": "$2a$10$...", // 실제로는 암호화된 값
    "email": "user@example.com",
    "address": "서울시 강남구"
}
```

### SimpleBeanPropertyFilter

`SimpleBeanPropertyFilter`는 `MappingJacksonValue`와 함께 사용되며 다음과 같은 주요 메소드를 제공합니다:

- `filterOutAllExcept()`: 지정된 필드만 포함하고 나머지는 제외
- `serializeAllExcept()`: 지정된 필드를 제외하고 나머지는 포함
- `serializeAll()`: 모든 필드 포함

## 정적 vs 동적 필터링: 선택 가이드

1. **정적 필터링(@JsonIgnore, @JsonProperty)을 선택해야 할 때:**
   - 모든 API 호출에서 일관되게 필드를 제외/변경해야 할 때
   - 코드가 단순하고 유지보수가 쉬워야 할 때
   - 보안상 민감한 정보를 항상 제외해야 할 때

2. **동적 필터링(MappingJacksonValue)을 선택해야 할 때:**
   - 사용자 권한에 따라 다른 필드를 보여줘야 할 때
   - 같은 엔티티에 대해 다양한 뷰가 필요할 때
   - 런타임에 필터링 규칙이 결정되는 경우
   - 클라이언트 요청에 따라 응답 형태를 조정해야 할 때

## 결론

REST API에서 JSON 응답을 필터링하는 방법은 애플리케이션의 요구사항에 따라 다릅니다. 정적 필터링은 구현이 간단하고 일관성을 유지할 수 있으며, 동적 필터링은 더 유연하게 데이터를 제공할 수 있습니다. 두 접근 방식을 적절히 조합하여 사용하면 효과적인 API를 설계할 수 있습니다.