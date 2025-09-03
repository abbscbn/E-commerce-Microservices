package com.abbas.ecommerce.common.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthBaseException extends AuthenticationException {


    public AuthBaseException(ErrorMessage errorMessage){
        super(errorMessage.preparedErrorMessage());
    }

}
