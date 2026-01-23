package com.skymoon.board.service;

import com.skymoon.board.domain.Member;
import com.skymoon.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final이 붙은 필드의 생성자를 자동으로 만들어줌 (DI 주입)
@Transactional(readOnly = true) // 기본적으로 조회만 하되, 필요할 때만 쓰기 모드 켬 (성능 최적화)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     *
     * @param member
     * @return
     */
    @Transactional // 여기서는 데이터를 저장(쓰기)해야 하니까 readOnly 해제
    public Long join(Member member) {
        // 1. 중복 회원 검증 (같은 이메일이 있으면 안 됨)
        validateDuplicateMember(member);

        // 2. 비밀번호 암호화 및 세팅 (강대원 님의 로직 반영)
        // member 객체 안에 있는 생(Raw) 비밀번호를 가져와 암호화한 뒤 다시 넣습니다.
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.updatePassword(encodedPassword);

        // 3. 저장
        memberRepository.save(member);

        return member.getId();
    }

    // 중복 확인 로직
    private void validateDuplicateMember(Member member) {
        // 아까 만든 existsByEmail 사용 (이게 참이면 이미 존재하는 회원)
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

}
