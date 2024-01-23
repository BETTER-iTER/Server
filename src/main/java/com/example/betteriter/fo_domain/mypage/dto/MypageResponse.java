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
    public static class PageDto {
        private Integer page;       // 현재 페이지 번호
        private Integer size;       // 한 페이지에 보여줄 요소 개수
        private Integer totalPage;  // 전체 페이지 개수
        private Long totalCount; // 전체 요소 개수

        private Boolean hasNext;    // 다음 페이지 존재 여부
        private Boolean hasPrev;    // 이전 페이지 존재 여부
    }

    @Getter
    @Builder
    public static class ReviewListDto {
        private PageDto pageInfo;
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
        private PageDto pageInfo;
        private List<FollowerDto> followerList;
    }

    @Getter
    @Builder
    public static class UserProfileDto {
        private String profileImage;
        private String nickname;
        private Job job;
        private Integer followerCount;
        private Integer followingCount;
    }

    @Getter
    @Builder
    public static class PointDetailDto {
        private Long id;
        private Integer totalPoint;
        private Integer totalReviewCount;
        private Integer totalLikeCount;
        private Integer totalScrapCount;
    }

}
