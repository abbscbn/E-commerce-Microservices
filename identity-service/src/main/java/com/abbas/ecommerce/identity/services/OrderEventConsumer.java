package com.abbas.ecommerce.identity.services;

import com.abbas.ecommerce.common.event.OrderCreatedEvent;
import com.abbas.ecommerce.common.event.OrderFailedEventIdentity;
import com.abbas.ecommerce.identity.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderEventConsumer {

    @Autowired
    UserService userService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {


        System.out.println("📩 Order Created Event received in Identity Service: " + event);

        boolean result = userService.checkUserByUserId(event.getUserId());

        if (!result) {

            OrderFailedEventIdentity.FailedItem failedItem = new OrderFailedEventIdentity.FailedItem(event.getUserId(), "UserId bulunamadı: " + event.getUserId().toString());
            OrderFailedEventIdentity orderFailedEventIdentity = new OrderFailedEventIdentity(event.getOrderId(), failedItem);
            rabbitTemplate.convertAndSend(
                    RabbitConfig.ORDER_EXCHANGE,
                    RabbitConfig.ORDER_FAILED_ROUTING_KEY_IDENTITY, // ✅ artık routing key
                    orderFailedEventIdentity
            );

            return;
        }


    }


}


