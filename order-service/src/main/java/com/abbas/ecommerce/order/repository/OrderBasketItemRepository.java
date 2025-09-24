package com.abbas.ecommerce.order.repository;

import com.abbas.ecommerce.order.model.OrderBasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBasketItemRepository extends JpaRepository<OrderBasketItem,Long> {
}
