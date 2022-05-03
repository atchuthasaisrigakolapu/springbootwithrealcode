package com.example.springBootWithRealcode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PTPIncludeInfoEnum {

    ACCUOUNT_INFO("AccountInfo"),
    COLLECTION_INFO("CollectioInfo"),
    RECOMMEDATION_INFO("RecommedationInfo");
    private final String value;
    PTPIncludeInfoEnum(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
