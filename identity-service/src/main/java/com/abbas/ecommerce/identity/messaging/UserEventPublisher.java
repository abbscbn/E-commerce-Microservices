package com.abbas.ecommerce.identity.messaging;

import com.abbas.ecommerce.common.event.UserValidationSucceededEvent;
import com.abbas.ecommerce.common.event.UserValidationFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private static final String ORDER_EVENTS_EXCHANGE = "order.events";

    public void publishUserValidationSucceeded(UserValidationSucceededEvent event) {
        System.out.println("ORDER A GÖNDERİLECEK BAŞARILI EVENT: "+event);
        rabbitTemplate.convertAndSend(
                ORDER_EVENTS_EXCHANGE,
                "order.user.validation.succeeded",
                event
        );
    }

    public void publishUserValidationFailed(UserValidationFailedEvent event) {
        System.out.println("ORDER A GÖNDERİLECEK BAŞARISIZ EVENT: "+event);
        rabbitTemplate.convertAndSend(
                ORDER_EVENTS_EXCHANGE,
                "order.user.validation.failed",
                event
        );
    }
}
