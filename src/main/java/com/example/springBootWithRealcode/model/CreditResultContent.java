package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditResultContent {

     CreditResultContentCreditInfo creditInfo;
     CreditResultContentCreditOptionObject creditOptionInfoObject;
     String clmTag;
}
