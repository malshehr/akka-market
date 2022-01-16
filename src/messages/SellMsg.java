package messages;

import main.TradingStyle;

public class SellMsg {

	
	private String trader;
	private String company;
	private float price;
	private TradingStyle style;
	private float earnings;
	
	public SellMsg(String trader, String company, float price, TradingStyle style, float earnings) {
		this.trader = trader;
		this.company = company;
		this.price = price;
		this.style = style;
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
	
	public TradingStyle getTradingStyle() {
		return style;
	}
	
	public float getEarnings() {
		return earnings;
	}
}
