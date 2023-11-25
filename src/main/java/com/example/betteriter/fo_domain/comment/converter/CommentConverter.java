package com.example.betteriter.fo_domain.comment.converter;

import com.example.betteriter.fo_domain.comment.domain.Comment;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.global.constant.Status;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentConverter {

    public static Comment toComment(
            String comment, Review review, Users writer
    ) {
        log.info("댓글 생성 시작: {}", comment);

        return Comment.builder()
                .review(review)
                .users(writer)
                .orderNum(1)
                .comment(comment)
                .status(Status.ACTIVE)
                .build();
    }
}
