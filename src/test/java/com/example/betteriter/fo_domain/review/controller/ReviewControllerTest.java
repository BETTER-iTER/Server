package com.example.betteriter.fo_domain.review.controller;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.dto.*;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto.CreateReviewImageRequestDto;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.global.config.security.SecurityConfig;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Job;
import com.example.betteriter.global.filter.JwtAuthenticationFilter;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.example.betteriter.global.constant.Category.PC;
import static com.example.betteriter.global.constant.RoleType.ROLE_USER;
import static com.example.betteriter.global.constant.Status.ACTIVE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ReviewController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RedisUtil.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class)}
)
@WithMockUser
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    private static Review createReview(long count) {

        Users users = Users.builder()
                .email("email")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nickname").job(Job.SW_DEVELOPER).build())
                .build();


        Review review = Review.builder()
                .id(count)
                .writer(users)
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
                .scrapedCount(count)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .clickCount(count)
                .shortReview("short")
                .build();

        review.setReviewImage(createReviewImage(review));
        return review;
    }

    private static ReviewImage createReviewImage(Review review) {
        return ReviewImage.builder()
                .id(review.getId())
                .review(review)
                .orderNum(0)
                .imgUrl("imgUrl")
                .build();
    }

    @Test
    @WithMockUser(username = "choi")
    @DisplayName("상품 이름에 해당하는 리뷰 리스트를 성공적으로 조회한다.")
    void getReviewsBySearchSuccessfulTest() throws Exception {
        // given
        Users user = Users.builder()
                .usersDetail(UsersDetail.builder()
                        .nickName("nickName")
                        .profileImage("profileImage")
                        .job(Job.SW_DEVELOPER)
                        .build())
                .build();

        Review review = Review.builder()
                .writer(user)
                .id(1L)
                .category(PC)
                .productName("productName")
                .price(10000)
                .storeName(1)
                .starPoint(1.0)
                .badPoint("badPoint")
                .build();

        GetReviewResponseDto getReviewResponseDto = GetReviewResponseDto.builder()
                .review(review)
                .reviewSpecData(List.of("string", "string02"))
                .firstImage("firstImage")
                .build();

        given(reviewService.getReviewBySearch(anyString(), anyString(), anyInt()))
                .willReturn(ReviewResponse.builder()
                        .getReviewResponseDtoList(List.of(getReviewResponseDto))
                        .hasNext(false).build());

        ReviewResponse reviewResponse = new ReviewResponse(List.of(getReviewResponseDto), true, true);
        // when && then
        mockMvc.perform(get("/review/search")
                        .param("name", "productName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS_200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.existed").value(true))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "choi")
    @DisplayName("상품 이름에 해당하는 리뷰 리스트를 조회 실패한다.")
    void getReviewsBySearchFailureTest() throws Exception {
        // given
        Users user = Users.builder()
                .usersDetail(UsersDetail.builder()
                        .nickName("nickName")
                        .profileImage("profileImage")
                        .job(Job.SW_DEVELOPER)
                        .build())
                .build();

        Review review = Review.builder()
                .writer(user)
                .id(1L)
                .category(PC)
                .productName("productName")
                .price(10000)
                .storeName(1)
                .starPoint(1.0)
                .badPoint("badPoint")
                .build();

        GetReviewResponseDto getReviewResponseDto = GetReviewResponseDto.builder()
                .review(review)
                .reviewSpecData(List.of("string", "string02"))
                .firstImage("firstImage")
                .build();

        given(reviewService.getReviewBySearch(anyString(), anyString(), anyInt()))
                .willReturn(ReviewResponse.builder()
                        .getReviewResponseDtoList(List.of(getReviewResponseDto))
                        .hasNext(false).build());

        ReviewResponse reviewResponse = new ReviewResponse(List.of(getReviewResponseDto), true, true);
        // when && then
        mockMvc.perform(get("/review/search")
                        .param("name", (String) null))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "choi", roles = "USER")
    @DisplayName("리뷰를 성공적으로 등록한다.")
    void createReviewSuccessfully() throws Exception {
        // given
        CreateReviewRequestDto requestDto
                = CreateReviewRequestDto.builder()
                .category(PC)
                .productName("productName")
                .boughtAt(LocalDate.now())
                .manufacturer("삼성")
                .price(1000)
                .storeName(1)
                .shortReview("shortReview")
                .starPoint(2)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .specData(List.of(1L, 2L))
                .images(List.of(CreateReviewImageRequestDto.builder().imgUrl("imgUrl").build()))
                .build();

        given(this.reviewService.createReview(any(CreateReviewRequestDto.class)))
                .willReturn(1L); // 생성된 리뷰 id


        // when & then
        mockMvc.perform(post("/review")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS_200"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result").value(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("변경된 상품명 리뷰 조회 테스트를 진행한다.")
    void getReviewByProductNameSuccessfully() throws Exception {
        // given
        Users user = Users.builder()
                .usersDetail(UsersDetail.builder()
                        .nickName("nickName")
                        .profileImage("profileImage")
                        .job(Job.SW_DEVELOPER)
                        .build())
                .build();

        Review review = Review.builder()
                .writer(user)
                .id(1L)
                .category(PC)
                .productName("productName")
                .price(10000)
                .storeName(1)
                .starPoint(1.0)
                .badPoint("badPoint")
                .build();

        GetReviewResponseDto getReviewResponseDto = GetReviewResponseDto.builder()
                .review(review)
                .reviewSpecData(List.of("string", "string02"))
                .firstImage("firstImage")
                .build();

        given(reviewService.getReviewBySearch(anyString(), anyString(), anyInt()))
                .willReturn(ReviewResponse.builder()
                        .getReviewResponseDtoList(List.of(getReviewResponseDto))
                        .hasNext(false)
                        .isExisted(true)
                        .build());
        // when
        mockMvc.perform(get("/review/search")
                        .param("name", "productName")
                        .param("sort", "mostLiked")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS_200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.existed").value(true));
        // then
        verify(this.reviewService, times(1)).getReviewBySearch(anyString(), anyString(), anyInt());
    }

    @Test
    @WithMockUser
    @DisplayName("카테고리별 좋아요 + 스크랩 수로 리뷰 조회 컨트롤러 테스트")
    void getReviewByCategoryControllerTest() throws Exception {
        // given
        Users users = Users.builder()
                .email("email")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nickname").job(Job.SW_DEVELOPER).build())
                .build();

        Review review = Review.builder()
                .writer(users)
                .category(PC)
                .productName("productName")
                .category(PC)
                .price(10)
                .storeName(1)
                .status(ACTIVE)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .shortReview("short")
                .build();

        List<GetReviewResponseDto> getReviewResponseList =
                List.of(GetReviewResponseDto.builder()
                        .review(review)
                        .reviewSpecData(List.of("s", "2"))
                        .firstImage("firstImage")
                        .build());

        ReviewResponse reviewResponse = ReviewResponse.builder()
                .getReviewResponseDtoList(getReviewResponseList)
                .hasNext(false)
                .isExisted(true)
                .build();

        given(this.reviewService.getReviewByCategory(any(Category.class), anyInt()))
                .willReturn(reviewResponse);
        // when & then
        mockMvc.perform(get("/review/category")
                        .param("category", "PC")
                        .param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS_200"))
                .andExpect(jsonPath("$.result.hasNext").value(false));

    }

    @Test
    @WithMockUser
    @DisplayName("상품 명 + 필터링 리뷰 조회 컨트롤러 API 변동으로 인한 테스트을 진행한다.")
    void getReviewsBySearchTest() throws Exception {
        // given
        Users users = Users.builder()
                .email("email")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nickname").job(Job.SW_DEVELOPER).build())
                .build();

        Review review = Review.builder()
                .writer(users)
                .category(PC)
                .productName("productName")
                .category(PC)
                .price(10)
                .storeName(1)
                .status(ACTIVE)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .shortReview("short")
                .build();

        List<GetReviewResponseDto> getReviewResponseList =
                List.of(GetReviewResponseDto.builder()
                        .review(review)
                        .reviewSpecData(List.of("s", "2"))
                        .firstImage("firstImage")
                        .build());

        ReviewResponse reviewResponse = ReviewResponse.builder()
                .getReviewResponseDtoList(getReviewResponseList)
                .hasNext(false)
                .isExisted(true)
                .build();

        given(this.reviewService.getReviewBySearch(anyString(), anyString(), anyInt()))
                .willReturn(reviewResponse);
        // when & then
        mockMvc.perform(get("/review/search")
                        .param("name", "name")
                        .param("sort", "mostScraped")
                        .param("page", "2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("리뷰 상세 조회 컨트롤러 테스트를 진행한다.")
    void getReviewDetailControllerTest() throws Exception {
        // given
        Users users = Users.builder()
                .email("email")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nickname").job(Job.SW_DEVELOPER).build())
                .build();

        Review review = Review.builder()
                .writer(users)
                .category(PC)
                .productName("productName")
                .category(PC)
                .price(10)
                .storeName(1)
                .status(ACTIVE)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .shortReview("short")
                .build();

        GetReviewDetailResponseDto getReviewDetailResponseDto = GetReviewDetailResponseDto.builder()
                .id(1L)
                .productName("productName")
                .reviewSpecData(List.of("spec1", "spec2"))
                .starPoint(2.0)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .shortReview("shortReview")
                .manufacturer("삼성")
                .storeName(1)
                .boughtAt(LocalDate.of(2023, 12, 12))
                .createdAt(LocalDate.of(2023, 12, 22))
                .reviewImages(List.of(GetReviewImageResponseDto.builder().imgUrl("imageUrl").orderNum(1).build()))
                .scrapedCount(2L)
                .build();

        ReviewDetailResponse.GetUserResponseDto writerInfo = ReviewDetailResponse.GetUserResponseDto.builder()
                .id(1L)
                .nickName("nickName")
                .job(Job.SW_DEVELOPER)
                .profileImage("profileImage")
                .isExpert(true)
                .build();

        List<ReviewDetailResponse.GetRelatedReviewResponseDto> getRelatedReviewResponseDtos
                = List.of(ReviewDetailResponse.GetRelatedReviewResponseDto.builder().productName("productName")
                .reviewImage("reviewImage")
                .writerName("동근")
                .isExpert(true).build());

        ReviewDetailResponse.ReviewLikeInfo reviewLikeInfo = ReviewDetailResponse.ReviewLikeInfo.builder()
                .reviewLikeUserInfo(List.of(ReviewDetailResponse.GetUserResponseForLikeAndComment.builder()
                        .nickName("nickName")
                        .job(Job.SW_DEVELOPER)
                        .profileImage("profileImage")
                        .build()))
                .reviewLikedCount(2L)
                .build();

        ReviewDetailResponse.ReviewCommentInfo reviewCommentInfo = ReviewDetailResponse.ReviewCommentInfo.builder().
                reviewCommentCount(2L)
                .reviewCommentResponses(List.of(ReviewDetailResponse.ReviewCommentInfo.ReviewCommentResponse.builder()
                        .reviewCommentUserInfo(ReviewDetailResponse.GetUserResponseForLikeAndComment.builder()
                                .nickName("nickName")
                                .job(Job.SW_DEVELOPER)
                                .profileImage("profileImage")
                                .build())
                        .comment("comment")
                        .commentCreatedAt(LocalDate.now())
                        .build()))
                .build();

        ReviewDetailResponse response = ReviewDetailResponse.builder()
                .getReviewDetailResponseDto(getReviewDetailResponseDto)
                .writerInfo(writerInfo)
                .getRelatedReviewResponseDto(getRelatedReviewResponseDtos)
                .reviewLikeInfo(reviewLikeInfo)
                .reviewCommentInfo(reviewCommentInfo)
                .build();

        given(this.reviewService.getReviewDetail(anyLong()))
                .willReturn(response);
        // when & then
        mockMvc.perform(get("/review/detail/{reviewId}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.writerInfo.id").value(1))
                .andExpect(jsonPath("$.result.relatedReviews[0].productName").value("productName"));
    }

    @Test
    @WithMockUser
    @DisplayName("리뷰 좋아요 컨트롤러 테스트를 진행한다.")
    void reviewLikeControllerTest() throws Exception {
        // given

        // 좋아요 하는 리뷰
        Review review = createReview(1L);

        // 좋아요 하는 유저
        Users users = Users.builder()
                .id(1L)
                .email("email")
                .roleType(ROLE_USER)
                .build();

        // when && then
        mockMvc.perform(post("/review/like/{reviewId}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("리뷰 상세 조회 API 리팩토링에 대한 컨트롤러 테스트를 진행한다.")
    void getReviewDetailTest() throws Exception {
        // given

        Users users = Users.builder()
                .id(1L)
                .email("email")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nickname").job(Job.SW_DEVELOPER).build())
                .build();

        Review review = Review.builder()
                .writer(users)
                .category(PC)
                .productName("productName")
                .category(PC)
                .price(10)
                .storeName(1)
                .status(ACTIVE)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .shortReview("short")
                .build();

        GetReviewDetailResponseDto getReviewDetailResponseDto = GetReviewDetailResponseDto.builder()
                .id(1L)
                .productName("productName")
                .reviewSpecData(List.of("spec1", "spec2"))
                .starPoint(2.0)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .shortReview("shortReview")
                .manufacturer("삼성")
                .storeName(1)
                .boughtAt(LocalDate.of(2023, 12, 12))
                .createdAt(LocalDate.of(2023, 12, 22))
                .reviewImages(List.of(GetReviewImageResponseDto.builder().imgUrl("imageUrl").orderNum(1).build()))
                .scrapedCount(2L)
                .build();

        ReviewDetailResponse.GetUserResponseDto writerInfo = ReviewDetailResponse.GetUserResponseDto.builder()
                .id(1L)
                .nickName("nickName")
                .job(Job.SW_DEVELOPER)
                .profileImage("profileImage")
                .isExpert(true)
                .build();

        List<ReviewDetailResponse.GetRelatedReviewResponseDto> getRelatedReviewResponseDtos
                = List.of(ReviewDetailResponse.GetRelatedReviewResponseDto.builder().productName("productName")
                .reviewImage("reviewImage")
                .writerName("동근")
                .isExpert(true).build());

        ReviewDetailResponse.ReviewLikeInfo reviewLikeInfo = ReviewDetailResponse.ReviewLikeInfo.builder()
                .reviewLikeUserInfo(List.of(ReviewDetailResponse.GetUserResponseForLikeAndComment.builder()
                        .nickName("nickName")
                        .job(Job.SW_DEVELOPER)
                        .profileImage("profileImage")
                        .build()))
                .reviewLikedCount(2L)
                .build();

        ReviewDetailResponse.ReviewCommentInfo reviewCommentInfo = ReviewDetailResponse.ReviewCommentInfo.builder().
                reviewCommentCount(2L)
                .reviewCommentResponses(List.of(ReviewDetailResponse.ReviewCommentInfo.ReviewCommentResponse.builder()
                        .reviewCommentUserInfo(ReviewDetailResponse.GetUserResponseForLikeAndComment.builder()
                                .nickName("nickName")
                                .job(Job.SW_DEVELOPER)
                                .profileImage("profileImage")
                                .build())
                        .comment("comment")
                        .commentCreatedAt(LocalDate.now())
                        .isMine(true)
                        .build()))
                .build();

        ReviewDetailResponse response = ReviewDetailResponse.builder()
                .getReviewDetailResponseDto(getReviewDetailResponseDto)
                .writerInfo(writerInfo)
                .getRelatedReviewResponseDto(getRelatedReviewResponseDtos)
                .reviewLikeInfo(reviewLikeInfo)
                .reviewCommentInfo(reviewCommentInfo)
                .build();

        given(this.reviewService.getReviewDetail(anyLong()))
                .willReturn(response);
        // when & then
        mockMvc.perform(get("/review/detail/{reviewId}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.writerInfo.id").value(1))
                .andExpect(jsonPath("$.result.relatedReviews[0].productName").value("productName"))
                .andExpect(jsonPath("$.result.reviewCommentInfo.reviewComments[0].mine").value(true));
    }

    @Test
    @DisplayName("리뷰 상세 조회 API 리팩토링에 대한 컨트롤러 테스트를 진행한다.")
    void getReviewDetailTest02() throws Exception {
        // given

        Users users = Users.builder()
                .id(1L)
                .email("email")
                .roleType(ROLE_USER)
                .usersDetail(UsersDetail.builder().nickName("nickname").job(Job.SW_DEVELOPER).build())
                .build();

        Review review = Review.builder()
                .writer(users)
                .category(PC)
                .productName("productName")
                .category(PC)
                .price(10)
                .storeName(1)
                .status(ACTIVE)
                .boughtAt(LocalDate.now())
                .starPoint(1)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .shortReview("short")
                .build();

        GetReviewDetailResponseDto getReviewDetailResponseDto = GetReviewDetailResponseDto.builder()
                .id(1L)
                .productName("productName")
                .reviewSpecData(List.of("spec1", "spec2"))
                .starPoint(2.0)
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .shortReview("shortReview")
                .manufacturer("삼성")
                .storeName(1)
                .boughtAt(LocalDate.of(2023, 12, 12))
                .createdAt(LocalDate.of(2023, 12, 22))
                .reviewImages(List.of(GetReviewImageResponseDto.builder().imgUrl("imageUrl").orderNum(1).build()))
                .scrapedCount(2L)
                .isLike(true)
                .isScrap(true)
                .build();

        ReviewDetailResponse.GetUserResponseDto writerInfo = ReviewDetailResponse.GetUserResponseDto.builder()
                .id(1L)
                .nickName("nickName")
                .job(Job.SW_DEVELOPER)
                .profileImage("profileImage")
                .isExpert(true)
                .build();

        List<ReviewDetailResponse.GetRelatedReviewResponseDto> getRelatedReviewResponseDtos
                = List.of(ReviewDetailResponse.GetRelatedReviewResponseDto.builder().productName("productName")
                .reviewImage("reviewImage")
                .writerName("동근")
                .isExpert(true).build());

        ReviewDetailResponse.ReviewLikeInfo reviewLikeInfo = ReviewDetailResponse.ReviewLikeInfo.builder()
                .reviewLikeUserInfo(List.of(ReviewDetailResponse.GetUserResponseForLikeAndComment.builder()
                        .nickName("nickName")
                        .job(Job.SW_DEVELOPER)
                        .profileImage("profileImage")
                        .build()))
                .reviewLikedCount(2L)
                .build();

        ReviewDetailResponse.ReviewCommentInfo reviewCommentInfo = ReviewDetailResponse.ReviewCommentInfo.builder().
                reviewCommentCount(2L)
                .reviewCommentResponses(List.of(ReviewDetailResponse.ReviewCommentInfo.ReviewCommentResponse.builder()
                        .reviewCommentUserInfo(ReviewDetailResponse.GetUserResponseForLikeAndComment.builder()
                                .nickName("nickName")
                                .job(Job.SW_DEVELOPER)
                                .profileImage("profileImage")
                                .build())
                        .comment("comment")
                        .commentCreatedAt(LocalDate.now())
                        .isMine(true)
                        .build()))
                .build();

        ReviewDetailResponse response = ReviewDetailResponse.builder()
                .getReviewDetailResponseDto(getReviewDetailResponseDto)
                .writerInfo(writerInfo)
                .getRelatedReviewResponseDto(getRelatedReviewResponseDtos)
                .reviewLikeInfo(reviewLikeInfo)
                .reviewCommentInfo(reviewCommentInfo)
                .build();

        given(this.reviewService.getReviewDetail(anyLong()))
                .willReturn(response);
        // when & then
        mockMvc.perform(get("/review/detail/{reviewId}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.writerInfo.id").value(1))
                .andExpect(jsonPath("$.result.relatedReviews[0].productName").value("productName"))
                .andExpect(jsonPath("$.result.reviewCommentInfo.reviewComments[0].mine").value(true))
                .andExpect(jsonPath("$.result.reviewDetail.like").value(true))
                .andExpect(jsonPath("$.result.reviewDetail.scrap").value(true));
    }
}