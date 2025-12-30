package com.skymoon.board.service;

import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import com.skymoon.board.repository.MemberRepository;
import com.skymoon.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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
}
