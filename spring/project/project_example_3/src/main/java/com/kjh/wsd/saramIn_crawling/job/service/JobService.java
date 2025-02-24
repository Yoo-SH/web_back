package com.kjh.wsd.saramIn_crawling.job.service;

import com.kjh.wsd.saramIn_crawling.auth.security.JwtUtil;
import com.kjh.wsd.saramIn_crawling.job.model.Job;
import com.kjh.wsd.saramIn_crawling.job.repository.JobRepository;
import com.kjh.wsd.saramIn_crawling.job.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * 채용 공고 관련 비즈니스 로직 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository repository;
    private final JwtUtil jwtUtil;

    /**
     * 채용 공고 목록을 조회합니다.
     *
     * @param keyword 검색 키워드
     * @param location 근무 위치 필터
     * @param experience 요구 경력 필터
     * @param salary 급여 필터
     * @param pageable 페이징 정보
     * @return 필터링된 채용 공고 목록
     */
    public Page<Job> getJobs(String keyword, String location, String experience, String salary, Pageable pageable) {
        Specification<Job> spec = Specification.where(null);

        if (keyword != null && !keyword.trim().isEmpty()) {
            String trimmedKeyword = keyword.trim();
            spec = spec.and(
                    Specification.where(JobSpecification.containsTitle(trimmedKeyword))
                            .or(JobSpecification.containsCompany(trimmedKeyword))
                            .or(JobSpecification.containsLocation(trimmedKeyword))
                            .or(JobSpecification.containsExperience(trimmedKeyword))
                            .or(JobSpecification.containsSalary(trimmedKeyword))
                            .or(JobSpecification.containsEmployment_type(trimmedKeyword))
                            .or(JobSpecification.containsRequirements(trimmedKeyword))
                            .or(JobSpecification.containsSector(trimmedKeyword))
            );
        }

        if (location != null && !location.trim().isEmpty()) {
            spec = spec.and(JobSpecification.containsLocation(location.trim()));
        }

        if (experience != null && !experience.trim().isEmpty()) {
            spec = spec.and(JobSpecification.containsExperience(experience.trim()));
        }

        if (salary != null && !salary.trim().isEmpty()) {
            spec = spec.and(JobSpecification.containsSalary(salary.trim()));
        }

        return repository.findAll(spec, pageable);
    }

    /**
     * 특정 ID의 채용 공고를 조회합니다.
     * 조회 시 조회수를 1 증가시킵니다.
     *
     * @param id 채용 공고 ID
     * @return 조회된 채용 공고
     * @throws IllegalArgumentException ID에 해당하는 채용 공고가 없을 경우 예외 발생
     */
    public Job getJobById(Long id) {
        Job job = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.setViews(job.getViews() + 1);
        repository.save(job);
        return job;
    }

    /**
     * 토큰 유효성을 검증합니다.
     *
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean isTokenValid(String token) {
        return jwtUtil.validateToken(token);
    }
}
