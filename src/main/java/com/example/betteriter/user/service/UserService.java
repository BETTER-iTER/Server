package com.example.betteriter.user.service;

import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.global.util.SecurityUtil;
import com.example.betteriter.user.domain.User;
import com.example.betteriter.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * - 유저 관련 로직을 담고 있는 서비스
 * - 회원 탈퇴, 로그아웃, 회원 정보 수정 등
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;


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
}
