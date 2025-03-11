# GraphQL 가이드

## 목차
- [GraphQL이란?](#graphql이란)
- [주요 특징](#주요-특징)
- [GraphQL vs REST](#graphql-vs-rest)
- [기본 개념](#기본-개념)
- [GraphQL 서버 구현하기](#graphql-서버-구현하기)
- [GraphQL 클라이언트](#graphql-클라이언트)
- [주요 도구 및 라이브러리](#주요-도구-및-라이브러리)
- [모범 사례](#모범-사례)
- [일반적인 문제 및 해결책](#일반적인-문제-및-해결책)
- [자주 묻는 질문](#자주-묻는-질문)
- [참고 자료](#참고-자료)

## GraphQL이란?

GraphQL은 페이스북에서 2012년에 개발하고 2015년에 공개한 API를 위한 쿼리 언어입니다. GraphQL은 클라이언트가 필요한 데이터를 정확히 요청할 수 있게 해주며, 여러 엔드포인트를 통해 데이터를 가져오는 대신 단일 요청으로 필요한 모든 데이터를 한 번에 가져올 수 있게 해줍니다.



<Image src="https://velog.velcdn.com/images/hyunspace/post/24af9f27-7521-4d44-993c-f533f2711664/image.png" width="500px" />

- 모바일 사용량의 증가로 효율적인 데이터 로딩이 필요(저전력 장치 및 느린 네트워크 문제) => 필요한 데이터만 한번에, 정확하게 요청한다.

- 다양한 플랫폼(플랫폼의 다양화로 인해 모든 요구 사항에 맞는 하나의 API를 구축하고 유지관리하기 어려움) => 엔드포인트를 통합한다.
- 빠른 기능 개발에 대한 기대(지속적 배포는 업계의 표준이 되었고 빠른 반복과 빈번한 제품 배포는 필수적이고 이에따라, 프론트엔드와 백엔드의 의사소통 비용은 증가) => 프론트엔드와 백엔드의 커뮤니케이션을 줄이고 개발에 집중한다.


## 주요 특징

- **클라이언트 중심**: 클라이언트가 필요한 데이터만 요청할 수 있음
- **단일 엔드포인트**: 모든 요청이 단일 URL로 전송됨
- **강력한 타입 시스템**: 명확한 스키마 정의로 타입 안전성 보장
- **자기 문서화**: 내장된 인트로스펙션을 통한 자동 문서화
- **실시간 업데이트**: 구독(Subscription)을 통한 실시간 데이터 처리
- **버전 없음**: API 버전 관리 필요성 감소
- **프론트엔드와 백엔드 분리**: 독립적인 개발 가능

<img src= "https://velog.velcdn.com/images/hyunjine/post/3a9b90c4-71b8-4d16-b016-117c89c80fe8/image.png" width="500px" />

## GraphQL vs REST

| 특징 | GraphQL | REST |
|------|---------|------|
| 엔드포인트 | 단일 | 다중 |
| 데이터 가져오기 | 정확히 필요한 데이터만 | 고정된 데이터 구조 |
| 오버페칭/언더페칭 | 최소화 | 일반적인 문제 |
| 버전 관리 | 필드 단위 진화 | API 버전 관리 필요 |
| 캐싱 | 복잡함 | HTTP 캐싱으로 단순함 |
| 문서화 | 자기 문서화 | 별도 문서화 필요 |
| 학습 곡선 | 중간~높음 | 낮음 |

_REST 엔드포인트_
<img src= "https://velog.velcdn.com/images/hyunjine/post/e70c8ff6-e06d-4fac-9f39-9196d9da3023/image.png" width="500px" />

- REST API에서는 URL이 자원을 나타내기 때문에 세가지 각기 다른 네트워크 요청을 보내야만 자원을 가져올 수 있습니다.


_GraphQL 엔드포인트_
<img src= "https://velog.velcdn.com/images/hyunjine/post/3d8cc27f-dc95-4530-8b1f-c250b4a64a9c/image.png" width="500px" />
- 반면 GraphQL은 이 3개의 네트워크 요청을 단 하나의 요청으로 통합할 수 있습니다.


_REST API 단점_
- `Overfetching.`
 REST API의 특성상 데이터를 주고받을 때 클라이언트에서 활용하지 않는 필요없는 데이터까지 주고받을 확률이 높습니다. 따라서 이는 불필요한 리소스 낭비를 초래합니다.

- `Underfetching.` 필요한 데이터를 만들기 위해 여러번의 API 호출이 필요합니다.

_GraphQL 장점_
- 필요한 데이터만 요청할 수 있습니다.
- 오버페칭과 언더페칭을 최소화합니다.
- 데이터를 중복하여 가져오지 않습니다.


## 기본 개념

### 스키마

GraphQL 스키마는 API의 타입, 필드, 작업을 정의합니다. 스키마 정의 언어(SDL)를 사용하여 작성합니다. 

 스키마는 API가 무엇을 할 수 있는지 기술한 것이기 때문에 GraphQL 클라이언트는 이 스키마를 통해 서비스에 어떻게 요청할지 알 수 있습니다. 스키마는 타입을 가진 필드를 그래프로 나타낸 것이며 이 그래프는 데이터 서비스를 통해 읽고 수정할 수 있는 모든 객체를 보여줍니다.

GraphQL은 프론트엔드 팀과 백엔드 팀이 스키마를 같이 정의하는 것으로 커뮤니케이션 비용을 줄입니다. 프론트엔드 팀과 백엔드 팀이 회의실에 앉아서 스키마에 대해 토의하고 무엇이 필요한지 정의합니다. 서로 스키마를 공유한다면 더이상의 커뮤니케이션은 필요하지 않습니다

```graphql
type User {
  id: ID!
  name: String!
  email: String!
  posts: [Post!]!
}

type Post {
  id: ID!
  title: String!
  content: String!
  author: User!
}

type Query {
  user(id: ID!): User
  users: [User!]!
  post(id: ID!): Post
  posts: [Post!]!
}

type Mutation {
  createUser(name: String!, email: String!): User!
  createPost(title: String!, content: String!, authorId: ID!): Post!
}
```

### 타입 시스템

GraphQL은 다음과 같은 타입들을 제공합니다:

- **스칼라 타입**: `Int`, `Float`, `String`, `Boolean`, `ID`
- **객체 타입**: 사용자 정의 객체 (예: `User`, `Post`)
- **쿼리 및 뮤테이션 타입**: 읽기와 쓰기 작업 정의
- **열거형(Enum)**: 가능한 값의 집합
- **인터페이스**: 여러 타입이 구현할 수 있는 추상 타입
- **유니온 타입**: 여러 타입 중 하나일 수 있는 타입
- **입력 타입**: 뮤테이션에 사용되는 복잡한 객체
- **리스트 타입**: 배열 형태의 데이터
- **논-널(Non-Null) 타입**: `!`로 표시되며 null이 될 수 없음

### 쿼리 및 뮤테이션

**쿼리(Query)**: 데이터 읽기 작업

```graphql
query {
  user(id: "1") {
    id
    name
    posts {
      id
      title
    }
  }
}
```

**뮤테이션(Mutation)**: 데이터 쓰기 작업

```graphql
mutation {
  createPost(
    title: "GraphQL 소개"
    content: "GraphQL은 API를 위한 쿼리 언어입니다."
    authorId: "1"
  ) {
    id
    title
    author {
      name
    }
  }
}
```

**구독(Subscription)**: 실시간 데이터 업데이트

```graphql
subscription {
  newPost {
    id
    title
    author {
      name
    }
  }
}
```

### 리졸버

리졸버는 GraphQL 필드가 값을 반환하는 방법을 정의하는 함수입니다. 각 필드마다 하나의 리졸버가 있습니다.

```javascript
// JavaScript 리졸버 예제
const resolvers = {
  Query: {
    user: (parent, args, context, info) => {
      return context.db.findUserById(args.id);
    },
    users: (parent, args, context, info) => {
      return context.db.findAllUsers();
    }
  },
  User: {
    posts: (parent, args, context, info) => {
      return context.db.findPostsByAuthorId(parent.id);
    }
  }
};
```

## GraphQL 서버 구현하기

### Node.js (Apollo Server)

```javascript
const { ApolloServer, gql } = require('apollo-server');

// 스키마 정의
const typeDefs = gql`
  type User {
    id: ID!
    name: String!
  }
  
  type Query {
    users: [User!]!
    user(id: ID!): User
  }
`;

// 샘플 데이터
const users = [
  { id: '1', name: '홍길동' },
  { id: '2', name: '김철수' }
];

// 리졸버 정의
const resolvers = {
  Query: {
    users: () => users,
    user: (_, { id }) => users.find(user => user.id === id)
  }
};

// 서버 생성
const server = new ApolloServer({ typeDefs, resolvers });

// 서버 시작
server.listen().then(({ url }) => {
  console.log(`🚀 Server ready at ${url}`);
});
```

### Java (Spring Boot)

```java
@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiring runtimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder
                        .dataFetcher("users", new UsersDataFetcher())
                        .dataFetcher("user", new UserDataFetcher()))
                .build();
    }
}

@Component
public class UsersDataFetcher implements DataFetcher<List<User>> {
    @Override
    public List<User> get(DataFetchingEnvironment environment) {
        // 사용자 목록 조회 로직
        return userRepository.findAll();
    }
}
```


## GraphQL 클라이언트

### Apollo Client

```javascript
import { ApolloClient, InMemoryCache, gql } from '@apollo/client';

const client = new ApolloClient({
  uri: 'http://localhost:4000/graphql',
  cache: new InMemoryCache()
});

// 쿼리 실행
client.query({
  query: gql`
    query GetUsers {
      users {
        id
        name
      }
    }
  `
})
.then(result => console.log(result));
```

### React with Apollo Client

```jsx
import { ApolloProvider, useQuery, gql } from '@apollo/client';

const GET_USERS = gql`
  query GetUsers {
    users {
      id
      name
    }
  }
`;

function UserList() {
  const { loading, error, data } = useQuery(GET_USERS);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error.message}</p>;

  return (
    <ul>
      {data.users.map(user => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}

function App() {
  return (
    <ApolloProvider client={client}>
      <div>
        <h2>사용자 목록</h2>
        <UserList />
      </div>
    </ApolloProvider>
  );
}
```


## 주요 도구 및 라이브러리

### 서버 측

- **Apollo Server**: Node.js 기반의 GraphQL 서버
- **Express GraphQL**: Express 미들웨어
- **GraphQL Java**: Java 구현체

### 클라이언트 측

- **Apollo Client**: 완전한 기능을 갖춘 GraphQL 클라이언트
- **urql**: 경량 GraphQL 클라이언트
- **graphql-request**: 간단한 GraphQL 클라이언트
- **Amplify**: AWS 서비스와 통합된 GraphQL 클라이언트

### 개발 도구

- **GraphiQL**: 브라우저 기반 GraphQL IDE
- **Apollo Studio**: API 관리 및 협업 플랫폼
- **GraphQL Playground**: 또 다른 GraphQL IDE
- **GraphQL Voyager**: GraphQL API를 시각적으로 탐색
- **graphql-code-generator**: 타입스크립트 타입 자동 생성

## 모범 사례

### 스키마 설계

1. **객체 타입 중심 설계**: 데이터 모델에 맞게 객체 타입을 설계
2. **명확한 명명 규칙**: 일관된 명명 규칙 사용 (camelCase)
3. **필요한 필드만 노출**: 클라이언트에 필요한 필드만 제공
4. **페이지네이션 고려**: `Connection` 패턴 또는 `limit`/`offset` 사용
5. **중첩 데이터 구조 활용**: GraphQL의 강점인 중첩 데이터 구조 활용

### 성능 최적화

1. **N+1 문제 방지**: DataLoader 패턴 사용
2. **배치 로딩**: 여러 개체를 한 번에 로드
3. **쿼리 복잡성 제한**: 깊이, 중첩, 필드 수 제한
4. **적절한 캐싱**: 클라이언트 및 서버 측 캐싱 구현
5. **쿼리 비용 분석**: 복잡한 쿼리의 실행 비용 계산

```javascript
// DataLoader 사용 예제
const DataLoader = require('dataloader');

const userLoader = new DataLoader(async (ids) => {
  const users = await database.findUsersByIds(ids);
  return ids.map(id => users.find(user => user.id === id));
});
```

## 일반적인 문제 및 해결책

### N+1 쿼리 문제

**문제**: 연결된 객체를 조회할 때 다수의 개별 쿼리 발생
**해결책**: DataLoader 사용으로 배치 로딩 구현

### 인증 및 권한 부여

**문제**: 다양한 접근 수준에 대한 권한 관리
**해결책**: 
- 컨텍스트에 사용자 정보 포함
- 리졸버 내에서 권한 확인
- 지시어(directive) 사용

```graphql
directive @auth(requires: Role = USER) on FIELD_DEFINITION

enum Role {
  USER
  ADMIN
}

type User {
  id: ID!
  publicInfo: String
  privateInfo: String @auth(requires: ADMIN)
}
```

### 오류 처리

**문제**: 복잡한 오류 시나리오 처리
**해결책**: 
- 일관된 오류 형식 정의
- 유니온 타입으로 성공/실패 응답 모델링

```graphql
union UserResult = User | UserError

type UserError {
  code: String!
  message: String!
}

type Mutation {
  createUser(input: CreateUserInput!): UserResult!
}
```

### 파일 업로드

**문제**: GraphQL은 기본적으로 파일 업로드를 지원하지 않음
**해결책**: `graphql-upload` 패키지 사용

```graphql
scalar Upload

type Mutation {
  uploadFile(file: Upload!): File!
}
```

## 자주 묻는 질문

### GraphQL이 REST를 완전히 대체할 수 있나요?
GraphQL과 REST는 상호 보완적일 수 있습니다. GraphQL은 복잡한 데이터 요구 사항에 유리하지만, 단순한 API나 파일 업로드와 같은 특정 사용 사례에서는 REST가 더 적합할 수 있습니다.

### GraphQL은 성능 문제를 일으키지 않나요?
잘 설계된 GraphQL API는 불필요한 데이터 조회를 줄이므로 성능이 향상될 수 있습니다. 그러나 N+1 쿼리 문제와 복잡한 쿼리 처리에 주의해야 합니다.

### GraphQL 서버는 어떻게 캐싱을 구현하나요?
GraphQL은 HTTP 수준 캐싱이 어렵지만, Apollo Client나 Relay와 같은 클라이언트는 ID 기반 캐싱을 제공합니다. 서버 측에서는 DataLoader를 사용한 배치 및 캐싱, Redis와 같은 외부 캐시 시스템을 활용할 수 있습니다.

### 큰 프로젝트에서 GraphQL 스키마를 관리하는 좋은 방법은?
스키마 스티칭(Schema Stitching)이나 페더레이션(Federation)을 사용하여 큰 스키마를 모듈식으로 관리할 수 있습니다. Apollo Federation은 마이크로서비스 아키텍처에서 특히 유용합니다.

### REST API를 GraphQL로 마이그레이션하는 방법은?
점진적 접근 방식을 취하는 것이 좋습니다. GraphQL 레이어를 기존 REST API 위에 구축하고, 기능을 점차 마이그레이션하는 방식으로 진행할 수 있습니다.


# 참고자료 
- [GraphQL](https://velog.io/@hyunjine/GraphQL)