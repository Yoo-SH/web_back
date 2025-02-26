# Spring Security: SecurityContextHolder와 Authentication 객체

## 개요
Spring Security에서 사용자 인증 정보를 관리하는 핵심 요소 중 하나는 `SecurityContextHolder`입니다. 이 객체를 통해 현재 로그인한 사용자의 인증 정보를 조회하고, `Authentication` 객체를 활용하여 사용자 정보를 관리할 수 있습니다.

본 문서는 `SecurityContextHolder`와 `Authentication` 객체의 개념과 사용법에 대해 설명합니다.

---

## 1. SecurityContextHolder란?
`SecurityContextHolder`는 **Spring Security의 보안 컨텍스트를 저장하고 관리하는 역할**을 합니다. 애플리케이션 전반에서 현재 로그인한 사용자의 인증 정보를 제공하며, 내부적으로 `SecurityContext` 객체를 저장하고 있습니다.

### SecurityContextHolder의 주요 특징
- **애플리케이션 어디서든 접근 가능** (ThreadLocal을 기본 저장소로 사용)
- `SecurityContext` 객체를 저장하고 있으며, `SecurityContext` 내부에 `Authentication` 객체를 포함함
- 인증 정보 변경이 필요할 때 직접 수정 가능

### SecurityContextHolder의 저장 전략
기본적으로 `ThreadLocal`을 사용하여 **각 요청마다 별도의 SecurityContext를 유지**합니다. 필요에 따라 `MODE_INHERITABLETHREADLOCAL`, `MODE_GLOBAL` 등의 전략을 설정할 수도 있습니다.

---

## 2. Authentication 객체란?
`Authentication` 객체는 현재 **인증된 사용자 정보**를 담고 있는 객체로, `SecurityContext` 내부에 저장됩니다.

### Authentication 객체의 주요 메서드
| 메서드 | 설명 |
|--------|--------|
| `getPrincipal()` | 인증된 사용자 정보 (`UserDetails` 또는 사용자 객체) |
| `getName()` | 사용자의 아이디 또는 식별값 반환 |
| `getAuthorities()` | 사용자의 권한 목록 반환 |
| `isAuthenticated()` | 사용자의 인증 여부 확인 |
| `getDetails()` | 추가적인 인증 정보 (IP 주소 등) |
| `getCredentials()` | 인증 자격 증명 정보 (보통 비밀번호, 하지만 보안상 보통 null 처리됨) |

### Authentication 객체의 흐름
1. 사용자가 로그인 요청을 보냄
2. Spring Security의 인증 필터가 요청을 가로챔
3. 인증이 성공하면 `Authentication` 객체를 생성하여 `SecurityContext`에 저장
4. 이후 애플리케이션에서 `SecurityContextHolder.getContext().getAuthentication()`을 통해 현재 로그인한 사용자 정보를 가져올 수 있음

---

## 3. SecurityContextHolder와 Authentication 사용 예제
### 현재 로그인한 사용자 정보 조회
```java
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
```

### Controller에서 Authentication 객체 사용
```java
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/me")
    public Map<String, Object> getUserInfo(Authentication authentication) {
        return Map.of(
            "username", authentication.getName(),
            "authorities", authentication.getAuthorities()
        );
    }
}
```

---

## 4. SecurityContextHolder 직접 설정하기
Spring Security는 기본적으로 `Authentication` 객체를 자동으로 설정하지만, 필요에 따라 직접 인증 정보를 설정할 수도 있습니다.

### 인증 정보 수동 설정 예제
```java
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

public class ManualAuthentication {
    public static void authenticateUser() {
        UserDetails user = new User("admin", "password", List.of());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
```

---

## 5. 결론
Spring Security에서 `SecurityContextHolder`와 `Authentication` 객체는 현재 인증된 사용자의 정보를 관리하는 핵심적인 요소입니다.

- `SecurityContextHolder`는 `SecurityContext`를 저장하고 있으며, 현재 인증된 사용자 정보를 관리함
- `Authentication` 객체는 사용자의 아이디, 권한, 추가 정보를 포함하고 있음
- `SecurityContextHolder.getContext().getAuthentication()`을 통해 현재 사용자 정보를 조회 가능
- 필요할 경우 `SecurityContextHolder`를 통해 직접 `Authentication` 객체를 설정할 수도 있음

Spring Security를 활용할 때, 인증된 사용자 정보를 다룰 경우 반드시 `SecurityContextHolder`와 `Authentication` 객체의 역할을 이해하고 적절히 활용하는 것이 중요합니다.

