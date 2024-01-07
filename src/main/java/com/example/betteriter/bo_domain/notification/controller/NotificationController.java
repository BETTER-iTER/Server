package com.example.betteriter.bo_domain.notification.controller;

import com.example.betteriter.bo_domain.notification.converter.NotificationResponseConverter;
import com.example.betteriter.bo_domain.notification.dto.NotificationRequest;
import com.example.betteriter.bo_domain.notification.dto.NotificationResponse;
import com.example.betteriter.bo_domain.notification.service.NotificationService;
import com.example.betteriter.global.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "NotificationControllers", description = "Notification API")
@Slf4j
@RequestMapping("/notification")
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 알림 수동 등록
     */
    @PostMapping("")
    public ResponseDto<NotificationResponse.CreateNotificationDto> createNotification(
            @RequestBody NotificationRequest.CreateNotificationDto request
    ) {
        notificationService.createNotification(request);
        return ResponseDto.onSuccess(NotificationResponseConverter.toCreateNotificationDto(request));
    }
}
