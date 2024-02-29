package com.example.betteriter.fo_domain.review.controller;

import static com.example.betteriter.global.common.code.status.ErrorStatus._METHOD_ARGUMENT_ERROR;

import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewSpecResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewCommentResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewDetailResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewLikeResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewResponse;
import com.example.betteriter.fo_domain.review.dto.UpdateReviewRequestDto;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.global.common.response.ResponseDto;
import com.example.betteriter.global.constant.Category;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Tag(name = "ReviewController", description = "Review API")
public class ReviewController {

    private final ReviewService reviewService;

    /* 리뷰 등록 API */
    @PostMapping
    public ResponseDto<Long> createReview(
        @RequestPart(value = "files") List<MultipartFile> images,
        @Valid @RequestPart(value = "review") CreateReviewRequestDto request,
        BindingResult bindingResult
    ) {
        this.checkRequestValidation(bindingResult);
        return ResponseDto.onSuccess(this.reviewService.createReview(request, images));
    }


    /* 리뷰 등록시 스펙 데이터 조회 API */
    @GetMapping("/spec/data")
    public ResponseDto<GetReviewSpecResponseDto> getReviewSpecDataResponse(@RequestParam String category) {
        return ResponseDto.onSuccess(this.reviewService.getReviewSpecDataResponse(Category.from(category)));
    }

    /* 카테고리 별 리뷰 조회 */
    @GetMapping("/category")
    public ResponseDto<ReviewResponse> getReviewsByCategory(@RequestParam String category, @RequestParam int page) {
        return ResponseDto.onSuccess(this.reviewService.getReviewByCategory(Category.from(category), page));
    }

    /* 상품 명 + 필터링 리뷰 조회 */
    @GetMapping("/search")
    public ResponseDto<ReviewResponse> getReviewsBySearch(@RequestParam String name, @RequestParam String sort,
        @RequestParam int page, @RequestParam(required = false) String category,
        @RequestParam(required = false) Boolean expert) {
        return ResponseDto.onSuccess(
            this.reviewService.getReviewBySearch(name, sort, page, Category.from(category), expert));
    }

    /* 리뷰 상세 조회 */
    @GetMapping("{reviewId}/detail")
    public ResponseDto<ReviewDetailResponse> getReviewDetail(@PathVariable Long reviewId) {
        return ResponseDto.onSuccess(this.reviewService.getReviewDetail(reviewId));
    }

    /* 리뷰 상세 좋아요 정보 조회 */
    @GetMapping("/{reviewId}/detail/likes")
    public ResponseDto<List<ReviewLikeResponse>> getReviewDetailLikes(@PathVariable Long reviewId) {
        return ResponseDto.onSuccess(this.reviewService.getReviewDetailLikes(reviewId));
    }

    /* 리뷰 상세 댓글 정보 조회 */
    @GetMapping("/{reviewId}/detail/comments")
    public ResponseDto<List<ReviewCommentResponse>> getReviewDetailComments(@PathVariable Long reviewId) {
        return ResponseDto.onSuccess(this.reviewService.getReviewDetailComments(reviewId));
    }

    /* 리뷰 좋아요 */
    @PostMapping("/{reviewId}/like")
    public ResponseDto<Void> reviewLike(@PathVariable Long reviewId) {
        this.reviewService.reviewLike(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 좋아요 취소 */
    @DeleteMapping("/{reviewId}/like")
    public ResponseDto<Void> deleteReviewLike(@PathVariable Long reviewId) {
        this.reviewService.deleteReviewLike(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 스크랩 */
    @PostMapping("{reviewId}/scrap")
    public ResponseDto<Void> reviewScrap(@PathVariable Long reviewId) {
        this.reviewService.reviewScrap(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 스크랩 취소 */
    @DeleteMapping("{reviewId}/scrap")
    public ResponseDto<Void> deleteReviewScrap(@PathVariable Long reviewId) {
        this.reviewService.deleteReviewScrap(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 삭제 */
    @DeleteMapping("/{reviewId}")
    public ResponseDto<Void> deleteReview(@PathVariable Long reviewId) {
        this.reviewService.deleteReview(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 수정 */
    @PutMapping("/{reviewId}")
    public ResponseDto<Void> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequestDto request,
            BindingResult bindingResult
    ) {
        this.checkRequestValidation(bindingResult);
        this.reviewService.checkReviewOwner(reviewId);
        this.reviewService.updateReview(reviewId, request);
        return ResponseDto.onSuccess(null);
    }

    @PostMapping("/image/{reviewId}")
    public ResponseDto<String> getTemporaryReviewImageUrl(
            @PathVariable Long reviewId,
            @RequestPart(value = "file") MultipartFile images
    ) {
        String imageUrl = this.reviewService.getTemporaryReviewImageUrl(reviewId, images);
        return ResponseDto.onSuccess(imageUrl);
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            log.debug("fieldError occurs : {}", fieldError.getDefaultMessage());
            throw new ReviewHandler(_METHOD_ARGUMENT_ERROR);
        }
    }
}
