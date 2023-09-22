package com.example.betteriter.user.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * - 우리 서비스에서 token 을 나타내는 클래스
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ServiceToken {
    private String tokenValue;
    private Long expiredTime;
}
