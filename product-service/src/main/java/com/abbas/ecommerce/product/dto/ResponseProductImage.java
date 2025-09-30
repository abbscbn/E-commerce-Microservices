package com.abbas.ecommerce.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseProductImage {
    private String desktopUrl;
    private String tabletUrl;
    private String mobileUrl;
    private boolean isMain;
}
