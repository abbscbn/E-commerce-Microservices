package com.abbas.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFailedEvent {
    private Long orderId;
    private List<FailedItem> failedItems; // ❌ başarısız olan ürünlerin listesi

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailedItem {
        private Long productId;
        private String reason;
    }
}
