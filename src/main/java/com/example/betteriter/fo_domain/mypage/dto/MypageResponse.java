package com.example.betteriter.fo_domain.mypage.dto;

import com.example.betteriter.global.constant.Job;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MypageResponse {

    @Getter
    @Builder
    public static class ReviewDto {
        private Long reviewId;
        private String title;
        private String thumbnailImage;

        private Long writerId;
        private Job writerJob;
        private String writerNickname;
        private String profileImage;

        private Long likeCount;
        private Long scrapCount;
        private Boolean isLike;
        private Boolean isScrap;
    }

    @Getter
    @Builder
    public static class ReviewListDto {
        private Integer reviewCount;
        private List<ReviewDto> reviewList;
    }

    @Getter
    @Builder
    public static class FollowerDto {
        private Long id;
        private String email;
        private String profileImage;
        private String nickname;
    }

    @Getter
    @Builder
    public static class FollowerListDto {
        private Integer totalCount;
        private List<FollowerDto> followerList;
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
