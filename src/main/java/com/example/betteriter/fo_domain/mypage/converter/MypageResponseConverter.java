package com.example.betteriter.fo_domain.mypage.converter;

import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;

import java.util.ArrayList;
import java.util.List;

public class MypageResponseConverter {
    public static List<MypageResponse.MyReviewDto> toMyReviewDtoList(List<Review> reviewList) {
        List<MypageResponse.MyReviewDto> myReviewList = new ArrayList<>();

        reviewList.forEach(r -> {
            MypageResponse.MyReviewDto myReviewDto = MypageResponse.MyReviewDto.builder()
                    .reviewId(r.getId())
                    .title(r.getProductName())
                    .profileImage((!r.getReviewImages().isEmpty()) ?
                                    r.getReviewImages().get(0).getImgUrl(): null)
                    .likeCount((long) r.getReviewLiked().size())
                    .scrapCount((long) r.getReviewScraped().size())
                    .build();
            myReviewList.add(myReviewDto);
        });

        return myReviewList;
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
}
