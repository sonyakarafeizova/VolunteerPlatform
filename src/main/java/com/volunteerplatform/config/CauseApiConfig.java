package com.volunteerplatform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix="causes.api")
public class CauseApiConfig {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public CauseApiConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}
