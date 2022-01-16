package actors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import main.TradingStyle;
import main.Trend;
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
	private Random rand = new Random();
	private ActorRef audit;
	private HashMap<String, LinkedList<Trend>> trends = new HashMap<String, LinkedList<Trend>>();
	private HashMap<String, Float> prev = new HashMap<String, Float>();
	
	public static Props props() {
		return Props.create(Trader.class);
	}

	public Receive createReceive() {
		return receiveBuilder().match(QuotesMsg.class, this::trade).match(AssignAuditMsg.class, this::assignAudit)
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
				prev.put(company, quotes.get(company));
				LinkedList<Trend> companyTrends = new LinkedList<Trend>();
				if(quotes.get(company) > 5f) {
					companyTrends.add(Trend.UPWARD);
				} else {
					companyTrends.add(Trend.DOWNWARD);
				}
				trends.put(company, companyTrends);
			} else {
				if(trends.get(company).size() == 3) { // move head 
					trends.get(company).set(0, trends.get(company).get(1));
				}
				if(prev.get(company) > quotes.get(company)) {
					trends.get(company).add(Trend.DOWNWARD);
				} else {
					trends.get(company).add(Trend.UPWARD);
				}
				totalCost.put(company, totalCost.get(company) + quotes.get(company));
				prev.put(company, quotes.get(company));
			}
		}
		if(rand.nextFloat() <= 0.5f && messagesCounter >= 3) {
			trend();
		} else if(rand.nextFloat() <= 0.25f) {
			victim();
		} else {
			calculated();
		}
	}

	private void calculated() {

		// buy if you can afford
		if (wallet > 10f) {
			for (String company : quotes.keySet()) {
				if (wallet > quotes.get(company)) {
					wallet -= quotes.get(company);
					// send message to Audit
					PurchaseMsg msg = new PurchaseMsg(getSelf().path().name(), company, quotes.get(company), TradingStyle.CALCULATED);
					audit.tell(msg, ActorRef.noSender());
					if (ownedQuotes.get(company) == null) {
						ownedQuotes.put(company, 1);
					} else {
						ownedQuotes.put(company, ownedQuotes.get(company) + 1);
					}
				}
			}
			// sell
		} else {
			for (String company : quotes.keySet()) {
				// sell with a profit
				if (quotes.get(company) > totalCost.get(company) / messagesCounter || wallet > 1000f && quotes.get(company) > 0) {
					float earnings = quotes.get(company) * ownedQuotes.get(company);
					wallet += earnings;
					SellMsg msg = new SellMsg(getSelf().path().name(), company, quotes.get(company), TradingStyle.CALCULATED, earnings);
					audit.tell(msg, ActorRef.noSender());
					System.out.println(this.context().self().path().name() + " sold " + company
							+ " quotes for a total of " + quotes.get(company) * ownedQuotes.get(company)
							+ "$ when the average price of a single quote is "
							+ totalCost.get(company) / messagesCounter + "$");
					ownedQuotes.put(company, 0);
				}
			}
		}

	}
	
	private void victim() {
		// buy if you can afford
		if (wallet > 10f) {
			for (String company : quotes.keySet()) {
				if (wallet > quotes.get(company)) {
					wallet -= quotes.get(company);
					// send message to Audit
					PurchaseMsg msg = new PurchaseMsg(getSelf().path().name(), company, quotes.get(company), TradingStyle.VICTIM);
					audit.tell(msg, ActorRef.noSender());
					if (ownedQuotes.get(company) == null) {
						ownedQuotes.put(company, 1);
					} else {
						ownedQuotes.put(company, ownedQuotes.get(company) + 1);
					}
				}
			}
			// sell
		} else {
			for (String company : quotes.keySet()) {
				// sell with a loss
				if (quotes.get(company) < totalCost.get(company) / messagesCounter || wallet > 1000f && quotes.get(company) > 0) {
					float earnings = quotes.get(company) * ownedQuotes.get(company);
					wallet += earnings;
					SellMsg msg = new SellMsg(getSelf().path().name(), company, quotes.get(company), TradingStyle.VICTIM, earnings);
					audit.tell(msg, ActorRef.noSender());
					System.out.println(this.context().self().path().name() + " sold " + company
							+ " quotes for a total of " + quotes.get(company) * ownedQuotes.get(company)
							+ "$ when the average price of a single quote is "
							+ totalCost.get(company) / messagesCounter + "$");
					ownedQuotes.put(company, 0);
				}
			}
		}
	}
	
	private void trend() {
		int down = 0;
		float buy; // used for probability of buying
		for (String company: quotes.keySet()) {
			for(Trend t: trends.get(company)) {
				if(t == Trend.DOWNWARD) {
					down++;
				}
			}
			// setting probs for buying/selling
			if(down == 3) {
				buy = 0.75f;
			} else if(down == 2) {
				buy = 0.55f;
			} else if(down == 1) {
				buy = 0.45f;
			} else {
				buy = 0.25f;
			}
			
			if(rand.nextFloat() <= buy) {
				wallet -= quotes.get(company);
				PurchaseMsg msg = new PurchaseMsg(getSelf().path().name(), company, quotes.get(company), TradingStyle.TRENDS);
				audit.tell(msg, ActorRef.noSender());
				if (ownedQuotes.get(company) == null) {
					ownedQuotes.put(company, 1);
				} else {
					ownedQuotes.put(company, ownedQuotes.get(company) + 1);
				}
			} else if(ownedQuotes.get(company) > 0){
				float earnings = quotes.get(company) * ownedQuotes.get(company);
				wallet += earnings;
				SellMsg msg = new SellMsg(getSelf().path().name(), company, quotes.get(company), TradingStyle.TRENDS, earnings);
				audit.tell(msg, ActorRef.noSender());
				ownedQuotes.put(company, 0);
			}
		}
	}

}
