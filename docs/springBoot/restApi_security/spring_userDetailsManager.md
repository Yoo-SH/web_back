# Spring Security UserDetailsManager

## 개요
`UserDetailsManager`는 Spring Security에서 사용자 계정을 관리하는 인터페이스입니다. `UserDetailsService`를 확장하여 사용자 저장, 업데이트, 삭제 등의 기능을 제공합니다.

## 주요 기능
`UserDetailsManager`는 다음과 같은 주요 기능을 제공합니다.
- `createUser(UserDetails user)`: 새로운 사용자 생성
- `updateUser(UserDetails user)`: 기존 사용자 정보 업데이트
- `deleteUser(String username)`: 사용자 삭제
- `changePassword(String oldPassword, String newPassword)`: 비밀번호 변경
- `userExists(String username)`: 사용자가 존재하는지 확인

## 기본 구현체
Spring Security는 `UserDetailsManager`의 여러 기본 구현체를 제공합니다.

### `InMemoryUserDetailsManager`
- 메모리에 사용자 정보를 저장하는 간단한 구현체
- 테스트 및 간단한 애플리케이션에서 유용
- 예제:
  ```java
  UserDetails user = User.withUsername("user")
      .password("password")
      .roles("USER")
      .build();

  UserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(user);
  ```

### `JdbcUserDetailsManager`
- 데이터베이스 기반 사용자 관리
- Spring Security에서 제공하는 기본적인 스키마 사용 가능하지만, 반드시 `users`와 `authorities` 테이블을 사용할 필요는 없음
- 기본적으로 Spring Security는 다음과 같은 테이블 구조를 기대합니다:
  ```sql
  CREATE TABLE users (
      username VARCHAR(50) NOT NULL PRIMARY KEY,
      password VARCHAR(100) NOT NULL,
      enabled BOOLEAN NOT NULL
  );

  CREATE TABLE authorities (
      username VARCHAR(50) NOT NULL,
      authority VARCHAR(50) NOT NULL,
      CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
  );
  ```
- 그러나 SQL 쿼리를 직접 지정하여 맞춤 테이블을 사용할 수도 있습니다:
  ```java
  JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
  manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM custom_users WHERE username = ?");
  manager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM custom_roles WHERE username = ?");
  ```
- `JdbcUserDetailsManager`는 `DataSource`를 주입받아 작동합니다.
- 예제:
  ```java
  @Bean
  public UserDetailsManager userDetailsManager(DataSource dataSource) {
      return new JdbcUserDetailsManager(dataSource);
  }
  ```
- 사용자 추가 예제:
  ```java
  UserDetails user = User.withUsername("dbuser")
      .password("dbpassword")
      .roles("USER")
      .build();

  userDetailsManager.createUser(user);
  ```

### `LdapUserDetailsManager`
- LDAP 기반 사용자 관리
- 엔터프라이즈 환경에서 사용됨

## 사용자 관리 예제
```java
@Autowired
private UserDetailsManager userDetailsManager;

public void createUser() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("newuser")
        .password("password")
        .roles("USER")
        .build();
    userDetailsManager.createUser(user);
}
```

## 결론
`UserDetailsManager`는 Spring Security에서 사용자 계정을 관리하는 강력한 도구입니다.
요구사항에 따라 `InMemoryUserDetailsManager`, `JdbcUserDetailsManager`, `LdapUserDetailsManager` 등을 사용할 수 있으며, `JdbcUserDetailsManager`는 기본 테이블을 따르지 않고 커스텀 쿼리를 활용하여 맞춤형 데이터베이스 구조를 사용할 수도 있습니다.
