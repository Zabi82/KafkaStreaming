import com.zabeer.kafkastreaming.model.Item;
import com.zabeer.kafkastreaming.model.ItemPrice;
import com.zabeer.kafkastreaming.model.ItemRetryTracker;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestProducer {

	public static final String ITEM_TOPIC = "item";
	private static final String ITEM_PRICE_TOPIC = "item-price" ;
	private static final String ITEM_RETRY_TRACKER_TOPIC = "item-retry-tracker";

	public void produceItemMessage(String itemId, String itemCode, String description, int quantity) {

		Properties props = getProducerProperties();

		KafkaProducer<String, Item> kafkaProducer = new KafkaProducer<>(props);

		for (int i = 0; i < quantity; i++) {
			Item item = new Item();
			item.setId(itemId);
			item.setCode(itemCode);
			item.setDescription(description);
			item.setItemTimestamp(System.currentTimeMillis());
			ProducerRecord<String, Item> record = new ProducerRecord<>(ITEM_TOPIC, item.getId().toString(), item);

			kafkaProducer.send(record, (recordMetaData, exception) -> {
				if (recordMetaData != null) {
					System.out.printf("Produced message with key = %s in topic = %s and partition = %s with offset = %s \n",
							item.getId(), recordMetaData.topic(), recordMetaData.partition(), recordMetaData.offset());
				}
				else if(exception != null) {
					System.out.println("Exception occured " + exception);
					exception.printStackTrace();
				}
			});

		}

		kafkaProducer.close();

	}


	public void produceItemPriceMessage(String itemId, float price) {

		Properties props = getProducerProperties();

		KafkaProducer<String, ItemPrice> kafkaProducer = new KafkaProducer<>(props);
		ItemPrice itemPrice = new ItemPrice();
		itemPrice.setId(itemId);
		itemPrice.setPrice(price);

		ProducerRecord<String, ItemPrice> record = new ProducerRecord<>(ITEM_PRICE_TOPIC, itemPrice.getId().toString(), itemPrice);

		kafkaProducer.send(record, (recordMetaData, exception) -> {
			if (recordMetaData != null) {
				System.out.printf("Produced message with key = %s in topic = %s and partition = %s with offset = %s \n",
						itemPrice.getId(), recordMetaData.topic(), recordMetaData.partition(), recordMetaData.offset());
			}
			else if(exception != null) {
				System.out.println("Exception occured " + exception);
				exception.printStackTrace();
			}
		});

		kafkaProducer.close();

	}

	public void produceItemRetryTrackerMessage(String itemId, long lastMessageSentForRetry, long lastMessageProcessedOnRetry) {

		Properties props = getProducerProperties();

		KafkaProducer<String, ItemRetryTracker> kafkaProducer = new KafkaProducer<>(props);
		ItemRetryTracker itemRetryTracker = new ItemRetryTracker();
		itemRetryTracker.setId(itemId);
		itemRetryTracker.setLastMessageSentForRetry(lastMessageSentForRetry);
		itemRetryTracker.setLastMessageProcessedOnRetry(lastMessageProcessedOnRetry);
		itemRetryTracker.setTimeLastMessageSentForRetry(0L);
		itemRetryTracker.setTimeLastMessageProcessedOnRetry(0L);

		ProducerRecord<String, ItemRetryTracker> record = new ProducerRecord<>(ITEM_RETRY_TRACKER_TOPIC, itemRetryTracker.getId().toString(), itemRetryTracker);

		kafkaProducer.send(record, (recordMetaData, exception) -> {
			if (recordMetaData != null) {
				System.out.printf("Produced message with key = %s in topic = %s and partition = %s with offset = %s \n",
						itemRetryTracker.getId(), recordMetaData.topic(), recordMetaData.partition(), recordMetaData.offset());
			}
			else if(exception != null) {
				System.out.println("Exception occured " + exception);
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


	public SpecificAvroSerializer<Item> avroItemSerializer() {
		final SpecificAvroSerializer<Item> avroSer = new SpecificAvroSerializer<>();
		Map<String, Object> serdeProperties = new HashMap<>();
		return avroSer;
	}

	public static void main(String[] args) throws Exception {
		TestProducer producer = new TestProducer();
		producer.produceItemMessage("6","CakeBB", "Cake Blueberry", 3);
		//simulate delay in publishing Item Price
		Thread.sleep(500);
		producer.produceItemPriceMessage("6", 200);
		//simulate few items of the same type published after price available
		producer.produceItemMessage("6","CakeBB", "Cake Blueberry", 2);
		//producer.produceItemRetryTrackerMessage("TEST123", 1L, 0L);

	}
}