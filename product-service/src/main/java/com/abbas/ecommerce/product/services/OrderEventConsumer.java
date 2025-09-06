package com.abbas.ecommerce.product.services;

import com.abbas.ecommerce.common.event.OrderCompletedEvent;
import com.abbas.ecommerce.common.event.OrderCreatedEvent;
import com.abbas.ecommerce.common.event.OrderFailedEvent;
import com.abbas.ecommerce.product.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderEventConsumer {

    @Autowired
    ProductService productService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {

        System.out.println("📩 Order Created Event received in Product Service: " + event);

        List<OrderFailedEvent.FailedItem> failedItems = new ArrayList<>();

        // Önce tüm ürünleri kontrol et
        for (OrderCreatedEvent.OrderItemDto item : event.getItems()) {
            Map checkProduct = productService.checkProduct(item.getProductId(), item.getQuantity());
            /// BURADA KALDIN DEVAM ET
            if (false) {
                System.out.println(item.getProductId() + " id li ürün stokta mevcut değil");
                failedItems.add(new OrderFailedEvent.FailedItem(item.getProductId(), "Yeterli Stok Bulunmamaktadır"));
            }
        }

        // Eğer başarısız ürün varsa event gönder
        if (!failedItems.isEmpty()) {
            OrderFailedEvent failedEvent = new OrderFailedEvent(event.getOrderId(), failedItems);
            rabbitTemplate.convertAndSend(
                    RabbitConfig.ORDER_EXCHANGE,
                    RabbitConfig.ORDER_FAILED_ROUTING_KEY, // ✅ artık routing key
                    failedEvent
            );
            return;
        }

        // Tüm ürünler varsa stok düş
        event.getItems().forEach(item -> {
            System.out.println(item.getProductId() + " id li ürün " + item.getQuantity() + " adet alınabilir");
            productService.setProductStockByProductId(item.getProductId(), item.getQuantity());
        });

        // ✅ OrderCompletedEvent yayınla
        OrderCompletedEvent completedEvent = new OrderCompletedEvent(
                event.getOrderId(),
                event.getUserId(),
                event.getItems().stream().map(OrderCreatedEvent.OrderItemDto::getProductId).toList()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EXCHANGE,
                RabbitConfig.ORDER_COMPLETED_ROUTING_KEY, // yeni routing key
                completedEvent
        );

        System.out.println("✅ Sipariş başarıyla işlendi ve OrderCompletedEvent yayınlandı.");

    }
}
