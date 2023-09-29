package com.example.betteriter.user.service;

import com.example.betteriter.global.config.security.UserAuthentication;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.global.util.SecurityUtil;
import com.example.betteriter.user.domain.User;
import com.example.betteriter.user.dto.EmailDto;
import com.example.betteriter.user.dto.JoinDto;
import com.example.betteriter.user.dto.LoginDto;
import com.example.betteriter.user.dto.UserServiceTokenResponseDto;
import com.example.betteriter.user.repository.UserRepository;
import com.example.betteriter.user.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * - 유저 관련 로직을 담고 있는 서비스
 * - 일반 회원가입, 로그인, 이메일 전송, 회원 탈퇴, 로그아웃 등
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
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

    /* 이메일 인증 */
    public void email(EmailDto emailDto) {

    }


    /* 로그인 */
    @Transactional
    public UserServiceTokenResponseDto login(LoginDto loginRequestDto) {
        User user = this.loadUserByUsername(loginRequestDto.getEmail());
        if (!this.passwordUtil.isEqual(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // -> 여기서 SecurityContext 에 저장된 UserAuthentication 존재 x
        UserAuthentication userAuthentication = new UserAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        UserServiceTokenResponseDto serviceToken = jwtUtil.getServiceToken(user);

        this.redisUtil.setData(String.valueOf(user.getId()), serviceToken.getRefreshToken()); // 토큰 발급 후 Redis 에 Refresh token 저장
        return serviceToken;
    }

    /* 로그아웃 */
    @Transactional
    public Long logout() {
        User user = getUser();
        this.redisUtil.deleteData(String.valueOf(user.getId()));
        return user.getId();
    }

    /* 회원 탈퇴 */
    @Transactional
    public void withdraw() {
        User user = getUser();
        this.userRepository.delete(user);
    }

    private User getUser() {
        return this.userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저 정보를 찾을 수 없습니다."));
    }


    // 이메일 인증 시 랜덤 문자열 생성

}
