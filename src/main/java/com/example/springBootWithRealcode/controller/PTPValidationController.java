package com.example.springBootWithRealcode.controller;


import com.example.springBootWithRealcode.model.PTPValidationRequest;
import com.example.springBootWithRealcode.model.PTPValidationResponse;
import com.example.springBootWithRealcode.service.PTPValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.example.springBootWithRealcode.constant.Constants.*;
import static com.example.springBootWithRealcode.constant.Constants.FRANCHISE;


@Slf4j
@RestController
@AllArgsConstructor
public class PTPValidationController {

    private final PTPValidationService ptpValidationService;

    @GetMapping(value="/v1/remote_action/account/{accountAlias}/promiseToPay/validation")
    public PTPValidationResponse promsieToPayValidation(@PathVariable(ACCOUNT_ALIAS) @NotBlank(message = ACCOUNT_REQUIRED) String accountAlias,
                                                        @RequestHeader(value=REQUEST_ID)String requestId,
                                                        @RequestHeader(value=CITSEL_SUBSCRIPTION_KEY)String citselSubscriptionKey,
                                                        @RequestHeader(value=APPLICATION_ID)String applicationId,
                                                        @Valid @RequestBody PTPValidationRequest ptpValidationRequest){

        return ptpValidationService.validatePTP(ptpValidationRequest,accountAlias);

    }

}
