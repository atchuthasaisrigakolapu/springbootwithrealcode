package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.PTPOperationEnum;
import com.example.springBootWithRealcode.model.PTPOperatonsEligibility;
import com.example.springBootWithRealcode.model.PTPResponseCodes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CreateAndUpdateOperationsInEligible extends BaseEligibility {

    public static final String CODE = PTPResponseCodes.PTP_ELIGIBILITY_601.name();
    public static final String MESSAGE = PTPResponseCodes.PTP_ELIGIBILITY_601.getValue();

    private final List<PTPOperatonsEligibility> eligibilityList;

    public final List<String> operations= Arrays.asList(PTPOperationEnum.CREATE.name(),PTPOperationEnum.UPDATE.name());


    public CreateAndUpdateOperationsInEligible(EligibilityContext eligibilityContext,List<PTPOperatonsEligibility> eligibilityList) {
        super(eligibilityContext, CODE, MESSAGE, true);
        this.eligibilityList =eligibilityList;
    }

    @Override
    public boolean check() {
        return this.eligibilityList.stream()
                .filter(Objects::nonNull)
                .noneMatch(list -> operations.contains(list.getPtpOperation()) && "Y".equalsIgnoreCase(list.getEligibilityInd()));
    }
}
