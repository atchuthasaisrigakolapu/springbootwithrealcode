package com.example.springBootWithRealcode.service.eligibility.create;

import com.example.springBootWithRealcode.model.PTPParam;
import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

import java.util.Optional;

public class MediumRiskCustomer extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_104.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_104.getValue();
   public  MediumRiskCustomer(EligibilityContext eligibilityContext){
       super(eligibilityContext,CODE,MESSAGE,true);
   }

     @Override
    public boolean check() {
        Optional<PTPParam> ptpParam = Optional.ofNullable(eligibilityContext.getCsPtpSvcResponse())
                .flatMap(cs -> Optional.ofNullable(cs.getPtpParams())
                        .map(ptpParams -> ptpParams.get(0)));
        return ptpParam.filter(param -> "MEDIUM".equalsIgnoreCase(param.getRisk()) && eligibilityContext.getCsPtpSvcResponse().getNumberOfBrokenPtp() > 0).isPresent();
    }
}
