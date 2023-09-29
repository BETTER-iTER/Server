package com.example.betteriter.user.controller;


import com.example.betteriter.user.dto.EmailDto;
import com.example.betteriter.user.dto.JoinDto;
import com.example.betteriter.user.dto.LoginDto;
import com.example.betteriter.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * - 이미 회원가입된 사용자는 에러처리
     * - /user/join
     **/
    @PostMapping("/join")
    public ResponseEntity<Long> join(
            @RequestBody @Valid JoinDto request) {
        return new ResponseEntity<>(this.userService.join(request), HttpStatus.CREATED);
    }

    /**
     * 2. 이메일 인증
     * - 사용자가 일반 회원가입에서 입력한 이메일을 통한 인증 로직
     * -
     */
    @PostMapping("/email")
    public ResponseEntity<Void> email(
            @RequestBody @Valid EmailDto emailDto
    ) {
        this.userService.email(emailDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 3. 로그인
     * - /user/login
     **/
    @PostMapping("/login")
    public ResponseEntity<UserServiceTokenResponseDto> login(
            @RequestBody @Valid LoginDto loginRequestDto) {
        return new ResponseEntity<>(this.userService.login(loginRequestDto), HttpStatus.OK);
    }

    /**
     * 4. 로그아웃
     * - /user/logout
     * 1. 프론트에서 가지고 있는 Access Token 삭제
     * 2. 백에서 Refresh Token 삭제
     **/
    @PostMapping("/logout")
    public ResponseEntity<Long> logout() {
        return new ResponseEntity<>(this.userService.logout(), HttpStatus.OK);
    }

    /**
     * 5. 회원 탈퇴
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
