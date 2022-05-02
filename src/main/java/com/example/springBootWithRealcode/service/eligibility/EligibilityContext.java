package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class EligibilityContext {

    private final PTPEligibilityRequest eligibilityRequest;
    private final CreditResultResponse creditResultResponse;
    private final CsPtpSvcResponse csPtpSvcResponse;
    private final AccounInfoResponse accounInfoResponse;
    private final CollectionInfoResponse collectionInfoResponse;
    private final CollectionPathResponse collectionPathResponse;
    private final List<RecommededPayment> recommededPayments;

}
