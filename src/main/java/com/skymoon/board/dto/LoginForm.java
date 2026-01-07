package com.skymoon.board.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
