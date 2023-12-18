package com.example.betteriter.bo_domain.news.service;

import com.example.betteriter.bo_domain.news.domain.News;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.repository.NewsRepository;
import com.example.betteriter.fo_domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;
    @Mock
    private UserService userService;
    @Mock
    private NewsRepository newsRepository;

    @Test
    @DisplayName("IT News 최신 5개를 조회한다.")
    void findTop5ByOrderBtCreatedAtDescTest() {
        // given
        given(newsRepository.findTop5ByOrderByCreatedAtDesc())
                .willReturn(List.of(
                        News.builder()
                                .id(1L)
                                .newsUrl("news")
                                .imageUrl("img")
                                .title("title")
                                .content("content")
                                .build(), News.builder()
                                .id(2L)
                                .newsUrl("news2")
                                .imageUrl("img2")
                                .title("title2")
                                .content("content2")
                                .build()));

        // when
        List<ITNewsResponseDto> top5ITNews = newsService.getTop5ITNews();

        // then
        assertThat(top5ITNews).isNotNull();
        assertThat(top5ITNews).hasSize(2);
        assertThat(top5ITNews.get(0))
                .extracting("id", "title", "content")
                .containsExactlyInAnyOrder(1L, "title", "content");
        verify(newsRepository, times(1)).findTop5ByOrderByCreatedAtDesc();

    }
}
