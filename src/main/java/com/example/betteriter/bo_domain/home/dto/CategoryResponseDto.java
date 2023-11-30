package com.example.betteriter.bo_domain.home.dto;

import com.example.betteriter.global.constant.Category;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private String name;
    private String imageUrl;

    public static List<CategoryResponseDto> of() {
        return Arrays.stream(Category.values())
                .map(category -> new CategoryResponseDto(category.getName(), category.getImgUrl()))
                .collect(Collectors.toList());
    }
}
