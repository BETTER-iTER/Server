package com.example.betteriter.bo_domain.home.dto;

import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * - 홈 화면에 보여지는 응답 데이터
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetHomeResponseDto {
    List<ITNewsResponseDto> news; // IT News
    List<CategoryResponseDto> categories; // Categories
    Map<String, List<ReviewResponseDto>> categoryReviews; // Category 해당하는 리뷰
    List<ReviewResponseDto> followingReviews; // 팔로잉 리뷰
    List<ReviewResponseDto> mostScrapedAndLikedReviews; // 좋아요 + 스크랩 수가 가장 많은 리뷰
}
