package com.example.betteriter.fo_domain.follow.converter;

import com.example.betteriter.fo_domain.follow.domain.Follow;
import com.example.betteriter.fo_domain.user.domain.Users;

public class FollowConverter {

    public static Follow toFollowing(Users user, Users targetUser) {
        return Follow.builder()
                .followee(targetUser)
                .follower(user)
                .build();
    }

}
