package com.abbas.ecommerce.order.messaging;

import com.abbas.ecommerce.order.domain.OrderCreatedDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderCreatedDomainEventHandler {

    private final OrderEventPublisher publisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedDomainEvent e) {
        // 1) User doğrulama eventi
        publisher.publishUserValidation(e.getOrderId(), e.getUserId());

        // 2) Ürün stok eventleri (gerekirse aynı productId’leri toplayabilirsin)

        publisher.publishStockCheck(e.getOrderId(), e.getItems());

    }
}
