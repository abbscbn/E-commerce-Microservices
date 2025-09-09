package com.abbas.ecommerce.order.domain;

import lombok.*;
import java.util.List;

@Getter @AllArgsConstructor
public class OrderCreatedDomainEvent {
    private final Long orderId;
    private final Long userId;
    private final List<Item> items;

    @Getter @AllArgsConstructor
    public static class Item {
        private final Long productId;
        private final int quantity;
    }
}