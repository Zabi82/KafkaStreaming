package com.zabeer.kafkastreaming.service;

import com.zabeer.kafkastreaming.model.ItemRetryTracker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Configuration
@EnableAsync
@Service
@Slf4j
public class ItemRetryTrackerNotificationServiceImpl implements ItemRetryTrackerNotificationService {

    public static final String ITEM_TRACKER_TOPIC = "item-retry-tracker";

    @Override
    public void notifyItemRetryTrackerOnRetry(String itemId, Long retrySequence) {
        //while sending this send retrySequence as Received and set processed as 0
        produceItemTrackerEntry(itemId, retrySequence, 0L);
        log.info("Updated Retry Tracker for Item Id {} with retrySequence {} ", itemId , retrySequence);
    }

    @Override
    @Async
    public void notifyItemRetryTrackerOnProcessed(String itemId, Long processedSequence) {
        //while sending this send processedSequence as Received and set retry as 0
        produceItemTrackerEntry(itemId, 0L, processedSequence);
        log.info("Updated Retry Tracker for Item Id {} with processedSequence {} ", itemId, processedSequence);
    }


    private void produceItemTrackerEntry(String itemId, Long retrySequence, Long processedSequence) {

        Properties props = getProducerProperties();

        KafkaProducer<String, ItemRetryTracker> kafkaProducer = new KafkaProducer<>(props);

        ItemRetryTracker itemRetryTracker = new ItemRetryTracker();
        itemRetryTracker.setId(itemId);
        itemRetryTracker.setLastMessageSentForRetry(retrySequence);
        itemRetryTracker.setLastMessageProcessedOnRetry(processedSequence);
        ProducerRecord<String, ItemRetryTracker> record = new ProducerRecord<>(ITEM_TRACKER_TOPIC, itemRetryTracker.getId().toString(), itemRetryTracker);

        kafkaProducer.send(record, (recordMetaData, exception) -> {
            if (recordMetaData != null) {
                log.info("Produced message with key {} in topic {} and partition {}  with offset {} ",
                        itemRetryTracker.getId(), recordMetaData.topic(), recordMetaData.partition(), recordMetaData.offset());
            }
            else if(exception != null) {
                log.error("Exception sending message from producer ", exception);
                exception.printStackTrace();
            }
        });


        kafkaProducer.close();

    }

    private Properties getProducerProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://localhost:8081");
        return props;
    }

}
