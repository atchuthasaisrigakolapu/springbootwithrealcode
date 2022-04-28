package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Value
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PTPEligibilityResponse {
}
