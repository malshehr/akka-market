package messages;

public class SellMsg {

	
	private String trader;
	private String company;
	private float price;
	private float wallet;
	private float earnings;
	
	public SellMsg(String trader, String company, float price, float wallet, float earnings) {
		this.trader = trader;
		this.company = company;
		this.price = price;
		this.wallet = wallet;
		this.earnings = earnings;
	}
	
	public String getTrader() {
		return trader;
	}
	
	public String getCompany() {
		return company;
	}
	
	public float getPrice() {
		return price;
	}
	
	public float getWallet() {
		return wallet;
	}
	
	public float getEarnings() {
		return earnings;
	}
}
