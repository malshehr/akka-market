package messages;

import java.util.Hashtable;

public class QuotesMsg {
	private Hashtable<String, Integer> quotes; // receives information of company -> current quote price
	
	public QuotesMsg() { // figure out how you want to store the key value pairs.. look into kafka streams
		
	}
	
	public Hashtable<String, Integer> getQuotes() {
		return quotes;
	}
}
