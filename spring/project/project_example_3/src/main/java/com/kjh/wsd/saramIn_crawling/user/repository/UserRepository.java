package com.kjh.wsd.saramIn_crawling.user.repository;

import com.kjh.wsd.saramIn_crawling.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 엔티티를 관리하기 위한 리포지토리 인터페이스
 * - 기본적인 CRUD 기능 제공
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 사용자 이름으로 사용자 정보를 조회
     *
     * @param username 조회할 사용자 이름
     * @return 사용자 정보를 담은 Optional 객체
     */
    Optional<User> findByUsername(String username);
}
