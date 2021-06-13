package com.zabeer.kafkastreaming.service;

import com.zabeer.kafkastreaming.KafkaStreamingApplication;
import com.zabeer.kafkastreaming.model.ItemRetryTracker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StateStoreQueryServiceImpl implements StateStoreQueryService {


    @Autowired
    private InteractiveQueryService queryService;


    @Override
    public  ItemRetryTracker getValue(String key) {

        ReadOnlyKeyValueStore<String, ItemRetryTracker> keyValueStore = queryService.getQueryableStore(KafkaStreamingApplication.STORE_NAME, QueryableStoreTypes.keyValueStore());


        ItemRetryTracker itemRetryTracker = keyValueStore.get(key);
        log.info("ItemRetry Tracker fetched from store " + itemRetryTracker);
        return itemRetryTracker;

    }
}
