package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.PTPResponseCodes;

public class CLMSuspendedAccount extends BaseEligibility{

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_602.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_602.getValue();

    public CLMSuspendedAccount(EligibilityContext eligibilityContext){
        super(eligibilityContext,CODE,MESSAGE,true);
    }

    @Override
    public boolean check() {
        return "S".equalsIgnoreCase(eligibilityContext.getAccounInfoResponse().getBanStaus())
                && "CLM".equalsIgnoreCase(eligibilityContext.getAccounInfoResponse().getStatusActRsnCode());
    }
}
