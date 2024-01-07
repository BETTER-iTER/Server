package com.example.betteriter.bo_domain.notification.converter;

import com.example.betteriter.bo_domain.notification.dto.NotificationRequest;
import com.example.betteriter.bo_domain.notification.dto.NotificationResponse;

public class NotificationResponseConverter {


    public static NotificationResponse.CreateNotificationDto toCreateNotificationDto(
            NotificationRequest.CreateNotificationDto request
    ) {
        return NotificationResponse.CreateNotificationDto.builder()
                .message("알림이 등록되었습니다.")
                .build();
    }
}
