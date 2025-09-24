package com.abbas.ecommerce.order.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_basket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBasket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // identity servisten gelen user id

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderBasketItem> items = new ArrayList<>();

    // getter, setter
}
