package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.betteriter.global.common.code.status.ErrorStatus._REVIEW_IMAGE_NOT_FOUND;

/* 리뷰 검색 용도 응답 dto */
@Getter
@NoArgsConstructor
@ToString
public class GetReviewResponseDto {
    private Long id; // 리뷰 아이디
    private String reviewImage; // 리뷰 대표 이미지
    private String productName; // 리뷰 상품 명
    private List<String> reviewSpecData; // 리뷰 스펙 데이터
    private double starPoint; // 별점
    private String shortReview; // 한줄 평
    private GetUserResponseDto userInfo; // 리뷰 작성자 정보
    private int scrapedCount; // 스크랩 갯수
    private int likedCount; // 좋아요 갯수

    @Builder
    public GetReviewResponseDto(Review review, List<String> reviewSpecData, String firstImage) {
        this.id = review.getId();
        this.reviewImage = firstImage;
        this.productName = review.getProductName();
        this.reviewSpecData = reviewSpecData;
        this.starPoint = review.getStarPoint();
        this.shortReview = review.getShortReview();
        this.userInfo = GetUserResponseDto.from(review);
        this.scrapedCount = review.getReviewScraped() == null ? 0 : review.getReviewScraped().size();
        this.likedCount = review.getReviewLiked() == null ? 0 : review.getReviewLiked().size();
    }

    public static GetReviewResponseDto of(Review review) {
        List<String> reviewSpecDataToStr = getReviewSpecDataToStr(review);
        String firstImage = getFirstImageWithReview(review);
        return new GetReviewResponseDto(review, reviewSpecDataToStr, firstImage);
    }


    private static List<String> getReviewSpecDataToStr(Review review) {
        return review.getSpecData().stream()
                .map(reviewSpecData -> reviewSpecData.getSpecData().getData())
                .collect(Collectors.toList());
    }

    private static String getFirstImageWithReview(Review review) {
        List<ReviewImage> reviewImages = review.getReviewImages();
        return reviewImages.stream()
                .filter(ri -> ri.getOrderNum() == 0)
                .findFirst()
                .orElseThrow(() -> new ReviewHandler(_REVIEW_IMAGE_NOT_FOUND))
                .getImgUrl();
    }

    @Getter
    @NoArgsConstructor
    static class GetUserResponseDto {
        private String nickName;
        private Job job;
        private String profileImage;

        @Builder
        public GetUserResponseDto(String nickName, Job job, String profileImage) {
            this.nickName = nickName;
            this.job = job;
            this.profileImage = profileImage;
        }

        public static GetUserResponseDto from(Review review) {
            Users user = review.getWriter();
            return GetUserResponseDto.builder()
                    .nickName(user.getUsersDetail().getNickName())
                    .job(user.getUsersDetail().getJob())
                    .profileImage(user.getUsersDetail().getProfileImage())
                    .build();
        }
    }
}
