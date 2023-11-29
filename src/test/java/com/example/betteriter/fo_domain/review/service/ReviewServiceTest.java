package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.global.constant.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰가 리뷰 이미지와 함께 정상적으로 등록된다.")
    void createReviewTest() {
        // given
        Manufacturer manufacturer = Manufacturer.builder()
                .coName("삼성")
                .build();

        Review review = Review.builder()
                .category(Category.PC)
                .productName("product1")
                .boughtAt(LocalDate.now())
                .manufacturer(manufacturer)
                .amount(1000)
                .storeName(1)
                .shortReview("예뻐요")
                .starPoint(1)
                .goodPoint("good")
                .badPoint("expensive")
                .reviewImages(List.of(ReviewImage.createReviewImage("imgUrl", 0), ReviewImage.createReviewImage("imgUrl2", 1)))
                .build();

        this.reviewRepository.save(review);
        // when

        // then
    }
}
