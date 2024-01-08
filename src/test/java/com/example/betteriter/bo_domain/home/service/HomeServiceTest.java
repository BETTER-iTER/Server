package com.example.betteriter.bo_domain.home.service;

import com.example.betteriter.bo_domain.home.dto.GetHomeResponseDto;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.service.NewsConnector;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.review.service.ReviewConnector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {
    @InjectMocks
    private HomeService homeService;

    @Mock
    private NewsConnector newsConnector;

    @Mock
    private ReviewConnector reviewConnector;

    @Test
    @DisplayName("홈 화면을 조회한다 - 성공")
    void getHomeResponseInSuccess() {
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

        given(newsConnector.getTop5ITNews())
                .willReturn(itNewsResponseDto);

        given(reviewConnector.getUserCategoryReviews())
                .willReturn(categoryReviews);

        given(reviewConnector.getFollowingReviews())
                .willReturn(followingReviews);

        given(reviewConnector.getMostScrapedAndLikedReviews())
                .willReturn(mostScrapedAndLikedReviews);

        // when
        GetHomeResponseDto result = this.homeService.getHome();

        // then
        assertThat(result.getCategories()).hasSize(12);
        assertThat(result.getNews()).isEqualTo(itNewsResponseDto);
        verify(newsConnector, times(1)).getTop5ITNews();
        verify(reviewConnector, times(1)).getUserCategoryReviews();
    }
}
