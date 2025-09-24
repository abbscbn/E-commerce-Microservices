package com.abbas.ecommerce.order.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "order_basket_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBasketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId; // product servisten gelen product id
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    private OrderBasket basket;

    // getter, setter
}
