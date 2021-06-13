package com.zabeer.kafkastreaming.service;

import com.zabeer.kafkastreaming.model.ItemRetryTracker;

public interface StateStoreQueryService {
    ItemRetryTracker getValue(String key);
}
