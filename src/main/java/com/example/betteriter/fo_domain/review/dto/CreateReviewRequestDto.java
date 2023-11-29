package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.global.constant.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateReviewRequestDto {
    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private Category category; // 카테고리
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String productName; // 상품명
    @NotBlank(message = "구매일자는 필수 입력 값입니다.")
    private LocalDate boughtAt; // 구매 일자
    @NotNull(message = "제조사 정보는 필수 입력 값입니다.")
    private Long manufacturerId; // 제조사 아이디
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int amount; // 가격
    @NotNull(message = "구매처 정보는 필수 입력 값입니다.")
    private int storeName; // 구매처
    @NotBlank(message = "한줄평은 필수 입력 값입니다.")
    private String shortReview; // 한줄평
    @NotBlank(message = "별점은 필수 입력 값입니다.")
    private int starPoint; // 별점
    private String goodPoint; // 좋은 점
    private String badPoint; // 나쁜 점
    private List<Integer> specData; // specData id 리스트
    private List<CreateReviewImageRequestDto> images; // 리뷰 이미지

    public Review toEntity(Manufacturer manufacturer, List<ReviewImage> reviewImages) {
        return Review.builder()
                .category(category)
                .productName(productName)
                .boughtAt(boughtAt)
                .manufacturer(manufacturer)
                .amount(amount)
                .storeName(storeName)
                .shortReview(shortReview)
                .starPoint(starPoint)
                .goodPoint(goodPoint)
                .badPoint(badPoint)
                .reviewImages(reviewImages)
                .build();
    }


    @Getter
    @NoArgsConstructor
    public static class CreateReviewImageRequestDto {
        private String imgUrl;
    }
}
