package com.example.springBootWithRealcode.exception;

import lombok.Value;

@Value
public class InternalServerException extends RuntimeException{

    String errorCode;
    public InternalServerException(String errorCode,String message){
        super(message);
        this.errorCode =errorCode;
    }
}
