package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.fo_domain.user.domain.Users;
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
    public ReviewLikeResponse(Long userId, String nickname, Job job, String profileImage) {
        this.userId = userId;
        this.nickname = nickname;
        this.job = job;
        this.profileImage = profileImage;
    }

    public static ReviewLikeResponse from(Users user) {
        return ReviewLikeResponse.builder()
                .userId(user.getId())
                .nickname(user.getUsersDetail().getNickName())
                .job(user.getUsersDetail().getJob())
                .profileImage(user.getUsersDetail().getProfileImage())
                .build();
    }
}
