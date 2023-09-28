package com.example.betteriter.user.dto;

import com.example.betteriter.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JoinDto {


    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "비밀번호는 영어/숫자/특수문자를 포함해야합니다.")
    private String password;

    @NotBlank(message = "올바른 닉네임 형식이 아닙니다.")
    private String nickName;

    @NotNull(message = "직업을 선택해야 합니다.")
    private int job;

    @Size(min = 1, max = 3, message = "관심사는 1개이상 3개 이하여야 합니다.")
    private List<Integer> interest;

    public User toEntity(String encryptPassword) {
        return User.builder()
                .email(email)
                .password(encryptPassword)
                .nickName(nickName)
                .job(job)
                .interests(interest)
                .build();
    }
}