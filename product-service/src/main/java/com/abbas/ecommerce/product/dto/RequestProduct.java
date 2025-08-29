package com.abbas.ecommerce.product.dto;

public record RequestProduct(

         String name,

         Double price,

         String description,

         Integer stock
) {
}
