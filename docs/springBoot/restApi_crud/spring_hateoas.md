# Spring HATEOAS 가이드

## 목차
- [HATEOAS란?](#hateoas란)
- [HATEOAS를 사용하는 이유](#hateoas를-사용하는-이유)
- [적합한 사용 상황](#적합한-사용-상황)
- [Spring에서 HATEOAS 사용법](#spring에서-hateoas-사용법)
  - [Entity와 Model의 관계](#entity와-model의-관계)
- [대표 예제](#대표-예제)
- [결론](#결론)

## HATEOAS란?
HATEOAS(Hypermedia As The Engine Of Application State)는 REST 아키텍처의 구성 요소 중 하나로, 클라이언트가 서버와 동적으로 상호작용할 수 있도록 하이퍼미디어 링크를 통해 애플리케이션의 상태를 관리하는 방식입니다. 간단히 말해, API 응답에 다음 가능한 액션에 대한 링크를 함께 제공하는 것입니다.

예를 들어, 사용자가 주문을 조회할 때 API는 다음과 같은 작업에 대한 링크를 함께 제공합니다:
- 주문 취소하기
- 배송 상태 확인하기
- 결제 상세정보 보기

## HATEOAS를 사용하는 이유

### 1. 클라이언트와 서버의 느슨한 결합
클라이언트가 API의 URI 구조를 미리 알 필요가 없으며, 응답에 포함된 링크를 통해 다음 작업을 진행할 수 있습니다. 서버가 URI 구조를 변경해도 클라이언트 코드를 수정할 필요가 없습니다.

### 2. API 탐색 가능성(Discoverability) 향상
클라이언트는 초기 진입점만 알면 응답에 포함된 링크를 따라가며 전체 API를 탐색할 수 있습니다.

### 3. 상태 전이의 명확한 표현
현재 리소스에서 가능한 상태 전이(다음 작업)를 명확하게 표현합니다. 예를 들어, 결제가 완료된 주문은 '취소' 링크를 제공하지 않을 수 있습니다.

### 4. API 버전 관리 용이성
API 진화에 유연하게 대응할 수 있습니다. 새로운 기능이나 리소스를 추가할 때 기존 링크는 그대로 유지하면서 새로운 링크를 추가할 수 있습니다.

### 5. 자기 설명적(Self-descriptive) API
API 응답 자체에 다음 작업에 대한 정보가 포함되어 있어 API 문서에 의존도가 줄어듭니다.

## 적합한 사용 상황

HATEOAS는 다음과 같은 상황에서 특히 유용합니다:

### 1. 복잡한 비즈니스 프로세스를 가진 API
여러 단계의 작업이 필요한 복잡한 비즈니스 프로세스(예: 주문 처리, 결제 처리 등)에서 각 단계별로 가능한 작업을 명확히 제시할 수 있습니다.

### 2. 공개 API
외부 개발자들이 사용하는 공개 API에서는 API의 사용성과 발견 가능성을 높이기 위해 HATEOAS를 적용하는 것이 좋습니다.

### 3. 상태에 따라 가능한 작업이 달라지는 경우
리소스의 상태에 따라 수행할 수 있는 작업이 달라지는 경우 (예: 결제 완료 전/후, 배송 전/중/후 등) HATEOAS를 통해 현재 상황에서 가능한 작업만 제시할 수 있습니다.

### 4. 장기적으로 확장될 API
시간이 지남에 따라 기능이 추가되거나 변경될 가능성이 높은 API에서는 HATEOAS를 통해 유연성을 확보할 수 있습니다.

## Spring에서 HATEOAS 사용법

Spring에서는 `spring-boot-starter-hateoas` 의존성을 추가하여 HATEOAS를 쉽게 구현할 수 있습니다.

### 의존성 추가

**Maven:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

**Gradle:**
```gradle
implementation 'org.springframework.boot:spring-boot-starter-hateoas'
```

### Entity와 Model의 관계

Spring HATEOAS에서 Entity와 Model의 관계를 이해하는 것이 중요합니다:

#### Entity
- 데이터베이스 테이블과 매핑되는 JPA 엔티티 클래스입니다.
- 데이터의 저장과 관련된 로직을 담당합니다.
- 링크 정보를 포함하지 않습니다.

예:
```java
@Entity
public class Customer {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String email;
    
    // getter, setter 등
}
```

#### Model (Resource/RepresentationModel)
- API 응답으로 클라이언트에게 전달되는 정보를 담는 객체입니다.
- Entity의 데이터를 포함하면서 추가적으로 하이퍼미디어 링크를 포함합니다.
- Spring HATEOAS에서는 `RepresentationModel`을 상속받아 구현합니다.

예:
```java
public class CustomerModel extends RepresentationModel<CustomerModel> {
    private Long id;
    private String name;
    private String email;
    
    // Entity로부터 Model을 생성하는 생성자
    public CustomerModel(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
    }
    
    // getter, setter 등
}
```

이렇게 Entity와 Model을 분리함으로써:
1. 데이터베이스 엔티티와 API 응답을 분리할 수 있습니다.
2. 엔티티 구조 변경이 API 응답 구조에 직접적인 영향을 미치지 않습니다.
3. API 응답에만 필요한 링크 정보를 추가할 수 있습니다.

### Spring HATEOAS 주요 클래스

1. **RepresentationModel**: 링크 정보를 포함하는 기본 모델 클래스
2. **EntityModel**: 단일 리소스를 표현하는 모델
3. **CollectionModel**: 리소스 컬렉션을 표현하는 모델
4. **PagedModel**: 페이지네이션된 리소스 컬렉션을 표현하는 모델
5. **Link**: 하이퍼미디어 링크를 나타내는 클래스
6. **WebMvcLinkBuilder**: 컨트롤러 메소드를 기반으로 링크를 생성하는 유틸리티

## 대표 예제

간단한 고객 관리 API를 통해 Spring HATEOAS 사용법을 알아보겠습니다.

### 1. Entity 정의

```java
@Entity
public class Customer {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String address;
    
    // 생성자, getter, setter 등
}
```

### 2. Model 정의

```java
public class CustomerModel extends RepresentationModel<CustomerModel> {
    private Long id;
    private String name;
    private String email;
    
    public CustomerModel(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
    }
    
    // getter, setter 등
}
```

### 3. 컨트롤러 구현

```java
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    private final CustomerRepository customerRepository;
    
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @GetMapping
    public CollectionModel<EntityModel<CustomerModel>> getAllCustomers() {
        List<EntityModel<CustomerModel>> customers = customerRepository.findAll().stream()
            .map(customer -> new CustomerModel(customer))
            .map(customerModel -> EntityModel.of(customerModel, 
                linkTo(methodOn(CustomerController.class).getCustomer(customerModel.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers")))
            .collect(Collectors.toList());
            
        return CollectionModel.of(customers, 
            linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
    }
    
    @GetMapping("/{id}")
    public EntityModel<CustomerModel> getCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
            
        CustomerModel customerModel = new CustomerModel(customer);
        
        return EntityModel.of(customerModel,
            linkTo(methodOn(CustomerController.class).getCustomer(id)).withSelfRel(),
            linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"),
            linkTo(methodOn(OrderController.class).getOrdersByCustomer(id)).withRel("orders"));
    }
    
    @PostMapping
    public ResponseEntity<EntityModel<CustomerModel>> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        CustomerModel customerModel = new CustomerModel(savedCustomer);
        
        EntityModel<CustomerModel> resource = EntityModel.of(customerModel,
            linkTo(methodOn(CustomerController.class).getCustomer(savedCustomer.getId())).withSelfRel(),
            linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"));
            
        return ResponseEntity
            .created(linkTo(methodOn(CustomerController.class).getCustomer(savedCustomer.getId())).toUri())
            .body(resource);
    }
    
    // 다른 엔드포인트들...
}
```

### 4. 응답 예시

```json
{
  "id": 1,
  "name": "홍길동",
  "email": "hong@example.com",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/customers/1"
    },
    "customers": {
      "href": "http://localhost:8080/api/customers"
    },
    "orders": {
      "href": "http://localhost:8080/api/customers/1/orders"
    }
  }
}
```

이 응답에는 다음과 같은 링크가 포함되어 있습니다:
- `self`: 현재 고객 정보를 다시 조회할 수 있는 링크
- `customers`: 모든 고객 목록을 조회할 수 있는 링크
- `orders`: 이 고객의 주문 목록을 조회할 수 있는 링크

클라이언트는 이러한 링크를 따라가며 API를 탐색할 수 있습니다.

## 결론

Spring HATEOAS는 REST API를 보다 성숙한 수준으로 구현할 수 있게 해주는 강력한 도구입니다. HATEOAS를 통해 클라이언트는 API의 구조에 덜 의존적이 되고, 서버는 API를 보다 유연하게 발전시킬 수 있습니다.

하지만 HATEOAS는 모든 상황에 필요한 것은 아닙니다. 간단한 CRUD 작업만 필요한 내부 API나, 모바일 앱과 같이 대역폭 사용을 최소화해야 하는 경우에는 HATEOAS의 오버헤드가 단점이 될 수 있습니다.

따라서 프로젝트의 특성과 요구사항을 고려하여 HATEOAS 적용 여부를 결정하는 것이 중요합니다. 복잡한 비즈니스 프로세스, 공개 API, 장기적으로 확장될 API에서는 HATEOAS의 이점을 충분히 활용할 수 있습니다.