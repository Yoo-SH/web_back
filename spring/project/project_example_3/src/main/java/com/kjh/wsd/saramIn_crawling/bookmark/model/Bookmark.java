package com.kjh.wsd.saramIn_crawling.bookmark.model;

import com.kjh.wsd.saramIn_crawling.job.model.Job;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * 북마크 엔티티
 * 특정 사용자와 공고 간의 북마크 관계를 나타냅니다.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "북마크 엔티티")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "북마크 ID", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @Schema(description = "북마크된 채용 공고", example = "Job 객체")
    private Job job;

    @Column(nullable = false)
    @Schema(description = "북마크한 사용자 이름", example = "user1")
    private String username;
}
