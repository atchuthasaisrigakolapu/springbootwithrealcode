package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.PTPEligibilityResponse;
import com.example.springBootWithRealcode.model.PTPOperationEnum;
import com.example.springBootWithRealcode.model.PTPOperatonsEligibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class CommonEligibility {

    private final ObjectMapper objectMapper;

    public PTPEligibilityResponse eligibilityCheck(EligibilityContext eligibilityContext){
        var eligibilityList = new ArrayList<>();
        if (eligibilityContext.getEligibilityRequest().getOperations().contains(PTPOperationEnum.CREATE)) {
            eligibilityList.add(createPTPEligibility(eligibilityContext));
        }
        return null;
    }

    private PTPOperatonsEligibility createPTPEligibility(EligibilityContext eligibilityContext) {

        //new ArrayList<>(new IppIndicatorY(eligibilityContext),new PTPStatusPending(eligibilityContext));
        return PTPOperatonsEligibility.builder().build();
    }

}
