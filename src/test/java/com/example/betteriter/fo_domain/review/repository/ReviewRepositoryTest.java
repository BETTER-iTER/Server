package com.example.betteriter.fo_domain.review.repository;

import com.example.betteriter.fo_domain.follow.domain.Follow;
import com.example.betteriter.fo_domain.follow.repository.FollowRepository;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewLike;
import com.example.betteriter.fo_domain.review.domain.ReviewScrap;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.betteriter.global.constant.Category.PC;
import static com.example.betteriter.global.constant.RoleType.ROLE_USER;
import static com.example.betteriter.global.constant.Status.ACTIVE;
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

    @Autowired
    private FollowRepository followRepository;


    private static Review createReview(long count) {

        return Review.builder()
                .category(PC)
                .productName("productName")
                .category(PC)
                .price(10)
                .storeName(1)
                .status(ACTIVE)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .likedCount(count)
                .scrapedCount(count)
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
                .price(100000)
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
                .price(100000)
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
                .price(100000)
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
                .roleType(ROLE_USER)
                .isExpert(true)
                .build();

        Users user02 = Users.builder()
                .oauthId("oauthId02")
                .email("whole34@naver.com")
                .password("5678")
                .roleType(ROLE_USER)
                .isExpert(true)
                .build();

        Users user03 = Users.builder()
                .oauthId("oauthId03")
                .email("whole12@naver.com")
                .password("9999")
                .roleType(ROLE_USER)
                .isExpert(true)
                .build();

        this.usersRepository.saveAll(List.of(user01, user02, user03));


        Review review01 = Review.builder()
                .category(PC)
                .productName("productName01")
                .price(100000)
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
                .price(100000)
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
                .price(100000)
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
                .price(100000)
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
                .roleType(ROLE_USER)
                .isExpert(true)
                .build();

        Users user02 = Users.builder()
                .oauthId("oauthId02")
                .email("danaver12@naver.net")
                .password("5678")
                .roleType(ROLE_USER)
                .isExpert(true)
                .build();

        Users user03 = Users.builder()
                .oauthId("oauthId03")
                .email("danaver12@kakao.net")
                .password("11111")
                .roleType(ROLE_USER)
                .isExpert(true)
                .build();

        this.usersRepository.saveAll(List.of(user01, user02, user03));

        // 2. 리뷰 저장
        Review review01 = Review.builder()
                .category(PC)
                .productName("productName01")
                .price(100000)
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
                .price(100000)
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
                .price(100000)
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
                .price(100000)
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
                = this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(PC, PageRequest.of(0, 5)).getContent();
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
                .roleType(ROLE_USER)
                .isExpert(true)
                .build();

        Users user = this.usersRepository.save(user01);

        // 1. 리뷰 저장
        Review review01 = Review.builder()
                .category(PC)
                .productName("productName01")
                .price(100000)
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
        List<Review> result = this.reviewRepository.findFirst20ByOrderByClickCountDescCreatedAtDesc();

        // then
        for (Review review : result) {
            System.out.println(result);
        }
    }

    @Test
    @DisplayName("상품명에 해당하는 리뷰를 좋아요 수가 많은 순으로 조회한다.")
    void findByProductNameOrderByLikedCountDescAndCreatedAtDesc() {
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

        Slice<Review> result = this.reviewRepository.findByProductNameOrderByLikedCountDescCreatedAtDesc("productName", PageRequest.of(0, 20));
        // then
        System.out.println(result.getContent().size());
    }

    @Test
    @DisplayName("상품명에 해당하는 리뷰의 스크랩 수가 많은 순으로 리뷰를 조회한다.")
    void findByProductNameAndStatusOrderByScrapedCountDescCreatedAtDesc() {
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
        Slice<Review> result = this.reviewRepository.findByProductNameOrderByScrapedCountDescCreatedAtDesc("productName", PageRequest.of(0, 20));

        // then
        System.out.println(result.getContent().size());
    }

    @Test
    @DisplayName("동일한 상품명 리뷰 조회하되 좋아요 + 스크랩 많은 top 4 을 조회한다.")
    void findTop4ByProductNameOrderByLikedCountAndScrapedCountTest() {
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

        List<Review> result = this.reviewRepository.findTop4ByProductNameOrderByScrapedCntPlusLikedCntDesc("productName");

        // then
        System.out.println(result.size());
    }

    @Test
    @DisplayName("동일한 상품명 리뷰 조회하되 작성자의 팔로워가 가장 많은 순으로 리뷰를 조회한다.")
    void findByProductNameOrderByMostWriterFollower() {
        // given
        Review review01 = createReview(1L);
        Review review02 = createReview(2L);
        Review review03 = createReview(3L);
        Review review04 = createReview(4L);
        Review review05 = createReview(5L);

        Users writer01 = Users.builder()
                .email("email01")
                .build();
        Users writer02 = Users.builder()
                .email("email02")
                .build();

        this.usersRepository.saveAll(List.of(writer01, writer02));

        review01.setWriter(writer01);
        review02.setWriter(writer02);
        review03.setWriter(writer01);
        review04.setWriter(writer01);
        review05.setWriter(writer01);

        this.reviewRepository.saveAll(List.of(review01, review02, review03, review04, review05));

        // writer01 -> writer02 팔로우
        Follow follow = Follow.builder()
                .follower(writer01)
                .followee(writer02)
                .build();

        // when

        // then
    }

    @Test
    @DisplayName("로그인한 유저가 팔로우하는 팔로잉의 리뷰를 최신순 7개 조회한다.")
    void findFirst7WrittenByFollowingCreatedAtDescTest() {
        // given
        Users follower = Users.builder()
                .email("danaver12@daum.net")
                .roleType(ROLE_USER)
                .build();

        Users followee = Users.builder()
                .email("whole34@naver.com")
                .roleType(ROLE_USER)
                .build();

        this.usersRepository.saveAll(List.of(followee, follower));

        this.followRepository.save(Follow.builder().follower(follower).followee(followee).build());

        Review review01 = createReview(1L);
        Review review02 = createReview(2L);
        Review review03 = createReview(3L);
        Review review04 = createReview(4L);

        review01.setWriter(followee);
        review02.setWriter(followee);
        review03.setWriter(followee);
        review04.setWriter(followee);

        this.reviewRepository.saveAll(List.of(review01, review02, review03, review04));
        // when
        List<Review> reviews
                = this.reviewRepository.findFirst7WrittenByFollowingCreatedAtDesc(follower, PageRequest.of(0, 7));
        // then
        assertThat(reviews).hasSize(4);
    }

    @Test
    @DisplayName("상품 명에 해당하는 리뷰 중 리뷰의 작성자 팔로워가 많은 순으로 리뷰를 조회한다.")
    void findByProductNameOrderByMostWriterFollowerTest() {
        // given
        Users follower = Users.builder()
                .email("danaver12@daum.net")
                .roleType(ROLE_USER)
                .build();

        Users followee01 = Users.builder()
                .email("whole34@naver.com")
                .roleType(ROLE_USER)
                .build();

        Users followee02 = Users.builder()
                .email("email03")
                .roleType(ROLE_USER)
                .build();


        Users followee03 = Users.builder()
                .email("email04")
                .roleType(ROLE_USER)
                .build();

        this.usersRepository.saveAll(List.of(followee01, follower, followee02, followee03));

        // follower -> followee01
        Follow follow00 = Follow.builder()
                .follower(follower)
                .followee(followee01)
                .build();

        // follower -> followee02
        Follow follow01 = Follow.builder()
                .follower(follower)
                .followee(followee02)
                .build();

        // follower -> followee03
        Follow follow02 = Follow.builder()
                .follower(follower)
                .followee(followee03)
                .build();

        // followee01 -> followee02
        Follow follow03 = Follow.builder()
                .follower(followee01)
                .followee(followee02)
                .build();

        // followee01 -> followee03
        Follow follow04 = Follow.builder()
                .follower(followee01)
                .followee(followee03)
                .build();

        // followee02 -> followee03
        Follow follow05 = Follow.builder()
                .follower(followee02)
                .followee(followee03)
                .build();

        this.followRepository.saveAll(List.of(follow00, follow01, follow02, follow03, follow04));

        Review review01 = createReview(1L);
        Review review02 = createReview(2L);
        Review review03 = createReview(3L);
        Review review04 = createReview(4L);

        review01.setWriter(follower);
        review02.setWriter(followee01);
        review03.setWriter(followee02);
        review04.setWriter(followee03);

        this.reviewRepository.saveAll(List.of(review01, review02, review03, review04));
        // when
        Slice<Review> result = this.reviewRepository.findByProductNameOrderByMostWriterFollower("productName", PageRequest.of(0, 10));
        // then
    }

    @Test
    @DisplayName("카테고리에 해당하는 리뷰를 스크랩 수 + 좋아요 수 순으로 정렬하여 조회한다.")
    void findReviewByCategoryOrderByScrapedCountAndLikedCountTest() {
        // given
        Review review01 = createReview(1L);
        Review review02 = createReview(2L);
        Review review03 = createReview(3L);

        this.reviewRepository.saveAll(List.of(review01, review02, review03));
        // when
        Slice<Review> reviews
                = this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(PC, PageRequest.of(0, 10));
        // then
        List<Review> content = reviews.getContent();
        assertThat(content.get(0)).isEqualTo(review03);
        assertThat(content.get(1)).isEqualTo(review02);
        assertThat(content.get(2)).isEqualTo(review01);
    }

    @Test
    @DisplayName("리뷰 조회 테스트 - review 조회 후 size(r.getReviewLiked) 하는 경우 다 가져오는지 테스트")
    void findReviewByIdTest() {
        // given
        Review review01 = createReview(1L);
        Review review02 = createReview(2L);
        Review review03 = createReview(3L);
        // when
        Optional<Review> review = this.reviewRepository.findById(1L);
        review.ifPresent(value -> System.out.println("hello" + value.getReviewLiked().size()));
        // then
    }

    @Test
    @DisplayName("동일한 제품명을 좋아요 + 스크랩 많은 순으로 조회한다.")
    void findTop4ByProductNameOrderByScrapedCntPlusLikedCntDescTest(){
        // given
        Review review01 = createReview(1L);
        Review review02 = createReview(2L);
        Review review03 = createReview(3L);
        Review review04 = createReview(4L);

        List<Review> reviews = this.reviewRepository.saveAll(List.of(review01, review02, review03, review04));

        // when
        List<Review> result = this.reviewRepository.findTop4ByProductNameOrderByScrapedCntPlusLikedCntDesc("productName");
        // then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getProductName()).isEqualTo("productName");
    }
}