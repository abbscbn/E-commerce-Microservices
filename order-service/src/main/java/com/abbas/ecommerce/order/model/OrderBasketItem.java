package com.abbas.ecommerce.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Double price;
    private String name;
    private String productImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    @JsonBackReference
    private OrderBasket basket;

    // getter, setter
}
