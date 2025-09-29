package com.abbas.ecommerce.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long id;

    private String username;

    private String email;

    private String token;

    private Set<String> roles;

}
