package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Value
@Jacksonized
@Accessors
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CollectionPathResponse {

    List<CollectionPathInfoList> collectionPathInfoList;
}
