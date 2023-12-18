package com.example.betteriter.fo_domain.review.controller;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto.CreateReviewImageRequestDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewResponse;
import com.example.betteriter.fo_domain.review.service.ReviewService;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.global.config.security.SecurityConfig;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.example.betteriter.global.constant.Category.PC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;


    @Test
    @WithMockUser(username = "choi")
    @DisplayName("상품 이름에 해당하는 리뷰 리스트를 성공적으로 조회한다.")
    void getReviewsBySearchSuccessfulTest() throws Exception {
        // given
        Users user = Users.builder()
                .usersDetail(UsersDetail.builder()
                        .nickName("nickName")
                        .profileImage("profileImage")
                        .job(Job.DEVELOPER)
                        .build())
                .build();

        Review review = Review.builder()
                .writer(user)
                .id(1L)
                .category(PC)
                .productName("productName")
                .amount(10000)
                .storeName(1)
                .starPoint(1.0)
                .badPoint("badPoint")
                .build();

        GetReviewResponseDto getReviewResponseDto = GetReviewResponseDto.builder()
                .review(review)
                .reviewSpecData(List.of("string", "string02"))
                .firstImage("firstImage")
                .build();

        given(reviewService.getReviewBySearch(anyString(), anyString()))
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
                .andExpect(jsonPath("$.data.isExited").value(true))
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
                        .job(Job.DEVELOPER)
                        .build())
                .build();

        Review review = Review.builder()
                .writer(user)
                .id(1L)
                .category(PC)
                .productName("productName")
                .amount(10000)
                .storeName(1)
                .starPoint(1.0)
                .badPoint("badPoint")
                .build();

        GetReviewResponseDto getReviewResponseDto = GetReviewResponseDto.builder()
                .review(review)
                .reviewSpecData(List.of("string", "string02"))
                .firstImage("firstImage")
                .build();

        given(reviewService.getReviewBySearch(anyString(), anyString()))
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
                .amount(1000)
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
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS_200"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result").value(1L));
    }
}