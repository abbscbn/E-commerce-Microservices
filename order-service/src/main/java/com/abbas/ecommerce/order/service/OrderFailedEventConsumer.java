package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.common.event.OrderFailedEvent;
import com.abbas.ecommerce.order.config.RabbitConfig;
import com.abbas.ecommerce.order.model.FailedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderFailedEventConsumer {

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = RabbitConfig.ORDER_FAILED_QUEUE)
    public void consumeOrderFailedEvent(OrderFailedEvent event) {

        List<FailedMessage> failedMessages= new ArrayList<>();

        System.out.println("❌ Order Failed Event received: " + event);

        event.getFailedItems().forEach(failedItem ->
//                System.out.println("Ürün ID: " + failedItem.getProductId() + " -> " + failedItem.getReason())
                failedMessages.add(FailedMessage.builder()
                                .productId(failedItem.getProductId())
                                .message(failedItem.getReason())
                        .build())
        );

        orderService.updateOrderStatus(event.getOrderId(), "CANCELED",failedMessages);

    }


}
