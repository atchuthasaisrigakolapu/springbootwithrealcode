package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Value
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationInfo {

    Integer numberOfInstallements;
    LocalDate firstEstimatedPostingDate;
    LocalDate lastEstimatedPostingDate;
    List<PTPItems> ptpItems;
    double totalInstallmentAmount;
    String ptpRisk;
    Integer recordAMaxInstallationments;
    LocalDate recordAMax1stInstallationDate;
    LocalDate recordAMaxLastInstallationDate;
    Integer recordBMaxInstallationments;
    LocalDate recordBMax1stInstallationDate;
    LocalDate recordBMaxLastInstallationDate;


    String ptpCategory;
    LocalDate extendDate;
    PTPOperationEnum operation;

    boolean restoreEligible;
    boolean extendEligible;

    String ptpNCatRiskAcc;


}
