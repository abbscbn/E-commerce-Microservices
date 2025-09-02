package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.common.event.OrderFailedEvent;
import com.abbas.ecommerce.order.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderFailedEventConsumer {

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = RabbitConfig.ORDER_FAILED_QUEUE)
    public void consumeOrderFailedEvent(OrderFailedEvent event) {
        System.out.println("❌ Order Failed Event received: " + event);
        event.getFailedItems().forEach(failedItem ->
                System.out.println("Ürün ID: " + failedItem.getProductId() + " -> " + failedItem.getReason())
        );
        orderService.updateOrderStatus(event.getOrderId(), "CANCELED");
    }


}
