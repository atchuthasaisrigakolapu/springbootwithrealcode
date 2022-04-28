package com.example.springBootWithRealcode.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class WebClientModel {

    @Value("${web.timeout.connect}")
    private Integer connectTimeOut;

    @Value("${web.timeout.readTimeoutGet}")
    private Integer readTimeOutGet;

    @Value("${web.timeout.readTimeoutPost}")
    private Integer readTimeOutPost;

    @Value("${tuxedo-proxy.citsel-service-url}")
    private String citselBaseUrl;

    @Value("${tuxedo-proxy.connectTimeout}")
    private Integer citselConnectTimeOut;

    @Value("${tuxedo-proxy.readTimeout}")
    private Integer citselReadTimeOut;

    @Value("${tuxedo-proxy.writeTimeout}")
    private Integer citselWriteTimeOut;

    @Value("${tuxedo-proxy.connectionProvider.maxConnections}")
    private Integer maxConnections;

    @Value("${tuxedo-proxy.connectionProvider.idleTimeout}")
    private Integer idleTimeout;

}
