package com.example.betteriter.fo_domain.follow.service;

import com.example.betteriter.fo_domain.follow.repository.FollowReadRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowConnectorImpl implements FollowConnector {

    private final FollowReadRepository followReadRepository;

    @Override
    public boolean isFollow(Users currentUser, Users reviewWriter) {
        return this.followReadRepository.existsByFollowerAndFollowee(currentUser, reviewWriter);
    }
}
