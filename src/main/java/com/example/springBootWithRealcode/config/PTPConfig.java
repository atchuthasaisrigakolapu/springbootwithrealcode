package com.example.springBootWithRealcode.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ptp-config")
@Component
@Data
public class PTPConfig {

    private int extendDays;
}
