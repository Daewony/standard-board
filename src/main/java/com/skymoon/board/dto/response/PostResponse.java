package com.skymoon.board.dto.response;

import com.skymoon.board.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String writer;      // 작성자 닉네임
    private Long viewCount;     // 조회수
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

        if (post.getMember() != null) {
            this.writer = post.getMember().getNickname();
        } else {
            this.writer = "(알 수 없음)";
        }

        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
