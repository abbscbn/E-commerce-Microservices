package com.abbas.ecommerce.product.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckProductModel {
    private boolean check;
    private String desc;
}
