package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.fo_domain.review.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetReviewDetailResponseDto {
    private Long reviewId; // 리뷰 아이디
    private String productName; // 리뷰 상품명
    private List<String> reviewSpecData; // 리뷰 스펙 데이터
    private double starPoint; // 리뷰 별점
    private String goodPoint; // 리뷰 좋은점
    private String badPoint; // 리뷰 안좋은점
    private String shortReview; // 리뷰 한줄평
    private String manufacturer; // 제조사 이름
    private int storeName; // 구매처
    private LocalDate boughtAt; // 구매 일
    private LocalDate createdAt; // 작성일
    private List<GetReviewImageResponseDto> reviewImages; // 리뷰 이미지

    private long scrapedCount; // 리뷰 스크랩 갯수
    private long likedCount; // 리뷰 좋아요 갯수
    private long commentCount; // 리뷰 댓글 갯수
    private boolean isScrap; // 리뷰 스크랩 여부
    private boolean isLike; // 리뷰 좋아요 여부
    private boolean isFollow; // 리뷰 팔로우 여부
    private boolean isMine; // 로그인한 유저 리뷰 작성자 여부


    @Builder
    public GetReviewDetailResponseDto(Long reviewId, String productName, List<String> reviewSpecData, double starPoint,
                                      String goodPoint, String badPoint, String shortReview, String manufacturer,
                                      int storeName, LocalDate boughtAt, LocalDate createdAt, List<GetReviewImageResponseDto> reviewImages,
                                      long scrapedCount, long likedCount, long commentCount,
                                      boolean isScrap, boolean isLike, boolean isFollow, boolean isMine
    ) {
        this.reviewId = reviewId;
        this.productName = productName;
        this.reviewSpecData = reviewSpecData;
        this.starPoint = starPoint;
        this.goodPoint = goodPoint;
        this.badPoint = badPoint;
        this.shortReview = shortReview;
        this.manufacturer = manufacturer;
        this.storeName = storeName;
        this.boughtAt = boughtAt;
        this.createdAt = createdAt;
        this.reviewImages = reviewImages;

        this.scrapedCount = scrapedCount;
        this.likedCount = likedCount;
        this.commentCount = commentCount;
        this.isScrap = isScrap;
        this.isLike = isLike;
        this.isFollow = isFollow;
        this.isMine = isMine;
    }

    public static GetReviewDetailResponseDto from(Review review, boolean isLike, boolean isScrap, boolean isFollow, boolean isMine) {
        return GetReviewDetailResponseDto.builder()
                .reviewId(review.getId())
                .productName(review.getProductName())
                .reviewSpecData(getReviewSpecDataToStr(review))
                .starPoint(review.getStarPoint())
                .goodPoint(review.getGoodPoint())
                .badPoint(review.getBadPoint())
                .shortReview(review.getShortReview())
                .manufacturer(review.getManufacturer().getCoName())
                .storeName(review.getStoreName())
                .boughtAt(review.getBoughtAt())
                .createdAt(review.getCreatedAt() == null ? null  : review.getCreatedAt().toLocalDate())
                .reviewImages(GetReviewImageResponseDto.of(review.getReviewImages()))
                .scrapedCount(review.getScrapedCount())
                .likedCount(review.getLikedCount())
                .commentCount(review.getReviewComment().size())
                .isScrap(isScrap)
                .isLike(isLike)
                .isFollow(isFollow)
                .isMine(isMine)
                .build();
    }

    private static List<String> getReviewSpecDataToStr(Review review) {
        return review.getSpecData().stream()
                .map(reviewSpecData -> reviewSpecData.getSpecData().getData())
                .collect(Collectors.toList());
    }
}