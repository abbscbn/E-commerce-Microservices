package com.abbas.ecommerce.common.handler;

import com.abbas.ecommerce.common.exception.AuthBaseException;
import com.abbas.ecommerce.common.exception.BaseException;
import com.abbas.ecommerce.common.response.RootResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalHandler {

    public List<String> addMapValue(List<String> stringList, String value){
        stringList.add(value);
        return stringList;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<RootResponse<Map<String, List<String>>>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest webRequest){

        Map<String,List<String>> errorMapList= new HashMap<>();

        for(ObjectError objectError: ex.getBindingResult().getAllErrors()){

            String fieldName= ((FieldError)objectError).getField();

            if(errorMapList.containsKey(fieldName)){
                errorMapList.put(fieldName,addMapValue(errorMapList.get(fieldName),objectError.getDefaultMessage()));
            }
            else{
                errorMapList.put(fieldName,addMapValue(new ArrayList<>(),objectError.getDefaultMessage()));
            }

        }


        return ResponseEntity.badRequest().body(RootResponse.error(errorMapList,webRequest));

    }

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<RootResponse<String>> BaseExceptionHandler(BaseException ex, WebRequest webRequest){
        return  ResponseEntity.badRequest().body(RootResponse.error(ex.getMessage(),webRequest));
    }

}