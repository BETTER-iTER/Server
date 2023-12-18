package com.example.betteriter.fo_domain.review.controller;

import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewSpecResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewResponse;
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

import javax.validation.Valid;

import static com.example.betteriter.global.common.code.status.ErrorStatus._METHOD_ARGUMENT_ERROR;

@Tag(name = "ReviewController", description = "Review API")
@Slf4j
@RequestMapping("/review")
@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    /* 리뷰 등록 API */
    @PostMapping
    public ResponseDto<Long> createReview(
            @Valid @RequestBody CreateReviewRequestDto request,
            BindingResult bindingResult
    ) {
        this.checkRequestValidation(bindingResult);
        return ResponseDto.onSuccess(this.reviewService.createReview(request));
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
            @RequestParam String category
    ) {
        return ResponseDto.onSuccess(this.reviewService.getReviewByCategory(Category.from(category)));
    }

    /* 이름으로 리뷰 조회 */
    @GetMapping("/search")
    public ResponseDto<ReviewResponse> getReviewsBySearch(
            @RequestParam String name,
            @RequestParam String sort
    ) {
        return ResponseDto.onSuccess(this.reviewService.getReviewBySearch(name, sort));
    }


    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            log.debug("fieldError occurs : {}", fieldError.getDefaultMessage());
            throw new ReviewHandler(_METHOD_ARGUMENT_ERROR);
        }
    }
}
