package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PTPExcludeInfoEnum {

    CREDIT_INFO("CreditInfo"),
    PTP_ELIGIBILITY_DETAILS("PTPeligibilityDetails"),
    PTP_INFO("PTPInfo"),
    PTP_PARAM("PTPParam");
    private final String value;
    PTPExcludeInfoEnum(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
