package com.example.betteriter.fo_domain.follow.converter;

import com.example.betteriter.fo_domain.follow.domain.Follow;
import com.example.betteriter.fo_domain.follow.dto.FollowResponse;

public class FollowResponseConverter {

    public static FollowResponse.FollowingDto toFollowingDto(Follow follow) {
        String message = follow.getFollowee().getUsername() + " 님을 팔로우 하였습니다.";

        return FollowResponse.FollowingDto.builder()
                .message(message)
                .build();
    }

    public static FollowResponse.UnfollowingDto toUnfollowingDto() {
        return FollowResponse.UnfollowingDto.builder()
                .message("팔로우를 취소하였습니다.")
                .build();
    }
}
