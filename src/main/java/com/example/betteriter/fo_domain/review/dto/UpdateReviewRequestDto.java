package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.global.constant.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateReviewRequestDto {

    private Category category; // 카테고리

    private String productName; // 상품명

    private LocalDate boughtAt; // 구매 일자

    private String manufacturer; // 제조사 이름

    private Integer price; // 가격

    private Integer storeName; // 구매처

    private String shortReview; // 한줄평

    @Max(5)
    private Double starPoint; // 별점

    private String goodPoint; // 좋은 점

    private String badPoint; // 나쁜 점

    private List<Long> specData; // specData id 리스트

    private List<Integer> imageIndex; // 바꾸고자 하는 이미지의 인덱스

    @Builder
    public UpdateReviewRequestDto(
            Category category,
            String productName,
            LocalDate boughtAt,
            String manufacturer,
            Integer price,
            Integer storeName,
            String shortReview,
            Double starPoint,
            String goodPoint,
            String badPoint,
            List<Long> specData,
            List<Integer> imageIndex
    ) {
        this.category = category;
        this.productName = productName;
        this.boughtAt = boughtAt;
        this.manufacturer = manufacturer;
        this.price = price;
        this.storeName = storeName;
        this.shortReview = shortReview;
        this.starPoint = starPoint;
        this.goodPoint = goodPoint;
        this.badPoint = badPoint;
        this.specData = specData;
        this.imageIndex = imageIndex;
    }
}
