package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserConnector;
import com.example.betteriter.global.constant.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.betteriter.global.common.code.status.ErrorStatus._REVIEW_IMAGE_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewConnectorImpl implements ReviewConnector {
    private static final int SIZE = 7;
    private final UserConnector userConnector;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @Override
    public Map<String, List<ReviewResponseDto>> getUserCategoryReviews() {
        List<Category> categories = this.getCurrentUser().getCategories(); // 유저가 등록한 관심 카테고리
        return this.getLatest7ReviewsByCategories(categories); // 카테고리에 해당하는 최신 순 리뷰
    }

    @Override
    public List<ReviewResponseDto> getFollowingReviews() {
        Users users = this.getCurrentUser();
        return this.reviewRepository.findFirst7WrittenByFollowingCreatedAtDesc(users, PageRequest.of(0, SIZE))
                .stream()
                .map(review -> review.of(this.getFirstImageWithReview(review)))
                .collect(Collectors.toList());
    }

    /* 가장 많은 스크랩 + 좋아요 수를 가지는 리뷰 가져오는 리스트 */
    @Override
    public List<ReviewResponseDto> getMostScrapedAndLikedReviews() {
        return this.reviewRepository.findTop7ReviewHavingMostScrapedAndLiked(PageRequest.of(0, 7))
                .stream()
                .map(review -> review.of(this.getFirstImageWithReview(review)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsReviewLikeByReviewAndUsers(Review review) {
        return this.reviewService.checkCurrentUserIsLikeReview(review);
    }

    @Override
    public boolean existsReviewScrapByReviewAndUsers(Review review) {
        return this.reviewService.checkCurrentUserIsScrapReview(review);
    }

    private Map<String, List<ReviewResponseDto>> getLatest7ReviewsByCategories(List<Category> categories) {
        Map<String, List<ReviewResponseDto>> result = new LinkedHashMap<>();
        for (Category category : categories) {
            List<ReviewResponseDto> reviews =
                    this.reviewRepository.findFirst7ByCategoryOrderByCreatedAtDesc(category).stream()
                            .map(review -> review.of(this.getFirstImageWithReview(review)))
                            .collect(Collectors.toList());
            result.put(category.getCategoryName(), reviews);
        }
        return result;
    }

    /* Review 에 첫번째 이미지 가져오는 메소드 */
    private String getFirstImageWithReview(Review review) {
        List<ReviewImage> reviewImages = review.getReviewImages();
        return reviewImages.stream()
                .filter(ri -> ri.getOrderNum() == 0)
                .findFirst()
                .orElseThrow(() -> new ReviewHandler(_REVIEW_IMAGE_NOT_FOUND))
                .getImgUrl();
    }

    private Users getCurrentUser() {
        return this.reviewService.getCurrentUser();
    }
}
