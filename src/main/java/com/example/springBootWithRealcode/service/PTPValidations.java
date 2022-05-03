package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.exception.BadRequestException;
import com.example.springBootWithRealcode.model.ValidationConstraints;
import com.example.springBootWithRealcode.service.eligibility.*;
import com.example.springBootWithRealcode.model.PTPOperationEnum;
import com.example.springBootWithRealcode.model.PTPValidationResponse;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class PTPValidations {

    public PTPValidationResponse validate(ValidationContext validationContext){
        var operation = validationContext.getValidationInfo().getOperation();
        if(PTPOperationEnum.CREATE.equals(operation)){
            return validateCreateOperation(validationContext);
        }
        if(PTPOperationEnum.UPDATE.equals(operation)){
            return validateCreateOperation(validationContext);
        }
        throw new BadRequestException("bad request","validation not implemented");
    }

    private PTPValidationResponse validateCreateOperation(ValidationContext validationContext) {
       var baseValidations = List.of(new RestoreEligibileAndCategoryAB(validationContext), new RestoreEliExtendEliAndCategoryAB(validationContext));
        return getPTPValidationResponse(baseValidations,validationContext);
    }

    private PTPValidationResponse getPTPValidationResponse(List<BaseValidation> baseValidations, ValidationContext validationContext) {

        var hardStop = false;
        var eligibilityConstraints = new ArrayList<ValidationConstraints>();
        for(BaseValidation eligibilityCheck:baseValidations){
            if(eligibilityCheck.isHardStop() && hardStop)
                continue;
            var eligibilityConstraint = eligibilityCheck.run();
            if(Objects.nonNull(eligibilityConstraint)){
                if(eligibilityConstraint.isHardStop()){
                    hardStop = true;
                }
                eligibilityConstraints.add(eligibilityConstraint);
            }
        }
        return PTPValidationResponse.builder()
                .refId(validationContext.getPtpEligibilityPersistanceRecord().getRefId())
                .validationFlag(hardStop?"N":"Y")
                .validationConstraints(eligibilityConstraints)
                .ptpCategory(validationContext.getValidationInfo().getPtpCategory())
                .extendDate(validationContext.getValidationInfo().getExtendDate())
                .pastDueAmt(validationContext.getPtpEligibilityPersistanceRecord().getPtpEligbilityDetails().getPostDueAmount())
                .build();
    }

    private PTPValidationResponse validateUpdateOperation(ValidationContext validationContext) {
        var baseValidations = List.of(new PTPInstallementsNotPending(validationContext),new RestoreEligibileAndCategoryAB(validationContext), new RestoreEliExtendEliAndCategoryAB(validationContext));
        return getPTPValidationResponse(baseValidations,validationContext);
    }

}
