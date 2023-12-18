package com.example.betteriter.fo_domain.user.dto.oauth;

import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoJoinDto {
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{1,10}$",
            message = "닉네임은 특수문자 없이 1 ~ 10 글자로 설정 해주세요.")
    private String nickname;

    @NotNull(message = "직업을 선택해야 합니다.")
    private Job job;

    @Size(max = 3, message = "관심사는 최대 3개 까지만 선택 가능합니다.")
    @NotNull(message = "올바른 관심사 입력 형식이 아닙니다.")
    private List<Category> categories;

    @Builder
    public KakaoJoinDto(String nickname, Job job, List<Category> categories) {
        this.nickname = nickname;
        this.job = job;
        this.categories = categories;
    }
}
