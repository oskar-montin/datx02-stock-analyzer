package data;


/**
 * Datatype describing the simplest stock values.
 * 
 * @author oskarnylen
 */

public class Stock implements MarketItem {

	private String name;
	private String symbol;
	private String business;
	
	public Stock(String name, String symbol, String business){
		this.name = name;
		this.symbol = symbol;
		this.business = business;
	}

	@Override
	public String toDBEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSymbol() {
		// TODO Auto-generated method stub
		return null;
	}
}
