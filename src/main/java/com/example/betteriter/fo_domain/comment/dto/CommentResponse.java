package com.example.betteriter.fo_domain.comment.dto;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetCommentDto {
        private Long id;
        private String comment;
        private String createdAt;
        private WriterDto writer;
        private List<GetCommentDto> child = new ArrayList<>();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WriterDto {
        private Long id;
        private String nickname;
        private String profileImage;
    }
}
