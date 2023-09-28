package com.example.betteriter.user.controller;


import com.example.betteriter.user.dto.JoinDto;
import com.example.betteriter.user.dto.LoginDto;
import com.example.betteriter.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 1. 일반 회원가입
     * - 서비스에서 사용자 인증 정보 관리
     * - /user/join
     **/
    @PostMapping("/join")
    public ResponseEntity<Long> join(
            @RequestBody @Valid JoinDto request) {
        return new ResponseEntity<>(this.userService.join(request), HttpStatus.CREATED);
    }

    /**
     * 2. 로그인
     * - /user/login
     **/
    @PostMapping("/login")
    public ResponseEntity<UserServiceTokenResponseDto> login(
            @RequestBody @Valid LoginDto loginRequestDto) {
        return new ResponseEntity<>(this.userService.login(loginRequestDto), HttpStatus.OK);
    }
}
