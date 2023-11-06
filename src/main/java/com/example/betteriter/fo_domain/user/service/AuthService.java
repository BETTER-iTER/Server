package com.example.betteriter.fo_domain.user.service;

import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.dto.JoinDto;
import com.example.betteriter.fo_domain.user.dto.LoginDto;
import com.example.betteriter.fo_domain.user.dto.PasswordResetRequestDto;
import com.example.betteriter.fo_domain.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.fo_domain.user.exception.UserHandler;
import com.example.betteriter.fo_domain.user.repository.UserDetailRepository;
import com.example.betteriter.fo_domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final EmailService emailService;
    private final JwtProperties jwtProperties;

    @Override
    public User loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserHandler(_USER_NOT_FOUND));
    }

    /* 회원 가입 */
    @Transactional
    public Long join(JoinDto joinDto) {
        // 이메일 중복 여부 파악
        this.checkEmailDuplication(joinDto.getEmail());
        return this.processJoin(joinDto, this.passwordUtil.encode(joinDto.getPassword()));
    }

    private Long processJoin(JoinDto joinDto, String encryptPassword) {
        return this.userRepository.save(joinDto.toUserEntity(encryptPassword, joinDto.toUserDetailEntity()))
                .getId();
    }

    /* 로그인 */
    @Transactional
    public UserServiceTokenResponseDto login(LoginDto loginRequestDto) {
        User user = this.loadUserByUsername(loginRequestDto.getEmail());
        this.checkPassword(loginRequestDto, user);
        return this.saveAuthenticationAndReturnServiceToken(user);
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
        User user = this.checkEmailExistenceAndType(request.getEmail());
        user.setPassword(this.passwordUtil.encode(request.getPassword()));
        log.info(this.passwordUtil.encode(request.getPassword()));
    }

    /* 인증 코드 체크 */
    @Transactional
    public void verifyAuthCode(EmailAuthenticationDto request) {
        // 1. 이메일 중복 여부 확인
        this.checkEmailDuplication(request.getEmail());
        // 2.요청받은 인증코드 존재 여부 확인
        String authCode = this.redisUtil.getData(request.getEmail());
        if (authCode == null) {
            log.debug("AuthService.verifyAuthCode() Exception Occurs! - auth code is null");
            throw new UserHandler(_AUTH_CODE_NOT_EXIST);
        }
        // 3. 요청 auth code 와 redis 저장된 auth code 가 다른지 확인
        if (!request.getCode().equals(authCode)) {
            log.warn("AuthService.verifyAuthCode() Exception Occurs! - auth code not match");
            throw new UserHandler(_AUTH_CODE_NOT_MATCH);
        }
        // 4.인증 코드 검증 성공(redis 데이터 삭제)
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

    private void checkEmailDuplication(String email) {
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new UserHandler(_EMAIL_DUPLICATION);
        }
    }

    private void checkPassword(LoginDto loginRequestDto, User user) {
        if (!this.passwordUtil.isEqual(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorCode._PASSWORD_NOT_MATCH);
        }
    }

    private User checkEmailExistenceAndType(String email) {
        // 해당 유저가 있는지 확인
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(_USER_NOT_FOUND));
        // 해당 유저의 회원가입 타입이 일반 회원가입인지 확인
        if (null != user.getOauthId()) {
            throw new UserHandler(_AUTH_SHOULD_BE_KAKAO);
        }
        return user;
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
    private UserServiceTokenResponseDto saveAuthenticationAndReturnServiceToken(User user) {
        // -> 여기서 SecurityContext 에 저장된 UserAuthentication 존재 x
        UserAuthentication userAuthentication = new UserAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        UserServiceTokenResponseDto serviceToken = jwtUtil.createServiceToken(user);

        this.redisUtil.setDataExpire(String.valueOf(user.getId()),
                serviceToken.getRefreshToken(), jwtProperties.getRefreshExpiration()); // 토큰 발급 후 Redis 에 Refresh token 저장
        return serviceToken;
    }
}
