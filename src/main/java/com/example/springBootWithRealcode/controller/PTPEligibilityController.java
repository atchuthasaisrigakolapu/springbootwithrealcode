package com.example.springBootWithRealcode.controller;

import com.example.springBootWithRealcode.model.*;
import com.example.springBootWithRealcode.service.PTPEligibilityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


import java.util.List;

import static com.example.springBootWithRealcode.constant.Constants.*;

@RestController
@Slf4j
@AllArgsConstructor
@Validated
public class PTPEligibilityController {

    private final PTPEligibilityService ptpEligibilityService;


    @GetMapping(value="/v1/remote_action/account/{accountAlias}/promiseToPay/eligibility")
    public ResponseEntity<PTPEligibilityResponse> getEligibility(@PathVariable(ACCOUNT_ALIAS) @NotBlank(message = ACCOUNT_REQUIRED) String accountAlias,
                                                                 @RequestParam(OPERATION) @NotEmpty(message = "Operation should not be empty") List<PTPOperationEnum> operations,
                                                                 @RequestParam(value=ENABLE_CACHE,required = false) EnableCacheEnum enableCache,
                                                                 @RequestParam(value=BROKEN_PTP_VALIDATION,required = false) EnableCacheEnum brokenPtpValidation,
                                                                 @RequestParam(value=INCLUDE_INFO,required = false) List<String> includeInfoList,
                                                                 @RequestParam(value=EXCLUDE_INFO,required = false) List<String> excludeInfoList,
                                                                 @RequestHeader(value=REQUEST_ID)String requestId,
                                                                 @RequestHeader(value=CITSEL_SUBSCRIPTION_KEY)String citselSubscriptionKey,
                                                                 @RequestHeader(value=APPLICATION_ID)String applicationId,
                                                                 @RequestHeader(value=FRANCHISE)String franchise,
                                                                 @RequestHeader(value=OPERATORID,required = false)String operatorId){

        var requestHeaders = RequestHeaders.builder()
                .requestId(requestId)
                .applicationId(applicationId)
                .franchise(franchise)
                .citselSubscriptionKey(citselSubscriptionKey)
                .operatorId(operatorId)
                .build();

        var ptpEligibilityRequest = PTPEligibilityRequest.builder()
                .ban(accountAlias)
                .operations(operations)
                .includeInfoList(includeInfoList)
                .excludeInfoList(excludeInfoList)
                .enableCache((enableCache != null && enableCache.name() != null)?enableCache.name():"Y" )
                .brokenPtpValidation((brokenPtpValidation != null && brokenPtpValidation.name() != null)?brokenPtpValidation.name():"N")
                .build();

        var ptpEligibilityResponse = ptpEligibilityService.ptpEligibility(ptpEligibilityRequest,requestHeaders);

        return ResponseEntity.ok(ptpEligibilityResponse);
    }

}
