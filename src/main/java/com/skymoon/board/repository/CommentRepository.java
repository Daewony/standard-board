package com.skymoon.board.repository;

import com.skymoon.board.common.EntityStatus;
import com.skymoon.board.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 나중에 "특정 게시글의 댓글 목록"을 가져올 때 필요함
    // List<Comment> findByPostId(Long postId);

    // 특정 게시글의 댓글 중 '활성 상태'인 것만 조회
//    List<Comment> findByPostIdAndStatus(Long postId, EntityStatus status);

    Page<Comment> findByPostIdAndStatus(Long postId, EntityStatus status, Pageable pageable);
}
