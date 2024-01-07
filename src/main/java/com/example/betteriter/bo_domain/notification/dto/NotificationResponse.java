package com.example.betteriter.bo_domain.notification.dto;

import lombok.Builder;
import lombok.Getter;

public class NotificationResponse {

    @Getter
    @Builder
    public static class CreateNotificationDto {
        private String message;
    }
}
