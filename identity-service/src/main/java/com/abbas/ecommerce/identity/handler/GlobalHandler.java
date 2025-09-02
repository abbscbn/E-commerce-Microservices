package com.abbas.ecommerce.identity.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    public ResponseEntity<Map<String, List<String>>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){

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





        return ResponseEntity.badRequest().body(errorMapList);

    }


}
