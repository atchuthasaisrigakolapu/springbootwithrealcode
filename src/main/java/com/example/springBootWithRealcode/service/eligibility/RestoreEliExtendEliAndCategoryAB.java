package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.ValidationContext;

public class RestoreEliExtendEliAndCategoryAB extends BaseValidation {
    public static final String CODE  = PTPResponseCodes.PTP_VALIDATION_102.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_VALIDATION_102.getValue();
    public RestoreEliExtendEliAndCategoryAB(ValidationContext validationContext) {
        super(validationContext,CODE,MESSAGE,false,null);
    }

    @Override
    protected boolean check() {
        return !validationContext.getValidationInfo().isRestoreEligible()
                && validationContext.getValidationInfo().isExtendEligible()
                && "A".equalsIgnoreCase(validationContext.getValidationInfo().getPtpCategory())
                && "B".equalsIgnoreCase(validationContext.getValidationInfo().getPtpCategory());


    }
}
