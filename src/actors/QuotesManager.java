package actors;

import akka.actor.Props;
import kafka.Producer;
import akka.actor.AbstractActor;
import messages.ProduceQuotesMsg;


public class QuotesManager extends AbstractActor {
	public static Props props() {
		return Props.create(QuotesManager.class);
	}

	public Receive createReceive() {
		return receiveBuilder()
				.match(ProduceQuotesMsg.class, this::produceQuotes)
				.build();
	}
	public void produceQuotes(ProduceQuotesMsg msg) {
		Producer producer = msg.getProducer();
		String address = msg.getAddress();
		String topic = msg.getTopic();

		producer.producerStart(address, topic);
	}

}
