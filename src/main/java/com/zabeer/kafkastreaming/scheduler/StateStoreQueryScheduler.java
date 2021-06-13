package com.zabeer.kafkastreaming.scheduler;

import com.zabeer.kafkastreaming.KafkaStreamingApplication;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class StateStoreQueryScheduler {


    @Autowired
    private InteractiveQueryService queryService;



    ReadOnlyKeyValueStore<Object, Object> keyValueStore;


    //@Scheduled(fixedRate = 2000, initialDelay = 1000)
    public void printItemRetryStats() {
        if (keyValueStore == null) {
            //keyValueStore = queryService.getQueryableStore("KSTREAM-AGGREGATE-STATE-STORE-0000000001", QueryableStoreTypes.keyValueStore());
            keyValueStore = queryService.getQueryableStore(KafkaStreamingApplication.STORE_NAME, QueryableStoreTypes.keyValueStore());
        }


        System.out.println("State Store Key: " + "TEST123" + " Count: " + keyValueStore.get("TEST123"));

    }
}
