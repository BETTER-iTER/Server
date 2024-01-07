package com.example.betteriter.fo_domain.user.service;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.exception.UserHandler;
import com.example.betteriter.fo_domain.user.repository.UsersRepository;
import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserConnectorImpl implements UserConnector {
    private final UsersRepository usersRepository;
    private final SecurityUtil securityUtil;

    @Override
    public Users getCurrentUser() {
        /* 현재 로그인한 회원 정보 가져오기 */
        return this.usersRepository.findByEmail(securityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new UserHandler(ErrorStatus._USER_NOT_FOUND));
    }
}
