package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.config.RestWebClientConfig;
import com.example.springBootWithRealcode.model.AccounInfoResponse;
import com.example.springBootWithRealcode.model.PTPWebClientConfig;
import com.example.springBootWithRealcode.model.RequestHeaders;
import com.example.springBootWithRealcode.model.WebClientModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
    
    private static MockWebServer mockBackEnd;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private AccountService accountService;

    @BeforeAll
    static void setUp()throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }
    @AfterAll
    static void tearDown()throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize(){
        RestWebClientConfig restWebClientConfig =  new RestWebClientConfig(getPtpWebClientConfig(),getWebClient());
    }

    @Test
    public void getAccountDetailsSuccess()throws Exception{
        mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(accountInfoResponse()))
                .addHeader("Content-Type","application/json"));
        var accountDetails = accountService.getAccountDetails("123456", RequestHeaders.builder().build());
        Assertions.assertThat(accountDetails.getBanStaus()).isEqualTo("active");

    }
    @Test
    public void getAccountDetailsWith500Internal()throws Exception{
        mockBackEnd.enqueue(new MockResponse().setResponseCode(500).setBody(objectMapper.writeValueAsString("Internal server error"))
                .addHeader("Content-Type","application/json"));
        var actualMessage = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,()->accountService.getAccountDetails("123456", RequestHeaders.builder().build()));
        org.junit.jupiter.api.Assertions.assertTrue(actualMessage.equals("Internal server error"));

    }
    @Test
    public void getAccountDetailsWith400Internal()throws Exception{
        mockBackEnd.enqueue(new MockResponse().setResponseCode(400).setBody(objectMapper.writeValueAsString("bad request"))
                .addHeader("Content-Type","application/json"));
        var actualMessage = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,()->accountService.getAccountDetails("123456", RequestHeaders.builder().build()));
        org.junit.jupiter.api.Assertions.assertTrue(actualMessage.equals("bad request"));

    }
    @Test
    public void getAccountDetailsWith404Internal()throws Exception{
        mockBackEnd.enqueue(new MockResponse().setResponseCode(404).setBody(objectMapper.writeValueAsString("not found"))
                .addHeader("Content-Type","application/json"));
        var actualMessage = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,()->accountService.getAccountDetails("123456", RequestHeaders.builder().build()));
        org.junit.jupiter.api.Assertions.assertTrue(actualMessage.equals("not found"));

    }
    @Test
    public void getAccountDetailsWith503Internal()throws Exception{
        mockBackEnd.enqueue(new MockResponse().setResponseCode(503).setBody(objectMapper.writeValueAsString("service unavilable"))
                .addHeader("Content-Type","application/json"));
        var actualMessage = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,()->accountService.getAccountDetails("123456", RequestHeaders.builder().build()));
        org.junit.jupiter.api.Assertions.assertTrue(actualMessage.equals("service unavilable"));

    }

    private PTPWebClientConfig getPtpWebClientConfig() {
        PTPWebClientConfig ptpWebClientConfig = new PTPWebClientConfig();
        ptpWebClientConfig.setAccountInfoUri("/v1/remote_data/account/{accountAlias}");
        return ptpWebClientConfig;
    }

    private WebClientModel getWebClient(){
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        WebClientModel webClientModel = new WebClientModel();
        webClientModel.setCitselBaseUrl(baseUrl);
        webClientModel.setCitselConnectTimeOut(5000);
        webClientModel.setCitselReadTimeOut(50000);
        webClientModel.setCitselWriteTimeOut(50000);
        webClientModel.setMaxConnections(500);
        webClientModel.setIdleTimeout(1500);
        return webClientModel;
    }
    private AccounInfoResponse accountInfoResponse(){
        return AccounInfoResponse.builder()
                .banStaus("Active")
                .build();
    }




    
    
}
