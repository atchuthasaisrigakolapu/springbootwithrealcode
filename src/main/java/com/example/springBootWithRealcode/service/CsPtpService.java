package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.model.CsPtpSvcRequest;
import com.example.springBootWithRealcode.model.CsPtpSvcResponse;
import com.example.springBootWithRealcode.model.RequestHeaders;
import com.example.springBootWithRealcode.util.TuxedoProxyUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CsPtpService {

    private final TuxedoProxyUtil tuxedoProxyUtil;

   public Mono<CsPtpSvcResponse> getCsPtpSvc(CsPtpSvcRequest csPtpSvcRequest, RequestHeaders requestHeaders) {
       Map<String, Object> tuxedoRequestObject = tuxedoRequest(csPtpSvcRequest);
       return tuxedoProxyUtil.getTuxedoBuildResponse(requestHeaders, tuxedoRequestObject, "csPtpSvc00")
               .flatMap(tuxedoResponse -> {
                   log.info("{}-TuxedoResponse:{}", "csPtpSvc00", tuxedoResponse);
                   return Mono.just(CsPtpSvcResponse.fromTuxedoResponse(tuxedoResponse));
               });
   }

    private Map<String,Object> tuxedoRequest(CsPtpSvcRequest csPtpSvcRequest) {
        HashMap<String, Object> tuxedoInputHashMap = new HashMap<>();
        tuxedoInputHashMap.put("BAN",csPtpSvcRequest.getBan());
        tuxedoInputHashMap.put("CLA",csPtpSvcRequest.getCla());
        tuxedoInputHashMap.put("CLM_TAG", !StringUtils.hasLength(csPtpSvcRequest.getClmTag())?String.valueOf((byte)csPtpSvcRequest.getClmTag().charAt(0)):csPtpSvcRequest.getClmTag());
        tuxedoInputHashMap.put("RISK_CLASS",csPtpSvcRequest.getRiskClass());
        tuxedoInputHashMap.put("CREDIT_INPUT_IND",!StringUtils.hasLength(csPtpSvcRequest.getCreditInputInd())?String.valueOf((byte)csPtpSvcRequest.getCreditInputInd().charAt(0)):csPtpSvcRequest.getCreditInputInd());
        return tuxedoInputHashMap;
    }

}
