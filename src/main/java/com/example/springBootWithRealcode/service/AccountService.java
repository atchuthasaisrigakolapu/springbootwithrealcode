package com.example.springBootWithRealcode.service;


import com.example.springBootWithRealcode.exception.BadRequestException;
import com.example.springBootWithRealcode.exception.ResourceNotFound;
import com.example.springBootWithRealcode.exception.ServiceUnavailableException;
import com.example.springBootWithRealcode.model.AccounInfoResponse;
import com.example.springBootWithRealcode.model.PTPWebClientConfig;
import com.example.springBootWithRealcode.model.RequestHeaders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class AccountService {

    @Resource(name="AccountWebClient")
    private final WebClient webClient;
    private final PTPWebClientConfig ptpWebClientConfig;

    public AccounInfoResponse getAccountDetails(String ban, RequestHeaders requestHeaders){
        return webClient.get().uri(uriBuilder -> uriBuilder.path(ptpWebClientConfig.getAccountInfoUri()).build(ban))
                .headers(httpHeaders -> {
                    httpHeaders.set("requestId",requestHeaders.getRequestId());
                    httpHeaders.set("applicationId", requestHeaders.getApplicationId());
                    httpHeaders.set("franchise", requestHeaders.getFranchise());
                    httpHeaders.set("citsel-Subscription-Key",requestHeaders.getCitselSubscriptionKey());
                    httpHeaders.set("operatorId",requestHeaders.getOperatorId());
                })
                .retrieve()
                .onStatus(status -> HttpStatus.NOT_FOUND == status, clientResponse -> Mono.error(new ResourceNotFound("BACKENDERROR", "ResourceNotFoundError")))
                .onStatus(status -> HttpStatus.SERVICE_UNAVAILABLE == status, clientResponse -> Mono.error(new ServiceUnavailableException("BACKENDERROR", "ServiceUnavilableException")))
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new BadRequestException("BACKENDERROR", "InvalidRequestFromBackEndService")))
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new BadRequestException("BACKENDERROR", "InternalServerErrorFromBackEndService")))
                .bodyToMono(AccounInfoResponse.class)
                .block();
    }

}
