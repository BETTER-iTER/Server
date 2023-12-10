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
}
