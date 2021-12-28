package actors;

import java.util.Hashtable;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.QuotesMsg;

public class Trader extends AbstractActor {

	private float wallet = 100f;
	private Hashtable<String, Float> quotes;
	
	public static Props props() {
		return Props.create(Trader.class);
	}
	
	public Receive createReceive() {
		return receiveBuilder()
				.match(QuotesMsg.class, this::trade)
				.build();
	}

	public void trade(QuotesMsg msg) {
		quotes = msg.getQuotes();
		
		System.out.println(self().path().name() + " Received Quotes");

		
	}
}
