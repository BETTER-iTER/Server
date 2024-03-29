package com.example.betteriter.fo_domain.mypage.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.betteriter.fo_domain.mypage.converter.MypageResponseConverter;
import com.example.betteriter.fo_domain.mypage.dto.MypageRequest;
import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.mypage.service.MypageService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.dto.ReviewResponse;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Tag(name = "MypageControllers", description = "Mypage API")
@Slf4j
@RequestMapping("/mypage")
@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;
    private final MypageResponseConverter mypageResponseConverter;

    /**
     * 내가 쓴 리뷰 조회
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/mine/{page}")
    public ResponseDto<MypageResponse.ReviewListDto> getReview(
            @PathVariable int page
    ) {
        Page<Review> reviewList = mypageService.getMyReviewList(page);
        return ResponseDto.onSuccess(mypageResponseConverter.toReviewListDto(reviewList));
    }

    /**
     * 내가 스크랩한 리뷰 조회
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/mine/scrap/{page}")
    public ResponseDto<MypageResponse.ReviewListDto> getScrapReview(
            @PathVariable int page
    ) {
        Page<Review> reviewList = mypageService.getScrapReviewList(page);
        return ResponseDto.onSuccess(mypageResponseConverter.toReviewListDto(reviewList));
    }

    /**
     * 내가 좋아요 한 리뷰 조회
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.MyReviewDto>
     */
    @GetMapping("/review/mine/like/{page}")
    public ResponseDto<ReviewResponse> getLikeReview(
            @PathVariable int page
    ) {
        Page<Review> reviewList = mypageService.getLikeReviewList(page);
        return ResponseDto.onSuccess(mypageResponseConverter.toReviewResponse(reviewList));
    }

    /**
     * 팔로워 조회 (나를 팔로우 하는 사람들)
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.FollowerDto>
     */
    @GetMapping("/followers/mine/{page}")
    public ResponseDto<MypageResponse.FollowerListDto> getFollower(
            @PathVariable int page
    ) {
        List<Users> followerList = mypageService.getFollowerList(page);
        Integer totalCount = mypageService.getFollowerCount();
        return ResponseDto.onSuccess(MypageResponseConverter.toFollowerListDto(followerList, totalCount));
    }

    /**
     * 팔로잉 조회 (내가 팔로우 하는 사람들)
     *
     * @param page 페이지 번호
     * @return List<MypageResponse.FollowerDto>
     */
    @GetMapping("/followings/mine/{page}")
    public ResponseDto<MypageResponse.FollowerListDto> getFollowee(
            @PathVariable int page
    ) {
        List<Users> followeeList = mypageService.getFollowingList(page);
        Integer totalCount = mypageService.getFollowingCount();
        return ResponseDto.onSuccess(MypageResponseConverter.toFollowerListDto(followeeList, totalCount));
    }

    /**
     * my user profile 조회
     *
     * @return MypageResponse.UserProfileDto
     */
    @GetMapping("/profile/mine")
    public ResponseDto<MypageResponse.MyProfileDto> getUserProfile() {
        MypageResponse.MyProfileDto result = mypageService.getMyProfile();
        return ResponseDto.onSuccess(result);
    }

    /**
     * user page 조회
     *
     * @param userId 조회할 user id
     * @return MypageResponse.UserProfileDto
     */
    @GetMapping("/profile/{userId}/{page}")
    public ResponseDto<MypageResponse.UserPageDto> getUserProfile(
            @PathVariable Long userId,
            @PathVariable int page
    ) {
        Users user = mypageService.getCurrentUser();
        MypageResponse.UserProfileDto profile = mypageService.getUserProfile(userId);
        Page<Review> reviewPage = mypageService.getUserReviewList(userId, page);
        return ResponseDto.onSuccess(mypageResponseConverter.toUserPageDto(user, profile, reviewPage));
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

    /**
     * user profile 수정
     *
     * @param request 수정할 user 정보
     * @return void
     */
    @PutMapping("/profile")
    public ResponseDto<Void> updateUserProfile(
            @RequestPart(value = "files", required = false) MultipartFile image,
            @Valid @RequestPart(value = "key", required = false) MypageRequest.UpdateProfileRequest request
    ) {
        Users user = mypageService.getCurrentUser();
        mypageService.updateUserProfile(user, request, image);
        return ResponseDto.onSuccess(null);
    }

    /**
     * user 관심 카테고리 수정
     *
     * @param request 수정할 user 정보
     * @return void
     */
    @PutMapping("/category")
    public ResponseDto<Void> updateUserCategory(
            @Valid @RequestBody MypageRequest.UpdateCategoryRequest request
    ) {
        Users user = mypageService.getCurrentUser();
        mypageService.updateUserCategory(user, request);
        return ResponseDto.onSuccess(null);
    }
}
