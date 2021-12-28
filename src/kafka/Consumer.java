package kafka;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import akka.actor.ActorRef;
import messages.QuotesMsg;

 // class used by kafkastreamer Actor to receive data from Kafka topic
public class Consumer {
	private KafkaConsumer<String, String> consumer;
	private boolean flag  = true; // to be used for turning the consumer on/off, could pass boolean from main for easy access
	
	// look into kafka streams for this method
	public void consumerStart(String address, String topic, String consumerID, ActorRef[] receivers) {
		Properties props = new Properties();
        props.setProperty("bootstrap.servers", address);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
        		consumerID); // "console-consumer-58236"
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        while (flag) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) { // decode message here
            	String[] quotes = record.value().split("\\s+");
            	QuotesMsg msg = new QuotesMsg(quotes);
            	for(ActorRef act: receivers) {
            		act.tell(msg, ActorRef.noSender());
            	}
            }
                
        }
	}
	
	public void consumerStop() {
		flag = false;
		consumer.close();
	}
}
