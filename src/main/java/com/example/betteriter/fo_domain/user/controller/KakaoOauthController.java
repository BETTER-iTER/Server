package com.example.betteriter.fo_domain.user.controller;

import com.example.betteriter.fo_domain.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.fo_domain.user.dto.oauth.KakaoJoinDto;
import com.example.betteriter.fo_domain.user.service.KakaoOauthService;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "KakaoOauthController", description = "Kakao Oauth API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoOauthController {
    private final KakaoOauthService kakaoOauthService;

    @GetMapping("/login/callback/kakao")
    public ResponseEntity<UserServiceTokenResponseDto> kakaoOauthLogin(
            @RequestParam String code
    ) throws IOException {
        return ResponseEntity.ok(this.kakaoOauthService.kakaoOauthLogin(code));
    }

    /**
     * # kakao oauth 회원가입 마무리
     * - kakao oauth 처음 로그인 한 회원인 경우 수행 되는 컨트롤러
     * - /auth/kakao/join
     **/
    @PostMapping("/kakao/join")
    public ResponseDto<Void> completeKakaoJoin(
            @RequestBody @Valid KakaoJoinDto request) {
        this.kakaoOauthService.completeKakaoJoin(request);
        return ResponseDto.onSuccess(null);
    }
}
