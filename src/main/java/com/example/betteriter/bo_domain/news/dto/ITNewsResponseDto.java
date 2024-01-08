package com.example.betteriter.bo_domain.news.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ITNewsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String newsUrl;

    @Builder
    public ITNewsResponseDto(Long id, String title, String content, String imageUrl, String newsUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.newsUrl = newsUrl;
    }
}
