package com.example.betteriter.infra;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuthenticationDto {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Pattern(regexp = "^\\d{6}", message = "코드는 정확히 6자리 여야 합니다.")
    private String code;
}
