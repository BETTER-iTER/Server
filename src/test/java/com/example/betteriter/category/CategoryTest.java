package com.example.betteriter.category;


import com.example.betteriter.bo_domain.home.dto.CategoryResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;


public class CategoryTest {
    @Test
    public void categoryTest() {
        List<CategoryResponseDto> result = CategoryResponseDto.of();
        for (CategoryResponseDto categoryResponseDto : result) {
            System.out.println("categoryResponseDto = " + categoryResponseDto);
        }
        System.out.println("result.size() = " + result.size());
    }
}
