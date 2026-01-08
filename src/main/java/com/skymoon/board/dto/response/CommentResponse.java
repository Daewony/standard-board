package com.skymoon.board.dto.response;

import com.skymoon.board.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private Long commentId;
    private String content;
    private String nickname; // 작성자 닉네임
    private LocalDateTime createdAt; // 작성일

    // 엔티티를 받아서 DTO로 변환하는 생성자
    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        
        if (comment.getMember() != null) {
            this.nickname = comment.getMember().getNickname();
        } else {
            this.nickname = "(알 수 없음)";
        }

        this.createdAt = comment.getCreatedAt();
    }
}
