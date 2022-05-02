package com.example.springBootWithRealcode.service.eligibility.extend;

import com.example.springBootWithRealcode.model.PTPResponseCodes;
import com.example.springBootWithRealcode.service.eligibility.BaseEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;

import java.util.Objects;

public class SkipSetPath extends BaseEligibility {

    public static final String CODE  = PTPResponseCodes.PTP_ELIGIBILITY_507.name();
    public static final String MESSAGE  = PTPResponseCodes.PTP_ELIGIBILITY_507.getValue();

    private final boolean extendEligibile;
    private final boolean restoreEligibile;

    public SkipSetPath(boolean restoreEligibile, boolean extendEligibile, EligibilityContext eligibilityContext){
        super(eligibilityContext,CODE,MESSAGE,false);
        this.restoreEligibile = restoreEligibile;
        this.extendEligibile = extendEligibile;

    }

    @Override
    public boolean check() {
        if(!extendEligibile)
            return false;
        var validCollectionInfo = Objects.nonNull(eligibilityContext.getCollectionInfoResponse()) && Objects.nonNull(eligibilityContext.getCollectionInfoResponse().getColNextStepNo());
        var validCollectionPath = Objects.nonNull(eligibilityContext.getCollectionPathResponse()) && Objects.nonNull(eligibilityContext.getCollectionPathResponse().getCollectionPathInfoList());
        if(validCollectionInfo && validCollectionPath){
            if(restoreEligibile)
                return false;
            var collectionPathInfoList = eligibilityContext.getCollectionPathResponse()
                    .getCollectionPathInfoList().stream()
                    .filter(colPathInfo -> eligibilityContext.getCollectionInfoResponse().getColNextStepNo().equals(colPathInfo.getCollectionStepNum()))
                    .findFirst();
            return collectionPathInfoList.isEmpty() || !("DS".equalsIgnoreCase(collectionPathInfoList.get().getCollectionActivityCode()) || "S".equalsIgnoreCase(collectionPathInfoList.get().getCollectionActivityCode()));
        }
        else
            return true;
    }
}
