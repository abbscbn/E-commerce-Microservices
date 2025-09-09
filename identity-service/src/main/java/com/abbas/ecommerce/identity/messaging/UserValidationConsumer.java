package com.abbas.ecommerce.identity.messaging;

import com.abbas.ecommerce.common.event.UserValidationEvent;
import com.abbas.ecommerce.common.event.UserValidationSucceededEvent;
import com.abbas.ecommerce.common.event.UserValidationFailedEvent;
import com.abbas.ecommerce.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidationConsumer {

    private final UserRepository userRepository;
    private final UserEventPublisher publisher;

    // Kuyruğun ismi Order-service tarafında binding yapılırken belirlenmiş olmalı
    @RabbitListener(queues = "identity.user.validate.q")
    public void consume(UserValidationEvent event) {
        System.out.println("ORDERDAN GELEN EVENT: "+event);
        boolean exists = userRepository.existsById(event.getUserId());

        if (exists) {
            publisher.publishUserValidationSucceeded(
                    new UserValidationSucceededEvent(event.getOrderId(), event.getUserId())
            );
        } else {
            publisher.publishUserValidationFailed(
                    new UserValidationFailedEvent(event.getOrderId(), event.getUserId(), "User not found")
            );
        }
    }
}
