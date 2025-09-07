package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.common.event.OrderFailedEvent;
import com.abbas.ecommerce.common.event.OrderFailedEventIdentity;
import com.abbas.ecommerce.order.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderFailedEventIdentityConsumer {

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = RabbitConfig.ORDER_FAILED_QUEUE_IDENTITY)
    public void consumeOrderFailedEvent(OrderFailedEventIdentity event) {

        System.out.println("Identityden gelen event yakalndı: "+event);



    }

}
