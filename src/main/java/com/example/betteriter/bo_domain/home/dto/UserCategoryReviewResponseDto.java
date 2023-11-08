package com.example.betteriter.bo_domain.home.dto;

import com.example.betteriter.fo_domain.review.dto.ReviewResponseDto;
import lombok.*;

import java.util.List;

/**
 * - [홈 화면 유저가 관심 등록한 카테고리 관련 리뷰 리스트]
 * # 관심 등록한 카테고리 관련 리뷰가 좌우 스크롤로 7개(최신 순)
 * # 노출 정보 : 대표 사진(1 st),제품명,작성자 프로필, 작성자 이름, 전문가 뱃지
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCategoryReviewResponseDto {
    private String categoryName; // 카테고리 이름
    private List<ReviewResponseDto> reviews; // 카테고리에 해당하는 리뷰 데이터
}
