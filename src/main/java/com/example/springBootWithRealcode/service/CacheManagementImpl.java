package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.entity.InstallmentRulesEntity;
import com.example.springBootWithRealcode.repository.InstallmentRuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@AllArgsConstructor
public class CacheManagementImpl {

    private final InstallmentRuleRepository installmentRuleRepository;

    @Cacheable(cacheNames="PTP_INSTALLMENT_RULES",key="(#channelName).concat(#franchise)")
    public Optional<InstallmentRulesEntity> fetchInstallmentRules(String channelName,String franchise){
        return installmentRuleRepository.findByChannelNameAndFranchise(channelName, franchise);
    }

}
