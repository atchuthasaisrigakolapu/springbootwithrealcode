package com.example.springBootWithRealcode.exception;

import lombok.Value;

@Value
public class TuxedoPreConditionException extends RuntimeException {

    String errorCode;
    public TuxedoPreConditionException(String errorCode,String message){
        super(message);
        this.errorCode = errorCode;
    }

}
