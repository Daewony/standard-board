package com.skymoon.board.service;

import com.skymoon.board.domain.Heart;
import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import com.skymoon.board.repository.HeartRepository;
import com.skymoon.board.repository.MemberRepository;
import com.skymoon.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HeartService {
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /**
     * 좋아요 토글 기능
     * - 이미 좋아요가 있으면 -> 삭제 (좋아요 취소) & 좋아요 수 감소
     * - 좋아요가 없으면 -> 생성 (좋아요 등록) & 좋아요 수 증가
     */

    public String toggleHeart(Long memberId, Long postId) {
        // 1. 게시글과 회원조회(없으면 에러)
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Optional<Heart> heartOptional = heartRepository.findByMemberAndPost(member, post);

        if (heartOptional.isPresent()) {
            heartRepository.delete(heartOptional.get());
            post.removeLike();
            return "좋아요 취소";
        } else {
            heartRepository.save(new Heart(member, post));
            post.addLike();
            return "좋아요 등록";
        }

    }

}
