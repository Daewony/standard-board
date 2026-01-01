package com.skymoon.board.repository;

import com.skymoon.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository<누구, PK타입>을 상속받으면,
// 기본적인 CRUD(저장, 조회, 삭제) 기능이 자동으로 다 생깁니다. (마법!)
public interface MemberRepository extends JpaRepository<Member, Long> {

    // "이메일로 회원 찾기" 기능은 기본제공이 아니라서 직접 한 줄 써줘야 합니다.
    // Optional은 "찾았는데 없을 수도 있다(Null 방지)"는 뜻입니다. (면접 필수 질문)
    Optional<Member> findByEmail(String email);

    // "닉네임이 이미 있는지 확인하기" (중복 가입 방지용)
    boolean existsByEmail(String email);
}

