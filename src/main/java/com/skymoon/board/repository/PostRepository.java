package com.skymoon.board.repository;

import com.skymoon.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본 저장(save), 조회(findById) 등은 이미 다 들어있습니다.
}
