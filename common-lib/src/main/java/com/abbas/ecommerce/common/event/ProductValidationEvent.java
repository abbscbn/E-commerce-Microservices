package com.abbas.ecommerce.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductValidationEvent implements Serializable {

    private Long orderId;              // hangi sipariş için kontrol yapıldığını anlamak için
    private List<Item> items;  // siparişteki ürünler

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item implements Serializable {
        private Long productId;
        private int quantity;

    }
}
