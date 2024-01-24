package com.example.betteriter.fo_domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

public class FollowResponse {

    @Getter
    @Builder
    public static class FollowingDto {
        private String message;
    }

    @Getter
    @Builder
    public static class UnfollowingDto {
        private String message;
    }
}
