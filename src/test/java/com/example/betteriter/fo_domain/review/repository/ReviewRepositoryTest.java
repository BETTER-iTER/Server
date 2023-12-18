package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewLike;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.betteriter.global.constant.Category.PC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
public class ReviewRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewScrapRepository reviewScrapRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private UsersRepository usersRepository;

    private static Review createReview(long count) {
        return Review.builder()
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

    @Test
    @DisplayName("카테고리 별 리뷰 조회을 한다.")
    void findReviewsByCategoryByDesc() {
        // given

        // 1. 유저 저장
        Users user01 = Users.builder()
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .build();

        Users user02 = Users.builder()
                .oauthId("oauthId02")
                .email("danaver12@naver.net")
                .password("5678")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .build();

        Users user03 = Users.builder()
                .oauthId("oauthId03")
                .email("danaver12@kakao.net")
                .password("11111")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .build();

        this.usersRepository.saveAll(List.of(user01, user02, user03));

        // 2. 리뷰 저장
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

        Review review04 = Review.builder()
                .category(PC)
                .productName("productName04")
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        this.reviewRepository.saveAll(List.of(review01, review02, review03, review04));

        // 3. 리뷰 스크랩 + 좋아요 저장
        // review01 스크랩
        ReviewScrap reviewScrap01 = ReviewScrap.builder()
                .review(review01)
                .users(user01)
                .build();

        // review02 스크랩
        ReviewScrap reviewScrap02 = ReviewScrap.builder()
                .review(review02)
                .users(user01)
                .build();

        // review03 스크랩
        ReviewScrap reviewScrap03 = ReviewScrap.builder()
                .review(review03)
                .users(user01)
                .build();

        // review01 스크랩
        ReviewScrap reviewScrap04 = ReviewScrap.builder()
                .review(review01)
                .users(user02)
                .build();

        // review02 스크랩
        ReviewScrap reviewScrap05 = ReviewScrap.builder()
                .review(review02)
                .users(user02)
                .build();

        // review01 스크랩
        ReviewScrap reviewScrap06 = ReviewScrap.builder()
                .review(review01)
                .users(user03)
                .build();

        // review01 좋아요
        ReviewLike reviewLike01 = ReviewLike.builder()
                .review(review01)
                .users(user01)
                .build();

        // review02 좋아요
        ReviewLike reviewLike02 = ReviewLike.builder()
                .review(review02)
                .users(user02)
                .build();

        // review03 좋아요
        ReviewLike reviewLike03 = ReviewLike.builder()
                .review(review01)
                .users(user03)
                .build();

        // review02 좋아요
        ReviewLike reviewLike04 = ReviewLike.builder()
                .review(review02)
                .users(user03)
                .build();

        // review04 좋아요
        ReviewLike reviewLike05 = ReviewLike.builder()
                .review(review04)
                .users(user02)
                .build();

        System.out.println("reviewLiked 저장");
        this.reviewLikeRepository.saveAll(List.of(reviewLike01, reviewLike02, reviewLike03, reviewLike04, reviewLike05));
        System.out.println("reviewScrap 저장");
        this.reviewScrapRepository.saveAll(List.of(reviewScrap01, reviewScrap02, reviewScrap03, reviewScrap04, reviewScrap05, reviewScrap05, reviewScrap06));


        // when
        Optional<Review> result01 = this.reviewRepository.findById(1L); // 영속성 컨텍스트에서 가져옴


        em.flush();
        em.clear();

        System.out.println("jpql 실행");
        List<Review> result
                = this.reviewRepository.findReviewByCategory(PC, PageRequest.of(0, 5)).getContent();
        System.out.println("jpql 실행 끝");

        // then
        for (Review review : result) {
            System.out.println("review.getId() = " + review.getId());
            System.out.println(review.getReviewScraped().get(0));
        }
    }

    @Test
    @DisplayName("리뷰 관련 JPA 테스트을 진행한다.")
    void testjpa() {
        // given
        // 1. 유저 저장
        Users user01 = Users.builder()
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .build();

        Users user = this.usersRepository.save(user01);

        // 1. 리뷰 저장
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


        ReviewLike reviewLike01 = ReviewLike.builder()
                .users(user)
                .review(review01)
                .build();

        ReviewLike reviewLike02 = ReviewLike.builder()
                .users(user)
                .review(review01)
                .build();

        review01.setReviewLiked(List.of(reviewLike01, reviewLike02));
        Review saveReview = this.reviewRepository.save(review01);

        em.flush();
        em.clear();

        Review foundReview = this.reviewRepository.findById(1L).get();
        System.out.println(foundReview.getReviewLiked().size());
        List<ReviewLike> reviewLiked = foundReview.getReviewLiked();

        for (ReviewLike reviewLike : reviewLiked) {
            System.out.println(reviewLike.getReview());
            System.out.println(reviewLike.getUsers().getId());
        }
    }

    @Test
    @DisplayName("최근 7일 유저들이 많이 클릭한 리뷰 top 20 개를 보여준다.")
    void test() {
        // given
        Review review01 = createReview(1L);
        Review review02 = createReview(2L);
        Review review03 = createReview(3L);
        Review review04 = createReview(4L);
        Review review05 = createReview(5L);
        Review review06 = createReview(6L);
        Review review07 = createReview(7L);
        Review review08 = createReview(8L);
        Review review09 = createReview(9L);
        Review review10 = createReview(10L);
        Review review11 = createReview(11L);
        Review review12 = createReview(12L);
        Review review13 = createReview(13L);
        Review review14 = createReview(14L);
        Review review15 = createReview(15L);
        Review review16 = createReview(16L);

        this.reviewRepository.saveAll(List.of(review01, review02, review03, review04, review05,
                review06, review07, review08, review09, review10, review11, review12, review13, review14, review15, review16));


        // when
        List<Review> result = this.reviewRepository.findFirst20ByOrderByClickCountDescCreatedAtDesc("productName");

        // then
        for (Review review : result) {
            System.out.println(result);
        }
    }
}