package com.abbas.ecommerce.identity.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "kullanıcı adı boş olamaz")
        @Size(min = 3,max = 30, message = "kullanıcı adı min 3 karaker max 30 karakter olabilir")
        String username,
        @NotBlank(message = "email boş olamaz")
        @Email(message = "email formatında olmalıdır")
        String email,
        @NotBlank(message = "şifre boş olamaz")
        @Size(min = 6, message = "şifre en az 6 karakterli olmalıdır")
        String password
) {
}
