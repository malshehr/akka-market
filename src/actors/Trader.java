package actors;

import java.util.HashMap;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import messages.AssignAuditMsg;
import messages.PurchaseMsg;
import messages.QuotesMsg;
import messages.SellMsg;

public class Trader extends AbstractActor {

	private float wallet = 100f;
	private HashMap<String, Float> quotes;
	private HashMap<String, Integer> ownedQuotes = new HashMap<String, Integer>();
	private HashMap<String, Float> totalCost = new HashMap<String, Float>();
	private int messagesCounter = 0;
	private ActorRef audit;

	public static Props props() {
		return Props.create(Trader.class);
	}

	public Receive createReceive() {
		return receiveBuilder()
				.match(QuotesMsg.class, this::trade)
				.match(AssignAuditMsg.class, this::assignAudit)
				.build();
	}
	
	private void assignAudit(AssignAuditMsg msg) {
		audit = msg.getAudit();
	}

	private void trade(QuotesMsg msg) {
		messagesCounter++;
		quotes = msg.getQuotes();
		for (String company : quotes.keySet()) {
			if (totalCost.get(company) == null) {
				totalCost.put(company, quotes.get(company));
			} else {
				totalCost.put(company, totalCost.get(company) + quotes.get(company));
			}
		}
		calculated();
	}

	private void calculated() {

		// buy if you can afford
		if (wallet > 10f) {
			for (String company : quotes.keySet()) {
				if (wallet > quotes.get(company)) {
					wallet -= quotes.get(company);
					// send message to Audit
					PurchaseMsg msg = new PurchaseMsg(getSelf().path().name(), company, quotes.get(company), wallet);
					audit.tell(msg, ActorRef.noSender());
					if(ownedQuotes.get(company) == null) {
						ownedQuotes.put(company, 1);
					} else {
						ownedQuotes.put(company, ownedQuotes.get(company) + 1);
					}
				}
			}
			// sell
		} else {
			for(String company: quotes.keySet()) {
				// sell with a profit
				if(quotes.get(company) > totalCost.get(company)/messagesCounter || wallet > 1000f) {
					float earnings = quotes.get(company)*ownedQuotes.get(company);
					wallet += earnings;
					SellMsg msg = new SellMsg(getSelf().path().name(), company, quotes.get(company), wallet, earnings);
					audit.tell(msg, ActorRef.noSender());
					System.out.println(this.context().self().path().name() + " sold " + company + " quotes for a total of " 
					+ quotes.get(company)*ownedQuotes.get(company) +  "$ when the average price of a single quote is " + totalCost.get(company)/messagesCounter + "$");
					//System.out.println("Wallet " + wallet);
					ownedQuotes.put(company, 0);
				}
			}
		}

	}
	
	// more traders logic here
	
	
}
