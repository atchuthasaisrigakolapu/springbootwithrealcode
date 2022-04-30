package com.example.springBootWithRealcode.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PTPEligibilityInstallmentInfo {

    String ptpISeqNo;

    Integer ptpIItem;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    LocalDate ptpIDatePromised;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    LocalDate ptpIDateEstimatedPosting;

    String ptpIMethod;

    Double ptpIAmount;

    String ptpIStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    LocalDate ptpIDateFulfilled;

    Double ptpIAmountFulfilled;

    String ptpIPaymentSequence;




}
