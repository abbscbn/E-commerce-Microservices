package com.abbas.ecommerce.common.exception;

import java.io.Serializable;


public class BaseException extends RuntimeException implements Serializable {


   public BaseException(ErrorMessage errorMessage){
        super(errorMessage.preparedErrorMessage());
    }

}
