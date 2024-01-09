package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;

import java.util.List;
import java.util.Map;

public interface ReviewConnector {
    Map<String, List<ReviewResponseDto>> getUserCategoryReviews();

    List<ReviewResponseDto> getFollowingReviews();

    List<ReviewResponseDto> getMostScrapedAndLikedReviews();

    boolean existsReviewLikeByReviewAndUsers(Review review);

    boolean existsReviewScrapByReviewAndUsers(Review review);
}
