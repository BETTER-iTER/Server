package com.example.betteriter.bo_domain.notification.service;

import com.example.betteriter.bo_domain.notification.converter.NotificationConverter;
import com.example.betteriter.bo_domain.notification.domain.Notification;
import com.example.betteriter.bo_domain.notification.dto.NotificationRequest;
import com.example.betteriter.bo_domain.notification.repository.NotificationRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final UserService userService;

    private final NotificationRepository notificationRepository;

    public void createNotification(NotificationRequest.CreateNotificationDto request) {
        log.info("createNotification request: {}", request);

        Users actor = userService.getUserById(request.getActorId());
        Users receiver = userService.getUserById(request.getReceiverId());

        Notification newNotification = NotificationConverter.toNotification(
                actor,
                receiver,
                request.getMessageType(),
                request.getMessageDetail().getContentId(),
                request.getMessageDetail().getTitle(),
                request.getMessageDetail().getContent()
        );

        notificationRepository.save(newNotification);
    }
}
