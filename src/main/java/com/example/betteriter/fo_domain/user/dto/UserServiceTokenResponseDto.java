package com.example.betteriter.fo_domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;


/**
 * - oauth 로그인 후 응답 객체
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class UserServiceTokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiredTime;
    @JsonProperty("isExisted")
    private boolean isExisted;
}
