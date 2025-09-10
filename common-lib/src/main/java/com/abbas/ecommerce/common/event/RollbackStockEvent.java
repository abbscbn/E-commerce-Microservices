package com.abbas.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RollbackStockEvent {
    private Long orderId;
    private List<OrderItemDto> items;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItemDto {
        private Long productId;
        private Integer quantity;
    }

}


