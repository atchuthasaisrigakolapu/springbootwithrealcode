package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import java.util.List;

@Value
@Builder
@Jacksonized
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CreditResultOutBoundResponse {

    @Valid
    List<ErrorContent> errors;
    CreditResultContent content;
    String service;
    String operation;
}
