package com.kjh.wsd.saramIn_crawling.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 사용자 정의 예외를 처리하는 클래스
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 사용자 이름을 찾을 수 없는 경우 처리
     *
     * @param ex UserNotFoundException 예외 객체
     * @return 에러 메시지 ResponseEntity
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * 비밀번호가 잘못된 경우 처리
     *
     * @param ex InvalidCredentialsException 예외 객체
     * @return 에러 메시지 ResponseEntity
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * 기타 예외 처리
     *
     * @param ex Exception 예외 객체
     * @return 에러 메시지 ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}