package com.example.betteriter.fo_domain.mypage.converter;

import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;

import java.util.ArrayList;
import java.util.List;

public class MypageResponseConverter {
    public static MypageResponse.ReviewListDto toReviewListDto(List<Review> reviewList) {
        List<MypageResponse.ReviewDto> myReviewList = new ArrayList<>();

        reviewList.forEach(r -> {
            MypageResponse.ReviewDto reviewDto = MypageResponse.ReviewDto.builder()
                    .reviewId(r.getId())
                    .title(r.getProductName())
                    .thumbnailImage((!r.getReviewImages().isEmpty()) ?
                                    r.getReviewImages().get(0).getImgUrl(): null)
                    .writerId(r.getWriter().getId())
                    .writerJob(r.getWriter().getUsersDetail().getJob())
                    .writerNickname(r.getWriter().getUsersDetail().getNickName())
                    .profileImage(r.getWriter().getUsersDetail().getProfileImage())
                    .likeCount((long) r.getReviewLiked().size())
                    .scrapCount((long) r.getReviewScraped().size())
                    .isLike(r.getReviewLiked().stream().anyMatch(rl -> rl.getUsers().getId().equals(r.getWriter().getId())))
                    .isScrap(r.getReviewScraped().stream().anyMatch(rs -> rs.getUsers().getId().equals(r.getWriter().getId())))
                    .build();
            myReviewList.add(reviewDto);
        });

        return MypageResponse.ReviewListDto.builder()
                .reviewCount(reviewList.size())
                .reviewList(myReviewList)
                .build();
    }

    public static MypageResponse.FollowerListDto toFollowerListDto(List<Users> followerList, Integer totalCount) {
        List<MypageResponse.FollowerDto> followerDtoList = new ArrayList<>();

        followerList.forEach(f -> {
            MypageResponse.FollowerDto followerDto = MypageResponse.FollowerDto.builder()
                    .email(f.getEmail())
                    .profileImage(f.getUsersDetail().getProfileImage())
                    .nickname(f.getUsersDetail().getNickName())
                    .build();
            followerDtoList.add(followerDto);
        });

        return MypageResponse.FollowerListDto.builder()
                .totalCount(totalCount)
                .followerList(followerDtoList)
                .build();
    }

    public static MypageResponse.UserProfileDto toUserProfileDto(
            Users user, Integer reviewCount, Integer scrapCount ,Integer followerCount, Integer followingCount
    ) {
        return MypageResponse.UserProfileDto.builder()
                .profileImage(user.getUsersDetail().getProfileImage())
                .nickname(user.getUsersDetail().getNickName())
                .job(user.getUsersDetail().getJob())
                .reviewCount(reviewCount)
                .scrapCount(scrapCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }

    public static MypageResponse.PointDetailDto toPointDetailDto(
            Users user, Integer totalLikeCount, Integer totalScrapCount
    ) {
        Integer totalPoint = user.getReviews().size() * 20 +
                totalLikeCount * 7 +
                totalScrapCount * 10;

        return MypageResponse.PointDetailDto.builder()
                .id(user.getId())
                .totalPoint(totalPoint)
                .totalReviewCount(user.getReviews().size())
                .totalLikeCount(totalLikeCount)
                .totalScrapCount(totalScrapCount)
                .build();
    }
}
