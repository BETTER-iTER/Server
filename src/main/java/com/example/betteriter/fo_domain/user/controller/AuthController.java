package com.example.betteriter.fo_domain.user.controller;

import com.example.betteriter.fo_domain.user.dto.JoinDto;
import com.example.betteriter.fo_domain.user.dto.LoginDto;
import com.example.betteriter.fo_domain.user.dto.PasswordResetRequestDto;
import com.example.betteriter.fo_domain.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.fo_domain.user.service.AuthService;
import com.example.betteriter.infra.EmailAuthenticationDto;
import com.example.betteriter.infra.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * - 회원 회원가입/로그인 등 인증,인가와 관련된 작업
 **/

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    /**
     * 1. 일반 회원가입
     * - 서비스에서 사용자 인증 정보 관리 컨트롤러
     * - 이미 회원가입된 사용자는 에러처리
     * - /auth/join
     **/
    @PostMapping("/join")
    public ResponseEntity<Long> join(
            @RequestBody @Valid JoinDto request) {
        return new ResponseEntity<>(this.authService.join(request), HttpStatus.CREATED);
    }


    /**
     * 2. 회원가입을 위한 이메일 인증 요청
     * - 사용자가 일반 회원가입에서 입력한 이메일을 통한 인증 컨트롤러
     * - 해당 이메일 중복 여부 판단 후, 인증 번호 전송
     * 과정 : 클라이언트 이메일 입력 및 인증 버튼 클릭 -> 서버로 해당 이메일과 함께 API 요청 ->
     * -> 해당 이메일을 가진 회원이 있는지 중복 체크 -> 없다면 인증 코드 이메일로 전송 ->
     * 사용자가 인증 번호 클릭 -> 서버에서 판단 후 승인 or 거부
     */
    @PostMapping("/join/emails")
    public ResponseEntity<Void> requestEmail(
            @RequestBody @Valid EmailDto emailDto
    ) {
        this.authService.requestEmailForJoin(emailDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 3. 인증 코드 검증
     * - 사용자가 이메일로 받은 인증 코드 검증하는 컨트롤러
     * - 회원가입 & 비밀번호 재설정 모두 사용
     * - /auth/check/code
     **/
    @PostMapping("/emails/verification")
    public ResponseEntity<Void> verifyAuthCode(
            @RequestBody @Valid EmailAuthenticationDto emailAuthenticationDto
    ) {
        this.authService.verifyAuthCode(emailAuthenticationDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 4. 일반 로그인
     * - /auth/login
     **/
    @PostMapping("/login")
    public ResponseEntity<UserServiceTokenResponseDto> login(
            @RequestBody @Valid LoginDto loginRequestDto) {
        return new ResponseEntity<>(this.authService.login(loginRequestDto), HttpStatus.OK);
    }

    /**
     * 5. 비밀번호 재설정 이메일 인증 요청 컨트롤러
     * - /auth/password/emails
     * - 비밀번호 재설정을 위한 이메일 요청 이미 해당 회원정보가 있어야 함
     **/
    @PostMapping("/password/emails")
    public ResponseEntity<Void> requestEmailForPassword(
            @RequestBody @Valid EmailDto emailDto
    ) {
        this.authService.requestEmailForPassword(emailDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 6. 비밀번호 재설정 요청 컨트롤러
     * - /auth/password/reset
     * - 새로운 비밀번호를 입력받아 설정
     * - 해당 유저가 존재 여부 확인 및 일반 회원가입 유저인지 다시 확인
     **/
    @PatchMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(
            @RequestBody PasswordResetRequestDto request
    ) {
        this.authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 7. 닉네임 중복 여부 판단 컨트롤러
     * - /auth/nickname/check
     * - 닉네임 중복 되지 않는 경우(사용 가능) -> true
     * - 닉네임 중복 되는 경우(사용 불가능) -> false
     **/
    @GetMapping("/nickname/check")
    public ResponseEntity<Boolean> checkNickname(
            @RequestParam String nickname
    ) {
        return new ResponseEntity<>(this.authService.checkNickname(nickname), HttpStatus.OK);
    }
}