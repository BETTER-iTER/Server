package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewScrap;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.repository.UsersRepository;
import com.example.betteriter.global.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.example.betteriter.global.constant.Category.PC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewScrapRepository reviewScrapRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("사용자 카테고리에 해당하는 최신순 7개의 리뷰를 조회한다.")
    void findFirst7ByCategoryOrderByCreatedAtDesc() {
        // given
        Review review01 = Review.builder()
                .category(PC)
                .productName("productName01")
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review02 = Review.builder()
                .category(PC)
                .productName("productName02")
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review03 = Review.builder()
                .category(PC)
                .productName("productName03")
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        reviewRepository.saveAll(List.of(review01, review02, review03));
        // when
        List<Review> result = reviewRepository.findFirst7ByCategoryOrderByCreatedAtDesc(PC);
        // then
        assertThat(result).hasSize(3);

    }

    @Test
    @DisplayName("좋아요 + 스크랩수가 가장 많은 리뷰를 조회한다.")
    void findTop7ReviewHavingMostScrapedAndLikedTest() {
        // given

        Users user01 = Users.builder()
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .build();

        Users user02 = Users.builder()
                .oauthId("oauthId02")
                .email("whole34@naver.com")
                .password("5678")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .build();

        Users user03 = Users.builder()
                .oauthId("oauthId03")
                .email("whole12@naver.com")
                .password("9999")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .build();

        this.usersRepository.saveAll(List.of(user01, user02, user03));


        Review review01 = Review.builder()
                .category(PC)
                .productName("productName01")
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review02 = Review.builder()
                .category(PC)
                .productName("productName02")
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review03 = Review.builder()
                .category(PC)
                .productName("productName01")
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review04 = Review.builder()
                .category(PC)
                .productName("productName01")
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();


        this.reviewRepository.saveAll(List.of(review01, review02, review03, review04));


        ReviewScrap reviewScrap01 = ReviewScrap.builder()
                .review(review01)
                .users(user01)
                .build();

        ReviewScrap reviewScrap02 = ReviewScrap.builder()
                .review(review02)
                .users(user01)
                .build();

        ReviewScrap reviewScrap0x = ReviewScrap.builder()
                .review(review02)
                .users(user02)
                .build();

        ReviewScrap reviewScrap03 = ReviewScrap.builder()
                .review(review03)
                .users(user01)
                .build();

        ReviewScrap reviewScrap04 = ReviewScrap.builder()
                .review(review03)
                .users(user02).build();

        ReviewScrap reviewScrap05 = ReviewScrap.builder()
                .review(review03)
                .users(user03)
                .build();

        this.reviewScrapRepository.saveAll(List.of(reviewScrap01, reviewScrap02, reviewScrap0x, reviewScrap03, reviewScrap04, reviewScrap05));
        // when
        List<Review> result = this.reviewRepository.findTop7ReviewHavingMostScrapedAndLiked(PageRequest.of(0, 7));
        // then
        for (Review review : result) {
            System.out.println("review = " + review);
        }
    }
}
