package com.skymoon.board.service;

import com.skymoon.board.common.EntityStatus;
import com.skymoon.board.domain.Comment;
import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import com.skymoon.board.dto.CommentForm;
import com.skymoon.board.dto.response.CommentResponse;
import com.skymoon.board.repository.CommentRepository;
import com.skymoon.board.repository.MemberRepository;
import com.skymoon.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(Long postId, Pageable pageable) {
        // 1. 게시글이 진짜 있는지 확인 (없으면 에러)
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 2. DB에서 '활성 상태'인 댓글만 가져오기
        Page<Comment> comments = commentRepository.findByPostIdAndStatus(postId, EntityStatus.ACTIVE, pageable);

        // 3. 엔티티(Comment) 목록을 DTO(CommentResponse) 목록으로 변환
        return comments.map(CommentResponse::new);
    }

    @Transactional
    public void writeComment(Long memberId, Long postId, String content) {
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
    }

    @Transactional
    public void updateComment(Long commentId, CommentForm form, Long memberId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        // 댓글 작성자가 맞는지 검증
        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        // 댓글 수정
        comment.update(form.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        // 댓글 작성자 검증
        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        // 댓글 삭제(소프트 삭제)
        comment.delete();
    }

}
