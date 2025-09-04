package com.abbas.ecommerce.common.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

import java.io.Serializable;

@Getter
public class AuthBaseException extends AuthenticationException implements Serializable {

    private final ErrorMessage errorMessage;

    public AuthBaseException(ErrorMessage errorMessage){
        super(errorMessage.preparedErrorMessage());
        this.errorMessage = errorMessage;
    }

}
