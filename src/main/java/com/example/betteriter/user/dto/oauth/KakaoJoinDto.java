package com.example.betteriter.user.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class KakaoJoinDto {
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{1,10}$",
            message = "닉네임은 특수문자 없이 1 ~ 10 글자로 설정 해주세요.")
    private String nickname;

    @NotNull(message = "직업을 선택해야 합니다.")
    private int job;

    @NotBlank(message = "올바른 관심사 입력 형식이 아닙니다.")
    private String interests;
}
