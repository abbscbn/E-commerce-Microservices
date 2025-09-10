package com.abbas.ecommerce.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRollbackDomainEvent {
    private Long orderId;
}
