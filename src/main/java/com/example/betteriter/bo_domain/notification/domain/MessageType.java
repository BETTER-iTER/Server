package com.example.betteriter.bo_domain.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    REVIEW_LIKE("리뷰 좋아요", "님이 회원님의 게시물을 좋아합니다"),
    REVIEW_COMMENT("리뷰 댓글", "님이 댓글을 남겼습니다"),
    FOLLOW("팔로우", "님이 회원님을 팔로우하기 시작했습니다"),
    ANNOUNCEMENT("공지사항", "새로운 공지사항이 등록되었습니다"),
    ;

    private final String description;
    private final String message;
}
