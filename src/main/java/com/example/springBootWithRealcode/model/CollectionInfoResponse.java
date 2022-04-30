package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Value
@Builder
@Accessors
@Jacksonized
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CollectionInfoResponse {

    String holdAutomaticTreatment;
    Integer colNextStepNo;
    String colPathCode;
    Double currenntDue;
    Double pastDue;
    LocalDate billDueData;
    String creditClass;
    LocalDate colNextStepDate;
    String colFixedPath;



}
