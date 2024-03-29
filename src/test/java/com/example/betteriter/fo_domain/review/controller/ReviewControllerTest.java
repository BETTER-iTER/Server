package com.example.betteriter.fo_domain.review.controller;

import static com.example.betteriter.global.constant.Category.PC;
import static com.example.betteriter.global.constant.Job.CEO;
import static com.example.betteriter.global.constant.Job.SW_DEVELOPER;
import static com.example.betteriter.global.constant.RoleType.ROLE_USER;
import static com.example.betteriter.global.constant.Status.ACTIVE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewDetailResponseDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewImageResponseDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewCommentResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewDetailResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewLikeResponse;
import com.example.betteriter.fo_domain.review.dto.ReviewResponse;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.global.common.code.status.ErrorStatus;
import com.example.betteriter.global.config.security.SecurityConfig;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Job;
import com.example.betteriter.global.filter.JwtAuthenticationFilter;
import com.example.betteriter.global.util.JwtUtil;
import com.example.betteriter.global.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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

        given(reviewService.getReviewBySearch(anyString(), anyString(), anyInt(), any(), any()))
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
    @DisplayName("카테고리 별 리뷰를 조회을 성공한다.")
    void getReviewsByCategoryInSuccessTest() throws Exception {
        // given
        // given
        Users user = Users.builder()
            .usersDetail(UsersDetail.builder()
                .nickName("nickName")
                .profileImage("profileImage")
                .job(Job.SW_DEVELOPER)
                .build())
            .build();

        Review review = createReview(1L);

        GetReviewResponseDto getReviewResponseDto = GetReviewResponseDto.builder()
            .review(review)
            .reviewSpecData(List.of("string", "string02"))
            .firstImage("firstImage")
            .isLike(true)
            .isScrap(true)
            .build();

        ReviewResponse reviewResponse = ReviewResponse.builder()
            .getReviewResponseDtoList(List.of(getReviewResponseDto))
            .hasNext(false)
            .isExisted(true)
            .build();

        given(this.reviewService.getReviewByCategory(any(Category.class), anyInt()))
            .willReturn(reviewResponse);

        // when && then
        this.mockMvc.perform(get("/review/category")
                .param("category", "기타")
                .param("page", "0")
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.reviews.size()").value(1));

    }

    @Test
    @DisplayName("카테고리 별 리뷰를 조회을 성공한다. - 빈 결과")
    void getEmptyReviewsByCategoryInSuccessTest() throws Exception {
        // given
        // given
        Users user = Users.builder()
            .usersDetail(UsersDetail.builder()
                .nickName("nickName")
                .profileImage("profileImage")
                .job(Job.SW_DEVELOPER)
                .build())
            .build();

        Review review = createReview(1L);

        ReviewResponse reviewResponse = ReviewResponse.builder()
            .getReviewResponseDtoList(List.of())
            .hasNext(false)
            .isExisted(false)
            .build();

        given(this.reviewService.getReviewByCategory(any(Category.class), anyInt()))
            .willReturn(reviewResponse);

        // when && then
        this.mockMvc.perform(get("/review/category")
                .param("category", "기타")
                .param("page", "0")
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.reviews.size()").value(0))
            .andExpect(jsonPath("$.result.existed").value(false));


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

        given(reviewService.getReviewBySearch(anyString(), anyString(), anyInt(), any(), any()))
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
            .comparedProductName("에어팟 2")
            .starPoint(2)
            .goodPoint("goodPoint")
            .badPoint("badPoint")
            .specData(List.of(1L, 2L))
            .build();

        MockMultipartFile file = new MockMultipartFile("files", "file.txt", "text/plain", "file content".getBytes());
        MockMultipartFile jsonPart = new MockMultipartFile("review", "", "application/json",
            objectMapper.writeValueAsString(requestDto).getBytes());

        given(this.reviewService.createReview(any(CreateReviewRequestDto.class), any()))
            .willReturn(1L); // 생성된 리뷰 id

        // when & then
        mockMvc.perform(multipart("/review")
                .file(file)
                .file(jsonPart)
                .with(csrf())
                .contentType(MULTIPART_FORM_DATA))
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

        given(reviewService.getReviewBySearch(anyString(), anyString(), anyInt(), any(), any()))
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
        verify(this.reviewService, times(1)).getReviewBySearch(anyString(), anyString(), anyInt(), any(), any());
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

        given(this.reviewService.getReviewBySearch(anyString(), anyString(), anyInt(), any(), any()))
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
            .comparedProductName("에어팟 2")
            .starPoint(1)
            .goodPoint("goodPoint")
            .badPoint("badPoint")
            .shortReview("short")
            .build();

        GetReviewDetailResponseDto getReviewDetailResponseDto = GetReviewDetailResponseDto.builder()
            .reviewId(1L)
            .productName("productName")
            .reviewSpecData(List.of("spec1", "spec2"))
            .starPoint(2.0)
            .goodPoint("goodPoint")
            .badPoint("badPoint")
            .shortReview("shortReview")
            .manufacturer("삼성")
            .comparedProductName("에어팟2")
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

        ReviewDetailResponse response = ReviewDetailResponse.builder()
            .getReviewDetailResponseDto(getReviewDetailResponseDto)
            .writerInfo(writerInfo)
            .getRelatedReviewResponseDto(getRelatedReviewResponseDtos)
            .build();

        given(this.reviewService.getReviewDetail(anyLong()))
            .willReturn(response);
        // when & then
        mockMvc.perform(get("/review/{reviewId}/detail", 1L))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andExpect(jsonPath("$.result.writerInfo.id").value(1))
            .andExpect(jsonPath("$.result.reviewDetail.comparedProductName").value("에어팟2"))
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
        mockMvc.perform(post("/review/{reviewId}/like", 1L)
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
            .reviewId(1L)
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

        ReviewDetailResponse response = ReviewDetailResponse.builder()
            .getReviewDetailResponseDto(getReviewDetailResponseDto)
            .writerInfo(writerInfo)
            .getRelatedReviewResponseDto(getRelatedReviewResponseDtos)
            .build();

        given(this.reviewService.getReviewDetail(anyLong()))
            .willReturn(response);
        // when & then
        mockMvc.perform(get("/review/{reviewId}/detail", 1L))
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
            .reviewId(1L)
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

        ReviewDetailResponse response = ReviewDetailResponse.builder()
            .getReviewDetailResponseDto(getReviewDetailResponseDto)
            .writerInfo(writerInfo)
            .getRelatedReviewResponseDto(getRelatedReviewResponseDtos)
            .build();

        given(this.reviewService.getReviewDetail(anyLong()))
            .willReturn(response);
        // when & then
        mockMvc.perform(get("/review/{reviewId}/detail/", 1L))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andExpect(jsonPath("$.result.writerInfo.id").value(1))
            .andExpect(jsonPath("$.result.relatedReviews[0].productName").value("productName"))
            .andExpect(jsonPath("$.result.reviewCommentInfo.reviewComments[0].mine").value(true))
            .andExpect(jsonPath("$.result.reviewDetail.like").value(true))
            .andExpect(jsonPath("$.result.reviewDetail.scrap").value(true));
    }

    @Test
    @DisplayName("리뷰 상세 조회 API 변경으로 인한 컨트롤러 테스트 재진행")
    void getReviewDetailTest03() throws Exception {
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
            .reviewId(1L)
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
            .likedCount(3L)
            .commentCount(100L)
            .isFollow(true)
            .isMine(false)
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

        ReviewDetailResponse response = ReviewDetailResponse.builder()
            .getReviewDetailResponseDto(getReviewDetailResponseDto)
            .writerInfo(writerInfo)
            .getRelatedReviewResponseDto(getRelatedReviewResponseDtos)
            .build();

        given(this.reviewService.getReviewDetail(anyLong()))
            .willReturn(response);

        // when & then
        mockMvc.perform(get("/review/detail/{reviewId}", 1L))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.reviewDetail.manufacturer").value("삼성"))
            .andExpect(jsonPath("$.result.reviewDetail.storeName").value(1));
    }

    @Test
    @DisplayName("리뷰 상세 좋아요 조회를 한다.")
    void getReviewDetailLikeControllerTest() throws Exception {
        // given
        ReviewLikeResponse response01 = ReviewLikeResponse.builder()
            .userId(1L)
            .nickname("nick01")
            .job(CEO)
            .profileImage("profileImage")
            .build();

        ReviewLikeResponse response02 = ReviewLikeResponse.builder()
            .userId(2L)
            .nickname("nick02")
            .job(SW_DEVELOPER)
            .profileImage("profileImage")
            .build();

        List<ReviewLikeResponse> response = List.of(response01, response02);

        given(this.reviewService.getReviewDetailLikes(anyLong()))
            .willReturn(response);
        // when & then
        mockMvc.perform(get("/review/{reviewId}/detail/likes", 1L))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andExpect(jsonPath("$.code").value("SUCCESS_200"))
            .andExpect(jsonPath("$.result").isNotEmpty())
            .andExpect(jsonPath("$.result[0].userId").value(1));
    }

    @Test
    @DisplayName("리뷰 상세 댓글 조회를 한다.")
    void getReviewDetailCommentsControllerTest() throws Exception {
        // given

        ReviewCommentResponse reviewCommentResponse01 = ReviewCommentResponse.builder()
            .id(1L)
            .reviewCommentUserInfoResponse(ReviewCommentResponse.ReviewCommentUserInfoResponse.builder()
                .userId(1L)
                .nickname("nick01")
                .job(SW_DEVELOPER)
                .profileImage("profileImage")
                .build())
            .comment("comment01")
            .createdAt(LocalDate.now())
            .isMine(true)
            .build();

        List<ReviewCommentResponse> response = List.of(reviewCommentResponse01);

        given(this.reviewService.getReviewDetailComments(1L))
            .willReturn(response);
        // when & then
        mockMvc.perform(get("/review/{reviewId}/detail/comments", 1L))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andExpect(jsonPath("$.code").value("SUCCESS_200"))
            .andExpect(jsonPath("$.result").isNotEmpty())
            .andExpect(jsonPath("$.result[0].id").value(1))
            .andExpect(jsonPath("$.result[0].reviewCommentUserInfo.userId").value(1));
    }

    @Test
    @DisplayName("리뷰을 삭제한다 - 일치하는 리뷰가 존재하지 않는다.")
    void deleteReviewControllerTestInFail() throws Exception {
        // given
        Review review = createReview(1L);

        given(this.reviewService.deleteReview(anyLong()))
            .willThrow(new ReviewHandler(ErrorStatus._REVIEW_NOT_FOUND));
        // when & then
        mockMvc.perform(delete("/review/{reviewId}", 1L).with(csrf()))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.isSuccess").value(false))
            .andExpect(jsonPath("$.code").value("REVIEW_NOT_FOUND_400"));
    }

    @Test
    @DisplayName("리뷰를 삭제한다 - 성공")
    void deleteReviewControllerTestInSuccess() throws Exception {
        // given
        Review review = createReview(1L);
        // when

        // then
        mockMvc.perform(delete("/review/{reviewId}", 1L).with(csrf()))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("리뷰 좋아요 취소를 한다 - 성공")
    void deleteReviewLikeControllerTestInSuccess() throws Exception {
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
        mockMvc.perform(delete("/review/{reviewId}/like", 1L)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    @DisplayName("리뷰 좋아요 취소 좋아요 유저와 로그인 유저 다름 - 실패")
    void deleteReviewLikeInFailure() throws Exception {
        // given
        given(this.reviewService.deleteReviewLike(anyLong()))
            .willThrow(new ReviewHandler(ErrorStatus._REVIEW_LIKE_NOT_FOUND));
        // when && then
        mockMvc.perform(delete("/review/{reviewId}/like", 1L)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.isSuccess").value(false))
            .andExpect(jsonPath("$.code").value("REVIEW_LIKE_NOT_FOUND_400"));
    }

    @Test
    @DisplayName("리뷰 스크랩을 한다 - 성공")
    void reviewScrapInSuccess() throws Exception {
        // given

        // 스크랩 하는 리뷰
        Review review = createReview(1L);

        // 스크랩 하는 유저
        Users users = Users.builder()
            .id(1L)
            .email("email")
            .roleType(ROLE_USER)
            .build();

        doNothing().when(this.reviewService).reviewScrap(anyLong());

        // when
        this.mockMvc.perform(post("/review/{reviewId}/scrap/", 1L)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    @DisplayName("리뷰 스크랩 취소를 한다 - 성공")
    void deleteReviewScrapInSuccess() throws Exception {
        // given

        // 스크랩 하는 리뷰
        Review review = createReview(1L);

        // 스크랩 하는 유저
        Users users = Users.builder()
            .id(1L)
            .email("email")
            .roleType(ROLE_USER)
            .build();

        // when && then
        this.mockMvc.perform(delete("/review/{reviewId}/scrap", 1L)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("리뷰 스크랩 취소 스크랩 유저와 로그인 유저 다름 - 실패")
    void deleteReviewScrapInFailure() throws Exception {
        // given
        given(this.reviewService.deleteReviewScrap(anyLong()))
            .willThrow(new ReviewHandler(ErrorStatus._REVIEW_SCRAP_NOT_FOUND));
        // when && then
        mockMvc.perform(delete("/review/{reviewId}/scrap", 1L)
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.isSuccess").value(false))
            .andExpect(jsonPath("$.code").value("REVIEW_SCRAP_NOT_FOUND_400"));
    }
}
