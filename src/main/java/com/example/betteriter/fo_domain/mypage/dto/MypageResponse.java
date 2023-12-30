package com.example.betteriter.fo_domain.mypage.dto;

import com.example.betteriter.global.constant.Job;
import lombok.Builder;
import lombok.Getter;

public class MypageResponse {

    @Getter
    @Builder
    public static class MyReviewDto{
        private Long reviewId;
        private String title;
        private String profileImage;
        private Long likeCount;
        private Long scrapCount;
    }

    @Getter
    @Builder
    public static class FollowerDto {
        private String email;
        private String profileImage;
        private String nickname;
    }

    @Getter
    @Builder
    public static class UserProfileDto {
        private String profileImage;
        private String nickname;
        private Job job;
        private Long followerCount;
        private Long followingCount;
        private Boolean isFollow;
        private Boolean isSelf;
    }

}
