package com.kjh.wsd.saramIn_crawling.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 필터 클래스
 * HTTP 요청에서 JWT를 추출하고 인증을 설정합니다.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * JwtFilter 생성자
     *
     * @param jwtUtil JWT 유틸리티 클래스
     */
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * HTTP 요청을 처리하며 JWT 인증을 수행합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 필터 처리 중 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = null;

        // Authorization 헤더에서 JWT 추출
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        // 쿠키에서 ACCESS_TOKEN 추출
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ACCESS_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("Token from Cookie: " + token); // 디버깅용 로그
                    break;
                }
            }
        }

        // 토큰이 유효한 경우 인증 정보 설정
        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("No token provided or token invalid");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 특정 경로를 필터에서 제외할지 결정합니다.
     *
     * @param request HTTP 요청 객체
     * @return 제외 여부 (true인 경우 필터링 제외)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean exclude = path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
        System.out.println("Path: " + path + ", Exclude: " + exclude); // 디버그 로그
        return exclude;
    }
}
