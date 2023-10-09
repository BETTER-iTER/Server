package com.example.betteriter.fo_domain.user.service;

import com.example.betteriter.fo_domain.user.repository.UserRepository;
import com.example.betteriter.global.config.security.UserAuthentication;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.infra.EmailAuthenticationDto;
import com.example.betteriter.infra.EmailDto;
import com.example.betteriter.infra.EmailService;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.dto.JoinDto;
import com.example.betteriter.fo_domain.user.dto.LoginDto;
import com.example.betteriter.fo_domain.user.dto.PasswordResetRequestDto;
import com.example.betteriter.fo_domain.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.fo_domain.user.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final EmailService emailService;

    @Override
    public User loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저 정보를 찾을 수 없습니다."));
    }

    /* 회원 가입 */
    @Transactional
    public Long join(JoinDto joinDto) {
        String encryptPassword = this.passwordUtil.encode(joinDto.getPassword());
        return this.userRepository.save(joinDto.toEntity(encryptPassword)).getId();
    }

    /* 이메일 인증 요청 */
    @Transactional
    public void requestEmailForJoin(EmailDto emailDto) {
        this.checkEmailDuplication(emailDto.getEmail());
        String authCode = makeAuthCodeAndSave(emailDto);
        this.emailService.sendEmailForJoin(emailDto.getEmail(), authCode);
    }

    /* 로그인 */
    @Transactional
    public UserServiceTokenResponseDto login(LoginDto loginRequestDto) {
        User user = this.loadUserByUsername(loginRequestDto.getEmail());
        if (!this.passwordUtil.isEqual(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        return saveAuthenticationAndReturnToken(user);
    }


    /* 비밀번호 재설정 이메일 요청 */
    @Transactional
    public void requestEmailForPassword(EmailDto emailDto) {
        // 해당 이메일 유저 존재 여부 확인 및 로그인 타입 확인
        this.checkEmailExistenceAndType(emailDto.getEmail());
        String authCode = makeAuthCodeAndSave(emailDto);
        this.emailService.sendEmailForResetPassword(emailDto.getEmail(), authCode);
    }

    /* 비밀번호 재설정 */
    @Transactional
    public void resetPassword(PasswordResetRequestDto request) {
        User user = checkEmailExistenceAndType(request.getEmail());
        user.setPassword(this.passwordUtil.encode(request.getPassword()));
        log.info(this.passwordUtil.encode(request.getPassword()));
    }

    /* 인증 코드 체크 */
    @Transactional
    public void verifyAuthCode(EmailAuthenticationDto emailAuthenticationDto) {
        // 1.요청받은 인증코드와 Redis 저장된 인증코드 비교
        String authCode = this.redisUtil.getData(emailAuthenticationDto.getEmail());
        if (authCode == null) {
            log.debug("AuthService.verifyAuthCode() Exception Occurs!");
            throw new RuntimeException("인증 코드의 유효기간이 이미 지났습니다.");
        }
        // 2. 요청 auth code 와 redis 저장된 auth code 가 다른지 확인
        if (!emailAuthenticationDto.getCode().equals(authCode)) {
            log.warn("AuthService.verifyAuthCode() Exception Occurs!");
            throw new RuntimeException("요청 받은 인증 코드의 검증이 실패했습니다.");
        }
        // 3.인증 코드 검증 성공(redis 데이터 삭제)
        this.redisUtil.deleteData(emailAuthenticationDto.getEmail());
    }


    /* 닉네임 중복 여부 */
    public Boolean checkNickname(String nickname) {
        return this.userRepository.countByNickName(nickname) == 0;
    }

    // ================================================================================ //

    /* 인가 코드 생성 및 Redis 저장 */
    private String makeAuthCodeAndSave(EmailDto emailDto) {
        String authCode = this.createAuthCode();
        // 해당 이메일을 키로 가지는 Redis 데이터가 존재하는 경우
        if (this.redisUtil.getData(emailDto.getEmail()) != null) {
            throw new RuntimeException("해당 이메일의 인증 코드가 이미 존재합니다.");
        }
        this.redisUtil.setDataExpire(emailDto.getEmail(), authCode, 60L * 3); // 3분
        return authCode;
    }

    private void checkEmailDuplication(String email) {
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("이미 존재하는 회원 이메일 입니다.");
        }
    }

    private User checkEmailExistenceAndType(String email) {
        // 해당 유저가 있는지 확인
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일 정보를 가진 유저 정보가 존재하지 않습니다."));
        // 해당 유저의 회원가입 타입이 일반 회원가입인지 확인
        if (null != user.getOauthId()) {
            throw new RuntimeException("해당 유저는 카카오 로그인 유저 입니다.");
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
    private UserServiceTokenResponseDto saveAuthenticationAndReturnToken(User user) {
        // -> 여기서 SecurityContext 에 저장된 UserAuthentication 존재 x
        UserAuthentication userAuthentication = new UserAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        UserServiceTokenResponseDto serviceToken = jwtUtil.getServiceToken(user);

        this.redisUtil.setData(String.valueOf(user.getId()), serviceToken.getRefreshToken()); // 토큰 발급 후 Redis 에 Refresh token 저장
        return serviceToken;
    }
}
