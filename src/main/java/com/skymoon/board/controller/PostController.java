package com.skymoon.board.controller;

import com.skymoon.board.PostForm;
import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import com.skymoon.board.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostRepository postRepository;

    @PostMapping("/posts")
    public String createPost(@RequestBody PostForm form, HttpServletRequest request) {
        // 1. 세션 가져오기 (없으면 null)
        HttpSession session = request.getSession(false);

        // 2. 로그인 검사 (세션이 없거나, 회원 정보가 없으면 튕겨내기
        if (session == null || session.getAttribute("loginMember") == null) {
            return "글쓰기 실패: 로그인이 필요합니다.";
        }

        // 3. 세션에서 로그인한 회원 정보 꺼내기
        Member loginMember = (Member) session.getAttribute("loginMember");

        // 4. 게시글 생성
        Post post = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .member(loginMember)
                .build();

        // 5. DB 저장
        postRepository.save(post);

        return "글쓰기 성공! (글 ID: " + post.getId() + ")";
    }

    // 게시글 단건 조회 (작성자 닉네임 포함)
    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        // ✨ 여기서 지연 로딩(LAZY)이 발동합니다!
        // post.getMember() 하는 순간에는 프록시(가짜)였다가,
        // .getNickname() 하는 순간 DB 조회 쿼리가 나갑니다.
        String authorName = post.getMember().getNickname();

        return "제목: " + post.getTitle() +
                "\n내용: " + post.getContent() +
                "\n작성자: " + authorName;
    }
}
