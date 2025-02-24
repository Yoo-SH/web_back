package com.kjh.wsd.saramIn_crawling.job.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * 채용 공고 엔티티
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "채용 공고 엔티티")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "채용 공고 ID", example = "101")
    private Long id;

    @Schema(description = "채용 공고 제목", example = "개발자")
    private String title;

    @Schema(description = "회사 이름", example = "Tech Corp")
    private String company;

    @Schema(description = "채용 공고 URL", example = "https://example.com/job/101")
    private String url;

    @Schema(description = "마감일", example = "12/31")
    private String deadline;

    @Schema(description = "근무 위치", example = "서울")
    private String location;

    @Schema(description = "요구 경력", example = "경력, 신입")
    private String experience;

    @Schema(description = "요구 사항", example = "Java, Python")
    private String requirements;

    @Schema(description = "고용 형태", example = "계약직, 정규직")
    @Column(name = "employment_type") // 정확한 컬럼 이름
    private String employment_type;


    @Schema(description = "급여 정보", example = "3000")
    private String salary;

    @Schema(description = "산업 분야", example = "개발자")
    private String sector;

    @Schema(description = "조회수", example = "100")
    private int views;
}
