# Spring에서 BCrypt 사용하기

## 개요
BCrypt는 보안성이 뛰어난 암호 해싱 함수로, Spring Security에서 비밀번호를 안전하게 저장하는 데 널리 사용됩니다. 본 문서에서는 BCrypt의 개념과 Spring Boot에서의 사용법을 설명합니다.

## BCrypt란?
BCrypt는 Blowfish 암호를 기반으로 한 단방향 해싱 함수입니다. 주요 특징은 다음과 같습니다:

- **Salt 포함**: 해시값이 항상 다르게 생성되어 무차별 대입 공격을 방어할 수 있습니다.
- **Stretching 기법**: 연산 비용을 조정할 수 있어 보안성을 높일 수 있습니다.
- **Rainbow Table 공격 방어**: 같은 비밀번호라도 다른 해시값을 생성하므로 사전 공격이 어렵습니다.

## Spring Boot에서 BCrypt 적용하기

### 1. 의존성 추가 (Maven)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2. BCryptPasswordEncoder 빈 등록
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 3. 비밀번호 해싱 및 검증
```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptExample {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 비밀번호 해싱
        String rawPassword = "mypassword";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        
        // 비밀번호 검증
        boolean isMatch = encoder.matches(rawPassword, encodedPassword);
        System.out.println("Password Match: " + isMatch);
    }
}
```

### 4. BCrypt의 강도 조절
BCrypt는 기본적으로 `strength` 값을 조정하여 보안 수준을 높일 수 있습니다. (기본값: 10)
```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // 강도를 12로 설정
```

## 비밀번호 솔팅 (Salting)
BCrypt는 내부적으로 랜덤한 Salt 값을 포함하여 비밀번호를 해싱합니다. Salt가 포함됨으로 인해 같은 비밀번호라도 해싱된 결과가 다르게 생성됩니다. 따라서, **Salt를 별도로 저장할 필요가 없습니다.**

## 비밀번호 변경 기능 구현
사용자가 비밀번호를 변경하는 경우 다음과 같은 절차를 따릅니다:

1. 사용자가 기존 비밀번호를 입력하고 데이터베이스의 해시된 비밀번호와 비교
2. 기존 비밀번호가 일치하면 새 비밀번호를 해싱하여 데이터베이스에 저장

```java
public boolean changePassword(String oldPassword, String newPassword, String storedPasswordHash) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    // 기존 비밀번호 검증
    if (!encoder.matches(oldPassword, storedPasswordHash)) {
        throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
    }
    
    // 새로운 비밀번호 해싱 후 저장
    String newHashedPassword = encoder.encode(newPassword);
    saveNewPasswordToDatabase(newHashedPassword);
    return true;
}
```

## 데이터베이스 고려 사항
BCrypt로 해싱된 비밀번호는 길이가 60자입니다. 따라서, 데이터베이스의 비밀번호 저장 컬럼은 최소한 다음과 같이 설정하는 것이 좋습니다:

```sql
ALTER TABLE users MODIFY password VARCHAR(255) NOT NULL;
```

일반적으로 `VARCHAR(255)`를 사용하면 향후 변경이 필요 없으며, 다양한 해싱 알고리즘으로 변경할 때도 유연하게 대처할 수 있습니다.

## 결론
BCrypt는 Spring Security에서 강력한 보안성을 제공하는 비밀번호 해싱 방식입니다. Salt를 포함하고 있어 보안성이 높으며, 쉽게 구현할 수 있습니다. Spring Boot에서는 `BCryptPasswordEncoder`를 사용하여 안전한 비밀번호 저장 및 검증을 수행할 수 있습니다. 또한, 비밀번호 변경과 데이터베이스 스키마 설정까지 고려하여 더욱 안전한 시스템을 구축할 수 있습니다.
