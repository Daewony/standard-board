package com.skymoon.board.controller;

import com.skymoon.board.domain.Member;
import com.skymoon.board.dto.CommentForm;
import com.skymoon.board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public String writeComment(@PathVariable Long postId, @RequestBody CommentForm form, HttpServletRequest request) {
        // 1. 로그인 체크
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginMember") == null) {
            throw new IllegalStateException("로그인이 필요합니다");
        }

        Member loginMember = (Member) session.getAttribute("loginMember");

        commentService.writeComment(loginMember.getId(), postId, form.getContent());

        return "댓글 작성 성공!";
    }
}
