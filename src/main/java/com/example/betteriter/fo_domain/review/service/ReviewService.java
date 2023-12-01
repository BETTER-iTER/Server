package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.bo_domain.menufacturer.service.ManufacturerService;
import com.example.betteriter.bo_domain.spec.service.SpecService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.domain.ReviewSpecData;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewSpecResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.repository.ReviewImageRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewSpecDataRepository;
import com.example.betteriter.fo_domain.user.domain.Follow;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.constant.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.betteriter.global.common.code.status.ErrorStatus.REVIEW_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final UserService userService;
    private final SpecService specService;
    private final ManufacturerService manufacturerService;

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewSpecDataRepository reviewSpecDataRepository;


    /* 리뷰 등록 */
    @Transactional
    public Long createReview(CreateReviewRequestDto request) {
        // 1. 리뷰 저장
        Review review = this.reviewRepository.save(request.toEntity(
                this.userService.getCurrentUser(),
                this.manufacturerService.findManufacturerById(request.getManufacturerId()), this.getReviewImages(request)));

        // 2. 리뷰 스펙 데이터 저장
        this.reviewSpecDataRepository.saveAll(this.getReviewSpecData(request, review));
        return review.getId();
    }

    /* 리뷰 등록시 카테고리에 해당하는 리뷰 스펙 데이터 조회 (입력 용)*/
    @Transactional(readOnly = true)
    public GetReviewSpecResponseDto getReviewSpecDataResponse(Category category) {
        return GetReviewSpecResponseDto.from(this.specService.findAllSpecDataByCategory(category));
    }

    private List<ReviewSpecData> getReviewSpecData(CreateReviewRequestDto request, Review review) {
        // 요청으로 들어온 specData 조회
        return this.specService.findAllSpecDataByIds(request.getSpecData())
                .stream()
                .map(sd -> ReviewSpecData.createReviewSpecData(review, sd))
                .collect(Collectors.toList());
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
            result.put(category.getCategoryName(), reviews);
        }
        return result;
    }

    /* 팔로우 하는 유저가 등록한 리뷰 리스트 조회 메소드 */
    public List<ReviewResponseDto> getFollowingReviews() {
        Users users = this.getCurrentUser();
        List<Users> followee = users.getFollowing().stream()
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

    private Users getCurrentUser() {
        return this.userService.getCurrentUser();
    }

    /* Review 에 첫번째 이미지 가져오는 메소드 */
    private String getFirstImageWithReview(Review review) {
        return this.reviewImageRepository.findFirstImageWithReview(review);
    }

    private List<ReviewImage> getReviewImages(CreateReviewRequestDto request) {
        return request.getImages().stream()
                .map(r -> ReviewImage.createReviewImage(r.getImgUrl(), request.getImages().indexOf(r)))
                .collect(Collectors.toList());
    }

    public Review findReviewById(Long reviewId) {
        return this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewHandler(REVIEW_NOT_FOUND));
    }
}
