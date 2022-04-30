package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PTPEligibilityHistoryInfo {


    String ptpStatus;

    String ptpSeqNo;

    String ptpOldPtp;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    LocalDate  ptpDate;

    String ptpRestore;

    String ptpCategory;

    String ptpRegion;

    String ptpArBalance;

    String ptpOverDueBalance;

    String ptpBanStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    LocalDate ptpLastPaymentDate;

    Double ptpLastPaymentAmount;

    Integer ptpDebtAge;

    Double ptpBalanceOldestDebt;

    Double ptpCla;

    Double ptpTotalBilledUnbilled;

    List<PTPEligibilityInstallmentInfo> ptpInstallments;

}
