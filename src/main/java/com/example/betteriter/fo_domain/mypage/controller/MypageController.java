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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param page 페이지 번호
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/mine")
    public ResponseDto<MypageResponse.ReviewListDto> getReview(
            @RequestParam int page
    ) {
        List<Review> reviewList = mypageService.getMyReviewList(page);
        return ResponseDto.onSuccess(MypageResponseConverter.toReviewListDto(reviewList));
    }

    /**
     * 내가 스크랩한 리뷰 조회
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/scrap")
    public ResponseDto<MypageResponse.ReviewListDto> getScrapReview(
            @RequestParam int page
    ) {
        List<Review> reviewList = mypageService.getScrapReviewList(page);
        return ResponseDto.onSuccess(MypageResponseConverter.toReviewListDto(reviewList));
    }

    /**
     * 내가 좋아요 한 리뷰 조회
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/like")
    public ResponseDto<MypageResponse.ReviewListDto> getLikeReview(
            @RequestParam int page
    ) {
        List<Review> reviewList = mypageService.getLikeReviewList(page);
        return ResponseDto.onSuccess(MypageResponseConverter.toReviewListDto(reviewList));
    }

    /**
     * 팔로워 조회 (나를 팔로우 하는 사람들)
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.FollowerDto>
     */
    @GetMapping("/followers")
    public ResponseDto<MypageResponse.FollowerListDto> getFollower(
            @RequestParam int page
    ) {
        List<Users> followerList = mypageService.getFolloweeList(page);
        Integer totalCount = mypageService.getFollowerCount();
        return ResponseDto.onSuccess(MypageResponseConverter.toFollowerListDto(followerList, totalCount));
    }

    /**
     * 팔로잉 조회 (내가 팔로우 하는 사람들)
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.FollowerDto>
     */
    @GetMapping("/followings")
    public ResponseDto<MypageResponse.FollowerListDto> getFollowee(
            @RequestParam int page
    ) {
        List<Users> followeeList = mypageService.getFolloweeList(page); // TODO: service 한번에 구하거나 users entity count 필드 추가
        Integer totalCount = mypageService.getFolloweeCount();
        return ResponseDto.onSuccess(MypageResponseConverter.toFollowerListDto(followeeList, totalCount));
    }

    /**
     * user profile 조회
     *
     * @return MypageResponse.UserProfileDto
     */
    @GetMapping("/profile")
    public ResponseDto<MypageResponse.UserProfileDto> getUserProfile() {
        MypageResponse.UserProfileDto result = mypageService.getUserProfile();
        return ResponseDto.onSuccess(result);
    }

    /**
     * user point detail 조회
     *
     * @return MypageResponse.PointDetailDto
     */
    @GetMapping("/point")
    public ResponseDto<MypageResponse.PointDetailDto> getPointDetail() {
        Users user = mypageService.getCurrentUser();
        Integer totalLikeCount = mypageService.getTotalLikeCount(user);
        Integer totalScrapCount = mypageService.getTotalScrapCount(user);
        return ResponseDto.onSuccess(MypageResponseConverter.toPointDetailDto(user, totalLikeCount, totalScrapCount));
    }
}
