package com.abbas.ecommerce.product.services;

import com.abbas.ecommerce.common.event.OrderCreatedEvent;
import com.abbas.ecommerce.product.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        System.out.println("📩 Order Created Event received in Product Service: " + event);

        // Burada stok azaltma gibi işlemler yapılabilir
        event.getItems().forEach(item -> {
            System.out.println("   -> ProductId: " + item.getProductId() + ", Quantity: " + item.getQuantity());
            // TODO: product stok tablosunda azaltma işlemi
        });
    }
}
