# Spring Boot - Command Bean과 양방향 바인딩

## 개요
Spring Boot에서 **Command Bean**과 **양방향 바인딩**을 활용하여 **Bean Validation 검증**과 **BindingResult를 활용한 오류 표시**까지의 과정에 대해 설명합니다.

---

## 1. Command Bean이란?
**Command Bean**은 컨트롤러에서 요청 데이터를 바인딩하여 사용하기 위한 객체입니다.
Spring MVC에서는 폼 데이터를 객체와 자동으로 매핑하여 편리하게 다룰 수 있도록 도와줍니다.

### **Command Bean의 특징**
- **자동 바인딩 지원**: `@ModelAttribute`를 사용하여 요청 데이터를 객체로 변환
- **유효성 검사(Validation) 적용 가능**: `@Valid` 또는 `@Validated`를 사용하여 검증
- **BindingResult와 함께 활용 가능**: 검증 실패 시 오류 메시지 제공

### **예제 코드**
```java
public class UserForm {
    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Size(min = 6, message = "비밀번호는 최소 6자리 이상이어야 합니다.")
    private String password;

    // Getter & Setter
}
```

---

## 2. 양방향 바인딩(Bidirectional Binding)
양방향 바인딩이란 **폼 데이터**와 **Command Bean**을 서로 동기화하는 방식입니다.

### **양방향 바인딩을 활용한 검증 흐름**
1. 사용자가 폼 데이터를 입력하고 제출
2. `@ModelAttribute`를 사용하여 데이터를 Command Bean에 자동 매핑
3. `@Valid` 또는 `@Validated`를 통해 검증 수행
4. 검증 실패 시 `BindingResult`를 활용하여 오류 메시지를 출력

### **Controller 예제 코드**
```java
@Controller
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "userForm";
    }

    @PostMapping("/submit")
    public String submitForm(@Valid @ModelAttribute("userForm") UserForm userForm,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "userForm"; // 오류가 있을 경우 폼 화면으로 이동
        }
        // 정상 처리 로직 (예: DB 저장)
        return "redirect:/success";
    }
}
```

---

## 3. BindingResult를 활용한 오류 표시
BindingResult는 `@Valid` 혹은 `@Validated`와 함께 사용하여 검증 오류를 감지하고, 이를 뷰에서 출력하는 데 활용됩니다.

### **BindingResult를 이용한 오류 메시지 출력 (Thymeleaf 예제)**
```html
<form th:action="@{/user/submit}" th:object="${userForm}" method="post">
    <label>이름:</label>
    <input type="text" th:field="*{name}"/>
    <span th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></span>

    <label>이메일:</label>
    <input type="text" th:field="*{email}"/>
    <span th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></span>

    <label>비밀번호:</label>
    <input type="password" th:field="*{password}"/>
    <span th:if="${#fields.hasErrors('password')}" th:errors="*{password}"></span>

    <button type="submit">제출</button>
</form>
```

---

## 4. 결론
Spring Boot에서 Command Bean과 양방향 바인딩을 활용하면 **사용자 입력 데이터를 자동 매핑**하고, **검증 및 오류 처리를 손쉽게 구현**할 수 있습니다.

### **핵심 정리**
✔ `@ModelAttribute`를 사용하여 폼 데이터를 Command Bean에 매핑  
✔ `@Valid`와 `BindingResult`를 활용하여 데이터 검증 및 오류 메시지 처리  
✔ Thymeleaf에서 `#fields.hasErrors`를 이용하여 검증 오류 출력

이를 활용하여 더욱 견고한 Spring Boot 웹 애플리케이션을 구축할 수 있습니다! 🚀

