package com.example.betteriter.bo_domain.notification.dto;

import com.example.betteriter.bo_domain.notification.domain.MessageType;
import lombok.Builder;
import lombok.Getter;

public class NotificationRequest {

    @Getter
    @Builder
    public static class CreateNotificationDto {
        private Long actorId;
        private Long receiverId;
        private MessageType messageType;
        private MessageDetailDto messageDetail;
    }

    @Getter
    @Builder
    public static class MessageDetailDto {
        private Long contentId; // message type에 따른 content id
        private String title;
        private String content;
    }
}
