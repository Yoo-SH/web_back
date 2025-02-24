package com.kjh.wsd.saramIn_crawling.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 데이터를 담는 클래스
 */
@Getter
@Setter
public class LoginRequest {
    /**
     * 사용자 이름
     */
    private String username;

    /**
     * 사용자 비밀번호
     */
    private String password;
}
