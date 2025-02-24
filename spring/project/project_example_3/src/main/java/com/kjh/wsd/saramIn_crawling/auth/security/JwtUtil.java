package com.kjh.wsd.saramIn_crawling.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 유틸리티 클래스
 * JWT 생성, 파싱 및 유효성 검사를 제공합니다.
 */
@Component
public class JwtUtil {

    private static final String SECRET_KEY_STRING = "Your-Very-Strong-Secret-Key-For-JWT"; // 비밀키
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    /**
     * JWT 토큰 생성
     *
     * @param subject JWT의 주체(사용자 이름)
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT에서 사용자 이름 추출
     *
     * @param token JWT 토큰
     * @return 추출된 사용자 이름
     * @throws IllegalArgumentException 토큰이 유효하지 않을 경우 발생
     */
    public String extractUsername(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    /**
     * JWT 유효성 확인 (만료 여부 포함)
     *
     * @param token JWT 토큰
     * @return 유효 여부 (true: 유효, false: 유효하지 않음)
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            boolean expired = isTokenExpired(claims);
            System.out.println("Token Validity: " + !expired + ", Claims: " + claims);
            return !expired;
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    /**
     * JWT의 Claims 정보 추출
     *
     * @param token JWT 토큰
     * @return 추출된 Claims 객체
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 만료 여부 확인
     *
     * @param claims JWT Claims 객체
     * @return 만료 여부 (true: 만료, false: 유효)
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * 액세스 토큰 만료 시간
     *
     * @return 만료 시간 (초 단위)
     */
    public int getAccessTokenExpiry() {
        return 60 * 60;
    }

    /**
     * 리프레시 토큰 만료 시간
     *
     * @return 만료 시간 (초 단위)
     */
    public int getRefreshTokenExpiry() {
        return 60 * 60 * 24 * 7;
    }

    /**
     * 리프레시 토큰 생성
     *
     * @param subject JWT의 주체(사용자 이름)
     * @return 생성된 리프레시 토큰
     */
    public String generateRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + getRefreshTokenExpiry() * 1000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
