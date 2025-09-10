package com.abbas.ecommerce.order.messaging;

import com.abbas.ecommerce.common.event.RollbackStockEvent;
import com.abbas.ecommerce.order.config.RabbitConfig;
import com.abbas.ecommerce.order.domain.OrderRollbackDomainEvent;
import com.abbas.ecommerce.order.model.Order;
import com.abbas.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRollbackEventHandler {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @EventListener
    public void handle(OrderRollbackDomainEvent domainEvent) {
        Order order = orderRepository.findById(domainEvent.getOrderId())
                .orElseThrow();

        RollbackStockEvent rollbackEvent = RollbackStockEvent.builder()
                .orderId(order.getId())
                .items(order.getItems().stream()
                        .map(it -> RollbackStockEvent.OrderItemDto.builder()
                                .productId(it.getProductId())
                                .quantity(it.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitConfig.ORDER_EVENTS_EXCHANGE,
                "rollback.stock",
                rollbackEvent
        );
    }
}
