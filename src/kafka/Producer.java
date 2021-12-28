package kafka;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import main.Quote;
import main.QuoteGenerator;

public class Producer {
	
	private KafkaProducer<String, String> producer;
	private QuoteGenerator quotesGen = new QuoteGenerator();;
	private boolean flag = true;
	
	public void producerStart(String address, String topic) {
		
		Properties props = new Properties();
		props.put("bootstrap.servers", address);
		props.put("acks", "all");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		producer = new KafkaProducer<>(props);
		 while(flag) {
			 StringBuilder quoteEnc = new StringBuilder();
			 ArrayList<Quote> quotes = quotesGen.generateQuotes();
			 for(int i =  0; i < quotes.size(); i++) {
				 quoteEnc.append(quotes.get(i).getCompany() + " " + quotes.get(i).getValue() + " ");
			 }
		     producer.send(new ProducerRecord<String, String>(topic, new String(quoteEnc)));

		 }
	}
}
