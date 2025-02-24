package com.kjh.wsd.saramIn_crawling.auth.exception;

/**
 * 사용자 이름을 찾을 수 없는 경우 발생하는 예외 클래스
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}