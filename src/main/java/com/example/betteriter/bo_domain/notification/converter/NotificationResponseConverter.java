package com.example.betteriter.bo_domain.notification.converter;

import com.example.betteriter.bo_domain.notification.domain.Notification;
import com.example.betteriter.bo_domain.notification.dto.NotificationRequest;
import com.example.betteriter.bo_domain.notification.dto.NotificationResponse;
import com.example.betteriter.bo_domain.notification.dto.message.MessageDetail;
import com.example.betteriter.bo_domain.notification.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationResponseConverter {

    private static MessageUtils messageUtils;

    public NotificationResponseConverter(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    public static NotificationResponse.CreateNotificationDto toCreateNotificationDto(
            NotificationRequest.CreateNotificationDto request
    ) {
        return NotificationResponse.CreateNotificationDto.builder()
                .message("알림이 등록되었습니다.")
                .build();
    }

    public static List<NotificationResponse.NotificationDto> toNotificationDtoList(
            List<Notification> notificationList
    ) {
        List<NotificationResponse.NotificationDto> notificationDtoList = new ArrayList<>();

        notificationList.stream().forEach(notification -> {
            MessageDetail messageDetail = messageUtils.getMessageDetail(notification);

            NotificationResponse.NotificationDto notificationDto = NotificationResponse.NotificationDto.builder()
                    .actorId(notification.getActor().getId())
                    .actorNickName(notification.getActor().getUsersDetail().getNickName())
                    .actorProfileImageUrl(notification.getActor().getUsersDetail().getProfileImage())
                    .messageType(notification.getMessageType())
                    .messageDetail(messageDetail)
                    .createdAt(notification.getCreatedAt())
                    .isRead(notification.getIsRead())
                    .build();
            notificationDtoList.add(notificationDto);
        });

        return notificationDtoList;
    }

}
