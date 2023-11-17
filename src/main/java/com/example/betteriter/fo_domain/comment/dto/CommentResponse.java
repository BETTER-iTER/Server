package com.example.betteriter.fo_domain.comment.dto;

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
        private List<CommentResponseDto> commentList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CommentResponseDto {
        private Long commentId;
        private String comment;
        private Integer orderNum;
        private Integer groupId;
        private String writer;
        private String createdAt;
        private String updatedAt;
    }
}
