package messages;

import kafka.Producer;

public class ProduceQuotesMsg {
	private Producer producer;
	private String bootstrapAddress;
	private String topic;

	public ProduceQuotesMsg(String address, String topic) {
		this.producer = new Producer();
		this.bootstrapAddress = address;
		this.topic = topic;
	}
	
	public String getAddress() {
		return bootstrapAddress;
	}
	
	public Producer getProducer() {
		return producer;
	}
	
	public String getTopic() {
		return topic;
	}

}
