package com.abbas.ecommerce.common.event;

import com.abbas.ecommerce.common.dto.ProductDTO;

public record ProductUpdatedEvent(ProductDTO product) {}
