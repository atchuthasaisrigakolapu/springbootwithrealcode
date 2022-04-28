package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.model.PTPEligibilityRequest;
import com.example.springBootWithRealcode.model.PTPEligibilityResponse;
import com.example.springBootWithRealcode.model.RequestHeaders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PTPEligibilityService {

    private final CreditEngineService creditEngineService;

    public PTPEligibilityResponse ptpEligibility(PTPEligibilityRequest ptpEligibilityRequest, RequestHeaders requestHeaders) {

        var ban = ptpEligibilityRequest.getBan();


        return null;
    }
}
