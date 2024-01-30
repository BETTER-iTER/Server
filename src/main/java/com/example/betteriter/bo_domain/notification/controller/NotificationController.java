package com.example.betteriter.bo_domain.notification.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/notification")
@RequiredArgsConstructor
@Tag(name = "NotificationController", description = "Notification API")
public class NotificationController {
}
