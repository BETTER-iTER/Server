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

    public static List<MypageResponse.FollowerDto> toFollowerDtoList(List<Users> followerList) {
        List<MypageResponse.FollowerDto> followerDtoList = new ArrayList<>();

        followerList.forEach(f -> {
            MypageResponse.FollowerDto followerDto = MypageResponse.FollowerDto.builder()
                    .email(f.getEmail())
                    .profileImage(f.getUsersDetail().getProfileImage())
                    .nickname(f.getUsersDetail().getNickName())
                    .build();
            followerDtoList.add(followerDto);
        });

        return followerDtoList;
    }

    public static MypageResponse.UserProfileDto toUserProfileDto(
            Users user, Boolean isFollow, Boolean isSelf, Long followerCount, Long followingCount
    ) {
        return MypageResponse.UserProfileDto.builder()
                .profileImage(user.getUsersDetail().getProfileImage())
                .nickname(user.getUsersDetail().getNickName())
                .job(user.getUsersDetail().getJob())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .isFollow(isFollow)
                .isSelf(isSelf)
                .build();
    }
}
