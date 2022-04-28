package com.example.springBootWithRealcode.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeaders {

    private String citselSubscriptionKey;
    private String requestId;
    private String applicationId;
    private String franchise;
    private String operatorId;
}
