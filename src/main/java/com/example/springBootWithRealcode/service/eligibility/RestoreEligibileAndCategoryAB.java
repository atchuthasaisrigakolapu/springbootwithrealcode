package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.ValidationContext;

public class RestoreEligibileAndCategoryAB extends BaseValidation {
    public static final String CODE  = PTPResponseCodes.PTP_VALIDATION_101.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_VALIDATION_101.getValue();
    public RestoreEligibileAndCategoryAB(ValidationContext validationContext) {
        super(validationContext,CODE,MESSAGE,false,null);
    }

    @Override
    protected boolean check() {
        return validationContext.getValidationInfo().isRestoreEligible() && "A".equalsIgnoreCase(validationContext.getValidationInfo().getPtpCategory())
                && "B".equalsIgnoreCase(validationContext.getValidationInfo().getPtpCategory());
    }
}
