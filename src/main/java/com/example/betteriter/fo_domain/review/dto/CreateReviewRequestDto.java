package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static com.example.betteriter.global.constant.Status.ACTIVE;

@Getter
@NoArgsConstructor
public class CreateReviewRequestDto {
    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Category category; // 카테고리

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String productName; // 상품명

    @NotNull(message = "구매일자는 필수 입력 값입니다.")
    private LocalDate boughtAt; // 구매 일자

    @NotNull(message = "제조사 정보는 필수 입력 값입니다.")
    private String manufacturer; // 제조사 이름

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int price; // 가격

    @NotNull(message = "구매처 정보는 필수 입력 값입니다.")
    private int storeName; // 구매처

    @NotBlank(message = "비교 제품은 필수 입력 값입니다.")
    private String comparedProductName; // 비교 제품

    @NotBlank(message = "한줄평은 필수 입력 값입니다.")
    private String shortReview; // 한줄평

    @NotNull(message = "별점은 필수 입력 값입니다.")
    @Max(5)
    private int starPoint; // 별점

    private String goodPoint; // 좋은 점

    private String badPoint; // 나쁜 점

    private List<Long> specData; // specData id 리스트

    private List<CreateReviewImageRequestDto> images; // 리뷰 이미지

    @Builder
    public CreateReviewRequestDto(Category category, String productName, LocalDate boughtAt,
                                  String manufacturer, int price, int storeName,
                                  String comparedProductName, String shortReview, int starPoint, String goodPoint,
                                  String badPoint, List<Long> specData, List<CreateReviewImageRequestDto> images
    ) {
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
        this.images = images;
    }

    public Review toEntity(Users users, Manufacturer manufacturer) {
        return Review.builder()
                .writer(users)
                .category(category)
                .productName(productName)
                .boughtAt(boughtAt)
                .manufacturer(manufacturer)
                .price(price)
                .comparedProductName(comparedProductName)
                .storeName(storeName)
                .shortReview(shortReview)
                .starPoint(starPoint)
                .goodPoint(goodPoint)
                .badPoint(badPoint)
                .status(ACTIVE)
                .build();
    }

    @Getter
    @NoArgsConstructor
    public static class CreateReviewImageRequestDto {
        @NotBlank
        private String imgUrl;

        @Builder
        private CreateReviewImageRequestDto(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
