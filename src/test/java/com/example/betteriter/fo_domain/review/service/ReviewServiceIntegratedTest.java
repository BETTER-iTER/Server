package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.global.constant.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static com.example.betteriter.global.constant.Category.PC;


@SpringBootTest
@ActiveProfiles("test")
public class ReviewServiceIntegratedTest {

    @Autowired
    private ReviewService reviewService;

    @MockBean
    private ReviewRepository reviewRepository;


    private static Review createReview(long count) {

        Users users = Users.builder()
                .usersDetail(UsersDetail.builder()
                        .nickName("nickName")
                        .job(Job.DEVELOPER)
                        .profileImage("profileImage")
                        .build())
                .build();

        return Review.builder()
                .writer(users)
                .category(PC)
                .productName("productName")
                .price(10)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .clickCount(count)
                .shortReview("short")
                .build();
    }
}