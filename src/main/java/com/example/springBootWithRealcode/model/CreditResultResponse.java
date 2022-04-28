package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class CreditResultResponse {

    @JsonIgnore
    String clmTag;
    @JsonIgnore
    String riskClass;
    Double cla;
    @JsonIgnore
    String creditClass;
    @JsonIgnore
    boolean success;
}
