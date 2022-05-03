package com.example.springBootWithRealcode.model;


import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Accessors
@Jacksonized
public class CsPtpSvcRequest {

    Integer ban;
    String riskClass;
    String clmTag;
    Double cla;
    String creditInputInd;
}
