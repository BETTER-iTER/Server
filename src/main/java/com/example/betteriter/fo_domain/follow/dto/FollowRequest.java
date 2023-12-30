package com.example.betteriter.fo_domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class FollowRequest {

    @Getter
    @Builder
    public static class FollowingDto {
        @NotNull
        private Long targetId;
    }

    @Getter
    @Builder
    public static class UnfollowingDto {

        @Email(message = "이메일 형식이 아닙니다.")
        @NotEmpty(message = "이메일은 필수 입력 값입니다.")
        private String email;
    }
}
