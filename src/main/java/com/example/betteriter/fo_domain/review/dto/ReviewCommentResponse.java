package com.example.betteriter.fo_domain.review.dto;

import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Job;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * - 리뷰 댓글 데이터 응답 DTO
**/
@Getter
@NoArgsConstructor
public class ReviewCommentResponse {
    private Long id;
    @JsonProperty("reviewCommentUserInfo")
    private ReviewCommentUserInfoResponse reviewCommentUserInfoResponse;
    private String comment;
    private LocalDate createdAt;
    private boolean isMine;

    @Builder
    public ReviewCommentResponse(Long id, ReviewCommentUserInfoResponse reviewCommentUserInfoResponse,
                                 String comment, LocalDate createdAt, boolean isMine
    ) {
        this.id = id;
        this.reviewCommentUserInfoResponse = reviewCommentUserInfoResponse;
        this.comment = comment;
        this.createdAt = createdAt;
        this.isMine = isMine;
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewCommentUserInfoResponse {
        private Long userId;
        private String nickname;
        private Job job;
        private String profileImage;

        @Builder
        public ReviewCommentUserInfoResponse(Long userId, String nickname, Job job, String profileImage) {
            this.userId = userId;
            this.nickname = nickname;
            this.job = job;
            this.profileImage = profileImage;
        }

        public static ReviewCommentUserInfoResponse from(Users user) {
            return ReviewCommentUserInfoResponse.builder()
                    .userId(user.getId())
                    .nickname(user.getUsersDetail().getNickName())
                    .job(user.getUsersDetail().getJob())
                    .profileImage(user.getUsersDetail().getProfileImage())
                    .build();
        }
    }
}
