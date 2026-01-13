package com.skymoon.board.repository;

import com.skymoon.board.domain.Heart;
import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    // 1. 이미 좋아요를 눌렀는지 확인하기 위한 조회 메서드
    // (누가, 어떤 글에 눌렀니?)
    Optional<Heart> findByMemberAndPost(Member member, Post post);
}
