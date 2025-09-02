package com.abbas.ecommerce.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private ErrorMessageType errorMessageType;

    private String ofStatic;

    public String preparedErrorMessage(){
        StringBuilder stringBuilder= new StringBuilder();
        stringBuilder.append("CODE: "+errorMessageType.getCode()+" DESCRIPTION: "+errorMessageType.getDesc());
        stringBuilder.append(" "+ofStatic);
        return stringBuilder.toString();
    }
}
