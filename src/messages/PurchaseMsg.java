package messages;

public class PurchaseMsg {

	
	private String trader;
	private String company;
	private float price;
	private float wallet;
	
	public PurchaseMsg(String trader, String company, float price, float wallet) {
		this.trader = trader;
		this.company = company;
		this.price = price;
		this.wallet = wallet;
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
}
