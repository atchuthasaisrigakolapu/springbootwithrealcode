package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.config.RestWebClientConfig;
import com.example.springBootWithRealcode.exception.TuxedoServiceException;
import com.example.springBootWithRealcode.model.*;
import com.example.springBootWithRealcode.util.TuxedoProxyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class CsPtpServiceTest {

    private static MockWebServer mockBackEnd;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    CsPtpService csPtpService;

    @Mock
    TuxedoProxyUtil tuxedoProxyUtil;
    RestWebClientConfig proxyClient;
    Map csPtpSvcServceTuxedoResponse;
    Map csPtpSvcServceTuxedoErrorResponse;

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
    void initialize() throws IOException {
        proxyClient =  new RestWebClientConfig(getPtpWebClientConfig(),getWebClient());
        tuxedoProxyUtil = new TuxedoProxyUtil(proxyClient,"");
        ReflectionTestUtils.setField(csPtpService,"tuxedoProxyUtil",tuxedoProxyUtil);
        csPtpSvcServceTuxedoResponse= objectMapper.readValue(new File("src/test/resources/csptp.json"), Map.class);
        csPtpSvcServceTuxedoErrorResponse= objectMapper.readValue(new File("src/test/resources/csptpError.json"), Map.class);
    }

    @Test
    void csPtpSuccess() throws JsonProcessingException {

        mockBackEnd.enqueue(new MockResponse().setResponseCode(200).
                setBody(objectMapper.writeValueAsString(csPtpSvcServceTuxedoResponse))
                .addHeader("Content-Type","application/json"));
        var csPtpSvc = csPtpService.getCsPtpSvc(CsPtpSvcRequest.builder().build(), RequestHeaders.builder().build());
        StepVerifier.create(csPtpSvc)
                .expectNextMatches(res->res!=null&&res.getBanStatus().equalsIgnoreCase("O"))
                .verifyComplete();
        Assertions.assertNotNull(csPtpSvc);

    }
    @Test
    void csPtpFailure() throws JsonProcessingException {

        mockBackEnd.enqueue(new MockResponse().setResponseCode(200).
                setBody(objectMapper.writeValueAsString(csPtpSvcServceTuxedoErrorResponse))
                .addHeader("Content-Type","application/json"));
        var csPtpSvc = csPtpService.getCsPtpSvc(CsPtpSvcRequest.builder().build(), RequestHeaders.builder().build());
        var message = Assertions.assertThrows(TuxedoServiceException.class, csPtpSvc::block).getMessage();

        Assertions.assertTrue(message.contains("Tuxedo Server Error"));

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
}
