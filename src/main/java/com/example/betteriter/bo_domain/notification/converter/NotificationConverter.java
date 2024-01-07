package com.example.betteriter.bo_domain.notification.converter;

import com.example.betteriter.bo_domain.notification.domain.MessageJson;
import com.example.betteriter.bo_domain.notification.domain.MessageType;
import com.example.betteriter.bo_domain.notification.domain.Notification;
import com.example.betteriter.fo_domain.user.domain.Users;

public class NotificationConverter {

    public static Notification toNotification(
        Users actor, Users receiver,
        MessageType messageType,
        Long contentId, String title, String content
    ) {
        return Notification.builder()
                .actor(actor)
                .actorName(actor.getUsersDetail().getNickName())
                .actorType(actor.getRoleType())
                .receiver(receiver)
                .messageType(messageType)
                .messageJson(MessageJson.of(contentId, title, content))
                .build();
    }
}
