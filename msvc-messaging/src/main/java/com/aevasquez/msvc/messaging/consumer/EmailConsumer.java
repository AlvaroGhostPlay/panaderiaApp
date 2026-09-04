package com.aevasquez.msvc.messaging.consumer;

import com.aevasquez.msvc.messaging.dto.EmailRequest;
import com.aevasquez.msvc.messaging.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final NotificationService emailService;

    @RabbitListener(queues = "${messaging.notification.queue}")
    public void consume(EmailRequest message) {
        emailService.sendRealEmail(message);
    }
}