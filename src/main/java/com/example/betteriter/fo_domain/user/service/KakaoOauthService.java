package com.example.betteriter.fo_domain.user.service;

import com.example.betteriter.fo_domain.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.fo_domain.user.dto.info.KakaoOauthUserInfo;
import com.example.betteriter.fo_domain.user.dto.oauth.KakaoToken;
import com.example.betteriter.fo_domain.user.repository.UserRepository;
import com.example.betteriter.global.config.properties.JwtProperties;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.SecurityUtil;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.dto.RoleType;
import com.example.betteriter.fo_domain.user.dto.oauth.KakaoJoinDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {
    private final static String PROVIDER_NAME = "kakao";
    private final UserRepository userRepository;
    private final InMemoryClientRegistrationRepository inMemoryClientRegistrationRepository;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    /**
     * - findUser : 회원 저장 및 리턴
     * - getServiceToken : 실제 서비스 jwt 발급
     **/
    public UserServiceTokenResponseDto kakaoOauthLogin(String code) throws IOException {
        return this.jwtUtil.getServiceToken(findUser(code));
    }

    /**
     * step 00 : 회원 조회
     **/
    private User findUser(String code) throws IOException {
        ClientRegistration kakaoClientRegistration
                = this.inMemoryClientRegistrationRepository.findByRegistrationId(PROVIDER_NAME);
        KakaoToken kakaoToken = getKakaoToken(code, kakaoClientRegistration);
        return saveUserWithKakaoUserInfo(kakaoToken, kakaoClientRegistration);
    }

    /**
     * step 01 : 카카오 jwt 요청
     * - https://kauth.kakao.com/oauth/token
     * - application/x-www-form-urlencoded
     **/
    private KakaoToken getKakaoToken(String code,
                                     ClientRegistration kakaoClientRegistration) {
        return WebClient.create()
                .post()
                .uri(kakaoClientRegistration.getProviderDetails().getTokenUri())
                .headers(h -> {
                    h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    h.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(kakaoTokenRequest(code, kakaoClientRegistration))
                .retrieve()
                .bodyToMono(KakaoToken.class)
                .block();
    }

    /**
     * - post request body
     * 1. code : 받은 인가 코드
     * 2. redirect-uri : 설정한 리다이렉트 주소
     * 3. grant-type : authorization_code
     * 4. client-id : rest api 앱 키
     * 5. client-secret : 받는 토큰 보안 강화
     **/
    private MultiValueMap<String, String> kakaoTokenRequest(String code, ClientRegistration kakaoClientRegistration) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("redirect_uri", kakaoClientRegistration.getRedirectUri());
        formData.add("grant_type", kakaoClientRegistration.getAuthorizationGrantType().getValue());
        formData.add("client_id", kakaoClientRegistration.getClientId());
        formData.add("client_secret", kakaoClientRegistration.getClientSecret());
        return formData;
    }

    /**
     * step 02 : 카카오 jwt 을 가지고 회원정보 요청
     * - https://kapi.kakao.com/user/me
     * - kakao auth server 에 존재하는 내정보를 이용해서 회원 엔티티 생성
     **/
    private User saveUserWithKakaoUserInfo(KakaoToken kakaoToken,
                                           ClientRegistration kakaoClientRegistration) throws IOException {
        Map<String, Object> attributes = getUserAttributes(kakaoToken, kakaoClientRegistration);
        KakaoOauthUserInfo kakaoOauthUserInfo = new KakaoOauthUserInfo(attributes);
        String oauthId = kakaoOauthUserInfo.getOauthId();
        String kakaoEmail = kakaoOauthUserInfo.getKakaoEmail();
        return this.userRepository.findByOauthId(oauthId).orElseGet(() -> this.userRepository.save(
                User.builder()
                        .oauthId(oauthId)
                        .email(kakaoEmail)
                        .role(RoleType.ROLE_USER)
                        .build()));
    }

    /* 실제 회원 정보 가져오기 */
    private Map<String, Object> getUserAttributes(KakaoToken kakaoToken, ClientRegistration kakaoClientRegistration) {
        return WebClient.create()
                .get()
                .uri(kakaoClientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(h -> h.setBearerAuth(kakaoToken.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

    /* 카카오 회원가입 마무리 */
    @Transactional
    public void completeKakaoJoin(KakaoJoinDto request) {
        getUser().completeKakaoJoin(request);
    }

    private User getUser() {
        return this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저 정보를 찾을 수 없습니다."));
    }
}