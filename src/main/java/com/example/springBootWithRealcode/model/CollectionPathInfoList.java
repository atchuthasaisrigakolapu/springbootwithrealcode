package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
@Accessors
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CollectionPathInfoList {

    String collectionPathCode;
    Integer collectionStepNum;
    String collectionActivityCode;
    Integer daysFromPrevStep;
    String pointOfDaysCount;
    String collectionctivityDesc;

}
