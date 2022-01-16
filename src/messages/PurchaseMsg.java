package messages;

import main.TradingStyle;

public class PurchaseMsg {

	
	private String trader;
	private String company;
	private float price;
	private TradingStyle style;
	
	public PurchaseMsg(String trader, String company, float price, TradingStyle style) {
		this.trader = trader;
		this.company = company;
		this.price = price;
		this.style = style;
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
	
	public TradingStyle getTradingStyle() {
		return style;
	}
}
