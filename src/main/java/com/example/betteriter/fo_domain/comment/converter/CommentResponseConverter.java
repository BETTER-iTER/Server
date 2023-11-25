package com.example.betteriter.fo_domain.comment.converter;

import com.example.betteriter.fo_domain.comment.dto.CommentResponse;

public class CommentResponseConverter {

    public static CommentResponse.DeleteCommentDto toDeleteCommentResponse() {
        return CommentResponse.DeleteCommentDto.builder()
                .message("댓글 삭제에 성공했습니다.")
                .build();
    }
}
