package com.example.betteriter.fo_domain.mypage.controller;


import com.example.betteriter.fo_domain.mypage.converter.MypageResponseConverter;
import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.mypage.service.MypageService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "MypageControllers", description = "Mypage API")
@Slf4j
@RequestMapping("/mypage")
@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/review?user_id={user_id}")
    public ResponseDto<List<MypageResponse.MyReviewDto>> getReview(
            @RequestParam(value = "user_id", required = false) Long user_id
    ) {
        List<Review> reviewList;
        if (mypageService.checkUserSelf(user_id)) reviewList = mypageService.getMyReviewList();
        else reviewList = mypageService.getTargetReviewList(user_id);

        return ResponseDto.onSuccess(MypageResponseConverter.toMyReviewDtoList(reviewList));
    }

    @GetMapping("/review/scrap/user_id={user_id}")
    public ResponseDto<List<MypageResponse.MyReviewDto>> getScrapReview(
            @RequestParam(value = "user_id", required = false) Long user_id
    ) {
        List<Review> reviewList = mypageService.getScrapReviewList(user_id);
        return ResponseDto.onSuccess(MypageResponseConverter.toMyReviewDtoList(reviewList));
    }

    @GetMapping("/review/like/user_id={user_id}")
    public ResponseDto<List<MypageResponse.MyReviewDto>> getLikeReview(
            @RequestParam(value = "user_id", required = false) Long user_id
    ) {
        List<Review> reviewList = mypageService.getLikeReviewList(user_id);
        return ResponseDto.onSuccess(MypageResponseConverter.toMyReviewDtoList(reviewList));
    }
}
