package com.kjh.wsd.saramIn_crawling.job.controller;

import com.kjh.wsd.saramIn_crawling.job.model.Job;
import com.kjh.wsd.saramIn_crawling.job.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 채용 공고 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService service;

    /**
     * 채용 공고 목록을 조회합니다.
     *
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param location 근무 위치 필터
     * @param keyword 검색 키워드 필터
     * @param experience 요구 경력 필터
     * @param salary 급여 필터
     * @param token 인증 토큰 (쿠키에서 추출)
     * @return 페이징된 채용 공고 목록
     */
    @Operation(summary = "채용 공고 목록 조회", description = "페이지네이션, 필터를 이용해 채용 공고 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<Job>> getJobs(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "근무 위치 필터", example = "서울")
            @RequestParam(required = false, defaultValue = "") String location,

            @Parameter(description = "키워드 필터", example = "Java")
            @RequestParam(required = false, defaultValue = "") String keyword,

            @Parameter(description = "요구 경력 필터", example = "경력, 신입")
            @RequestParam(required = false, defaultValue = "") String experience,

            @Parameter(description = "급여 필터", example = "4500, 정보없음")
            @RequestParam(required = false, defaultValue = "") String salary,

            @CookieValue(name = "ACCESS_TOKEN", required = false) String token
    ) {
        if (token == null || !service.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PageRequest pageable = PageRequest.of(page, size);
        Page<Job> jobs = service.getJobs(keyword, location, experience, salary, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return new ResponseEntity<>(jobs, headers, HttpStatus.OK);
    }

    /**
     * 특정 채용 공고를 ID로 조회합니다.
     *
     * @param id 채용 공고 ID
     * @param token 인증 토큰 (쿠키에서 추출)
     * @return 채용 공고 상세 정보
     */
    @Operation(summary = "채용 공고 상세 조회", description = "ID를 이용해 특정 채용 공고의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(
            @Parameter(description = "채용 공고 ID", example = "101") @PathVariable Long id,
            @CookieValue(name = "ACCESS_TOKEN", required = false) String token
    ) {
        if (token == null || !service.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Job job = service.getJobById(id);
        return ResponseEntity.ok(job);
    }
}
