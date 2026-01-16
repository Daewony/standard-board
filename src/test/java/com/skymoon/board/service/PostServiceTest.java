package com.skymoon.board.service;

import com.skymoon.board.domain.Member;
import com.skymoon.board.domain.Post;
import com.skymoon.board.dto.response.PostResponse;
import com.skymoon.board.repository.MemberRepository;
import com.skymoon.board.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Transactional // 테스트 끝나면 데이터 깔끔하게 롤백!
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("게시글 작성 성공 테스트")
    void writePost() {
        // 1. Given (준비: 회원 정보)
        Member member = Member.builder()
                .email("test@test.com")
                .password("1234")
                .nickname("테스터")
                .build();
        memberRepository.save(member);

        // 2. When (실행: 게시글 작성 서비스 호출)
        Long postId = postService.writePost(member.getId(), "테스트 제목", "테스트 내용입니다.");

        // 3. Then (검증: 잘 저장됐는지 확인)
        Post findPost = postRepository.findById(postId).get(); // 저장된 글 꺼내오기

        assertThat(findPost.getTitle()).isEqualTo("테스트 제목");
        assertThat(findPost.getContent()).isEqualTo("테스트 내용입니다.");
        assertThat(findPost.getMember().getNickname()).isEqualTo("테스터");
    }

    @Test
    @DisplayName("게시글 단건 조회 성공")
    void getPost() {
        // 1. Given (준비: 회원 & 게시글 미리 저장)
        Member member = Member.builder()
                .email("read@test.com")
                .password("1234")
                .nickname("독서가")
                .build();
        memberRepository.save(member);

        Long postId = postService.writePost(member.getId(), "읽기 전용", "내용");

        // 2. When (실행: 조회 서비스 호출)
        PostResponse findPost = postService.getPost(postId);

        // 3. Then (검증)
        assertThat(findPost.getTitle()).isEqualTo("읽기 전용");
        assertThat(findPost.getContent()).isEqualTo("내용");
    }
}