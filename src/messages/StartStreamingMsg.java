package messages;

import akka.actor.ActorRef;
import kafka.Consumer;

public class StartStreamingMsg {
	private Consumer consumer;
	private ActorRef[] receivers;
	private String bootstrapAddress;
	private String topic;
	private String consumerID;
	
	public StartStreamingMsg(String address, String topic, String consumerID, ActorRef[] receivers) {
		this.consumer = new Consumer();
		this.receivers = receivers;
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
	
	public ActorRef[] getReceivers() {
		return receivers;
	}
}
