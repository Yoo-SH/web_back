package com.kjh.wsd.saramIn_crawling.auth.exception;

/**
 * 비밀번호가 잘못된 경우 발생하는 예외 클래스
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}