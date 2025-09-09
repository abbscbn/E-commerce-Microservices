package com.abbas.ecommerce.order.messaging;

import com.abbas.ecommerce.common.event.ProductValidationEvent;
import com.abbas.ecommerce.common.event.UserValidationEvent;
import com.abbas.ecommerce.order.domain.OrderCreatedDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.abbas.ecommerce.order.config.RabbitConfig.*;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishUserValidation(Long orderId, Long userId) {
        UserValidationEvent evt = new UserValidationEvent(orderId, userId);

        rabbitTemplate.convertAndSend(ORDER_EVENTS_EXCHANGE, RK_USER_VALIDATE, evt);
    }

    public void publishStockCheck(Long orderId, List<OrderCreatedDomainEvent.Item> items) {
        List<Long> productIds= new ArrayList<>();
        items.forEach(item -> productIds.add(item.getProductId()));
        List<ProductValidationEvent.Item> productValidationEventItems= new ArrayList<>();

        items.forEach(item -> productValidationEventItems.add(new ProductValidationEvent.Item(item.getProductId(), item.getQuantity())));
        ProductValidationEvent evt =new ProductValidationEvent(orderId,productValidationEventItems);

        rabbitTemplate.convertAndSend(ORDER_EVENTS_EXCHANGE,RK_PRODUCT_VALIDATE, evt);
    }
}
