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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageService {

    private static final int SIZE = 10;

    private final UserService userService;
    private final ReviewService reviewService;
    private final FollowService followService;

    @Transactional(readOnly = true)
    public List<Review> getMyReviewList(int page) {
        Users user = userService.getCurrentUser();
        return reviewService.getReviewList(user, page, SIZE);
    }

    @Transactional(readOnly = true)
    public List<Review> getScrapReviewList(int page) {
        Users user = userService.getCurrentUser();
        return reviewService.getScrapReviewList(user, page, SIZE);
    }

    @Transactional(readOnly = true)
    public List<Review> getLikeReviewList(int page) {
        Users user = userService.getCurrentUser();
        return reviewService.getLikeReviewList(user, page, SIZE);
    }

    @Transactional(readOnly = true)
    public List<Review> getTargetReviewList(Long id){
        Users user = userService.getUserById(id);
        return reviewService.getTargetReviewList(user);
    }

    @Transactional(readOnly = true)
    public List<Users> getFollowerList(Long id) {
        Users user = userService.getUserById(id);
        return followService.getFollowerList(user);
    }

    @Transactional(readOnly = true)
    public List<Users> getFolloweeList(Long id) {
        Users user = userService.getUserById(id);
        return followService.getFolloweeList(user);
    }

    public boolean checkUserSelf(String email) {
        Users user = userService.getCurrentUser();
        return user.getEmail().equals(email);
    }

    public boolean checkUserSelf(Long id) {
        Users user = userService.getCurrentUser();
        return user.getId().equals(id);
    }

    @Transactional(readOnly = true)
    public MypageResponse.UserProfileDto getUserProfile(Long id) {
        Users user = userService.getUserById(id);
        Users currentUser = userService.getCurrentUser();

        boolean isFollow = false;
        boolean isSelf = true;

        if (!Objects.equals(user.getId(), currentUser.getId())) {
            isFollow = followService.isFollow(currentUser, user);
            isSelf = false;
        }

        List<Users> followerList = followService.getFollowerList(user);
        List<Users> followeeList = followService.getFolloweeList(user);

        return MypageResponseConverter.toUserProfileDto(
                user, isFollow, isSelf,
                (long) followerList.size(),
                (long) followeeList.size()
        );
    }
}
