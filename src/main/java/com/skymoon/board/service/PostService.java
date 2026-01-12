package com.skymoon.board.service;

import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import com.skymoon.board.dto.response.PostResponse;
import com.skymoon.board.repository.MemberRepository;
import com.skymoon.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로는 조회 모드 (성능 최적화)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public Long writePost(Long memberId, String title, String content) {
        // 1. 작성자가 있는지 확인, 없다면 에러 처리
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. 게시글 생성 (Builder 패턴 활용)
        Post post = Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .build();

        // 3. 저장 및 ID 반환
        postRepository.save(post);
        return post.getId();
    }

    /**
     * 게시글 단건 조회
     */
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public void updatePost(Long postId, String title, String content, Member loginMember) {
        // 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        // 검증
        validateAuthor(post, loginMember);

        // 값 변경
        post.update(title, content);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(Long postId, Member loginMember) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        validateAuthor(post, loginMember);

        postRepository.delete(post);
    }

    /**
     * 🔒 작성자 검증 내부 메서드 (중복 제거)
     */
    private void validateAuthor(Post post, Member loginMember) {
        Long postAuthorId = post.getMember().getId();
        Long loginMemberId = loginMember.getId();

        // ID가 다르면 예외 발생!
        if (!postAuthorId.equals(loginMemberId)) {
            throw new IllegalStateException("작성자만 수정/삭제할 수 있습니다.");
        }
    }

    /**
     * 게시글 목록 조회 (검색 + 페이징)
     */
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostList(String keyword, Pageable pageable) {
        Page<Post> posts;

        if (keyword == null || keyword.trim().isEmpty()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByTitleContaining(keyword, pageable);
        }

        return posts.map(PostResponse::new);
    }
}
