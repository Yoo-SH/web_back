package com.kjh.wsd.saramIn_crawling.bookmark.controller;

import com.kjh.wsd.saramIn_crawling.bookmark.model.Bookmark;
import com.kjh.wsd.saramIn_crawling.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 북마크 컨트롤러
 * 북마크 추가, 삭제 및 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
@Tag(name = "Bookmarks", description = "북마크를 관리하는 API")
public class BookmarkController {

    private final BookmarkService service;

    /**
     * 북마크 추가 또는 삭제 메서드
     * 특정 공고 ID에 대한 북마크를 추가하거나 제거합니다.
     *
     * @param jobId 북마크할 공고 ID
     * @param token 인증 토큰 (쿠키에서 전달됨)
     * @return 성공적으로 북마크를 추가하거나 제거한 결과 메시지
     */
    @PostMapping("/{jobId}")
    @Operation(summary = "북마크 토글", description = "특정 공고 ID에 대해 북마크를 추가하거나 제거합니다.")
    public ResponseEntity<String> toggleBookmark(
            @PathVariable Long jobId,
            @CookieValue(name = "ACCESS_TOKEN", required = false) String token
    ) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: Missing token");
        }

        String result = service.toggleBookmark(jobId, token);
        return ResponseEntity.ok(result);
    }

    /**
     * 북마크 목록 조회 메서드
     * 인증된 사용자의 북마크 목록을 페이징하여 반환합니다.
     *
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param token 인증 토큰 (쿠키에서 전달됨)
     * @return 사용자의 북마크 목록
     */
    @GetMapping
    @Operation(summary = "북마크 목록 조회", description = "사용자의 북마크 목록을 페이징하여 반환합니다.")
    public ResponseEntity<Page<Bookmark>> getBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @CookieValue(name = "ACCESS_TOKEN", required = false) String token
    ) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        PageRequest pageable = PageRequest.of(page, size);
        Page<Bookmark> bookmarks = service.getBookmarks(pageable, token);
        return ResponseEntity.ok(bookmarks);
    }
}
