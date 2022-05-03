package com.example.springBootWithRealcode.repository;

import com.example.springBootWithRealcode.entity.InstallmentRulesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstallmentRuleRepository extends JpaRepository<InstallmentRuleRepository,String> {

    Optional<InstallmentRulesEntity> findByChannelNameAndFranchise(String channelName, String franchise);
}
