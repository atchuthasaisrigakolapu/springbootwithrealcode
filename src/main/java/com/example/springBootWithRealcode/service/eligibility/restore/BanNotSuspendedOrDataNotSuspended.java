package com.example.springBootWithRealcode.service.eligibility.restore;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

import java.util.Objects;

public class BanNotSuspendedOrDataNotSuspended extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_401.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_401.getValue();

    public BanNotSuspendedOrDataNotSuspended(EligibilityContext eligibilityContext){
        super(eligibilityContext,CODE,MESSAGE,true);
    }

    @Override
    public  boolean check() {
        return Objects.nonNull(eligibilityContext.getCsPtpSvcResponse().getBanStatus())
                && !("S".equalsIgnoreCase(eligibilityContext.getCsPtpSvcResponse().getBanStatus()) ||
                "O".equalsIgnoreCase(eligibilityContext.getCsPtpSvcResponse().getBanStatus())
                        && "D".equalsIgnoreCase(eligibilityContext.getCsPtpSvcResponse().getBanStatus()));
    }
}
