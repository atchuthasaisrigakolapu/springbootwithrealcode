package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PTPParam {
    String risk;
    String brokenPromises;
    String isClm;
    String type;
    Integer installments;
    Integer maxDays1stInst;
    Integer maxDaysLastInst;

}
