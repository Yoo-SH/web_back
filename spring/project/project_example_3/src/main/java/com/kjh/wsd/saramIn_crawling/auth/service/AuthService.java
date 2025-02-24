package com.kjh.wsd.saramIn_crawling.auth.service;

import com.kjh.wsd.saramIn_crawling.auth.dto.*;
import com.kjh.wsd.saramIn_crawling.auth.exception.*;
import com.kjh.wsd.saramIn_crawling.auth.security.JwtUtil;
import com.kjh.wsd.saramIn_crawling.user.model.User;
import com.kjh.wsd.saramIn_crawling.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 인증 서비스 클래스
 * 회원가입, 로그인, 리프레시 토큰 처리, 프로필 업데이트 등의 기능을 제공합니다.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * AuthService 생성자
     *
     * @param userRepository  사용자 정보 저장소
     * @param passwordEncoder 비밀번호 암호화 도구
     * @param jwtUtil         JWT 유틸리티 클래스
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 회원가입 메서드
     * 새로운 사용자를 등록합니다.
     *
     * @param request 회원가입 요청 데이터 (사용자명 및 비밀번호)
     * @throws RuntimeException 사용자 이름이 이미 존재하는 경우 발생
     */
    public void registerUser(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    /**
     * 로그인 메서드
     * 사용자 이름과 비밀번호를 검증하고 JWT를 생성하여 쿠키에 저장합니다.
     *
     * @param username 사용자 이름
     * @param password 사용자 비밀번호
     * @param response HTTP 응답 객체 (쿠키 저장용)
     * @throws UserNotFoundException 사용자 이름이 잘못된 경우 발생
     * @throws InvalidCredentialsException 비밀번호가 잘못된 경우 발생
     */
    public void loginUser(String username, String password, HttpServletResponse response) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Invalid username"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String accessToken = jwtUtil.generateToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        addCookie(response, "ACCESS_TOKEN", accessToken, jwtUtil.getAccessTokenExpiry());
        addCookie(response, "REFRESH_TOKEN", refreshToken, jwtUtil.getRefreshTokenExpiry());
    }

    /**
     * 리프레시 토큰 처리 메서드
     * 유효한 리프레시 토큰으로 새로운 액세스 토큰을 생성합니다.
     *
     * @param refreshToken 리프레시 토큰
     * @param response HTTP 응답 객체 (쿠키 저장용)
     * @throws InvalidCredentialsException 토큰이 유효하지 않거나 만료된 경우 발생
     * @throws UserNotFoundException 사용자 정보가 없는 경우 발생
     */
    public void refreshAccessToken(String refreshToken, HttpServletResponse response) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidCredentialsException("Invalid or expired refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newAccessToken = jwtUtil.generateToken(username);

        addCookie(response, "ACCESS_TOKEN", newAccessToken, jwtUtil.getAccessTokenExpiry());
    }

    /**
     * 프로필 업데이트 메서드
     * 사용자 비밀번호를 업데이트합니다.
     *
     * @param updateRequest 비밀번호 업데이트 요청 데이터
     * @param accessToken 액세스 토큰 (유효성 검사에 사용)
     * @throws InvalidCredentialsException 액세스 토큰이 유효하지 않은 경우 발생
     * @throws UserNotFoundException 사용자 정보가 없는 경우 발생
     */
    public void updateProfile(ProfileUpdateRequest updateRequest, String accessToken) {
        if (!jwtUtil.validateToken(accessToken)) {
            throw new InvalidCredentialsException("Invalid or expired access token");
        }

        String username = jwtUtil.extractUsername(accessToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (updateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        userRepository.save(user);
    }

    /**
     * 쿠키 생성 메서드
     * 쿠키를 생성하여 응답 객체에 추가합니다.
     *
     * @param response HTTP 응답 객체
     * @param name 쿠키 이름
     * @param value 쿠키 값
     * @param expiry 쿠키 만료 시간 (초 단위)
     */
    private void addCookie(HttpServletResponse response, String name, String value, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }
}
