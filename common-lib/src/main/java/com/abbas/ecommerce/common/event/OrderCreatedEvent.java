package com.abbas.ecommerce.common.event;

import com.abbas.ecommerce.common.dto.OrderDTO;

public record OrderCreatedEvent(OrderDTO order) {}