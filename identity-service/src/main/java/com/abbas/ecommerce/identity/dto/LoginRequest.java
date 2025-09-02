package com.abbas.ecommerce.identity.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "kullanıcı adı boş olamaz")
        String username,
        @NotBlank(message = "şifre boş olamaz")
        String password
) {
}
