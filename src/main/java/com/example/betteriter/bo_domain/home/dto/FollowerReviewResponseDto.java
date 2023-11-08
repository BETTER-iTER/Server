package com.example.betteriter.bo_domain.home.dto;

import com.example.betteriter.global.constant.RoleType;
import lombok.*;

import java.util.List;

/**
 * - [유저가 팔로우 유저의 리뷰 리스트]
 * # 팔로우한 유저의 리뷰 7개(최신 순)
 * # 노출 정보 : 대표 사진(1 st), 제품명, 작성자 프로필, 전문가 뱃지
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerReviewResponseDto {
    private Long id;
    private String imageUrl;
    private String productName;
    private String nickName;
    private String profileImageUrl;
    private List<RoleType> roles;
}
