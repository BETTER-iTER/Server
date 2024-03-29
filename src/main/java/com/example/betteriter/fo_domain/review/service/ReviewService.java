package com.example.betteriter.fo_domain.review.service;

import static com.example.betteriter.global.common.code.status.ErrorStatus.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.service.ManufacturerConnector;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.bo_domain.spec.service.SpecConnector;
import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.follow.service.FollowConnector;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.domain.ReviewLike;
import com.example.betteriter.fo_domain.review.domain.ReviewScrap;
import com.example.betteriter.fo_domain.review.domain.ReviewSpecData;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewResponseDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewSpecResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewCommentResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewDetailResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewLikeResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewResponse;
import com.example.betteriter.fo_domain.review.dto.UpdateReviewRequestDto;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.repository.ReviewImageRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewLikeRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewScrapRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewSpecDataRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserConnector;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.infra.s3.ImageUploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private static final int SIZE = 7;
    private final UserConnector userConnector;
    private final SpecConnector specConnector;
    private final ManufacturerConnector manufacturerConnector;
    private final FollowConnector followConnector;
    private final ImageUploadService s3Service;

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewScrapRepository reviewScrapRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewSpecDataRepository reviewSpecDataRepository;

    private static boolean isSameSpecId(ReviewSpecData nowData, SpecData newData) {
        return newData.getId().equals(nowData.getSpecData().getSpec().getId());
    }

    private static boolean isChanged(SpecData newSpecData, SpecData nowSpecData) {
        return (!newSpecData.getId().equals(nowSpecData.getId())) &&
            (newSpecData.getSpec().getId().equals(nowSpecData.getSpec().getId()));
    }

    private static boolean isAdded(SpecData newSpecData, List<ReviewSpecData> nowReviewSpecDataList) {
        return nowReviewSpecDataList.stream()
            .noneMatch(nowReviewSpecData -> newSpecData.getId().equals(nowReviewSpecData.getSpecData().getId()));
    }

    /* 리뷰 등록 */
    @Transactional
    public Long createReview(CreateReviewRequestDto request, List<MultipartFile> images) {
        // 1. 리뷰 저장
        Review review = this.reviewRepository.save(request.toEntity(
            this.getCurrentUser(),
            this.manufacturerConnector.findManufacturerByName(request.getManufacturer())));

        // 2. 리뷰 이미지 저장
        this.uploadReviewImages(images, review);

        // 3. 리뷰 스펙 데이터 저장
        this.reviewSpecDataRepository.saveAll(this.getReviewSpecData(request, review));

        return review.getId();
    }

    /* 리뷰 등록시 카테고리에 해당하는 리뷰 스펙 데이터 조회 (입력 용)*/
    @Transactional(readOnly = true)
    public GetReviewSpecResponseDto getReviewSpecDataResponse(Category category) {
        return GetReviewSpecResponseDto.from(this.specConnector.findAllSpecDataByCategory(category));
    }

    /* 카테고리에 해당하는 리뷰 조회 */
    @Transactional(readOnly = true)
    public ReviewResponse getReviewByCategory(Category category, int page) {
        Slice<Review> result
            = this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(category,
            PageRequest.of(page, SIZE));

        List<GetReviewResponseDto> reviewResponse = result.getContent().stream()
            .map(review -> GetReviewResponseDto.of(review,
                this.checkCurrentUserIsScrapReview(review),
                this.checkCurrentUserIsLikeReview(review))
            ).collect(Collectors.toList());

        return new ReviewResponse(reviewResponse, result.hasNext(), !result.isEmpty());
    }

    /**
     * - 상품 명 + 필터링 리뷰 조회 case 01 : 없다면 7일 동안 유저들이 많이 클릭한 리뷰 20개 리턴 case 02 : 있다면 최신순 리뷰 리스트 응답
     **/
    @Transactional(readOnly = true)
    public ReviewResponse getReviewBySearch(String name, String sort, int page, Category category, boolean expert) {
        Pageable pageable = PageRequest.of(page, SIZE);
        // 1. 필터링 따른 상품 이름에 해당하는 리뷰 조회
        Slice<Review> reviews = reviewRepository.findReviewsBySearch(name, sort, pageable, category, expert);

        // 2. 데이터 갯수 null 인 경우
        ReviewResponse result = checkIsEmptyReviews(reviews);
        if (result != null) {
            return result;
        }

        // 3. 데이터 갯수 null 아닌 경우
        List<GetReviewResponseDto> getReviewResponseDtos = reviews.getContent().stream()
            .map(review -> GetReviewResponseDto.of(review,
                this.checkCurrentUserIsScrapReview(review),
                this.checkCurrentUserIsLikeReview(review))
            ).collect(Collectors.toList());

        return new ReviewResponse(getReviewResponseDtos, reviews.hasNext(), true);
    }

    public boolean checkCurrentUserIsLikeReview(Review review) {
        Users currentUser = this.getCurrentUser();
        return this.reviewLikeRepository.existsByReviewAndUsers(review, currentUser);
    }

    public boolean checkCurrentUserIsScrapReview(Review review) {
        Users currentUser = this.getCurrentUser();
        return this.reviewScrapRepository.existsByReviewAndUsers(review, currentUser);
    }

    @Nullable
    private ReviewResponse checkIsEmptyReviews(Slice<Review> reviews) {
        // 검색 결과 없는 경우
        if (reviews.getContent().isEmpty()) {
            List<GetReviewResponseDto> result
                = this.reviewRepository.findFirst20ByOrderByClickCountDescCreatedAtDesc().stream()
                .map(review -> GetReviewResponseDto.of(review,
                    this.checkCurrentUserIsScrapReview(review),
                    this.checkCurrentUserIsLikeReview(review))
                ).collect(Collectors.toList());
            return new ReviewResponse(result, false, false);
        }
        return null;
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
                currentUserLikeReview, currentUserScrapReview, currentUserFollowReviewWriter,
                isCurrentUserIsReviewWriter);
        }
        int remain = 4 - relatedReviews.size();
        // 3. 동일한 카테고리 중 좋아요 + 스크랩 순 정렬 조회 (나머지)
        List<Review> restRelatedReviews
            = this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(review.getCategory(),
            PageRequest.of(0, remain)).getContent();
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
        review.addReviewLikedCount();
    }

    /* 리뷰 좋아요 취소 */
    @Transactional
    public Void deleteReviewLike(Long reviewId) {
        Review review = this.findReviewById(reviewId);
        ReviewLike reviewLike = checkReviewLikeValidation(review);
        this.reviewLikeRepository.delete(reviewLike);
        review.minusReviewLikedCount();
        return null;
    }

    @NotNull
    private ReviewLike checkReviewLikeValidation(Review review) {
        Users currentUser = this.getCurrentUser();
        return this.reviewLikeRepository.findByReviewAndUsers(review, currentUser)
            .orElseThrow(() -> new ReviewHandler(_REVIEW_LIKE_NOT_FOUND));
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
        review.addReviewScrapedCount();
    }

    /* 리뷰 스크랩 취소 */
    @Transactional
    public Void deleteReviewScrap(Long reviewId) {
        Review review = this.findReviewById(reviewId);
        ReviewScrap reviewScrap = checkReviewScrapValidation(review);
        this.reviewScrapRepository.delete(reviewScrap);
        review.minusReviewScrapedCount();
        return null;
    }

    @NotNull
    private ReviewScrap checkReviewScrapValidation(Review review) {
        Users currentUser = this.getCurrentUser();
        return this.reviewScrapRepository.findByReviewAndUsers(review, currentUser)
            .orElseThrow(() -> new ReviewHandler(_REVIEW_SCRAP_NOT_FOUND));
    }

    /* 리뷰 삭제 */
    @Transactional
    public Void deleteReview(Long reviewId) {
        Review review = this.findReviewById(reviewId);
        if (!review.getWriter().getId().equals(this.getCurrentUser().getId())) {
            throw new ReviewHandler(_REVIEW_WRITER_IS_NOT_MATCH);
        }
        this.deleteReviewImages(review.getReviewImages());
        this.reviewRepository.delete(review);
        return null;
    }

    private List<ReviewSpecData> getReviewSpecData(CreateReviewRequestDto request, Review review) {
        // 요청으로 들어온 specData 조회
        return this.specConnector.findAllSpecDataByIds(request.getSpecData())
            .stream()
            .map(sd -> ReviewSpecData.createReviewSpecData(review, sd))
            .collect(Collectors.toList());
    }

    List<ReviewSpecData> getReviewSpecData(UpdateReviewRequestDto request, Review review) {
        return this.specConnector.findAllSpecDataByIds(request.getSpecData())
            .stream()
            .map(sd -> ReviewSpecData.createReviewSpecData(review, sd))
            .collect(Collectors.toList());
    }

    public Users getCurrentUser() {
        return this.userConnector.getCurrentUser();
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

    public Review findReviewById(Long reviewId) {
        return this.reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewHandler(_REVIEW_NOT_FOUND));
    }

    public Page<Review> getReviewList(Users user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.reviewRepository.findAllByUser(user, pageable);
    }

    public Page<Review> getScrapReviewList(Users user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.reviewRepository.findAllByReviewScrapedUser(user, pageable);
    }

    public Page<Review> getLikeReviewList(Users user, int page, int size) {
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
        return this.followConnector.isFollow(currentUser, review.getWriter());
    }

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

    private void uploadReviewImages(List<MultipartFile> images, Review review) {
        this.checkUploadReviewImagesRequestValidation(images);

        List<ReviewImage> reviewImages = images.stream()
            .map(image -> s3Service.uploadImage(image, review, images.indexOf(image)))
            .collect(Collectors.toList());

        reviewImageRepository.saveAll(reviewImages);
    }


    private void deleteReviewImages(List<ReviewImage> imageUrls) {
        imageUrls.forEach(imageUrl -> s3Service.deleteImages(imageUrl.getImgUrl()));
    }

    private void checkUploadReviewImagesRequestValidation(List<MultipartFile> images) {
        if (images == null || images.isEmpty() || images.size() > 5) {
            throw new ReviewHandler(_IMAGE_FILE_UPLOAD_REQUEST_IS_NOT_VALID);
        }
    }

    @Transactional
    public void updateReview(Long reviewId, UpdateReviewRequestDto request) {
        Review review = this.findReviewById(reviewId);

        // 1. 리뷰 데이터 업데이트
        this.updateReviewData(request, review);

        // 2. 리뷰 이미지 업데이트
        this.clearReviewImages(review);
        this.updateReviewImages(review, request.getImageList());

        // 3. 리뷰 스펙 데이터 업데이트
        this.clearReviewSpecData(review);

        List<SpecData> newSpecDataList = this.specConnector.findAllSpecDataByIds(request.getSpecData());
        this.updateReviewSpecData(review, newSpecDataList);

        // 4. 필요없는 리뷰 이미지 삭제
        // this.clearS3ReviewImage(review);
    }

    private void clearS3ReviewImage(Review review) {
        List<String> s3ReviewImageUrls = this.getReviewImageUrls(review);
        List<String> activeReviewImageUrls = review.getReviewImages().stream()
            .map(ReviewImage::getImgUrl)
            .collect(Collectors.toList());

        s3ReviewImageUrls.stream()
            .filter(s3ReviewImageUrl -> !activeReviewImageUrls.contains(s3ReviewImageUrl))
            .forEach(s3Service::deleteImages);
    }

    private void updateReviewData(UpdateReviewRequestDto request, Review review) {
        Manufacturer manufacturer = null;
        if (!request.getManufacturer().isEmpty()) {
            manufacturer = manufacturerConnector.findManufacturerByName(request.getManufacturer());
        }

        review.updateReview(request, manufacturer);

        reviewRepository.save(review);
    }

    private void updateReviewImages(Review review, List<String> imageList) {
        for (int i = 0; i < imageList.size(); i++) {
            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .imgUrl(imageList.get(i))
                    .orderNum(i)
                    .build();

            log.debug("reviewImageId : {}", reviewImage.getId());
            log.debug("review : {}", review);
            log.debug("imgUrl : {}", imageList.get(i));
            log.debug("orderNum : {}", i);

            reviewImageRepository.save(reviewImage);
        }
    }

    private void clearReviewImages(Review review) {
        List<ReviewImage> nowReviewImages = review.getReviewImages();
        // review.getReviewImages().removeAll(nowReviewImages);

        List<Long> reviewImageIds = nowReviewImages.stream()
                .map(ReviewImage::getId)
                .collect(Collectors.toList());

        reviewImageRepository.deleteAllByIdInBatch(reviewImageIds);
    }


    private void updateReviewSpecData(Review review, List<SpecData> newSpecDataList) {
        List<ReviewSpecData> newReviewSpecDataList = newSpecDataList.stream()
            .map(specData -> ReviewSpecData.createReviewSpecData(review, specData))
            .collect(Collectors.toList());
        reviewSpecDataRepository.saveAll(newReviewSpecDataList);
    }

    private void clearReviewSpecData(Review review) {
        List<ReviewSpecData> nowReviewSpecDataList = review.getSpecData();
        review.getSpecData().removeAll(nowReviewSpecDataList);
        reviewSpecDataRepository.deleteAll(nowReviewSpecDataList);
    }

    private boolean isChanged(SpecData newSpecData, List<SpecData> nowSpecDataList) {
        return nowSpecDataList.stream()
            .anyMatch(now -> isChanged(newSpecData, now));
    }

    public String getTemporaryReviewImageUrl(Long reviewId, MultipartFile image) {
        Review review = this.findReviewById(reviewId);
        this.checkReviewOwner(review);
        return s3Service.uploadTemporaryImage(image, review);
    }

    private void checkReviewOwner(Review review) {
        this.checkReviewOwner(review.getId());
    }

    public void checkReviewOwner(Long reviewId) {
        Review review = this.findReviewById(reviewId);
        if (!review.getWriter().getId().equals(this.getCurrentUser().getId())) {
            throw new ReviewHandler(_REVIEW_WRITER_IS_NOT_MATCH);
        }
    }

    public List<String> getReviewImageUrls(Review review) {
        return s3Service.getReviewImageUrlsInS3(review);
    }
}
