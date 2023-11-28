package com.example.betteriter.fo_domain.user.service;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersWithdrawReason;
import com.example.betteriter.fo_domain.user.exception.UserHandler;
import com.example.betteriter.fo_domain.user.repository.UsersRepository;
import com.example.betteriter.fo_domain.user.repository.UsersWithdrawReasonRepository;
import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.util.RedisUtil;
import com.example.betteriter.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * - 유저 관련 로직을 담고 있는 서비스
 * - 회원 탈퇴, 로그아웃, 회원 정보 수정 등
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final UsersWithdrawReasonRepository usersWithdrawReasonRepository;
    private final RedisUtil redisUtil;


    /* 로그아웃 */
    @Transactional
    public Long logout() {
        Users users = this.getUserAndDeleteRefreshToken();
        SecurityUtil.clearSecurityContext(); // SecurityContext 초기화
        return users.getId();
    }


    /* 회원 탈퇴 */
    @Transactional
    public void withdraw(String reasons) {
        Users users = this.getUserAndDeleteRefreshToken();
        SecurityUtil.clearSecurityContext(); // SecurityContext 초기화
        this.saveUsersWithdrawReason(reasons); // 유저 탈퇴 사유 저장 메소드
        this.usersRepository.delete(users); // 유저 삭제
    }

    /* 회원 탈퇴 사유 저장 */
    private void saveUsersWithdrawReason(String reasons) {
        Arrays.stream(reasons.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toList()).stream()
                .map(UsersWithdrawReason::new)
                .forEach(this.usersWithdrawReasonRepository::save);
    }

    private Users getUserAndDeleteRefreshToken() {
        Users users = this.getCurrentUser();
        this.redisUtil.deleteData(String.valueOf(users.getId()));
        return users;
    }

    /* 현재 로그인한 회원 정보 가져오기 */
    public Users getCurrentUser() {
        return this.usersRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }
}
