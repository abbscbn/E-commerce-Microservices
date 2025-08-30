package com.abbas.ecommerce.common.dto;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock
) {
}
