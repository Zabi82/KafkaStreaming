package com.zabeer.kafkastreaming.service;

public interface ItemRetryTrackerNotificationService {

    void notifyItemRetryTrackerOnRetry(String itemId, Long retrySequence);

    void notifyItemRetryTrackerOnProcessed(String itemId, Long processedSequence);
}
