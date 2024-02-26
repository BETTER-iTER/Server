package com.example.betteriter.fo_domain.mypage.service;

import static com.example.betteriter.global.common.code.status.ErrorStatus.*;

import com.example.betteriter.fo_domain.follow.service.FollowService;
import com.example.betteriter.fo_domain.mypage.converter.MypageResponseConverter;
import com.example.betteriter.fo_domain.mypage.dto.MypageRequest;
import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.mypage.exception.MypageHandler;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.infra.s3.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageService {

    private static final int SIZE = 10;
    private final UserService userService;

    private final ReviewService reviewService;
    private final FollowService followService;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public Page<Review> getMyReviewList(int page) {
        Users user = userService.getCurrentUser();
        return reviewService.getReviewList(user, page, SIZE);
    }

    @Transactional(readOnly = true)
    public Page<Review> getScrapReviewList(int page) {
        Users user = userService.getCurrentUser();
        return reviewService.getScrapReviewList(user, page, SIZE);
    }

    @Transactional(readOnly = true)
    public Page<Review> getLikeReviewList(int page) {
        Users user = userService.getCurrentUser();
        return reviewService.getLikeReviewList(user, page, SIZE);
    }

    @Transactional(readOnly = true)
    public List<Users> getFollowerList(int page) {
        Users user = userService.getCurrentUser();
        return followService.getFollowerList(user, page, SIZE);
    }

    @Transactional(readOnly = true)
    public List<Users> getFollowingList(int page) {
        Users user = userService.getCurrentUser();
        return followService.getFollowingList(user, page, SIZE);
    }

    public MypageResponse.MyProfileDto getMyProfile() {
        Users user = userService.getCurrentUser();
        Integer followerCount = followService.getFollowerCount(user);
        Integer followeeCount = followService.getFolloweeCount(user);

        return MypageResponseConverter.toMyProfileDto(
                user, followerCount, followeeCount
        );
    }

    public MypageResponse.UserProfileDto getUserProfile(Long userId) {
        Users user = userService.getCurrentUser();
        Users targetUser = userService.getUserById(userId);
        Integer followerCount = followService.getFollowerCount(user);
        Integer followeeCount = followService.getFolloweeCount(user);

        boolean isFollow = followService.isFollow(user, targetUser);

        return MypageResponseConverter.toUserProfileDto(
                targetUser, followerCount, followeeCount, isFollow
        );
    }

    public Integer getFollowerCount() {
        Users user = userService.getCurrentUser();
        return followService.getFollowerCount(user);
    }

    public Integer getFollowingCount() {
        Users user = userService.getCurrentUser();
        return followService.getFolloweeCount(user);
    }

    public Users getCurrentUser() {
        return userService.getCurrentUser();
    }

    public Integer getTotalLikeCount(Users user) {
        return reviewService.getTotalLikeCount(user);
    }

    public Integer getTotalScrapCount(Users user) {
        return reviewService.getTotalScrapCount(user);
    }

    public Page<Review> getUserReviewList(Long userId, int page) {
        Users targetUser = userService.getUserById(userId);
        return reviewService.getReviewList(targetUser, page, SIZE);
    }

	public void updateUserProfile(Users user, MypageRequest.UpdateProfileRequest request, MultipartFile image) {
	    // 1. 프로필 이미지 업로드
        String profileImageUrl = image != null? this.uploadProfileImage(user, image) : null;

        // 2. 프로필 정보 수정
        UsersDetail detail = user.getUsersDetail();
        detail.updateProfile(request.getNickname(), request.getJob(), profileImageUrl);
        userService.updateUserDetail(detail);
    }

    private String uploadProfileImage(Users user, MultipartFile image) {
        this.checkUploadProfileImageRequestValidation(image);
        return s3Service.uploadImage(image, user);
    }

    private void checkUploadProfileImageRequestValidation(MultipartFile image) {
        if(image == null || image.isEmpty()) {
            throw new MypageHandler(_IMAGE_FILE_UPLOAD_REQUEST_IS_NOT_VALID);
        }
    }

	public void updateUserCategory(Users user, MypageRequest.UpdateCategoryRequest request) {
        userService.updateUserCategory(user, request.getCategories());
    }
}
