package com.example.betteriter.bo_domain.home.dto;

import com.example.betteriter.global.constant.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
    private String name;
    private String imageUrl;

    @Builder
    public CategoryResponseDto(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static List<CategoryResponseDto> of() {
        return Arrays.stream(Category.values())
                .map(category -> new CategoryResponseDto(category.getCategoryName(), category.getImgUrl()))
                .collect(Collectors.toList());
    }
}
