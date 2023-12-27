package com.example.betteriter.fo_domain.mypage.service;

import com.example.betteriter.fo_domain.follow.service.FollowService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageService {

    private final UserService userService;
    private final ReviewService reviewService;
    private final FollowService followService;

    @Transactional(readOnly = true)
    public List<Review> getMyReviewList() {
        Users user = userService.getCurrentUser();
        return reviewService.getReviewList(user);
    }

    @Transactional(readOnly = true)
    public List<Review> getScrapReviewList(Long id) {
        Users user = userService.getUserById(id);
        return reviewService.getScrapReviewList(user);
    }

    @Transactional(readOnly = true)
    public List<Review> getLikeReviewList(Long id) {
        Users user = userService.getUserById(id);
        return reviewService.getLikeReviewList(user);
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

}
