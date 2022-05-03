package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PTPResponseCodes {

    PTP_ELIGIBILITY_101("Customer is currently enrolled for IPP"),
    PTP_ELIGIBILITY_102("There is existing PTP with pending status"),
    PTP_ELIGIBILITY_103("For High risk customer with broken PTPS with 1 or more in six months"),
    PTP_ELIGIBILITY_104("For Medium risk customer with broken PTPS with 2 or more in six months"),

    PTP_ELIGIBILITY_201("Activity Allowed only for PTP in Pending status"),
    PTP_ELIGIBILITY_202("Only one change for PTP allowd"),

    PTP_ELIGIBILITY_301("Activity Allowed only for PTP in Pending status or Broken status"),
    PTP_ELIGIBILITY_401("BAN must be  suspended or have data suspension"),
    PTP_ELIGIBILITY_501("Cannot extend since BAN is not delinquent status D"),
    PTP_ELIGIBILITY_502("Extended is not possible when collection is not present"),
    PTP_ELIGIBILITY_503("Ban have hold automatic treatment"),
    PTP_ELIGIBILITY_504("Ban must have path"),
    PTP_ELIGIBILITY_505("DF Step should be found in path"),
    PTP_ELIGIBILITY_506("Extended wonot be performed while restoring account"),
    PTP_ELIGIBILITY_507("Set path is ineligible"),
    PTP_ELIGIBILITY_601("Create and Update PTP operation is not eligible "),
    PTP_ELIGIBILITY_602("Restore and Extend ineligible for CLM customer"),
    PTP_VALIDATION_101("Automatic Restore/Extend operation happens for PTP category with A/B Suspended Data/Suspended  Ban .Manual operations are ineligibile"),
    PTP_VALIDATION_102("Automatic Extend operation happens for PTP category A/B with Delinquent Ban .Manual operations are ineligibile"),
    PTP_VALIDATION_201("Existing installement details can be updated if it is pending status");


    private final String value;

    PTPResponseCodes(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
