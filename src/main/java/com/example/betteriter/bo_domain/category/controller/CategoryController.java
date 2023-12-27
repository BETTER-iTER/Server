package com.example.betteriter.bo_domain.category.controller;

import com.example.betteriter.bo_domain.category.dto.GetCategoriesResponseDto;
import com.example.betteriter.bo_domain.category.service.CategoryService;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "CategoryController", description = "Category API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/category")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseDto<List<GetCategoriesResponseDto>> getCategories() {
        return ResponseDto.onSuccess(this.categoryService.getCategories());
    }
}
