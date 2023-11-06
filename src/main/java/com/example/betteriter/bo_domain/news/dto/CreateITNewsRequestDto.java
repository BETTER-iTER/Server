package com.example.betteriter.bo_domain.news.dto;

import com.example.betteriter.bo_domain.news.domain.News;
import com.example.betteriter.fo_domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateITNewsRequestDto {
    private String title;
    @NotBlank(message = "IT 뉴스 내용은 필수 입력 값 입니다.")
    private String content;
    private String imageUrl;
    private String newsUrl;

    public News toEntity(User user) {
        return News.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .newsUrl(newsUrl)
                .writer(user)
                .build();
    }
}
