package com.example.betteriter.fo_domain.comment.dto;

import com.example.betteriter.fo_domain.comment.validation.annotation.ExistComment;
import com.example.betteriter.fo_domain.comment.validation.annotation.UserHaveComment;
import com.example.betteriter.fo_domain.review.validation.annotation.ExistReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentDto {

        @ExistReview
        private Long review_id;
        private String comment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteCommentDto {

        private Long comment_id;

    }
}
