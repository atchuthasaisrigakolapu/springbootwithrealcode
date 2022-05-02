package com.example.springBootWithRealcode.service.eligibility.create;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

public class IPPIndicatorY extends BaseEligibility {

    public static final String CODE = PTPResponseCodes.PTP_ELIGIBILITY_101.name();
    public static final String MESSAGE = PTPResponseCodes.PTP_ELIGIBILITY_101.getValue();

    public IPPIndicatorY(EligibilityContext eligibilityContext) {
        super(eligibilityContext, CODE, MESSAGE, true);
    }

    @Override
    public boolean check() {
        return "Y".equalsIgnoreCase(eligibilityContext.getCsPtpSvcResponse().getIppInd());
    }
}
