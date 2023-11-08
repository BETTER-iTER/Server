package com.example.betteriter.fo_domain.review.dto;

import lombok.*;

/**
 * - 리뷰 응답 전용 DTO
 **/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String imageUrl;
    private String productName;
    private String nickname;
    private String profileImageUrl;
    private boolean isExpert;
}
