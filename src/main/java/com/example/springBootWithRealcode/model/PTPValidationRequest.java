package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Builder(toBuilder = true)
@Value
@Jacksonized
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PTPValidationRequest implements Serializable {

    String refId;
    @NotNull
     PTPOperationEnum operation;
    String  userText;
    String ptpId;
    List<PTPItems> ptpItems;
}
