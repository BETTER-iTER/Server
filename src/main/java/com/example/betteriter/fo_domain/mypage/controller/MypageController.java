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

import javax.validation.Valid;
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
    @GetMapping("/review/mine/{page}")
    public ResponseDto<MypageResponse.ReviewListDto> getReview(
            @PathVariable @Valid Integer page
    ) {
        List<Review> reviewList = mypageService.getMyReviewList(page - 1);
        return ResponseDto.onSuccess(MypageResponseConverter.toReviewListDto(reviewList));
    }

    /**
     * 내가 스크랩한 리뷰 조회
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/scrap/{page}")
    public ResponseDto<MypageResponse.ReviewListDto> getScrapReview(
            @PathVariable Integer page
    ) {
        List<Review> reviewList = mypageService.getScrapReviewList(page - 1);
        return ResponseDto.onSuccess(MypageResponseConverter.toReviewListDto(reviewList));
    }

    /**
     * 내가 좋아요 한 리뷰 조회
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/like/{page}")
    public ResponseDto<MypageResponse.ReviewListDto> getLikeReview(
            @PathVariable Integer page
    ) {
        List<Review> reviewList = mypageService.getLikeReviewList(page - 1);
        return ResponseDto.onSuccess(MypageResponseConverter.toReviewListDto(reviewList));
    }

    /**
     * 팔로워 조회 (나를 팔로우 하는 사람들)
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.FollowerDto>
     */
    @GetMapping("/follower/{page}")
    public ResponseDto<MypageResponse.FollowerListDto> getFollower(
            @PathVariable Integer page
    ) {
        List<Users> followerList = mypageService.getFollowerList(page - 1);
        Integer totalCount = mypageService.getFollowerCount();
        return ResponseDto.onSuccess(MypageResponseConverter.toFollowerListDto(followerList, totalCount));
    }

    /**
     * 팔로잉 조회 (내가 팔로우 하는 사람들)
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.FollowerDto>
     */
    @GetMapping("/followee/{page}")
    public ResponseDto<MypageResponse.FollowerListDto> getFollowee(
            @PathVariable Integer page
    ) {
        List<Users> followeeList = mypageService.getFolloweeList(page - 1); // TODO: service 한번에 구하거나 users entity count 필드 추가
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
