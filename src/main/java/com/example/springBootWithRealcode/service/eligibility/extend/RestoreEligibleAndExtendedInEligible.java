package com.example.springBootWithRealcode.service.eligibility.extend;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;

public class RestoreEligibleAndExtendedInEligible extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_506.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_506.getValue();

    boolean restoreEligible;
    boolean extendEligible;

    public RestoreEligibleAndExtendedInEligible(boolean restoreEligible,boolean extendEligible){
        super(null,CODE,MESSAGE,true);
        this.restoreEligible = restoreEligible;
        this.extendEligible = extendEligible;
    }

    @Override
    public boolean check() {
        return restoreEligible && !extendEligible;
    }
}
