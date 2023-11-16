package com.example.betteriter.fo_domain.comment.dto;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.validation.annotation.ActivatedReview;
import com.example.betteriter.fo_domain.review.validation.annotation.ExistReview;
import com.example.betteriter.fo_domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentRequestDto {

        @ExistReview
        @ActivatedReview
        private Long review_id;
        private String comment;
        private Integer orderNum;
        private Integer groupId;

        public Comment toEntity(Review review, User writer) {
            return Comment.builder()
                    .review(review)
                    .user(writer)
                    .comment(this.comment)
                    .orderNum(this.orderNum)
                    .groupId(this.groupId)
                    .build();
        }
    }
}
