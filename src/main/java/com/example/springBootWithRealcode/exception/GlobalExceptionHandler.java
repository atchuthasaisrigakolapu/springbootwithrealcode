package com.example.springBootWithRealcode.exception;


import com.example.springBootWithRealcode.constant.Constants;
import com.example.springBootWithRealcode.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    @Autowired
    BuildProperties buildProperties;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exeception(Exception exception){
        return new ErrorResponse(Constants.ErrorCodes.TUXEDO_ERROR, List.of("Tech error"),buildProperties.getArtifact());
    }
    @ExceptionHandler(TuxedoServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse exeception(TuxedoServiceException exception){
        return new ErrorResponse("Un processing entity", List.of("Tech error"),buildProperties.getArtifact());
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> exeception(ResponseStatusException exception){
        var tech_error = Optional.ofNullable(exception.getErrorCode()).orElse("Tech error");
        var tech_message = Optional.ofNullable(exception.getMessage()).orElse("Tech error message");
        var httpStatus = exception.getHttpStatus();
        if(Objects.isNull(httpStatus)){
            tech_error =  "Un processing entity";
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        }
        return new ResponseEntity<>(new ErrorResponse(tech_error ,List.of(tech_message),buildProperties.getArtifact()),httpStatus);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationException(ConstraintViolationException exception){
        var collect = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        log.error(exception.getMessage(),collect,buildProperties.getArtifact());
        return new ErrorResponse("Bad Request" ,collect,buildProperties.getArtifact());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException exception){
        var collect = exception.getBindingResult().getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getField()+"-"+fieldError.getDefaultMessage())
                                .collect(Collectors.toList());
        log.error(exception.getMessage(),collect,buildProperties.getArtifact());
        return new ErrorResponse("Bad Request" ,collect,buildProperties.getArtifact());
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse missingServletRequestParameterException(MissingServletRequestParameterException exception){
        var s = Optional.ofNullable(exception.getMessage()).orElse("");
        log.error(exception.getMessage(),s);
        return new ErrorResponse("Bad Request" ,List.of(s),buildProperties.getArtifact());
    }
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse missingRequestHeaderException(MissingRequestHeaderException exception){
        var s = Optional.ofNullable(exception.getMessage()).orElse("");
        log.error(exception.getMessage(),s);
        return new ErrorResponse("Bad Request" ,List.of(s),buildProperties.getArtifact());
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumen(MethodArgumentTypeMismatchException exception){
        var errorMessage = "Invalid/value/input Found:/validated/value".replace("/value/",exception.getMessage()
                .replace("/validated/value",(CharSequence) Objects.requireNonNull(exception.getValue()
        )));
        if("dataSource".equalsIgnoreCase(exception.getName())){
            errorMessage  = errorMessage.concat("Expected true/false");
        }
        log.error(exception.getMessage(),errorMessage);
        return new ErrorResponse("Bad Request" ,List.of(errorMessage),buildProperties.getArtifact());
    }

}
