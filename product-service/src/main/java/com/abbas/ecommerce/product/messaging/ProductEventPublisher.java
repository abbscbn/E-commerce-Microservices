package com.abbas.ecommerce.product.messaging;

import com.abbas.ecommerce.common.event.ProductValidationSucceededEvent;
import com.abbas.ecommerce.common.event.ProductValidationFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String ORDER_EVENTS_EXCHANGE = "order.events";

    public void publishValidationSucceeded(ProductValidationSucceededEvent event) {
        System.out.println("GÖNDERİLECEK EVENT: "+event);
        rabbitTemplate.convertAndSend(
                ORDER_EVENTS_EXCHANGE,
                "order.product.validation.succeeded",
                event
        );
    }

    public void publishValidationFailed(ProductValidationFailedEvent event) {
        System.out.println("GÖNDERİLECEK EVENT: "+event);
        rabbitTemplate.convertAndSend(
                ORDER_EVENTS_EXCHANGE,
                "order.product.validation.failed",
                event
        );
    }
}
