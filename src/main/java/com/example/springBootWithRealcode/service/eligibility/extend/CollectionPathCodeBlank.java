package com.example.springBootWithRealcode.service.eligibility.extend;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

import java.util.Objects;

public class CollectionPathCodeBlank extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_504.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_504.getValue();

    public CollectionPathCodeBlank(EligibilityContext eligibilityContext){
        super(eligibilityContext,CODE,MESSAGE,true);
    }

    @Override
    public boolean check() {
        return Objects.nonNull(eligibilityContext.getCollectionInfoResponse()) && Objects.nonNull(eligibilityContext.getCollectionInfoResponse().getColPathCode());
    }
}
