package com.example.springBootWithRealcode.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Accessors
@Jacksonized
public class PTPEligibilityRequest {
    String ban;
    List<PTPOperationEnum> operations;
    List<String> includeInfoList;
    List<String> excludeInfoList;
    String enableCache;
    String brokenPtpValidation;
}
