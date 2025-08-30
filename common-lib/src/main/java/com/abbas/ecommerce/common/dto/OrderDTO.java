package com.abbas.ecommerce.common.dto;

import java.util.List;

public record OrderDTO(
        Long orderId,
        Long userId,
        List<ProductDTO> products,
        Double totalAmount
) {}
