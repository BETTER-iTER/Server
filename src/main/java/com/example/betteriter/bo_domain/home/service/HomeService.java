package com.example.betteriter.bo_domain.home.service;

import com.example.betteriter.bo_domain.home.dto.CategoryResponseDto;
import com.example.betteriter.bo_domain.home.dto.GetHomeResponseDto;
import com.example.betteriter.bo_domain.news.dto.ITNewsResponseDto;
import com.example.betteriter.bo_domain.news.service.NewsConnector;
import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import com.example.betteriter.fo_domain.review.service.ReviewConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {
    private final NewsConnector newsConnector;
    private final ReviewConnector reviewConnector;

    /**
     * 1. 최신 IT 뉴스
     * 2. 모든 카테고리(찐 리뷰)
     * 3. 관심 카테고리 최신 리뷰 리스트
     * 5. 팔로우들의 리뷰 리스트
     * 6. 리뷰보고 구매했어요
     **/

    @Transactional(readOnly = true)
    public GetHomeResponseDto getHome() {

        List<ITNewsResponseDto> recentItNews = this.newsConnector.getTop5ITNews();
        Map<String, List<ReviewResponseDto>> categoryReviews = this.reviewConnector.getUserCategoryReviews();
        List<ReviewResponseDto> followingReviews = this.reviewConnector.getFollowingReviews();
        List<ReviewResponseDto> mostScrapedAndLikedReviews = this.reviewConnector.getMostScrapedAndLikedReviews();

        return GetHomeResponseDto.builder()
                .news(recentItNews) // 1. 최신 IT 뉴스 (5)
                .categories(CategoryResponseDto.of()) // 2. 모든 카테고리(찐 리뷰) (7)
                .categoryReviews(categoryReviews) // 3. 관심 등록한 카테고리의 리뷰 (7)
                .followingReviews(followingReviews) // 4. 유저가 팔로우 하는 팔로잉의 리뷰 (7)
                .mostScrapedAndLikedReviews(mostScrapedAndLikedReviews) // 5. 전체 리뷰 중 가장 많은 스크랩 + 좋아요 리뷰 (7)
                .build();
    }
}
