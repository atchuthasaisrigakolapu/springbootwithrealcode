package com.example.springBootWithRealcode.exception;


import io.netty.handler.codec.http2.Http2SecurityUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseStatusException extends RuntimeException{

    private String errorCode;
    private HttpStatus httpStatus;
    public ResponseStatusException(){}
    public ResponseStatusException(String message){
        super(message);
    }
    public ResponseStatusException(String errorCode,String message){
        super(message);
        this.errorCode = errorCode;
    }
    public ResponseStatusException(HttpStatus httpStatus,String message,String errorCode){
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }


}
