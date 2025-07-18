package com.ll.demo03.domain.webhook;

public interface WebhookProcessor<T> {
    void processWebhookEvent(T event);
    String getStatus(T event);
    Object getResourceData(T event);
    boolean isCompleted(T event);
    boolean isResourceDataEmpty(Object resourceData);
}