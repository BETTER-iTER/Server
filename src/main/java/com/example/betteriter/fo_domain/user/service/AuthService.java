package com.example.betteriter.fo_domain.user.service;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.dto.JoinDto;
import com.example.betteriter.fo_domain.user.dto.LoginDto;
import com.example.betteriter.fo_domain.user.dto.PasswordResetRequestDto;
import com.example.betteriter.fo_domain.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.fo_domain.user.exception.UserHandler;
import com.example.betteriter.fo_domain.user.repository.UserDetailRepository;
import com.example.betteriter.fo_domain.user.repository.UsersRepository;
import com.example.betteriter.fo_domain.user.util.PasswordUtil;
import com.example.betteriter.global.config.properties.JwtProperties;
import com.example.betteriter.global.config.security.UserAuthentication;
import com.example.betteriter.global.error.exception.ErrorCode;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.infra.EmailAuthenticationDto;
import com.example.betteriter.infra.EmailDto;
import com.example.betteriter.infra.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import static com.example.betteriter.global.error.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final UserDetailRepository userDetailRepository;

    private final EmailService emailService;

    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final JwtProperties jwtProperties;

    @Override
    public Users loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return this.usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserHandler(_USER_NOT_FOUND));
    }

    /* 회원 가입 */
    @Transactional
    public Long join(JoinDto joinDto) {
        // 이메일 중복 여부 파악
        this.checkEmailDuplication(joinDto.getEmail());
        return this.processJoin(joinDto, this.passwordUtil.encode(joinDto.getPassword()));
    }

    /* 로그인 */
    @Transactional
    public UserServiceTokenResponseDto login(LoginDto loginRequestDto) {
        Users users = this.loadUserByUsername(loginRequestDto.getEmail());
        this.checkUserLoginType(users);
        this.checkPassword(loginRequestDto, users);
        return this.saveAuthenticationAndReturnServiceToken(users);
    }

    /* 회원가입 이메일 인증 요청 */
    @Transactional
    public void requestEmailForJoin(EmailDto emailDto) {
        this.checkEmailDuplication(emailDto.getEmail());
        String authCode = this.makeAuthCodeAndSave(emailDto);
        this.emailService.sendEmailForJoin(emailDto.getEmail(), authCode);
    }


    /* 비밀번호 재설정 이메일 요청 */
    @Transactional
    public void requestEmailForPasswordReset(EmailDto emailDto) {
        // 해당 이메일 유저 존재 여부 확인 및 로그인 타입 확인
        this.checkEmailExistenceAndType(emailDto.getEmail());
        String authCode = this.makeAuthCodeAndSave(emailDto);
        this.emailService.sendEmailForResetPassword(emailDto.getEmail(), authCode);
    }

    /* 비밀번호 재설정 */
    @Transactional
    public void resetPassword(PasswordResetRequestDto request) {
        // 해당 이메일 유저 존재 여부 확인 및 로그인 타입 확인
        Users users = this.checkEmailExistenceAndType(request.getEmail());
        users.setPassword(this.passwordUtil.encode(request.getPassword()));
        log.info(this.passwordUtil.encode(request.getPassword()));
    }

    /* 회원 가입 인증 코드 체크 */
    @Transactional
    public void verifyJoinAuthCode(EmailAuthenticationDto request) {
        // 1. 이메일 체크
        this.checkEmailDuplication(request.getEmail());
        // 2. 요청 인증 코드 검증
        this.verifyAuthCode(request);
    }

    /* 비밀번호 재설정 인증 코드 체크 */
    @Transactional
    public void verifyPasswordResetAuthCode(EmailAuthenticationDto request) {
        // 해당 이메일 유저 존재 여부 확인 및 로그인 타입 확인
        this.checkEmailExistenceAndType(request.getEmail());
        // 2. 요청 인증 코드 검증
        this.verifyAuthCode(request);
    }

    private void verifyAuthCode(EmailAuthenticationDto request) {
        // 요청받은 인증코드 존재 여부 확인
        String authCode = this.redisUtil.getData(request.getEmail());
        if (authCode == null) {
            log.debug("AuthService.verifyAuthCode() Exception Occurs! - auth code is null");
            throw new UserHandler(_AUTH_CODE_NOT_EXIST);
        }
        // 요청 auth code 와 redis 저장된 auth code 가 다른지 확인
        if (!request.getCode().equals(authCode)) {
            log.warn("AuthService.verifyAuthCode() Exception Occurs! - auth code not match");
            throw new UserHandler(_AUTH_CODE_NOT_MATCH);
        }
        // 인증 코드 검증 성공(redis 데이터 삭제)
        this.redisUtil.deleteData(request.getEmail());
    }

    /* 닉네임 중복 여부 */
    @Transactional
    public Boolean checkNickname(String nickname) {
        return this.userDetailRepository.countByNickName(nickname) == 0;
    }

    // ================================================================================ //

    /* 인가 코드 생성 및 Redis 저장 */
    private String makeAuthCodeAndSave(EmailDto emailDto) {
        String authCode = this.createAuthCode();
        // 해당 이메일을 키로 가지는 Redis 데이터가 존재하는 경우
        if (this.redisUtil.getData(emailDto.getEmail()) != null) {
            throw new UserHandler(_AUTH_CODE_ALREADY_EXIT);
        }
        this.redisUtil.setDataExpire(emailDto.getEmail(), authCode, 60L * 3); // 3분
        return authCode;
    }

    /* 이메일 중복 여부 체크 및 인증 타입 체크 for 회원가입 */
    private void checkEmailDuplication(String email) {
        if (this.usersRepository.findByEmail(email).isPresent()) {
            throw new UserHandler(_EMAIL_DUPLICATION);
        }
    }

    /* 이메일 중복 여부 체크 및 인증 타입 체크 for 비밀번호 재설정 */
    private void checkEmailExistence(String email) {
        if (this.usersRepository.findByEmail(email).isEmpty()) {
            throw new UserHandler(_EMAIL_NOT_FOUND);
        }
    }

    private void checkPassword(LoginDto loginRequestDto, Users users) {
        if (!this.passwordUtil.isEqual(loginRequestDto.getPassword(), users.getPassword())) {
            throw new UserHandler(ErrorCode._PASSWORD_NOT_MATCH);
        }
    }

    private Users checkEmailExistenceAndType(String email) {
        // 해당 유저가 있는지 확인
        Users users = this.usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(_USER_NOT_FOUND));
        // 해당 유저의 회원가입 타입이 일반 회원가입인지 확인
        if (null != users.getOauthId()) {
            throw new UserHandler(_AUTH_SHOULD_BE_KAKAO);
        }
        return users;
    }

    // 이메일 인증 시 랜덤 문자열 생성
    private String createAuthCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException exception) {
            log.debug("UserService.createCode() exception occurs");
            throw new RuntimeException("이메일 인증 코드 생성 중 예외가 발생했습니다.");
        }
    }

    // SecurityContext 에 Authentication 저장 및 ServiceToken 발급
    private UserServiceTokenResponseDto saveAuthenticationAndReturnServiceToken(Users users) {
        // -> 여기서 SecurityContext 에 저장된 UserAuthentication 존재 x
        UserAuthentication userAuthentication = new UserAuthentication(users);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        UserServiceTokenResponseDto serviceToken = jwtUtil.createServiceToken(users);

        this.redisUtil.setDataExpire(String.valueOf(users.getId()),
                serviceToken.getRefreshToken(), jwtProperties.getRefreshExpiration()); // 토큰 발급 후 Redis 에 Refresh token 저장
        log.info(String.valueOf(serviceToken));
        return serviceToken;
    }

    // 로그인 시도 회원이 카카오 로그인 회원인지 여부 판단
    private void checkUserLoginType(Users users) {
        if (users.getOauthId() != null) {
            throw new UserHandler(_AUTH_SHOULD_BE_KAKAO);
        }
    }

    private Long processJoin(JoinDto joinDto, String encryptPassword) {
        return this.usersRepository.save(joinDto.toUserEntity(encryptPassword, joinDto.toUserDetailEntity()))
                .getId();
    }
}
