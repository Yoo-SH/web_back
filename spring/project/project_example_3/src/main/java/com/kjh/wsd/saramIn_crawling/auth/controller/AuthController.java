package com.kjh.wsd.saramIn_crawling.auth.controller;

import com.kjh.wsd.saramIn_crawling.auth.dto.*;
import com.kjh.wsd.saramIn_crawling.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 요청을 처리하는 컨트롤러
 * 회원가입, 로그인, 토큰 갱신, 비밀번호 변경 등을 제공합니다.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * AuthController 생성자
     *
     * @param authService 인증 서비스를 처리하는 비즈니스 로직 클래스
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 회원가입 요청을 처리합니다.
     *
     * @param registerRequest 회원가입 요청 데이터 (사용자 이름 및 비밀번호)
     * @return 성공 메시지 ResponseEntity
     */
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Parameter(description = "회원가입 요청 데이터", required = true)
            @RequestBody RegisterRequest registerRequest
    ) {
        authService.registerUser(registerRequest);
        return ResponseEntity.ok("User registered successfully.");
    }

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param loginRequest 로그인 요청 데이터 (사용자 이름 및 비밀번호)
     * @param response     HTTP 응답 객체 (쿠키에 JWT 저장)
     * @return 성공 메시지 ResponseEntity
     */
    @Operation(summary = "로그인", description = "사용자 이름과 비밀번호로 로그인하고 쿠키에 JWT를 저장합니다.")
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(description = "로그인 요청 데이터", required = true)
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        authService.loginUser(loginRequest.getUsername(), loginRequest.getPassword(), response);
        return ResponseEntity.ok("Login successful.");
    }

    /**
     * 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급받습니다.
     *
     * @param refreshToken 리프레시 토큰 (쿠키에서 전달됨)
     * @param response     HTTP 응답 객체 (새로운 액세스 토큰 쿠키 저장)
     * @return 성공 메시지 ResponseEntity
     */
    @Operation(summary = "리프레시 토큰", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(
            @CookieValue(value = "REFRESH_TOKEN", required = false, defaultValue = "NONE") String refreshToken,
            HttpServletResponse response
    ) {
        System.out.println("Received Refresh Token: " + refreshToken);
        if ("NONE".equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing");
        }
        authService.refreshAccessToken(refreshToken, response);
        return ResponseEntity.ok("Access token refreshed successfully.");
    }

    /**
     * 사용자 비밀번호를 업데이트합니다.
     *
     * @param updateRequest 비밀번호 업데이트 요청 데이터
     * @param accessToken   액세스 토큰 (쿠키에서 전달됨)
     * @return 성공 메시지 ResponseEntity
     */
    @Operation(summary = "비밀번호 변경", description = "현재 사용자의 비밀번호를 업데이트합니다.")
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @RequestBody ProfileUpdateRequest updateRequest,
            @CookieValue(name = "ACCESS_TOKEN", required = false) String accessToken
    ) {
        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: No access token provided.");
        }

        authService.updateProfile(updateRequest, accessToken);
        return ResponseEntity.ok("Profile updated successfully.");
    }
}
