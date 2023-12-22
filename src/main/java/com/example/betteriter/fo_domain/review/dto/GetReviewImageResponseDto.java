package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetReviewImageResponseDto {
    private String imgUrl;
    private int orderNum;

    @Builder
    public GetReviewImageResponseDto(String imgUrl, int orderNum) {
        this.imgUrl = imgUrl;
        this.orderNum = orderNum;
    }

    public static List<GetReviewImageResponseDto> of(List<ReviewImage> reviewImages) {
        return reviewImages.stream()
                .map(ri -> new GetReviewImageResponseDto(ri.getImgUrl(), ri.getOrderNum()))
                .collect(Collectors.toList());
    }
}