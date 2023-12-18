package com.example.betteriter.fo_domain.mypage.converter;

import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

public class MypageResponseConverter {
    public static List<MypageResponse.MyReviewDto> toMyReviewDtoList(List<Review> reviewList) {
        List<MypageResponse.MyReviewDto> myReviewList = new ArrayList<>();

        reviewList.forEach(r -> {
            MypageResponse.MyReviewDto myReviewDto = MypageResponse.MyReviewDto.builder()
                    .review_id(r.getId())
                    .title(r.getProductName())
                    .profile_image((r.getReviewImages().size() > 0) ?
                            r.getReviewImages().get(0).getImgUrl():
                            "none")
                    .like_count(r.getReviewLiked().stream().count())
                    .scrap_count(r.getReviewScraped().stream().count())
                    .build();
            myReviewList.add(myReviewDto);
        });

        return myReviewList;
    }
}
