package messages;

import actors.Consumer;

public class StopStreamingMsg {
	private Consumer consumer;
	private String bootstrapAddress;
	private String topic;
	private String consumerID;
	
	public StopStreamingMsg(Consumer consumer) {
		this.consumer = consumer;
	}
	// kafka streamer actor should not be able to use this method with the current implementation.. will be considered
	public Consumer getConsumer() {
		return consumer;
	}
}
