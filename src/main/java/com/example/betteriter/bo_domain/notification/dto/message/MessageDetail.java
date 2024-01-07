package com.example.betteriter.bo_domain.notification.dto.message;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageDetail<T> {
    private Long contentId;
    private String message;
    private T detail;

    public static <T> MessageDetail<T> of(Long contentId, String message, T detail) {
        MessageDetail<T> messageDetail = new MessageDetail<>();
        messageDetail.contentId = contentId;
        messageDetail.message = message;
        messageDetail.detail = detail;
        return messageDetail;
    }

    @Getter
    @Builder
    public static class ReviewLikeMessageDetail extends MessageDetail<ReviewLikeMessageDetail> {

    }

    @Getter
    @Builder
    public static class ReviewCommentMessageDetail extends MessageDetail<ReviewCommentMessageDetail> {
        private String commentMessage;
    }

    @Getter
    @Builder
    public static class FollowMessageDetail extends MessageDetail<FollowMessageDetail> {
        private boolean isFollow;
    }

    @Getter
    @Builder
    public static class AnnouncementMessageDetail extends MessageDetail<AnnouncementMessageDetail> {
        private String title;
        private String content;
    }
}
