package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Job;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.betteriter.fo_domain.review.dto.GetReviewDetailResponseDto.*;
import static com.example.betteriter.fo_domain.review.dto.ReviewDetailResponse.GetUserResponseDto.from;
import static com.example.betteriter.global.common.code.status.ErrorStatus._REVIEW_IMAGE_NOT_FOUND;


/**
 * - 리뷰 상세 조회 응답 DTO
**/
@JsonPropertyOrder({"reviewDetail", "writerInfo", "relatedReviews"})
@Getter
@NoArgsConstructor
public class ReviewDetailResponse {
    @JsonProperty("reviewDetail")
    private GetReviewDetailResponseDto getReviewDetailResponseDto; // 리뷰 상세 데이터
    private GetUserResponseDto writerInfo; // 리뷰 작성자 장보
    @JsonProperty("relatedReviews")
    private List<GetRelatedReviewResponseDto> getRelatedReviewResponseDto; // 연관 리뷰 데이터

    @Builder
    public ReviewDetailResponse(GetReviewDetailResponseDto getReviewDetailResponseDto,
                                GetUserResponseDto writerInfo,
                                List<GetRelatedReviewResponseDto> getRelatedReviewResponseDto
    ) {
        this.getReviewDetailResponseDto = getReviewDetailResponseDto;
        this.writerInfo = writerInfo;
        this.getRelatedReviewResponseDto = getRelatedReviewResponseDto;
    }

    public static ReviewDetailResponse of(Review review, List<Review> relatedReviews, boolean isCurrentUserLikeReview,
                                          boolean isCurrentUserScrapReview, boolean isCurrentUserFollow, boolean isCurrentUserIsReviewWriter
    ) {
        GetReviewDetailResponseDto reviewDetail // 리뷰 상세
                = GetReviewDetailResponseDto.from(review, isCurrentUserLikeReview, isCurrentUserScrapReview,isCurrentUserFollow, isCurrentUserIsReviewWriter);

        GetUserResponseDto writerInfo = from(review.getWriter()); // 리뷰 작성자 데이터

        List<GetRelatedReviewResponseDto> getRelatedReviewResponseDto = GetRelatedReviewResponseDto.from(relatedReviews); // 연관 리뷰 데이터

        return ReviewDetailResponse.builder()
                .getReviewDetailResponseDto(reviewDetail)
                .writerInfo(writerInfo)
                .getRelatedReviewResponseDto(getRelatedReviewResponseDto)
                .build();
    }


    @Getter
    @NoArgsConstructor
    public static class GetRelatedReviewResponseDto {
        private Long id;
        private String productName;
        private String reviewImage;
        private String writerName;
        private boolean isExpert;


        @Builder
        public GetRelatedReviewResponseDto(Long id, String productName, String reviewImage,
                                           String writerName, boolean isExpert
        ) {
            this.id = id;
            this.productName = productName;
            this.reviewImage = reviewImage;
            this.writerName = writerName;
            this.isExpert = isExpert;
        }

        public static List<GetRelatedReviewResponseDto> from(List<Review> reviews) {
            return reviews.stream()
                    .map(r -> new GetRelatedReviewResponseDto(
                            r.getId(), r.getProductName(), getFirstImageWithReview(r), r.getWriter().getUsername(), r.getWriter().isExpert()))
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
    }

    @Getter
    @NoArgsConstructor
    public static class GetUserResponseDto {
        private Long id;
        private String nickName;
        private Job job;
        private String profileImage;
        private boolean isExpert;

        @Builder
        public GetUserResponseDto(Long id, String nickName, Job job,
                                  String profileImage, boolean isExpert) {
            this.id = id;
            this.nickName = nickName;
            this.job = job;
            this.profileImage = profileImage;
            this.isExpert = isExpert;
        }

        public static ReviewDetailResponse.GetUserResponseDto from(Users writer) {
            return ReviewDetailResponse.GetUserResponseDto.builder()
                    .id(writer.getId())
                    .nickName(writer.getUsersDetail().getNickName())
                    .job(writer.getUsersDetail().getJob())
                    .isExpert(writer.isExpert())
                    .profileImage(writer.getUsersDetail().getProfileImage())
                    .build();
        }
    }
}