package com.example.springBootWithRealcode.service;

import com.example.springBootWithRealcode.config.PTPConfig;
import com.example.springBootWithRealcode.model.PTPEligibilityPersistanceRecord;
import com.example.springBootWithRealcode.model.ValidationInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ValidationContext {

    private final ValidationInfo validationInfo;
    private final PTPConfig ptpConfig;
    private final PTPEligibilityPersistanceRecord ptpEligibilityPersistanceRecord;
}
