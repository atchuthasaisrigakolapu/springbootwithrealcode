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
public class PTPEligibilityPersistanceRecord {

    String refId;
    String ban;
    String profile;
    List<PTPOperatonsEligibility> ptpOperatonsEligibilityList;
    CsPtpSvcResponse ptpEligbilityDetails;
    AccounInfoResponse accounInfo;
    CreditResultResponse creditInfo;
    CollectionInfoResponse collectionInfo;
    boolean restoreElibleInd;
    boolean extendElibleInd;
    CollectionPathResponse collectionPathResponse;
    PTPValidationRequest ptpValidationRequest;
    PTPValidationResponse ptpValidationResponse;
    List<RecommededPayment> recommededPayments;
}
