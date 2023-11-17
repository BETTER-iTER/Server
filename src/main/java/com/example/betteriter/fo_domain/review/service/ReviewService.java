package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.service.ManufacturerService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto.CreateReviewImageRequestDto;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.repository.ReviewImageRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.user.domain.Follow;
import com.example.betteriter.fo_domain.user.domain.User;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.betteriter.global.error.exception.ErrorCode.REVIEW_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final UserService userService;
    private final ManufacturerService manufacturerService;

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;


    /* 리뷰 등록 */
    @Transactional
    public Long createReview(CreateReviewRequestDto request) {
        Manufacturer manufacturer = this.manufacturerService.findManufacturerById(request.getManufacturerId());
        Review review = this.reviewRepository.save(request.toEntity(manufacturer));
        this.saveReviewImagesFromRequest(request, review);
        return review.getId();
    }

    /* 유저가 관심 등록한 카테고리 리뷰 리스트 조회 메소드 */
    public Map<String, List<ReviewResponseDto>> getUserCategoryReviews() {
        List<Category> categories = this.getCurrentUser().getCategories(); // 유저가 등록한 관심 카테고리
        Map<String, List<ReviewResponseDto>> result = new LinkedHashMap<>();
        // 카테고리에 해당하는 최신 순 리뷰
        for (Category category : categories) {
            List<ReviewResponseDto> reviews = this.reviewRepository.findFirst7ByCategoryOrderByCreatedAtDesc(category).stream()
                    .map(review -> review.of(this.getFirstImageWithReview(review)))
                    .collect(Collectors.toList());
            result.put(category.getName(), reviews);
        }
        return result;
    }

    /* 팔로우 하는 유저가 등록한 리뷰 리스트 조회 메소드 */
    public List<ReviewResponseDto> getFollowingReviews() {
        User user = this.getCurrentUser();
        List<User> followee = user.getFollowing().stream()
                .map(Follow::getFollowee)
                .collect(Collectors.toList());

        return this.reviewRepository.findFirst7ByWriterInOrderByCreatedAtDesc(followee)
                .stream().map(review -> review.of(this.getFirstImageWithReview(review)))
                .collect(Collectors.toList());
    }

    /* 가장 많은 스크랩 + 좋아요 수를 가지는 리뷰 가져오는 리스트 */
    public List<ReviewResponseDto> getMostScrapedAndLikedReviews() {
        return this.reviewRepository.findTop7ReviewHavingMostScrapedAndLiked(PageRequest.of(0, 7))
                .stream()
                .map(review -> review.of(this.getFirstImageWithReview(review)))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        return this.userService.getCurrentUser();
    }

    /* Review 에 첫번째 이미지 가져오는 메소드 */
    private String getFirstImageWithReview(Review review) {
        return this.reviewImageRepository.findFirstImageWithReview(review);
    }

    private void saveReviewImagesFromRequest(CreateReviewRequestDto request, Review review) {
        List<CreateReviewImageRequestDto> images = request.getImages();
        for (CreateReviewImageRequestDto image : images) {
            reviewImageRepository.save(image.toEntity(images.indexOf(image), review));
        }
    }

    public Review findReviewById(Long reviewId) {
        return this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewHandler(REVIEW_NOT_FOUND));
    }
}
