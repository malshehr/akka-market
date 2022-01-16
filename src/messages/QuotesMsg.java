package messages;

import java.util.HashMap;


public class QuotesMsg {
	private HashMap<String, Float> quotes; // receives information of company -> current quote price
	
	public QuotesMsg(String[] quotes) {
		this.quotes = new HashMap<String, Float>();
		for(int i = 0; i < quotes.length; i+=2) {
			this.quotes.put(quotes[i], Float.valueOf(quotes[i+1]));
		}
	}
	
	public HashMap<String, Float> getQuotes() {
		return quotes;
	}
}
