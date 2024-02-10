package com.example.betteriter.fo_domain.review.controller;

import com.example.betteriter.fo_domain.review.dto.*;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.global.common.response.ResponseDto;
import com.example.betteriter.global.constant.Category;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.example.betteriter.global.common.code.status.ErrorStatus._METHOD_ARGUMENT_ERROR;

@Slf4j
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
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
    public ResponseDto<GetReviewSpecResponseDto> getReviewSpecDataResponse(
        @RequestParam String category
    ) {
        return ResponseDto.onSuccess(this.reviewService.getReviewSpecDataResponse(Category.from(category)));
    }

    /* 카테고리 별 리뷰 조회 */
    @GetMapping("/category")
    public ResponseDto<ReviewResponse> getReviewsByCategory(
        @RequestParam String category,
        @RequestParam int page
    ) {
        return ResponseDto.onSuccess(this.reviewService.getReviewByCategory(Category.from(category), page));
    }

    /* 상품 명 + 필터링 리뷰 조회 */
    @GetMapping("/search")
    public ResponseDto<ReviewResponse> getReviewsBySearch(
        @RequestParam String name,
        @RequestParam String sort,
        @RequestParam int page,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Boolean expert
    ) {
        return ResponseDto.onSuccess(
            this.reviewService.getReviewBySearch(name, sort, page, Category.from(category), expert));
    }

    /* 리뷰 상세 조회 */
    @GetMapping("{reviewId}/detail")
    public ResponseDto<ReviewDetailResponse> getReviewDetail(
        @PathVariable Long reviewId
    ) {
        return ResponseDto.onSuccess(this.reviewService.getReviewDetail(reviewId));
    }

    /* 리뷰 상세 좋아요 정보 조회 */
    @GetMapping("/{reviewId}/detail/likes")
    public ResponseDto<List<ReviewLikeResponse>> getReviewDetailLikes(
        @PathVariable Long reviewId
    ) {
        return ResponseDto.onSuccess(this.reviewService.getReviewDetailLikes(reviewId));
    }

    /* 리뷰 상세 댓글 정보 조회 */
    @GetMapping("/{reviewId}/detail/comments")
    public ResponseDto<List<ReviewCommentResponse>> getReviewDetailComments(
        @PathVariable Long reviewId
    ) {
        return ResponseDto.onSuccess(this.reviewService.getReviewDetailComments(reviewId));
    }

    /* 리뷰 좋아요 */
    @PostMapping("/{reviewId}/like")
    public ResponseDto<Void> reviewLike(
        @PathVariable Long reviewId
    ) {
        this.reviewService.reviewLike(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 좋아요 취소 */
    @DeleteMapping("/{reviewId}/like")
    public ResponseDto<Void> deleteReviewLike(
        @PathVariable Long reviewId
    ) {
        this.reviewService.deleteReviewLike(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 스크랩 */
    @PostMapping("{reviewId}/scrap")
    public ResponseDto<Void> reviewScrap(
        @PathVariable Long reviewId
    ) {
        this.reviewService.reviewScrap(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 스크랩 취소 */
    @DeleteMapping("{reviewId}/scrap")
    public ResponseDto<Void> deleteReviewScrap(
        @PathVariable Long reviewId
    ) {
        this.reviewService.deleteReviewScrap(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 삭제 */
    @DeleteMapping("/{reviewId}")
    public ResponseDto<Void> deleteReview(
        @PathVariable Long reviewId
    ) {
        this.reviewService.deleteReview(reviewId);
        return ResponseDto.onSuccess(null);
    }

    /* 리뷰 수정 */
    @PutMapping("/{reviewId}")
    public ResponseDto<Void> updateReview(
        @RequestPart(value = "files") List<MultipartFile> images,
        @PathVariable Long reviewId,
        @Valid @RequestBody UpdateReviewRequestDto request,
        BindingResult bindingResult
    ) {
        this.checkRequestValidation(bindingResult);
        this.reviewService.updateReview(reviewId, request, images);
        return ResponseDto.onSuccess(null);
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            log.debug("fieldError occurs : {}", fieldError.getDefaultMessage());
            throw new ReviewHandler(_METHOD_ARGUMENT_ERROR);
        }
    }
}
