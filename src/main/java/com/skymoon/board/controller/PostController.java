package com.skymoon.board.controller;

import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import com.skymoon.board.dto.PostForm;
import com.skymoon.board.dto.response.PostResponse;
import com.skymoon.board.repository.PostRepository;
import com.skymoon.board.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;

    @PostMapping("/posts")
    public String createPost(@RequestBody @Valid PostForm form, HttpServletRequest request) {
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
    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
//
//        // ✨ 여기서 지연 로딩(LAZY)이 발동합니다!
//        // post.getMember() 하는 순간에는 프록시(가짜)였다가,
//        // .getNickname() 하는 순간 DB 조회 쿼리가 나갑니다.
//        String authorName = post.getMember().getNickname();

        return postService.getPost(postId);
    }

    // 게시글 전제 조회 (V1: 성능 최적화 안 된 버전)
    @GetMapping("/v1/posts")
    public List<String> getAllPostsV1() {
        // 1. 모든 글을 가져오기
        List<Post> posts = postRepository.findAll();

        List<String> results = new ArrayList<>();

        for (Post post : posts) {
            // 2. 여기서 문제가 터집니다! (N+1 문제 발생)
            // 작성자(Member)는 LAZY 로딩이라 프록시(가짜) 상태인데,
            // .getNickname()을 하는 순간 DB에 "작성자 이름 가져와!" 하고 쿼리를 또 날립니다.
            // 글이 10개면 여기서 쿼리가 10번 더 나갑니다.
            results.add("제목: " + post.getTitle() + ", 작성자: " + post.getMember().getNickname());
        }

        return results;
    }

    // 게시글 전체 조회 (V2: Fetch Join 적용!)
    @GetMapping("/v2/posts")
    public List<String> getAllPostsV2() {
        List<Post> posts = postRepository.findAllWithMember();

        List<String> results = new ArrayList<>();

        for (Post post : posts) {
            results.add("제목: " + post.getTitle() + ", 작성자: " + post.getMember().getNickname());
        }

        return results;
    }

    // 게시글 수정
    @PutMapping("/posts/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody @Valid PostForm form, HttpServletRequest request) {
        // 로그인 체크
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginMember") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 2. 로그인 멤버 꺼내기
        Member loginMember = (Member) session.getAttribute("loginMember");

        // 3. 서비스 호출 (ID와 변경할 내용, 그리고 누구인지 같이 전달)
        postService.updatePost(postId, form.getTitle(), form.getContent(), loginMember);

        return "게시글 수정 완료!";
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable Long postId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginMember") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Member loginMember = (Member) session.getAttribute("loginMember");

        postService.deletePost(postId, loginMember);

        return "게시글 삭제 완료!";
    }

    // 게시글 목록 조회 API
    @GetMapping("/posts")
    public Page<PostResponse> getList(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return postService.getPostList(keyword, pageable);
    }
}
