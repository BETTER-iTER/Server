package com.example.betteriter.user.controller;

import com.example.betteriter.user.dto.UserOauthLoginResponseDto;
import com.example.betteriter.user.service.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoOauthController {

    private final KakaoOauthService kakaoOauthService;

    @GetMapping("/login/callback/kakao")
    public ResponseEntity<UserOauthLoginResponseDto> kakaoOauthLogin(
            @RequestParam String code
    ) {
        return ResponseEntity.ok(this.kakaoOauthService.kakaoOauthLogin(code));
    }

    @GetMapping("/test")
    public ResponseEntity<Void> test() {
        return ResponseEntity.ok(this.kakaoOauthService.test());
    }
}