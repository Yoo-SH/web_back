package com.kjh.wsd.saramIn_crawling.job.repository;

import com.kjh.wsd.saramIn_crawling.job.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 채용 공고 엔티티의 데이터베이스 접근을 담당하는 리포지토리
 */
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

}
