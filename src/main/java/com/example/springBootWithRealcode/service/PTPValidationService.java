package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.config.PTPConfig;
import com.example.springBootWithRealcode.config.RedisClientPTPEligibility;
import com.example.springBootWithRealcode.exception.BadRequestException;
import com.example.springBootWithRealcode.exception.CacheExpiredException;
import com.example.springBootWithRealcode.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Validated
public class PTPValidationService {

    private final RedisClientPTPEligibility redisClientPTPEligibility;
    private final PTPConfig ptpConfig;
    private final  PTPValidations ptpValidations;

    public PTPValidationResponse validatePTP(@Valid PTPValidationRequest ptpValidationRequest,String ban){

        validateRequest(ptpValidationRequest);
        var ptpId = PTPOperationEnum.CREATE.equals(ptpValidationRequest.getOperation()) ? "O" : ptpValidationRequest.getPtpId();
        var eligibiityPersistanceRecord = getEligibilityPersistanceRecord(ptpValidationRequest.getRefId(),ban,ptpId,ptpValidationRequest.getOperation());
        validatePtpItemDates(ptpValidationRequest,eligibiityPersistanceRecord);
        var ptpItems = sortPtpItems(ptpValidationRequest);
        ptpValidationRequest = ptpValidationRequest.toBuilder()
                .ptpItems(ptpItems)
                .build();
        var validationInfo = findValidationInfo(eligibiityPersistanceRecord, ptpValidationRequest);
        var ptpValidationResponse = ptpValidations.validate(new ValidationContext(validationInfo,ptpConfig,eligibiityPersistanceRecord));
        updateValidationResultsInRedis(eligibiityPersistanceRecord,ptpValidationRequest,ptpValidationResponse);
        return ptpValidationResponse;
    }

    private void updateValidationResultsInRedis(PTPEligibilityPersistanceRecord eligibiityPersistanceRecord, PTPValidationRequest ptpValidationRequest, PTPValidationResponse ptpValidationResponse) {
        PTPEligibilityPersistanceRecord build = eligibiityPersistanceRecord.toBuilder()
                .ptpValidationRequest(ptpValidationRequest)
                .ptpValidationResponse(ptpValidationResponse)
                .build();
        var  redisKey = Optional.ofNullable(eligibiityPersistanceRecord.getRefId()).orElse(eligibiityPersistanceRecord.getBan());
        redisClientPTPEligibility.put(redisKey,build);

    }

    private ValidationInfo findValidationInfo(PTPEligibilityPersistanceRecord eligibiityPersistanceRecord, PTPValidationRequest ptpValidationRequest) {
        var validationInfo = ValidationInfo.builder().build();
        for (PTPParam ptpParam : eligibiityPersistanceRecord.getPtpEligbilityDetails().getPtpParams()) {
            if ("A".equalsIgnoreCase(ptpParam.getType())) {
                validationInfo = validationInfo.toBuilder()
                        .recordAMax1stInstallationDate(LocalDate.now(ZoneId.of("America/Toronoto")).plusDays(ptpParam.getMaxDays1stInst()))
                        .recordAMaxLastInstallationDate(LocalDate.now(ZoneId.of("America/Toronoto")).plusDays(ptpParam.getMaxDaysLastInst()))
                        .recordAMaxInstallationments(ptpParam.getInstallments())
                        .build();
            }
            if ("B".equalsIgnoreCase(ptpParam.getType())) {
                validationInfo = validationInfo.toBuilder()
                        .recordBMax1stInstallationDate(LocalDate.now(ZoneId.of("America/Toronoto")).plusDays(ptpParam.getMaxDays1stInst()))
                        .recordBMaxLastInstallationDate(LocalDate.now(ZoneId.of("America/Toronoto")).plusDays(ptpParam.getMaxDaysLastInst()))
                        .recordBMaxInstallationments(ptpParam.getInstallments())
                        .build();
            }

        }
        var ptpItems = ptpValidationRequest.getPtpItems();
        validationInfo.toBuilder()
                .numberOfInstallements(ptpItems.size())
                .firstEstimatedPostingDate(ptpItems.get(0).getDateEstimatedPosting())
                .lastEstimatedPostingDate(ptpItems.get(ptpItems.size() - 1).getDateEstimatedPosting())
                .ptpItems(ptpItems)
                .ptpRisk(eligibiityPersistanceRecord.getPtpEligbilityDetails().getPtpParams().get(0).getRisk())
                .ptpNCatRiskAcc(eligibiityPersistanceRecord.getPtpEligbilityDetails().getPtpNCatRiskAcc())
                .build();
        var totalInstallmentAmount = ptpItems.stream()
                .map(PTPItems::getAmount)
                .reduce(0.0, Double::sum);
        var ptpCategory = findPtpCategory(validationInfo);
        LocalDate extendDate = null;
        if (eligibiityPersistanceRecord.getPtpOperatonsEligibilityList().stream()
                .anyMatch(eligibility -> (PTPOperationEnum.RESTORE.toString().equals(eligibility.getPtpOperation())
                        || PTPOperationEnum.EXTEND.toString().equals(eligibility.getPtpOperation()))
                        && "Y".equals(eligibility.getEligibilityInd())))
            extendDate = validationInfo.getLastEstimatedPostingDate().plusDays(ptpConfig.getExtendDays());
        return validationInfo.toBuilder()
                .totalInstallmentAmount(totalInstallmentAmount)
                .ptpCategory(ptpCategory)
                .extendDate(extendDate)
                .operation(ptpValidationRequest.getOperation())
                .restoreEligible(findRestoreExtendEligibile(eligibiityPersistanceRecord, PTPOperationEnum.RESTORE))
                .extendEligible(findRestoreExtendEligibile(eligibiityPersistanceRecord, PTPOperationEnum.EXTEND))
                .build();


    }
    private boolean findRestoreExtendEligibile(PTPEligibilityPersistanceRecord eligibiityPersistanceRecord, PTPOperationEnum operationEnum) {
        return  eligibiityPersistanceRecord.getPtpOperatonsEligibilityList().stream()
                .anyMatch(eligibility->operationEnum.toString().equalsIgnoreCase(eligibility.getPtpOperation())
                        &&"Y".equalsIgnoreCase(eligibility.getEligibilityInd()));
    }

    private String findPtpCategory(ValidationInfo validationInfo) {

        var ptpNCatRiskAcc = validationInfo.getPtpNCatRiskAcc();
        if(Objects.nonNull(ptpNCatRiskAcc) && Arrays.asList(ptpNCatRiskAcc.split(",")).contains(validationInfo.getPtpRisk()))
            return "N";
        if(!validationInfo.getFirstEstimatedPostingDate().isAfter(validationInfo.getRecordAMax1stInstallationDate())
                && !validationInfo.getLastEstimatedPostingDate().isAfter(validationInfo.getRecordAMaxLastInstallationDate())
        &&validationInfo.getNumberOfInstallements()<= validationInfo.getRecordAMaxInstallationments())
            return "A";
        if(!validationInfo.getFirstEstimatedPostingDate().isAfter(validationInfo.getRecordBMax1stInstallationDate())
                && !validationInfo.getLastEstimatedPostingDate().isAfter(validationInfo.getRecordBMaxLastInstallationDate())
                &&validationInfo.getNumberOfInstallements()<= validationInfo.getRecordBMaxInstallationments())
            return "B";
        return "N";
    }

    private List<PTPItems> sortPtpItems(PTPValidationRequest ptpValidationRequest) {

        return ptpValidationRequest.getPtpItems().stream()
                .sorted(Comparator.comparing(PTPItems::getDateEstimatedPosting))
                .collect(Collectors.toList());

    }

    private void validatePtpItemDates(PTPValidationRequest ptpValidationRequest, PTPEligibilityPersistanceRecord eligibiityPersistanceRecord) {

        var ptpItems = ptpValidationRequest.getPtpItems();
        LocalDate today = LocalDate.now(ZoneId.of("America/Toronta"));
        var  inValid = ptpItems.stream().anyMatch(ptpItem -> {
            if (PTPOperationEnum.UPDATE.equals(ptpValidationRequest.getOperation())) {
                var ptpInfo = eligibiityPersistanceRecord.getPtpEligbilityDetails().getPtpInfo();
                var installmentInfoOpt = ptpInfo.getPtpInstallments().stream()
                        .filter(ptpInstallment -> String.valueOf(ptpInstallment.getPtpIItem()).equalsIgnoreCase(ptpItem.getItem()))
                        .findFirst();
                if (installmentInfoOpt.isPresent()) {
                    var installmentInfo = installmentInfoOpt.get();
                    if (!"P".equalsIgnoreCase(installmentInfo.getPtpIStatus()))
                        return false;
                    if (ptpItem.getDatePromised().equals(installmentInfoOpt.get().getPtpIDatePromised()))
                        return ptpItem.getDateEstimatedPosting().isBefore(today);
                }
            }
            return ptpItem.getDatePromised().isBefore(today) || ptpItem.getDateEstimatedPosting().isBefore(today);

        });
        if(inValid)
            throw new BadRequestException("Bad request","PTP installment dates are backdated or posting date is before installment date");

    }

    private PTPEligibilityPersistanceRecord getEligibilityPersistanceRecord(String refId, String ban, String ptpId, PTPOperationEnum operation) {

        var ptpEligibilityPersistanceRecord = redisClientPTPEligibility.get(refId);
        if(ptpEligibilityPersistanceRecord == null)
            throw new CacheExpiredException("cache expired","cache is expired or not available");
        if(!ban.equalsIgnoreCase(ptpEligibilityPersistanceRecord.getBan()))
            throw new CacheExpiredException("Bad request","Ban is mismatch with eligibility reference");
        if(ObjectUtils.isEmpty(ptpEligibilityPersistanceRecord.getPtpEligbilityDetails())){
            var eligibilityPtpId  = ptpEligibilityPersistanceRecord.getPtpEligbilityDetails().getPtpInfo() != null ?
                    ptpEligibilityPersistanceRecord.getPtpEligbilityDetails().getPtpInfo().getPtpSeqNo() : "O";
            if(!"O".equalsIgnoreCase(ptpId) && !eligibilityPtpId.equalsIgnoreCase(ptpId))
                throw new BadRequestException("Bad request", "PTP_ID mismatch with eligibilityReference");

        }
        if(eligibilityFailed(ptpEligibilityPersistanceRecord,operation))
            throw new BadRequestException("Bad request","Unable to proceed as eligibility indicator N for requested operation");


        return ptpEligibilityPersistanceRecord;

    }

    private boolean eligibilityFailed(PTPEligibilityPersistanceRecord ptpEligibilityPersistanceRecord, PTPOperationEnum operation) {

        return
        ptpEligibilityPersistanceRecord.getPtpOperatonsEligibilityList().stream()
                .noneMatch(eligibility->eligibility.getPtpOperation().equalsIgnoreCase(operation.toString())&&("Y".equalsIgnoreCase(eligibility.getEligibilityInd())));
    }

    private void validateRequest(PTPValidationRequest ptpValidationRequest) {
        if (!List.of(PTPOperationEnum.CREATE, PTPOperationEnum.UPDATE).contains(ptpValidationRequest.getOperation()))
            throw new BadRequestException("Bad request","validation not implemented");
        if(PTPOperationEnum.UPDATE.equals(ptpValidationRequest.getOperation()) && ObjectUtils.isEmpty(ptpValidationRequest.getPtpId())){
            throw new BadRequestException("Bad request","PTP Id is required for update operation");
        }

    }

}
