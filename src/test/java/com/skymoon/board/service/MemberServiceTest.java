package com.skymoon.board.service;

import com.skymoon.board.domain.Member;
import com.skymoon.board.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest // 스프링 컨테이너를 띄워서 테스트 (DB까지 실제 연결)
@Transactional // 중요! 테스트가 끝나면 데이터를 '롤백(지우기)' 해줌 (반복 테스트 가능)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공 / Member Join Success")
    public void join_success() throws Exception {
        // Given (주어지면) : 이런 회원이 있을 때
        Member member = Member.builder()
                .email("test@email.com")
                .password("1234")
                .nickname("skyUser")
                .build();

        // When (실행하면) : 가입을 시키면
        Long savedId = memberService.join(member);

        // Then (검증) : 저장된 ID가 나와야 하고, DB에 있는 정보랑 같아야 함
        Member findMember = memberRepository.findById(savedId).get();

        assertEquals(member.getEmail(), findMember.getEmail());
        assertEquals(member.getNickname(), findMember.getNickname());

        System.out.println("가입된 회원 ID = " + savedId);
    }

    @Test
    @DisplayName("중복 회원 예외 발생 / Duplicate Member Exception")
    public void join_duplicate_exception() throws Exception {
        // Given
        Member member1 = Member.builder().email("same@test.com").password("1111").nickname("user1").build();
        Member member2 = Member.builder().email("same@test.com").password("2222").nickname("user2").build(); // 이메일 같음!

        // When
        memberService.join(member1); // 첫 번째는 성공해야 함

        // 두 번째 가입할 때 IllegalStateException이 터져야 정상!
        // (예외가 안 터지면 테스트 실패임)
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }
}