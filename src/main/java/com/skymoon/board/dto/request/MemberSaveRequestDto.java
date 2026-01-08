package com.skymoon.board.dto.request;

import com.skymoon.board.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSaveRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickname;

    @Builder
    public MemberSaveRequestDto(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    // DTO -> Entity 변환 메서드 (Service에서 편하게 쓰기 위해)
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}

/**
 * 💡 면접관이 코드를 보고 물어볼 포인트 (미리 대비)
 * Q: "왜 Entity(@Entity)를 컨트롤러에서 바로 안 받고 DTO를 따로 만들었나요? 귀찮게..."
 * <p>
 * A:
 * 보안: Entity는 DB와 직결된 핵심 자산입니다. 화면에서 넘어오는 데이터 스펙이 바뀐다고 해서 DB 구조(Entity)까지 흔들리면 안 됩니다.
 * 검증 분리: 회원가입 때는 비밀번호가 필수지만, 회원수정 때는 비밀번호가 없을 수도 있죠? Entity 하나에 검증 로직(@NotBlank 등)을 다 때려 넣으면 상황별 대처가 불가능해집니다. 그래서 화면(Request)에 맞는 DTO를 따로 둡니다.
 */

