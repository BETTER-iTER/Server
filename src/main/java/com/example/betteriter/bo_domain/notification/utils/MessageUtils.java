package com.example.betteriter.bo_domain.notification.utils;

import com.example.betteriter.bo_domain.notification.domain.Notification;
import com.example.betteriter.bo_domain.notification.dto.message.MessageDetail;
import com.example.betteriter.fo_domain.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageUtils {

    private final FollowService followService;

    public MessageDetail getMessageDetail(Notification notification) {
        MessageDetail messageDetail = null;

        switch (notification.getMessageType()) {
            case REVIEW_LIKE:
                MessageDetail.ReviewLikeMessageDetail reviewLikeMessageDetail = MessageDetail.ReviewLikeMessageDetail.builder()
                        .build();

                messageDetail = MessageDetail.of(
                        notification.getMessageJson().getId(),
                        notification.getMessageType().getMessage(),
                        reviewLikeMessageDetail);
                break;
            case REVIEW_COMMENT:
                MessageDetail.ReviewCommentMessageDetail reviewCommentMessageDetail = MessageDetail.ReviewCommentMessageDetail.builder()
                        .commentMessage(notification.getMessageJson().getContent())
                        .build();

                messageDetail = MessageDetail.of(
                        notification.getMessageJson().getId(),
                        notification.getMessageType().getMessage(),
                        reviewCommentMessageDetail);
                break;
            case FOLLOW:
                MessageDetail.FollowMessageDetail followMessageDetail = MessageDetail.FollowMessageDetail.builder()
                        .isFollow(followService.isFollow(notification.getActor(), notification.getReceiver()))
                        .build();

                messageDetail = MessageDetail.of(
                        notification.getMessageJson().getId(),
                        notification.getMessageType().getMessage(),
                        followMessageDetail);
                break;
            case ANNOUNCEMENT:
                MessageDetail.AnnouncementMessageDetail announcementMessageDetail = MessageDetail.AnnouncementMessageDetail.builder()
                        .title(notification.getMessageJson().getTitle())
                        .content(notification.getMessageJson().getContent())
                        .build();
                break;
            default:
                break;
        }
        return messageDetail;
    }
}
