package com.abbas.ecommerce.order.repository;

import com.abbas.ecommerce.order.model.OrderBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderBasketRepository extends JpaRepository<OrderBasket,Long> {

    Optional<OrderBasket> findByUserId(Long userId);
}
