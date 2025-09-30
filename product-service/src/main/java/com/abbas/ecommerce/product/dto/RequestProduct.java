package com.abbas.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.multipart.MultipartFile;

public record RequestProduct(

        @NotBlank(message = "ürün ismi alanı boş olamaz")
        String name,
        @NotNull(message = "fiyat kısmı boş olamaz")
        @Positive(message = "fiyat alanı pozitif bir sayı olmalıdır")
        Double price,
        @NotBlank(message = "açıklama kısmı boş olamaz")
        String description,
        @NotNull(message = "stok sayısı null olamaz")
        @PositiveOrZero(message = "stok sayısı en az 0 olabilir")
        Integer stock,

        MultipartFile desktopImage,  // frontend’den resim dosyası gelir
        MultipartFile tabletImage,
        MultipartFile mobileImage
) {
}
