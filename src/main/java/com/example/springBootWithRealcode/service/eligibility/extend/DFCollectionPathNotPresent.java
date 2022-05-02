package com.example.springBootWithRealcode.service.eligibility.extend;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

public class DFCollectionPathNotPresent extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_505.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_505.getValue();

    public DFCollectionPathNotPresent(EligibilityContext eligibilityContext){
        super(eligibilityContext,CODE,MESSAGE,true);
    }

    @Override
    public boolean check() {
        return Objects.nonNull(eligibilityContext.getCollectionPathResponse())
                && !CollectionUtils.isEmpty(eligibilityContext.getCollectionPathResponse().getCollectionPathInfoList())
                && eligibilityContext.getCollectionPathResponse().getCollectionPathInfoList().stream()
                .noneMatch(collectionPathInfo->"DF".equalsIgnoreCase(collectionPathInfo.getCollectionActivityCode()));

    }
}
