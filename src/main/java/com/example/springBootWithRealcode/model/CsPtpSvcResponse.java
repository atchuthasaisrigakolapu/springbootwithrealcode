package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Value
@Builder(toBuilder = true)
@Accessors
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CsPtpSvcResponse {

    String eligibilityInd;

    Integer numberOfBrokenPtp;

    String ippInd;

    Double postDueAmount;

    Double mimArrangeMent;

    Double recArrangeMent;

    Double arBalance;

    Double overDueBalance;

    Integer debtAge;

    Double oldestDebtBalance;

    String region;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
     LocalDate depositDate;

    Double originalAmount;

    Double unBilledUsageAmount;

    Double pendingChargeCreditAmount;

    String banStatus;

    String ptpNCatRiskAcc;

    String ptpBrokenFromHistory;

    Double cla;

    String clmTag;

    String riskClass;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime localUpdateDate;

    String lastUpdateStamp;

    PTPEligibilityHistoryInfo ptpInfo;

    List<PTPParam> ptpParams;

    public static CsPtpSvcResponse fromTuxedoResponse(TuxedoResponse response){

        response.checkErrors();

        var ptpRisk = response.<String>getAsList("PTP_RISK");
        var ptp_broken_promises = response.<String>getAsList("PTP_BROKEN_PROMISES");
        var ptp_is_clm = response.<String>getAsList("PTP_IS_CLM");
        var ptp_type = response.<String>getAsList("PTP_TYPE");
        var  ptp_installements = response.<Integer>getAsList("PTP_INSTALLEMENTS");
        var  ptp_max_days_1ST_inst = response.<Integer>getAsList("PTP_MAX_DAYS_1ST_INST");
        var ptp_max_days_last_inst = response.<Integer>getAsList("PTP_MAX_DAYS_LAST_INST");

        var ptpi_seq_no = response.<Integer>getAsList("PTPI_SEQ_NO");
        var ptpi_item = response.<Integer>getAsList("PTPI_ITEM");
        var ptpi_date_promised = response.<String>getAsList("PTPI_DATE_PROMISED");
        var ptpi_date_estimated_posting = response.<String>getAsList("PTPI_DATE_ESTIMATED_POSTING");

        var ptpi_method = response.<String>getAsList("PTPI_METHOD");
        var ptpi_amount = response.<Double>getAsList("PTPI_AMOUNT");
        var  ptpi_status = response.<String>getAsList("PTPI_STATUS");
        var ptpi_date_fulfilled = response.<String>getAsList("PTPI_DATE_FULFILLED");
        var ptpi_amount_fulfilled = response.<Double>getAsList("PTPI_AMOUNT_FULFILLED");
        var ptpi_payment_sequence = response.<Integer>getAsList("PTPI_PAYMENT_SEQUENCE");
        var ptpitemrowcount = response.<Integer>getFirst("PTPITEMROWCOUNT");
        var rowcount = response.<Integer>getFirst("ROWCOUNT");

        return CsPtpSvcResponse.builder()
                .eligibilityInd(TuxedoResponse.toString(response.getFirst("PTP_ELIGIBILITY_IND")))
                .numberOfBrokenPtp(response.getFirst("NUM_OF_BROKEN_PTP"))
                .ippInd(TuxedoResponse.toString(response.getFirst("IPP_IND")))
                .postDueAmount(response.getFirst("PAST_DUE_AMT"))
                .mimArrangeMent(response.getFirst("MINARRAGEMENT"))
                .recArrangeMent(response.getFirst("RECARRAGEMENT"))
                .arBalance(response.getFirst("ARBALANCE"))
                .overDueBalance(response.getFirst("OVERDUEBALANCE"))
                .debtAge(response.getFirst("DEBTAGE"))
                .oldestDebtBalance(response.getFirst("OLDESTDEBTBALANCE"))
                .region(response.getFirst("COL_REGION"))
                .depositDate(TuxedoResponse.parse(response.getFirst("DEPOSITE_DATE")))
                .originalAmount(response.getFirst("ORIGINAL_AMOUNT"))
                .unBilledUsageAmount(response.getFirst("UNBILLED_USAGE_AMT"))
                .pendingChargeCreditAmount(response.getFirst("PENDING_CHRG_CRD_AMT"))
                .banStatus(TuxedoResponse.toString(response.getFirst("BAN_STATUS")))
                .localUpdateDate(TuxedoResponse.parseDateTime(response.getFirst("LAST_UPDATE_DATE")))
                .lastUpdateStamp(response.getFirst("LAST_UPDATE_STAMP").toString())
                .ptpNCatRiskAcc(response.getFirst("PTP_N_CAT_RISK_ACC"))
                .ptpBrokenFromHistory(response.getFirst("PTP_BROKEN_FROM_HISTORY"))
                .cla(response.getFirst("CLA"))
                .clmTag(response.getFirst("CLM_TAG"))
                .riskClass(response.getFirst("RISK_CLASS"))
                .ptpParams(IntStream.range(0, Objects.requireNonNullElse(rowcount, 0))
                        .mapToObj(i -> PTPParam.builder()
                                .risk(ptpRisk.get(i))
                                .brokenPromises(ptp_broken_promises.get(i))
                                .isClm(TuxedoResponse.toString(ptp_is_clm.get(i)))
                                .type(TuxedoResponse.toString(ptp_type.get(i)))
                                .installments(ptp_installements.get(i))
                                .maxDays1stInst(ptp_max_days_1ST_inst.get(i))
                                .maxDaysLastInst(ptp_max_days_last_inst.get(i))
                                .build())
                        .collect(Collectors.toList()))
                .ptpInfo(PTPEligibilityHistoryInfo.builder()
                        .ptpStatus(TuxedoResponse.toString(response.getFirst("PTP_STATUS")))
                        .ptpSeqNo(response.getFirst("PTP_SEQ_NO").toString())
                        .ptpOldPtp(response.getFirst("PTP_OLD_PTP").toString())
                        .ptpDate(TuxedoResponse.parse(response.getFirst("PTP_DATE")))
                        .ptpRestore(TuxedoResponse.toString(response.getFirst("PTP_RESTORE")))
                        .ptpCategory(TuxedoResponse.toString(response.getFirst("PTP_CATEGORY")))
                        .ptpRegion(response.getFirst("PTP_REGION"))
                        .ptpArBalance(response.getFirst("PTP_AR_BALANCE"))
                        .ptpOverDueBalance(response.getFirst("PTP_OVERDUE_BALANCE"))
                        .ptpBanStatus(TuxedoResponse.toString(response.getFirst("PTP_BAN_STATUS")))
                        .ptpLastPaymentDate(TuxedoResponse.parse(response.getFirst("PTP_LAST_PAYMENT_DATE")))
                        .ptpLastPaymentAmount(response.getFirst("PTP_LAST_PAYMENT_AMOUNT"))
                        .ptpDebtAge(response.getFirst("PTP_DEBT_AGE"))
                        .ptpBalanceOldestDebt(response.getFirst("PTP_BALANCE_OLDEST_DEBT"))
                        .ptpCla(response.getFirst("PTP_CLA"))
                        .ptpTotalBilledUnbilled(response.getFirst("PTP_TOTAL_BILLED_UNBILLED"))
                        .ptpInstallments(IntStream.range(0,Objects.requireNonNullElse(ptpitemrowcount,0))
                                .mapToObj(i->PTPEligibilityInstallmentInfo
                                        .builder()
                                        .ptpISeqNo(ptpi_seq_no.get(i).toString())
                                        .ptpIItem(ptpi_item.get(i))
                                        .ptpIDatePromised(TuxedoResponse.parse(ptpi_date_promised.get(i)))
                                        .ptpIDateEstimatedPosting(TuxedoResponse.parse(ptpi_date_estimated_posting.get(i)))
                                        .ptpIMethod(ptpi_method.get(i))
                                        .ptpIAmount(ptpi_amount.get(i))
                                        .ptpIStatus(TuxedoResponse.toString(ptpi_status.get(i)))
                                        .ptpIDateFulfilled(TuxedoResponse.parse(ptpi_date_promised.get(i)))
                                        .ptpIAmountFulfilled(ptpi_amount_fulfilled.get(i))
                                        .ptpIPaymentSequence(ptpi_payment_sequence.get(i).toString())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .build();




    }

}
