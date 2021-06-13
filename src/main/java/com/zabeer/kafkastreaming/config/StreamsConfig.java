package com.zabeer.kafkastreaming.config;

import com.zabeer.kafkastreaming.model.Item;
import com.zabeer.kafkastreaming.model.ItemPrice;
import com.zabeer.kafkastreaming.model.ItemProcessed;
import com.zabeer.kafkastreaming.model.ItemRetryTracker;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StreamsConfig {


    @Bean
    public Serde<Item> avroItemInSerde() {
        final SpecificAvroSerde<Item> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();
        return avroInSerde;
    }

    @Bean
    public Serde<ItemPrice> avroItemPriceInSerde() {
        final SpecificAvroSerde<ItemPrice> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();
        return avroInSerde;
    }

    @Bean
    public Serde<ItemProcessed> avroItemProcessedInSerde() {
        final SpecificAvroSerde<ItemProcessed> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();
        return avroInSerde;
    }



    @Bean
    public Serde<ItemRetryTracker> avroItemRetryTrackerInSerde() {
        final SpecificAvroSerde<ItemRetryTracker> avroInSerde = new SpecificAvroSerde<>();
        Map<String, Object> serdeProperties = new HashMap<>();
        return avroInSerde;
    }

}
