package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Value
@Jacksonized
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RecommededPayment {

  String installmentBase;
  String profile;
  List<Integer> installmentPercentages;
  List<Double> installmentAmount;
}
