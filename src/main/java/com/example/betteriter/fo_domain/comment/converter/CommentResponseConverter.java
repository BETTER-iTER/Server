package com.example.betteriter.fo_domain.comment.converter;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.comment.dto.CommentResponse;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentResponseConverter {

    public static CommentResponse.DeleteCommentDto toDeleteCommentResponse() {
        return CommentResponse.DeleteCommentDto.builder()
                .message("댓글 삭제에 성공했습니다.")
                .build();
    }

    public static List<CommentResponse.GetCommentDto> toGetCommentDto(List<Comment> commentList) {
        List<CommentResponse.GetCommentDto> comments = new ArrayList<>();

        commentList.forEach(c -> {
            CommentResponse.GetCommentDto commentDto = CommentResponse.GetCommentDto.builder()
                    .id(c.getId())
                    .comment(c.getComment())
                    .createdAt(c.getCreatedAt().toString())
                    .writer(CommentResponse.WriterDto.builder()
                            .id(c.getUser().getId())
                            .nickname(c.getUser().getUsersDetail().getNickName())
                            .profileImage(c.getUser().getUsersDetail().getProfileImage())
                            .build())
                    .build();

            comments.add(commentDto);
        });

        return comments;
    }
}
