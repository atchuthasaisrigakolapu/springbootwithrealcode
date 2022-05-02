package com.example.springBootWithRealcode.service.eligibility.update;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

import java.util.Objects;

public class PTPOldPTPIdNotZero extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_202.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_202.getValue();

   public PTPOldPTPIdNotZero(EligibilityContext eligibilityContext){
       super(eligibilityContext,CODE,MESSAGE,true);
   }

    @Override
    public boolean check() {
        return Objects.nonNull(eligibilityContext.getCsPtpSvcResponse().getPtpInfo())
                &&(!"O".equalsIgnoreCase(eligibilityContext.getCsPtpSvcResponse().getPtpInfo().getPtpOldPtp()));
    }
}
