package messages;

import kafka.Consumer;

public class StopStreamingMsg {
	private Consumer consumer;
	
	public StopStreamingMsg(Consumer consumer) {
		this.consumer = consumer;
	}
	// kafka streamer actor should not be able to use this method with the current implementation.. will be considered
	public Consumer getConsumer() {
		return consumer;
	}
}
