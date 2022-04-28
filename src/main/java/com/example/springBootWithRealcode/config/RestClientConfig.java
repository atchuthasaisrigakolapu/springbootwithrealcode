package com.example.springBootWithRealcode.config;

import com.example.springBootWithRealcode.constant.Constants;
import com.example.springBootWithRealcode.exception.TuxedoServiceException;
import com.example.springBootWithRealcode.model.PTPWebClientConfig;
import com.example.springBootWithRealcode.model.WebClientModel;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class RestClientConfig {

    private final String baseUrl;
    private final Integer connectTimeout;
    private final Integer readTimeOut;
    private final Integer writeTimeOut;
    private final PTPWebClientConfig ptpWebClientConfig;
    private final WebClientModel webClientModel;

    public RestClientConfig(PTPWebClientConfig ptpWebClientConfig, WebClientModel webClientModel) {
        this.ptpWebClientConfig = ptpWebClientConfig;
        this.webClientModel = webClientModel;
        this.baseUrl = webClientModel.getCitselBaseUrl();
        this.connectTimeout = webClientModel.getConnectTimeOut();
        this.readTimeOut = webClientModel.getCitselReadTimeOut();
        this.writeTimeOut = webClientModel.getCitselWriteTimeOut();
    }

    @Bean
    public WebClient restWebClient() {
        return WebClient.builder().baseUrl(baseUrl)
                .clientConnector(clientHttpConnector())
                .defaultHeader(HttpHeaders.USER_AGENT, "WebClient")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .filter(logRequest())
                .filter(logResposeStatus())
                .build();
    }
    @Bean("AccountWebClient")
    public WebClient accountWebClient() {
        return WebClient.builder().baseUrl(ptpWebClientConfig.getAccountInfoUrl())
                .clientConnector(clientHttpConnector())
                .defaultHeader(HttpHeaders.USER_AGENT, "WebClient")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .filter(logRequest())
                .filter(logResposeStatus())
                .build();
    }
    @Bean("creditEngineWebClient")
    public WebClient creditEngineWebClient() {
        return WebClient.builder().baseUrl(ptpWebClientConfig.getCreditEngineUrl())
                .clientConnector(clientHttpConnector())
                .defaultHeader(HttpHeaders.USER_AGENT, "WebClient")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .filter(logRequest())
                .filter(logResposeStatus())
                .build();
    }
    @Bean("CollectionInfoWebClient")
    public WebClient collectionInfoWebClient() {
        return WebClient.builder().baseUrl(ptpWebClientConfig.getCollectionInfoUrl())
                .clientConnector(clientHttpConnector())
                .defaultHeader(HttpHeaders.USER_AGENT, "WebClient")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .filter(logRequest())
                .filter(logResposeStatus())
                .build();
    }
    @Bean("CollectionPathWebClient")
    public WebClient collectionPathWebClient() {
        return WebClient.builder().baseUrl(ptpWebClientConfig.getCollectionPathUrl())
                .clientConnector(clientHttpConnector())
                .defaultHeader(HttpHeaders.USER_AGENT, "WebClient")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .filter(logRequest())
                .filter(logResposeStatus())
                .build();
    }

    private ClientHttpConnector clientHttpConnector() {
        var build = ConnectionProvider.builder("connection-provider")
                .maxConnections(webClientModel.getMaxConnections())
                .maxIdleTime(Duration.of(webClientModel.getIdleTimeout(), ChronoUnit.SECONDS))
                .build();
        var httpClient = HttpClient.create(build).tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeOut, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new ReadTimeoutHandler(readTimeOut, TimeUnit.MILLISECONDS))
                ));
        return new ReactorClientHttpConnector(httpClient.wiretap(true));
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request:{}{}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> {
                if (name.equalsIgnoreCase("requestId"))
                    log.info("{}={}", name, value);
            }));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResposeStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response Status {}", clientResponse.statusCode());
            if (!CollectionUtils.isEmpty(clientResponse.headers().header("TUXEDO-APPLICATION-CODE")) && (!ObjectUtils.isEmpty(clientResponse.headers().header("TUXEDO-APPLICATION-CODE").get(0)))) {
                var applicationCode = Integer.parseInt(clientResponse.headers().header("TUXEDO-APPLICATION-CODE").get(0));
                log.info("Tuxedo Application {}", applicationCode);
                if (applicationCode < 0)
                    throw new TuxedoServiceException(Constants.ErrorCodes.TUXEDO_ERROR, Constants.ErrorCodes.TUXEDO_ERROR_DESCRIPTION);
            }
            return Mono.just(clientResponse);
        });
    }
}

