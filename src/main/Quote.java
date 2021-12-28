package main;

public class Quote {

	
	private String company;
	private float value;
	
	public Quote(String companyName, float price){
		company = companyName;
		value = price;
	}
	
	public String getCompany() {
		return company;
	}
	
	public float getValue() {
		return value;
	}
}
