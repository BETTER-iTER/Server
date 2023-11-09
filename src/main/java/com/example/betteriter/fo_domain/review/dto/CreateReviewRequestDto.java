package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.global.constant.Category;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {
    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category; // 카테고리
    private String productName; // 상품명
    private LocalDate boughtAt; // 구매 일자
    private Long manufacturerId; // 제조사 아이디
    private int amount; // 가격
    private int storeName; // 구매처
    private String shortReview; //
    private int starPoint; // 별점
    private String goodPoint; // 좋은 점
    private String badPoint; // 나쁜 점
    private List<CreateReviewImageRequestDto> images; // 리뷰 이미지

    public Review toEntity(Manufacturer manufacturer) {
        return Review.builder()
                .category(this.toCategory())
                .productName(productName)
                .boughtAt(boughtAt)
                .manufacturer(manufacturer)
                .amount(amount)
                .storeName(storeName)
                .shortReview(shortReview)
                .starPoint(starPoint)
                .goodPoint(goodPoint)
                .badPoint(badPoint)
                .build();
    }

    private Category toCategory() {
        for (Category category : Category.values()) {
            if (this.category.equals(category.getName())) {
                return category;
            }
        }
        return null;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewImageRequestDto {
        private String imgUrl;

        public ReviewImage toEntity(int orderNum, Review review) {
            return ReviewImage.builder()
                    .imgUrl(imgUrl)
                    .review(review)
                    .orderNum(orderNum)
                    .build();
        }
    }
}
