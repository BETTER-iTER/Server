package com.example.betteriter.fo_domain.user.controller;


import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 1. 카카오 oauth 로그인 시 처음 회원 인지 반환
 * 2. 회원가입 후 관심 분야 반환 해야 하나?
 **/
@Tag(name = "UserController", description = "User API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    /**
     * 1. 로그아웃
     * - /user/logout
     * 1. 프론트에서 가지고 있는 Access Token 삭제
     * 2. 백에서 Refresh Token 삭제
     **/
    @PostMapping("/logout")
    public ResponseDto<Long> logout() {
        return ResponseDto.onSuccess(this.userService.logout());
    }

    /**
     * 2. 회원 탈퇴
     * - /user/withdraw
     * 1. 프론트에서 가지고 있는 Access Token 삭제
     * 2. User 정보 삭제
     **/
    @DeleteMapping("/withdraw/{reasons}")
    public ResponseDto<Void> withdraw(
            @PathVariable String reasons
    ) {
        this.userService.withdraw(reasons);
        return ResponseDto.onSuccess(null);
    }
}
