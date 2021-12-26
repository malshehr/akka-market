package messages;

import actors.Consumer;

public class StartStreamingMsg {
	private Consumer consumer;
	private String bootstrapAddress;
	private String topic;
	private String consumerID;
	
	public StartStreamingMsg(String address, String topic, String consumerID) {
		this.consumer = new Consumer();
		this.bootstrapAddress = address;
		this.topic = topic;
		this.consumerID = consumerID;
	}
	
	public String getAddress() {
		return bootstrapAddress;
	}
	
	public Consumer getConsumer() {
		return consumer;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public String getConsumerID() {
		return consumerID;
	}
}
