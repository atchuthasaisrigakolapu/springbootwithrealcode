package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder(toBuilder = true)
@Value
@Jacksonized
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PTPOperatonsEligibility {
    String ptpOperation;
    String eligibilityInd;
    List<EligibilityConstraint> eligibilityConstraint;
}