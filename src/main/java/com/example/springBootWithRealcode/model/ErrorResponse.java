package com.example.springBootWithRealcode.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse implements Serializable {

    private String errorCode;
    private List<String> errors;
    private String serviceName;

}
