package com.abbas.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseProduct {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private Integer stock;
    // ProductImage DTO
    private ResponseProductImage image;
}
