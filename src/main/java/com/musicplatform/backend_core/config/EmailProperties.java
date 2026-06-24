package com.musicplatform.backend_core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.email")
public class EmailProperties {

    private String provider = "log";
    private String from = "noreply@music-platform.local";
    private int verificationExpiryHours = 24;
    private String frontendBaseUrl = "http://localhost:3000";
    private String resendApiKey = "";

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getVerificationExpiryHours() {
        return verificationExpiryHours;
    }

    public void setVerificationExpiryHours(int verificationExpiryHours) {
        this.verificationExpiryHours = verificationExpiryHours;
    }

    public String getFrontendBaseUrl() {
        return frontendBaseUrl;
    }

    public void setFrontendBaseUrl(String frontendBaseUrl) {
        this.frontendBaseUrl = frontendBaseUrl;
    }

    public String getResendApiKey() {
        return resendApiKey;
    }

    public void setResendApiKey(String resendApiKey) {
        this.resendApiKey = resendApiKey;
    }
}
