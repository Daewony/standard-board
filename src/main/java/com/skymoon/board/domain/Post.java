package com.skymoon.board.domain;

import com.skymoon.board.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Long viewCount;

    // N:1 관계 설정 (가장 중요!)
    // FetchType.LAZY : 필요할 때만 멤버 정보를 가져온다 (성능 최적화 필수 질문
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성자 대신 Builder 패턴 사용
    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.viewCount = 0L;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
