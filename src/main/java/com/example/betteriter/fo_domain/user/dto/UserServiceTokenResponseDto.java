package com.example.betteriter.fo_domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime expiredTime;
    @JsonProperty("isExisted")
    private boolean isExisted;
}
