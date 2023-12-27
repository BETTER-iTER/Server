package com.example.betteriter.bo_domain.category.service;

import com.example.betteriter.bo_domain.category.dto.GetCategoriesResponseDto;
import com.example.betteriter.global.constant.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {
    public List<GetCategoriesResponseDto> getCategories() {
        return Stream.of(Category.values())
                .map(GetCategoriesResponseDto::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
