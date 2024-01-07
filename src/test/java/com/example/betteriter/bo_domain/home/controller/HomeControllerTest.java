package com.example.betteriter.bo_domain.home.controller;

import com.example.betteriter.bo_domain.home.dto.CategoryResponseDto;
import com.example.betteriter.bo_domain.home.dto.GetHomeResponseDto;
import com.example.betteriter.bo_domain.home.service.HomeService;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.global.config.security.SecurityConfig;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = HomeController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RedisUtil.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class)}
)
@WithMockUser
class HomeControllerTest {

    @MockBean
    private HomeService homeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("홈 화면을 조회한다.")
    void getHomeControllerTest() throws Exception {
        // given

        List<ITNewsResponseDto> itNewsResponseDto = List.of(ITNewsResponseDto.builder()
                .id(1L)
                .title("title")
                .content("content")
                .imageUrl("imageUrl")
                .newsUrl("newUrl")
                .build());

        ReviewResponseDto reviewResponseDto1 = ReviewResponseDto.builder()
                .id(2L)
                .imageUrl("imageUrl1")
                .productName("productName1")
                .nickname("nickname1")
                .profileImageUrl("profileImageUrl1")
                .isExpert(false)
                .build();

        ReviewResponseDto reviewResponseDto2 = ReviewResponseDto.builder()
                .id(3L)
                .imageUrl("imageUrl2")
                .productName("productName2")
                .nickname("nickname2")
                .profileImageUrl("profileImageUrl2")
                .isExpert(false)
                .build();

        Map<String, List<ReviewResponseDto>> categoryReviews = new HashMap<>();
        categoryReviews.put("PC", List.of(reviewResponseDto1));

        List<ReviewResponseDto> followingReviews = List.of(reviewResponseDto1, reviewResponseDto2);
        List<ReviewResponseDto> mostScrapedAndLikedReviews = List.of(reviewResponseDto1, reviewResponseDto2);

        given(this.homeService.getHome())
                .willReturn(GetHomeResponseDto.builder()
                        .news(itNewsResponseDto)
                        .categories(CategoryResponseDto.of())
                        .categoryReviews(categoryReviews)
                        .followingReviews(followingReviews)
                        .mostScrapedAndLikedReviews(mostScrapedAndLikedReviews)
                        .build());

        // when && then
        this.mockMvc.perform(get("/home")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
