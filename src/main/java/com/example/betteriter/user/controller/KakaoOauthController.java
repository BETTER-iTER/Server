package com.example.betteriter.user.controller;

import com.example.betteriter.user.dto.UserOauthLoginResponseDto;
import com.example.betteriter.user.service.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoOauthController {

    private final KakaoOauthService kakaoOauthService;

    @GetMapping("/login/callback/kakao")
    public ResponseEntity<UserOauthLoginResponseDto> kakaoOauthLogin(
            @RequestParam String code
    ) throws IOException {
        return ResponseEntity.ok(this.kakaoOauthService.kakaoOauthLogin(code));
    }

    @GetMapping("/test")
    public String test() {
        log.info("테스트 성공!");
        return "테스트 성공!";
    }
}
