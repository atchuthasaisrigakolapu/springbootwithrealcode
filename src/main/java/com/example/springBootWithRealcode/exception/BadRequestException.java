package com.example.springBootWithRealcode.exception;

import lombok.Value;

@Value
public class BadRequestException extends RuntimeException{
    String errorCode;
    public BadRequestException(String errorCode,String message){
        super(message);
        this.errorCode =errorCode;
    }
}
