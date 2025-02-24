package com.kjh.wsd.saramIn_crawling.bookmark.repository;

import com.kjh.wsd.saramIn_crawling.bookmark.model.Bookmark;
import com.kjh.wsd.saramIn_crawling.job.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 북마크 레포지토리
 * 북마크 데이터를 데이터베이스에서 관리합니다.
 */
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    /**
     * 특정 사용자의 북마크 목록을 조회합니다.
     *
     * @param username 사용자 이름
     * @param pageable 페이지 요청 정보
     * @return 사용자의 북마크 목록 (Page 객체)
     */
    @Query("SELECT b FROM Bookmark b WHERE b.username = :username")
    Page<Bookmark> findByUsername(@Param("username") String username, Pageable pageable);

    /**
     * 특정 사용자와 공고 ID로 북마크를 조회합니다.
     *
     * @param username 사용자 이름
     * @param job 채용 공고 객체
     * @return 북마크 객체 (존재하지 않으면 Optional.empty())
     */
    @Query("SELECT b FROM Bookmark b WHERE b.username = :username AND b.job = :job")
    Optional<Bookmark> findByUsernameAndJob(@Param("username") String username, @Param("job") Job job);
}
