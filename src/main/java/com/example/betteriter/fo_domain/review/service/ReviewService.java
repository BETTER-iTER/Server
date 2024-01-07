package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.bo_domain.menufacturer.service.ManufacturerService;
import com.example.betteriter.bo_domain.spec.service.SpecService;
import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.follow.service.FollowService;
import com.example.betteriter.fo_domain.review.domain.*;
import com.example.betteriter.fo_domain.review.dto.*;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.repository.*;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.constant.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.betteriter.global.common.code.status.ErrorStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private static final int SIZE = 7;
    private final UserService userService;
    private final SpecService specService;
    private final ManufacturerService manufacturerService;
    private final FollowService followService;

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewScrapRepository reviewScrapRepository;
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
    public ReviewResponse getReviewByCategory(Category category, int page) {
        Slice<Review> result
                = this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(category, PageRequest.of(page, SIZE));

        List<GetReviewResponseDto> reviewResponse = result.getContent().stream()
                .map(GetReviewResponseDto::of)
                .collect(Collectors.toList());

        return new ReviewResponse(reviewResponse, result.hasNext(), !result.isEmpty());
    }

    /**
     * - 상품 명 + 필터링 리뷰 조회
     * case 01 : 없다면 7일 동안 유저들이 많이 클릭한 리뷰 20개 리턴
     * case 02 : 있다면 최신순 리뷰 리스트 응답
     **/
    @Transactional(readOnly = true)
    public ReviewResponse getReviewBySearch(String name, String sort, int page) {
        // 1. 필터링 따른 상품 이름에 해당하는 리뷰 조회
        Slice<Review> reviews = getReviews(name, sort, page);

        // 2. 데이터 갯수 null 인 경우
        if (Objects.requireNonNull(reviews).isEmpty()) {
            List<GetReviewResponseDto> result = this.reviewRepository.findFirst20ByOrderByClickCountDescCreatedAtDesc()
                    .stream()
                    .map(GetReviewResponseDto::of)
                    .collect(Collectors.toList());
            return new ReviewResponse(result, false, false);
        }

        // 3. 데이터 갯수 null 아닌 경우
        List<GetReviewResponseDto> getReviewResponseDtos = reviews.getContent().stream()
                .map(GetReviewResponseDto::of)
                .collect(Collectors.toList());

        return new ReviewResponse(getReviewResponseDtos, reviews.hasNext(), true);
    }

    /* 리뷰 상세 조회 */
    @Transactional
    public ReviewDetailResponse getReviewDetail(Long reviewId) {
        // 1. reviewId 에 해당하는 리뷰 조회
        Review review = this.findReviewById(reviewId);
        Users currentUser = this.getCurrentUser();

        review.addClickCountsAndShownCounts();

        // 2. 동일한 제품명 리뷰 조회(4)
        List<Review> relatedReviews
                = this.reviewRepository.findTop4ByProductNameOrderByScrapedCntPlusLikedCntDesc(review.getProductName());

        boolean currentUserLikeReview = this.isCurrentUserLikeReview(review, currentUser);
        boolean currentUserScrapReview = this.isCurrentUserScrapReview(review, currentUser);
        boolean currentUserFollowReviewWriter = this.isCurrentUserFollowReviewWriter(review, currentUser);
        boolean isCurrentUserIsReviewWriter = this.currentUserIsReviewWriter(review, currentUser);

        if (relatedReviews.size() == 4) {
            return ReviewDetailResponse.of(review, relatedReviews,
                    currentUserLikeReview, currentUserScrapReview, currentUserFollowReviewWriter, isCurrentUserIsReviewWriter);
        }
        int remain = 4 - relatedReviews.size();
        // 3. 동일한 카테고리 중 좋아요 + 스크랩 순 정렬 조회 (나머지)
        List<Review> restRelatedReviews
                = this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(review.getCategory(), PageRequest.of(0, remain)).getContent();
        // 4. 관련 리뷰 결과
        List<Review> totalRelatedReviews = Stream.concat(relatedReviews.stream(), restRelatedReviews.stream())
                .collect(Collectors.toList());


        return ReviewDetailResponse.of(review, totalRelatedReviews,
                currentUserLikeReview, currentUserScrapReview,
                currentUserFollowReviewWriter, isCurrentUserIsReviewWriter);

    }

    /* 리뷰 상세 좋아요 조회 */
    @Transactional(readOnly = true)
    public List<ReviewLikeResponse> getReviewDetailLikes(Long reviewId) {
        // 1. reviewId 에 해당하는 리뷰 조회
        Review review = this.findReviewById(reviewId);

        // 2. review 관련 reviewLike 리스트 가져오기 & 반환
        return review.getReviewLiked().stream()
                .map(reviewLike -> ReviewLikeResponse.from(reviewLike.getUsers()))
                .collect(Collectors.toList());
    }

    /* 리뷰 상세 댓글 조회 */
    @Transactional(readOnly = true)
    public List<ReviewCommentResponse> getReviewDetailComments(Long reviewId) {
        Users currentUser = this.getCurrentUser();
        return this.findReviewById(reviewId).getReviewComment().stream()
                .map(comment -> ReviewCommentResponse.from(comment, this.isCurrentUserCommentReview(comment, currentUser)))
                .collect(Collectors.toList());
    }

    /* 리뷰 좋아요 */
    @Transactional
    public void reviewLike(Long reviewId) {
        // 1. reviewId 에 해당하는 리뷰 조회
        Review review = this.findReviewById(reviewId);
        // 2. 현재 로그인한 회원 조회
        Users currentUser = this.getCurrentUser();
        this.reviewLikeRepository.save(ReviewLike.builder().review(review).users(currentUser).build());
        // 3. 리뷰 좋아요 카운트 증가
        review.countReviewLikedCount();
    }

    /* 리뷰 좋아요 취소 */
    @Transactional
    public Void deleteReviewLike(Long reviewId) {
        // 1. reviewId 에 해당하는 리뷰 조회
        Review review = this.findReviewById(reviewId);
        ReviewLike reviewLike = checkReviewLikeValidation(review);
        this.reviewLikeRepository.delete(reviewLike);
        return null;
    }

    @NotNull
    private ReviewLike checkReviewLikeValidation(Review review) {
        // 2. reviewId 해당하는 ReviewLike 엔티티 조회
        ReviewLike reviewLike = this.reviewLikeRepository.findByReview(review)
                .orElseThrow(() -> new ReviewHandler(_REVIEW_LIKE_NOT_FOUND));
        // 3. reviewLike 의 user 와 current 유저 동일한지 체크
        if (!Objects.equals(reviewLike.getUsers().getId(), this.getCurrentUser().getId())) {
            throw new ReviewHandler(_REVIEW_LIKE_USER_NOT_MATCH);
        }
        return reviewLike;
    }

    /* 리뷰 스크랩 */
    @Transactional
    public void reviewScrap(Long reviewId) {
        // 1. reviewId 에 해당하는 리뷰 조회
        Review review = this.findReviewById(reviewId);
        // 2. 현재 로그인한 회원 조회
        Users currentUser = this.getCurrentUser();
        this.reviewScrapRepository.save(ReviewScrap.builder().review(review).users(currentUser).build());
        // 3. 리뷰 스크랩 카운트 증가
        review.countReviewScrapedCount();
    }

    /* 리뷰 삭제 */
    @Transactional
    public Void deleteReview(Long reviewId) {
        Review review = this.findReviewById(reviewId);
        if (!review.getWriter().getId().equals(this.getCurrentUser().getId())) {
            throw new ReviewHandler(_REVIEW_WRITER_IS_NOT_MATCH);
        }
        this.reviewRepository.delete(review);
        return null;
    }

    private Slice<Review> getReviews(String name, String sort, int page) {
        Slice<Review> reviews = null;
        Pageable pageable = PageRequest.of(page, SIZE);
        switch (sort) {
            // 최신순
            case "latest":
                reviews = this.reviewRepository
                        .findByProductNameOrderByCreatedAtDesc(name, pageable);
                break;
            // 좋아요 많은 순
            case "mostLiked":
                reviews = this.reviewRepository
                        .findByProductNameOrderByLikedCountDescCreatedAtDesc(name, pageable);
                break;
            // 스크랩 많은 순
            case "mostScraped":
                reviews = this.reviewRepository
                        .findByProductNameOrderByScrapedCountDescCreatedAtDesc(name, pageable);

                // 리뷰 작성자의 팔로워가 많은 순
            case "mostFollowers":
                reviews = this.reviewRepository.findByProductNameOrderByMostWriterFollower(name, pageable);
                break;
        }
        return reviews;
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

    /* 팔로우 하는 유저가 등록한 리뷰 리스트 최신순 7개 조회 메소드 */
    public List<ReviewResponseDto> getFollowingReviews() {
        Users users = this.getCurrentUser();
        return this.reviewRepository.findFirst7WrittenByFollowingCreatedAtDesc(users, PageRequest.of(0, SIZE))
                .stream()
                .map(review -> review.of(this.getFirstImageWithReview(review)))
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

    public List<Review> getReviewList(Users user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.reviewRepository.findAllByUser(user, pageable);
    }

    public List<Review> getScrapReviewList(Users user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.reviewRepository.findAllByReviewScrapedUser(user, pageable);
    }

    public List<Review> getLikeReviewList(Users user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.reviewRepository.findAllByReviewLikedUser(user, pageable);
    }

    public List<Review> getTargetReviewList(Users user) {
        return this.reviewRepository.findAllByTargetUser(user);
    }

    private boolean isCurrentUserLikeReview(Review review, Users currentUser) {
        return review.getReviewLiked().stream()
                .anyMatch(reviewLike -> reviewLike.getUsers().getId().equals(currentUser.getId()));
    }

    private boolean isCurrentUserScrapReview(Review review, Users currentUser) {
        return review.getReviewScraped().stream()
                .anyMatch(reviewScrap -> reviewScrap.getUsers().getId().equals(currentUser.getId()));
    }

    private boolean currentUserIsReviewWriter(Review review, Users currentUser) {
        return review.getWriter().getId().equals(currentUser.getId());
    }

    private boolean isCurrentUserFollowReviewWriter(Review review, Users currentUser) {
        return this.followService.isFollow(currentUser, review.getWriter());
    }

    /* 댓글 작성자와 로그인한 유저가 동일한지 확인 */
    private boolean isCurrentUserCommentReview(Comment comment, Users currentUser) {
        return currentUser.getId().equals(comment.getUsers().getId());
    }

    public Integer getReviewCount(Users user) {
        return this.reviewRepository.countByWriter(user);
    }

    public Integer getScrapCount(Users user) {
        return this.reviewRepository.countByMyScrap(user);
    }

    public Integer getTotalLikeCount(Users user) {
        return this.reviewRepository.countByReviewLiked(user);
    }

    public Integer getTotalScrapCount(Users user) {
        return this.reviewRepository.countByReviewScraped(user);
    }
}
