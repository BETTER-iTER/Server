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
    private Long id; // 리뷰 아이디
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


    @Builder
    public GetReviewDetailResponseDto(Long id, String productName, List<String> reviewSpecData, double starPoint,
                                      String goodPoint, String badPoint, String shortReview, String manufacturer,
                                      int storeName, LocalDate boughtAt, LocalDate createdAt, List<GetReviewImageResponseDto> reviewImages, long scrapedCount
    ) {
        this.id = id;
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
    }

    public static GetReviewDetailResponseDto from(Review review) {
        return GetReviewDetailResponseDto.builder()
                .id(review.getId())
                .productName(review.getProductName())
                .reviewSpecData(getReviewSpecDataToStr(review))
                .starPoint(review.getStarPoint())
                .goodPoint(review.getGoodPoint())
                .badPoint(review.getBadPoint())
                .shortReview(review.getShortReview())
                .manufacturer(review.getManufacturer().getCoName())
                .storeName(review.getStoreName())
                .boughtAt(review.getBoughtAt())
                .createdAt(review.getCreatedAt() == null ? null : review.getCreatedAt().toLocalDate())
                .reviewImages(GetReviewImageResponseDto.of(review.getReviewImages()))
                .scrapedCount(review.getScrapedCount())
                .build();
    }

    private static List<String> getReviewSpecDataToStr(Review review) {
        return review.getSpecData().stream()
                .map(reviewSpecData -> reviewSpecData.getSpecData().getData())
                .collect(Collectors.toList());
    }
}