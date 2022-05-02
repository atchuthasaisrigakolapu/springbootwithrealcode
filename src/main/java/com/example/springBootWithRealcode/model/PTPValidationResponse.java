package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Value
@Jacksonized
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PTPValidationResponse {

    String refId;
    String validationFlag;
    List<ValidationConstraints> validationConstraints;

    String ptpCategory;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    LocalDate extendDate;

    Double pastDueAmt;
}
