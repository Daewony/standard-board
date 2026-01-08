package com.skymoon.board.controller;

import com.skymoon.board.domain.Member;
import com.skymoon.board.dto.CommentForm;
import com.skymoon.board.dto.response.CommentResponse;
import com.skymoon.board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 목록 조회
    // GET /posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public Page<CommentResponse> getComments(
            @PathVariable Long postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return commentService.getComments(postId, pageable);
    }

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

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public String update(@PathVariable Long commentId, @RequestBody CommentForm form, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) return "로그인이 필요합니다.";

        commentService.updateComment(commentId, form, loginMember.getId());
        return "댓글 수정 성공!";
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public String delete(@PathVariable Long commentId, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) return "로그인이 필요합니다.";

        commentService.deleteComment(commentId, loginMember.getId());

        return "댓글 삭제 성공!";
    }

}
