package com.example.betteriter.bo_domain.news.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ITNewsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String newsUrl;
}
