package com.example.betteriter.user.service;

import com.example.betteriter.global.config.properties.JwtProperties;
import com.example.betteriter.global.util.JwtService;
import com.example.betteriter.user.repository.UserRepository;
import com.example.betteriter.user.dto.UserOauthLoginResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final InMemoryClientRegistrationRepository inMemoryClientRegistrationRepository;
    private final JwtProperties jwtProperties;

    /**
     * - kakao 로그인 후 jwt 발급하는 로직
     * - kakao oauth server 에 jwt 을 요청하고 받아서 https://kapi.kakao.com/user/me
    **/
    public UserOauthLoginResponseDto kakaoOauthLogin(String code) {
        log.info(this.jwtProperties.getAccessExpiration());
        log.info(this.jwtProperties.getRefreshExpiration());
        log.info(this.jwtProperties.getSecret());
        return null;
    }

    public Void test() {
        log.info(this.jwtProperties.getAccessExpiration());
        log.info(this.jwtProperties.getRefreshExpiration());
        log.info(this.jwtProperties.getSecret());
        return null;
    }
}
