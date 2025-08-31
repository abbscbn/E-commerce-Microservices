package com.abbas.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent{
    private Long orderId;
    private Long userId;
    private List<OrderItemDto> items; // productId, quantity

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDto{
        private Long productId;
        private Integer quantity;
    }
}