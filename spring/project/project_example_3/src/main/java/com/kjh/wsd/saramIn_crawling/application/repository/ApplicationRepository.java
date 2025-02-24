package com.kjh.wsd.saramIn_crawling.application.repository;

import com.kjh.wsd.saramIn_crawling.application.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // 중복 지원 확인
    boolean existsByUniqueKey(String uniqueKey);

    // 사용자별 지원 내역 조회
    Page<Application> findByUsername(String username, Pageable pageable);
}


