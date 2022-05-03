package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.config.RedisClientPTPEligibility;
import com.example.springBootWithRealcode.entity.InstallementConfiguration;
import com.example.springBootWithRealcode.entity.InstallmentRulesEntity;
import com.example.springBootWithRealcode.exception.TuxedoServiceException;
import com.example.springBootWithRealcode.model.*;
import com.example.springBootWithRealcode.service.eligibility.CommonEligibility;
import com.example.springBootWithRealcode.service.eligibility.EligibilityContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class PTPEligibilityService {

    private final CreditEngineService creditEngineService;

    private final CsPtpService csPtpService;

    private final AccountService accountService;

    private final CollectionInfoService collectionInfoService;

    private final CollectionPathService collectionPathService;

    private final RedisClientPTPEligibility redisClientPTPEligibility;

    private final CacheManagementImpl cacheManagementImpl;

    private final CommonEligibility eligibility;

    private final ObjectMapper objectMapper;


    public PTPEligibilityResponse ptpEligibility(PTPEligibilityRequest ptpEligibilityRequest, RequestHeaders requestHeaders) {

        var ban = ptpEligibilityRequest.getBan();

        CreditResultResponse creditEngineResponse;
        try {
            creditEngineResponse = creditEngineService.getCreditResults(ban, requestHeaders);
            log.info("Credit Engine Response: {}", creditEngineResponse);
        } catch (Exception e) {
            creditEngineResponse = CreditResultResponse.builder()
                    .success(false)
                    .build();
        }
        var csPtpSvcRequest = populateCsPtpScResquest(ban, creditEngineResponse);
        var csPtpSvcResponse = csPtpService.getCsPtpSvc(csPtpSvcRequest, requestHeaders).block();
        log.info("Eligibility Tuxedo Service Response:{}", csPtpSvcResponse);
        if (Objects.isNull(csPtpSvcResponse) && Objects.isNull(csPtpSvcResponse.getPtpParams())) {
            throw new TuxedoServiceException("Tuxedo Error", "Invalid response");
        }
        AccounInfoResponse accounInfoResponse = null;
        if (!Objects.isNull(ptpEligibilityRequest.getIncludeInfoList())
                && ptpEligibilityRequest.getIncludeInfoList().contains(PTPIncludeInfoEnum.ACCUOUNT_INFO.getValue())
                || ptpEligibilityRequest.getIncludeInfoList().contains(PTPOperationEnum.RESTORE)
                || ptpEligibilityRequest.getIncludeInfoList().contains(PTPOperationEnum.EXTEND)) {
            var accountDetails = accountService.getAccountDetails(ban, requestHeaders);
            if (ObjectUtils.isEmpty(accountDetails))
                throw new TuxedoServiceException("Tuxedo Error", "Invalid response");

            log.info("Account -ms Response : {}", accounInfoResponse);
        }
        CollectionInfoResponse collectionInfoResponse = null;
        CollectionPathResponse collectionPathResponse = null;
        if (!Objects.isNull(ptpEligibilityRequest.getIncludeInfoList())
                && ptpEligibilityRequest.getIncludeInfoList().contains(PTPIncludeInfoEnum.COLLECTION_INFO.getValue())
                || ptpEligibilityRequest.getIncludeInfoList().contains(PTPOperationEnum.RESTORE)
                || ptpEligibilityRequest.getIncludeInfoList().contains(PTPOperationEnum.EXTEND)) {
            var collectionInfo = collectionInfoService.getCollectionInfoDetails(ban, requestHeaders);
            if (ObjectUtils.isEmpty(collectionInfo))
                throw new TuxedoServiceException("Tuxedo Error", "Invalid response");
            log.info("CollectionInfo -ms Response : {}", collectionInfo);
            if(invokeCollectionPath(collectionInfoResponse,csPtpSvcResponse)){
               var riskClass =  creditEngineResponse.isSuccess() && !ObjectUtils.isEmpty(creditEngineResponse.getRiskClass())
                        ?creditEngineResponse.getRiskClass():csPtpSvcResponse.getRiskClass();
               var collectionPathDetails = collectionPathService.getCollectionPathDetails(ban, collectionInfoResponse.getColPathCode(), riskClass, requestHeaders);
                log.info("CollectionPath -ms Response : {}", collectionPathDetails);
            }
        }
        List<RecommededPayment> recommededPaymentResponse = null;
        if(!ObjectUtils.isEmpty(ptpEligibilityRequest.getIncludeInfoList())
                && ptpEligibilityRequest.getIncludeInfoList().contains(PTPIncludeInfoEnum.RECOMMEDATION_INFO.getValue())){

            try{
                var installementConfiguration = cacheManagementImpl.fetchInstallmentRules(requestHeaders.getApplicationId(), requestHeaders.getFranchise())
                        .map(InstallmentRulesEntity::getInstallementConfiguration).
                        orElse(null);
                getInstallementAmount(installementConfiguration,csPtpSvcResponse);

            }
            catch(Exception e){

                log.error("recommended payments catch :{}",e);
            }

        }
        var ptpEligibilityResponse = eligibility.eligibilityCheck(new EligibilityContext(ptpEligibilityRequest, creditEngineResponse, csPtpSvcResponse, accounInfoResponse, collectionInfoResponse, collectionPathResponse, recommededPaymentResponse));
        if("Y".equalsIgnoreCase(ptpEligibilityRequest.getEnableCache())){
            populateRedis(ptpEligibilityResponse,accounInfoResponse,collectionPathResponse,recommededPaymentResponse);
        }
        var ptpParams = excludeInfo(ptpEligibilityRequest.getExcludeInfoList(), PTPExcludeInfoEnum.PTP_PARAM) ? null : ptpEligibilityResponse.getCsPtpSvcResponse().getPtpParams();
        var ptpInfo = excludeInfo(ptpEligibilityRequest.getExcludeInfoList(), PTPExcludeInfoEnum.PTP_PARAM) ? null : ptpEligibilityResponse.getCsPtpSvcResponse().getPtpInfo();
        return ptpEligibilityResponse.toBuilder()
                .refId("Y".equalsIgnoreCase(ptpEligibilityRequest.getEnableCache())?ptpEligibilityResponse.getRefId():null)
                .accounInfoResponse(includeInfo(ptpEligibilityRequest.getIncludeInfoList(),PTPIncludeInfoEnum.ACCUOUNT_INFO)?ptpEligibilityResponse.getAccounInfoResponse():null)
                .collectionInfoResponse(includeInfo(ptpEligibilityRequest.getIncludeInfoList(),PTPIncludeInfoEnum.COLLECTION_INFO)?ptpEligibilityResponse.getCollectionInfoResponse():null)
                .recommededPaymentsList(includeInfo(ptpEligibilityRequest.getIncludeInfoList(),PTPIncludeInfoEnum.RECOMMEDATION_INFO)?null:ptpEligibilityResponse.getRecommededPaymentsList())
                .creditInfo(excludeInfo(ptpEligibilityRequest.getExcludeInfoList(),PTPExcludeInfoEnum.CREDIT_INFO)?null:ptpEligibilityResponse.getCreditInfo())
                .csPtpSvcResponse(excludeInfo(ptpEligibilityRequest.getExcludeInfoList(),PTPExcludeInfoEnum.PTP_ELIGIBILITY_DETAILS)?null:ptpEligibilityResponse.getCsPtpSvcResponse().toBuilder()
                        .ptpParams(ptpParams)
                        .ptpInfo(ptpInfo)
                        .build())
                .build();
    }

    private void populateRedis(PTPEligibilityResponse ptpEligibilityResponse, AccounInfoResponse accounInfoResponse, CollectionPathResponse collectionPathResponse, List<RecommededPayment> recommededPaymentResponse) {
        var ptpEligibilityPersistanceRecordBuilder = objectMapper.convertValue(ptpEligibilityResponse, PTPEligibilityPersistanceRecord.class).toBuilder()
                .accounInfo(accounInfoResponse)
                .collectionPathResponse(collectionPathResponse)
                .recommededPayments(recommededPaymentResponse)
                .build();
        log.info("Save details with transaction Id{} in redis",ptpEligibilityResponse.getRefId());
        redisClientPTPEligibility.put(ptpEligibilityResponse.getRefId(),ptpEligibilityPersistanceRecordBuilder);
        log.info("PTP eligibility details saved successfully in Redis");

    }

    private void getInstallementAmount(InstallementConfiguration installementConfiguration, CsPtpSvcResponse csPtpSvcResponse) {
        if(installementConfiguration != null && installementConfiguration.getRecommededPayments() != null){
            for(RecommededPayment recommededPayment:installementConfiguration.getRecommededPayments()){
               Double totalAmount =  csPtpSvcResponse.getPostDueAmount() != null ? csPtpSvcResponse.getPostDueAmount() : 0.0;
               //DoubleRounder.rounder(totalAmount/100)*recommededPayment.getInstallmentPercentages().get(0),2);
                Double amount1 = (totalAmount/100)*recommededPayment.getInstallmentPercentages().get(0);
                double amount2 = Math.abs(totalAmount - amount1);
                recommededPayment.setInstallmentAmount(List.of(amount1,amount2));
            }
        }
    }

    private boolean invokeCollectionPath(CollectionInfoResponse collectionInfoResponse,CsPtpSvcResponse csPtpSvcResponse) {

        return !ObjectUtils.isEmpty(collectionInfoResponse)&&!ObjectUtils.isEmpty(collectionInfoResponse.getColPathCode())
                &&!ObjectUtils.isEmpty(csPtpSvcResponse)&&!ObjectUtils.isEmpty(csPtpSvcResponse.getRiskClass());
    }

    private CsPtpSvcRequest populateCsPtpScResquest (String ban, CreditResultResponse creditEngineResponse){
            if (Objects.nonNull(creditEngineResponse)
                    && creditEngineResponse.isSuccess()
                    && Objects.nonNull(creditEngineResponse.getCla())
                    && Objects.nonNull(creditEngineResponse.getClmTag())
                    && Objects.nonNull(creditEngineResponse.getRiskClass())) {
                return CsPtpSvcRequest.builder()
                        .ban(Integer.valueOf(ban))
                        .creditInputInd("Y")
                        .cla(creditEngineResponse.getCla())
                        .clmTag(creditEngineResponse.getClmTag())
                        .riskClass(creditEngineResponse.getRiskClass())
                        .build();
            } else {
                return CsPtpSvcRequest.builder()
                        .ban(Integer.valueOf(ban))
                        .creditInputInd("N")
                        .build();
            }

        }
        private boolean includeInfo(List<String> includeList,PTPIncludeInfoEnum ptpIncludeInfoEnum){
        if(ObjectUtils.isEmpty(includeList))
            return false;
        else
            return includeList.contains(ptpIncludeInfoEnum);
        }
    private boolean excludeInfo(List<String> includeList,PTPExcludeInfoEnum ptpExcludeInfoEnum){
        if(ObjectUtils.isEmpty(includeList))
            return false;
        else
            return includeList.contains(ptpExcludeInfoEnum);
    }

    }

