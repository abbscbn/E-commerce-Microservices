package com.abbas.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockCheckFailedEvent {
    private Long orderId;
    private Long productId;
    private String reason;
}
