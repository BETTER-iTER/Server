package com.example.betteriter.bo_domain.notification.dto;

import com.example.betteriter.bo_domain.notification.domain.MessageType;
import com.example.betteriter.bo_domain.notification.dto.message.MessageDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class NotificationResponse {

    @Getter
    @Builder
    public static class CreateNotificationDto {
        private String message;
    }

    @Getter
    @Builder
    public static class NotificationDto<T> {
        private Long actorId;
        private String actorNickName;
        private String actorProfileImageUrl;

        private MessageType messageType;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private MessageDetail<T> messageDetail;

        private LocalDateTime createdAt;
        private boolean isRead;
    }
}
