package com.example.betteriter.fo_domain.mypage.dto;

import lombok.Builder;
import lombok.Getter;

public class MypageResponse {

    @Getter
    @Builder
    public static class MyReviewDto{
        private Long review_id;
        private String title;
        private String profile_image;
        private Long like_count;
        private Long scrap_count;
    }
}
