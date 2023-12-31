package com.example.betteriter.fo_domain.comment.dto;

import com.example.betteriter.fo_domain.comment.validation.annotation.ExistComment;
import com.example.betteriter.fo_domain.comment.validation.annotation.UserHaveComment;
import com.example.betteriter.fo_domain.review.validation.annotation.ExistReview;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "리뷰 아이디", example = "1")
        private Long review_id;

        @Schema(description = "댓글 내용", example = "댓글 내용")
        private String comment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteCommentDto {

        @ExistComment
        @UserHaveComment
        @Schema(description = "댓글 아이디", example = "1")
        private Long comment_id;

    }
}
