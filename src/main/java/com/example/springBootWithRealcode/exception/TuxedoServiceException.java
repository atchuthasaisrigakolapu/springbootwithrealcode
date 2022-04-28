package com.example.springBootWithRealcode.exception;

public class TuxedoServiceException extends RuntimeException{

    String errorCode;
    public TuxedoServiceException(String message){
        super(message);
    }
    public TuxedoServiceException(String errorCode,String message){
        super(message);
        this.errorCode = errorCode;
    }

}
