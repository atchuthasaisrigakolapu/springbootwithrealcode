package com.example.springBootWithRealcode.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "citsel")
@Component
@Data
public class PTPWebClientConfig {

    private String creditEngineUrl;
    private String checkCreditUri;
    private String ceSubscriptionKey;
    private String collectionInfoUrl;
    private String collectionInfoUri;
    private String accountInfoUrl;
    private String accountInfoUri;
    private String collectionPathUrl;
    private String collectionPathUri;
}
