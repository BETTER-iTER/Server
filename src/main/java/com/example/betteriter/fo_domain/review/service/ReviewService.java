package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.bo_domain.menufacturer.service.ManufacturerService;
import com.example.betteriter.bo_domain.spec.service.SpecService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.domain.ReviewSpecData;
import com.example.betteriter.fo_domain.review.dto.*;
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
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.betteriter.global.common.code.status.ErrorStatus._REVIEW_IMAGE_NOT_FOUND;
import static com.example.betteriter.global.common.code.status.ErrorStatus._REVIEW_NOT_FOUND;

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
                this.manufacturerService.findManufacturerByName(request.getManufacturer())));

        // 2. 리뷰 이미지 저장
        this.reviewImageRepository.saveAll(this.getReviewImages(review, request));

        // 3. 리뷰 스펙 데이터 저장
        this.reviewSpecDataRepository.saveAll(this.getReviewSpecData(request, review));
        return review.getId();
    }

    /* 리뷰 등록시 카테고리에 해당하는 리뷰 스펙 데이터 조회 (입력 용)*/
    @Transactional(readOnly = true)
    public GetReviewSpecResponseDto getReviewSpecDataResponse(Category category) {
        return GetReviewSpecResponseDto.from(this.specService.findAllSpecDataByCategory(category));
    }

    /* 카테고리에 해당하는 리뷰 조회 */
    @Transactional(readOnly = true)
    public ReviewResponse getReviewByCategory(Category category) {
        Slice<Review> result = this.reviewRepository.findReviewByCategory(category, PageRequest.of(0, 5));
        List<GetReviewResponseDto> reviewResponse = result.getContent().stream()
                .map(GetReviewResponseDto::of)
                .collect(Collectors.toList());
        return new ReviewResponse(reviewResponse, result.hasNext());
    }

    /**
     * - 이름에 해당하는 리뷰 조회
     * case 01 : 없다면 7일 동안 유저들이 많이 클릭한 리뷰 20개 리턴
     * case 02 : 있다면 최신순 리뷰 리스트 응답
     **/
    @Transactional(readOnly = true)
    public ReviewResponse getReviewBySearch(String name) {

        // 1. 이름에 해당하는 최신순 리뷰 조회
        Slice<Review> latestReview
                = this.reviewRepository.findByProductNameOrderByCreatedAtDesc(name, PageRequest.of(0, 5));

        // 2. 데이터 갯수 null 인 경우
        if (latestReview.isEmpty()) {
            List<GetReviewResponseDto> result = this.reviewRepository.findFirst20ByOrderByClickCountDescCreatedAtDesc(name)
                    .stream()
                    .map(GetReviewResponseDto::of)
                    .collect(Collectors.toList());
            return new ReviewResponse(result, false);
        }

        // 3. 데이터 갯수 null 아닌 경우
        List<GetReviewResponseDto> getReviewResponseDtos = latestReview.getContent().stream()
                .map(GetReviewResponseDto::of)
                .collect(Collectors.toList());

        return new ReviewResponse(getReviewResponseDtos, latestReview.hasNext());
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
        return this.getLatest7ReviewsByCategories(categories); // 카테고리에 해당하는 최신 순 리뷰
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
        List<ReviewImage> reviewImages = review.getReviewImages();
        return reviewImages.stream()
                .filter(ri -> ri.getOrderNum() == 0)
                .findFirst()
                .orElseThrow(() -> new ReviewHandler(_REVIEW_IMAGE_NOT_FOUND))
                .getImgUrl();
    }

    private List<ReviewImage> getReviewImages(Review review, CreateReviewRequestDto request) {
        return request.getImages().stream()
                .map(r -> ReviewImage.createReviewImage(review, r.getImgUrl(), request.getImages().indexOf(r)))
                .collect(Collectors.toList());
    }

    public Review findReviewById(Long reviewId) {
        return this.reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewHandler(_REVIEW_NOT_FOUND));
    }
}
