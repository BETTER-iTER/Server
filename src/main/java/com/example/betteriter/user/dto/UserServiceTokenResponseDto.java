package com.example.betteriter.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * - oauth 로그인 후 응답 객체
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserServiceTokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private String expiredTime;
    @JsonProperty("isExisted")
    private boolean isExisted;
}
