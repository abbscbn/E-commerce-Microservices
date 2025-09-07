package com.abbas.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFailedEventIdentity{
    private Long orderId;
    private OrderFailedEventIdentity.FailedItem failedItems; // ❌ başarısız olan ürünlerin listesi

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailedItem {
        private Long userId;
        private String reason;
    }

}