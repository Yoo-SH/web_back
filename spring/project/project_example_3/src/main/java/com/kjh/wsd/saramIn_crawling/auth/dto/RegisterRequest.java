package com.kjh.wsd.saramIn_crawling.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청 데이터를 담는 클래스
 */
@Getter
@Setter
public class RegisterRequest {
    /**
     * 사용자 이름
     */
    private String username;

    /**
     * 사용자 비밀번호
     */
    private String password;
}
