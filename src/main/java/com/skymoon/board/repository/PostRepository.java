package com.skymoon.board.repository;

import com.skymoon.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본 저장(save), 조회(findById) 등은 이미 다 들어있습니다.

    @Query("select p from Post p join fetch p.member")
    List<Post> findAllWithMember();

    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}
