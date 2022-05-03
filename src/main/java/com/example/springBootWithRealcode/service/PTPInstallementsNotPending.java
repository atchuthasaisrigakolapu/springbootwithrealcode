package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseValidation;

public class PTPInstallementsNotPending extends BaseValidation {

    public static final String CODE  = PTPResponseCodes.PTP_VALIDATION_201.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_VALIDATION_201.getValue();
    public PTPInstallementsNotPending(ValidationContext validationContext) {
        super(validationContext,CODE,MESSAGE,false,null);
    }

    @Override
    protected boolean check() {
        return validationContext.getValidationInfo().getPtpItems().stream()
                .anyMatch(ptpItems -> validationContext.getPtpEligibilityPersistanceRecord().getPtpEligbilityDetails().getPtpInfo().getPtpInstallments().stream()
                        .anyMatch(ptpHistoryInstallement->String.valueOf(
                                ptpHistoryInstallement.getPtpIItem()).equalsIgnoreCase(ptpItems.getItem())&& !"P".equalsIgnoreCase(ptpHistoryInstallement.getPtpIStatus())
                        )
                );
    }
}
