package com.kjh.wsd.saramIn_crawling.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 정보를 나타내는 엔티티 클래스
 */
@Entity
@Data
@Getter
@Setter
@Table(name = "users")
@Schema(description = "사용자 엔티티")
public class User {

    /**
     * 사용자 ID (기본 키)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    /**
     * 사용자 이름
     * - 고유한 값이어야 하며 중복될 수 없음
     */
    @Column(nullable = false, unique = true)
    @Schema(description = "사용자 이름", example = "user1")
    private String username;

    /**
     * 사용자 비밀번호 (암호화된 값으로 저장됨)
     */
    @Column(nullable = false)
    @Schema(description = "비밀번호 (암호화)", example = "$2a$10$CjF2/...")
    private String password;
}
