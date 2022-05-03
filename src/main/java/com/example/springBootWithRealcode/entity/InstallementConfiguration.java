package com.example.springBootWithRealcode.entity;

import com.example.springBootWithRealcode.model.RecommededPayment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Value
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstallementConfiguration {
    List<RecommededPayment> recommededPayments;
}
