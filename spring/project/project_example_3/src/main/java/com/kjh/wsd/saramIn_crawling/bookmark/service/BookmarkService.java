package com.kjh.wsd.saramIn_crawling.bookmark.service;

import com.kjh.wsd.saramIn_crawling.auth.security.JwtUtil;
import com.kjh.wsd.saramIn_crawling.bookmark.model.Bookmark;
import com.kjh.wsd.saramIn_crawling.bookmark.repository.BookmarkRepository;
import com.kjh.wsd.saramIn_crawling.job.model.Job;
import com.kjh.wsd.saramIn_crawling.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 북마크 서비스
 * 북마크 추가, 삭제 및 조회를 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final JobRepository jobRepository;
    private final JwtUtil jwtUtil;

    /**
     * 북마크 추가 또는 삭제 메서드
     * 특정 공고 ID에 대해 북마크를 추가하거나 제거합니다.
     *
     * @param jobId 공고 ID
     * @param token 사용자 인증 토큰
     * @return "Bookmarked" 또는 "Unbookmarked" 메시지
     * @throws IllegalArgumentException 공고를 찾을 수 없는 경우 발생
     */
    public String toggleBookmark(Long jobId, String token) {
        String username = jwtUtil.extractUsername(token);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUsernameAndJob(username, job);

        if (existingBookmark.isPresent()) {
            bookmarkRepository.delete(existingBookmark.get());
            return "Unbookmarked";
        } else {
            Bookmark bookmark = Bookmark.builder()
                    .job(job)
                    .username(username)
                    .build();
            bookmarkRepository.save(bookmark);
            return "Bookmarked";
        }
    }

    /**
     * 북마크 목록 조회 메서드
     * 인증된 사용자의 북마크 목록을 페이징하여 반환합니다.
     *
     * @param pageable 페이지 요청 정보
     * @param token 사용자 인증 토큰
     * @return 사용자의 북마크 목록 (Page 객체)
     */
    public Page<Bookmark> getBookmarks(Pageable pageable, String token) {
        String username = jwtUtil.extractUsername(token);

        return bookmarkRepository.findByUsername(username, pageable);
    }
}
