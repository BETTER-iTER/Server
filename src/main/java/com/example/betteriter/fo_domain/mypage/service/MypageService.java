package com.example.betteriter.fo_domain.mypage.service;

import com.example.betteriter.fo_domain.follow.service.FollowService;
import com.example.betteriter.fo_domain.mypage.converter.MypageResponseConverter;
import com.example.betteriter.fo_domain.mypage.dto.MypageResponse;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageService {

    private static final int SIZE = 10;

    private final UserService userService;
    private final ReviewService reviewService;
    private final FollowService followService;

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

    @Transactional(readOnly = true)
    public MypageResponse.UserProfileDto getUserProfile() {
        Users user = userService.getCurrentUser();
        Integer followerCount = followService.getFollowerCount(user);
        Integer followeeCount = followService.getFolloweeCount(user);

        return MypageResponseConverter.toUserProfileDto(
                user, followerCount, followeeCount
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
}
