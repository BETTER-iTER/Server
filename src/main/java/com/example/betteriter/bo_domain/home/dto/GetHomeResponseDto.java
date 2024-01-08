package com.example.betteriter.bo_domain.home.dto;

import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * - 홈 화면에 보여지는 응답 데이터
 **/
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"news", "categories", "interestedCategoryReviews", "followingReviews", "mostScrapedAndLikedReviews"})
public class GetHomeResponseDto {
    List<ITNewsResponseDto> news; // IT News
    List<CategoryResponseDto> categories; // Categories
    @JsonProperty("interestedCategoryReviews")
    Map<String, List<ReviewResponseDto>> categoryReviews; // Category 해당하는 리뷰
    List<ReviewResponseDto> followingReviews; // 팔로잉 리뷰
    List<ReviewResponseDto> mostScrapedAndLikedReviews; // 좋아요 + 스크랩 수가 가장 많은 리뷰

    @Builder
    public GetHomeResponseDto(List<ITNewsResponseDto> news, List<CategoryResponseDto> categories,
                              Map<String, List<ReviewResponseDto>> categoryReviews,
                              List<ReviewResponseDto> followingReviews,
                              List<ReviewResponseDto> mostScrapedAndLikedReviews
    ) {
        this.news = news;
        this.categories = categories;
        this.categoryReviews = categoryReviews;
        this.followingReviews = followingReviews;
        this.mostScrapedAndLikedReviews = mostScrapedAndLikedReviews;
    }
}
