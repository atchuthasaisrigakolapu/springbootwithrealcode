package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Value
@Jacksonized
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ValidationConstraints {

    String code;
    String label;
    String otherInfo;

    @JsonIgnore
    boolean hardStop;
}
