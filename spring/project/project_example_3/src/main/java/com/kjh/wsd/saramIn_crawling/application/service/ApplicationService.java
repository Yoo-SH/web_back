package com.kjh.wsd.saramIn_crawling.application.service;

import com.kjh.wsd.saramIn_crawling.application.model.Application;
import com.kjh.wsd.saramIn_crawling.application.repository.ApplicationRepository;
import com.kjh.wsd.saramIn_crawling.auth.security.JwtUtil;
import com.kjh.wsd.saramIn_crawling.job.model.Job;
import com.kjh.wsd.saramIn_crawling.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 지원 관리 서비스
 * 비즈니스 로직 처리를 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final JwtUtil jwtUtil;

    /**
     * 사용자가 특정 공고에 지원합니다.
     *
     * @param token 사용자 인증을 위한 JWT 토큰
     * @param jobId 지원하려는 공고 ID
     * @return 생성된 Application 객체
     * @throws IllegalStateException 동일 공고에 이미 지원한 경우
     * @throws IllegalArgumentException 지원하려는 공고를 찾을 수 없는 경우
     */
    public Application createApplication(String token, Long jobId) {
        String username = jwtUtil.extractUsername(token);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        String uniqueKey = generateUniqueKey(username, jobId);

        if (applicationRepository.existsByUniqueKey(uniqueKey)) {
            throw new IllegalStateException("You have already applied for this job.");
        }

        Application application = Application.builder()
                .username(username)
                .job(job)
                .uniqueKey(uniqueKey)
                .appliedAt(LocalDateTime.now())
                .build();

        return applicationRepository.save(application);
    }

    /**
     * 사용자의 지원 내역을 조회합니다.
     *
     * @param token    사용자 인증을 위한 JWT 토큰
     * @param pageable 페이징 정보
     * @return 사용자의 지원 내역 목록 (페이징 포함)
     */
    public Page<Application> getApplications(String token, Pageable pageable) {
        String username = jwtUtil.extractUsername(token);
        return applicationRepository.findByUsername(username, pageable);
    }

    /**
     * 사용자가 특정 지원 내역을 취소합니다.
     *
     * @param token         사용자 인증을 위한 JWT 토큰
     * @param applicationId 취소하려는 지원 ID
     * @throws IllegalStateException 사용자가 해당 지원을 취소할 권한이 없는 경우
     * @throws IllegalArgumentException 취소하려는 지원 내역을 찾을 수 없는 경우
     */
    public void cancelApplication(String token, Long applicationId) {
        String username = jwtUtil.extractUsername(token);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        if (!application.getUsername().equals(username)) {
            throw new IllegalStateException("Unauthorized to cancel this application");
        }

        applicationRepository.delete(application);
    }

    /**
     * 지원 고유 키를 생성합니다.
     *
     * @param username 사용자 이름
     * @param jobId    지원하려는 공고 ID
     * @return 고유 키 (username + "_" + jobId)
     */
    private String generateUniqueKey(String username, Long jobId) {
        return username + "_" + jobId;
    }
}
