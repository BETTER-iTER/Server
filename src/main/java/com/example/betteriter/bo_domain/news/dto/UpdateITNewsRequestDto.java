package com.example.betteriter.bo_domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateITNewsRequestDto {
    private String title;
    @NotBlank(message = "IT 뉴스 내용은 필수 입력 값 입니다.")
    private String content;
    private String imageUrl;
    private String newsUrl;

}
