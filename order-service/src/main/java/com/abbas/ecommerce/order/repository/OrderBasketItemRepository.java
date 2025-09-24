package com.abbas.ecommerce.order.repository;

import com.abbas.ecommerce.order.model.OrderBasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBasketItemRepository extends JpaRepository<OrderBasketItem,Long> {
}
