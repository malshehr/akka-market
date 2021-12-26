package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.StartStreamingMsg;
import messages.StopStreamingMsg;

public class KafkaStreamer extends AbstractActor{
	
	public static Props props() {
		return Props.create(KafkaStreamer.class);
	}

	public Receive createReceive() {
		return receiveBuilder()
				.match(StartStreamingMsg.class, this::startStreaming)
				.match(StopStreamingMsg.class, this::stopStreaming)
				.build();
	}
	public void startStreaming(StartStreamingMsg msg) {
		Consumer consumer = msg.getConsumer();
		String address = msg.getAddress();
		String topic = msg.getTopic();
		String consumerID = msg.getConsumerID();

		consumer.consumerStart(address, topic, consumerID);
	}

	public void stopStreaming(StopStreamingMsg msg) {
		Consumer consumer = msg.getConsumer();
		consumer.consumerStop();
	}
	
	
}
