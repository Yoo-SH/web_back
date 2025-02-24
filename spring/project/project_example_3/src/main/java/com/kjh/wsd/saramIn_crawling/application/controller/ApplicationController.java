package com.kjh.wsd.saramIn_crawling.application.controller;

import com.kjh.wsd.saramIn_crawling.application.model.Application;
import com.kjh.wsd.saramIn_crawling.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 지원 관리 컨트롤러
 * 사용자가 지원 기능을 사용할 수 있도록 API를 제공합니다.
 */
@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * 사용자가 특정 공고에 지원합니다.
     *
     * @param jobId 지원하려는 공고의 ID
     * @param token 사용자 인증을 위한 JWT 토큰 (쿠키에서 전달됨)
     * @return 생성된 지원 객체 또는 에러 메시지를 포함한 ResponseEntity
     * @throws HttpStatus.UNAUTHORIZED 토큰이 없거나 유효하지 않은 경우
     * @throws HttpStatus.CONFLICT 이미 지원한 공고일 경우
     * @throws HttpStatus.NOT_FOUND 지원하려는 공고를 찾을 수 없는 경우
     */
    @PostMapping
    @Operation(summary = "지원하기", description = "사용자가 특정 공고에 지원합니다.")
    public ResponseEntity<?> createApplication(
            @RequestParam Long jobId,
            @CookieValue(name = "ACCESS_TOKEN", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Missing token");
        }

        try {
            Application application = applicationService.createApplication(token, jobId);
            return ResponseEntity.status(HttpStatus.CREATED).body(application);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * 사용자의 지원 내역을 조회합니다.
     *
     * @param page  조회할 페이지 번호 (기본값: 0)
     * @param size  페이지당 항목 수 (기본값: 10)
     * @param token 사용자 인증을 위한 JWT 토큰 (쿠키에서 전달됨)
     * @return 사용자의 지원 내역 또는 에러 메시지를 포함한 ResponseEntity
     * @throws HttpStatus.UNAUTHORIZED 토큰이 없거나 유효하지 않은 경우
     * @throws HttpStatus.INTERNAL_SERVER_ERROR 조회 중 오류 발생 시
     */
    @GetMapping
    @Operation(summary = "지원 내역 조회", description = "사용자의 지원 내역을 조회합니다.")
    public ResponseEntity<?> getApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @CookieValue(name = "ACCESS_TOKEN", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Missing token");
        }

        try {
            PageRequest pageable = PageRequest.of(page, size);
            Page<Application> applications = applicationService.getApplications(token, pageable);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching applications.");
        }
    }

    /**
     * 사용자가 특정 지원 내역을 취소합니다.
     *
     * @param id    취소하려는 지원 ID
     * @param token 사용자 인증을 위한 JWT 토큰 (쿠키에서 전달됨)
     * @return 성공 메시지 또는 에러 메시지를 포함한 ResponseEntity
     * @throws HttpStatus.UNAUTHORIZED 토큰이 없거나 유효하지 않은 경우
     * @throws HttpStatus.FORBIDDEN 사용자가 해당 지원을 취소할 권한이 없는 경우
     * @throws HttpStatus.NOT_FOUND 취소하려는 지원 내역을 찾을 수 없는 경우
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "지원 취소", description = "특정 지원을 취소합니다.")
    public ResponseEntity<?> cancelApplication(
            @PathVariable Long id,
            @CookieValue(name = "ACCESS_TOKEN", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Missing token");
        }

        try {
            applicationService.cancelApplication(token, id);
            return ResponseEntity.ok("Application cancelled successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
