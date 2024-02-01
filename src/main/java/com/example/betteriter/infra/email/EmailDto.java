package com.example.betteriter.infra.email;

import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
}
