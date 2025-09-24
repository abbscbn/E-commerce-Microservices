package com.abbas.ecommerce.order.repository;

import com.abbas.ecommerce.order.model.OrderBasket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBasketRepository extends JpaRepository<OrderBasket,Long> {
}
