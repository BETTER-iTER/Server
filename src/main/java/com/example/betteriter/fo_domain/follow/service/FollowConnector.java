package com.example.betteriter.fo_domain.follow.service;

import com.example.betteriter.fo_domain.user.domain.Users;

public interface FollowConnector {
    boolean isFollow(Users currentUser, Users reviewWriter);
}
