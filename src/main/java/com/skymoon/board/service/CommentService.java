package com.skymoon.board.service;

import com.skymoon.board.domain.Comment;
import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import com.skymoon.board.repository.CommentRepository;
import com.skymoon.board.repository.MemberRepository;
import com.skymoon.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long writeComment(Long memberId, Long postId, String content) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));


        // 댓글 생성
        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .post(post)
                .build();

        // 저장
        commentRepository.save(comment);

        return comment.getId();

    }
}
