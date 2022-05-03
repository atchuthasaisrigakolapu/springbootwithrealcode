package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder(toBuilder = true)
@Value
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PTPEligibilityResponse {

    String refId;

    String ban;

    List<PTPOperatonsEligibility> ptpOperatonsEligibility;

    CsPtpSvcResponse csPtpSvcResponse;

    AccounInfoResponse accounInfoResponse;

    CreditResultResponse creditInfo;

    CollectionInfoResponse collectionInfoResponse;

    List<RecommededPayment> recommededPaymentsList;
}
