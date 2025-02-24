package com.kjh.wsd.saramIn_crawling.auth.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 비밀번호 업데이트 요청 데이터를 담는 클래스
 */
@Getter
@Setter
public class ProfileUpdateRequest {
    /**
     * 새 비밀번호
     */
    private String password;
}
