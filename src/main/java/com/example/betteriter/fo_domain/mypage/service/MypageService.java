package com.example.betteriter.fo_domain.mypage.service;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MypageService {

    private final UserService userService;
    private final ReviewService reviewService;

    public List<Review> getMyReviewList() {
        Users user = userService.getCurrentUser();
        return reviewService.getReviewList(user.getId());
    }

    public List<Review> getScrapReviewList(String email) {
        Users user = userService.getUserByEmail(email);
        return reviewService.getScrapReviewList(user);
    }

    public List<Review> getLikeReviewList(String email) {
        Users user = userService.getUserByEmail(email);
        return reviewService.getLikeReviewList(user);
    }

    public List<Review> getTargetReviewList(String email) {
        Users user = userService.getUserByEmail(email);
        return reviewService.getTargetReviewList(user.getId());
    }

    public boolean checkUserSelf(String email) {
        Users user = userService.getCurrentUser();
        return user.getEmail().equals(email);
    }
}
