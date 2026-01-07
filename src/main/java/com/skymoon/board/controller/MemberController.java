package com.skymoon.board.controller;

import com.skymoon.board.domain.Member;
import com.skymoon.board.dto.LoginForm;
import com.skymoon.board.dto.MemberSaveRequestDto;
import com.skymoon.board.repository.MemberRepository;
import com.skymoon.board.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController // 1. "나 여기 직원(컨트롤러)이에요!" 명찰 달기
@RequiredArgsConstructor // 2. "매니저(Service) 데려와!" (자동 주입)
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /**
     * 회원가입 요청을 받는 창구
     * 주소: POST http://localhost:8080/api/members
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/members")
    public Long join(@RequestBody @Valid MemberSaveRequestDto requestDto) {

        // 1. @RequestBody: "JSON으로 온 데이터를 자바 객체(DTO)로 바꿔줘!"
        // 2. @Valid: "DTO에 붙인 딱지(@NotBlank 등) 검사해! 통과 못하면 입구 컷 시켜!"

        // 3. 검사 통과하면? -> DTO를 Entity로 바꿔서 매니저(Service)에게 넘김
        return memberService.join(requestDto.toEntity());
    }

    /**
     * 로그인 API
     */

    @PostMapping("/login")
    public String login(@RequestBody LoginForm form, HttpServletRequest request) {
        // 검증
        //  회원 조회
        // 1. 이메일로 회원이 있는지 조회
        Optional<Member> loginMember = memberRepository.findByEmail(form.getEmail());

        //  실패 처리
        // 2. 회원이 없거나, 비밀번호가 틀리면 "실패" 리턴
        if (loginMember.isEmpty() || !loginMember.get().getPassword().equals(form.getPassword())) {
            return "로그인 실패: 아이디 또는 비밀번호가 틀렸습니다.";
        }

        //  성공 처리
        // 3. 로그인 성공 처리 (세션 생성)
        // getSession(): 세션이 있으면 가져오고, 없으면 새로 만듦 (true)
        HttpSession session = request.getSession();

        //  회원 정보 저장
        // 4. 세션에 회원 정보 저장 (이제 서버가 이 사용자를 기억함!)
        // "loginMember"라는 이름표를 붙여서 회원 정보를 보관함
        session.setAttribute("loginMember", loginMember.get());

        // 반환
        return "로그인 성공! (세션 ID: " + session.getId() + ")";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션을 가져와서 (없으면 null 반환)
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate(); // 세션 날려버리기 (사물함 비우기)
        }

        return "로그아웃 성공!";
    }
}
