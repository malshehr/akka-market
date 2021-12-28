package messages;

import java.util.Hashtable;


public class QuotesMsg {
	private Hashtable<String, Float> quotes; // receives information of company -> current quote price
	
	public QuotesMsg(String[] quotes) { // look into kafka streams
		this.quotes = new Hashtable<String, Float>();
		for(int i = 0; i < quotes.length; i+=2) {
			this.quotes.put(quotes[i], Float.valueOf(quotes[i+1]));
		}
	}
	
	public Hashtable<String, Float> getQuotes() {
		return quotes;
	}
}
