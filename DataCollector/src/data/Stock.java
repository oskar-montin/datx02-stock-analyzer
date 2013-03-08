package data;


/**
 * Datatype describing the simplest stock values.
 * 
 * @author oskarnylen
 */

public class Stock implements MarketItem, Comparable<Stock> {

	private String name;
	private String symbol;
	private String business;
	private String stockExchange;
	
	public Stock(String name, String symbol, String stockExchange){
		this.name = name;
		this.symbol = symbol;
		this.business = "default";
		this.stockExchange = stockExchange;
	}
	


	public Stock(String name, String symbol, String business, String stockExchange){
		this.name = name;
		this.symbol = symbol;
		this.business = business;
		this.stockExchange = stockExchange;
	}

	@Override
	public String toDBEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return symbol;
	}
	
	public String getName() {
		return name;
	}

	public String getBusiness() {
		return business;
	}

	public String getStockExchange() {
		return stockExchange;
	}
	
	@Override
	public String toString() {
		return "Stock [name=" + name + ", symbol=" + symbol + ", business="
				+ business + ", stockExchange=" + stockExchange + "]";
	}



	@Override
	public int compareTo(Stock o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
