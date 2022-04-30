package com.example.springBootWithRealcode.exception;

import lombok.Value;

@Value
public class ResourceNotFound extends RuntimeException{

    String errorCode;
    public ResourceNotFound(String errorCode,String message){
        super(message);
    this.errorCode =errorCode;
    }


}
