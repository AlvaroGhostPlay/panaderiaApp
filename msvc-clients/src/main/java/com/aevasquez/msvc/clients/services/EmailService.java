package com.aevasquez.msvc.clients.services;

import com.aevasquez.msvc.clients.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-messaging")
public interface EmailService {

    @PostMapping("/notifications/welcome")
    void sendEmailWelcome(@RequestBody EmailRequest emailRequest);
}
