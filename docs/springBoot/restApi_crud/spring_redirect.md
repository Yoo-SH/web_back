# Spring Controller에서 페이지 리다이렉션 하는 방법

## 리다이렉션이 필요한 경우

Spring Controller에서 리다이렉션을 사용해야 하는 일반적인 상황:

1. 폼 제출 후 중복 제출 방지(POST-REDIRECT-GET 패턴)
2. 인증/인가 처리 후 적절한 페이지로 이동
3. RESTful URL 구조 유지
4. 사용자 작업 완료 후 결과 페이지로 이동
5. 데이터 처리 후 다른 컨트롤러로 제어 이전

## 리다이렉션 방법

### 1. `redirect:` 접두사 사용

가장 일반적인 방법으로, 컨트롤러 메서드에서 문자열 반환 시 "redirect:" 접두사를 사용합니다.

```java
@Controller
public class MyController {
    
    @PostMapping("/process")
    public String processForm() {
        // 폼 처리 로직
        return "redirect:/result";  // /result URL로 리다이렉션
    }
    
    @GetMapping("/result")
    public String showResult() {
        return "resultPage";  // resultPage.jsp 또는 Thymeleaf 템플릿
    }
}
```

### 2. RedirectView 객체 사용

명시적으로 RedirectView 객체를 반환할 수 있습니다.

```java
@Controller
public class MyController {
    
    @PostMapping("/process")
    public RedirectView processForm() {
        // 처리 로직
        return new RedirectView("/result");
    }
}
```

### 3. RedirectAttributes로 파라미터 전달

리다이렉션 시 데이터를 전달해야 하는 경우:

```java
@Controller
public class MyController {
    
    @PostMapping("/process")
    public String processForm(RedirectAttributes redirectAttributes) {
        // 처리 로직
        redirectAttributes.addAttribute("id", 123);  // URL 파라미터로 추가
        redirectAttributes.addFlashAttribute("message", "처리 완료");  // FlashAttribute로 추가
        
        return "redirect:/result";  // /result?id=123 으로 리다이렉션
    }
    
    @GetMapping("/result")
    public String showResult(@RequestParam(required=false) Integer id, Model model) {
        // Flash attribute는 자동으로 모델에 추가됨
        return "resultPage";
    }
}
```

## 대안적 접근 방법

### 1. Forward 사용

서버 내부에서 요청을 전달하는 방식으로, URL이 변경되지 않습니다.

```java
@Controller
public class MyController {
    
    @PostMapping("/process")
    public String processForm() {
        // 처리 로직
        return "forward:/result";  // /result로 포워드
    }
}
```

### 2. ResponseEntity 사용

REST API에서 주로 사용되는 방식:

```java
@Controller
public class MyController {
    
    @PostMapping("/process")
    public ResponseEntity<Void> processForm() {
        // 처리 로직
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/result"));
        
        return new ResponseEntity<>(headers, HttpStatus.FOUND);  // 302 Found
    }
}
```

### 3. AJAX 요청 처리

클라이언트 측에서 리다이렉션을 처리하는 방식:

```java
@RestController
public class MyRestController {
    
    @PostMapping("/api/process")
    public Map<String, String> processForm() {
        // 처리 로직
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", "/result");
        
        return response;
    }
}
```

```javascript
// 클라이언트 측 JavaScript
$.post("/api/process", formData, function(response) {
    if (response.redirectUrl) {
        window.location.href = response.redirectUrl;
    }
});
```

## 주의사항

1. POST-REDIRECT-GET 패턴: POST 요청 후 항상 리다이렉션하여 폼 재제출 방지
2. Flash attributes는 세션에 임시 저장되므로 과도하게 큰 데이터는 피해야 함
3. URL 파라미터로 민감한 정보 전달 금지
4. 순환 리다이렉션 주의