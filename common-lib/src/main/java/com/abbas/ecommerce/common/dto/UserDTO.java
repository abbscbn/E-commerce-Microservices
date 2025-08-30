package com.abbas.ecommerce.common.dto;

public record UserDTO(
        Long id,
        String username,
        String email
) {}