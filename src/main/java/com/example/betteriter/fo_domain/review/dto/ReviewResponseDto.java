package com.example.betteriter.fo_domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * - 리뷰 응답 전용 DTO
 **/
@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String imageUrl;
    private String productName;
    private String nickname;
    private String profileImageUrl;
    private boolean isExpert;

    @Builder
    public ReviewResponseDto(Long id, String imageUrl, String productName,
                             String nickname, String profileImageUrl, boolean isExpert
    ) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isExpert = isExpert;
    }
}
