package com.example.betteriter.bo_domain.notification.domain;

import lombok.Getter;

@Getter
public enum MessageType {
    REVIEW_LIKE("리뷰 좋아요"),
    REVIEW_COMMENT("리뷰 댓글"),
    FOLLOW("팔로우"),
    ANNOUNCEMENT("공지사항")

    ;

    private final String description;

    MessageType(String description) {
        this.description = description;
    }

}
