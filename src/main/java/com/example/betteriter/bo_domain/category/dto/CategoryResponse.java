package com.example.betteriter.bo_domain.category.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String categoryName;
    private String imageUrl;
}
