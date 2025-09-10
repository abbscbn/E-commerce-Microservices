package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.common.event.RollbackStockEvent;
import com.abbas.ecommerce.order.model.Order;
import com.abbas.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderSagaService {

    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository orderRepository;

    @Transactional
    public void finalizeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.isUserValidated() && order.isProductValidated()) {
            order.setStatus("COMPLETED");
        } else {
            order.setStatus("CANCELLED");

            // Rollback event publish
            RollbackStockEvent rollbackEvent = RollbackStockEvent.builder()
                    .orderId(order.getId())
                    .items(order.getItems().stream()
                            .map(item -> RollbackStockEvent.OrderItemDto.builder()
                                    .productId(item.getProductId())
                                    .quantity(item.getQuantity())
                                    .build())
                            .toList())
                    .build();

            rabbitTemplate.convertAndSend("rollback.stock.exchange", "rollback.stock", rollbackEvent);
        }

        orderRepository.save(order);
    }
}