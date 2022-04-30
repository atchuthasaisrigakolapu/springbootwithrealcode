package com.example.springBootWithRealcode.exception;

import lombok.Value;

@Value
public class ServiceUnavailableException extends RuntimeException{

    String errorCode;
    public ServiceUnavailableException(String errorCode,String message){
        super(message);
        this.errorCode =errorCode;
    }
}
