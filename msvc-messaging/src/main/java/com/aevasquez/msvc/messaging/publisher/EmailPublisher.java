package com.aevasquez.msvc.messaging.publisher;

import com.aevasquez.msvc.messaging.config.MessagingProperties;
import com.aevasquez.msvc.messaging.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties properties;

    public void publish(EmailRequest message) {

        rabbitTemplate.convertAndSend(
                properties.getExchange().getName(),
                properties.getNotification().getRoutingKey(),
                message
        );
    }
}
