package com.abbas.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductValidationFailedEvent implements Serializable {

    private Long orderId;
    private List<FailedItem> failedItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailedItem implements Serializable {
        private Long productId;
        private String reason; // örn: "STOK YETERSİZ MEVCUT MİKTAR: 5"
    }
}