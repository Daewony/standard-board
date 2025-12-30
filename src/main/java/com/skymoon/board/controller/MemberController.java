package com.skymoon.board.controller;

import com.skymoon.board.dto.MemberSaveRequestDto;
import com.skymoon.board.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // 1. "나 여기 직원(컨트롤러)이에요!" 명찰 달기
@RequiredArgsConstructor // 2. "매니저(Service) 데려와!" (자동 주입)
public class MemberController {
    private final MemberService memberService;

    // 회원가입 요청을 받는 창구
    // 주소: POST http://localhost:8080/api/members
    @PostMapping("/api/members")
    public Long join(@RequestBody @Valid MemberSaveRequestDto requestDto) {

        // 1. @RequestBody: "JSON으로 온 데이터를 자바 객체(DTO)로 바꿔줘!"
        // 2. @Valid: "DTO에 붙인 딱지(@NotBlank 등) 검사해! 통과 못하면 입구 컷 시켜!"

        // 3. 검사 통과하면? -> DTO를 Entity로 바꿔서 매니저(Service)에게 넘김
        return memberService.join(requestDto.toEntity());
    }
}
