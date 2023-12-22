package com.example.betteriter.fo_domain.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class ReviewResponse {
    @JsonProperty("reviews")
    private List<GetReviewResponseDto> getReviewResponseDtoList;
    private boolean hasNext;
    private boolean isExisted;

    @Builder
    public ReviewResponse(List<GetReviewResponseDto> getReviewResponseDtoList,
                          boolean hasNext, boolean isExisted
    ) {
        this.getReviewResponseDtoList = getReviewResponseDtoList;
        this.hasNext = hasNext;
        this.isExisted = isExisted;
    }
}