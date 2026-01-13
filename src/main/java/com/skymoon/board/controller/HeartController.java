package com.skymoon.board.controller;

import com.skymoon.board.domain.Member;
import com.skymoon.board.service.HeartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    // 좋아요 토글 API
    // POST /api/posts/{postId}/hearts
    @PostMapping("/api/posts/{postId}/hearts")
    public String toggleHeart(@PathVariable Long postId, HttpSession session) {

        // 1. 로그인 체크 (세션에서 꺼내기)
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "로그인이 필요합니다.";
        }

        // 2. 서비스 호출 (누가, 어떤 글에 눌렀는지 전달)
        String result = heartService.toggleHeart(loginMember.getId(), postId);

        return result; // "좋아요 등록" 또는 "좋아요 취소" 메시지 반환
    }
}
