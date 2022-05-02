package com.example.springBootWithRealcode.service.eligibility.extend;

import com.example.springBootWithRealcode.model.PTPOperatonsEligibility;
import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

import java.util.List;

public class RestoreIneligibleAndBanStatusNotDelinquent extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_501.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_501.getValue();
    List<PTPOperatonsEligibility> eligibilityList;
    boolean restoreEligible;
    public RestoreIneligibleAndBanStatusNotDelinquent(EligibilityContext eligibilityContext,List<PTPOperatonsEligibility> eligibilityList,boolean restoreEligible){
        super(eligibilityContext,CODE,MESSAGE,true);
        this.eligibilityList = eligibilityList;
        this.restoreEligible =restoreEligible;
    }
    @Override
    public boolean check() {
        return !restoreEligible && !"D".equalsIgnoreCase(eligibilityContext.getAccounInfoResponse().getColDelingStatus());
    }
}
