package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.exception.BadRequestException;
import com.example.springBootWithRealcode.exception.ResourceNotFound;
import com.example.springBootWithRealcode.exception.ServiceUnavailableException;
import com.example.springBootWithRealcode.model.CreditResultOutBoundResponse;
import com.example.springBootWithRealcode.model.CreditResultResponse;
import com.example.springBootWithRealcode.model.PTPWebClientConfig;
import com.example.springBootWithRealcode.model.RequestHeaders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class CreditEngineService {

    private final WebClient creditEngineWebClient;
    private final PTPWebClientConfig ptpWebClientConfig;
    //private final CreditEngineService
    public CreditResultResponse getCreditResults(String ban, RequestHeaders requestHeaders){
        return creditEngineWebClient.get().uri(uriBuilder -> uriBuilder.path(ptpWebClientConfig.getCheckCreditUri())
                .queryParam("requestDate", LocalDate.now(ZoneId.of("America/Toronto")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .queryParam("applicationID", "Citsel")
                .queryParam("transactionID", requestHeaders.getRequestId())
                .queryParam("calcLines", "Y")
                .build(ban))
                .headers(httpHeaders -> httpHeaders.set("CCE-Susbcription-Key",ptpWebClientConfig.getCeSubscriptionKey())).retrieve()
                .onStatus(status-> HttpStatus.NOT_FOUND==status,clientResponse -> Mono.error(new ResourceNotFound("BACKENDERROR","ResourceNotFoundErrorBckendSide")))
                .onStatus(status-> HttpStatus.SERVICE_UNAVAILABLE==status,clientResponse -> Mono.error(new ServiceUnavailableException("BACKENDERROR","ResourceNotFoundErrorBckendSide")))
                .onStatus(HttpStatus::is4xxClientError,clientResponse -> Mono.error(new BadRequestException("BACKENDERROR","InvalidRequestBodyfromBackEndService")))
                .onStatus(HttpStatus::is4xxClientError,clientResponse -> Mono.error(new ServiceUnavailableException("BACKENDERROR","InternalServerErrorFromBackEnd")))
                .bodyToMono(CreditResultOutBoundResponse.class)
                .flatMap(resp->{
                    log.info("CreditEngineService::"+resp);
                    var creditResultResponse = constructCreditEngineResponse(resp);
                    return Mono.just(creditResultResponse);
                }).block();

    }
    private CreditResultResponse constructCreditEngineResponse(CreditResultOutBoundResponse resp){
        var creditResultResponseBuilder = CreditResultResponse.builder();
        if(Objects.nonNull(resp)){
            if(Objects.nonNull(resp.getContent())){
                creditResultResponseBuilder.clmTag(resp.getContent().getClmTag());
                creditResultResponseBuilder.cla(resp.getContent().getCreditOptionInfoObject() != null ?resp.getContent().getCreditOptionInfoObject().getCla():null);
                constructCreditInfoDetails(resp,creditResultResponseBuilder);
                creditResultResponseBuilder.success(true);
             }
            else if(!ObjectUtils.isEmpty(resp.getErrors()) && !ObjectUtils.isEmpty(resp.getErrors().get(0).getErrorMessage())){
                creditResultResponseBuilder.success(false);
            }
        }
        return creditResultResponseBuilder.build();
    }
    private void constructCreditInfoDetails(CreditResultOutBoundResponse resp,CreditResultResponse.CreditResultResponseBuilder creditResultResponseBuilder){
        if(resp.getContent().getCreditInfo() != null){
            creditResultResponseBuilder.riskClass(resp.getContent().getCreditInfo().getRiskClass());
            creditResultResponseBuilder.creditClass(resp.getContent().getCreditInfo().getCreditClass());
        }
    }


}
