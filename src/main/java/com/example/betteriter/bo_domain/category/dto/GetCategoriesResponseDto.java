package com.example.betteriter.bo_domain.category.dto;

import com.example.betteriter.global.constant.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCategoriesResponseDto {
    private String name;
    private String imageUrl;

    @Builder
    public GetCategoriesResponseDto(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static GetCategoriesResponseDto from(Category category) {
        return GetCategoriesResponseDto.builder()
                .name(category.getCategoryName())
                .imageUrl(category.getImgUrl())
                .build();
    }
}
