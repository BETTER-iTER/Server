package com.example.betteriter.fo_domain.mypage.controller;


import com.example.betteriter.fo_domain.mypage.converter.MypageResponseConverter;
import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.mypage.service.MypageService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;


@Tag(name = "MypageControllers", description = "Mypage API")
@Slf4j
@RequestMapping("/mypage")
@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;

    /**
     * 내가 쓴 리뷰 조회
     *
     * @param id 사용자 id
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/{id}")
    public ResponseDto<List<MypageResponse.MyReviewDto>> getReview(
            @PathVariable Long id
    ) {
        List<Review> reviewList;
        if (mypageService.checkUserSelf(id)) reviewList = mypageService.getMyReviewList();
        else reviewList = mypageService.getTargetReviewList(id);

        return ResponseDto.onSuccess(MypageResponseConverter.toMyReviewDtoList(reviewList));
    }

    /**
     * 내가 쓴 리뷰 조회
     *
     * @param id 사용자 id
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/scrap/{id}")
    public ResponseDto<List<MypageResponse.MyReviewDto>> getScrapReview(
            @PathVariable Long id
    ) {
        List<Review> reviewList = mypageService.getScrapReviewList(id);
        return ResponseDto.onSuccess(MypageResponseConverter.toMyReviewDtoList(reviewList));
    }

    /**
     * 내가 쓴 리뷰 조회
     *
     * @param id 사용자 id
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/like/{id}")
    public ResponseDto<List<MypageResponse.MyReviewDto>> getLikeReview(
            @PathVariable Long id
    ) {
        List<Review> reviewList = mypageService.getLikeReviewList(id);
        return ResponseDto.onSuccess(MypageResponseConverter.toMyReviewDtoList(reviewList));
    }

    /**
     * 팔로워 조회 (내가 팔로우한 사람)
     *
     * @param id 사용자 id
     * @return List<MypageResponse.FollowerDto>
     */
    @GetMapping("/follower/{id}")
    public ResponseDto<List<MypageResponse.FollowerDto>> getFollower(
            @PathVariable Long id
    ) {
        List<Users> followerList = mypageService.getFollowerList(id);
        return ResponseDto.onSuccess(MypageResponseConverter.toFollowerDtoList(followerList));
    }

    /**
     * 팔로잉 조회 (나를 팔로우한 사람)
     *
     * @param id 사용자 id
     * @return List<MypageResponse.FollowerDto>
     */
    @GetMapping("/followee/{id}")
    public ResponseDto<List<MypageResponse.FollowerDto>> getFollowee(
            @PathVariable Long id
    ) {
        List<Users> followeeList = mypageService.getFolloweeList(id);
        return ResponseDto.onSuccess(MypageResponseConverter.toFollowerDtoList(followeeList));
    }
}
