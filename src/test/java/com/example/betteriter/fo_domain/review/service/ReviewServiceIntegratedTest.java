package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.dto.ReviewResponse;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.global.constant.Job;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.example.betteriter.global.constant.Category.PC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


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
                .amount(10)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .clickCount(count)
                .shortReview("short")
                .build();
    }

    @Test
    @DisplayName("test")
    void test() {
        // given
        List<Review> reviews = List.of(createReview(1L), createReview(2L));

        given(this.reviewRepository.findByProductNameOrderByCreatedAtDesc(anyString(), any()))
                .willReturn(new SliceImpl<>(reviews));

        // when
        ReviewResponse result = this.reviewService.getReviewBySearch("productName");

        // then
        assertThat(result).isNotNull();
    }
}