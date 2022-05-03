package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.*;
import com.example.springBootWithRealcode.service.eligibility.cancel.PTPStatusNotPendingOrBroken;
import com.example.springBootWithRealcode.service.eligibility.create.HighRiskCustomer;
import com.example.springBootWithRealcode.service.eligibility.create.IPPIndicatorY;
import com.example.springBootWithRealcode.service.eligibility.create.MediumRiskCustomer;
import com.example.springBootWithRealcode.service.eligibility.create.PTPPendingStatus;
import com.example.springBootWithRealcode.service.eligibility.extend.*;
import com.example.springBootWithRealcode.service.eligibility.restore.BanNotSuspendedOrDataNotSuspended;
import com.example.springBootWithRealcode.service.eligibility.update.PTPOldPTPIdNotZero;
import com.example.springBootWithRealcode.service.eligibility.update.PTPStatusNotPending;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Component
@AllArgsConstructor
public class CommonEligibility {

    private final ObjectMapper objectMapper;

    public PTPEligibilityResponse eligibilityCheck(EligibilityContext eligibilityContext){
        var eligibilityList = new ArrayList<PTPOperatonsEligibility>();
        if (eligibilityContext.getEligibilityRequest().getOperations().contains(PTPOperationEnum.CREATE)) {
            eligibilityList.add(createPTPEligibility(eligibilityContext));
        }
        if (eligibilityContext.getEligibilityRequest().getOperations().contains(PTPOperationEnum.UPDATE)) {
            eligibilityList.add(updatePTPEligibility(eligibilityContext));
        }
        if (eligibilityContext.getEligibilityRequest().getOperations().contains(PTPOperationEnum.CANCEL)) {
            eligibilityList.add(cancelPTPEligibility(eligibilityContext));
        }
        if (eligibilityContext.getEligibilityRequest().getOperations().contains(PTPOperationEnum.RESTORE)) {
            eligibilityList.add(restorePTPEligibility(eligibilityContext,eligibilityList));
        }
        if (eligibilityContext.getEligibilityRequest().getOperations().contains(PTPOperationEnum.EXTEND)) {
            eligibilityList.add(extendPTPEligibility(eligibilityContext,eligibilityList));
        }
        return PTPEligibilityResponse.builder()
                .refId(UUID.randomUUID().toString())
                .ban(eligibilityContext.getEligibilityRequest().getBan())
                .ptpOperatonsEligibility(eligibilityList)
                .csPtpSvcResponse(eligibilityContext.getCsPtpSvcResponse())
                .accounInfoResponse(ObjectUtils.isEmpty(eligibilityContext.getAccounInfoResponse())?null
                        :objectMapper.convertValue(eligibilityContext.getAccounInfoResponse(), AccounInfoResponse.class))
                .creditInfo(eligibilityContext.getCreditResultResponse())
                .collectionInfoResponse(eligibilityContext.getCollectionInfoResponse())
                .recommededPaymentsList(eligibilityContext.getRecommededPayments())
                .build();
    }

    private PTPOperatonsEligibility createPTPEligibility(EligibilityContext eligibilityContext) {

        var eligibilityCheck = Arrays.asList(new IPPIndicatorY(eligibilityContext), new PTPPendingStatus(eligibilityContext));
        if("Y".equalsIgnoreCase(eligibilityContext.getEligibilityRequest().getBrokenPtpValidation())){
            eligibilityCheck = Arrays.asList(new IPPIndicatorY(eligibilityContext), new PTPPendingStatus(eligibilityContext),new HighRiskCustomer(eligibilityContext),new MediumRiskCustomer(eligibilityContext));
        }
        return getPtpOperationsEligibility(eligibilityCheck,PTPOperationEnum.CREATE);

    }
    private PTPOperatonsEligibility updatePTPEligibility(EligibilityContext eligibilityContext) {

        var eligibilityCheck = Arrays.asList(new PTPStatusNotPending(eligibilityContext), new PTPOldPTPIdNotZero(eligibilityContext));
        return getPtpOperationsEligibility(eligibilityCheck,PTPOperationEnum.UPDATE);

    }
    private PTPOperatonsEligibility cancelPTPEligibility(EligibilityContext eligibilityContext) {

        var eligibilityCheck = new ArrayList<BaseEligibility>(Collections.singletonList(new PTPStatusNotPendingOrBroken(eligibilityContext)));
        return getPtpOperationsEligibility(eligibilityCheck,PTPOperationEnum.CANCEL);

    }
    private PTPOperatonsEligibility restorePTPEligibility(EligibilityContext eligibilityContext,List<PTPOperatonsEligibility> eligibilityList) {

        var eligibilityCheck = Arrays.asList(new CreateAndUpdateOperationsInEligible(eligibilityContext,eligibilityList),
                new CLMSuspendedAccount(eligibilityContext),
                new BanNotSuspendedOrDataNotSuspended(eligibilityContext));
        return getPtpOperationsEligibility(eligibilityCheck,PTPOperationEnum.RESTORE);

    }

    private PTPOperatonsEligibility extendPTPEligibility(EligibilityContext eligibilityContext,List<PTPOperatonsEligibility> eligibilityList){

        var restoreEligibilityOpt = eligibilityList.stream().filter(Objects::nonNull)
                .filter(list -> PTPOperationEnum.RESTORE.name().equalsIgnoreCase(list.getPtpOperation()))
                .findFirst();
        var restoreEligible = restoreEligibilityOpt.orElseGet(() -> restorePTPEligibility(eligibilityContext, eligibilityList));
        var isRestoreEligible = "Y".equalsIgnoreCase(restoreEligible.getEligibilityInd());
        var eligibleList = Arrays.asList(new CreateAndUpdateOperationsInEligible(eligibilityContext, eligibilityList), new CLMSuspendedAccount(eligibilityContext),
                new RestoreIneligibleAndBanStatusNotDelinquent(eligibilityContext, eligibilityList, isRestoreEligible),
                new CollectionInfoNotPresent(eligibilityContext),
                new HoldAutomaticTratementY(eligibilityContext),
                new CollectionPathCodeBlank(eligibilityContext),
                new DFCollectionPathNotPresent(eligibilityContext));
        var  ptpOperationsEligibility = getPtpOperationsEligibility(eligibleList, PTPOperationEnum.EXTEND);

        var extendElible = "Y".equalsIgnoreCase(ptpOperationsEligibility.getEligibilityInd());
        var softwarningCheck = Arrays.asList(new RestoreEligibleAndExtendedInEligible(isRestoreEligible, extendElible), new SkipSetPath(isRestoreEligible, extendElible, eligibilityContext));
        var ptpOperationsEligibilitySoftWarning = getPtpOperationsEligibility(softwarningCheck, PTPOperationEnum.EXTEND);
        var eligibileConstraints = new ArrayList<>(ptpOperationsEligibility.getEligibilityConstraint());
        eligibileConstraints.addAll(ptpOperationsEligibilitySoftWarning.getEligibilityConstraint());
        return ptpOperationsEligibility.toBuilder().eligibilityConstraint(eligibileConstraints).build();
    }



    private PTPOperatonsEligibility getPtpOperationsEligibility(List<BaseEligibility> eligibilityChecks, PTPOperationEnum ptpOPerationEnum) {

        var hardStop = false;
        var eligibilityConstraints = new ArrayList<EligibilityConstraint>();
        for(BaseEligibility eligibilityCheck:eligibilityChecks){
            if(eligibilityCheck.isHardStop() && hardStop)
                continue;
            var eligibilityConstraint = eligibilityCheck.run();
            if(Objects.nonNull(eligibilityConstraint)){
                if(eligibilityConstraint.isHardstop()){
                    hardStop = true;
                }
                eligibilityConstraints.add(eligibilityConstraint);
            }
        }
        return PTPOperatonsEligibility.builder()
                .ptpOperation(ptpOPerationEnum.name())
                .eligibilityInd(hardStop?"N":"Y")
                .eligibilityConstraint(eligibilityConstraints)
                .build();
    }

}
