package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.global.constant.Category;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateReviewRequestDto {

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Category category; // 카테고리

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String productName; // 상품명

    @NotNull(message = "구매일자는 필수 입력 값입니다.")
    private LocalDate boughtAt; // 구매 일자

    @NotNull(message = "제조사 정보는 필수 입력 값입니다.")
    private String manufacturer; // 제조사 이름

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price; // 가격

    @NotNull(message = "구매처 정보는 필수 입력 값입니다.")
    private Integer storeName; // 구매처

    @NotBlank(message = "비교 제품은 필수 입력 값입니다.")
    private String comparedProductName; // 비교 제품

    @NotBlank(message = "한줄평은 필수 입력 값입니다.")
    private String shortReview; // 한줄평

    @NotNull(message = "별점은 필수 입력 값입니다.")
    @Max(5)
    private Double starPoint; // 별점

    private String goodPoint; // 좋은 점

    private String badPoint; // 나쁜 점

    private List<Long> specData; // specData id 리스트

    private List<String> imageList; // 이미지 리스트

    @Builder
    public UpdateReviewRequestDto(Category category, String productName, LocalDate boughtAt, String manufacturer,
        Integer price, Integer storeName, String comparedProductName, String shortReview, Double starPoint,
        String goodPoint, String badPoint, List<Long> specData, List<String> imageList) {
        this.category = category;
        this.productName = productName;
        this.boughtAt = boughtAt;
        this.manufacturer = manufacturer;
        this.price = price;
        this.storeName = storeName;
        this.comparedProductName = comparedProductName;
        this.shortReview = shortReview;
        this.starPoint = starPoint;
        this.goodPoint = goodPoint;
        this.badPoint = badPoint;
        this.specData = specData;
        this.imageList = imageList;
    }
}
