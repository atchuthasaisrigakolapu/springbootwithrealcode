package com.example.springBootWithRealcode.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String ACCOUNT_ALIAS ="accountAlias";
    public static final String ACCOUNT_REQUIRED = "account number is required";
    public static final String OPERATION = "operation";
    public static final String ENABLE_CACHE = "enableCache";
    public static final String BROKEN_PTP_VALIDATION = "brokenPtpValidation";
    public static final String INCLUDE_INFO = "includeInfo";
    public static final String EXCLUDE_INFO = "excludeInfo";
    public static final String REQUEST_ID = "requestId";
    public static final String CITSEL_SUBSCRIPTION_KEY = "citsel_Subscription-Key";
    public static final String APPLICATION_ID = "applicationId";
    public static final String FRANCHISE = "franchise";
    public static final String OPERATORID = "operatorId";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ErrorCodes{
        public static final String TUXEDO_ERROR = "TUXEDOERROR";
        public static final String TUXEDO_ERROR_DESCRIPTION = "Tuxedo Service Error";
    }



}
