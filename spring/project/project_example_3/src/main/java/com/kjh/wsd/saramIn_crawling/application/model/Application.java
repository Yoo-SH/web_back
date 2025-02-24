package com.kjh.wsd.saramIn_crawling.application.model;

import com.kjh.wsd.saramIn_crawling.job.model.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 지원 정보 엔티티 클래스
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    /**
     * 지원 ID (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 지원자의 사용자 이름
     */
    @Column(nullable = false)
    private String username;

    /**
     * 지원한 공고 정보
     */
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    /**
     * 지원 고유 키 (username + jobId)
     */
    @Column(nullable = false, unique = true)
    private String uniqueKey;

    /**
     * 지원한 날짜 및 시간
     */
    @Column(nullable = false)
    private LocalDateTime appliedAt;
}
