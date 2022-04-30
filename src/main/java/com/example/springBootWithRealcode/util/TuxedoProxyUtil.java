package com.example.springBootWithRealcode.util;

import com.example.springBootWithRealcode.config.RestWebClientConfig;
import com.example.springBootWithRealcode.exception.BadRequestException;
import com.example.springBootWithRealcode.exception.ResourceNotFound;
import com.example.springBootWithRealcode.exception.ServiceUnavailableException;
import com.example.springBootWithRealcode.model.RequestHeaders;
import com.example.springBootWithRealcode.model.TuxedoResponse;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Component
@Value
@Slf4j
public class TuxedoProxyUtil {

    WebClient webClient;
    String uri;

    public TuxedoProxyUtil(RestWebClientConfig restWebClientConfig,
                           @org.springframework.beans.factory.annotation.Value("${tuxedo-proxy.citsel-service-url}") String uri) {
        this.webClient = restWebClientConfig.restWebClient();
        this.uri = uri;
    }

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_OF_STRING_TO_OBJECT_REFRENCE = new ParameterizedTypeReference<>() {
    };

    public Mono<TuxedoResponse> getTuxedoBuildResponse(RequestHeaders requestHeaders, Map<String, Object> tuxedoRequest, String serivceName) {
        if (Objects.nonNull(requestHeaders.getOperatorId()))
            tuxedoRequest.put("OPERATOR_ID", requestHeaders.getOperatorId());

        log.info("{}-TuxedoRequest:{}", serivceName, tuxedoRequest);

        return webClient.post().uri(uriBuilder -> uriBuilder.path(uri).queryParam("serviceName", serivceName).build())
                .header("requestId", requestHeaders.getRequestId())
                .header("applicationId", requestHeaders.getApplicationId())
                .body(Mono.just(tuxedoRequest), Map.class)
                .retrieve()
                .onStatus(status -> HttpStatus.NOT_FOUND == status, clientResponse -> Mono.error(new ResourceNotFound("BACKENDERROR", "ResourceNotFoundError")))
                .onStatus(status -> HttpStatus.SERVICE_UNAVAILABLE == status, clientResponse -> Mono.error(new ServiceUnavailableException("BACKENDERROR", "ServiceUnavilableException")))
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new BadRequestException("BACKENDERROR", "InvalidRequestFromBackEndService")))
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new BadRequestException("BACKENDERROR", "InternalServerErrorFromBackEndService")))
                .bodyToMono(MAP_OF_STRING_TO_OBJECT_REFRENCE)
                .map(TuxedoResponse::of);
    }
}
