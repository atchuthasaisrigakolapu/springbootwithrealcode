package com.example.springBootWithRealcode.service.eligibility.create;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

import java.util.Objects;

public class PTPPendingStatus extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_102.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_102.getValue();

    public PTPPendingStatus(EligibilityContext eligibilityContext){
        super(eligibilityContext,CODE,MESSAGE,true);
    }

    @Override
    protected boolean check() {
        return Objects.nonNull(eligibilityContext.getCsPtpSvcResponse().getPtpInfo())
                &&("P".equalsIgnoreCase(eligibilityContext.getCsPtpSvcResponse().getPtpInfo().getPtpStatus()));
    }
}
