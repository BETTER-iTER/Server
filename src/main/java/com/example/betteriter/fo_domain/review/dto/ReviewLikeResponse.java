package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.global.constant.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * - 리뷰 좋아요 데이터 응답 DTO
 **/
@Getter
@NoArgsConstructor
public class ReviewLikeResponse {
    private Long userId;
    private String nickname;
    private Job job;
    private String profileImage;

    @Builder
    public ReviewLikeResponse(Long id, String nickname, Job job, String profileImage) {
        this.userId = id;
        this.nickname = nickname;
        this.job = job;
        this.profileImage = profileImage;
    }
}
