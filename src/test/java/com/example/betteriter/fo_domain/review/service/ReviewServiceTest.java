package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.service.ManufacturerService;
import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.bo_domain.spec.service.SpecService;
import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.follow.service.FollowService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.domain.ReviewLike;
import com.example.betteriter.fo_domain.review.domain.ReviewScrap;
import com.example.betteriter.fo_domain.review.dto.*;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto.CreateReviewImageRequestDto;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.repository.*;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Job;
import com.example.betteriter.global.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.betteriter.global.constant.Category.LAPTOP;
import static com.example.betteriter.global.constant.Category.PC;
import static com.example.betteriter.global.constant.RoleType.ROLE_USER;
import static com.example.betteriter.global.constant.Status.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith({MockitoExtension.class})
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private UserService userService;

    @Mock
    private SpecService specService;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private FollowService followService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private ReviewScrapRepository reviewScrapRepository;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @Mock
    private ReviewSpecDataRepository reviewSpecDataRepository;

    private Review createReview(long count) {

        Users writer = Users.builder()
                .id(1L)
                .email("email")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nickname").job(Job.SW_DEVELOPER).build())
                .build();

        Users user01 = Users.builder()
                .id(2L)
                .email("email01")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nick01").job(Job.SW_DEVELOPER).build())
                .build();

        Users user02 = Users.builder()
                .id(3L)
                .email("email02")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nick02").job(Job.CEO).build())
                .build();


        Review review = Review.builder()
                .id(count)
                .writer(writer)
                .category(PC)
                .productName("productName")
                .category(PC)
                .price(10)
                .storeName(1)
                .status(ACTIVE)
                .manufacturer(Manufacturer.builder().coName("삼성").build())
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .likedCount(count)
                .shownCount(count)
                .scrapedCount(count)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .clickCount(count)
                .shortReview("short")
                .build();

        List<ReviewLike> reviewLikes = List.of(ReviewLike.builder().review(review).users(user01).build(),
                ReviewLike.builder().review(review).users(user02).build());

        List<Comment> comments = List.of(Comment.builder().users(user01).comment("comment01").status(ACTIVE).build(),
                Comment.builder().users(user02).comment("comment02").status(ACTIVE).build());

        review.setReviewsComment(comments);
        review.setReviewLikes(reviewLikes);

        review.setReviewImage(createReviewImage(review));
        return review;
    }

    private ReviewImage createReviewImage(Review review) {
        return ReviewImage.builder()
                .id(review.getId())
                .review(review)
                .orderNum(0)
                .imgUrl("imgUrl")
                .build();
    }

    @Test
    @DisplayName("리뷰을 리뷰 이미지와 함께 정상적으로 등록한다.")
    void createReviewTest() {
        // given
        CreateReviewRequestDto requestDto =
                CreateReviewRequestDto.builder()
                        .category(LAPTOP)
                        .productName("맥북1")
                        .boughtAt(LocalDate.now())
                        .manufacturer("기타")
                        .price(100000)
                        .storeName(1)
                        .comparedProductName("에어팟 맥스")
                        .shortReview("한줄 평")
                        .starPoint(1)
                        .goodPoint("goodPoint")
                        .badPoint("badPoint")
                        .specData(List.of(1L, 2L))
                        .images(List.of(CreateReviewImageRequestDto.builder().imgUrl("imgUrl1").build(),
                                CreateReviewImageRequestDto.builder().imgUrl("imgUrl2").build())
                        ).build();

        given(this.manufacturerService.findManufacturerByName(anyString()))
                .willReturn(Manufacturer.createManufacturer("삼성")); // 삼성

        given(this.specService.findAllSpecDataByIds(anyList()))
                .willReturn(List.of(
                        SpecData.builder()
                                .spec(Spec.createSpec(LAPTOP, "title1"))
                                .build(),
                        SpecData.builder()
                                .spec(Spec.createSpec(LAPTOP, "title2"))
                                .build()
                )); // specData


        Review review = Review.builder()
                .writer(Users.builder()
                        .oauthId("oauthId")
                        .email("danaver12@daum.net")
                        .password("password")
                        .roleType(ROLE_USER)
                        .isExpert(true)
                        .build())
                .manufacturer(Manufacturer.createManufacturer("삼성"))
                .category(LAPTOP)
                .productName("상품명1")
                .price(10000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .shortReview("ShortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        given(this.reviewRepository.save(any(Review.class)))
                .willReturn(review);

        // when
        Long result = this.reviewService.createReview(requestDto);

        // then
        assertThat(result).isNotNull();
        verify(manufacturerService, times(1)).findManufacturerByName(anyString());
        verify(reviewRepository, times(1)).save(any());
        verify(reviewSpecDataRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("리뷰 등록시 카테고리에 해당하는 리뷰 스펙 데이터를 조회한다.")
    void getReviewSpecDataWhenCreateReview() {
        // given
        Category category = Category.PC;
        Spec spec01 = Spec.builder().category(category).title("title1").build();
        Spec spec02 = Spec.builder().category(category).title("title2").build();
        List<Spec> specs = List.of(spec01, spec02);

        given(this.specService.findAllSpecDataByCategory(any(Category.class)))
                .willReturn(specs);
        // when
        GetReviewSpecResponseDto reviewSpecResponseDto = this.reviewService.getReviewSpecDataResponse(category);
        // then
        assertThat(reviewSpecResponseDto.getSpecs()).isNotNull();
        assertThat(reviewSpecResponseDto.getSpecs()).hasSize(2);
    }

    @Test
    @DisplayName("카테고리에 해당하는 모든 리뷰를 조회한다.")
    void getReviewByCategory() {
        // given

        Users user01 = Users.builder()
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .usersDetail(UsersDetail.builder()
                        .nickName("nickname")
                        .job(Job.SW_DEVELOPER).build())
                .build();


        Review review01 = Review.builder()
                .id(1L)
                .category(PC)
                .productName("productName01")
                .price(100000)
                .writer(user01)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review02 = Review.builder()
                .id(2L)
                .category(PC)
                .productName("productName02")
                .writer(user01)
                .price(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review03 = Review.builder()
                .id(3L)
                .category(PC)
                .productName("productName01")
                .writer(user01)
                .price(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review04 = Review.builder()
                .id(4L)
                .category(PC)
                .productName("productName01")
                .writer(user01)
                .price(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();


        given(reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(any(Category.class), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(review01, review02, review03, review04)));

        // when
        ReviewResponse result = this.reviewService.getReviewByCategory(PC, 1);
        // then
        for (GetReviewResponseDto getReviewResponseDto : result.getGetReviewResponseDtoList()) {
            System.out.println("getReviewResponseDto = " + getReviewResponseDto);
        }
    }

    @Test
    @DisplayName("카테고리에 해당하는 모든 리뷰를 리뷰의 스크랩 수 + 좋아요 순으로 정렬해서 조회한다.")
    void getReviewByCategoryServiceTest() {
        // given
        Review review00 = createReview(1L);
        Review review01 = createReview(2L);
        Review review02 = createReview(3L);


        Slice<Review> result = new SliceImpl<>(List.of(review00, review01, review02));
        given(this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(any(Category.class), any(Pageable.class)))
                .willReturn(result);
        // when
        ReviewResponse response = this.reviewService.getReviewByCategory(PC, 2);
        // then
        List<GetReviewResponseDto> getReviewResponseDtoList = response.getGetReviewResponseDtoList();
        assertThat(response.isHasNext()).isFalse();
        assertThat(response.isExisted()).isTrue();
        assertThat(getReviewResponseDtoList).hasSize(3);
    }

    @Test
    @DisplayName("카테고리에 해당하는 모든 리뷰를 리뷰의 스크랩 수 + 좋아요 순으로 정렬해서 조회한다.(조회 결과가 없는 경우 isExisted = false")
    void getReviewByCategoryServiceTest02() {
        // given
        Slice<Review> result = new SliceImpl<>(List.of());
        given(this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(any(Category.class), any(Pageable.class)))
                .willReturn(result);
        // when
        ReviewResponse response = this.reviewService.getReviewByCategory(PC, 2);
        // then
        List<GetReviewResponseDto> getReviewResponseDtoList = response.getGetReviewResponseDtoList();
        assertThat(response.isHasNext()).isFalse();
        assertThat(response.isExisted()).isFalse();
        assertThat(getReviewResponseDtoList).hasSize(0);
    }

    @Test
    @DisplayName("리뷰 상세조회를 한다.(동일한 제품명 리뷰 조회 4개 조회되는 경우)")
    void getReviewDetailTest01() {
        // given
        Review review = createReview(1L);

        Users currentUser = Users.builder()
                .email("danaver12@daum.net")
                .roleType(ROLE_USER)
                .build();

        given(this.reviewRepository.findById(review.getId()))
                .willReturn(Optional.of(review));

        given(this.userService.getCurrentUser())
                .willReturn(currentUser);

        /* 동일한 제품명 리뷰 조회 */
        given(this.reviewRepository.findTop4ByProductNameOrderByScrapedCntPlusLikedCntDesc(anyString()))
                .willReturn(List.of(createReview(2L), createReview(3L), createReview(4L), createReview(5L)));
        // when
        ReviewDetailResponse reviewDetail = this.reviewService.getReviewDetail(1L);
        // then
        assertThat(reviewDetail.getGetReviewDetailResponseDto().getReviewId()).isEqualTo(1L);
        assertThat(reviewDetail.getGetReviewDetailResponseDto().getProductName()).isEqualTo(review.getProductName());
        assertThat(reviewDetail.getGetReviewDetailResponseDto().getScrapedCount()).isEqualTo(1L);
        assertThat(reviewDetail.getWriterInfo().getId()).isEqualTo(review.getWriter().getId());
        assertThat(reviewDetail.getWriterInfo().getNickName()).isEqualTo(review.getWriter().getUsersDetail().getNickName());
        assertThat(reviewDetail.getGetReviewDetailResponseDto().getShownCount()).isEqualTo(2L);
        assertThat(reviewDetail.getGetReviewDetailResponseDto().getPrice()).isEqualTo(10);
        verify(reviewRepository, times(1)).findById(anyLong());
        verify(reviewRepository, times(0)).findReviewByCategoryOrderByScrapedCountAndLikedCount(any(Category.class), any(Pageable.class));
    }

    @Test
    @DisplayName("리뷰 상세조회를 한다.(동일한 제품명 리뷰 조회 2개 + 같은 카테고리 리뷰 2개 조회되는 경우")
    void getReviewDetailTest02() {
        // given
        Review review = createReview(1L);

        Users currentUser = Users.builder()
                .email("danaver12@daum.net")
                .roleType(ROLE_USER)
                .build();

        given(this.reviewRepository.findById(review.getId()))
                .willReturn(Optional.of(review));

        given(this.userService.getCurrentUser())
                .willReturn(currentUser);

        /* 동일한 제품명 리뷰 조회 (2개) */
        given(this.reviewRepository.findTop4ByProductNameOrderByScrapedCntPlusLikedCntDesc(anyString()))
                .willReturn(List.of(createReview(2L), createReview(3L)));
        /* 동일한 카테고리 리뷰 조회 (2개) */
        given(this.reviewRepository.findReviewByCategoryOrderByScrapedCountAndLikedCount(any(), any()))
                .willReturn(new SliceImpl<>(List.of(createReview(3L), createReview(4L))));

        // when
        ReviewDetailResponse reviewDetail = this.reviewService.getReviewDetail(1L);
        // then
        assertThat(reviewDetail.getGetReviewDetailResponseDto().getReviewId()).isEqualTo(1L);
        assertThat(reviewDetail.getGetReviewDetailResponseDto().getProductName()).isEqualTo(review.getProductName());
        assertThat(reviewDetail.getGetReviewDetailResponseDto().getScrapedCount()).isEqualTo(1L);
        assertThat(reviewDetail.getWriterInfo().getId()).isEqualTo(review.getWriter().getId());
        assertThat(reviewDetail.getWriterInfo().getNickName()).isEqualTo(review.getWriter().getUsersDetail().getNickName());

        verify(reviewRepository, times(1)).findById(anyLong());
        verify(reviewRepository, times(1)).findReviewByCategoryOrderByScrapedCountAndLikedCount(any(Category.class), any(Pageable.class));
    }

    @Test
    @DisplayName("리뷰 좋아요를 한다.")
    void reviewLikeServiceTest() {
        // given

        // 좋아요 하는 리뷰
        Review review = createReview(1L);

        // 좋아요 하는 유저
        Users users = Users.builder()
                .id(1L)
                .email("email")
                .roleType(ROLE_USER)
                .build();

        given(this.reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        given(this.userService.getCurrentUser())
                .willReturn(users);

        given(this.reviewLikeRepository.save(any(ReviewLike.class)))
                .willReturn(ReviewLike.builder().review(review).users(users).build());
        // when
        ReviewLike reviewLike = null;
        // then
        assertThat(reviewLike.getUsers()).isEqualTo(users);
        assertThat(reviewLike.getReview()).isEqualTo(review);
        verify(reviewRepository, times(1)).findById(anyLong());
        verify(userService, times(1)).getCurrentUser();
        verify(reviewLikeRepository, times(1)).save(any(ReviewLike.class));
    }

    @Test
    @DisplayName("리뷰 스크랩을 한다.")
    void reviewScrapServiceTest() {
        // given

        // 스크랩 하는 리뷰
        Review review = createReview(1L);

        // 좋아요 하는 유저
        Users users = Users.builder()
                .id(1L)
                .email("email")
                .roleType(ROLE_USER)
                .build();

        given(this.reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        given(this.userService.getCurrentUser())
                .willReturn(users);

        given(this.reviewScrapRepository.save(any(ReviewScrap.class)))
                .willReturn(ReviewScrap.builder().review(review).users(users).build());

        // when
        // then
        verify(this.reviewRepository, times(1)).findById(anyLong());
        verify(this.userService, times(1)).getCurrentUser();
        verify(this.reviewScrapRepository, times(1)).save(any(ReviewScrap.class));
    }

    @Test
    @DisplayName("리뷰 상세 조회 좋아요 조회을 한다.")
    void getReviewDetailLike() {
        // given
        Review review = createReview(1L);

        given(this.reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        // when
        List<ReviewLikeResponse> result = this.reviewService.getReviewDetailLikes(1L);

        // then
        assertThat(result).hasSize(2);

        for (ReviewLikeResponse reviewLikeResponse : result) {
            System.out.println(reviewLikeResponse.getUserId());
            System.out.println(reviewLikeResponse.getNickname());
        }
    }

    @Test
    @DisplayName("리뷰 상세 조회 댓글 조회를 한다.")
    void getReviewDetailCommentsServiceTest() {
        // given
        Review review = createReview(1L);

        given(this.reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        given(this.userService.getCurrentUser())
                .willReturn(Users.builder().id(1L).email("email").roleType(ROLE_USER).build());
        // when
        List<ReviewCommentResponse> result = this.reviewService.getReviewDetailComments(1L);
        // then
        assertThat(result).hasSize(2);
        verify(userService, times(1)).getCurrentUser();
        for (ReviewCommentResponse reviewCommentResponse : result) {
            System.out.println(reviewCommentResponse.getComment());
            System.out.println(reviewCommentResponse.isMine());
        }
    }

    @Test
    @DisplayName("리뷰 삭제를 한다.")
    void deleteReviewServiceTest() {
        // given
        Review review = createReview(1L);

        given(this.reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        // when
        this.reviewService.deleteReview(1L);
        // then
        verify(this.reviewRepository, times(1)).findById(anyLong());
        verify(this.reviewRepository, times(1)).delete(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 좋아요 취소를 성공적으로 한다.")
    void deleteReviewLikeServiceTest() {
        // given
        Review review = createReview(1L);

        Users user = Users.builder()
                .roleType(ROLE_USER)
                .email("danaver12@daum.net")
                .build();

        ReviewLike reviewLike = ReviewLike.builder()
                .review(review)
                .users(user)
                .build();

        given(this.reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        given(this.reviewLikeRepository.findByReviewAndUsers(any(Review.class), any(Users.class)))
                .willReturn(Optional.of(reviewLike));

        given(this.userService.getCurrentUser())
                .willReturn(user);
        // when
        this.reviewService.deleteReviewLike(1L);

        // then
        verify(this.reviewRepository, times(1)).findById(anyLong());
        verify(this.reviewLikeRepository, times(1)).findByReviewAndUsers(any(Review.class), any(Users.class));
    }


    @Test
    @DisplayName("리뷰 스크랩 취소를 한다 - 성공")
    void deleteReviewScrapInSuccess() {
        // given
        Review review = createReview(1L);

        Users user = Users.builder()
                .roleType(ROLE_USER)
                .email("danaver12@daum.net")
                .build();

        ReviewScrap reviewScrap = ReviewScrap.builder()
                .review(review)
                .users(user)
                .build();

        given(this.reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        given(this.userService.getCurrentUser())
                .willReturn(user);

        given(this.reviewScrapRepository.findByReviewAndUsers(any(Review.class), any(Users.class)))
                .willReturn(Optional.of(reviewScrap));

        // when
        this.reviewService.deleteReviewScrap(1L);

        // then
        verify(this.reviewRepository, times(1)).findById(anyLong());
        verify(this.userService, times(1)).getCurrentUser();
        verify(this.reviewScrapRepository, times(1)).findByReviewAndUsers(any(), any());
    }

    @Test
    @DisplayName("리뷰 스크랩 취소를 한다 - 실패(리뷰스크랩 데이터 존재 x)")
    void deleteReviewScrapInFailure() {
        // given
        Review review = createReview(1L);

        Users user = Users.builder()
                .roleType(ROLE_USER)
                .email("danaver12@daum.net")
                .build();


        given(this.reviewRepository.findById(anyLong()))
                .willReturn(Optional.of(review));

        given(this.userService.getCurrentUser())
                .willReturn(user);

        given(this.reviewScrapRepository.findByReviewAndUsers(any(Review.class), any(Users.class)))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> this.reviewService.deleteReviewScrap(1L))
                .isInstanceOf(ReviewHandler.class);
        verify(this.reviewRepository, times(1)).findById(anyLong());
        verify(this.userService, times(1)).getCurrentUser();
    }
}
