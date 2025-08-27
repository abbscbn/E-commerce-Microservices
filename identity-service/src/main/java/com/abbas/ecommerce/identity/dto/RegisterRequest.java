package com.abbas.ecommerce.identity.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
