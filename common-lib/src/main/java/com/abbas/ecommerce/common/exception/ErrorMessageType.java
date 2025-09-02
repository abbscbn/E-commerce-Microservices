package com.abbas.ecommerce.common.exception;

import lombok.Getter;

@Getter
public enum ErrorMessageType {

    GENERAL_EXCEPTION(1001,"GENEL HATA"),
    USERNAME_ALREADY_EXIST(1002,"KULLANICI ÇOKTAN KAYITLI"),
    EMAIL_ALREADY_EXIST(1003,"EMAİL ÇOKTAN KAYITLI"),
    USER_NOT_FOUND(1004,"KULLANICI BULUNAMADI"),
    USER_OR_PASSWORD_INCORECT(1005,"KULLANICI ADI VEYA ŞİFRE HATALI"),
    TOKEN_IS_NOT_VALID(1006,"GEÇERSİZ TOKEN");

    private Integer code;

    private String desc;

    ErrorMessageType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
