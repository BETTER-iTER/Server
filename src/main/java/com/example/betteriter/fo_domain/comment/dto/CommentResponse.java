package com.example.betteriter.fo_domain.comment.dto;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CommentResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadCommentDto {
        private Long reviewId;
        private List<Comment> commentList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteCommentDto {
        private String message;
    }
}
