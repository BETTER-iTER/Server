package com.example.betteriter.fo_domain.user.service;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.fo_domain.user.domain.UsersWithdrawReason;
import com.example.betteriter.fo_domain.user.dto.info.GetUserInfoResponseDto;
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
    private final SecurityUtil securityUtil;

    /* 로그아웃 */
    @Transactional
    public Long logout() {
        Users users = this.getUserAndDeleteRefreshToken();
        securityUtil.clearSecurityContext(); // SecurityContext 초기화
        return users.getId();
    }

    /* 회원 정보 가져오기 */
    @Transactional(readOnly = true)
    public GetUserInfoResponseDto getUserInfo() {
        Users currentUser = this.getCurrentUser();
        return GetUserInfoResponseDto.from(currentUser, currentUser.getUsersDetail());
    }


    /* 회원 탈퇴 */
    @Transactional
    public void withdraw(String reasons) {
        Users users = this.getUserAndDeleteRefreshToken();
        securityUtil.clearSecurityContext(); // SecurityContext 초기화
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

    protected Users getUserAndDeleteRefreshToken() {
        Users users = this.getCurrentUser();
        this.redisUtil.deleteData(String.valueOf(users.getId()));
        return users;
    }

    /* 현재 로그인한 회원 정보 가져오기 */
    public Users getCurrentUser() {
        return this.usersRepository.findByEmail(securityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new UserHandler(ErrorStatus._USER_NOT_FOUND));
    }

    /* 현재 로그인한 회원 상세 정보 가져오기 */
    public UsersDetail getCurrentUsersDetail() {
        return this.getCurrentUser().getUsersDetail();
    }

    /* 회원 정보 가져오기 */
    public Users getUserById(Long userId) {
        return this.usersRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus._USER_NOT_FOUND));
    }

    public Users getUserByEmail(String email) {
        return this.usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus._USER_NOT_FOUND));
    }
}
