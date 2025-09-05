package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.common.event.OrderCompletedEvent;
import com.abbas.ecommerce.order.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCompletedEventConsumer {

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = RabbitConfig.ORDER_COMPLETED_QUEUE)
    public void consumeOrderCompletedEvent(OrderCompletedEvent event) {
        System.out.println("✅ Order Completed Event received: " + event);
        orderService.updateOrderStatus(event.getOrderId(), "COMPLETED",null);
    }
}
