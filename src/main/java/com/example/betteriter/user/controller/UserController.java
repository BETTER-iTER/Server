package com.example.betteriter.user.controller;


import com.example.betteriter.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 1. 카카오 oauth 로그인 시 처음 회원 인지 반환
 * 2. 회원가입 후 관심 분야 반환 해야 하나?
 **/

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
    public ResponseEntity<Long> logout() {
        return new ResponseEntity<>(this.userService.logout(), HttpStatus.OK);
    }

    /**
     * 2. 회원 탈퇴
     * - /user/withdraw
     * 1. 프론트에서 가지고 있는 Access Token 삭제
     * 2. User 정보 삭제
     **/
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw() {
        this.userService.withdraw();
        return ResponseEntity.ok().build();
    }
}
