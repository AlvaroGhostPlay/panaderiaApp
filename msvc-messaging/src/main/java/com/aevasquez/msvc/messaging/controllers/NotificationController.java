package com.aevasquez.msvc.messaging.controllers;

import com.aevasquez.msvc.messaging.dto.EmailRequest;
import com.aevasquez.msvc.messaging.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/welcome")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailRequest request) {
        notificationService.sendEmail(request);
        return ResponseEntity.accepted().build();
    }
}
