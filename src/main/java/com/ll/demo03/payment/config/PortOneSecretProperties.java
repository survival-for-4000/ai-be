package com.ll.demo03.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "portone.secret")
public class PortOneSecretProperties {
    private String api;
    private String webhook;

    public String getApi() {
        return api;
    }
    public void setApi(String api) {
        this.api = api;
    }

    public String getWebhook() {
        return webhook;
    }
    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }
}
