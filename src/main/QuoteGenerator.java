package main;
import java.util.ArrayList;
import java.util.Random;

public class QuoteGenerator {

	private ArrayList<Quote> quotes;
	private Random rdm;
	
	public QuoteGenerator() {
		quotes = new ArrayList<Quote>();
		rdm = new Random();
	}
	
	public ArrayList<Quote> generateQuotes(){
		String company = "Meta";
		float price = rdm.nextFloat() * 10;
		quotes.add(new Quote(company, price));
		company = "Google";
		price = rdm.nextFloat() * 10;
		quotes.add(new Quote(company, price));
		company = "Amazon";
		price = rdm.nextFloat() * 10;
		quotes.add(new Quote(company, price));
		company = "Robinhood";
		price = rdm.nextFloat() * 10;
		quotes.add(new Quote(company, price));
		company = "Stc";
		price = rdm.nextFloat() * 10;
		quotes.add(new Quote(company, price));
		return quotes;
	}
}
