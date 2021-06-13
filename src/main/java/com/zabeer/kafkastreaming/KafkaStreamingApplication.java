package com.zabeer.kafkastreaming;

import com.zabeer.kafkastreaming.model.Item;
import com.zabeer.kafkastreaming.model.ItemPrice;
import com.zabeer.kafkastreaming.model.ItemProcessed;
import com.zabeer.kafkastreaming.model.ItemRetryTracker;
import com.zabeer.kafkastreaming.service.ItemRetryTrackerNotificationService;
import com.zabeer.kafkastreaming.service.StateStoreQueryService;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


@SpringBootApplication
@EnableScheduling
@Slf4j
public class KafkaStreamingApplication {

    public static final String STORE_NAME = "item-retry-count-store";

    @Autowired
    private ItemRetryTrackerNotificationService itemRetryTrackerNotificationService;

    @Autowired
    private StateStoreQueryService stateStoreQueryService;

    @Value("${spring.cloud.stream.kafka.streams.binder.configuration.schema.registry.url}")
    private String schemaRegistryUrl;


    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamingApplication.class, args);
    }


    @Bean
    public Consumer<KStream<String, ItemProcessed>> monitorProcessedRecords() {
        return input -> {
            input.peek(((key, value) -> log.info("key for ProcessedItem is {} and value {} ", key, value.toString())));
        };
    }


    @Bean
    public BiFunction<KStream<String, Item>, KTable<String, ItemPrice>, KStream<String, ItemProcessed>[]> itemProcessor() {

        Predicate<String, ItemProcessed> normalFlow = (k, v) -> v.getSequence() == null || v.getSequence().longValue() == 0L;
        Predicate<String, ItemProcessed> retryFlow = (k, v) -> v.getSequence() != null && v.getSequence().longValue() > 0L;

        return (itemStream, itemPriceTable) -> itemStream
                .leftJoin(itemPriceTable,
                        (item, price) -> new JoinResultHolder<>(item.getId().toString(), item, price))
                .map((key, value) -> {
                    //check if this needs to be forwarded to retry topic
                    //if price is null/0.0 then its not available in KTable and should be sent to retry topic
                    //if price is not null/0.0, but the lastMessageSentForRetry and lastMessageProcessedOnRetry are not equal then also sent to retry topic
                    //(if it's sent to retry topic then set sequence of message to lastMessageSentForRetry (default 0) + 1
                    //Also produce message to retryTracker to update the lastMessageSentForRetry)
                    //else this can be processed by this processor
                    long sequenceToSet = 0L;
                    if (value.getValue2() == null || value.getValue2().getPrice() == 0.0f) {
                        //sent event to update/notify ItemRetryTracker
                        //same condition will be checked later to branch of output message to retrytopic
                        ItemRetryTracker itemRetryTracker = stateStoreQueryService.getValue(value.getKey());
                        sequenceToSet = (itemRetryTracker == null ? 0L : itemRetryTracker.getLastMessageSentForRetry()) + 1;
                        itemRetryTrackerNotificationService.notifyItemRetryTrackerOnRetry(value.getKey(), sequenceToSet);
                        log.info("Item with id {} and sequence {} sent for retry " , value.getKey(), sequenceToSet);
                    } else {
                        ItemRetryTracker itemRetryTracker = stateStoreQueryService.getValue(value.getKey());
                        long lastMessageProcessedOnRetry = itemRetryTracker == null ? 0L : itemRetryTracker.getLastMessageSentForRetry();
                        long lastMessageSentForRetry = itemRetryTracker == null ? 0L : itemRetryTracker.getLastMessageProcessedOnRetry();
                        //TODO need additional conditon to check for some reasonable time interval since the lastMessageProcessed for new events to be processed directly
                        //without retry.
                        if (lastMessageProcessedOnRetry < lastMessageSentForRetry) {
                            //sent event to update/notify ItemRetryTracker
                            sequenceToSet = lastMessageSentForRetry + 1;
                            itemRetryTrackerNotificationService.notifyItemRetryTrackerOnRetry(value.getKey(), sequenceToSet);
                            log.info("Item with id {} and sequence {} sent for retry " , value.getKey(), sequenceToSet);
                        }
                        else {
                            log.info("Item with id {} processed without retry " , value.getKey());
                        }
                    }
                    return new KeyValue<>(key,
                            new ItemProcessed(value.getValue1().getId(), value.getValue1().getCode(), value.getValue1().getDescription(),
                                    value.getValue1().getItemTimestamp(), value.getValue2() == null ? 0.0f : value.getValue2().getPrice(), sequenceToSet));
                }).branch(normalFlow, retryFlow);


    }


    @Bean
    public BiFunction<KStream<String, ItemProcessed>, KTable<String, ItemPrice>, KStream<String, ItemProcessed>[]> itemRetryProcessor() {

        Predicate<String, ItemProcessed> normalFlow = (k, v) -> v.getSequence() == null || v.getSequence().longValue() == 0L;
        Predicate<String, ItemProcessed> retryFlow = (k, v) -> v.getSequence() != null && v.getSequence().longValue() > 0L;

        return (itemProcessedStream, itemPriceTable) -> itemProcessedStream
                .leftJoin(itemPriceTable,
                        (itemProcessed, price) -> new JoinResultHolder<>(itemProcessed.getId().toString(), itemProcessed, price))
                .map((key, value) -> {
                    //check if price is available (i.e. > 0.0f) and if messageSequence is equal to lastMessageProcessedOnRetry + 1
                    //if so process it forward
                    //else if price is not available (0.0f) or messageSequence not equal to lastMessageProcessedOnRetry + 1 put it again for re-try
                    boolean resentForRetry = true;
                    ItemRetryTracker itemRetryTracker = stateStoreQueryService.getValue(value.getKey());
                    long lastMessageProcessedOnRetry = itemRetryTracker == null ? 0L : itemRetryTracker.getLastMessageProcessedOnRetry();
                    if (value.getValue1().getPrice() > 0.0f && lastMessageProcessedOnRetry + 1 == value.getValue1().getSequence()) {
                        //can process the message as order is expected and wont cause out of order processing
                        resentForRetry = false;
                        //TODO since notification is published before the processing is finished there is chance the main processor can go ahead with some new items
                        //before the pending retries are completely processed.
                        itemRetryTrackerNotificationService.notifyItemRetryTrackerOnProcessed(value.getKey(), value.getValue1().getSequence());
                        log.info("Item with id {} and sequence {} processed on retry " , value.getKey(), value.getValue1().getSequence());
                    }
                    else {
                        log.info("Item with id {} and sequence {} being retried further " , value.getKey(), value.getValue1().getSequence());
                    }
                    return new KeyValue<>(key,
                            new ItemProcessed(value.getValue1().getId(), value.getValue1().getCode(), value.getValue1().getDescription(),
                                    value.getValue1().getItemTimestamp(), value.getValue2() == null ? 0.0f : value.getValue2().getPrice(), resentForRetry ? value.getValue1().getSequence() : 0L));
                }).branch(normalFlow, retryFlow);
    }


    @Bean
    public Function<KStream<String, ItemRetryTracker>, KStream<String, ItemRetryTracker>> itemProcessorRetryTracker() {

        return input -> input
                .groupByKey(Grouped.with(Serdes.String(), avroItemRetryTrackerInSerde())).aggregate(
                        () -> new ItemRetryTracker("", 0L, 0L),
                        ((key, value, aggregate) ->
                                new ItemRetryTracker(key,
                                        Math.max(value.getLastMessageSentForRetry(), aggregate.getLastMessageSentForRetry()),
                                        Math.max(value.getLastMessageProcessedOnRetry(), aggregate.getLastMessageProcessedOnRetry()))),
                        Materialized.<String, ItemRetryTracker, KeyValueStore<Bytes, byte[]>>as(STORE_NAME).withKeySerde(Serdes.String()).withValueSerde(avroItemRetryTrackerInSerde()))
                .toStream();
    }


    private Serde<ItemRetryTracker> avroItemRetryTrackerInSerde() {
        final SpecificAvroSerde<ItemRetryTracker> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();

        avroInSerde.configure(Collections.singletonMap(
                "schema.registry.url", schemaRegistryUrl), false);
        return avroInSerde;
    }


    private static final class JoinResultHolder<K, V1, V2> {

        private final K key;
        private final V1 value1;
        private final V2 value2;

        public JoinResultHolder(K key, V1 value1, V2 value2) {

            this.key = key;
            this.value1 = value1;
            this.value2 = value2;
        }


        public K getKey() {
            return key;
        }

        public V1 getValue1() {
            return value1;
        }

        public V2 getValue2() {
            return value2;
        }

    }


}
