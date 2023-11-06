package com.example.betteriter.fo_domain.user.dto;

import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.domain.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

import static com.example.betteriter.fo_domain.user.dto.RoleType.ROLE_USER;

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

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{1,10}$",
            message = "닉네임은 특수문자 없이 1 ~ 10 글자로 설정 해주세요.")
    private String nickName;

    @NotNull(message = "직업을 선택해야 합니다.")
    private int job;

    @NotBlank(message = "올바른 관심사 입력 형식이 아닙니다.")
    private String interests;

    public User toUserEntity(String encryptPassword, UserDetail userDetail) {
        return User.builder()
                .email(email)
                .password(encryptPassword)
                .role(ROLE_USER)
                .userDetail(userDetail)
                .build();
    }

    public UserDetail toUserDetailEntity() {
        return UserDetail.builder()
                .job(job)
                .interests(interests)
                .nickName(nickName)
                .build();
    }
}
