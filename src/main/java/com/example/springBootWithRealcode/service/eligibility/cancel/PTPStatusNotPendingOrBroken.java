package com.example.springBootWithRealcode.service.eligibility.cancel;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

import java.util.Objects;

public class PTPStatusNotPendingOrBroken extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_301.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_301.getValue();

    public PTPStatusNotPendingOrBroken(EligibilityContext eligibilityContext){
        super(eligibilityContext,CODE,MESSAGE,true);
    }
    @Override
    public  boolean check() {
        return Objects.nonNull(eligibilityContext.getCsPtpSvcResponse().getPtpInfo())
                && !("P".equalsIgnoreCase(eligibilityContext.getCsPtpSvcResponse().getPtpInfo().getPtpOldPtp()) ||
                "B".equalsIgnoreCase(eligibilityContext.getCsPtpSvcResponse().getPtpInfo().getPtpOldPtp()));
    }
}
